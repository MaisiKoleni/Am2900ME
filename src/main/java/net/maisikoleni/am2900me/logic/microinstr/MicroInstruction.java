package net.maisikoleni.am2900me.logic.microinstr;

import java.util.List;

/**
 * Represents a 80 bit wide Microinstruction for this specific 16bit Am2900
 * machine.
 *
 * @author MaisiKoleni
 *
 */
public class MicroInstruction {
	public static final List<String> FIELD_NAMES = List.of("mwe", "ir_ld", "bz_ea", "bz_inc", "bz_ed", "bz_ld", "bar",
			"am2910_Inst", "ccen", "am2904_Inst", "ce_m", "ce_µ", "am2904_Shift", "am2904_Carry", "dbus", "abus",
			"bsel", "rb_addr", "asel", "ra_addr", "am2901_Dest", "am2901_Func", "am2901_Src", "k", "kmux", "interrupt",
			"ie");

	public static final MicroInstruction DEFAULT = new MicroInstruction(_MWE.R, _IR_LD.H, _BZ_EA.H, _BZ_INC.H, _BZ_ED.H,
			_BZ_LD.H, BAR.DEFAULT, Am2910_Inst.CONT, _CCEN.PS, Am2904_Inst.Load_Load_I_Z, _CE_M.H, _CE_µ.H,
			Am2904_Shift.DEFAULT, Am2904_Carry.CI0, _DBUS.H, _ABUS.H, BSEL.IR, RB_ADDR.DEFAULT, ASEL.IR,
			RA_ADDR.DEFAULT, Am2901_Dest.NOP, Am2901_Func.ADD, Am2901_Src.AB, Konst.DEFAULT, KMUX.K, Interrupt.DEFAULT,
			IE.DIS);

	public static final List<µIField> FIELD_DEFAULTS = List.of(DEFAULT.mwe, DEFAULT.ir_ld, DEFAULT.bz_ea,
			DEFAULT.bz_inc, DEFAULT.bz_ed, DEFAULT.bz_ld, DEFAULT.bar, DEFAULT.am2910_Inst, DEFAULT.ccen,
			DEFAULT.am2904_Inst, DEFAULT.ce_m, DEFAULT.ce_µ, DEFAULT.am2904_Shift, DEFAULT.am2904_Carry, DEFAULT.dbus,
			DEFAULT.abus, DEFAULT.bsel, DEFAULT.rb_addr, DEFAULT.asel, DEFAULT.ra_addr, DEFAULT.am2901_Dest,
			DEFAULT.am2901_Func, DEFAULT.am2901_Src, DEFAULT.k, DEFAULT.kmux, DEFAULT.interrupt, DEFAULT.ie);

	public final _MWE mwe;
	public final _IR_LD ir_ld;
	public final _BZ_EA bz_ea;
	public final _BZ_INC bz_inc;
	public final _BZ_ED bz_ed;
	public final _BZ_LD bz_ld;
	public final BAR bar;
	public final Am2910_Inst am2910_Inst;
	public final _CCEN ccen;
	public final Am2904_Inst am2904_Inst;
	public final _CE_M ce_m;
	public final _CE_µ ce_µ;
	public final Am2904_Shift am2904_Shift;
	public final Am2904_Carry am2904_Carry;
	public final _DBUS dbus;
	public final _ABUS abus;
	public final BSEL bsel;
	public final RB_ADDR rb_addr;
	public final ASEL asel;
	public final RA_ADDR ra_addr;
	public final Am2901_Dest am2901_Dest;
	public final Am2901_Func am2901_Func;
	public final Am2901_Src am2901_Src;
	public final Konst k;
	public final KMUX kmux;
	public final Interrupt interrupt;
	public final IE ie;

