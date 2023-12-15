package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ChangeAlgorithmEventHandler implements EventHandler<ActionEvent> {
	private VBox algorithmBox;
	private BorderPane mainPane;
	
	public ChangeAlgorithmEventHandler(VBox algorithmBox, BorderPane mainPane) {
		this.algorithmBox = algorithmBox;
		this.mainPane = mainPane;
	}

	@Override
	public void handle(ActionEvent event) {
		mainPane.setCenter(algorithmBox);
	}

}
