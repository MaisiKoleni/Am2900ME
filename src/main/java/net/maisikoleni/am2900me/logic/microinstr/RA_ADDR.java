package net.maisikoleni.am2900me.logic.microinstr;

public class RA_ADDR extends NBitsUInt implements µIField {
	static final RA_ADDR NONE = new RA_ADDR(0x00);

	public RA_ADDR(int uint_8bit) {
		super(8, uint_8bit);
	}

	@Override
	public String getFullName() {
		return "Register A Direktadresse";
	}
}