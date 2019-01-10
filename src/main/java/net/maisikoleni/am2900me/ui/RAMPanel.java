package net.maisikoleni.am2900me.ui;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import net.maisikoleni.am2900me.logic.MachineRAM;
import net.maisikoleni.am2900me.logic.MappingPROM;
import net.maisikoleni.am2900me.util.HexIntStringConverter;
import net.maisikoleni.am2900me.util.IOUtil;

/**
 * Panel for viewing and modifying the {@link MachineRAM}. (live)
 *
 * @author MaisiKoleni
 */
public class RAMPanel extends BorderPane {
	private final TableView<RAM16CellRow> currentPage;
	private ComboBox<Integer> pageChooser;
	private ToolBar actions;
	private final VBox pageNotUsed;
	private final Button allocPage;
	private final ObservableList<RAM16CellRow> entries;
	private IntegerProperty page;
	private final MachineRAM ram;

	/**
	 * Creates a new Mapping PROM Panel connected to the given {@link MappingPROM},
	 * meaning that the PROM gets updated/'programmed' when the addresses in the
	 * table are changed
	 * 
	 * @author MaisiKoleni
	 */
	public RAMPanel(MachineRAM ram) {
		this.ram = ram;
		entries = FXCollections.observableArrayList();
		currentPage = new TableView<>(entries);
		allocPage = new Button("Allocate the page to edit it");
		pageNotUsed = new VBox(new Label("RAM page currently not used"), allocPage);
		pageNotUsed.setSpacing(8);
		pageNotUsed.setStyle("-fx-alignment:CENTER;");
		configureToolBar();
		configureTableView();
		updatePage(false);
		setCenter(new StackPane(currentPage, pageNotUsed));
	}

	private void configureToolBar() {
		Label pageLabel = new Label("RAM page:");
		pageChooser = new ComboBox<>(FXCollections
				.observableArrayList(IntStream.range(0, ram.pageCount()).boxed().collect(Collectors.toList())));
		pageChooser.setValue(0);
		page = IntegerProperty.integerProperty(pageChooser.valueProperty());
		Button update = new Button("Update page values");
		update.setOnAction(e -> updatePage(false));
		Button loadFile = new Button("Load from File");
		loadFile.setOnAction(e -> IOUtil.readLines(this, this::readCSV));
		Button saveFile = new Button("Save to File");
		saveFile.setOnAction(e -> IOUtil.writeLines(this, this::toCSV));
		actions = new ToolBar(pageLabel, pageChooser, update, loadFile, saveFile);
		setupListeners();
		setTop(actions);
	}

	private void configureTableView() {
		TableColumn<RAM16CellRow, Integer> start = new TableColumn<>("Offset");
		start.setCellFactory(TextFieldTableCell.forTableColumn(HexIntStringConverter.INT_16));
		start.setCellValueFactory(new PropertyValueFactory<>("offset"));
		start.setEditable(false);
		start.setReorderable(false);
		start.setSortable(false);
		currentPage.getColumns().add(start);

		for (int i = 0; i < 16; i++) {
			String hex = String.format("0x%X", i);
			TableColumn<RAM16CellRow, Integer> col = new TableColumn<>(hex);
			col.setCellFactory(TextFieldTableCell.forTableColumn(HexIntStringConverter.INT_16));
			col.setCellValueFactory(new PropertyValueFactory<>("column" + hex));
			col.setReorderable(false);
			col.setEditable(true);
			col.setSortable(false);
			col.setPrefWidth(60);
			col.setResizable(false);
			currentPage.getColumns().add(col);
		}

		for (int i = 0; i < ram.cellCount(); i += 16) {
			entries.add(new RAM16CellRow(i));
		}
		currentPage.setEditable(true);
	}

	private void setupListeners() {
		allocPage.setOnAction((e) -> {
			updatePage(true);
		});
		page.addListener((obs, o, n) -> updatePage(false));
	}

