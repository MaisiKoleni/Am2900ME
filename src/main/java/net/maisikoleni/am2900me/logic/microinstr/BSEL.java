package net.maisikoleni.am2900me.logic.microinstr;

public enum BSEL implements µIField {
	IR,
	MR;

	@Override
	public String getFullName() {
		return "Register B Adress-Quelle";
	}
}