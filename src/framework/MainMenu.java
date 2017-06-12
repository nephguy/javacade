package framework;

import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

import framework.menu.CreditsMenu;
import framework.menu.GameListMenu;
import framework.menu.OptionsMenu;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;

/**
 * @author Nick Hansen
 */

public class MainMenu extends StackPane{
	
	// actual main menu shit
	VBox actualMainMenu;
	Label title;
	Label spacer;
	Label games;
	Label options;
	Label scoreboard;
	Label credits;
	Label close;
	
	// misc
	String font = "press-start.ttf";
	public MediaPlayer bgMusic;
	String appDataPath;
	
	public MainMenu () {
		
		//create the menu
		this.setBackground(new Background(new BackgroundFill(
				Color.LIGHTGRAY,
				CornerRadii.EMPTY,
				Insets.EMPTY)));
		
		setMinSize(600,600);
		setMaxSize(600,600);
		
		title = Util.styleLabel(font, 60, Color.BLACK, Color.DARKGRAY, false, false, "JAVACADE");
		spacer = Util.styleLabel(font, 50, false, "");
		games = Util.styleLabel(font, 25, true, "Games");
		games.setOnMouseClicked(event -> {
			this.getChildren().add(new GameListMenu(this));
		});
		options = Util.styleLabel(font, 25, true, "Options");
		options.setOnMouseClicked(event -> {
			this.getChildren().add(new OptionsMenu(this));
		});
		scoreboard = Util.styleLabel(font, 25, true, "High Scores");
		scoreboard.setOnMouseClicked(event -> {
			//TODO - ADD HIGH SCORE SCREEN
			this.getChildren().add(new OptionsMenu(this));
		});
		credits = Util.styleLabel(font, 25, true, "Credits");
		credits.setOnMouseClicked(event -> {
			this.getChildren().add(new CreditsMenu(this));
		});
		close = Util.styleLabel(font, 25, true, "Exit");
		close.setOnMouseClicked(event -> {
			Platform.exit();
		});
		
		actualMainMenu = new VBox(title,spacer,games,options,credits,close);
		actualMainMenu.setAlignment(Pos.CENTER);
		actualMainMenu.setSpacing(20);
		this.getChildren().add(actualMainMenu);
		
		
		
		
		// find the app data path
		final String sep = System.getProperty("file.separator");
		appDataPath = System.getenv("APPDATA");
	    String folder = "Javacade";
	    String os = System.getProperty("os.name").toUpperCase();
	    if (os.contains("WIN"))
	      appDataPath = System.getenv("APPDATA") + sep + folder;
	    if (os.contains("MAC"))
	      appDataPath = System.getProperty("user.home") + sep + "Documents" + sep + folder;
	    if (os.contains("NUX"))
	      appDataPath = System.getProperty("user.dir") + sep + folder;
	    
	    // init bg music
		Duration noIntroStart = Duration.millis(13475);
		bgMusic = new MediaPlayer(new Media(ClassLoader.getSystemResource("framework/Distance.wav").toExternalForm()));
		bgMusic.setVolume(1);
		bgMusic.setOnEndOfMedia(() -> {
	    	bgMusic.seek(noIntroStart);
	    	bgMusic.play();
	    });
		
		// make directory for data file
	    File directory = new File(appDataPath);
	    if (!directory.exists()) directory.mkdir();
	    
	    // make data file
	    File dataFile = new File(appDataPath + "/data.txt");
    	try {
		    if (!dataFile.createNewFile()) {
		    	bgMusic.setStartTime(noIntroStart);
	    		bgMusic.play();
		    }
	    	else {
	    		dataFile.mkdir();
	    		bgMusic.play();
	    		firstLaunchFanciness();
	    	}
    	}
    	catch (IOException e) {e.printStackTrace();}
	}
	
