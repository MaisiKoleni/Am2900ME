package net.maisikoleni.am2900me.logic.microinstr;

public enum Am2904_Carry implements µIField {
	CI0,
	CI1,
	CX,
	USE_SR;

	@Override
	public String getFullName() {
		return "Instruktionsbit des Am2904 - Übertragssteuerung";
	}
}