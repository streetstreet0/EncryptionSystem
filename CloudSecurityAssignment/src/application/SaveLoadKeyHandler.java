package application;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SaveLoadKeyHandler implements EventHandler<ActionEvent> {
	private String user;
	private TextField keyField;
	private String algorithm;

	public SaveLoadKeyHandler(String user, TextField keyField, String algorithm) {
		this.user = user;
		this.keyField = keyField;
		this.algorithm = algorithm;
	}
	
	private Alert createAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		GridPane mainPane = new GridPane();
		alert.setTitle("Save/Load key");
		alert.setGraphic(mainPane);
		alert.setHeaderText("");
		
		
		Text keyDescriptionText = new Text("Key: ");
		Text algorithmDescriptionText = new Text("Algorithm: ");
		Text keyText = new Text(keyField.getText());
		Text algorithmText = new Text(algorithm);
		mainPane.getChildren().addAll(keyDescriptionText, algorithmDescriptionText, keyText, algorithmText);
		
		GridPane.setColumnIndex(keyDescriptionText, 0);
		GridPane.setColumnIndex(keyText, 1);
		GridPane.setColumnIndex(algorithmDescriptionText, 0);
		GridPane.setColumnIndex(algorithmText, 1);
		GridPane.setRowIndex(keyDescriptionText, 1);
		GridPane.setRowIndex(keyText, 1);
		GridPane.setRowIndex(algorithmDescriptionText, 0);
		GridPane.setRowIndex(algorithmText, 0);
		
		
		Button saveKeyToFileButton = new Button("Save to File");
		Button saveKeyToDatabaseButton = new Button("Save to Database");
		Button loadKeyFromFileButton = new Button("Load from File");
		Button loadKeyFromDatabaseButton = new Button("Load from Database");
		mainPane.getChildren().addAll(saveKeyToFileButton, saveKeyToDatabaseButton, loadKeyFromFileButton, loadKeyFromDatabaseButton);
		
		GridPane.setColumnIndex(saveKeyToFileButton, 0);
		GridPane.setColumnIndex(saveKeyToDatabaseButton, 0);
		GridPane.setColumnIndex(loadKeyFromFileButton, 1);
		GridPane.setColumnIndex(loadKeyFromDatabaseButton, 1);
		GridPane.setRowIndex(saveKeyToFileButton, 2);
		GridPane.setRowIndex(saveKeyToDatabaseButton, 3);
		GridPane.setRowIndex(loadKeyFromFileButton, 2);
		GridPane.setRowIndex(loadKeyFromDatabaseButton, 3);
		
		saveKeyToFileButton.setOnAction(new SaveKeyEventHandler(true, user, keyField.getText(), algorithm));
		saveKeyToDatabaseButton.setOnAction(new SaveKeyEventHandler(false, user, keyField.getText(), algorithm));
		loadKeyFromFileButton.setOnAction(new LoadKeyEventHandler(true, user, keyField, algorithm, keyText));
		loadKeyFromDatabaseButton.setOnAction(new LoadKeyEventHandler(false, user, keyField, algorithm, keyText));
		
		return alert;
	}

	@Override
	public void handle(ActionEvent event) {
		Alert alert = createAlert();
		alert.showAndWait();
	}

}