	private void firstLaunchFanciness () {
		Rectangle blackScreen = new Rectangle(600,600,Color.BLACK);
		Label title = Util.styleLabel(font, 45, Color.WHITE, Color.TRANSPARENT, false, true, "APCS Project");
		Label date = Util.styleLabel(font,18, Color.WHITE, Color.TRANSPARENT, false, true, "2017");
		Label authors = Util.styleLabel(font, 18, Color.WHITE, Color.TRANSPARENT, false, true, "By:\nNick Hansen, Alvin Chu,\nJackson Chui, Benjamin Zhang,\nand Lovejit Kharod");
		Label welcome = Util.styleLabel(font, 35, Color.WHITE, Color.TRANSPARENT, false, true, "Welcome To...");
		this.getChildren().addAll(blackScreen,title,date,authors,welcome);
		title.setTranslateY(320);
		authors.translateYProperty().bind(title.translateYProperty().add(110));
		date.translateYProperty().bind(title.translateYProperty().add(50));
		welcome.translateYProperty().bind(title.translateYProperty().add(530));
		welcome.opacityProperty().bind(blackScreen.opacityProperty());
		welcome.visibleProperty().bind(blackScreen.visibleProperty());
		Timeline titleTimeline = new Timeline(moveTo(title,3272,320),
											  moveTo(title,6516,-70),
											  moveTo(title,9751,-70),
											  moveTo(title,13475,-530));
		titleTimeline.play();
		Timeline backgroundTimeline = new Timeline (opacity(blackScreen,0,100),
													opacity(blackScreen,12664,100),
													opacity(blackScreen,13475,0));
													
		backgroundTimeline.play();
	}
	private void firstLaunchFancinessFOR_SONG_ONE () {
		Rectangle blackScreen = new Rectangle(600,600,Color.BLACK);
		Label title = Util.styleLabel(font, 45, Color.WHITE, Color.TRANSPARENT, false, true, "APCS Project");
		Label date = Util.styleLabel(font,18, Color.WHITE, Color.TRANSPARENT, false, true, "2017");
		Label authors = Util.styleLabel(font, 18, Color.WHITE, Color.TRANSPARENT, false, true, "By:\nNick Hansen, Alvin Chu,\nJackson Chui, Benjamin Zhang,\nand Lovejit Kharod");
		Label welcome = Util.styleLabel(font, 35, Color.WHITE, Color.TRANSPARENT, false, true, "Welcome To...");
		this.getChildren().addAll(blackScreen,title,date,authors,welcome);
		title.setTranslateY(320);
		authors.translateYProperty().bind(title.translateYProperty().add(110));
		date.translateYProperty().bind(title.translateYProperty().add(50));
		welcome.translateYProperty().bind(title.translateYProperty().add(530));
		welcome.visibleProperty().bind(blackScreen.visibleProperty());
		Timeline titleTimeline = new Timeline(moveTo(title,4000,320),
											  moveTo(title,7700,-70),
											  moveTo(title,11000,-70),
											  moveTo(title,16000,-530));
		titleTimeline.play();
		Timeline backgroundTimeline = new Timeline (visible(blackScreen,0,true),
													visible(blackScreen,16900,false),
													visible(blackScreen,17000,true),
													visible(blackScreen,17100,false),
													visible(blackScreen,17200,true),
													visible(blackScreen,17300,false));
		backgroundTimeline.play();
	}
	private KeyFrame moveTo (Node node, double durationInMs, double pos) {
		return new KeyFrame (Duration.millis(durationInMs),new KeyValue(node.translateYProperty(),pos));
	}
	private KeyFrame visible (Node node, double durationInMs, boolean visible) {
		return new KeyFrame (Duration.millis(durationInMs),new KeyValue(node.visibleProperty(),visible));
	}
	private KeyFrame opacity (Node node, double durationInMs, double opacity) {
		return new KeyFrame (Duration.millis(durationInMs),new KeyValue(node.opacityProperty(),opacity));
	}
	
}
