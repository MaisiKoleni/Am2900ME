package net.maisikoleni.am2900me.ui;

import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import net.maisikoleni.am2900me.logic.MappingPROM;
import net.maisikoleni.am2900me.util.HexIntStringConverter;
import net.maisikoleni.am2900me.util.IOUtil;

/**
 * Panel for viewing and programming the {@link MappingPROM}.
 *
 * @author Christian Femers
 */
public class MappingPromPanel extends BorderPane {

	private TableView<MappingEntry> addrTable;
	private ObservableList<MappingEntry> entries;
	private final MappingPROM mprom;

	/**
	 * Creates a new Mapping PROM Panel connected to the given {@link MappingPROM},
	 * meaning that the PROM gets updated/'programmed' when the addresses in the
	 * table are changed
	 *
	 * @author Christian Femers
	 */
	public MappingPromPanel(MappingPROM mprom) {
		this.mprom = mprom;
		entries = FXCollections.observableArrayList();
		for (int i = 0x00; i < mprom.size(); i++) {
			entries.add(new MappingEntry(i));
		}
		addrTable = new TableView<>(entries);
		configureTableView();
		setCenter(addrTable);
		configureToolbar();
	}

	private void configureToolbar() {
		Button loadFile = new Button("Load from File");
		loadFile.setOnAction(e -> IOUtil.readLines(this, this::readCSV));
		Button saveFile = new Button("Save to File");
		saveFile.setOnAction(e -> IOUtil.writeLines(this, this::toCSV));
		ToolBar tb = new ToolBar(loadFile, saveFile);
		setTop(tb);
	}

	private void configureTableView() {
		TableColumn<MappingEntry, Integer> opCodes = new TableColumn<>("OP-Code");
		opCodes.setCellFactory(TextFieldTableCell.forTableColumn(HexIntStringConverter.INT_8));
		opCodes.setCellValueFactory(new PropertyValueFactory<>(MappingEntry.PROP_OP_CODE));
		opCodes.setEditable(false);
		opCodes.setReorderable(false);

		TableColumn<MappingEntry, Integer> mapAddresses = new TableColumn<>("Mapping Address");
		mapAddresses.setCellFactory(TextFieldTableCell.forTableColumn(HexIntStringConverter.INT_12));
		mapAddresses.setCellValueFactory(new PropertyValueFactory<>(MappingEntry.PROP_ADDRESS));
		mapAddresses.setEditable(true);
		mapAddresses.setReorderable(false);

		TableColumn<MappingEntry, String> comments = new TableColumn<>("Comment");
		comments.setCellFactory(TextFieldTableCell.forTableColumn());
		comments.setCellValueFactory(new PropertyValueFactory<>(MappingEntry.PROP_COMMENT));
		comments.setPrefWidth(200);
		comments.setEditable(true);
		comments.setReorderable(false);

		addrTable.getColumns().add(opCodes);
		addrTable.getColumns().add(mapAddresses);
		addrTable.getColumns().add(comments);
		addrTable.setEditable(true);
	}

	private final Iterable<String> toCSV() {
		ArrayList<String> lines = new ArrayList<>();
		for (MappingEntry me : entries) {
			lines.add(me.toString());
		}
		return lines;
	}

	private final void readCSV(Iterable<String> lines) {
		for (String line : lines) {
			MappingEntry newME = parseME(line);
			entries.set(newME.opCode.get(), newME);
		}
	}

	public MappingEntry parseME(String s) {
		String[] parts = s.split(",", 3);
		MappingEntry me = new MappingEntry(Integer.decode(parts[0]));
		me.mappingAddr.set(Integer.decode(parts[1]));
		me.comment.set(parts[2]);
		return me;
	}

	public class MappingEntry implements Comparable<MappingEntry> {
		static final String PROP_OP_CODE = "opCode";
		static final String PROP_ADDRESS = "mappingAddr";
		static final String PROP_COMMENT = "comment";

		final ReadOnlyIntegerProperty opCode;
		final IntegerProperty mappingAddr;
		final StringProperty comment;

		MappingEntry(int opCode) {
			this.opCode = new ReadOnlyIntegerWrapper(this, PROP_OP_CODE, opCode);
			this.mappingAddr = new SimpleIntegerProperty(this, PROP_ADDRESS, opCode << 4);
			this.mappingAddr.addListener((obs, o, n) -> MappingPromPanel.this.mprom.set(opCode, n.intValue()));
			this.comment = new SimpleStringProperty(this, PROP_COMMENT, "");
			MappingPromPanel.this.mprom.set(opCode, mappingAddr.get());
		}

		@Override
		public String toString() {
			return HexIntStringConverter.INT_8.toString(opCode.get()) + ","
					+ HexIntStringConverter.INT_12.toString(mappingAddr.get()) + "," + comment.get();
		}

		public final ReadOnlyIntegerProperty opCodeProperty() {
			return opCode;
		}

		public final IntegerProperty mappingAddrProperty() {
			return mappingAddr;
		}

		public final StringProperty commentProperty() {
			return comment;
		}

		@Override
		public int compareTo(MappingEntry o) {
			return opCode.intValue() - o.opCode.get();
		}
	}
}
