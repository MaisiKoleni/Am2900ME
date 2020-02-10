package net.maisikoleni.am2900me.util;

import javafx.util.StringConverter;

/**
 * {@link StringConverter} for enum types.
 *
 * @author Christian Femers
 */
public class EnumStringConverter<T extends Enum<T>> extends StringConverter<T> {

	private final T instance;

	/**
	 * Requires an instance of the enum for converting because of type erasure.
	 * 
	 * @author Christian Femers
	 */
	public EnumStringConverter(T instance) {
		this.instance = instance;
	}

	@Override
	public String toString(T object) {
		if (object == null)
			return "";
		return object.name();
	}

	@Override
	public T fromString(String string) {
		if (string == null || string.trim().isEmpty())
			return null;
		return Enum.valueOf(instance.getDeclaringClass(), string);
	}

}
