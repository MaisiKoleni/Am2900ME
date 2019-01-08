package net.maisikoleni.am2900me.ui;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.TableRow;
import net.maisikoleni.am2900me.util.StyleClassProperty;

/**
 * A {@link TableRow} with extended support for {@link StyleClassProperty}
 *
 * @author MaisiKoleni
 *
 */
public class StyledTableRow<T> extends TableRow<T> {

	private final Map<String, StyleClassProperty> styleClassProperies;

	public StyledTableRow() {
		super();
		styleClassProperies = new HashMap<>();
	}

	public BooleanProperty styleClassPropertyFor(String styleClass) {
		return styleClassProperies.computeIfAbsent(styleClass, sc -> new StyleClassProperty(this.getStyleClass(), sc));
	}
}
