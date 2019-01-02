package net.maisikoleni.am2900me.logic.microinstr;

public class BAR extends NBitsUInt implements µIField {
	static final BAR NONE = new BAR(0x000);

	public BAR(int uint_12bit) {
		super(12, uint_12bit);
	}

	@Override
	public String getFullName() {
		return "Direktadressfeld";
	}
}