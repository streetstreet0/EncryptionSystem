package application;
	
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;

import java.awt.Dimension;
import java.awt.Toolkit;
import javafx.event.EventHandler;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class Main extends Application {
	private Stage primaryStage;
	private String currentUser;
	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
			primaryStage.setWidth(size.getWidth() * 0.9);
			primaryStage.setHeight(size.getHeight() * 0.9);
			
			changeScene(loginScene());
			primaryStage.setTitle("Cloud Security");
			primaryStage.getIcons().add(new Image(new FileInputStream(new File("icon.png"))));
			primaryStage.show();
		} 
		catch(Exception exception) {
			exception.printStackTrace();
		}
	}
	
	public Scene loginScene() {
		VBox loginBox = new VBox();
		GridPane loginPane = new GridPane();
		loginBox.getChildren().add(loginPane);
		loginBox.setAlignment(Pos.CENTER);
		loginPane.setAlignment(Pos.CENTER);
		
		Text usernameText = new Text("Username ");
		TextField usernameField = new TextField();
		usernameField.setPromptText("username");
		loginPane.getChildren().add(usernameField);
		loginPane.getChildren().add(usernameText);
		GridPane.setColumnIndex(usernameField, 1);
		GridPane.setColumnIndex(usernameText, 0);
		GridPane.setRowIndex(usernameField, 0);
		GridPane.setRowIndex(usernameText, 0);
		
		Text passwordText = new Text("Password ");
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("password");
		loginPane.getChildren().add(passwordField);
		loginPane.getChildren().add(passwordText);
		GridPane.setColumnIndex(passwordField, 1);
		GridPane.setColumnIndex(passwordText, 0);
		GridPane.setRowIndex(passwordField, 1);
		GridPane.setRowIndex(passwordText, 1);
		
		Scene loginScene = new Scene(loginBox);
//		loginBox.getChildren().add(loginPane);
		
		Button loginButton = new Button("Log in");
		loginBox.getChildren().add(loginButton);
		
		Text errorText = new Text();
		loginBox.getChildren().add(errorText);
		errorText.setFill(Color.RED);
		
		
		loginButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String username = usernameField.getText();
				String password = passwordField.getText();
				try {
					if (DatabaseConnector.validUser(username, password)) {
						currentUser = username;
						changeScene(mainScene());
					}
					else {
						errorText.setText("Incorrect username or password");
					}
				}
				catch (Exception exception) {
					errorText.setText("Failed to connect to database");
				}
			}
			
		});
