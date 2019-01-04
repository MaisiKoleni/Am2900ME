package net.maisikoleni.am2900me.logic.microinstr;

import net.maisikoleni.am2900me.util.NBitsUInt;

public class Am2904_Shift extends NBitsUInt implements µIField {
	static final Am2904_Shift DEFAULT = new Am2904_Shift(0x0);

	public Am2904_Shift(int uint_4bit) {
		super(4, uint_4bit);
	}

	@Override
	public String getFullName() {
		return "Instruktionsbit des Am2904 - Schiebesteuerung";
	}

	@Override
	public Am2904_Shift valueOf(int i) {
		return new Am2904_Shift(i);
	}
}