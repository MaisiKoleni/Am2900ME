package net.maisikoleni.am2900me.logic.microinstr;

import net.maisikoleni.am2900me.util.NBitsUInt;

public class RB_ADDR extends NBitsUInt implements µIField {
	static final RB_ADDR DEFAULT = new RB_ADDR(0x00);

	public RB_ADDR(int uint_8bit) {
		super(8, uint_8bit);
	}

	@Override
	public String getFullName() {
		return "Register B Direktadresse";
	}

	@Override
	public RB_ADDR valueOf(int i) {
		return new RB_ADDR(i);
	}
}