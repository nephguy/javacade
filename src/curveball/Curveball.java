package curveball;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;

import framework.*;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

/**
 * @author Nick Hansen
 */

public class Curveball extends GameRootPane {
	
	// parameters of the game, used to make the game easy to changes
	private double verticalInset = 65;
	private double horizontalInset = 10;
	private double scaleDown = 4;
	double startingRate = 1500;
	Color bgStrokeColor = Color.GREEN;
	int numBgBoxes = 8;
	double difficulty = 1;
	int numLives = 3;
	
	// play objects
	Paddle playerPaddle;
	Paddle enemyPaddle;
	Paddle enemyPaddleHitbox;
	Ball ball;
	StackPane ballPane;
	Timeline scaleBallPane;
	EventHandler<MouseEvent> initialHit;
	Score score;
	
	// for player paddle
	double deltaX;
	double deltaY;
	double ballPathX;
	double ballPathY;
	Robot mouseMover;
	double absCenterX;
	double absCenterY;
	boolean firedByRobot = true;
	int mouseMovedCounter;
	
	// for enemy paddle
	double enemyDeltaX;
	double enemyDeltaY;
	double ballX;
	double ballY;
	double enemyPaddleX;
	double enemyPaddleY;
	boolean ballCrashed;
	
	// for score
	int bounceCount;
	HBox lives;
	
	public Curveball () {
		super("CURVEBALL","press-start.ttf","Derp 2 somewhat sped up.wav",35,144);
		String tutorial = "Just like Pong, but 3-D!\n\n" +
						  "Your mouse controls the paddle. To start the game, click the paddle on the ball. " +
						  "Afterward, move the paddle over the ball to hit it. The ball curves based on the paddle's speed. " + 
						  "Curve the ball past your opponent's paddle to earn points. " +
						  "But beware, if you miss the ball, you lose a life.\n\n" + 
						  "How many points can you score?";
		initMenu(60, 20, Color.BLACK, Color.LIGHTSLATEGRAY, "Pong Intro.wav", tutorial);
		
		try {
			mouseMover = new Robot();
		} catch (AWTException e) {}	
	}

	public void onGameStart() {
		
		//general
		score = new Score(this,"Score: ",20,Color.WHITE,Color.TRANSPARENT,Pos.TOP_RIGHT);
		bounceCount = 0;
		setCursor(Cursor.NONE);
		final double playAreaWidth = this.getWidth() - horizontalInset*2;
		final double playAreaHeight = this.getWidth() - verticalInset*2;
		
		// move the paddle and restrict the mouse to the scene
		setOnMouseMoved(event -> {
			mouseMovedCounter = 5;
			if (!paused) {
				absCenterX = MouseInfo.getPointerInfo().getLocation().getX() - event.getX() + this.getWidth()/2;
				absCenterY = MouseInfo.getPointerInfo().getLocation().getY() - event.getY() + this.getHeight()/2;
				if (!firedByRobot) {
					deltaX = event.getX() - 300;
					deltaY = event.getY() - 300;
					//System.out.println(deltaX + " " + deltaY);
					playerPaddle.translate(deltaX, deltaY);
					firedByRobot = true;
					mouseMover.mouseMove((int)absCenterX, (int)absCenterY);
				}
				else firedByRobot = false;
				
			}
		});
		
		// init paddles and ball
		ball = new Ball(4.5);
		ballPane = new StackPane(ball);
		ballPane.setMinSize(this.getWidth()-horizontalInset*2, this.getHeight()-verticalInset*2);
		ballPane.setMaxSize(this.getWidth()-horizontalInset*2, this.getHeight()-verticalInset*2);	
		// dont bother reading these next five lines. they just work
		final double minCorrection = (scaleDown-1)/2;
		final double maxCorrection = (scaleDown-1)/2+1;
		enemyPaddle = new Paddle(4.5/scaleDown, playAreaWidth/scaleDown*minCorrection+horizontalInset , playAreaWidth/scaleDown*maxCorrection+horizontalInset , playAreaHeight/scaleDown*minCorrection+verticalInset , playAreaHeight/scaleDown*maxCorrection+verticalInset,Color.RED);
		enemyPaddleHitbox = new Paddle(4.5, horizontalInset , this.getWidth()-horizontalInset , verticalInset , this.getHeight()-verticalInset);
		playerPaddle = new Paddle(4.5, horizontalInset , this.getWidth()-horizontalInset , verticalInset , this.getHeight()-verticalInset ,Color.BLUE);
		addPaneBelow(ballPane);
		addPaneBelow(enemyPaddle);
		addPaneAbove(enemyPaddleHitbox);
		addSprite(playerPaddle);
		
		// gives the appearance of depth as the ball moves
		scaleBallPane = new Timeline ();
		scaleBallPane.getKeyFrames().add(new KeyFrame(Duration.millis(startingRate),event -> {bounceBallOffPaddle();},new KeyValue(ballPane.scaleXProperty(),1/scaleDown)));
		scaleBallPane.getKeyFrames().add(new KeyFrame(Duration.millis(startingRate),new KeyValue(ballPane.scaleYProperty(),1/scaleDown)));
		scaleBallPane.getKeyFrames().add(new KeyFrame(Duration.millis(startingRate*2),event -> {bounceBallOffPaddle();},new KeyValue(ballPane.scaleXProperty(),1)));
		scaleBallPane.getKeyFrames().add(new KeyFrame(Duration.millis(startingRate*2),new KeyValue(ballPane.scaleYProperty(),1)));
		scaleBallPane.setCycleCount(Animation.INDEFINITE);
		
		// adds life counter
		lives = new HBox(Util.styleLabel(font, 20, Color.WHITE, Color.TRANSPARENT, false, false, "Lives:"));
		for (int i = 0; i < numLives; i++)
			lives.getChildren().add(new Ball(1));
		lives.setSpacing(10);
		lives.setPadding(new Insets(20));
		lives.setAlignment(Pos.CENTER_LEFT);
		lives.setMaxSize(170 + numLives*26, 65);
		addPaneAbove(lives);
		StackPane.setAlignment(lives, Pos.TOP_LEFT);
		
		//fires the ball at the start of the game
		initialHit = new EventHandler<MouseEvent>() {
			public void handle (MouseEvent event) {
				if (!paused && ballTouched(playerPaddle)) {
					playerPaddle.flash();
					bounceCount++;
					if (deltaX != 0) ballPathX = deltaX/Math.abs(deltaX) * Math.sqrt(Math.abs(deltaX)); // the first term retains the sign,
					if (deltaY != 0) ballPathY = deltaY/Math.abs(deltaY) * Math.sqrt(Math.abs(deltaY)); // the second term recudes the velocity
					ballPane.setBorder(new Border(new BorderStroke(Color.WHITE,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,new BorderWidths(2))));
					scaleBallPane.play();
					removeEventHandler(MouseEvent.MOUSE_PRESSED, this);
				}
			}
		};
		addEventHandler(MouseEvent.MOUSE_PRESSED, initialHit);
		
		// draw the background
		setBackground(Color.BLACK);
		addBgRect(1,Color.TRANSPARENT);
		for (int i = 0; i < numBgBoxes; i++) {
			Color bgColor = Color.TRANSPARENT;
			if (i == numBgBoxes-1) bgColor = Color.BLACK;
			addBgRect(1/Math.sqrt(scaleDown*(scaleDown/numBgBoxes*(i+1))),bgColor);
		}
		Path bgX = new Path(new MoveTo(0,0),
							new LineTo(playAreaWidth,playAreaHeight),
							new MoveTo(playAreaWidth,0),
							new LineTo(0,playAreaHeight));
		bgX.setFill(Color.TRANSPARENT);
		bgX.setStroke(bgStrokeColor);
		bgX.setStrokeWidth(1);
		addPaneBelow(bgX);
		
	}
	