//		loginButton.setOnAction(new EventHandler<ActionEvent>() {
//
//			@Override
//			public void handle(ActionEvent event) {
//				String username = usernameField.getText();
//				String password = passwordField.getText();
//				createNewUser(username, password);
//			}
//			
//		});
		
		return loginScene;
	}
	
	public Scene mainScene() {
		BorderPane mainPane = new BorderPane();
		mainPane.setCenter(affineBox());
		VBox sideMenu = new VBox();
		sideMenu.setAlignment(Pos.CENTER);
		
		Button affineButton = new Button("Affine Cipher");
		sideMenu.getChildren().add(affineButton);
		affineButton.setOnAction(new ChangeAlgorithmEventHandler(affineBox(), mainPane));
		
		Button DESButton = new Button("DES Cipher");
		sideMenu.getChildren().add(DESButton);
		DESButton.setOnAction(new ChangeAlgorithmEventHandler(modernEncryptionBox("DES"), mainPane));
		
		Button AESButton = new Button("AES Cipher");
		sideMenu.getChildren().add(AESButton);
		AESButton.setOnAction(new ChangeAlgorithmEventHandler(modernEncryptionBox("AES"), mainPane));
		
		Button settingsButton = new Button("Settings");
		sideMenu.getChildren().add(settingsButton);
		settingsButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Alert settingsAlert = new Alert(Alert.AlertType.INFORMATION);
				settingsAlert.setTitle("Settings");
				settingsAlert.setHeaderText("");
				settingsAlert.setGraphic(settingsVBox());
				settingsAlert.showAndWait();
			}
			
		});
		
		mainPane.setLeft(sideMenu);
		Scene mainScene = new Scene(mainPane);
		
		return mainScene;
	}
	
	public VBox settingsVBox() {
		VBox settingsVBox = new VBox();
		Text temp = new Text("Temp");
		settingsVBox.getChildren().add(temp);
		
		return settingsVBox;
	}
	
	public VBox modernEncryptionBox(String algorithm) {
		VBox encryptionBox = new VBox();
		Text algorithmText = new Text(algorithm);
		algorithmText.setFont(new Font(26));
		algorithmText.setUnderline(true);
		encryptionBox.getChildren().add(algorithmText);
		
		GridPane encryptPane = new GridPane();
		encryptionBox.getChildren().add(encryptPane);
		encryptionBox.setAlignment(Pos.CENTER);
		encryptPane.setAlignment(Pos.CENTER);
		
		Text inputText = new Text("Plain Text ");
		TextField inputTextField = new TextField();
		inputTextField.setPromptText("plain text");
		encryptPane.getChildren().add(inputTextField);
		encryptPane.getChildren().add(inputText);
		GridPane.setColumnIndex(inputTextField, 1);
		GridPane.setColumnIndex(inputText, 0);
		GridPane.setRowIndex(inputTextField, 0);
		GridPane.setRowIndex(inputText, 0);
		
		Text keyText = new Text("Text Key ");
		TextField keyField = new TextField();
		keyField.setPromptText("text key");
		String defaultText = "";
		int defaultLength = 0;
		if (algorithm.equals("DES")) {
			keyField.setTextFormatter(generateModernKeyFormatter(8));
			defaultLength = 8;
		}
		else if (algorithm.equals("AES")) {
			keyField.setTextFormatter(generateModernKeyFormatter(32));
			defaultLength = 32;
		}
		for (int i=0; i<defaultLength; i++) {
			defaultText += "*";
		}
		keyField.setText(defaultText);
		Button saveLoadKeyButton = new Button("Save/Load Key");
//		Button saveKeyButton = new Button("Save Key");
//		Button loadKeyButton = new Button("Load Key");
		encryptPane.getChildren().add(keyField);
		encryptPane.getChildren().add(keyText);
		encryptPane.getChildren().add(saveLoadKeyButton);
//		encryptPane.getChildren().add(saveKeyButton);
//		encryptPane.getChildren().add(loadKeyButton);
		GridPane.setColumnIndex(keyField, 1);
		GridPane.setColumnIndex(keyText, 0);
		GridPane.setColumnIndex(saveLoadKeyButton, 2);
//		GridPane.setColumnIndex(saveKeyButton, 2);
//		GridPane.setColumnIndex(loadKeyButton, 3);
		GridPane.setRowIndex(keyText, 1);
		GridPane.setRowIndex(keyField, 1);
		GridPane.setRowIndex(saveLoadKeyButton, 1);
//		GridPane.setRowIndex(loadKeyButton, 1);
//		GridPane.setRowIndex(saveKeyButton, 1);
		
		GridPane encryptDecryptPane = new GridPane();
		encryptionBox.getChildren().add(encryptDecryptPane);
		encryptDecryptPane.setAlignment(Pos.CENTER);
		
		Button encryptButton = new Button("Encrypt");
		encryptDecryptPane.getChildren().add(encryptButton);
		GridPane.setColumnIndex(encryptButton, 0);
		
		Button decryptButton = new Button("Decrypt");
		encryptDecryptPane.getChildren().add(decryptButton);
		GridPane.setColumnIndex(encryptButton, 1);
		
		Text resultText = new Text();
		encryptionBox.getChildren().add(resultText);
		
		encryptButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ModernCipher cipher = new ModernCipher(algorithm, keyField.getText());
				try {
					resultText.setText(cipher.encryptText(inputTextField.getText()));
					resultText.setFill(Color.BLACK);
				}
				catch (Throwable exception) {
//					exception.printStackTrace();
					resultText.setText("Error: Input text could not be encrypted with " + algorithm);
					resultText.setFill(Color.RED);
				}
			}
			
		});
		decryptButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ModernCipher cipher = new ModernCipher(algorithm, keyField.getText());
				try {
					resultText.setText(cipher.decryptText(inputTextField.getText()));
					resultText.setFill(Color.BLACK);
				}
				catch (Throwable exception) {
//					exception.printStackTrace();
					resultText.setText("Error: Input text was not encrypted with " + algorithm);
					resultText.setFill(Color.RED);
				}
			}
			
		});
