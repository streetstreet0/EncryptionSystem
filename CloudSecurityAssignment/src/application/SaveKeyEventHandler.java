package application;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SaveKeyEventHandler implements EventHandler<ActionEvent> {
	private String user;
	private String key;
	private String algorithm;
	private boolean toFile;

	public SaveKeyEventHandler(boolean toFile, String user, String key, String algorithm) {
		this.user = user;
		this.key = key;
		this.algorithm = algorithm;
		this.toFile = toFile;
	}

	@Override
	public void handle(ActionEvent event) {
		Alert resultAlert;
		try {
			if (toFile) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Select Key File");
				fileChooser.setInitialDirectory(new File("keys/"));
				File keyFile = fileChooser.showSaveDialog(new Stage());
				if (keyFile == null) {
					return;
				}
				else if (keyFile.getName().endsWith("_temp")) {
					resultAlert = new Alert(AlertType.ERROR);
					resultAlert.setHeaderText("Key files cannot end in _temp");
					resultAlert.showAndWait();
					return;
				}
				ModernCipher.saveKeyToFile(algorithm, key, keyFile.getName());
			}
			else {
				DatabaseConnector.saveKeyToDatabase(algorithm, key, user);
			}
			resultAlert = new Alert(AlertType.INFORMATION);
			resultAlert.setHeaderText("Key successfully saved");
		}
		catch (Throwable exception) {
			exception.printStackTrace();
			resultAlert = new Alert(AlertType.ERROR);
			resultAlert.setHeaderText("Failed to save key");
		}
		resultAlert.showAndWait();
	}

}
