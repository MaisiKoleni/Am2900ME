package net.maisikoleni.am2900me.logic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a 80 bit wide Microinstruction for this specific 16bit Am2900
 * machine.
 *
 * @author MaisiKoleni
 *
 */
public class MicroInstruction {
	_MWE mwe = _MWE.R;
	_IR_LD ir_ld = _IR_LD.H;
	_BZ_EA bz_ea = _BZ_EA.H;
	_BZ_INC bz_inc = _BZ_INC.H;
	_BZ_ED bz_ed = _BZ_ED.H;
	_BZ_LD bz_ld = _BZ_LD.H;
	BAR bar = BAR.NONE;
	Am2910_Inst am2910_Inst = Am2910_Inst.CONT;
	_CCEN ccen = _CCEN.PS;
	Am2904_Inst am2904_Inst = Am2904_Inst.Load_Load_I_Z;
	_CE_M ce_m = _CE_M.H;
	_CE_µ ce_µ = _CE_µ.H;
	Am2904_Shift am2904_Shift = Am2904_Shift.NONE;
	Am2904_Carry am2904_Carry = Am2904_Carry.CI0;
	_DBUS dbus = _DBUS.H;
	_ABUS abus = _ABUS.H;
	BSEL bsel = BSEL.IR;
	RB_ADDR rb_addr = RB_ADDR.NONE;
	ASEL asel = ASEL.IR;
	RA_ADDR ra_addr = RA_ADDR.NONE;
	Am2901_Dest am2901_Dest = Am2901_Dest.NOP;
	Am2901_Func am2901_Func = Am2901_Func.ADD;
	Am2901_Src am2901_Src = Am2901_Src.AB;
	Konst k = Konst.NONE;
	KMUX kmux = KMUX.D;
	Interrupt interrupt = Interrupt.NONE;
	IE ie = IE.DIS;
}

interface µIField {
	String getFullName();
}

enum _MWE implements µIField {
	W, R;

	@Override
	public String getFullName() {
		return "¬ Memory-Write-Enable";
	}
}

enum _IR_LD implements µIField {
	L, H;

	@Override
	public String getFullName() {
		return "¬ Instruktionsregister Laden";
	}
}

enum _BZ_EA implements µIField {
	E, H;

	@Override
	public String getFullName() {
		return "¬ Befehlszähler Enable Adressbus";
	}
}

enum _BZ_INC implements µIField {
	I, H;

	@Override
	public String getFullName() {
		return "¬ Befehlszähler Inkrement";
	}
}

enum _BZ_ED implements µIField {
	E, H;

	@Override
	public String getFullName() {
		return "¬ Befehlszähler Enable Datenbus";
	}
}

enum _BZ_LD implements µIField {
	L, H;

	@Override
	public String getFullName() {
		return "¬ Befehlszähler Laden";
	}
}

class BAR implements µIField {
	static final BAR NONE = new BAR(0x000);

	final int uint_12bit;

	public BAR(int uint_12bit) {
		this.uint_12bit = uint_12bit;
	}

	@Override
	public String getFullName() {
		return "Direktadressfeld";
	}
}

enum Am2910_Inst implements µIField {
	JZ, CJS, JMAP, CJP, PUSH, JSRP, CJV, JRP, RFCT, RPCT, CRTN, CJPP, LDCT, LOOP, CONT, TWB;

	@Override
	public String getFullName() {
		return "Fortschaltebefehle des Am2910";
	}
}

enum _CCEN implements µIField {
	C, PS;

	@Override
	public String getFullName() {
		return "Condition Code Enable Am2910";
	}
}

/**
 * Pattern:<br>
 * [microSR]_[machineSR]_[condCodeSource]_[condCode]<br>
 * <ul>
 * <li>Load = load from I inputs (Am2904s)</li>
 * <li>condCodeSource = µ, M, I (and a special case: I and M)</li>
 * <li>condCode = the boolean term defining the CT output</li>
 * </ul>
 */
