
/**
 * Am2900ME v0.1.1
 * 
 * @author MaisiKoleni
 */
module am2900me {
	exports net.maisikoleni.am2900me.logic;
	exports net.maisikoleni.am2900me.ui;
	exports net.maisikoleni.am2900me.logic.microinstr;
	exports net.maisikoleni.am2900me.util;

	requires transitive java.prefs;
	requires transitive javafx.base;
	requires transitive javafx.controls;
	requires transitive javafx.graphics;
}