	private void updatePage(boolean doAlloc) {
		int p = page.get();
		boolean inUse = ram.isPageInUse(p);
		if (!inUse && doAlloc) {
			ram.allocatePage(p);
			inUse = !inUse;
		}
		if (inUse)
			for (RAM16CellRow row : entries)
				row.setPage(p);
		pageNotUsed.setVisible(!inUse);
		currentPage.setVisible(inUse);
	}

	private final Iterable<String> toCSV() {
		ArrayList<String> lines = new ArrayList<>();
		int pageSize = ram.cellCount();
		for (int i = 0; i < ram.pageCount(); i++) {
			if (!ram.isPageInUse(i))
				continue;
			for (int k = 0; k < pageSize; k += 16) {
				int offset = i * pageSize + k;
				StringBuilder sb = new StringBuilder();
				sb.append(HexIntStringConverter.INT_16.toString(offset) + ",");
				for (int j = 0; j < 15; j++) {
					sb.append(HexIntStringConverter.INT_16.toString((int) ram.get(offset + j)) + ",");
				}
				sb.append(HexIntStringConverter.INT_16.toString((int) ram.get(offset + 15)));
				lines.add(sb.toString());
			}
		}
		return lines;
	}

	private final void readCSV(Iterable<String> lines) {
		for (String line : lines) {
			String[] parts = line.split(",");
			int offset = Integer.decode(parts[0]);
			for (int i = 0; i < 16; i++) {
				ram.set(offset + i, Integer.decode(parts[i + 1]));
			}
		}
		updatePage(false);
	}

	private int getValue(int inPageAddress) {
		return ram.get(page.get() * ram.cellCount() + inPageAddress);
	}

	private void setValue(int inPageAddress, int value) {
		ram.set(page.get() * ram.cellCount() + inPageAddress, value);
	}

	@SuppressWarnings("synthetic-access")
	public class RAM16CellRow {
		private final IntegerProperty[] columns = new SimpleIntegerProperty[16];
		private final IntegerProperty offset;
		private final int inPageOffset;
		private boolean isUserChange = true;

		public RAM16CellRow(int offset) {
			this.inPageOffset = offset;
			this.offset = new SimpleIntegerProperty(offset);
			for (int i = 0; i < columns.length; i++) {
				final int addr = inPageOffset + i;
				columns[i] = new SimpleIntegerProperty();
				columns[i].addListener((obs, o, n) -> {
					if (isUserChange)
						setValue(addr, n.intValue());
				});
			}
		}

		void setPage(int page) {
			offset.set(inPageOffset + ram.cellCount() * page);
			isUserChange = false;
			for (int i = 0; i < 16; i++) {
				columns[i].set(getValue(inPageOffset + i));
			}
			isUserChange = true;
		}

		public final ReadOnlyIntegerProperty offsetProperty() {
			return offset;
		}

		public final IntegerProperty column0x0Property() {
			return columns[0x0];
		}

		public final IntegerProperty column0x1Property() {
			return columns[0x1];
		}

		public final IntegerProperty column0x2Property() {
			return columns[0x2];
		}

		public final IntegerProperty column0x3Property() {
			return columns[0x3];
		}

		public final IntegerProperty column0x4Property() {
			return columns[0x4];
		}

		public final IntegerProperty column0x5Property() {
			return columns[0x5];
		}

		public final IntegerProperty column0x6Property() {
			return columns[0x6];
		}

		public final IntegerProperty column0x7Property() {
			return columns[0x7];
		}

		public final IntegerProperty column0x8Property() {
			return columns[0x8];
		}

		public final IntegerProperty column0x9Property() {
			return columns[0x9];
		}

		public final IntegerProperty column0xAProperty() {
			return columns[0xA];
		}

		public final IntegerProperty column0xBProperty() {
			return columns[0xB];
		}

		public final IntegerProperty column0xCProperty() {
			return columns[0xC];
		}

		public final IntegerProperty column0xDProperty() {
			return columns[0xD];
		}

		public final IntegerProperty column0xEProperty() {
			return columns[0xE];
		}

		public final IntegerProperty column0xFProperty() {
			return columns[0xF];
		}
	}
}
