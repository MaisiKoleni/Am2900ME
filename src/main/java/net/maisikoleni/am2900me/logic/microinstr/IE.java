package net.maisikoleni.am2900me.logic.microinstr;

public enum IE implements µIField {
	IE,
	DIS;

	@Override
	public String getFullName() {
		return "Interrupt Enable";
	}
}