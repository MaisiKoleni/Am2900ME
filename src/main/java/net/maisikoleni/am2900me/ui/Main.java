package net.maisikoleni.am2900me.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * JavaFX application entry point.
 *
 * @author MaisiKoleni
 *
 */
public class Main extends Application {

	public static final String DESCRIPTOR = "Am2900ME (v0.1.1)";

	private final ObservableAm2900Machine machine;
	private final List<String> stylesheets = List.of(
			getClass().getResource("/style/am2900me_style.css").toExternalForm(),
			"com/sun/javafx/scene/control/skin/modena/modena-embedded-performance.css");
	private final List<Image> icons = getImages("/icons/µIcon16.png", "/icons/µIcon32.png", "/icons/µIcon128.png");

	public Main() {
		machine = new ObservableAm2900Machine();
	}

	@Override
	public void start(Stage s) {
		s.setTitle(DESCRIPTOR + " - Microinstructions");
		s.getIcons().addAll(icons);
		Scene scene = new Scene(new MicroInstrPanel(machine));
		scene.getStylesheets().addAll(stylesheets);
		s.setScene(scene);
		s.setHeight(800);
		s.setWidth(1870);
		s.show();
		startMPROM();
		startRAM();
		startRegStatus();
	}

	private void startMPROM() {
		Stage s = new Stage();
		s.getIcons().addAll(icons);
		s.setTitle(DESCRIPTOR + " - Mapping PROM");
		Scene scene = new Scene(new MappingPromPanel(machine.getmProm()));
		scene.getStylesheets().addAll(stylesheets);
		s.setScene(scene);
		s.setHeight(480);
		s.setWidth(450);
		s.show();
	}

	private void startRAM() {
		Stage s = new Stage();
		s.getIcons().addAll(icons);
		s.setTitle(DESCRIPTOR + " - Machine RAM");
		Scene scene = new Scene(new RAMPanel(machine));
		scene.getStylesheets().addAll(stylesheets);
		s.setScene(scene);
		s.setHeight(600);
		s.setWidth(1050);
		s.show();
	}

	private void startRegStatus() {
		Stage s = new Stage();
		s.getIcons().addAll(icons);
		s.setTitle(DESCRIPTOR + " - Registers and Status");
		Scene scene = new Scene(new RegisterStatusPanel(machine));
		scene.getStylesheets().addAll(stylesheets);
		s.setScene(scene);
		s.setHeight(580);
		s.setWidth(500);
		s.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public List<Image> getImages(final String... fileNames) {
		final ArrayList<Image> images = new ArrayList<>();
		for (int i = 0; i < fileNames.length; i++)
			images.add(new Image(getClass().getResource(fileNames[i]).toString()));
		return images;
	}
}