enum Am2904_Inst implements µIField {
	LoadM_LoadY_µ_NxorOVRorZ(0_00), Set_Set_µ_NxnorOVRornotZ(0_01), Swap_Swap_µ_NxorOVR(0_02),
	Reset_Reset_µ_NxnorOVR(0_03), Load_LoadForShiftThroughOvr_µ_Z(0_04), Load_Invert_µ_notZ(0_05),
	LoadOvrRetain_Load_µ_OVR(0_06), LoadOvrRetain_Load_µ_notOVR(0_07), ResetZ_LoadCarryInvert_µ_CorZ(0_10),
	SetZ_LoadCarryInvert_µ_notCandnotZ(0_11), ResetC_Load_µ_C(0_12), SetC_Load_µ_notC(0_13),
	ResetN_Load_µ_notCorZ(0_14), SetN_Load_µ_CandnotZ(0_15), ResetOvr_Load_IM_NxorN(0_16), SetOvr_Load_IM_NxnorN(0_17),
	Load_Load_µ_NxorOVRorZ(0_20), Load_Load_µ_NxnorOVRornotZ(0_21), Load_Load_µ_NxorOVR(0_22),
	Load_Load_µ_NxnorOVR(0_23), Load_Load_µ_Z(0_24), Load_Load_µ_notZ(0_25), Load_Load_µ_OVR(0_26),
	Load_Load_µ_notOVR(0_27), LoadCarryInvert_LoadCarryInvert_µ_CorZ(0_30),
	LoadCarryInvert_LoadCarryInvert_µ_notCandnotZ(0_31), Load_Load_µ_C(0_32), Load_Load_µ_notC(0_33),
	Load_Load_µ_notCorZ(0_34), Load_Load_µ_CandnotZ(0_35), Load_Load_µ_N(0_36), Load_Load_µ_notN(0_37),
	Load_Load_M_NxorOVRorZ(0_40), Load_Load_M_NxnorOVRornotZ(0_41), Load_Load_M_NxorOVR(0_42),
	Load_Load_M_NxnorOVR(0_43), Load_Load_M_Z(0_44), Load_Load_M_notZ(0_45), Load_Load_M_OVR(0_46),
	Load_Load_M_notOVR(0_47), LoadCarryInvert_LoadCarryInvert_M_CorZ(0_50),
	LoadCarryInvert_LoadCarryInvert_M_notCandnotZ(0_51), Load_Load_M_C(0_52), Load_Load_M_notC(0_53),
	Load_Load_M_notCorZ(0_54), Load_Load_M_CandnotZ(0_55), Load_Load_M_N(0_56), Load_Load_M_notN(0_57),
	Load_Load_I_NxorOVRorZ(0_60), Load_Load_I_NxnorOVRornotZ(0_61), Load_Load_I_NxorOVR(0_62),
	Load_Load_I_NxnorOVR(0_63), Load_Load_I_Z(0_64), Load_Load_I_notZ(0_65), Load_Load_I_OVR(0_66),
	Load_Load_I_notOVR(0_67), LoadCarryInvert_LoadCarryInvert_I_notCorZ(0_70),
	LoadCarryInvert_LoadCarryInvert_I_CandnotZ(0_71), Load_Load_I_C(0_72), Load_Load_I_notC(0_73),
	Load_Load_I_notCorZ(0_74), Load_Load_I_CandnotZ(0_75), Load_Load_I_N(0_76), Load_Load_I_notN(0_77);

	public final int code;

	private Am2904_Inst(int code) {
		this.code = code;
	}

	private static final Map<Integer, Am2904_Inst> instCodes;

	static {
		Map<Integer, Am2904_Inst> instCodesMod = new HashMap<>();
		Am2904_Inst[] vals = values();
		for (Am2904_Inst inst : vals) {
			assert inst.code == inst.ordinal();
			instCodesMod.put(inst.code, inst);
		}
		if (instCodesMod.size() != 64)
			throw new IllegalStateException(
					"There must be exactly 64 Am2904 instruction codes, but was " + instCodesMod.size());
		instCodes = Collections.unmodifiableMap(instCodesMod);
	}

	public static Am2904_Inst getInstFor(int I_543210) {
		Am2904_Inst inst = instCodes.get(I_543210);
		if (inst != null)
			return inst;
		throw new IllegalArgumentException("Invaild Am2904 instruction code: " + I_543210);
	}

