package framework;

import java.util.ArrayList;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.media.MediaPlayer;

/**
 * @author Nick Hansen
 */

public abstract class GameRootPane extends StackPane {
	
	// game loop stuff
	Timeline gameLoop;
	EventHandler<ActionEvent> gameUpdate;
	
	// interface, array lists, and functions for keybindings 
	public interface KeyAction {KeyCode getKey(); boolean fireOnce(); void action();}
	ArrayList<KeyAction> keyBindings;
	protected void addKeyBinding (KeyAction action) {keyBindings.add(action);}
	private ArrayList<KeyCode> keysPressed = new ArrayList<KeyCode>();
	private ArrayList<KeyCode> keysFiredOnce = new ArrayList<KeyCode>();
	
	// timer
	public double timeElapsedInMs;
	public double timeElapsedInS;
	private int hh;
	private int mm;
	private int ss;
	protected Label timer;
	
	// game main menu labels
	protected Label title;
	protected Label play;
	protected Label tutorial;
	protected Label backToMenu;
	
	// mouse stuff
	private double mouseX;
	private double mouseY;
	
	// general variables
	protected StackPane spritePane;
	protected boolean paused;
	PauseScreen pauseScreen;
	String font;
	protected String packageName;
	protected MediaPlayer bgMusic;
	
	// variables to make the pause screen and main menu work properly
	public String gameTitle;
	protected String backgroundMusicFileName;
	protected double musicVolume;
	Background currentBackground;
	
	public GameRootPane (String gameTitle, String backgroundMusicFileName) {
		this(gameTitle,"press-start.ttf",backgroundMusicFileName,50,60);
	}
	
	public GameRootPane (String gameTitle, String localFont, String backgroundMusicFileName, double musicVolume, double fps) {
		// create and add content pane
		spritePane = new StackPane();
		addPaneAbove(spritePane);
		
		// initialize object variables
		if (localFont.equalsIgnoreCase("default") || localFont.equals(null) || localFont.equals("")) font = "press-start.ttf";
		else font = localFont;
		this.gameTitle = gameTitle;
		this.backgroundMusicFileName = backgroundMusicFileName; 
		this.musicVolume = Util.clamp(musicVolume/100)*100;
		this.packageName = this.getClass().getPackage().getName();
		
		
		// initialize misc. settings
		paused = false;
		pauseScreen = new PauseScreen(this);
		keyBindings = new ArrayList<KeyAction>();
		setMinHeight(600);
		setMaxHeight(600);
		setMinWidth(600);
		setMaxHeight(600);
		setBackground(Color.ALICEBLUE);
		
		// game loop. runs every frame
		gameUpdate = event -> {
			timeElapsedInMs += gameLoop.getCurrentTime().toMillis();
			timeElapsedInS += gameLoop.getCurrentTime().toSeconds();
			if (timer != null) updateTimer();
			
			if (keysPressed.size() > 0) {
				for (int i = 0; i < keysPressed.size(); i++)
					fireKeyAction(keysPressed.get(i));
			}
			
			update();
		};
		gameLoop = new Timeline (new KeyFrame(Duration.millis(1000/fps), gameUpdate));
		gameLoop.setCycleCount(Animation.INDEFINITE);
	}
	
	/****************************************PRIVATE FUNCTIONS****************************************/
	
	private void updateTimer () {
		ss = (int)timeElapsedInS %60;
		mm = (int)timeElapsedInS /60 %60;
		hh = (int)timeElapsedInS /360;
		
		String newTime = "";
		if (hh != 0) newTime += hh + ":";
		if (mm < 10) newTime += "0" + mm + ":";
		else newTime += mm + ":";
		if (ss < 10) newTime += "0" + ss;
		else newTime += ss;
		if (timer != null) timer.setText(newTime);
	}
	
