package net.maisikoleni.am2900me.ui;

import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import net.maisikoleni.am2900me.util.AdvBindings;
import net.maisikoleni.am2900me.util.HexIntStringConverter;
import net.maisikoleni.am2900me.util.ProcessingStringConverter;
import net.maisikoleni.am2900me.util.StyleOnChangeAutoReset;

public class RegisterStatusPanel extends BorderPane {

	private final ObservableAm2900Machine m;
	private TableView<RegisterEntry> regTable;
	private TableView<RegisterEntry> otherRegTable;
	private TableView<StatusEntry> statusTable;
	private boolean updateAllActive;
	private BooleanProperty autoUpdate;

	public RegisterStatusPanel(ObservableAm2900Machine machine) {
		this.m = machine;
		configureStatus();
		configureOtherRegTable();
		configureRegisters();
		configureToolbar();
		m.addListener(obs -> {
			if (autoUpdate.get())
				updateAll();
		});
		SplitPane tables = new SplitPane(otherRegTable, statusTable);
		tables.setOrientation(Orientation.VERTICAL);
		tables.setDividerPositions(0.53);
		setCenter(tables);
		setRight(regTable);
	}

	private void configureStatus() {
		TableColumn<StatusEntry, String> names = new TableColumn<>("Statusbit");
		names.setCellFactory(TextFieldTableCell.forTableColumn());
		names.setCellValueFactory(new PropertyValueFactory<>("name"));
		names.setEditable(false);
		names.setReorderable(false);
		names.setSortable(false);

		TableColumn<StatusEntry, Boolean> values = new TableColumn<>("Value");
		values.setCellFactory(
				new StyleOnChangeAutoReset<>(ChoiceBoxTableCell.forTableColumn(Boolean.FALSE, Boolean.TRUE),
						"changed-cell", m, (o, n) -> o != null && n != null));
		values.setCellValueFactory(new PropertyValueFactory<>("value"));
		values.setEditable(true);
		values.setReorderable(false);
		values.setSortable(false);

		ObservableList<StatusEntry> items = FXCollections.observableArrayList();
		items.add(new StatusEntry("µC"));
		items.add(new StatusEntry("µN"));
		items.add(new StatusEntry("µZ"));
		items.add(new StatusEntry("µOVR"));
		items.add(new StatusEntry("MC"));
		items.add(new StatusEntry("MN"));
		items.add(new StatusEntry("MZ"));
		items.add(new StatusEntry("MOVR"));
		statusTable = new TableView<>(items);
		statusTable.getColumns().add(names);
		statusTable.getColumns().add(values);
		statusTable.setEditable(true);
	}

	private void configureOtherRegTable() {
		TableColumn<RegisterEntry, String> names = new TableColumn<>("Register Name");
		names.setCellFactory(TextFieldTableCell.forTableColumn());
		names.setCellValueFactory(new PropertyValueFactory<>("name"));
		names.setEditable(false);
		names.setReorderable(false);
		names.setSortable(false);

		TableColumn<RegisterEntry, Integer> values = new TableColumn<>("Value");
		values.setCellFactory(new StyleOnChangeAutoReset<>(list -> {
			TextFieldTableCell<RegisterEntry, Integer> cell = new TextFieldTableCell<>();
			cell.converterProperty()
					.bind(AdvBindings.map(
							AdvBindings.map(cell.tableRowProperty(), r -> r == null ? null : r.itemProperty()),
							re -> re == null ? null : re.conv));
			return cell;
		}, "changed-cell", m, (o, n) -> o != null && n != null));
		values.setCellValueFactory(new PropertyValueFactory<>("value"));
		values.setEditable(true);
		values.setReorderable(false);
		values.setSortable(false);

		ObservableList<RegisterEntry> items = FXCollections.observableArrayList();
		items.add(new RegisterEntry("PC", m.getPc()::setPc, m.getPc()::getPc, HexIntStringConverter.INT_16));
		items.add(new RegisterEntry("IR", m.getIr()::setInstruction, m.getIr()::getInstruction,
				HexIntStringConverter.INT_16));
		items.add(new RegisterEntry("µPC", m.getAm2910()::setµPC, m.getAm2910()::getµPC, HexIntStringConverter.INT_12));
		items.add(new RegisterEntry("Register/Counter", m.getAm2910()::setRegisterCounter,
				m.getAm2910()::getRegisterCounter, HexIntStringConverter.INT_12));
		items.add(new RegisterEntry("Stack Pointer", m.getAm2910()::setStackPointer, m.getAm2910()::getStackPointer,
				new ProcessingStringConverter<>(i -> Math.max(0, Math.min(5, i)), new IntegerStringConverter())));
		for (int i = 4; i >= 0; i--) {
			final int pos = i;
			items.add(new RegisterEntry("Stack [" + pos + "]", x -> m.getAm2910().setStack(pos, x),
					() -> m.getAm2910().getStack(pos), HexIntStringConverter.INT_12));
		}
		otherRegTable = new TableView<>(items);
		otherRegTable.getColumns().add(names);
		otherRegTable.getColumns().add(values);
		otherRegTable.setEditable(true);
	}

