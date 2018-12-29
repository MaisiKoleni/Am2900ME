package net.maisikoleni.am2900me.logic;

/**
 * Holds the current machine instruction.
 *
 * @author MaisiKoleni
 *
 */
public class InstructionRegister {
	final InstructionRegisterInput input = new InstructionRegisterInput();
	final InstructionRegisterOutput output = new InstructionRegisterOutput();

	private int instruction;

	/**
	 * Loads the next instruction from the data bus, if _IR_LD is low.
	 * 
	 * @author MaisiKoleni
	 */
	public void process() {
		// assuming no one changes the output :)
		if (input._IR_LD != _IR_LD.L)
			return;
		if (input.data == BitUtil.TRI_STATE_OFF)
			throw new IllegalStateException("cannot write instruction register from data bus, no data signals");
		instruction = input.data;
		output.opCode = instruction >>> 16;
		output.regAAddr = (instruction >>> 4) & 0b0111;
		output.regBAddr = instruction & 0b0111;
	}
}

class InstructionRegisterInput {
	_IR_LD _IR_LD;
	int data;
}

class InstructionRegisterOutput {
	int opCode;
	int regAAddr;
	int regBAddr;
}