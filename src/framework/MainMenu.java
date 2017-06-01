package framework;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import framework.menu.CreditsMenu;
import framework.menu.GameListMenu;
import framework.menu.OptionsMenu;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;

/**
 * @author Nick Hansen
 */

public class MainMenu extends VBox{

	Label title;
	Label spacer;
	Label games;
	Label options;
	Label scoreboard;
	Label credits;
	Label close;
	
	String font = "press-start.ttf";
	
	public MainMenu () {
		
		this.setBackground(new Background(new BackgroundFill(
				Color.LIGHTGRAY,
				CornerRadii.EMPTY,
				Insets.EMPTY)));
		
		setMinHeight(600);
		setMinWidth(600);
		
		title = Util.styleLabel(font, 60, Color.BLACK, Color.DARKGRAY, false, false, "JAVACADE");
		spacer = Util.styleLabel(font, 50, false, "");
		games = Util.styleLabel(font, 25, true, "Games");
		games.setOnMouseClicked(event -> {
			this.getScene().setRoot(new GameListMenu());
		});
		options = Util.styleLabel(font, 25, true, "Options");
		options.setOnMouseClicked(event -> {
			this.getScene().setRoot(new OptionsMenu());
		});
		scoreboard = Util.styleLabel(font, 25, true, "High Scores");
		scoreboard.setOnMouseClicked(event -> {
			//TODO - ADD HIGH SCORE SCREEN
			this.getScene().setRoot(new OptionsMenu());
		});
		credits = Util.styleLabel(font, 25, true, "Credits");
		credits.setOnMouseClicked(event -> {
			this.getScene().setRoot(new CreditsMenu());
		});
		close = Util.styleLabel(font, 25, true, "Exit");
		close.setOnMouseClicked(event -> {
			Platform.exit();
		});
		
		getChildren().addAll(title,spacer,games,options,credits,close);
		this.setAlignment(Pos.CENTER);
		this.setSpacing(20);
		
	}
	
}
