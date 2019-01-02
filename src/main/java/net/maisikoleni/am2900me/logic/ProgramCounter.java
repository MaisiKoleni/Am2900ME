package net.maisikoleni.am2900me.logic;

import net.maisikoleni.am2900me.logic.microinstr._BZ_EA;
import net.maisikoleni.am2900me.logic.microinstr._BZ_ED;
import net.maisikoleni.am2900me.logic.microinstr._BZ_INC;
import net.maisikoleni.am2900me.logic.microinstr._BZ_LD;
import net.maisikoleni.am2900me.util.BitUtil;

/**
 * The machines program counter.
 *
 * @author MaisiKoleni
 *
 */
public class ProgramCounter {
	private int pc;

	final ProgramCounterInput input = new ProgramCounterInput();
	final ProgramCounterOutput output = new ProgramCounterOutput();

	/**
	 * Does with the program counter whatever is specified i the 2. to 5. bit of the
	 * microinstruction. May not respond correctly to invalid inputs.
	 * 
	 * @author MaisiKoleni
	 */
	public void process() {
		boolean loadD = input._BZ_LD == _BZ_LD.L;
		boolean writeA = input._BZ_EA == _BZ_EA.E;
		boolean writeD = input._BZ_ED == _BZ_ED.E;
		if (loadD && writeD)
			throw new IllegalArgumentException("cannot load and write data at the same time");
		if (loadD) {
			if (input.data == BitUtil.TRI_STATE_OFF)
				throw new IllegalArgumentException("cannot load pc with no data signal");
			pc = input.data;
		}
		if (writeA)
			output.addr = pc;
		else
			output.addr = BitUtil.TRI_STATE_OFF;
		if (writeD)
			output.data = pc;
		else
			output.data = BitUtil.TRI_STATE_OFF;
		if (input._BZ_INC == _BZ_INC.I)
			pc++;
	}
}

class ProgramCounterInput {
	int data;
	_BZ_EA _BZ_EA;
	_BZ_INC _BZ_INC;
	_BZ_ED _BZ_ED;
	_BZ_LD _BZ_LD;
}

class ProgramCounterOutput {
	int data;
	int addr;
}