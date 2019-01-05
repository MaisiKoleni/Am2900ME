package net.maisikoleni.am2900me.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.prefs.Preferences;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

public class IOUtil {
	public static final Preferences PREFERENCES = Preferences.userRoot().node("am2900me");

	public static boolean readLines(Node alertOwner, Consumer<List<String>> onSuccess) {
		FileChooser fc = new FileChooser();
		workingDir().ifPresent(f -> fc.setInitialDirectory(f));
		File f = fc.showOpenDialog(alertOwner.getScene().getWindow());
		try {
			onSuccess.accept(Files.readAllLines(f.toPath()));
			PREFERENCES.put("lastFile", f.getAbsolutePath());
			return true;
		} catch (Exception ex) {
			Alert a = new Alert(AlertType.ERROR, "Load from File failed:\n" + ex, ButtonType.CLOSE);
			a.initOwner(alertOwner.getScene().getWindow());
			a.show();
			return false;
		}
	}

	public static boolean writeLines(Node alertOwner, Supplier<Iterable<String>> onSuccess) {
		FileChooser fc = new FileChooser();
		workingDir().ifPresent(f -> fc.setInitialDirectory(f));
		File f = fc.showSaveDialog(alertOwner.getScene().getWindow());
		try {
			Files.write(f.toPath(), onSuccess.get(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			PREFERENCES.put("lastFile", f.getAbsolutePath());
			return true;
		} catch (Exception ex) {
			Alert a = new Alert(AlertType.ERROR, "Save to File failed:\n" + ex, ButtonType.CLOSE);
			a.initOwner(alertOwner.getScene().getWindow());
			a.show();
			return false;
		}
	}

	private static Optional<File> workingDir() {
		String nonPref = System.getProperty("user.dir");
		if (nonPref == null)
			nonPref = System.getProperty("user.home");
		String path = PREFERENCES.get("lastFile", nonPref);
		if (path == null)
			return Optional.empty();
		return Optional.ofNullable(new File(path).getParentFile());
	}
}
