package spaceinvaders;

import framework.*;
import java.util.ArrayList;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;

public class SpaceInvaders extends GameRootPane {
	
	PixelSprite ship;
	PixelSprite invader1;
	PixelSprite invader2;
	PixelSprite wallLeft;
	PixelSprite wallRight;
	PixelSprite wallTop;
	PixelSprite wallBottom;
	
	int shipPosX;
	int invadersLeft;
	int listNum;
	boolean movingRight = true;
	boolean bulletExists = false;
	boolean movingDown = false;
	boolean bulletImpacted = false;
	
	Score score = new Score(this, 20, Color.WHITE, Color.TRANSPARENT, Pos.TOP_CENTER);
	ArrayList<PixelSprite> listBullets = new ArrayList<PixelSprite>();
	ArrayList<PixelSprite> listInv1 = new ArrayList<PixelSprite>();
	ArrayList<PixelSprite> listInv2 = new ArrayList<PixelSprite>();
	
	private int[][] spriteBullet = new int [][]{{0,1,0},
												{1,1,1},
												{1,1,1},
												{1,1,1},
												{1,1,1}};
	
	public SpaceInvaders ()
	{
		super("Space\nInvaders","press-start.ttf","SpaceInvadersTheme.mp3",50,20);
		
		initMenu(50,30,Color.WHITE, Color.BLACK, "SpaceInvadersTheme.mp3","Kill the aliens\nA/D - Movement\nW - Fire");
		
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.W;}
			public boolean fireOnce() {return true;}
			public void action () {
				if (!bulletExists) {
					PixelSprite bullet = new PixelSprite(spriteBullet,5,10,Color.WHITE);
					addSprite(bullet, shipPosX, 550);
					listBullets.add(bullet);
					bulletExists = true;
				}
			}
		});
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.A;}
			public boolean fireOnce() {return false;}
			public void action () {
				ship.translate(-10, 0);
				if (shipPosX > 25) shipPosX -= 10;
				
			}
		});
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.D;}
			public boolean fireOnce() {return false;}
			public void action () {
				ship.translate(10, 0);
				if (shipPosX < 570) shipPosX += 10;
			}
		});
		addKeyBinding(new KeyAction () {
			public KeyCode getKey() {return KeyCode.E;}
			public boolean fireOnce() {return false;}
			public void action () {
				ship.moveTo(300,550);
			}
		});
	}

	public void onGameStart() {
		shipPosX = 300;
		invadersLeft = 16;
		setBackground(Color.BLACK);
		// Sprite Arrays
		int[][] spriteShip = new int [][]{{0,0,0,0,0,0,1,0,0,0,0,0,0},
										  {0,0,0,0,0,1,1,1,0,0,0,0,0},
										  {0,0,0,0,0,1,1,1,0,0,0,0,0},
										  {0,0,0,0,0,1,1,1,0,0,0,0,0},
										  {0,1,1,1,1,1,1,1,1,1,1,1,0},
										  {1,1,1,1,1,1,1,1,1,1,1,1,1}};		
		int[][] spriteInvader2 = new int [][]{{0,0,1,0,0,0,0,0,1,0,0},
											  {0,0,0,1,0,0,0,1,0,0,0},
											  {0,0,1,1,1,1,1,1,1,0,0},
											  {0,1,1,0,1,1,1,0,1,1,0},
											  {1,1,1,1,1,1,1,1,1,1,1},
											  {1,0,1,1,1,1,1,1,1,0,1},
											  {1,0,1,0,0,0,0,0,1,0,1},
											  {0,0,0,1,1,0,1,1,0,0,0}};		
		int[][] spriteInvader1 = new int [][]{{0,0,0,1,1,0,0,0},
											  {0,0,1,1,1,1,0,0},
											  {0,1,1,1,1,1,1,0},
											  {1,1,0,1,1,0,1,1},
											  {1,1,1,1,1,1,1,1},
											  {0,0,1,0,0,1,0,0},
											  {0,1,0,1,1,0,1,0},
											  {1,0,1,0,0,1,0,1}};
		int[][] spriteWall = new int [][]{{1}};
		// Sprite Creation
		ship = new PixelSprite(spriteShip, 50, 50, Color.WHITE);
		wallLeft = new PixelSprite(spriteWall, 0, 600, Color.TRANSPARENT);
		wallRight = new PixelSprite(spriteWall, 0, 600, Color.TRANSPARENT);
		wallTop = new PixelSprite(spriteWall, 600, 0, Color.TRANSPARENT);
		wallBottom = new PixelSprite(spriteWall, 600, 0, Color.TRANSPARENT);
		// Add Sprite To Scene
		addSprite(ship,300,550);
		addSprite(wallLeft,5,300);
		addSprite(wallRight,595,300);
		addSprite(wallTop,300,5);
		addSprite(wallBottom,300,525);
		// Invader Sprite Creation As ArrayList
		for (int initList = 0; initList < 8; initList++) {
			listInv1.add(new PixelSprite(spriteInvader1,50,50,"enemy",Color.WHITE));
			listInv2.add(new PixelSprite(spriteInvader2,50,50,"enemy",Color.WHITE));
		}
		// Add Invaders To Scene From ArrayList
		listNum = 0;
		for (int x = 40; x <= 550; x += 65)	{
			addSprite(listInv1.get(listNum), x, 100);
			addSprite(listInv2.get(listNum), x, 175);
			listNum++;
		}
	}
	
	public void update() {
		// Invader Movement Checks
		listInv1.forEach(inv -> {
			if (inv.collided(wallRight)) {
				movingRight = false;
				movingDown = true;
			} else if (inv.collided(wallLeft)) {
				movingRight = true;
				movingDown = true;
			}
		});
		listInv2.forEach(inv -> {
			if (inv.collided(wallRight)) {
				movingRight = false;
				movingDown = true;
			} else if (inv.collided(wallLeft)) {
				movingRight = true;
				movingDown = true;
			}
		});
		// Actual Movement Based On Checks
		listInv1.forEach(inv -> {
			if (movingDown)
				inv.translate(0, 20);
			if (movingRight)
				inv.translate(2, 0);
			else
				inv.translate(-2, 0);
		});
		listInv2.forEach(inv -> {
			if (movingDown)
				inv.translate(0, 20);
			if (movingRight)
				inv.translate(2, 0);
			else
				inv.translate(-2, 0);
		});
		movingDown = false;
		// Bullet Impact Check And Kill
		listBullets.forEach(b -> {
			b.translate(0, -20);
			if (b.collided("enemy")) {
				removeSprite(b.getCollided("enemy"));
				bulletImpacted = true;
				score.addToScore(10);
				bulletExists = false;
				invadersLeft--;
			}
			if (b.collided(wallTop)) {
				bulletImpacted = true;
				bulletExists = false;
			}
		});
		// Bullet Removal Based On Impact
		if (bulletImpacted) {
			removeSprite(listBullets.get(0));
			listBullets.clear();
			bulletImpacted = false;
		}
		// End Game 
		if (invadersLeft == 0) { // Player Wins, New Enemies Spawn
			listNum = 0;
			for (int x = 40; x <= 550; x += 65)
			{
				addSprite(listInv1.get(listNum), x, 100);
				addSprite(listInv2.get(listNum), x, 175);
				listNum++;
			}
			shipPosX = 300;
			invadersLeft = 16;
		} else if (wallBottom.collided("enemy")) { // Player Loses, Game Over
			removeSprite(ship);
			listInv1.forEach(inv -> {
				removeSprite(inv);
			});
			listInv2.forEach(inv -> {
				removeSprite(inv);
			});
			super.togglePause();
		}
	}
	
	public void onPause () {
		
	}
	
	public void onResume () {
		
	}
}