	private MicroInstruction(_MWE mwe, _IR_LD ir_ld, _BZ_EA bz_ea, _BZ_INC bz_inc, _BZ_ED bz_ed, _BZ_LD bz_ld, BAR bar,
			Am2910_Inst am2910_Inst, _CCEN ccen, Am2904_Inst am2904_Inst, _CE_M ce_m, _CE_µ ce_µ,
			Am2904_Shift am2904_Shift, Am2904_Carry am2904_Carry, _DBUS dbus, _ABUS abus, BSEL bsel, RB_ADDR rb_addr,
			ASEL asel, RA_ADDR ra_addr, Am2901_Dest am2901_Dest, Am2901_Func am2901_Func, Am2901_Src am2901_Src,
			Konst k, KMUX kmux, Interrupt interrupt, IE ie) {
		this.mwe = mwe;
		this.ir_ld = ir_ld;
		this.bz_ea = bz_ea;
		this.bz_inc = bz_inc;
		this.bz_ed = bz_ed;
		this.bz_ld = bz_ld;
		this.bar = bar;
		this.am2910_Inst = am2910_Inst;
		this.ccen = ccen;
		this.am2904_Inst = am2904_Inst;
		this.ce_m = ce_m;
		this.ce_µ = ce_µ;
		this.am2904_Shift = am2904_Shift;
		this.am2904_Carry = am2904_Carry;
		this.dbus = dbus;
		this.abus = abus;
		this.bsel = bsel;
		this.rb_addr = rb_addr;
		this.asel = asel;
		this.ra_addr = ra_addr;
		this.am2901_Dest = am2901_Dest;
		this.am2901_Func = am2901_Func;
		this.am2901_Src = am2901_Src;
		this.k = k;
		this.kmux = kmux;
		this.interrupt = interrupt;
		this.ie = ie;
	}

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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MicroInstruction))
			return false;
		MicroInstruction other = (MicroInstruction) obj;
		if (abus != other.abus)
			return false;
		if (am2901_Dest != other.am2901_Dest)
			return false;
		if (am2901_Func != other.am2901_Func)
			return false;
		if (am2901_Src != other.am2901_Src)
			return false;
		if (am2904_Carry != other.am2904_Carry)
			return false;
		if (am2904_Inst != other.am2904_Inst)
			return false;
		if (am2904_Shift == null) {
			if (other.am2904_Shift != null)
				return false;
		} else if (!am2904_Shift.equals(other.am2904_Shift))
			return false;
		if (am2910_Inst != other.am2910_Inst)
			return false;
		if (asel != other.asel)
			return false;
		if (bar == null) {
			if (other.bar != null)
				return false;
		} else if (!bar.equals(other.bar))
			return false;
		if (bsel != other.bsel)
			return false;
		if (bz_ea != other.bz_ea)
			return false;
		if (bz_ed != other.bz_ed)
			return false;
		if (bz_inc != other.bz_inc)
			return false;
		if (bz_ld != other.bz_ld)
			return false;
		if (ccen != other.ccen)
			return false;
		if (ce_m != other.ce_m)
			return false;
		if (ce_µ != other.ce_µ)
			return false;
		if (dbus != other.dbus)
			return false;
		if (ie != other.ie)
			return false;
		if (interrupt == null) {
			if (other.interrupt != null)
				return false;
		} else if (!interrupt.equals(other.interrupt))
			return false;
		if (ir_ld != other.ir_ld)
			return false;
		if (k == null) {
			if (other.k != null)
				return false;
		} else if (!k.equals(other.k))
			return false;
		if (kmux != other.kmux)
			return false;
		if (mwe != other.mwe)
			return false;
		if (ra_addr == null) {
			if (other.ra_addr != null)
				return false;
		} else if (!ra_addr.equals(other.ra_addr))
			return false;
		if (rb_addr == null) {
			if (other.rb_addr != null)
				return false;
		} else if (!rb_addr.equals(other.rb_addr))
			return false;
		return true;
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

	public final MicroInstruction withMwe(_MWE mwe) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withIr_ld(_IR_LD ir_ld) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withBz_ea(_BZ_EA bz_ea) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withBz_inc(_BZ_INC bz_inc) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withBz_ed(_BZ_ED bz_ed) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withBz_ld(_BZ_LD bz_ld) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withBar(BAR bar) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withAm2910_Inst(Am2910_Inst am2910_Inst) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withCcen(_CCEN ccen) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withAm2904_Inst(Am2904_Inst am2904_Inst) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withCe_m(_CE_M ce_m) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withCe_µ(_CE_µ ce_µ) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withAm2904_Shift(Am2904_Shift am2904_Shift) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withAm2904_Carry(Am2904_Carry am2904_Carry) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withDbus(_DBUS dbus) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withAbus(_ABUS abus) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withBsel(BSEL bsel) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withRb_addr(RB_ADDR rb_addr) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withAsel(ASEL asel) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withRa_addr(RA_ADDR ra_addr) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withAm2901_Dest(Am2901_Dest am2901_Dest) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withAm2901_Func(Am2901_Func am2901_Func) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withAm2901_Src(Am2901_Src am2901_Src) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withK(Konst k) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withKmux(KMUX kmux) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withInterrupt(Interrupt interrupt) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	public final MicroInstruction withIe(IE ie) {
		return new MicroInstruction(mwe, ir_ld, bz_ea, bz_inc, bz_ed, bz_ld, bar, am2910_Inst, ccen, am2904_Inst, ce_m,
				ce_µ, am2904_Shift, am2904_Carry, dbus, abus, bsel, rb_addr, asel, ra_addr, am2901_Dest, am2901_Func,
				am2901_Src, k, kmux, interrupt, ie);
	}

	/**
	 * Not intended to be used
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}