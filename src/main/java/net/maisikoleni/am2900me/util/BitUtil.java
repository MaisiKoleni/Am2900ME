package net.maisikoleni.am2900me.util;

/**
 * Utility class for bit-operations and similar
 *
 * @author MaisiKoleni
 *
 */
public class BitUtil {

	private BitUtil() {
	}

	/**
	 * Represents the OFF / high impedance state of a circuit connected to a
	 * tri-state bus.
	 * 
	 * @author MaisiKoleni
	 */
	public static final int TRI_STATE_OFF = -1;

	/**
	 * The bit of operations returning undefined results
	 *
	 * @author MaisiKoleni
	 */
	public static final int UNDEFINED = -1;

	/**
	 * Returns 1 if the bitVector int has one bit set to 1, 0 otherwise
	 * 
	 * @author MaisiKoleni
	 */
	public static int or(int bitVector) {
		return bitVector == 0 ? 0 : 1;
	}

	/**
	 * Transforms a 4 bit signed integer (-8 to 7) to a int representing the same
	 * number. (Adding leading 1-bits if the 4 bit int is negative)
	 * 
	 * @author MaisiKoleni
	 */
	public static int signed4ToSigned32(int signed4bit) {
		if ((signed4bit & 0b1000) > 0)
			return signed4bit | 0xFF_FF_FF_F0;
		return signed4bit & 0x00_00_00_0F;
	}

	/**
	 * Transforms a 16 bit signed integer (-32768 to 32767 - a short) to a int
	 * representing the same number. (Adding leading 1-bits if the 16 bit int is
	 * negative)
	 * 
	 * @author MaisiKoleni
	 */
	public static int signed16ToSigned32(int signed16bit) {
		return (short) signed16bit;
	}

	/**
	 * Special case for four bits of {@link #singleNBits(int, int)}
	 * 
	 * @author MaisiKoleni
	 */
	public static int[] single4Bits(int fourBits) {
		return new int[] { fourBits & 1, (fourBits >> 1) & 1, (fourBits >> 2) & 1, (fourBits >> 3) & 1 };
	}

	/**
	 * Separates the last n single bits in the bitVerctor-int into an array
	 * containing those bits (1 or 0). The least significant bit comes first.<br>
	 * Inverse of {@link #toBitVector(int...)}
	 * 
	 * @author MaisiKoleni
	 */
	public static int[] singleNBits(int bitVector, int n) {
		int temp = bitVector;
		int[] bits = new int[n];
		for (int i = 0; i < bits.length; i++) {
			bits[i] = temp & 1;
			temp >>>= 1;
		}
		return bits;
	}

	/**
	 * Combines the bits.length bits into an integer. The first bit in the array ist
	 * the last (least significant) bit in the resulting int.<br>
	 * Inverse of {@link #singleNBits(int, int)}
	 * 
	 * @author MaisiKoleni
	 */
	public static int toBitVector(int... bits) {
		int res = 0;
		for (int i = bits.length - 1; i >= 0; i--) {
			res |= bits[i];
			res <<= 1;
		}
		return res;
	}

	/**
	 * Convenience method for array creation by using varargs.
	 * 
	 * @author MaisiKoleni
	 */
	public static int[] toBitArray(int... bits) {
		return bits;
	}

	/**
	 * Transforms the last four bits of an int to a string that contains the binary
	 * ('1' and '0') representation of the 4 bits
	 * 
	 * @author MaisiKoleni
	 */
	public static String to4bitBin(int x) {
		StringBuilder sb = new StringBuilder(4);
		sb.append((x & 0b1000) == 0 ? '0' : '1');
		sb.append((x & 0b0100) == 0 ? '0' : '1');
		sb.append((x & 0b0010) == 0 ? '0' : '1');
		sb.append((x & 0b0001) == 0 ? '0' : '1');
		return sb.toString();
	}

	/**
	 * Transforms the last n bits of an int to a string that contains the binary
	 * ('1' and '0') representation of the n bits
	 * 
	 * @author MaisiKoleni
	 */
	public static String toNbitString(int x, int n) {
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			sb.append((x >> i) & 1);
		}
		return sb.reverse().toString();
	}

	/**
	 * Tests if only the lowest n bits are set, if any. Tests basically if the value
	 * is in the range [0, 2^n)
	 * 
	 * @author MaisiKoleni
	 */
	public static boolean isInRange(int value, int nBits) {
		return (value >>> nBits) == 0;
	}
}
