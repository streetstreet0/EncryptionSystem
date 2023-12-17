package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.layout.BackgroundImage;
import java.io.FileInputStream;

public class BackgroundImageEventHandler implements EventHandler<ActionEvent> {
	private BorderPane mainPane;
	private String username;

	public BackgroundImageEventHandler(BorderPane mainPane, String username) {
		this.mainPane = mainPane;
		this.username = username;
	}

	@Override
	public void handle(ActionEvent event) {
		try {
			mainPane.setBackground(new Background(new BackgroundImage(new Image(new FileInputStream("images/wooper.png")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, null)));
			DatabaseConnector.saveBackgroundImageToDatabase(username);
		}
		catch (Throwable exception) {
//			exception.printStackTrace();
		}
	}
}
