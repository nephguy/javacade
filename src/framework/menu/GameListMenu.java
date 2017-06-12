package framework.menu;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;

import framework.GameRootPane;
import framework.MainMenu;
import framework.Util;

/**IMPORT GAMES HERE**/

import curveball.Curveball;
import spaceinvaders.SpaceInvaders;
import tron_snake.TronSnake;

/**IMPORT GAMES HERE**/

/**
 * @author Nick Hansen
 */

public class GameListMenu extends Menu {

	String font = "press-start.ttf";
	
	TilePane games;
	
	public GameListMenu (MainMenu parent) {
		super(parent);
		
		games = new TilePane();
		games.setOrientation(Orientation.HORIZONTAL);
		games.setAlignment(Pos.TOP_LEFT);
		this.setCenter(games);
		
		/**ADD YOUR GAME HERE**/
		addGame (new Curveball());
		addGame (new SpaceInvaders());
		addGame (new TronSnake());
		/**ADD YOUR GAME HERE**/
	}
	
	private void addGame (GameRootPane game) {
		VBox gameButton = new VBox ();
		Label gameName = Util.styleLabel(font, 10, false, game.gameTitle);
		ImageView gameImage = Util.getImage(game.getClass().getPackage().getName(), "previewimg.png");
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
			parent.bgMusic.stop();
			if (game.bgMusic != null) game.bgMusic.play();
		});
		
		games.getChildren().add(gameButton);
	}
	
}