	private void bounceBallOffWall () {
		double ballRadius = ball.getWidth()*ballPane.getScaleX()/2;
		double ballPaneWidth = ballPane.getWidth()*ballPane.getScaleX();
		double ballPaneHeight = ballPane.getHeight()*ballPane.getScaleY();
		double ballTranslateX = (ball.getTranslateX()+ballPathX)*ballPane.getScaleX();
		double ballTranslateY = (ball.getTranslateY()+ballPathY)*ballPane.getScaleY();
		final boolean atRightBorder = ballTranslateX + ballRadius >= ballPaneWidth/2;
        final boolean atLeftBorder = ballTranslateX - ballRadius <= -ballPaneWidth/2;
        final boolean atBottomBorder = ballTranslateY + ballRadius >= ballPaneHeight/2;
        final boolean atTopBorder = ballTranslateY - ballRadius <= -ballPaneHeight/2;
		if (atRightBorder || atLeftBorder)
			ballPathX *= -1;
		if (atTopBorder || atBottomBorder)
			ballPathY *= -1;
	}
	
	private void bounceBallOffPaddle() {
		if (bounceCount%2 == 0) { // player
			if (!ballTouched(playerPaddle)) {
				ball.crashed();
				removeLife();
			}
			else {
				playerPaddle.flash();
				if (deltaX != 0) ballPathX = deltaX/Math.abs(deltaX) * Math.sqrt(Math.abs(deltaX)); // the first term retains the sign,
				if (deltaY != 0) ballPathY = deltaY/Math.abs(deltaY) * Math.sqrt(Math.abs(deltaY)); // the second term recudes the velocity
				bounceCount++;
			}
		}
		else { // enemy
			if (!ballTouched(enemyPaddleHitbox)) {
				ball.crashed();
				score.addToScore(1);
				resetGame(true);
			}
			else {
				enemyPaddle.flash();
				bounceCount++;
			}
		}
	}
	
