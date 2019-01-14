package net.maisikoleni.am2900me.logic;

import net.maisikoleni.am2900me.logic.microinstr.Am2901_Dest;
import net.maisikoleni.am2900me.logic.microinstr.Am2901_Func;
import net.maisikoleni.am2900me.logic.microinstr.Am2901_Src;
import net.maisikoleni.am2900me.util.BitUtil;

/**
 * Four {@link Am2901} combined (without Am2902 here) make a 16 bit ALU.
 *
 * @author MaisiKoleni
 *
 */
public class Am2901x4 {
	// nibble3 being the most significant
	final Am2901 nibble3 = new Am2901();
	final Am2901 nibble2 = new Am2901();
	final Am2901 nibble1 = new Am2901();
	final Am2901 nibble0 = new Am2901();

	final Am2901[] all = { nibble0, nibble1, nibble2, nibble3 };

	final Am2901x4input input = new Am2901x4input();
	final Am2901x4output output = new Am2901x4output();

	/**
	 * Calculates the Y output and all status bits. Does not change the state of any
	 * registers.
	 * 
	 * @author MaisiKoleni
	 */
	public Am2901x4output processStep1() {
		// preparation
		for (int i = 0; i < all.length; i++) {
			all[i].input.mi_func = input.mi_func;
			all[i].input.mi_dest = input.mi_dest;
			all[i].input.mi_src = input.mi_src;
			all[i].input._OE = input._OE;
			all[i].input.regA_addr = input.regA_addr;
			all[i].input.regB_addr = input.regB_addr;
		}
		nibble0.input.D = input.D & 0b1111;
		nibble1.input.D = (input.D >> 4) & 0b1111;
		nibble2.input.D = (input.D >> 8) & 0b1111;
		nibble3.input.D = (input.D >> 12) & 0b1111;
		// calculation
		nibble0.input.Cn = input.C0;
		nibble0.processStep1();
		nibble1.input.Cn = nibble0.output.Cn4;
		nibble1.processStep1();
		nibble2.input.Cn = nibble1.output.Cn4;
		nibble2.processStep1();
		nibble3.input.Cn = nibble2.output.Cn4;
		nibble3.processStep1();
		// setting outputs
		output.IC = nibble3.output.Cn4;
		output.IN = nibble3.output.F3;
		output.IOVR = nibble3.output.OVR;
		output.IZ = nibble0.output.F0;
		output.IZ &= nibble1.output.F0;
		output.IZ &= nibble2.output.F0;
		output.IZ &= nibble3.output.F0;

		output.QIO0 = nibble0.output.Q0;
		output.SIO0 = nibble0.output.RAM0;
		output.QIO3 = nibble3.output.Q3;
		output.SIO3 = nibble3.output.RAM3;

		int Y = nibble0.output.Y;
		Y |= nibble1.output.Y << 4;
		Y |= nibble2.output.Y << 8;
		Y |= nibble3.output.Y << 12;
		if (input._OE == 0)
			output.Y = Y;
		else
			output.Y = BitUtil.TRI_STATE_OFF;
		return output;
	}

	/**
	 * Shifts and saves the results to the registers, depending on the instructions.
	 * 
	 * @author MaisiKoleni
	 */
	public Am2901x4output processStep2() {
		// prepare shifting
		nibble0.input.Q0 = input.QIO0;
		nibble0.input.RAM0 = input.SIO0;
		for (int i = 0; i < all.length - 1; i++) {
			all[i].input.Q3 = all[i + 1].output.Q0;
			all[i].input.RAM3 = all[i + 1].output.RAM0;
			all[i + 1].input.Q0 = all[i].output.Q3;
			all[i + 1].input.RAM0 = all[i].output.RAM3;
		}
		nibble3.input.Q3 = input.QIO3;
		nibble3.input.RAM3 = input.SIO3;
		// saving & shifting
		for (int i = 0; i < all.length; i++) {
			all[i].processStep2();
		}
		return output;
	}

	@SuppressWarnings("unused")
	private void emulateAm2902() {
		// not relevant, because no lookahead needed :/
		// the P and G signals would be used to predict the carry of each Am2901,
		// so that the "doForAllAndInBetween" would not be needed. But since
		// everything here is sequential, that would make it just more complicated.
		// Maybe implement a Am2902 later just for demonstration purposes.
	}

	public final int getRegisters4bit(int addr) {
		int reg = nibble0.getRegisters4bit(addr);
		reg |= nibble1.getRegisters4bit(addr) << 4;
		reg |= nibble2.getRegisters4bit(addr) << 8;
		reg |= nibble3.getRegisters4bit(addr) << 12;
		return reg;
	}

	public final int getQ() {
		int reg = nibble0.getQ();
		reg |= nibble1.getQ() << 4;
		reg |= nibble2.getQ() << 8;
		reg |= nibble3.getQ() << 12;
		return reg;
	}

	public final void setRegisters4bit(int addr, int register4bit) {
		nibble0.setRegisters4bit(addr, register4bit & 0b1111);
		nibble1.setRegisters4bit(addr, (register4bit >> 4) & 0b1111);
		nibble2.setRegisters4bit(addr, (register4bit >> 8) & 0b1111);
		nibble3.setRegisters4bit(addr, (register4bit >> 12) & 0b1111);
	}

	public final void setQ(int q) {
		nibble0.setQ(q & 0b1111);
		nibble1.setQ((q >> 4) & 0b1111);
		nibble2.setQ((q >> 8) & 0b1111);
		nibble3.setQ((q >> 12) & 0b1111);
	}

	public void reset() {
		nibble0.reset();
		nibble1.reset();
		nibble2.reset();
		nibble3.reset();
	}
}

class Am2901x4input {
	Am2901_Dest mi_dest;
	Am2901_Func mi_func;
	Am2901_Src mi_src;
	int C0;
	int D;
	int _OE;
	int regA_addr;
	int regB_addr;
	int SIO0;
	int SIO3;
	int QIO0;
	int QIO3;
}

class Am2901x4output {
	int IZ;
	int IC;
	int IOVR;
	int IN;
	int Y;
	int SIO0;
	int SIO3;
	int QIO0;
	int QIO3;
}