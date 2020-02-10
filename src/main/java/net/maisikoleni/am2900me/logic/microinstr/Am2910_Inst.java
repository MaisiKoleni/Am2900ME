package net.maisikoleni.am2900me.logic.microinstr;

public enum Am2910_Inst implements µIField {
	JZ,
	CJS,
	JMAP,
	CJP,
	PUSH,
	JSRP,
	CJV,
	JRP,
	RFCT,
	RPCT,
	CRTN,
	CJPP,
	LDCT,
	LOOP,
	CONT,
	TWB;

	@Override
	public String getFullName() {
		return "Fortschaltebefehle des Am2910";
	}

	@Override
	public boolean isNotRelevantForTum() {
		switch (this) {
		case JZ:
		case JMAP:
		case CJP:
		case CONT:
			return false;
		default:
			return true;
		}
	}
}