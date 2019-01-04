package net.maisikoleni.am2900me.util;

abstract public class NBitsUInt {
	public final int value;
	public final int bits;

	public NBitsUInt(int bits, int value) {
		if (!BitUtil.isInRange(bits - 1, 5))
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bits;
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof NBitsUInt))
			return false;
		NBitsUInt other = (NBitsUInt) obj;
		if (bits != other.bits)
			return false;
		if (value != other.value)
			return false;
		return true;
	}

	/**
	 * Returns an instance of the class (NBitsUInt sub-class) representing the given
	 * integer value.
	 */
	public abstract NBitsUInt valueOf(int i);
}