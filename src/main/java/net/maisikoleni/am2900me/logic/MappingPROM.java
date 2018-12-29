package net.maisikoleni.am2900me.logic;

/**
 * The Mapping PROM maps the {@link InstructionRegister}s OP-Code to the correct
 * microinstruction address. In 'our' case that is always 16 times the OP-Code.
 * That should later be changed to allow programming of the 'P'ROM.
 *
 * @author MaisiKoleni
 *
 */
public class MappingPROM {
	final MappingPROMinput input = new MappingPROMinput();
	final MappingPROMoutput output = new MappingPROMoutput();

	/**
	 * Sets the output miAddress to the OP-Code times 16, or
	 * {@link BitUtil#TRI_STATE_OFF} if _OE is high.<br>
	 * <i>This behaviour (the way of mapping) is subject to change.</i>
	 * 
	 * @author MaisiKoleni
	 */
	public void process() {
		output.miAddress = input._OE == 0 ? input.opCode << 4 : BitUtil.TRI_STATE_OFF;
	}

}

class MappingPROMinput {
	int _OE;
	int opCode;
}

class MappingPROMoutput {
	int miAddress;
}