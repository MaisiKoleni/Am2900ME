package net.maisikoleni.am2900me.util;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanPropertyBase;

public class ManualObservable implements Observable, InvalidationListener {

	private final ManualBooleanProperty obs = new ManualBooleanProperty();

	@Override
	public void addListener(InvalidationListener listener) {
		obs.addListener(listener);
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		obs.removeListener(listener);
	}

	public void invalidate() {
		obs.fireValueChangedEvent();
	}

	@Override
	public void invalidated(Observable observable) {
		invalidate();
	}

	private static class ManualBooleanProperty extends BooleanPropertyBase {

		ManualBooleanProperty() {
			super(false);
		}

		@Override
		protected void fireValueChangedEvent() {
			super.fireValueChangedEvent();
		}

		@Override
		public Object getBean() {
			return null;
		}

		@Override
		public String getName() {
			return "";
		}
	}

	public Observable and(Observable o) {
		return combine(this, o);
	}

	public static Observable combine(Observable... observables) {
		return new ManualObservable() {
			{
				for (Observable observable : observables) {
					observable.addListener(this);
				}
			}
		};
	}
}
