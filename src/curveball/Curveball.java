package curveball;

import framework.*;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.geometry.Pos;

/**
 * @author Nick Hansen
 */

public class Curveball extends GameRootPane {
	
	PixelSprite playerPaddle;
	PixelSprite enemyPaddle;
	PixelSprite ball;
	
	public Curveball () {
		super("CURVEBALL","TroyBoi - On My Own.mp3");
		
		initMenu(60, 20, Color.LIGHTSKYBLUE, Util.radialGradient(0.5, 0.5, Color.DARKSLATEGRAY, Color.BLACK), "", "");
		
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.W;}
			public boolean fireOnce() {return false;}
			public void action () {
				playerPaddle.translate(0, -3);
			}
		});
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.A;}
			public boolean fireOnce() {return false;}
			public void action () {
				playerPaddle.translate(-3, 0);
			}
		});
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.S;}
			public boolean fireOnce() {return false;}
			public void action () {
				playerPaddle.translate(0, 3);
			}
		});
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.D;}
			public boolean fireOnce() {return false;}
			public void action () {
				playerPaddle.translate(3, 0);
			}
		});
		
	}

	public void onGameStart() {
		this.setCursor(Cursor.NONE);
		int[][] paddleArray = PixelSprite.spriteFromFile(this,"paddle.txt",29,45);
		int[][] ballArray = PixelSprite.spriteFromFile(this, "ball.txt", 16, 16);
		enemyPaddle = new PixelSprite(paddleArray,29*2.25,45*2.25,"paddle",Color.RED,Paint.valueOf("#FF450080"),Color.RED);
		ball = new PixelSprite(ballArray,16*4.5,16*4.5,"ball",Color.DARKORANGE,Color.ORANGE,Color.GOLD);
		playerPaddle = new PixelSprite(paddleArray,29*4.5,45*4.5,"paddle",Color.BLUE,Paint.valueOf("#ADD8E680"),Color.BLUE);
		
		ball.scaleAnimation(0.5, 1000, true, true);
		
		this.addSprite(enemyPaddle);
		this.addSprite(ball);
		this.addSprite(playerPaddle,200,200);
		
		this.enableTimer(20, Color.GRAY, Color.LIGHTGRAY, Pos.TOP_CENTER);
		
	}
	
	public void update() {
		playerPaddle.moveTo(this.getMouseX(), this.getMouseY());
	}
	
	public void onPause () {
		this.setCursor(Cursor.DEFAULT);
	}
	
	public void onResume () {
		this.setCursor(Cursor.NONE);
	}
	
}