//		loadKeyButton.setOnAction(new EventHandler<ActionEvent>() {
//
//			@Override
//			public void handle(ActionEvent event) {
//				try {
//					Key loadedKey = ModernCipher.loadKeyFromFile("key file");
//					if (algorithm.equals(loadedKey.getAlgorithm())) {
//						keyField.setText(loadedKey.getKey());
//					}
//					else {
//						resultText.setText("This key is for a different algorithm");
//						resultText.setFill(Color.RED);
//					}
//				}
//				catch (IOException exception) {
//					resultText.setText("Couldn't load key");
//					resultText.setFill(Color.RED);
//				}
//			}
//			
//		});
		saveLoadKeyButton.setOnAction(new SaveLoadKeyHandler(currentUser, keyField, algorithm));
		
		return encryptionBox;
	}
	
	public VBox affineBox() {
		VBox affineBox = new VBox();
		Text algorithmText = new Text("Affine Cipher");
		algorithmText.setFont(new Font(26));
		algorithmText.setUnderline(true);
		affineBox.getChildren().add(algorithmText);
		
		GridPane encryptPane = new GridPane();
		affineBox.getChildren().add(encryptPane);
		affineBox.setAlignment(Pos.CENTER);
		encryptPane.setAlignment(Pos.CENTER);
		
		Text inputText = new Text("Plain Text ");
		TextField inputTextField = new TextField();
		inputTextField.setPromptText("plain text");
		encryptPane.getChildren().add(inputTextField);
		encryptPane.getChildren().add(inputText);
		GridPane.setColumnIndex(inputTextField, 1);
		GridPane.setColumnIndex(inputText, 0);
		GridPane.setRowIndex(inputTextField, 0);
		GridPane.setRowIndex(inputText, 0);
		
		GridPane affineKeyPane = new GridPane();
		affineBox.getChildren().add(affineKeyPane);
		affineKeyPane.setAlignment(Pos.CENTER);
		
		
		Text multKeyText = new Text("a ");
		TextField multKeyField = new TextField();
		multKeyField.setPromptText("a key");
		affineKeyPane.getChildren().add(multKeyField);
		affineKeyPane.getChildren().add(multKeyText);
		GridPane.setColumnIndex(multKeyField, 0);
		GridPane.setColumnIndex(multKeyText, 0);
		GridPane.setRowIndex(multKeyField, 1);
		GridPane.setRowIndex(multKeyText, 0);
		
		Text addKeyText = new Text("b ");
		TextField addKeyField = new TextField();
		addKeyField.setPromptText("b key");
		affineKeyPane.getChildren().add(addKeyField);
		affineKeyPane.getChildren().add(addKeyText);
		GridPane.setColumnIndex(addKeyField, 1);
		GridPane.setColumnIndex(addKeyText, 1);
		GridPane.setRowIndex(addKeyField, 1);
		GridPane.setRowIndex(addKeyText, 0);
		
		multKeyField.setTextFormatter(generateAffineKeyFormatter());
		addKeyField.setTextFormatter(generateAffineKeyFormatter());
		multKeyField.setText("1");
		addKeyField.setText("3");
		
		GridPane encryptDecryptPane = new GridPane();
		affineBox.getChildren().add(encryptDecryptPane);
		encryptDecryptPane.setAlignment(Pos.CENTER);
		
		Button encryptButton = new Button("Encrypt");
		encryptDecryptPane.getChildren().add(encryptButton);
		GridPane.setColumnIndex(encryptButton, 0);
		
		Button decryptButton = new Button("Decrypt");
		encryptDecryptPane.getChildren().add(decryptButton);
		GridPane.setColumnIndex(encryptButton, 1);
		
		Text resultText = new Text();
		affineBox.getChildren().add(resultText);
		
		encryptButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				int multKeyValue = Integer.parseInt(multKeyField.getText());
				if (multKeyValue == 0 || (multKeyValue % 2)== 0 || multKeyValue == 13) {
					resultText.setText("Error: a must be non-zero, odd and cannot be 13");
					resultText.setFill(Color.RED);
				}
				else {
					AffineCipher affineCipher = new AffineCipher(multKeyValue, Integer.parseInt(addKeyField.getText()));
					String encryptedText = affineCipher.encryptString(inputTextField.getText());
					resultText.setText(encryptedText);
					resultText.setFill(Color.BLACK);
				}
			}
			
		});
		decryptButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				int multKeyValue = Integer.parseInt(multKeyField.getText());
				if (multKeyValue == 0 || multKeyValue == 2 || multKeyValue == 13) {
					resultText.setText("Error: a must be non-zero and cannot be 2 or 13");
					resultText.setFill(Color.RED);
				}
				else {
					AffineCipher affineCipher = new AffineCipher(multKeyValue, Integer.parseInt(addKeyField.getText()));
					String decryptedText = affineCipher.decryptString(inputTextField.getText());
					resultText.setText(decryptedText);
					resultText.setFill(Color.BLACK);
				}
			}
			
		});
		
		return affineBox;
	}
	
	public TextFormatter<Integer> generateAffineKeyFormatter() {
		TextFormatter<Integer> affineKeyFormatter = new TextFormatter<Integer>(new StringConverter<Integer>() {

			@Override
			public Integer fromString(String string) {
				return Integer.parseInt(string);
			}

			@Override
			public String toString(Integer num) {
				return "" + num;
			}
			
		}, 0);
		affineKeyFormatter.valueProperty().addListener(new ChangeListener<Integer>() {

			@Override
			public void changed(ObservableValue<? extends Integer> value, Integer oldValue, Integer newValue) {
				if (newValue < 0) {
					affineKeyFormatter.setValue(0);
				}
				else if (newValue > 25) {
					affineKeyFormatter.setValue(25);
				}
			}
			
		});
		return affineKeyFormatter;
	}
	
	public TextFormatter<String> generateModernKeyFormatter(int length) {
		TextFormatter<String> modernKeyFormatter = new TextFormatter<String>(new StringConverter<String>() {

			@Override
			public String fromString(String string) {
				return string;
			}

			@Override
			public String toString(String string) {
				return string;
			}
			
			
		});
		modernKeyFormatter.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> value, String oldValue, String newValue) {
				if (newValue.length() < length) {
					String extendedValue = newValue;
					for (int i=newValue.length(); i<length; i++) {
						extendedValue += "*";
					}
					modernKeyFormatter.setValue(extendedValue);
				}
				else if (newValue.length() > length) {
					modernKeyFormatter.setValue(newValue.substring(0, length));
				}
			}
			
		});
		return modernKeyFormatter;
	}
	
	public void changeScene(Scene scene) {
		primaryStage.setScene(scene);
		primaryStage.setWidth(primaryStage.getWidth());
		primaryStage.setHeight(primaryStage.getHeight());
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
