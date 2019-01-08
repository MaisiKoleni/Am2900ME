package net.maisikoleni.am2900me.logic.microinstr;

import net.maisikoleni.am2900me.util.NBitsUInt;

public class RA_ADDR extends NBitsUInt implements µIField {
	static final RA_ADDR DEFAULT = new RA_ADDR(0x0);

	public RA_ADDR(int uint_4bit) {
		super(4, uint_4bit);
	}

	@Override
	public String getFullName() {
		return "Register A Direktadresse";
	}

	@Override
	public RA_ADDR valueOf(int i) {
		return new RA_ADDR(i);
	}
}