	private void configureToolbar() {
		Button update = new Button("Update all");
		update.setOnAction(e -> updateAll());
		ToggleButton autoUpButton = new ToggleButton("Auto Update");
		autoUpdate = autoUpButton.selectedProperty();
		update.disableProperty().bind(autoUpdate);
		autoUpButton.setSelected(true);
		ToolBar tb = new ToolBar(update, autoUpButton);
		setTop(tb);
	}

	private void configureRegisters() {
		TableColumn<RegisterEntry, String> names = new TableColumn<>("Registers");
		names.setCellFactory(TextFieldTableCell.forTableColumn());
		names.setCellValueFactory(new PropertyValueFactory<>("name"));
		names.setEditable(false);
		names.setReorderable(false);
		names.setSortable(false);

		TableColumn<RegisterEntry, Integer> values = new TableColumn<>("Value");
		values.setCellFactory(
				new StyleOnChangeAutoReset<>(TextFieldTableCell.forTableColumn(HexIntStringConverter.INT_16),
						"changed-cell", m, (o, n) -> o != null && n != null));
		values.setCellValueFactory(new PropertyValueFactory<>("value"));
		values.setEditable(true);
		values.setReorderable(false);
		values.setSortable(false);

		ObservableList<RegisterEntry> items = FXCollections.observableArrayList();
		items.add(new RegisterEntry("Q", m.getAm2904_01x4()::setQ, m.getAm2904_01x4()::getQ));
		for (int i = 0; i < 16; i++) {
			final int addr = i;
			items.add(new RegisterEntry(HexIntStringConverter.INT_4.toString(i),
					n -> m.getAm2904_01x4().setRegisters4bit(addr, n),
					() -> m.getAm2904_01x4().getRegisters4bit(addr)));
		}
		regTable = new TableView<>(items);
		regTable.getColumns().add(names);
		regTable.getColumns().add(values);
		regTable.setEditable(true);
	}

	private void updateAll() {
		updateAllActive = true;
		for (RegisterEntry re : regTable.getItems()) {
			re.update();
		}
		for (RegisterEntry re : otherRegTable.getItems()) {
			re.update();
		}
		for (StatusEntry re : statusTable.getItems()) {
			re.update();
		}
		updateAllActive = false;
	}

	public class RegisterEntry {
		final ReadOnlyStringProperty name;
		final IntegerProperty value;
		final ObjectProperty<StringConverter<Integer>> conv;
		private final IntSupplier getter;

		RegisterEntry(String name, IntConsumer setter, IntSupplier getter) {
			this(name, setter, getter, null);
		}

		RegisterEntry(String name, IntConsumer setter, IntSupplier getter, StringConverter<Integer> conv) {
			this.name = new ReadOnlyStringWrapper(name);
			this.conv = new SimpleObjectProperty<>(conv);
			this.getter = getter;
			this.value = new SimpleIntegerProperty(getter.getAsInt());
			this.value.addListener(new OnUserChange<>(n -> setter.accept(n.intValue())));
		}

		final void update() {
			value.set(getter.getAsInt());
		}

		public final ReadOnlyStringProperty nameProperty() {
			return name;
		}

		public final IntegerProperty valueProperty() {
			return value;
		}
	}

	public class StatusEntry {
		final ReadOnlyStringProperty name;
		final BooleanProperty value;

		@SuppressWarnings("synthetic-access")
		StatusEntry(String name) {
			this.name = new ReadOnlyStringWrapper(name);
			this.value = new SimpleBooleanProperty(m.getAm2904_01x4().isStatusSet(name));
			this.value.addListener(new OnUserChange<>(n -> m.getAm2904_01x4().setStatus(name, n)));
		}

		@SuppressWarnings("synthetic-access")
		final void update() {
			value.set(m.getAm2904_01x4().isStatusSet(name.get()));
		}

		public final ReadOnlyStringProperty nameProperty() {
			return name;
		}

		public final BooleanProperty valueProperty() {
			return value;
		}
	}

	private class OnUserChange<T> implements ChangeListener<T> {
		private final Consumer<T> c;

		OnUserChange(Consumer<T> c) {
			this.c = c;
		}

		@SuppressWarnings("synthetic-access")
		@Override
		public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
			if (!RegisterStatusPanel.this.updateAllActive)
				c.accept(newValue);
		}
	}
}
