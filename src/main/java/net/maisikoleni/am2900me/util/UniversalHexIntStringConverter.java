package net.maisikoleni.am2900me.util;

import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

import javafx.util.StringConverter;

/**
 * Like {@link HexIntStringConverter} but gets supplied with from and to int
 * functions to convert any type that can be represented by an integer.
 *
 * @author MaisiKoleni
 */
public class UniversalHexIntStringConverter<T> extends StringConverter<T> {
	private final ToIntFunction<T> toInt;
	private final IntFunction<T> fromInt;
	private final HexIntStringConverter conv;

	public UniversalHexIntStringConverter(ToIntFunction<T> toInt, IntFunction<T> fromInt, HexIntStringConverter conv) {
		this.toInt = toInt;
		this.fromInt = fromInt;
		this.conv = conv;
	}

	@Override
	public String toString(T object) {
		return conv.toString(toInt.applyAsInt(object));
	}

	@Override
	public T fromString(String string) {
		return fromInt.apply(conv.fromString(string));
	}
}