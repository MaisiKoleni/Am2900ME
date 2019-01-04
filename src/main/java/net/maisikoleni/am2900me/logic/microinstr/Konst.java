package net.maisikoleni.am2900me.logic.microinstr;

import net.maisikoleni.am2900me.util.NBitsUInt;

public class Konst extends NBitsUInt implements µIField {
	static final Konst DEFAULT = new Konst(0x0000);

	public Konst(int uint_16bit) {
		super(16, uint_16bit);
	}

	@Override
	public String getFullName() {
		return "Konstantenfeld";
	}

	@Override
	public Konst valueOf(int i) {
		return new Konst(i);
	}
}