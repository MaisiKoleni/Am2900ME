package net.maisikoleni.am2900me.ui;

import java.util.ArrayList;
import java.util.Optional;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import net.maisikoleni.am2900me.logic.MicroprogramMemory;
import net.maisikoleni.am2900me.logic.microinstr.ASEL;
import net.maisikoleni.am2900me.logic.microinstr.Am2901_Dest;
import net.maisikoleni.am2900me.logic.microinstr.Am2901_Func;
import net.maisikoleni.am2900me.logic.microinstr.Am2901_Src;
import net.maisikoleni.am2900me.logic.microinstr.Am2904_Carry;
import net.maisikoleni.am2900me.logic.microinstr.Am2904_Inst;
import net.maisikoleni.am2900me.logic.microinstr.Am2904_Shift;
import net.maisikoleni.am2900me.logic.microinstr.Am2910_Inst;
import net.maisikoleni.am2900me.logic.microinstr.BAR;
import net.maisikoleni.am2900me.logic.microinstr.BSEL;
import net.maisikoleni.am2900me.logic.microinstr.IE;
import net.maisikoleni.am2900me.logic.microinstr.Interrupt;
import net.maisikoleni.am2900me.logic.microinstr.KMUX;
import net.maisikoleni.am2900me.logic.microinstr.Konst;
import net.maisikoleni.am2900me.logic.microinstr.MicroInstruction;
import net.maisikoleni.am2900me.logic.microinstr.RA_ADDR;
import net.maisikoleni.am2900me.logic.microinstr.RB_ADDR;
import net.maisikoleni.am2900me.logic.microinstr._ABUS;
import net.maisikoleni.am2900me.logic.microinstr._BZ_EA;
import net.maisikoleni.am2900me.logic.microinstr._BZ_ED;
import net.maisikoleni.am2900me.logic.microinstr._BZ_INC;
import net.maisikoleni.am2900me.logic.microinstr._BZ_LD;
import net.maisikoleni.am2900me.logic.microinstr._CCEN;
import net.maisikoleni.am2900me.logic.microinstr._CE_M;
import net.maisikoleni.am2900me.logic.microinstr._CE_µ;
import net.maisikoleni.am2900me.logic.microinstr._DBUS;
import net.maisikoleni.am2900me.logic.microinstr._IR_LD;
import net.maisikoleni.am2900me.logic.microinstr._MWE;
import net.maisikoleni.am2900me.logic.microinstr.µIField;
import net.maisikoleni.am2900me.util.AdvBindings;
import net.maisikoleni.am2900me.util.HexIntStringConverter;
import net.maisikoleni.am2900me.util.IOUtil;
import net.maisikoleni.am2900me.util.NBitsUInt;
import net.maisikoleni.am2900me.util.StyleClassProperty;
import net.maisikoleni.am2900me.util.UniversalHexIntStringConverter;

/**
 * Panel for viewing and programming the {@link MicroprogramMemory}.
 *
 * @author MaisiKoleni
 */
public class MicroInstrPanel extends BorderPane {

	private final TableView<MicroInstrItem> miTable;
	private final ObservableList<MicroInstrItem> mis;
	private final ObservableAm2900Machine m;
	private final MicroprogramMemory mpm;
	private final IntegerProperty currentMI;

