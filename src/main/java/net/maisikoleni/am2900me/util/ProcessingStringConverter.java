package net.maisikoleni.am2900me.util;

import java.util.function.UnaryOperator;

import javafx.util.StringConverter;

public class ProcessingStringConverter<T> extends StringConverter<T> {

	private final UnaryOperator<T> processor;
	private final StringConverter<T> conv;

	public ProcessingStringConverter(UnaryOperator<T> processor, StringConverter<T> conv) {
		this.processor = processor;
		this.conv = conv;
	}

	@Override
	public String toString(T object) {
		return conv.toString(processor.apply(object));
	}

	@Override
	public T fromString(String string) {
		return processor.apply(conv.fromString(string));
	}
}
