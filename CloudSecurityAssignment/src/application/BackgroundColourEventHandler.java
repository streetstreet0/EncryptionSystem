package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class BackgroundColourEventHandler implements EventHandler<ActionEvent> {
	private BorderPane mainPane;
	private ColorPicker backgroundPicker;
	private String username;

	public BackgroundColourEventHandler(BorderPane mainPane, ColorPicker backgroundPicker, String username) {
		this.mainPane = mainPane;
		this.backgroundPicker = backgroundPicker;
		this.username = username;
	}

	@Override
	public void handle(ActionEvent event) {
		Color colour = backgroundPicker.getValue();
		mainPane.setBackground(new Background(new BackgroundFill(colour, null, null)));
		try {
			DatabaseConnector.saveBackgroundColourToDatabase(username, colour);
		}
		catch (Throwable exception) {
//			exception.printStackTrace();
		}
	}
}
