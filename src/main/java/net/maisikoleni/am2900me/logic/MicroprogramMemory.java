package net.maisikoleni.am2900me.logic;

import net.maisikoleni.am2900me.logic.microinstr.MicroInstruction;
import net.maisikoleni.am2900me.util.BitUtil;

/**
 * The microprogram memory stores 4096 microinstructions that are addressed by
 * the {@link Am2910}s Y output.
 *
 * @author MaisiKoleni
 *
 */
public class MicroprogramMemory {
	private final MicroInstruction[] instructions = new MicroInstruction[4096];

	/**
	 * Returns the microinstruction at the given address
	 * 
	 * @author MaisiKoleni
	 */
	public MicroInstruction getInstruction(int address) {
		if (!BitUtil.isInRange(address, 12))
			throw new IllegalArgumentException("microprogram memory address out of bounds: " + address);
		return instructions[address];
	}

	/**
	 * Sets the instruction at the given address
	 * 
	 * @author MaisiKoleni
	 */
	public void setInstruction(int address, MicroInstruction mi) {
		if (!BitUtil.isInRange(address, 12))
			throw new IllegalArgumentException("microprogram memory address out of bounds: " + address);
		instructions[address] = mi;
	}

	/**
	 * Returns the size of the microprogram memory
	 * 
	 * @author MaisiKoleni
	 */
	public int size() {
		return instructions.length;
	}
}
