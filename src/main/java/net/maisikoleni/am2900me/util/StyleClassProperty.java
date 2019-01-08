package net.maisikoleni.am2900me.util;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;

/**
 * Allows observing, toggling or binding of a single style class.
 *
 * @author MaisiKoleni
 */
public class StyleClassProperty extends SimpleBooleanProperty {

	private final ObservableList<String> styleClassList;
	private final String styleClass;
	// Still not completely thread safe ?
	private volatile boolean internalChange;
	private final InvalidationListener listener;

	public StyleClassProperty(ObservableList<String> styleClassList, String styleClass) {
		super();
		this.styleClassList = styleClassList;
		this.styleClass = styleClass;
		this.listener = obs -> readFromList();
		this.internalChange = false;
	}

	@Override
	protected void invalidated() {
		super.invalidated();
		writeToList();
	}

	private void writeToList() {
		if (internalChange)
			return;
		boolean newVal = this.get();
		if (styleClassList.contains(styleClass) != newVal) {
			internalChange = true;
			if (newVal)
				styleClassList.add(styleClass);
			else
				styleClassList.remove(styleClass);
			internalChange = false;
		}
	}

	private void readFromList() {
		if (internalChange)
			return;
		boolean contains = styleClassList.contains(styleClass);
		if (contains != this.get()) {
			if (this.isBound())
				throw new RuntimeException(
						"A bound value cannot be set (to " + contains + ", styleClass=\"" + styleClass + "\")");
			internalChange = true;
			this.set(contains);
			internalChange = false;
		}
	}

	/**
	 * Connects the StyleClassProperty to the list again and updates the list's
	 * state if the property is bound, otherwise the property gets updated with the
	 * list's current state
	 * 
	 * @author MaisiKoleni
	 */
	public void connectToList() {
		styleClassList.addListener(listener);
		if (this.isBound())
			writeToList();
		else
			readFromList();
	}

	/**
	 * The Property will not reflect the status of the style class in the list
	 * anymore by removing the listener.<br>
	 * Allows the {@link StyleClassProperty} to be garbage collected (if is is not
	 * referenced anywhere else).
	 * 
	 * @author MaisiKoleni
	 */
	public void disconnectFromList() {
		styleClassList.removeListener(listener);
	}
}
