package net.maisikoleni.am2900me.util;

import java.util.function.BiPredicate;

import javafx.beans.Observable;
import javafx.scene.control.Cell;
import javafx.util.Callback;

public class StyleOnChangeAutoReset<S, P, R extends Cell<S>> implements Callback<P, R> {

	private final Callback<P, R> callback;
	private final String styleClass;
	private final Observable obs;
	private final BiPredicate<S, S> condition;

	public StyleOnChangeAutoReset(Callback<P, R> callback, String styleClass, Observable obs,
			BiPredicate<S, S> condition) {
		this.callback = callback;
		this.styleClass = styleClass;
		this.obs = obs;
		this.condition = condition;
	}

	public StyleOnChangeAutoReset(Callback<P, R> callback, String styleClass, Observable obs) {
		this(callback, styleClass, obs, null);
	}

	@Override
	public R call(P param) {
		final R r = callback.call(param);
		r.itemProperty().addListener((obs, o, n) -> {
			if (condition == null || condition.test(o, n))
				r.getStyleClass().add(styleClass);
		});
		obs.addListener(obs -> r.getStyleClass().remove(styleClass));
		return r;
	}

	public final StyleOnChangeAutoReset<S, P, R> withCondition(BiPredicate<S, S> condition) {
		return new StyleOnChangeAutoReset<>(callback, styleClass, obs, condition);
	}
}
