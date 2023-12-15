package application;

import java.io.File;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoadKeyEventHandler implements EventHandler<ActionEvent> {
	private String user;
	private TextField keyField;
	private String algorithm;
	private boolean toFile;
	private Text keyText;

	public LoadKeyEventHandler(boolean toFile, String user, TextField keyField, String algorithm, Text keyText) {
		this.user = user;
		this.keyField = keyField;
		this.algorithm = algorithm;
		this.toFile = toFile;
		this.keyText = keyText;
	}

	@Override
	public void handle(ActionEvent event) {
		Alert resultAlert;
		try {
			Key loadedKey;
			if (toFile) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Select Key File");
				fileChooser.setInitialDirectory(new File("keys/"));
				File keyFile = fileChooser.showOpenDialog(new Stage());
				if (keyFile == null) {
					return;
				}
				loadedKey = ModernCipher.loadKeyFromFile(keyFile.getName());
			}
			else {
				ObservableArrayList<String> keys = new ObservableArrayList<String>(DatabaseConnector.loadKeysFromDatabase(algorithm, user));
				
				resultAlert = new Alert(AlertType.CONFIRMATION);
				resultAlert.setHeaderText("");
				resultAlert.setTitle("Select key");
				
				ComboBox<String> selectKeyBox = new ComboBox<String>(keys);
				selectKeyBox.getSelectionModel().selectFirst();
				resultAlert.setGraphic(selectKeyBox);
				
				resultAlert.showAndWait();
				if (resultAlert.getResult().getText().equals("Cancel")) {
					return;
				}
				else {
					loadedKey = new Key(selectKeyBox.getValue(), algorithm);
				}
			}
			
			
			if (algorithm.equals(loadedKey.getAlgorithm())) {
				keyField.setText(loadedKey.getKey());
				keyText.setText(loadedKey.getKey());
				resultAlert = new Alert(AlertType.INFORMATION);
				resultAlert.setHeaderText("Key loaded successfully");
			}
			else {
				resultAlert = new Alert(AlertType.ERROR);
				resultAlert.setHeaderText("Key is for a different algorithm");
			}
		}
		catch (Throwable exception) {
//			exception.printStackTrace();
			resultAlert = new Alert(AlertType.ERROR);
			resultAlert.setHeaderText("Failed to load key");
		}
		resultAlert.showAndWait();
	}

}
