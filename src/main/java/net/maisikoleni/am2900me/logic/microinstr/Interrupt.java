package net.maisikoleni.am2900me.logic.microinstr;

import net.maisikoleni.am2900me.util.NBitsUInt;

public class Interrupt extends NBitsUInt implements µIField {
	static final Interrupt DEFAULT = new Interrupt(0x0);

	public Interrupt(int uint_4bit) {
		super(4, uint_4bit);
	}

	@Override
	public String getFullName() {
		return "Interruptsteuerung";
	}

	@Override
	public Interrupt valueOf(int i) {
		return new Interrupt(i);
	}
}