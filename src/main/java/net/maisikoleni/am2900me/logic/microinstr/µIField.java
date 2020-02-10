package net.maisikoleni.am2900me.logic.microinstr;

public interface µIField {
	String getFullName();

	default boolean isNotRelevantForTum() {
		return false;
	}
}