package net.maisikoleni.am2900me.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.maisikoleni.am2900me.logic.Am2900Machine;
import net.maisikoleni.am2900me.logic.MicroprogramMemory;

/**
 * JavaFX application entry point.
 *
 * @author MaisiKoleni
 *
 */
public class Main extends Application {

	private final Am2900Machine machine;

	public Main() {
		machine = new Am2900Machine();
	}

	@Override
	public void start(Stage primaryStage) {
		// TODO: EVERYTHING EXPERIMENTAL!!
		primaryStage.setTitle("AM2900ME");
//		Scene scene = new Scene(new MappingPromPanel(machine.getmProm()));
		Scene scene = new Scene(new MicroInstrPanel(new MicroprogramMemory()));
		scene.getStylesheets().add(getClass().getResource("am2900me_style.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setHeight(600);
		primaryStage.setWidth(1050);
		primaryStage.show();
	}

	@SuppressWarnings("javadoc")
	public static void main(String[] args) {
		launch(args);
	}
}
