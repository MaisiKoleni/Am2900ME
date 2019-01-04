package net.maisikoleni.am2900me.logic.microinstr;

import net.maisikoleni.am2900me.util.NBitsUInt;

public class BAR extends NBitsUInt implements µIField {
	static final BAR DEFAULT = new BAR(0x000);

	public BAR(int uint_12bit) {
		super(12, uint_12bit);
	}

	@Override
	public String getFullName() {
		return "Direktadressfeld";
	}

	@Override
	public BAR valueOf(int i) {
		return new BAR(i);
	}
}