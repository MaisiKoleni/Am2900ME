package net.maisikoleni.am2900me.logic.microinstr;

public enum Am2901_Func implements µIField {
	ADD,
	SUBR,
	SUBS,
	OR,
	AND,
	NOTRS,
	EXOR,
	EXNOR;

	@Override
	public String getFullName() {
		return "ALU Funktionen";
	}
}