package application;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginEventHandler implements EventHandler<ActionEvent> {
	private Stage primaryStage;
	private Text errorText;
	private TextField usernameField;
	private TextField passwordField;
	
	public LoginEventHandler(Stage primaryStage, Text errorText, TextField usernameField, TextField passwordField) {
		this.primaryStage = primaryStage;
		this.errorText = errorText;
		this.usernameField = usernameField;
		this.passwordField = passwordField;
	}

	@Override
	public void handle(ActionEvent event) {
		
	}

}
