package net.maisikoleni.am2900me.ui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import net.maisikoleni.am2900me.logic.MappingPROM;

/**
 * Panel for viewing and programming the {@link MappingPROM}.
 *
 * @author MaisiKoleni
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
	 * @author MaisiKoleni
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
		comments.setEditable(true);
		comments.setReorderable(false);

//		addrTable.getSortOrder().add(opCodes);
		addrTable.getColumns().add(opCodes);
		addrTable.getColumns().add(mapAddresses);
		addrTable.getColumns().add(comments);
		addrTable.setEditable(true);
	}

	@SuppressWarnings("javadoc")
	public class MappingEntry implements Comparable<MappingEntry> {
		static final String PROP_OP_CODE = "opCode";
		static final String PROP_ADDRESS = "mappingAddr";
		static final String PROP_COMMENT = "comment";

		private final IntegerProperty opCode;
		private final IntegerProperty mappingAddr;
		private final StringProperty comment;

		@SuppressWarnings("synthetic-access")
		MappingEntry(int opCode) {
			this.opCode = new SimpleIntegerProperty(this, PROP_OP_CODE, opCode);
			this.mappingAddr = new SimpleIntegerProperty(this, PROP_ADDRESS, opCode << 4);
			this.mappingAddr.addListener((obs, o, n) -> MappingPromPanel.this.mprom.set(opCode, n.intValue()));
			this.comment = new SimpleStringProperty(this, PROP_COMMENT, "");
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