	private boolean ballTouched (Node node) {
		final double ballMinX = ball.getBoundsInParent().getMinX() + horizontalInset;
		final double ballMaxX = ball.getBoundsInParent().getMaxX() + horizontalInset;
		final double ballMinY = ball.getBoundsInParent().getMinY() + verticalInset;
		final double ballMaxY = ball.getBoundsInParent().getMaxY() + verticalInset;
		Bounds nodeBounds = node.getBoundsInParent();
		
		final boolean minX = ballMinX > nodeBounds.getMinX() && ballMinX < nodeBounds.getMaxX();
		final boolean maxX = ballMaxX > nodeBounds.getMinX() && ballMaxX < nodeBounds.getMaxX();
		final boolean minY = ballMinY > nodeBounds.getMinY() && ballMinY < nodeBounds.getMaxY();
		final boolean maxY = ballMaxY > nodeBounds.getMinY() && ballMaxY < nodeBounds.getMaxY();
		
		final boolean corner = (minX && minY) || (minX && maxY) || (maxX && minY) || (maxX && maxY);
		final boolean edge = (minX && minY && maxY) || (maxX && minY && maxY) || (minY && minX && maxX) | (maxY && minX && maxX);
		final boolean center = minX && minY && maxX && maxY;
		
		return corner || edge || center;
	}
	
	private void resetGame (boolean wait) {
		// stop game
		scaleBallPane.stop();
		ballPathX = ballPathY = 0;
		
		// reset game
		Platform.runLater(() -> {
			try {
				if (wait) Thread.sleep(2000);
				bounceCount = 0;
				ballPane.setScaleX(1);
				ballPane.setScaleY(1);
				enemyPaddle.moveTo(getWidth()/2, getHeight()/2);
				enemyPaddleHitbox.moveTo(getWidth()/2, getHeight()/2);
				ballPane.getChildren().remove(ball);
				ball = new Ball(4.5);
				ballPane.getChildren().add(ball);
				addEventHandler(MouseEvent.MOUSE_PRESSED, initialHit);
			} catch (InterruptedException e) {e.printStackTrace();}
		});
	}
	
	private void removeLife () {
		if (lives.getChildren().size() > 1) {
			lives.getChildren().remove(lives.getChildren().size()-1);
			resetGame(true);
		}
		else {
			resetGame(false);
			removePane(score);
			Label gameOver = Util.styleLabel(font, 60, Color.WHITE, Color.TRANSPARENT, false, false, "GAME OVER");
			Label restart = Util.styleLabel(font, 25, Color.WHITE, Color.TRANSPARENT, true, false, "Restart?");
			restart.setOnMouseClicked(event -> {
				restart();
			});
			VBox gameOverScreen = new VBox (gameOver,score,restart);
			gameOverScreen.setMaxSize(getWidth(), getHeight());
			gameOverScreen.setBackground(new Background(new BackgroundFill(Color.valueOf("#d3d3d350"),CornerRadii.EMPTY,Insets.EMPTY)));
			gameOverScreen.setAlignment(Pos.CENTER);
			paused = true;
			setCursor(Cursor.DEFAULT);
			addPaneAbove(gameOverScreen);
		}
	}
	
	private void addBgRect (double scale, Color bgColor) {
		Rectangle bgRect = new Rectangle (this.getWidth() - horizontalInset*2,this.getWidth() - verticalInset*2);
		bgRect.setStroke(bgStrokeColor);
		bgRect.setStrokeWidth(2);
		bgRect.setFill(bgColor);
		bgRect.setScaleX(scale);
		bgRect.setScaleY(scale);
		addPaneBelow(bgRect);
	}
	
	private double clampLessThan (double value, double maxVal) {
		if (value > 0) return value > maxVal ? maxVal : value;
		else if (value < 0) return value < -maxVal ? -maxVal : value;
		else return value;
	}
	
	protected void update() {
		if (mouseMovedCounter == 0) deltaX = deltaY = 0;	// fixes deltaX and deltaY having a nonzero value
		else mouseMovedCounter--;							// when the paddle is stationary
		bounceBallOffWall();
		ball.translate(ballPathX, ballPathY);
		
		//translate enemy paddle
		enemyPaddleX = enemyPaddleHitbox.getTranslateX() + this.getWidth()/2;
		enemyPaddleY = enemyPaddleHitbox.getTranslateY() + this.getHeight()/2;
		ballX = ball.getTranslateX() + this.getWidth()/2;
		ballY = ball.getTranslateY() + this.getHeight()/2;
		enemyDeltaX = clampLessThan(ballX - enemyPaddleX, difficulty);
		enemyDeltaY = clampLessThan(ballY - enemyPaddleY, difficulty);
		enemyPaddleHitbox.translate(enemyDeltaX, enemyDeltaY);
		enemyPaddle.translate(enemyDeltaX/scaleDown, enemyDeltaY/scaleDown);
	}
	
	protected void onPause () {
		this.setCursor(Cursor.DEFAULT);
		if (scaleBallPane.getStatus() == Status.RUNNING) scaleBallPane.pause();
	}
	
	protected void onResume () {
		this.setCursor(Cursor.NONE);
		if (scaleBallPane.getStatus() == Status.PAUSED) scaleBallPane.play();
	}
}