	private void fireKeyAction (KeyCode key) {
		keyBindings.forEach(k -> {
			if (key == k.getKey()) {
				if (!keysFiredOnce.contains(key)) k.action();
				if (k.fireOnce() && !keysFiredOnce.contains(key)) keysFiredOnce.add(k.getKey());
			}
		});
	}
	
	/****************************************PROTECTED/PUBLIC FUNCTIONS****************************************/
	
	/**This function is run every frame. Put collision checks and network updates within this function.**/
	protected abstract void update();
	
	/**This function is run upon pausing the game. Place any animations that need to be paused within this function.**/
	protected abstract void onPause();
	
	/**This function is run upon resuming the game. Place any animations that need to be resumed within this function.**/
	protected abstract void onResume();
	
	/**This function is run upon clicking "play" in the game's main menu. INITIALIZE ALL SPRITES within this function.**/
	protected abstract void onGameStart();
	
	
	/**HIGHLY SUGGESTED to run this function in the game's constructor to create your game's main menu.
	 *  A default is provided to prevent compile and runtime errors, but it is not ideal to use it.
	 * 
	 * @param titleSize the font size of the game's title
	 * @param menuElementSize the font size of all other menu elements on the main menu 
	 * @param menuMusicFileName the file name, including file extension (.mp3, .wav, etc), of the game menu music
	 * @param tutorialText the text for the "how to play" screen
	 * @param bgFill the background of the main menu.
	 * **/
	protected void initMenu (double titleSize, double menuElementSize, Paint textColor, Paint bgFill, String menuMusicFileName, String tutorialText) { 
		this.setBackground(bgFill);
		this.initMenu(titleSize, menuElementSize, textColor, menuMusicFileName, tutorialText);
	}
	
	/**HIGHLY SUGGESTED to run this function in the game's constructor to create your game's main menu.
	 *  A default is provided to prevent compile and runtime errors, but it is not ideal to use it.
	 * 
	 * @param titleSize the font size of the game's title
	 * @param menuElementSize the font size of all other menu elements on the main menu 
	 * @param menuMusicFileName the file name, including file extension (.mp3, .wav, etc), of the game menu music
	 * @param tutorialText the text for the "how to play" screen
	 * @param bgImage the background of the main menu
	 * **/
	protected void initMenu (double titleSize, double menuElementSize, Paint textColor, String menuMusicFileName, ImageView bgImage, String tutorialText) { 
		this.setBackground(bgImage);
		this.initMenu(titleSize, menuElementSize, textColor, menuMusicFileName, tutorialText);
	}
	
	private void initMenu (double titleSize, double menuElementSize, Paint textColor, String menuMusicFileName, String tutorialText) {
		if (!(menuMusicFileName.equals(null) || menuMusicFileName.equals(""))) setBgMusic(menuMusicFileName, musicVolume);
		VBox gameMenu = new VBox ();
		
		title = Util.styleLabel(font, titleSize, textColor, Color.TRANSPARENT, false, true, gameTitle);
		
		Label spacer = Util.styleLabel(font, titleSize-10, false, "");
		
		play = Util.styleLabel(font, menuElementSize, textColor, Color.TRANSPARENT, true, true, "Play Game");
		play.setOnMouseClicked(event -> {
			setBgMusic(backgroundMusicFileName, musicVolume);
			this.removePane(gameMenu);
			arm();
			gameLoop.play();
			onGameStart();
		});
		
		tutorial = Util.styleLabel(font, menuElementSize, textColor, Color.TRANSPARENT, true, true, "How To Play");
		tutorial.setOnMouseClicked(event -> {
			this.addPaneAbove(new Tutorial(this,tutorialText));
		});
		
		backToMenu = Util.styleLabel(font, menuElementSize, textColor, Color.TRANSPARENT, true, true, "Exit Game");
		backToMenu.setOnMouseClicked(event -> {
			disarm();
			this.getScene().setRoot(new MainMenu());
		});
		
		gameMenu.getChildren().addAll(title,spacer,play,tutorial,backToMenu);
		gameMenu.setAlignment(Pos.CENTER);
		gameMenu.setMinHeight(600);
		gameMenu.setMaxHeight(600);
		gameMenu.setMinWidth(600);
		gameMenu.setMaxHeight(600);
		gameMenu.setSpacing(20);
		gameMenu.setBackground(this.getBackground());
		
		this.addPaneAbove(gameMenu);
	}
	