	public int getBlock() {
		return code >> 4;
	}

	public boolean isLoadCarryInvert() {
		return (code & 0b001_110) == 0b001_000;
	}

	@Override
	public String getFullName() {
		return "Am2904 Instructions";
	}
}

enum _CE_M implements µIField {
	L, H;

	@Override
	public String getFullName() {
		return "Enablebit für das MSR";
	}
}

enum _CE_µ implements µIField {
	L, H;

	@Override
	public String getFullName() {
		return "Enablebit für das µSR";
	}
}

class Am2904_Shift implements µIField {
	static final Am2904_Shift NONE = new Am2904_Shift(0x0);

	final int uint_4bit;

	public Am2904_Shift(int uint_4bit) {
		this.uint_4bit = uint_4bit;
	}

	@Override
	public String getFullName() {
		return "Instruktionsbit des Am2904 - Schiebesteuerung";
	}
}

enum Am2904_Carry implements µIField {
	CI0, CI1, CX, USE_SR;

	@Override
	public String getFullName() {
		return "Instruktionsbit des Am2904 - Übertragssteuerung";
	}
}

enum _DBUS implements µIField {
	DB, H;

	@Override
	public String getFullName() {
		return "Datenbus Select";
	}
}

enum _ABUS implements µIField {
	AB, H;

	@Override
	public String getFullName() {
		return "Adressbus Select";
	}
}

enum BSEL implements µIField {
	IR, MR;

	@Override
	public String getFullName() {
		return "Register B Adress-Quelle";
	}
}

class RB_ADDR implements µIField {
	static final RB_ADDR NONE = new RB_ADDR(0x00);

	final int uint_8bit;

	public RB_ADDR(int uint_8bit) {
		this.uint_8bit = uint_8bit;
	}

	@Override
	public String getFullName() {
		return "Register B Direktadresse";
	}
}

enum ASEL implements µIField {
	IR, MR;

	@Override
	public String getFullName() {
		return "Register A Adress-Quelle";
	}
}

class RA_ADDR implements µIField {
	static final RA_ADDR NONE = new RA_ADDR(0x00);

	final int uint_8bit;

	public RA_ADDR(int uint_8bit) {
		this.uint_8bit = uint_8bit;
	}

	@Override
	public String getFullName() {
		return "Register A Direktadresse";
	}
}

enum Am2901_Dest implements µIField {
	QREG, NOP, RAMA, RAMF, RAMQD, RAMD, RAMQU, RAMU;

	public boolean doesShift() {
		return ordinal() >= 4;
	}

	public int getShiftDir() {
		return doesShift() ? ordinal() < 6 ? -1 : 1 : 0;
	}

	public int getI7() {
		return this.ordinal() >> 1 & 1;
	}

	@Override
	public String getFullName() {
		return "ALU Zielsteuerung";
	}
}

enum Am2901_Func implements µIField {
	ADD, SUBR, SUBS, OR, AND, NOTRS, EXOR, EXNOR;

	@Override
	public String getFullName() {
		return "ALU Funktionen";
	}
}

enum Am2901_Src implements µIField {
	AQ, AB, ZQ, ZB, ZA, DA, DQ, DZ;

	@Override
	public String getFullName() {
		return "ALU Quelloperanden";
	}
}

class Konst implements µIField {
	static final Konst NONE = new Konst(0x0000);

	final int uint_16bit;

	public Konst(int uint_16bit) {
		this.uint_16bit = uint_16bit;
	}

	@Override
	public String getFullName() {
		return "Konstantenfeld";
	}
}

enum KMUX implements µIField {
	K, D;

	@Override
	public String getFullName() {
		return "KMUX";
	}
}

class Interrupt implements µIField {
	static final Interrupt NONE = new Interrupt(0x0);

	final int uint_4bit;

	public Interrupt(int uint_4bit) {
		this.uint_4bit = uint_4bit;
	}

	@Override
	public String getFullName() {
		return "Interruptsteuerung";
	}
}

enum IE implements µIField {
	IE, DIS;

	@Override
	public String getFullName() {
		return "Interrupt Enable";
	}
}