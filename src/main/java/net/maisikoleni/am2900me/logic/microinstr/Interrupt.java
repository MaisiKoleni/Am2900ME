package net.maisikoleni.am2900me.logic.microinstr;

public class Interrupt extends NBitsUInt implements µIField {
	static final Interrupt NONE = new Interrupt(0x0);

	public Interrupt(int uint_4bit) {
		super(4, uint_4bit);
	}

	@Override
	public String getFullName() {
		return "Interruptsteuerung";
	}
}