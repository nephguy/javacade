package curveball;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;

import framework.*;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.VLineTo;
import javafx.geometry.Pos;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * @author Nick Hansen
 */

public class Curveball extends GameRootPane {
	
	Paddle playerPaddle;
	Paddle enemyPaddle;
	Ball ball;
	
	double deltaX;
	double deltaY;
	double ballX;
	double ballY;
	Robot mouseMover;
	double absCenterX;
	double absCenterY;
	boolean firedByRobot = true;
	
	int bounceCount;
	
	public Curveball () {
		super("CURVEBALL","TroyBoi - On My Own.mp3");
		
		initMenu(60, 20, Color.BLACK, Color.LIGHTSLATEGRAY, "", "");
		
		try {
			mouseMover = new Robot();
		} catch (AWTException e) {}
		/*
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.W;}
			public boolean fireOnce() {return false;}
			public void action () {
				playerPaddle.translate(0, -3);
			}
		});
		*/
		
	}

	public void onGameStart() {
		this.setBackground(Color.BLACK);
		
		// draw the outside
		for (int i = 0; i < 5; i++) {
			double width = this.getWidth()-20-this.getWidth()/10*i;
			double nextWidth = this.getWidth()-20-this.getWidth()/10*(i+1);
			double height = this.getHeight()-100-this.getHeight()/10*i;
			double nextHeight = this.getHeight()-100-this.getHeight()/10*(i+1);
			double slashWidth = (width-nextWidth)/2;
			double slashHeight = (height-nextHeight)/2;
			Path border = new Path ();
			border.setStroke(Color.WHITE);
			border.setStrokeWidth(1);
			border.getElements().addAll(new MoveTo(0,0), new LineTo(slashWidth,slashHeight), new LineTo(0,0),
										new HLineTo(width), new LineTo(width-slashWidth, slashHeight), new LineTo(width,0),
										new VLineTo(height), new LineTo(width-slashWidth, height-slashHeight), new LineTo(width,height),
										new HLineTo(0), new LineTo(slashWidth,height-slashHeight), new LineTo(0,height),
										new VLineTo(0));
			
			this.addPaneBelow(border);
			if (i == 4) {
				Rectangle finalBorder = new Rectangle (nextWidth,nextHeight,Color.TRANSPARENT);
				finalBorder.setStroke(Color.WHITE);
				finalBorder.setStrokeWidth(1);
				this.addPaneBelow(finalBorder);
			}
		}
		
		
		this.setOnMouseMoved(event -> {
			if (!paused) {
				absCenterX = MouseInfo.getPointerInfo().getLocation().getX() - event.getX() + this.getWidth()/2;
				absCenterY = MouseInfo.getPointerInfo().getLocation().getY() - event.getY() + this.getHeight()/2;
				if (!firedByRobot) {
					deltaX = event.getX() - 300;
					deltaY = event.getY() - 300;
					playerPaddle.translate(deltaX, deltaY);
					firedByRobot = true;
					mouseMover.mouseMove((int)absCenterX, (int)absCenterY);
				}
				else firedByRobot = false;
				
			}
		});
		
		this.setCursor(Cursor.NONE);
		enemyPaddle = new Paddle(2.25,0,0,0,0,Color.RED);
		ball = new Ball();
		playerPaddle = new Paddle(4.5,10,590,50,550,Color.BLUE);
		
		ball.scaleAnimation(0.5, 1000, true, true, event -> {
			bounceCount++;
			if (bounceCount%2 == 0) { // player
				if (!ball.getBoundsInParent().intersects(playerPaddle.getBoundsInParent())) resetBall();
				else {
					playerPaddle.flash();
					ballX = deltaX;
					ballY = deltaY;
				}
			}
			else { // enemy
				if (!ball.getBoundsInParent().intersects(enemyPaddle.getBoundsInParent())) resetBall();
				else {
					enemyPaddle.flash();
				}
			}
		});
		
		this.addSprite(enemyPaddle);
		this.addSprite(ball);
		this.addSprite(playerPaddle,200,200);
		
		this.enableTimer(20, Color.GRAY, Color.LIGHTGRAY, Pos.TOP_CENTER);
		
	}
	
	public void update() {
		
		ball.translate(ballX, ballY);
	}
	
	public void onPause () {
		this.setCursor(Cursor.DEFAULT);
	}
	
	public void onResume () {
		this.setCursor(Cursor.NONE);
	}
	
	private void resetBall () {
		ball.crashed();
		ballX = ballY = 0;
	}
}
