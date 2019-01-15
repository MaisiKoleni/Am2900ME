package net.maisikoleni.am2900me.ui;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import net.maisikoleni.am2900me.logic.Am2900Machine;
import net.maisikoleni.am2900me.util.ManualObservable;

public class ObservableAm2900Machine extends Am2900Machine implements Observable {

	private ManualObservable obsHelper = new ManualObservable();

	@Override
	public void executeNext() {
		super.executeNext();
		obsHelper.invalidate();
	}

	public void executeNextN(int n) {
		for (int i = 0; i < n; i++) {
			super.executeNext();
		}
		obsHelper.invalidate();
	}

	@Override
	public void addListener(InvalidationListener listener) {
		obsHelper.addListener(listener);
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		obsHelper.removeListener(listener);
	}

	@Override
	public void reset() {
		super.reset();
		obsHelper.invalidate();
	}
}
