package net.maisikoleni.am2900me.logic.microinstr;

public enum _MWE implements µIField {
	W,
	R;

	@Override
	public String getFullName() {
		return "¬ Memory-Write-Enable";
	}
}