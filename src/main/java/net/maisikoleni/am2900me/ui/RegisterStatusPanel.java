package net.maisikoleni.am2900me.ui;

import java.util.function.Consumer;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import net.maisikoleni.am2900me.logic.Am2900Machine;
import net.maisikoleni.am2900me.util.HexIntStringConverter;

public class RegisterStatusPanel extends BorderPane {

	private final Am2900Machine machine;
	private TableView<RegisterEntry> regTable;
	private boolean updateAllActive;

	public RegisterStatusPanel(Am2900Machine machine) {
		this.machine = machine;
		configureStatus();
		configureRegisters();
		configureToolbar();
	}

	private void configureStatus() {
		// TODO
	}

	private void configureToolbar() {
		Button update = new Button("Update all");
		update.setOnAction(e -> updateAll());
		ToolBar tb = new ToolBar(update);
		setTop(tb);
	}

	private void configureRegisters() {
		TableColumn<RegisterEntry, Integer> addrs = new TableColumn<>("Address");
		addrs.setCellFactory(TextFieldTableCell.forTableColumn(HexIntStringConverter.INT_4));
		addrs.setCellValueFactory(new PropertyValueFactory<>("addr"));
		addrs.setEditable(false);
		addrs.setReorderable(false);

		TableColumn<RegisterEntry, Integer> values = new TableColumn<>("Value");
		values.setCellFactory(TextFieldTableCell.forTableColumn(HexIntStringConverter.INT_16));
		values.setCellValueFactory(new PropertyValueFactory<>("value"));
		values.setEditable(true);
		values.setReorderable(false);

		ObservableList<RegisterEntry> items = FXCollections.observableArrayList();
		for (int i = 0; i < 16; i++) {
			items.add(new RegisterEntry(i));
		}
		regTable = new TableView<>(items);
		regTable.getColumns().add(addrs);
		regTable.getColumns().add(values);
		regTable.setEditable(true);
		setCenter(regTable);
	}

	private void updateAll() {
		updateAllActive = true;
		for (int i = 0; i < 16; i++) {
			regTable.getItems().get(i).value.set(machine.getAm2904_01x4().getRegisters4bit(i));
		}
		updateAllActive = false;
	}

	public class RegisterEntry {
		final ReadOnlyIntegerProperty addr;
		final IntegerProperty value;

		@SuppressWarnings("synthetic-access")
		RegisterEntry(int addr) {
			this.addr = new ReadOnlyIntegerWrapper(addr);
			this.value = new SimpleIntegerProperty(machine.getAm2904_01x4().getRegisters4bit(addr));
			this.value.addListener(
					new OnUserChange<>((n) -> machine.getAm2904_01x4().setRegisters4bit(addr, n.intValue())));
		}

		public final ReadOnlyIntegerProperty addrProperty() {
			return addr;
		}

		public final IntegerProperty valueProperty() {
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
