package net.maisikoleni.am2900me.util;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

import javafx.beans.Observable;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

/**
 * Some extensions of {@link Bindings}
 *
 * @author MaisiKoleni
 */
public final class AdvBindings {

	private AdvBindings() {
		// utility class only
	}

	/**
	 * Creates a {@link ObjectBinding} from an {@link ObservableValue} T that is
	 * mapped using a given mapping {@link Function} to a type U. The Binding gets
	 * updated when either observable or the mapped {@link ObservableValue} becomes
	 * invalid.
	 * 
	 * @param observable the observable value getting mapped; must not be null
	 * @param mapping    the mapping function gets applied at each invalidation of
	 *                   observable, must be null tolerant if observable returns a
	 *                   null value.
	 * 
	 * @see Bindings#createObjectBinding(Callable, javafx.beans.Observable...)
	 * 
	 * @author MaisiKoleni
	 */
	public static <T, U> ObjectBinding<U> map(ObservableValue<T> observable, Function<T, ObservableValue<U>> mapping) {
		return new ObjectBinding<>() {
			private ObservableValue<U> dependTwo;
			private ObservableList<Observable> depend;
			{
				observable.addListener(e -> updateMapping());
				depend = FXCollections.observableArrayList(observable);
				bind(observable);
				updateMapping();
			}

			@Override
			protected U computeValue() {
				try {
					return dependTwo == null ? null : dependTwo.getValue();
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			private void updateMapping() {
				ObservableValue<U> newDT = mapping.apply(observable.getValue());
				if (newDT != dependTwo) {
					if (dependTwo != null) {
						depend.remove(dependTwo);
						super.unbind(dependTwo);
					}
					if (newDT != null) {
						depend.add(newDT);
						super.bind(newDT);
					}
					dependTwo = newDT;
				}
			}

			@Override
			public void dispose() {
				super.unbind(observable);
				if (dependTwo != null)
					super.unbind(dependTwo);
			}

			@Override
			public ObservableList<?> getDependencies() {
				return FXCollections.unmodifiableObservableList(depend);
			}
		};
	}

	public static BooleanBinding contains(ObservableList<?> list, ObservableValue<?> value) {
		return new BooleanBinding() {

			{
				bind(list, value);
			}

			@Override
			protected boolean computeValue() {
				return list.contains(value.getValue());
			}

			@Override
			public void dispose() {
				super.unbind(list, value);
			}

			@Override
			public ObservableList<?> getDependencies() {
				return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(list, value));
			}
		};
	}

	public static BooleanBinding contains(ObservableSet<?> set, ObservableValue<?> value) {
		return new BooleanBinding() {

			{
				bind(set, value);
			}

			@Override
			protected boolean computeValue() {
				return set.contains(value.getValue());
			}

			@Override
			public void dispose() {
				super.unbind(set, value);
			}

			@Override
			public ObservableList<?> getDependencies() {
				return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(set, value));
			}
		};
	}

	public static <E extends Observable> ObservableList<E> observableChangeArrayList() {
		return FXCollections.observableArrayList(e -> new Observable[] { e });
	}

	public static <E extends Observable> ObservableList<E> observableChangeListOf(List<E> list) {
		return FXCollections.observableList(list, e -> new Observable[] { e });
	}

	public static <E, T> Binding<T> reduce(ObservableList<E> list, T indentity, BiFunction<T, E, T> reduce) {
		return new ObjectBinding<>() {

			{
				bind(list);
			}

			@Override
			protected T computeValue() {
				T res = indentity;
				for (E e : list) {
					res = reduce.apply(res, e);
				}
				return res;
			}

			@Override
			public void dispose() {
				super.unbind(list);
			}

			@Override
			public ObservableList<?> getDependencies() {
				return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(list));
			}
		};
	}

	public static <E> BooleanBinding reduce(ObservableList<E> list, boolean indentity, Predicate<E> test,
			BooleanBinaryOperator combiner) {
		return new BooleanBinding() {

			{
				bind(list);
			}

			@Override
			protected boolean computeValue() {
				boolean res = indentity;
				for (E e : list) {
					res = combiner.apply(res, test.test(e));
				}
				return res;
			}

			@Override
			public void dispose() {
				super.unbind(list);
			}

			@Override
			public ObservableList<?> getDependencies() {
				return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(list));
			}
		};
	}

	public enum BooleanOperation implements BooleanBinaryOperator {
		AND {
			@Override
			public boolean apply(boolean a, boolean b) {
				return a && b;
			}
		},
		NAND {
			@Override
			public boolean apply(boolean a, boolean b) {
				return !(a && b);
			}
		},
		OR {
			@Override
			public boolean apply(boolean a, boolean b) {
				return a || b;
			}
		},
		NOR {
			@Override
			public boolean apply(boolean a, boolean b) {
				return !(a || b);
			}
		},
		XOR {
			@Override
			public boolean apply(boolean a, boolean b) {
				return a ^ b;
			}
		},
		XNOR {
			@Override
			public boolean apply(boolean a, boolean b) {
				return !(a ^ b);
			}
		};
	}

	@FunctionalInterface
	public static interface BooleanBinaryOperator extends BinaryOperator<Boolean> {
		abstract boolean apply(boolean a, boolean b);

		@Override
		default Boolean apply(Boolean t, Boolean u) {
			return apply(t.booleanValue(), u.booleanValue());
		}

		default BooleanBinaryOperator not() {
			return (a, b) -> !apply(a, b);
		}
	}
}
