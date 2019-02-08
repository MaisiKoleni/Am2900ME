package net.maisikoleni.am2900me.logic.microinstr;

public enum Am2901_Dest implements µIField {
	QREG,
	NOP,
	RAMA,
	RAMF,
	RAMQD,
	RAMD,
	RAMQU,
	RAMU;

	public boolean doesShift() {
		return ordinal() >= 4;
	}

	public int getShiftDir() {
		return doesShift() ? (ordinal() < 6 ? -1 : 1) : 0;
	}

	public int getI7() {
		return this.ordinal() >> 1 & 1;
	}

	@Override
	public String getFullName() {
		return "ALU Zielsteuerung";
	}
}