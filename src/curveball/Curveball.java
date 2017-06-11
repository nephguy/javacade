package curveball;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;

import framework.*;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.VLineTo;
import javafx.scene.shape.Line;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

/**
 * @author Nick Hansen
 */

public class Curveball extends GameRootPane {
	
	Paddle playerPaddle;
	Paddle enemyPaddle;
	StackPane ballPane;
	Ball ball;
	Rectangle topBallColl;
	Rectangle bottomBallColl;
	Rectangle rightBallColl;
	Rectangle leftBallColl;
	
	// 
	private double verticalInset = 75;
	private double horizontalInset = 10;
	private double scaleDown = 4;
	
	double deltaX;
	double deltaY;
	double ballPathX;
	double ballPathY;
	
	Robot mouseMover;
	double absCenterX;
	double absCenterY;
	boolean firedByRobot = true;
	
	int bounceCount;
	
	public Curveball () {
		super("CURVEBALL","press-start.ttf","TroyBoi - On My Own.mp3",0,144);
		
		initMenu(60, 20, Color.BLACK, Color.LIGHTSLATEGRAY, "", "");
		
		try {
			mouseMover = new Robot();
		} catch (AWTException e) {}
		
	}

	public void onGameStart() {
		// draw the background/aesthetic
		//this.setCursor(Cursor.NONE);
		this.setBackground(Color.BLACK);
		
		
		
		
		// move the paddle and restrict the mouse to the scene
		this.setOnMouseMoved(event -> {
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
		
		
		// init collision boxes so the ball bounces
		topBallColl = newBallColl(false);
		bottomBallColl = newBallColl(false);
		rightBallColl = newBallColl(true);
		leftBallColl = newBallColl(true);
		ball = new Ball();
		ballPane = new StackPane(topBallColl,bottomBallColl,rightBallColl,leftBallColl,ball);
		StackPane.setAlignment(topBallColl, Pos.TOP_CENTER);
		StackPane.setAlignment(bottomBallColl, Pos.BOTTOM_CENTER);
		StackPane.setAlignment(rightBallColl, Pos.CENTER_RIGHT);
		StackPane.setAlignment(leftBallColl, Pos.CENTER_LEFT);
		
		
		
		
		enemyPaddle = new Paddle(4.5/scaleDown,0,0,0,0,Color.RED);
		playerPaddle = new Paddle(4.5,horizontalInset,this.getWidth()-horizontalInset,verticalInset,this.getHeight()-verticalInset,Color.BLUE);
		
		 
		EventHandler<ActionEvent> bounceBall = event -> {
			bounceCount++;
			if (bounceCount%2 == 0) { // player
				if (!ball.getBoundsInParent().intersects(playerPaddle.getBoundsInParent())) ;//resetBall();
				else {
					playerPaddle.flash();
					ballPathX = deltaX;
					ballPathY = deltaY;
				}
			}
			else { // enemy
				if (!ball.getBoundsInParent().intersects(enemyPaddle.getBoundsInParent())) ;//resetBall();
				else {
					enemyPaddle.flash();
				}
			}
		};
		ball.scaleAnimation(1/scaleDown, 1000, true, true, bounceBall);
		addPaneBelow(ballPane);
		addPaneAbove(rightBallColl);
		this.addSprite(enemyPaddle);
		this.addSprite(playerPaddle);
		
	}
	
	private Rectangle newBallColl (boolean vertical) {
		Rectangle rect = new Rectangle(this.getWidth(),this.getHeight());
		if (vertical) rect.setWidth(1);
		else rect.setHeight(1);
		rect.setStroke(Color.WHITE);
		rect.setFill(Color.WHITE);
		return rect;
	}
	
	private void bounceBall () {
		final boolean atRightBorder = ball.getTranslateX() + ball.getWidth()/2 >= ballPane.getWidth()/2;
        final boolean atLeftBorder = ball.getTranslateX() - ball.getWidth()/2 <= -ballPane.getWidth()/2;
        final boolean atBottomBorder = ball.getTranslateY() + ball.getHeight()/2 >= ballPane.getHeight()/2;
        final boolean atTopBorder = ball.getTranslateY() - ball.getHeight()/2 <= -ballPane.getHeight()/2;
		if (atRightBorder || atLeftBorder)
			ballPathX *= -1;
		if (atTopBorder || atBottomBorder)
			ballPathY *= -1;
	}
	
	public void update() { 
		bounceBall();
		ball.translate(ballPathX, ballPathY);
	}
	
	public void onPause () {
		this.setCursor(Cursor.DEFAULT);
	}
	
	public void onResume () {
		this.setCursor(Cursor.NONE);
	}
	
	private void resetBall () {
		ball.crashed();
		ballPathX = ballPathY = 0;
	}
}
