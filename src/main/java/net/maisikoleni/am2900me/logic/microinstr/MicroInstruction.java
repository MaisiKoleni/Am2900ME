package net.maisikoleni.am2900me.logic.microinstr;

/**
 * Represents a 80 bit wide Microinstruction for this specific 16bit Am2900
 * machine.
 *
 * @author MaisiKoleni
 *
 */
@SuppressWarnings("javadoc")
public class MicroInstruction {
	public _MWE mwe = _MWE.R;
	public _IR_LD ir_ld = _IR_LD.H;
	public _BZ_EA bz_ea = _BZ_EA.H;
	public _BZ_INC bz_inc = _BZ_INC.H;
	public _BZ_ED bz_ed = _BZ_ED.H;
	public _BZ_LD bz_ld = _BZ_LD.H;
	public BAR bar = BAR.NONE;
	public Am2910_Inst am2910_Inst = Am2910_Inst.CONT;
	public _CCEN ccen = _CCEN.PS;
	public Am2904_Inst am2904_Inst = Am2904_Inst.Load_Load_I_Z;
	public _CE_M ce_m = _CE_M.H;
	public _CE_µ ce_µ = _CE_µ.H;
	public Am2904_Shift am2904_Shift = Am2904_Shift.NONE;
	public Am2904_Carry am2904_Carry = Am2904_Carry.CI0;
	public _DBUS dbus = _DBUS.H;
	public _ABUS abus = _ABUS.H;
	public BSEL bsel = BSEL.IR;
	public RB_ADDR rb_addr = RB_ADDR.NONE;
	public ASEL asel = ASEL.IR;
	public RA_ADDR ra_addr = RA_ADDR.NONE;
	public Am2901_Dest am2901_Dest = Am2901_Dest.NOP;
	public Am2901_Func am2901_Func = Am2901_Func.ADD;
	public Am2901_Src am2901_Src = Am2901_Src.AB;
	public Konst k = Konst.NONE;
	public KMUX kmux = KMUX.K;
	public Interrupt interrupt = Interrupt.NONE;
	public IE ie = IE.DIS;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("MicroInstruction: {\n");
		sb.append("\t" + mwe + ",\n");
		sb.append("\t" + ir_ld + ",\n");
		sb.append("\t" + bz_ea + ",\n");
		sb.append("\t" + bz_inc + ",\n");
		sb.append("\t" + bz_ed + ",\n");
		sb.append("\t" + bz_ld + ",\n");
		sb.append("\t" + bar + ",\n");
		sb.append("\t" + am2910_Inst + ",\n");
		sb.append("\t" + ccen + ",\n");
		sb.append("\t" + am2904_Inst + ",\n");
		sb.append("\t" + ce_m + ",\n");
		sb.append("\t" + ce_µ + ",\n");
		sb.append("\t" + am2904_Shift + ",\n");
		sb.append("\t" + am2904_Carry + ",\n");
		sb.append("\t" + dbus + ",\n");
		sb.append("\t" + abus + ",\n");
		sb.append("\t" + bsel + ",\n");
		sb.append("\t" + rb_addr + ",\n");
		sb.append("\t" + asel + ",\n");
		sb.append("\t" + ra_addr + ",\n");
		sb.append("\t" + am2901_Dest + ",\n");
		sb.append("\t" + am2901_Func + ",\n");
		sb.append("\t" + am2901_Src + ",\n");
		sb.append("\t" + k + ",\n");
		sb.append("\t" + kmux + ",\n");
		sb.append("\t" + interrupt + ",\n");
		sb.append("\t" + ie + "\n}");
		return sb.toString();
	}

	public final _MWE getMwe() {
		return mwe;
	}

	public final _IR_LD getIr_ld() {
		return ir_ld;
	}

	public final _BZ_EA getBz_ea() {
		return bz_ea;
	}

	public final _BZ_INC getBz_inc() {
		return bz_inc;
	}

	public final _BZ_ED getBz_ed() {
		return bz_ed;
	}

	public final _BZ_LD getBz_ld() {
		return bz_ld;
	}

	public final BAR getBar() {
		return bar;
	}

	public final Am2910_Inst getAm2910_Inst() {
		return am2910_Inst;
	}

	public final _CCEN getCcen() {
		return ccen;
	}

	public final Am2904_Inst getAm2904_Inst() {
		return am2904_Inst;
	}

	public final _CE_M getCe_m() {
		return ce_m;
	}

	public final _CE_µ getCe_µ() {
		return ce_µ;
	}

	public final Am2904_Shift getAm2904_Shift() {
		return am2904_Shift;
	}

	public final Am2904_Carry getAm2904_Carry() {
		return am2904_Carry;
	}

	public final _DBUS getDbus() {
		return dbus;
	}

	public final _ABUS getAbus() {
		return abus;
	}

	public final BSEL getBsel() {
		return bsel;
	}

	public final RB_ADDR getRb_addr() {
		return rb_addr;
	}

	public final ASEL getAsel() {
		return asel;
	}

	public final RA_ADDR getRa_addr() {
		return ra_addr;
	}

	public final Am2901_Dest getAm2901_Dest() {
		return am2901_Dest;
	}

	public final Am2901_Func getAm2901_Func() {
		return am2901_Func;
	}

	public final Am2901_Src getAm2901_Src() {
		return am2901_Src;
	}

	public final Konst getK() {
		return k;
	}

	public final KMUX getKmux() {
		return kmux;
	}

	public final Interrupt getInterrupt() {
		return interrupt;
	}

	public final IE getIe() {
		return ie;
	}
}