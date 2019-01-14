package net.maisikoleni.am2900me.logic;

import net.maisikoleni.am2900me.logic.microinstr.Am2901_Dest;
import net.maisikoleni.am2900me.logic.microinstr.Am2901_Func;
import net.maisikoleni.am2900me.logic.microinstr.Am2901_Src;
import net.maisikoleni.am2900me.logic.microinstr.Am2904_Carry;
import net.maisikoleni.am2900me.logic.microinstr.Am2904_Inst;
import net.maisikoleni.am2900me.logic.microinstr.Am2904_Shift;
import net.maisikoleni.am2900me.logic.microinstr._CE_M;
import net.maisikoleni.am2900me.logic.microinstr._CE_µ;

/**
 * The combination of {@link Am2904} and {@link Am2901x4}; everything wired up
 * to one unit because it behaves like one and helps to reduce input/output
 * complexity.
 *
 * @author MaisiKoleni
 *
 */
public class Am2904_01x4 {
	private final Am2904 am2904 = new Am2904();
	private final Am2901x4 am2901x4 = new Am2901x4();

	final Am2904_01x4input input = new Am2904_01x4input();
	final Am2904_01x4output output = new Am2904_01x4output();

	/**
	 * Creates a new {@link Am2904_01x4} complex, all machine status registers are
	 * always enabled, because the machine in the example does not seem to provide
	 * so details control over the MSR. Still left _OECT and _OE for
	 * performance/experimental reasons.
	 * 
	 * @author MaisiKoleni
	 */
	public Am2904_01x4() {
		am2904.input._EZ = 0;
		am2904.input._EC = 0;
		am2904.input._EN = 0;
		am2904.input._EOVR = 0;
		am2904.input.CX = 0;
		am2904.input._OECT = 0;
		am2901x4.input._OE = 0;
	}

	/**
	 * Does all the calculations the ALU and status tests are told by the input
	 * instructions. Results in a CT and Y output.
	 * 
	 * @author MaisiKoleni
	 */
	public void process() {
		// Am2904 setup
		am2904.input.mi_carry = input.mi_carry;
		am2904.input.mi_inst = input.mi_inst;
		am2904.input.mi_shift = input.mi_shift;
		am2904.input._CEM = input._CEM;
		am2904.input._CEµ = input._CEµ;
		am2904.input._SE = input.mi_dest.doesShift() ? 0 : 1;
		am2904.input.I10 = input.mi_dest.getI7();
		am2904.input._OEY = input._OEY;
		am2904.input._OECT = input._OECT;
		// Am2901 x 4 setup
		am2901x4.input.mi_src = input.mi_src;
		am2901x4.input.mi_func = input.mi_func;
		am2901x4.input.mi_dest = input.mi_dest;
		am2901x4.input.D = input.D;
		am2901x4.input.regA_addr = input.regA_addr;
		am2901x4.input.regB_addr = input.regB_addr;
		// first step
		am2904.processStep1();
		am2901x4.input.C0 = am2904.output.C0;
		am2901x4.processStep1();
		am2904.input.IZ = am2901x4.output.IZ;
		am2904.input.IC = am2901x4.output.IC;
		am2904.input.IN = am2901x4.output.IN;
		am2904.input.IOVR = am2901x4.output.IOVR;
		// second step
		am2904.input.QIO0 = am2901x4.output.QIO0;
		am2904.input.QIO3 = am2901x4.output.QIO3;
		am2904.input.SIO0 = am2901x4.output.SIO0;
		am2904.input.SIO3 = am2901x4.output.SIO3;
		am2904.input.Y3 = am2901x4.output.Y >> 12;
		am2904.processStep2();
		am2901x4.input.QIO0 = am2904.output.QIO0;
		am2901x4.input.QIO3 = am2904.output.QIO3;
		am2901x4.input.SIO0 = am2904.output.SIO0;
		am2901x4.input.SIO3 = am2904.output.SIO3;
		am2901x4.processStep2();
		// collect results
		int Y = am2901x4.output.Y;
		// "simulate" _OE = H of Am2901 Nr. 3 (too complicated now otherwise)
		if (input._OEY == 0)
			Y = (Y & 0x0FFF) | (am2904.output.Y3 << 12);
		output.Y = Y;
		output.CT = am2904.output.CT;
	}

	public final int getµC() {
		return am2904.getµC();
	}

	public final int getµN() {
		return am2904.getµN();
	}

	public final int getµZ() {
		return am2904.getµZ();
	}

	public final int getµOVR() {
		return am2904.getµOVR();
	}

	public final int getMC() {
		return am2904.getMC();
	}

	public final int getMN() {
		return am2904.getMN();
	}

	public final int getMZ() {
		return am2904.getMZ();
	}

	public final int getMOVR() {
		return am2904.getMOVR();
	}

	public final void setµC(int μC) {
		am2904.setµC(μC);
	}

	public final void setµN(int μN) {
		am2904.setµN(μN);
	}

	public final void setµZ(int μZ) {
		am2904.setµZ(μZ);
	}

	public final void setµOVR(int μOVR) {
		am2904.setµOVR(μOVR);
	}

	public final void setMC(int mC) {
		am2904.setMC(mC);
	}

	public final void setMN(int mN) {
		am2904.setMN(mN);
	}

	public final void setMZ(int mZ) {
		am2904.setMZ(mZ);
	}

	public final void setMOVR(int mOVR) {
		am2904.setMOVR(mOVR);
	}

	public final int getRegisters4bit(int addr) {
		return am2901x4.getRegisters4bit(addr);
	}

	public final int getQ() {
		return am2901x4.getQ();
	}

	public final void setRegisters4bit(int addr, int register4bit) {
		am2901x4.setRegisters4bit(addr, register4bit);
	}

	public final void setQ(int q) {
		am2901x4.setQ(q);
	}
}

class Am2904_01x4input {
	Am2901_Dest mi_dest;
	Am2901_Func mi_func;
	Am2901_Src mi_src;
	Am2904_Inst mi_inst;
	Am2904_Carry mi_carry;
	Am2904_Shift mi_shift;
	_CE_M _CEM;
	_CE_µ _CEµ;
	int _OEY;
	int _OECT;
	int D;
	int regA_addr;
	int regB_addr;
}

class Am2904_01x4output {
	int CT;
	int Y;
}