	protected void enableTimer (double fontSize, Pos position) {
		this.enableTimer(fontSize, Color.BLACK, Color.TRANSPARENT, position);
	}
	
	/**Makes the timer Label functional. Run this if you want a timer in your game, before you place it in the scene.**/
	protected void enableTimer (double fontSize, Paint fontColor, Color dropShadowColor, Pos position) {
		timer = Util.styleLabel(font, fontSize, fontColor, dropShadowColor, false, true, "");
		timer.setPadding(new Insets(20));
		addPaneAbove(timer);
		StackPane.setAlignment(timer, position);
		updateTimer();
	}
	
	
	/**Stops previously playing background music, initializes new background music, and begins playing it.
	 * <p>
	 * @param volume MUST be between 0.0 and 100.0
	 * @param musicFileName the exact file name, including type extension (.mp3, .wav, etc)**/
	protected void setBgMusic (String musicFileName, double volume) {
		if (bgMusic != null) bgMusic.stop();
		bgMusic = new MediaPlayer (Util.getMusic(packageName, musicFileName));
		bgMusic.setCycleCount(Integer.MAX_VALUE);
		bgMusic.setVolume(volume/100);
		bgMusic.play();
	}
	
	/**This initializes the sound effect, and subsequently plays it.
	 * <p>
	 * @param volume MUST be between 0.0 and 100.0
	 * @param musicFileName the exact file name, including type extension (.mp3, .wav, etc)**/
	protected void playSfx (String sfxFileName, double volume) {
		MediaPlayer sfx = new MediaPlayer(Util.getSfx(packageName, sfxFileName));
		sfx.setVolume(volume/100);
		sfx.play();
	}
	
	/**Pauses/unpauses the game and manages the pause screen**/
	protected void togglePause() {
		Sprite s;
		if (!paused) {
			// automatically pauses currently running animated sprites
			for (int i = 0; i < this.spritePane.getChildren().size(); i++) {
				if (this.spritePane.getChildren().get(i) instanceof Sprite) {
					s = (Sprite)this.spritePane.getChildren().get(i);
					s.pause();
				}
			}
			
			// normal pause actions
			addPaneAbove(pauseScreen);
			paused = true;
			onPause();
			bgMusic.pause();
			gameLoop.pause();
		}
		else {
			// automatically resumes currently paused animated sprites
			for (int i = 0; i < this.spritePane.getChildren().size(); i++) {
				if (this.spritePane.getChildren().get(i) instanceof Sprite) {
					s = (Sprite)this.spritePane.getChildren().get(i);
					s.resume();
				}
			}
			
			// normal pause actions
			removePane(pauseScreen);
			paused = false;
			onResume();
			bgMusic.play();
			gameLoop.play();
		}
	}
	
	/**Set the background to be a flat color or a gradient
	 * <p>
	 * For a flat color, use the enumerators contained in javafx.scene.paint.Color.
	 * <br />
	 * For a gradient, use the Util.linearGradient() or Util.radialGradient() functions
	 * </p>
	 * @param color the color of the background**/
	protected void setBackground (Paint color) {
		Background bg = new Background(new BackgroundFill (
				color,
				CornerRadii.EMPTY,
				Insets.EMPTY));
		currentBackground = bg;
		this.setBackground(bg);
	}
	
	/**Set the background to be an image, resized to fit the 600px X 600px game field**/
	protected void setBackground (ImageView image) {
		Background bg = new Background(new BackgroundImage(
				image.getImage(),
				BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER,BackgroundSize.DEFAULT));
		currentBackground = bg;
		this.setBackground(bg);
	}
	
