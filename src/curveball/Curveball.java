package curveball;

import framework.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.geometry.Pos;

/**
 * @author Nick Hansen
 */

public class Curveball extends GameRootPane {
	
	PixelSprite paddle;
	
	public Curveball () {
		super("Curveball","TroyBoi - On My Own.mp3");
		
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.W;}
			public boolean fireOnce() {return false;}
			public void action () {
				paddle.translate(0, -3);
			}
		});
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.A;}
			public boolean fireOnce() {return false;}
			public void action () {
				paddle.translate(-3, 0);
			}
		});
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.S;}
			public boolean fireOnce() {return false;}
			public void action () {
				paddle.translate(0, 3);
			}
		});
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.D;}
			public boolean fireOnce() {return false;}
			public void action () {
				paddle.translate(3, 0);
			}
		});
		
	}

	public void onGameStart() {
		int[][] paddleArray = Sprite.spriteFromFile(this,"paddle.txt",29,45);
		paddle = new PixelSprite(paddleArray,29*4.5,45*4.5,"",Color.BLUE,Paint.valueOf("#ADD8E680"),Color.BLUE);
		this.addSprite(paddle,200,200);
	}
	
	public void update() {
		
	}
	
	public void onPause () {
		
	}
	
	public void onResume () {
		
	}
	
}
