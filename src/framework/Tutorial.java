package framework;

import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import framework.Util;
import javafx.application.Platform;
import javafx.geometry.Insets;

/**
 * @author Nick Hansen
 */

public class Tutorial extends BorderPane{

	GameRootPane parentGame;
	Color textColor;
	
	public Tutorial (GameRootPane parent, String tutorialText) {
		parentGame = parent;
		textColor = (Color)parent.getBackground().getFills().get(0).getFill();
		textColor = Color.rgb(255-(int)textColor.getRed(), 255-(int)textColor.getGreen(), 255-(int)textColor.getBlue());
		
		Label tutorial = Util.styleLabel(parentGame.getFont(), 10, textColor, Color.TRANSPARENT, false, true, tutorialText);
		Label backToMenu = Util.styleLabel(parentGame.getFont(), 25, textColor, Color.TRANSPARENT, true, false, "Back to Game");
		backToMenu.setOnMouseClicked(event -> {
			returnToGame();
		});
		
		Platform.runLater(new Runnable() {
			  public void run() {
			    arm();
			  }
		});
		
		
		this.setBackground(parentGame.currentBackground);
		
		
		this.setCenter(tutorial);
		BorderPane.setAlignment(tutorial, Pos.TOP_LEFT);
		this.setBottom(backToMenu);
		BorderPane.setAlignment(backToMenu, Pos.BOTTOM_CENTER);
		this.setPadding(new Insets (20));
		this.setMinHeight(600);
		this.setMinWidth(600);
	}
	
	private void returnToGame () {
		parentGame.removePane(this);
	}
	
	private void arm () {
		this.getScene().setOnKeyPressed(event -> {
			switch (event.getCode()) {
				default: break;
				case ESCAPE: returnToGame(); break;
				case BACK_SPACE: returnToGame(); break;
			}
		});
	}
	
}