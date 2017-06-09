package spaceinvaders;

import framework.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;

public class SpaceInvaders extends GameRootPane {
	
	PixelSprite ship;
	PixelSprite invader1;
	PixelSprite invader2;
	PixelSprite bullet;
	
	Score score;
	
	public SpaceInvaders () 
	{
		super("Space\nInvaders","press-start.ttf","SpaceInvadersTheme.mp3",50,5);
		
		String tutorialText = "test"+
							  "test2";
		initMenu(50,30,Color.WHITE, Color.BLACK, "SpaceInvadersTheme.mp3","Kill the aliens");
		
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.W;}
			public boolean fireOnce() {return true;}
			public void action () {
				
			}
		});
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.A;}
			public boolean fireOnce() {return false;}
			public void action () {
				ship.translate(-5, 0);
			}
		});
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.D;}
			public boolean fireOnce() {return false;}
			public void action () {
				ship.translate(5, 0);
			}
		});
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.Q;}
			public boolean fireOnce() {return false;}
			public void action () {
				
			}
		});
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.E;}
			public boolean fireOnce() {return false;}
			public void action () {
				
			}
		});
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.R;}
			public boolean fireOnce() {return false;}
			public void action () {
				ship.moveTo(300,600);
			}
		});
	}

	public void onGameStart() {
		score = new Score(this, 20, Color.WHITE, Color.TRANSPARENT, Pos.TOP_CENTER);
		setBackground(Color.BLACK);
		int[][] spriteShip = new int [][]{{0,0,0,0,0,0,1,0,0,0,0,0,0},
										  {0,0,0,0,0,1,1,1,0,0,0,0,0},
										  {0,0,0,0,0,1,1,1,0,0,0,0,0},
										  {0,0,0,0,0,1,1,1,0,0,0,0,0},
										  {0,1,1,1,1,1,1,1,1,1,1,1,0},
										  {1,1,1,1,1,1,1,1,1,1,1,1,1}};		
		int[][] spriteInvader1 = new int [][]{{0,0,1,0,0,0,0,0,1,0,0},
											  {0,0,0,1,0,0,0,1,0,0,0},
											  {0,0,1,1,1,1,1,1,1,0,0},
											  {0,1,1,0,1,1,1,0,1,1,0},
											  {1,1,1,1,1,1,1,1,1,1,1},
											  {1,0,1,1,1,1,1,1,1,0,1},
											  {1,0,1,0,0,0,0,0,1,0,1},
											  {0,0,0,1,1,0,1,1,0,0,0}};		
		int[][] spriteInvader2 = new int [][]{{0,0,0,1,1,0,0,0},
											  {0,0,1,1,1,1,0,0},
											  {0,1,1,1,1,1,1,0},
											  {1,1,0,1,1,0,1,1},
											  {1,1,1,1,1,1,1,1},
											  {0,0,1,0,0,1,0,0},
											  {0,1,0,1,1,0,1,0},
											  {1,0,1,0,0,1,0,1}};		
		int[][] spriteBullet = new int [][]{{0,1,0},
										   {1,1,1},
										   {1,1,1},
										   {1,1,1},
										   {1,1,1}};
									  		  
		ship = new PixelSprite(spriteShip,50,50,Color.WHITE);
		bullet = new PixelSprite(spriteBullet,50,50,Color.WHITE);
		invader1 = new PixelSprite(spriteInvader1,50,50,"enemy",Color.WHITE);
		invader2 = new PixelSprite(spriteInvader2,50,50,"enemy",Color.WHITE);
		
		this.addSprite(ship,100,600);
		for (int x = 10; x <= 400; x += 50)
		{
			this.addSprite(invader1, x, 100);
			this.addSprite(invader2, x, 200);
		}
		//test2.moveTo(100, 100);
		//test2.translateAnimation(0, 100, 1000, true, false);
		//test2.rotateAnimation(360, 1000, true, false);
	}
	
	public void update() {
		if (bullet.collided("enemy")) {
			//removeSprite(bullet.getCollided("enemy"));
			score.addToScore(10);
		}
	}
	
	public void onPause () {
		
	}
	
	public void onResume () {
		
	}
}