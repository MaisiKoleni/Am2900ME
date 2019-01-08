package net.maisikoleni.am2900me.util;

import java.util.concurrent.Callable;
import java.util.function.Function;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Some extensions of {@link Bindings}
 *
 * @author MaisiKoleni
 */
public class AdvBindings {

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
}
