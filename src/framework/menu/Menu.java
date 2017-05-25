package framework.menu;

import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import framework.MainMenu;
import framework.Util;
import javafx.application.Platform;
import javafx.geometry.Insets;

/**
 * @author Nick Hansen
 */

public class Menu extends BorderPane{

	String font = "press-start.ttf";
	
	public Menu () {
		
		Label backToMenu = Util.styleLabel(font, 25, true, "Back to Main Menu");
		backToMenu.setOnMouseClicked(event -> {
			returnToMainMenu();
		});
		
		Platform.runLater(new Runnable() {
			  public void run() {
			    arm();
			  }
		});
		
		this.setBottom(backToMenu);
		BorderPane.setAlignment(backToMenu, Pos.BOTTOM_CENTER);
		this.setPadding(new Insets (20));
		this.setMinHeight(600);
		this.setMinWidth(600);
	}
	
	private void returnToMainMenu () {
		this.getScene().setRoot(new MainMenu());
	}
	
	private void arm () {
		this.getScene().setOnKeyPressed(event -> {
			switch (event.getCode()) {
				default: break;
				case ESCAPE: returnToMainMenu(); break;
				case BACK_SPACE: returnToMainMenu(); break;
			}
		});
	}
	
}