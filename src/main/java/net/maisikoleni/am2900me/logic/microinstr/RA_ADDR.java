package net.maisikoleni.am2900me.logic.microinstr;

import net.maisikoleni.am2900me.util.NBitsUInt;

public class RA_ADDR extends NBitsUInt implements µIField {
	static final RA_ADDR DEFAULT = new RA_ADDR(0x00);

	public RA_ADDR(int uint_8bit) {
		super(8, uint_8bit);
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