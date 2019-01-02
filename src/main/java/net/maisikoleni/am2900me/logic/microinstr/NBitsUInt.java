package net.maisikoleni.am2900me.logic.microinstr;

import net.maisikoleni.am2900me.util.BitUtil;

abstract public class NBitsUInt {
	public final int value;
	public final int bits;

	public NBitsUInt(int bits, int value) {
		if (BitUtil.isInRange(bits - 1, 5))
			throw new IllegalArgumentException("illegal number of bits: " + bits);
		if (!BitUtil.isInRange(value, bits))
			throw new IllegalArgumentException(
					"argument must be an " + bits + " bit unsigned integer but was " + value);
		this.bits = bits;
		this.value = value;
	}

	@Override
	public String toString() {
		return BitUtil.toNbitString(value, bits);
	}
}