	/**
	 * Creates a new microprogram memory for programming the microinstructions in
	 * the machine using the given {@link MicroprogramMemory}.
	 * 
	 * @author MaisiKoleni
	 */
	public MicroInstrPanel(ObservableAm2900Machine m) {
		this.m = m;
		this.mpm = m.getMpm();
		this.currentMI = new SimpleIntegerProperty(-1);
		this.miTable = new TableView<>();
		configureTableView();
		mis = FXCollections.observableArrayList();
		for (int i = 0; i < mpm.size(); i++) {
			MicroInstruction mi = MicroInstruction.DEFAULT;
			mpm.setInstruction(i, mi);
			mis.add(new MicroInstrItem(mi, i));
		}
		miTable.setItems(mis);
		setCenter(miTable);
		configureToolbar();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void configureTableView() {
		miTable.setRowFactory(tv -> {
			StyledTableRow<MicroInstrItem> row = new StyledTableRow<>();
			BooleanBinding isDefault = AdvBindings.map(row.itemProperty(), mii -> mii == null ? null : mii.mi)
					.isEqualTo(MicroInstruction.DEFAULT);
			row.styleClassPropertyFor("default-mi").bind(isDefault);
			BooleanBinding executed = row.indexProperty().isEqualTo(currentMI);
			row.styleClassPropertyFor("executed-mi").bind(executed);
			return row;
		});
		TableColumn<MicroInstrItem, Integer> addrCol = new TableColumn<>("Address");
		addrCol.setCellValueFactory(new PropertyValueFactory<>("address"));
		addrCol.setCellFactory(TextFieldTableCell.forTableColumn(HexIntStringConverter.INT_12));
		addrCol.setReorderable(false);
		addrCol.setEditable(false);
		addrCol.setSortable(false);
		miTable.getColumns().add(addrCol);
		for (int i = MicroInstruction.FIELD_NAMES.size() - 1; i >= 0; i--) {
			µIField f = MicroInstruction.FIELD_DEFAULTS.get(i);
			TableColumn<MicroInstrItem, ?> col;
			if (f instanceof Enum<?>)
				col = generateColumnFor((Enum) f); // more elegant enum handling?
			else
				col = generateColumnFor((NBitsUInt) f);
			col.setCellValueFactory(new PropertyValueFactory<>(MicroInstruction.FIELD_NAMES.get(i)));
			col.setReorderable(false);
			col.setEditable(true);
			col.setSortable(false);
			miTable.getColumns().add(col);
		}
		miTable.setEditable(true);
	}

	private void configureToolbar() {
		Button execNext = new Button();
		execNext.textProperty()
				.bind(Bindings.when(currentMI.isEqualTo(-1)).then("Startup Machine").otherwise("Execute Next"));
		execNext.setOnAction(e -> executeNextN(1));
		Button execNextN = new Button();
		execNextN.textProperty().bind(Bindings.when(currentMI.isEqualTo(-1)).then("Execute Next N (inkl. Startup)")
				.otherwise("Execute Next N"));
		execNextN.setOnAction(e -> {
			TextInputDialog t = new TextInputDialog("20");
			t.initOwner(this.getScene().getWindow());
			t.setTitle("Execute Next N");
			t.setHeaderText("Please enter the number of microinstruction cycles you want to execute");
			t.setContentText("positive integer (expect 5 mio. to take ~ 1 sec.)");
			Optional<String> input = t.showAndWait();
			if (!input.isPresent() || input.get().trim().isEmpty())
				return;
			try {
				executeNextN(Integer.decode(input.get()));
			} catch (@SuppressWarnings("unused") NumberFormatException ex) {
				Alert a = new Alert(AlertType.ERROR, "The entered string is not a valid integer", ButtonType.OK);
				a.initOwner(this.getScene().getWindow());
				a.show();
			}
		});
		Button loadFile = new Button("Load from File");
		loadFile.setOnAction(e -> IOUtil.readLines(this, this::readCSV));
		Button saveFile = new Button("Save to File");
		saveFile.setOnAction(e -> IOUtil.writeLines(this, this::toCSV));
		Button reset = new Button("Reset Machine State");
		reset.setOnAction(e -> {
			m.reset();
			currentMI.set(-1);
		});
		ToolBar tb = new ToolBar(execNext, execNextN, loadFile, saveFile, reset);
		setTop(tb);
	}

	private void executeNextN(int n) {
		try {
			m.executeNextN(n);
		} catch (Exception ex) {
			Alert a = new Alert(AlertType.ERROR, "An error occured during execution:\n" + ex, ButtonType.CLOSE);
			a.show();
		}
		currentMI.set(m.getCurrentMicroInstruction());
	}

	@SuppressWarnings("unchecked")
	static <T extends NBitsUInt> TableColumn<MicroInstrItem, T> generateColumnFor(T defaultVal) {
		TableColumn<MicroInstrItem, T> column = new TableColumn<>(defaultVal.getClass().getSimpleName());
		column.setCellFactory(list -> {
			TableCell<MicroInstrItem, T> cell = new TextFieldTableCell<>(
					new UniversalHexIntStringConverter<T>(t -> t.value, i -> (T) defaultVal.valueOf(i),
							HexIntStringConverter.forNibbles(defaultVal.bits / 4)));
			new StyleClassProperty(cell.getStyleClass(), "changed-mi")
					.bind(cell.itemProperty().isNotEqualTo(defaultVal));
			return cell;
		});
		return column;
	}

	static <T extends Enum<T>> TableColumn<MicroInstrItem, ?> generateColumnFor(T defaultVal) {
		TableColumn<MicroInstrItem, T> column = new TableColumn<>(defaultVal.getClass().getSimpleName());
		T[] values = defaultVal.getDeclaringClass().getEnumConstants();
		column.setCellFactory(list -> {
			TableCell<MicroInstrItem, T> cell = new ChoiceBoxTableCell<>(values);
			new StyleClassProperty(cell.getStyleClass(), "changed-mi")
					.bind(cell.itemProperty().isNotEqualTo(defaultVal));
			return cell;
		});
		return column;
	}

	private final Iterable<String> toCSV() {
		ArrayList<String> lines = new ArrayList<>();
		for (MicroInstrItem mii : mis) {
			lines.add(mii.toString());
		}
		return lines;
	}

	private final void readCSV(Iterable<String> lines) {
		for (String line : lines) {
			MicroInstrItem newMII = parseMII(line);
			mis.set(newMII.address.get(), newMII);
		}
	}

	public MicroInstrItem parseMII(String s) {
		MicroInstruction newMI = MicroInstruction.DEFAULT;
		String[] parts = s.split(",");
		newMI = newMI.withMwe(_MWE.valueOf(parts[1]));
		newMI = newMI.withIr_ld(_IR_LD.valueOf(parts[2]));
		newMI = newMI.withBz_ea(_BZ_EA.valueOf(parts[3]));
		newMI = newMI.withBz_inc(_BZ_INC.valueOf(parts[4]));
		newMI = newMI.withBz_ed(_BZ_ED.valueOf(parts[5]));
		newMI = newMI.withBz_ld(_BZ_LD.valueOf(parts[6]));
		newMI = newMI.withBar(new BAR(Integer.decode(parts[7])));
		newMI = newMI.withAm2910_Inst(Am2910_Inst.valueOf(parts[8]));
		newMI = newMI.withCcen(_CCEN.valueOf(parts[9]));
		newMI = newMI.withAm2904_Inst(Am2904_Inst.valueOf(parts[10]));
		newMI = newMI.withCe_m(_CE_M.valueOf(parts[11]));
		newMI = newMI.withCe_µ(_CE_µ.valueOf(parts[12]));
		newMI = newMI.withAm2904_Shift(new Am2904_Shift(Integer.decode(parts[13])));
		newMI = newMI.withAm2904_Carry(Am2904_Carry.valueOf(parts[14]));
		newMI = newMI.withDbus(_DBUS.valueOf(parts[15]));
		newMI = newMI.withAbus(_ABUS.valueOf(parts[16]));
		newMI = newMI.withBsel(BSEL.valueOf(parts[17]));
		newMI = newMI.withRb_addr(new RB_ADDR(Integer.decode(parts[18])));
		newMI = newMI.withAsel(ASEL.valueOf(parts[19]));
		newMI = newMI.withRa_addr(new RA_ADDR(Integer.decode(parts[20])));
		newMI = newMI.withAm2901_Dest(Am2901_Dest.valueOf(parts[21]));
		newMI = newMI.withAm2901_Func(Am2901_Func.valueOf(parts[22]));
		newMI = newMI.withAm2901_Src(Am2901_Src.valueOf(parts[23]));
		newMI = newMI.withK(new Konst(Integer.decode(parts[24])));
		newMI = newMI.withKmux(KMUX.valueOf(parts[25]));
		newMI = newMI.withInterrupt(new Interrupt(Integer.decode(parts[26])));
		newMI = newMI.withIe(IE.valueOf(parts[27]));
		return new MicroInstrItem(newMI, Integer.decode(parts[0]));
	}

	public class MicroInstrItem {
		private final ObjectProperty<_ABUS> abus;
		private final ObjectProperty<Am2901_Dest> am2901_Dest;
		private final ObjectProperty<Am2901_Func> am2901_Func;
		private final ObjectProperty<Am2901_Src> am2901_Src;
		private final ObjectProperty<Am2904_Carry> am2904_Carry;
		private final ObjectProperty<Am2904_Inst> am2904_Inst;
		private final ObjectProperty<Am2904_Shift> am2904_Shift;
		private final ObjectProperty<Am2910_Inst> am2910_Inst;
		private final ObjectProperty<ASEL> asel;
		private final ObjectProperty<BAR> bar;
		private final ObjectProperty<BSEL> bsel;
		private final ObjectProperty<_BZ_EA> bz_ea;
		private final ObjectProperty<_BZ_ED> bz_ed;
		private final ObjectProperty<_BZ_INC> bz_inc;
		private final ObjectProperty<_BZ_LD> bz_ld;
		private final ObjectProperty<_CCEN> ccen;
		private final ObjectProperty<_CE_M> ce_m;
		private final ObjectProperty<_CE_µ> ce_µ;
		private final ObjectProperty<_DBUS> dbus;
		private final ObjectProperty<IE> ie;
		private final ObjectProperty<Interrupt> interrupt;
		private final ObjectProperty<_IR_LD> ir_ld;
		private final ObjectProperty<Konst> k;
		private final ObjectProperty<KMUX> kmux;
		private final ObjectProperty<_MWE> mwe;
		private final ObjectProperty<RA_ADDR> ra_addr;
		private final ObjectProperty<RB_ADDR> rb_addr;

		final ObjectProperty<MicroInstruction> mi;
		final ReadOnlyIntegerProperty address;

		@SuppressWarnings("synthetic-access")
		public MicroInstrItem(MicroInstruction miInput, int address) {
			this.mi = new SimpleObjectProperty<>(miInput);
			this.address = new ReadOnlyIntegerWrapper(address);
			this.mwe = new SimpleObjectProperty<>(mi.get().mwe);
			this.ir_ld = new SimpleObjectProperty<>(mi.get().ir_ld);
			this.bz_ea = new SimpleObjectProperty<>(mi.get().bz_ea);
			this.bz_inc = new SimpleObjectProperty<>(mi.get().bz_inc);
			this.bz_ed = new SimpleObjectProperty<>(mi.get().bz_ed);
			this.bz_ld = new SimpleObjectProperty<>(mi.get().bz_ld);
			this.bar = new SimpleObjectProperty<>(mi.get().bar);
			this.am2910_Inst = new SimpleObjectProperty<>(mi.get().am2910_Inst);
			this.ccen = new SimpleObjectProperty<>(mi.get().ccen);
			this.am2904_Inst = new SimpleObjectProperty<>(mi.get().am2904_Inst);
			this.ce_m = new SimpleObjectProperty<>(mi.get().ce_m);
			this.ce_µ = new SimpleObjectProperty<>(mi.get().ce_µ);
			this.am2904_Shift = new SimpleObjectProperty<>(mi.get().am2904_Shift);
			this.am2904_Carry = new SimpleObjectProperty<>(mi.get().am2904_Carry);
			this.dbus = new SimpleObjectProperty<>(mi.get().dbus);
			this.abus = new SimpleObjectProperty<>(mi.get().abus);
			this.bsel = new SimpleObjectProperty<>(mi.get().bsel);
			this.rb_addr = new SimpleObjectProperty<>(mi.get().rb_addr);
			this.asel = new SimpleObjectProperty<>(mi.get().asel);
			this.ra_addr = new SimpleObjectProperty<>(mi.get().ra_addr);
			this.am2901_Dest = new SimpleObjectProperty<>(mi.get().am2901_Dest);
			this.am2901_Func = new SimpleObjectProperty<>(mi.get().am2901_Func);
			this.am2901_Src = new SimpleObjectProperty<>(mi.get().am2901_Src);
			this.k = new SimpleObjectProperty<>(mi.get().k);
			this.kmux = new SimpleObjectProperty<>(mi.get().kmux);
			this.interrupt = new SimpleObjectProperty<>(mi.get().interrupt);
			this.ie = new SimpleObjectProperty<>(mi.get().ie);
			dbus.addListener((obs, o, n) -> mi.set(mi.get().withDbus(n)));
			mwe.addListener((obs, o, n) -> mi.set(mi.get().withMwe(n)));
			bz_ea.addListener((obs, o, n) -> mi.set(mi.get().withBz_ea(n)));
			ir_ld.addListener((obs, o, n) -> mi.set(mi.get().withIr_ld(n)));
			bz_inc.addListener((obs, o, n) -> mi.set(mi.get().withBz_inc(n)));
			bz_ed.addListener((obs, o, n) -> mi.set(mi.get().withBz_ed(n)));
			bz_ld.addListener((obs, o, n) -> mi.set(mi.get().withBz_ld(n)));
			bar.addListener((obs, o, n) -> mi.set(mi.get().withBar(n)));
			am2910_Inst.addListener((obs, o, n) -> mi.set(mi.get().withAm2910_Inst(n)));
			ccen.addListener((obs, o, n) -> mi.set(mi.get().withCcen(n)));
			am2904_Inst.addListener((obs, o, n) -> mi.set(mi.get().withAm2904_Inst(n)));
			ce_m.addListener((obs, o, n) -> mi.set(mi.get().withCe_m(n)));
			ce_µ.addListener((obs, o, n) -> mi.set(mi.get().withCe_µ(n)));
			am2904_Shift.addListener((obs, o, n) -> mi.set(mi.get().withAm2904_Shift(n)));
			am2904_Carry.addListener((obs, o, n) -> mi.set(mi.get().withAm2904_Carry(n)));
			abus.addListener((obs, o, n) -> mi.set(mi.get().withAbus(n)));
			bsel.addListener((obs, o, n) -> mi.set(mi.get().withBsel(n)));
			rb_addr.addListener((obs, o, n) -> mi.set(mi.get().withRb_addr(n)));
			asel.addListener((obs, o, n) -> mi.set(mi.get().withAsel(n)));
			ra_addr.addListener((obs, o, n) -> mi.set(mi.get().withRa_addr(n)));
			am2901_Dest.addListener((obs, o, n) -> mi.set(mi.get().withAm2901_Dest(n)));
			am2901_Func.addListener((obs, o, n) -> mi.set(mi.get().withAm2901_Func(n)));
			am2901_Src.addListener((obs, o, n) -> mi.set(mi.get().withAm2901_Src(n)));
			k.addListener((obs, o, n) -> mi.set(mi.get().withK(n)));
			kmux.addListener((obs, o, n) -> mi.set(mi.get().withKmux(n)));
			interrupt.addListener((obs, o, n) -> mi.set(mi.get().withInterrupt(n)));
			ie.addListener((obs, o, n) -> mi.set(mi.get().withIe(n)));
			mi.addListener((obs, o, n) -> mpm.setInstruction(address, n));
			mpm.setInstruction(address, mi.get());
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(HexIntStringConverter.INT_12.toString(address.get()) + ",");
			sb.append(mwe.get() + ",");
			sb.append(ir_ld.get() + ",");
			sb.append(bz_ea.get() + ",");
			sb.append(bz_inc.get() + ",");
			sb.append(bz_ed.get() + ",");
			sb.append(bz_ld.get() + ",");
			sb.append(bar.get() + ",");
			sb.append(am2910_Inst.get() + ",");
			sb.append(ccen.get() + ",");
			sb.append(am2904_Inst.get() + ",");
			sb.append(ce_m.get() + ",");
			sb.append(ce_µ.get() + ",");
			sb.append(am2904_Shift.get() + ",");
			sb.append(am2904_Carry.get() + ",");
			sb.append(dbus.get() + ",");
			sb.append(abus.get() + ",");
			sb.append(bsel.get() + ",");
			sb.append(rb_addr.get() + ",");
			sb.append(asel.get() + ",");
			sb.append(ra_addr.get() + ",");
			sb.append(am2901_Dest.get() + ",");
			sb.append(am2901_Func.get() + ",");
			sb.append(am2901_Src.get() + ",");
			sb.append(k.get() + ",");
			sb.append(kmux.get() + ",");
			sb.append(interrupt.get() + ",");
			sb.append(ie.get());
			return sb.toString();
		}

		public final ReadOnlyIntegerProperty addressProperty() {
			return address;
		}

		public final ObjectProperty<_ABUS> abusProperty() {
			return abus;
		}

		public final ObjectProperty<Am2901_Dest> am2901_DestProperty() {
			return am2901_Dest;
		}

		public final ObjectProperty<Am2901_Func> am2901_FuncProperty() {
			return am2901_Func;
		}

		public final ObjectProperty<Am2901_Src> am2901_SrcProperty() {
			return am2901_Src;
		}

		public final ObjectProperty<Am2904_Carry> am2904_CarryProperty() {
			return am2904_Carry;
		}

		public final ObjectProperty<Am2904_Inst> am2904_InstProperty() {
			return am2904_Inst;
		}

		public final ObjectProperty<Am2904_Shift> am2904_ShiftProperty() {
			return am2904_Shift;
		}

		public final ObjectProperty<Am2910_Inst> am2910_InstProperty() {
			return am2910_Inst;
		}

		public final ObjectProperty<ASEL> aselProperty() {
			return asel;
		}

		public final ObjectProperty<BAR> barProperty() {
			return bar;
		}

		public final ObjectProperty<BSEL> bselProperty() {
			return bsel;
		}

		public final ObjectProperty<_BZ_EA> bz_eaProperty() {
			return bz_ea;
		}

		public final ObjectProperty<_BZ_ED> bz_edProperty() {
			return bz_ed;
		}

		public final ObjectProperty<_BZ_INC> bz_incProperty() {
			return bz_inc;
		}

		public final ObjectProperty<_BZ_LD> bz_ldProperty() {
			return bz_ld;
		}

		public final ObjectProperty<_CCEN> ccenProperty() {
			return ccen;
		}

		public final ObjectProperty<_CE_M> ce_mProperty() {
			return ce_m;
		}

		public final ObjectProperty<_CE_µ> ce_µProperty() {
			return ce_µ;
		}

		public final ObjectProperty<_DBUS> dbusProperty() {
			return dbus;
		}

		public final ObjectProperty<IE> ieProperty() {
			return ie;
		}

		public final ObjectProperty<Interrupt> interruptProperty() {
			return interrupt;
		}

		public final ObjectProperty<_IR_LD> ir_ldProperty() {
			return ir_ld;
		}

		public final ObjectProperty<KMUX> kmuxProperty() {
			return kmux;
		}

		public final ObjectProperty<Konst> kProperty() {
			return k;
		}

		public final ObjectProperty<_MWE> mweProperty() {
			return mwe;
		}

		public final ObjectProperty<RA_ADDR> ra_addrProperty() {
			return ra_addr;
		}

		public final ObjectProperty<RB_ADDR> rb_addrProperty() {
			return rb_addr;
		}
	}
}
