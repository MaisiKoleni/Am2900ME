package net.maisikoleni.am2900me.util;

import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

/**
 * Like {@link IntegerStringConverter} but uses a fixed length hexadecimal
 * representation, more than that, it also casts the integer to the predefined
 * bit length to ensure the integer always stays in this predefined length/size.
 *
 * @author MaisiKoleni
 */
public class HexIntStringConverter extends StringConverter<Integer> {

	public static final HexIntStringConverter INT_4 = new HexIntStringConverter(1);
	public static final HexIntStringConverter INT_8 = new HexIntStringConverter(2);
	public static final HexIntStringConverter INT_12 = new HexIntStringConverter(3);
	public static final HexIntStringConverter INT_16 = new HexIntStringConverter(4);
	public static final HexIntStringConverter INT_20 = new HexIntStringConverter(5);
	public static final HexIntStringConverter INT_24 = new HexIntStringConverter(6);
	public static final HexIntStringConverter INT_28 = new HexIntStringConverter(7);
	public static final HexIntStringConverter INT_32 = new HexIntStringConverter(8);

	private final String format;
	private final int mask;

	private HexIntStringConverter(int nibbles) {
		this.mask = 0xFF_FF_FF_FF >>> (32 - nibbles * 4);
		this.format = "0x%0" + nibbles + "X";
	}

	@Override
	public Integer fromString(String value) {
		if (value == null || value.trim().isEmpty())
			return 0;
		return cast(Integer.decode(value.trim()));
	}

	@Override
	public String toString(Integer value) {
		if (value == null)
			return String.format(format, 0);
		return String.format(format, cast(value));
	}

	public int cast(int value) {
		return value & mask;
	}

	public static HexIntStringConverter forNibbles(int n) {
		switch (n) {
		case 1:
			return INT_4;
		case 2:
			return INT_8;
		case 3:
			return INT_12;
		case 4:
			return INT_16;
		case 5:
			return INT_20;
		case 6:
			return INT_24;
		case 7:
			return INT_28;
		case 8:
			return INT_32;
		default:
			throw new IllegalArgumentException("invalid number of int nibbles: " + n);
		}
	}
}