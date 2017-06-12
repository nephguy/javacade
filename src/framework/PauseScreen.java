package framework;

import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

/**
 * @author Nick Hansen
 */

public class PauseScreen extends Pane {
	
	GameRootPane parentGame;
	
	VBox pauseText;
	
	Label title;
	Label spacer;
	Label resume;
	Label restart;
	Label exit;
	
	String font;
	
	public PauseScreen (GameRootPane parent) {
		parentGame = parent;
		font = parentGame.getFont();
		this.minWidthProperty().bind(parentGame.widthProperty());
		this.minHeightProperty().bind(parentGame.heightProperty());
		
		this.setBackground(new Background(new BackgroundFill(
				Paint.valueOf("#808080dd"),
				CornerRadii.EMPTY,
				Insets.EMPTY
				)));
		
		title = Util.styleLabel(font,40,false,"Paused");
		spacer = Util.styleLabel(font,15,false,"");
		resume = Util.styleLabel(font,20,true,"Resume");
		resume.setOnMouseClicked(event -> {
			parentGame.togglePause();
			event.consume();
		});
		restart = Util.styleLabel(font,20,true,"Restart");
		restart.setOnMouseClicked(event -> {
			parentGame.restart();
		});
		exit = Util.styleLabel(font,20,true,"Exit Game");
		exit.setOnMouseClicked(event -> {
			parentGame.close();
			this.getScene().setRoot(new MainMenu());
			event.consume();
		});
		
		pauseText = new VBox (title,spacer,resume,restart,exit);
		pauseText.setAlignment(Pos.CENTER);
		pauseText.setSpacing(20);
		pauseText.prefHeightProperty().bind(this.heightProperty());
		pauseText.prefWidthProperty().bind(this.widthProperty());
		
		this.getChildren().add(pauseText);
	}
}
