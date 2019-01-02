package net.maisikoleni.am2900me.logic.microinstr;

public enum Am2901_Src implements µIField {
	AQ,
	AB,
	ZQ,
	ZB,
	ZA,
	DA,
	DQ,
	DZ;

	@Override
	public String getFullName() {
		return "ALU Quelloperanden";
	}
}