	/**Contains functions that require use of the parent scene.
	 * Must be run AFTER scene.setRoot(this) in the Main Application class.
	 * <P>
	 * This only loads the game. You must call gameLoop.play() to actually start playing.**/
	public void arm () {
		/* OLD PAUSE FUNCTION
		 * NOT USED, BUT KEPT BECAUSE IT WAS SOME GOOD ASS CODE
		
		// pause the game upon pressing escape.
		// if the game is not paused, fire any valid key bindings
		getScene().setOnKeyPressed(event -> {
			KeyCode key = event.getCode();
			if (key == KeyCode.ESCAPE) togglePause();
			else if (!paused)
				keyBindings.forEach(k -> {
					if (key == k.getKey()) k.action();
				});
			event.consume();
		});
		*/
		
		getScene().setOnKeyPressed(event -> {
			KeyCode key = event.getCode();
			if (key == KeyCode.ESCAPE) togglePause();
			else if(!paused && !keysPressed.contains(key)) keysPressed.add(key);
		});
		getScene().setOnKeyReleased(event -> {
			KeyCode key = event.getCode();
			keysPressed.remove(key);
			if (keysFiredOnce.contains(key)) keysFiredOnce.remove(key);
		});
		
		// track mouse movement
		//this.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
		this.setOnMouseMoved(event -> {
			mouseX = event.getX();
			mouseY = event.getY();
		});
	}
	/***/
	public void disarm () {
		gameLoop.stop();
		if (bgMusic != null) bgMusic.stop();
		timeElapsedInMs = timeElapsedInS = hh = mm = ss = 0;
		updateTimer();
	}
	/**Called upon closing the game. Ensures the game loops exits safely, and any game-specific settings are reset.
	 * This prevents settings leaking from one game to another, and readies it for the game's next launch.**/
	public void close () {
		gameLoop.stop();
		if (bgMusic != null) bgMusic.stop();
		keyBindings.clear();
	}
	
	public double getTimeElapsedInMs () {
		return timeElapsedInMs;
	}
	public double getTimeElapsedInS () {
		return timeElapsedInS;
	}
	
	public void setFont (String f) {
		font = f;
	}
	public String getFont () {
		return font;
	}
	
	/**Returns the mouse's current x-coordinate. Updates every frame.**/
	protected double getMouseX() {
		return mouseX;
	}
	/**Returns the mouse's current y-coordinate. Updates every frame.**/
	protected double getMouseY() {
		return mouseY;
	}
	
	
	
	/**DO NOT USE THIS FUNCTION UNLESS YOU KNOW WHAT YOU ARE DOING.
	 * Adds a pane to the game, above all other panes, including the timer and score counter.**/
	public void addPaneBelow (Node node) {
		this.getChildren().add(0,node);
	}
	public void addPaneAbove (Node node) {
		this.getChildren().add(node);
	}
	/**Removes a pane from the scene.**/
	public void removePane (Node node) {
		this.getChildren().remove(node);
	}
	
	public void addSprite (Sprite sprite, double x, double y) {
		spritePane.getChildren().add(sprite);
		sprite.moveTo(x, y);
	}
	public void addSprite (Sprite sprite) {
		spritePane.getChildren().add(sprite);
	}
	public void addSprite (Sprite... sprites) {
		spritePane.getChildren().addAll(sprites);
		for (int i = 0; i < sprites.length; i++) {
			sprites[i].moveTo(1, 1);
		}
	}
	public void removeSprite (Sprite sprite) {
		spritePane.getChildren().remove(sprite);
	}
	
	public void restart () {
		gameLoop.stop();
		disarm();
		spritePane.getChildren().clear();
		setBgMusic(backgroundMusicFileName, musicVolume);
		arm();
		onGameStart();
		gameLoop.play();
	}
}
