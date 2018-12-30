package net.maisikoleni.am2900me.logic;

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
		if (address < 0 || address >= 4096)
			throw new IllegalArgumentException("microprogram memory address out of bounds: " + address);
		return instructions[address];
	}

	/**
	 * Sets the instruction at the given address
	 * 
	 * @author MaisiKoleni
	 */
	public void setInstruction(int address, MicroInstruction mi) {
		if (address < 0 || address >= 4096)
			throw new IllegalArgumentException("microprogram memory address out of bounds: " + address);
		instructions[address] = mi;
	}
}
