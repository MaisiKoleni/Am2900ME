package net.maisikoleni.am2900me.logic;

import net.maisikoleni.am2900me.logic.microinstr.Am2901_Dest;
import net.maisikoleni.am2900me.logic.microinstr.Am2901_Func;
import net.maisikoleni.am2900me.logic.microinstr.Am2901_Src;
import net.maisikoleni.am2900me.util.BitUtil;

/**
 * The Am2901 - a "Four-Bit Bipolar Microprocessor Slice"<br>
 * The ALU of a Am2900 Machine, multiple of which are combined to work with 4
 * times n Bits; e.g. 16, 32 or 48.
 *
 * @author Christian Femers
 *
 */
public class Am2901 {
	private int[] registers4bit = new int[16];
	private int Q;
	private int fSignal;

	final Am2901input input = new Am2901input();
	final Am2901output output = new Am2901output();

	/**
	 * Does everything <b>except</b> storing to the registers and the shifting of
	 * the RAM and Q registers
	 *
	 * @author Christian Femers
	 */
	@SuppressWarnings("incomplete-switch")
	public Am2901output processStep1() {
		Am2901_Func f = input.mi_func;
		Am2901_Dest d = input.mi_dest;
		Am2901_Src s = input.mi_src;
		int R = getR(s);
		int S = getS(s);
		fSignal = alu(f, R, S);
		// (Re-)set the shifting outputs
		output.Q0 = output.Q3 = BitUtil.TRI_STATE_OFF;
		output.RAM0 = output.RAM3 = BitUtil.TRI_STATE_OFF;
		if (input._OE == 0)
			output.Y = fSignal;
		else
			output.Y = BitUtil.TRI_STATE_OFF;
		switch (d) {
		case RAMA:
			if (input._OE == 0)
				output.Y = registers4bit[input.regA_addr];
			break;
		case RAMQD:
			output.Q0 = Q & 0b0001; //$FALL-THROUGH$
		case RAMD:
			output.RAM0 = fSignal & 0b0001;
			break;
		case RAMQU:
			output.Q3 = Q >> 3; //$FALL-THROUGH$
		case RAMU:
			output.RAM3 = fSignal >> 3;
			break;
		}
		return output;
	}

	/**
	 * Stores the (shifted) F-signal and the (shifted) Q-signal to the RB / Q
	 * registers, dependent on the destination.
	 *
	 * @author Christian Femers
	 */
	public Am2901output processStep2() {
		Am2901_Dest d = input.mi_dest;
		switch (d) {
		case QREG:
			Q = fSignal;
			break;
		case NOP:
			break;
		case RAMA:
		case RAMF:
			registers4bit[input.regB_addr] = fSignal;
			break;
		case RAMQD:
			if (input.Q3 == BitUtil.TRI_STATE_OFF)
				throw new IllegalStateException("Illegal Shift input state for Q3: " + BitUtil.TRI_STATE_OFF);
			Q = (Q >> 1) | (input.Q3 << 3);
			//$FALL-THROUGH$
		case RAMD:
			if (input.RAM3 == BitUtil.TRI_STATE_OFF)
				throw new IllegalStateException("Illegal Shift input state for RAM3: " + BitUtil.TRI_STATE_OFF);
			registers4bit[input.regB_addr] = (fSignal >> 1) | (input.RAM3 << 3);
			break;
		case RAMQU:
			if (input.Q0 == BitUtil.TRI_STATE_OFF)
				throw new IllegalStateException("Illegal Shift input state for Q0: " + BitUtil.TRI_STATE_OFF);
			Q = (Q << 1 & 0b1111) | input.Q0; //$FALL-THROUGH$
		case RAMU:
			if (input.RAM0 == BitUtil.TRI_STATE_OFF)
				throw new IllegalStateException("Illegal Shift input state for RAM0: " + BitUtil.TRI_STATE_OFF);
			registers4bit[input.regB_addr] = (fSignal << 1 & 0b1111) | input.RAM0;
			break;
		default:
			throw new IllegalArgumentException("unknown am2901 destination: " + d);
		}
		return output;
	}

	private int getR(Am2901_Src src) {
		switch (src) {
		case AB:
		case AQ:
			return registers4bit[input.regA_addr];
		case DA:
		case DQ:
		case DZ:
			return input.D;
		case ZA:
		case ZB:
		case ZQ:
			return 0b0000;
		default:
			throw new IllegalArgumentException("unknown am2901 source: " + src);
		}
	}

	private int getS(Am2901_Src src) {
		switch (src) {
		case DA:
		case ZA:
			return registers4bit[input.regA_addr];
		case AB:
		case ZB:
			return registers4bit[input.regB_addr];
		case AQ:
		case DQ:
		case ZQ:
			return Q;
		case DZ:
			return 0b0000;
		default:
			throw new IllegalArgumentException("unknown am2901 source: " + src);
		}
	}

