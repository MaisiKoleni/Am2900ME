package net.maisikoleni.am2900me.logic.microinstr;

public class RB_ADDR extends NBitsUInt implements µIField {
	static final RB_ADDR NONE = new RB_ADDR(0x00);

	public RB_ADDR(int uint_8bit) {
		super(8, uint_8bit);
	}

	@Override
	public String getFullName() {
		return "Register B Direktadresse";
	}
}