package framework.menu;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;

import curveball.Curveball;
import framework.GameRootPane;
import framework.Util;

/**
 * @author Nick Hansen
 */

public class GameListMenu extends Menu {

	String font = "press-start.ttf";
	
	TilePane games;
	
	Curveball curveball = new Curveball ();
	
	public GameListMenu () {
		
		games = new TilePane();
		games.setOrientation(Orientation.HORIZONTAL);
		games.setAlignment(Pos.TOP_LEFT);
		
		addGame (curveball,"curveball","Curveball");
		addGame (curveball,"curveball","Curveball");
		addGame (curveball,"curveball","Curveball");
		addGame (curveball,"curveball","Curveball");
		addGame (curveball,"curveball","Curveball");
		
		
		
		
		
		this.setCenter(games);
		this.setPadding(new Insets (20));
		this.setMinHeight(600);
		this.setMinWidth(600);
	}
	
	private void addGame (GameRootPane game, String gamePackageName, String name) {
		VBox gameButton = new VBox ();
		Label gameName = Util.styleLabel(font, 10, false, name);
		ImageView gameImage = Util.getImage(gamePackageName, "previewimg.png");
		gameImage.setFitHeight(120);
		gameImage.setFitWidth(120);
		
		gameButton.getChildren().addAll(gameImage,gameName);
		gameButton.setPadding(new Insets (10));
		gameButton.setAlignment(Pos.CENTER);
		gameButton.setSpacing(5);
		gameButton.setPadding(new Insets (10));
		gameButton.setCursor(Cursor.HAND);
		gameButton.setOnMouseClicked(event -> {
			this.getScene().setRoot(game);
		});
		
		games.getChildren().add(gameButton);
	}
	
}