	private int alu(Am2901_Func fun, int R, int S) {
		switch (fun) {
		case ADD:
			return add(R, S);
		case SUBR:
			return add(S, ~R);
		case SUBS:
			return add(R, ~S);
		case OR:
			return or(R, S);
		case AND:
			return and(R, S);
		case NOTRS:
			return and(~R, S);
		case EXOR:
			return xnor(~R, S);
		case EXNOR:
			return xnor(R, S);
		default:
			throw new IllegalArgumentException("unknown am2901 function: " + fun);
		}
	}

	// All following operations are implemented according to the Am2900 Family Data
	// Book, page 2-3, 2-6

	private int add(int R, int S) {
		// subtraction is R + ~S + input.Cn, which is the same as A - B - (1 - input.Cn)
		int f = R + S + input.Cn;
		int C3 = C3(R, S, input.Cn);
		int C4 = C4(R, S, input.Cn);
		output.Cn4 = C4;
		output.OVR = C3 ^ C4;
		output.F0 = F0(f);
		output.F3 = F3(f);
		output._G = 1 - BitUtil.or(C4 & 0b1_1110);
		output._P = 1 - (R | S) / 0b1111;
		return (f & 0b1111);
	}

	private int or(int R, int S) {
		int f = R | S;
		int P3210 = f / 0b1111;
		output.Cn4 = 1 - P3210 | input.Cn;
		output.OVR = output.Cn4;
		output.F0 = F0(f);
		output.F3 = F3(f);
		output._G = P3210;
		output._P = 0;
		return (f & 0b1111);
	}

	private int and(int R, int S) {
		int f = R & S;
		output.Cn4 = 1 - BitUtil.or(f | input.Cn);
		output.OVR = output.Cn4;
		output.F0 = F0(f);
		output.F3 = F3(f);
		output._G = 1 - BitUtil.or(f);
		output._P = 0;
		return (f & 0b1111);
	}

	private int xnor(int R, int S) {
		int f = ~(R ^ S);
		int p = (R & S) | 0b1111_0000;
		int g = (R | S);
		int gp = ((g | 0b0001) & (p >> 1) & (p >> 2) & (p >> 3) & (p | 0b1110));
		output.Cn4 = 1 - BitUtil.or((gp & (g | 0b1110)) | (gp & 0b0001 & (1 - input.Cn)));
		int notp = (~(R & S) << 1) | 0b0001;
		int notg3 = ~g | 0b1111_0000;
		int notg2 = notg3 | 0b1000;
		output.OVR = BitUtil.or(0b1111 & notp & notg2 & (notg2 >> 1) & (notg2 >> 2) & (input.Cn | 0b1110)) ^ BitUtil
				.or(0b1_1111 & notp & notg3 & (notg3 >> 1) & (notg3 >> 2) & (notg3 >> 3) & (input.Cn | 0b1_1110));
		output.F0 = F0(f);
		output.F3 = F3(f);
		output._G = BitUtil.or(gp);
		output._P = BitUtil.or(g);
		return (f & 0b1111);
	}

	private static int F0(int f) {
		return 1 - BitUtil.or(f & 0b1111);
	}

	private static int F3(int f) {
		return (f & 0b1000) >> 3;
	}

	private static int C3(int r, int s, int cn) {
		int p = (r | s) | 0b0011_1000;
		int g = ((r & s) << 1) | 0b0001;
		int cnb = cn | 0b1110;
		int c3 = cnb & g & p & (p >> 1) & (p >> 2);
		return BitUtil.or(c3);
	}

	private static int C4(int r, int s, int cn) {
		int p = (r | s) | 0b1111_0000;
		int g = ((r & s) << 1) | 0b0_0001;
		int cnb = cn | 0b1_1110;
		int c4 = cnb & g & p & (p >> 1) & (p >> 2) & (p >> 3);
		return BitUtil.or(c4);
	}

	public final int getRegisters4bit(int addr) {
		return registers4bit[addr];
	}

	public final int getQ() {
		return Q;
	}

	public final void setRegisters4bit(int addr, int register4bit) {
		this.registers4bit[addr] = register4bit;
	}

	public final void setQ(int q) {
		Q = q;
	}

	public void reset() {
		for (int i = 0; i < registers4bit.length; i++) {
			setRegisters4bit(i, 0);
		}
		setQ(0);
	}

	public static class Am2901input {
		Am2901_Dest mi_dest;
		Am2901_Func mi_func;
		Am2901_Src mi_src;
		int Cn;
		int D;
		int _OE;
		int regA_addr;
		int regB_addr;
		int Q0;
		int Q3;
		int RAM0;
		int RAM3;
	}

	public static class Am2901output {
		int Cn4;
		int F3;
		int OVR;
		int F0;
		int Y;
		int _P;
		int _G;
		int Q0;
		int Q3;
		int RAM0;
		int RAM3;
	}
}
