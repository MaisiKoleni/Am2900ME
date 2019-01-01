package net.maisikoleni.am2900me.logic;

/**
 * The Mapping PROM maps the {@link InstructionRegister}s 8 bit OP-Code to the
 * correct microinstruction address.
 *
 * @author MaisiKoleni
 *
 */
public class MappingPROM {
	final MappingPROMinput input = new MappingPROMinput();
	final MappingPROMoutput output = new MappingPROMoutput();

	int[] addressTable = new int[256];

	/**
	 * The default mapping maps each OP-Code to the OP-Code * 16 microinstruction
	 * address.
	 */
	public MappingPROM() {
		for (int i = 0; i < addressTable.length; i++) {
			addressTable[i] = i << 4;
		}
	}

	/**
	 * Sets the output miAddress according to the entry in the address table, or
	 * {@link BitUtil#TRI_STATE_OFF} if _OE is high.<br>
	 * 
	 * @author MaisiKoleni
	 */
	public void process() {
		output.miAddress = input._OE == 0 ? addressTable[input.opCode & 0xFF] : BitUtil.TRI_STATE_OFF;
	}

	/**
	 * Set the 12 bit microinstruction address for the given 8 bit OP-Code
	 * 
	 * @author MaisiKoleni
	 */
	public void set(int opCode, int miAddress) {
		if (!BitUtil.isInRange(opCode, 8))
			throw new IllegalArgumentException("Invalid op code range: " + opCode);
		if (!BitUtil.isInRange(miAddress, 12))
			throw new IllegalArgumentException("Invalid microinstruction address range: " + miAddress);
		addressTable[opCode] = miAddress;
	}

	/**
	 * Returns the microinstruction address for the given 8 bit OP-Code
	 * 
	 * @author MaisiKoleni
	 */
	public int get(int opCode) {
		if (!BitUtil.isInRange(opCode, 8))
			throw new IllegalArgumentException("Invalid op code range: " + opCode);
		return addressTable[opCode];
	}

	/**
	 * Returns the size of the address table; usually 256.
	 * 
	 * @author MaisiKoleni
	 */
	public int size() {
		return addressTable.length;
	}
}

class MappingPROMinput {
	int _OE;
	int opCode;
}

class MappingPROMoutput {
	int miAddress;
}