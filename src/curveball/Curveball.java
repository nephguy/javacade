package curveball;

import framework.*;
import framework.sprite.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

/**
 * @author Nick Hansen
 */

public class Curveball extends GameRootPane {
	
	PixelSprite test;
	PixelSprite test2;
	
	public Curveball () {
		super("curveball", "Curveball", "TroyBoi - On My Own.mp3");
		
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.W;}
			public boolean fireOnce() {return false;}
			public void action () {
				test.translate(0, -5);
			}
		});
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.S;}
			public boolean fireOnce() {return false;}
			public void action () {
				test.translate(0, 5);
			}
		});
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.A;}
			public boolean fireOnce() {return false;}
			public void action () {
				test.translate(-5, 0);
			}
		});
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.D;}
			public boolean fireOnce() {return false;}
			public void action () {
				test.translate(5, 0);
			}
		});
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.Q;}
			public boolean fireOnce() {return false;}
			public void action () {
				test.rotate(-5);
			}
		});
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.E;}
			public boolean fireOnce() {return false;}
			public void action () {
				test.rotate(5);
			}
		});
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.R;}
			public boolean fireOnce() {return false;}
			public void action () {
				test.rotateToNearest90Animation(1000, false, true);
			}
		});
		
	}

	public void onGameStart() {
		setBackground(Color.ALICEBLUE);
		int[][] sprite = new int [][]{{1,0,0,0,1},
									  {1,0,0,0,1},
									  {1,0,0,0,1},
									  {1,0,0,0,1},
									  {1,0,0,0,1}};
									  		  
		test = new PixelSprite (sprite,Color.BLUE,50,50);
		test2 = new PixelSprite (sprite,Color.BLACK,50,50,"test2");
		
		test.translateAnimation(20, 100, 250, true, true);
		
		this.addSprite(test, test2);
		test2.moveTo(100, 100);
		test2.translateAnimation(0, 100, 1000, true, false);
		test2.rotateAnimation(360, 1000, true, false);
	}
	
	public void update() {
		if (test.collided("test2")) {
			System.out.println("collided");
			this.removeSprite(test.getCollided("test2"));
		}
	}
	
	public void onPause () {
		
	}
	
	public void onResume () {
		
	}
	
}
