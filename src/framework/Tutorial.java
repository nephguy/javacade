package framework;

import javafx.scene.layout.BorderPane;
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
	
	public Tutorial (GameRootPane parent, String tutorialText) {
		parentGame = parent;
		Label tutorial = Util.styleLabel(parentGame.getFont(), 10, false, tutorialText);
		Label backToMenu = Util.styleLabel(parentGame.getFont(), 25, true, "Back to Game");
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