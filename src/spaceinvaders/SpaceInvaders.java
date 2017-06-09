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
	boolean movingRight = true;
	boolean bulletExists = false;
	boolean movingDown = false;
	boolean bulletImpacted = false;
	
	Score score;
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
		super("Space\nInvaders","press-start.ttf","SpaceInvadersTheme.mp3",50,50);
		
		initMenu(50,30,Color.WHITE, Color.BLACK, "SpaceInvadersTheme.mp3","Kill the aliens");
		
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
		score = new Score(this, 20, Color.WHITE, Color.TRANSPARENT, Pos.TOP_CENTER);
		shipPosX = 300;
		invadersLeft = 16;
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
		int[][] spriteWall = new int [][]{{1}};
									  		  
		ship = new PixelSprite(spriteShip,50,50,Color.WHITE);
		wallLeft = new PixelSprite(spriteWall, 0, 600, Color.TRANSPARENT);
		wallRight = new PixelSprite(spriteWall, 0, 600, Color.TRANSPARENT);
		wallTop = new PixelSprite(spriteWall, 600, 0, Color.TRANSPARENT);
		wallBottom = new PixelSprite(spriteWall, 600, 0, Color.TRANSPARENT);
		
		this.addSprite(ship,300,550);
		this.addSprite(wallLeft,5,300);
		this.addSprite(wallRight,595,300);
		this.addSprite(wallTop,300,5);
		this.addSprite(wallBottom,300,575);
		
		for (int initList = 0; initList < 8; initList++) {
			listInv1.add(new PixelSprite(spriteInvader1,50,50,"enemy",Color.WHITE));
			listInv2.add(new PixelSprite(spriteInvader2,50,50,"enemy",Color.WHITE));
		}
		
		int listNum = 0;
		
		for (int x = 40; x <= 550; x += 65)
		{
			this.addSprite(listInv1.get(listNum), x, 100);
			this.addSprite(listInv2.get(listNum), x, 175);
			listNum++;
		}
	}
	
	public void update() {
		//wallBottom.collided("enemy")
		System.out.println(listBullets);
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
		if (bulletImpacted) {
			removeSprite(listBullets.get(0));
			listBullets.clear();
			bulletImpacted = false;
		}
		
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
		
		listInv1.forEach(inv -> {
			if (inv.collided(ship)) {
				removeSprite(ship);
			}
			if (movingDown) {
				inv.translate(0, 20);
			}
			if (movingRight) {
				inv.translate(2, 0);
			} else if (!movingRight) {
				inv.translate(-2, 0);
			}
		});
		listInv2.forEach(inv -> {
			if (inv.collided(ship)) {
				removeSprite(ship);
				
			}
			if (movingDown) {
				inv.translate(0, 20);
			}
			if (movingRight) {
				inv.translate(2, 0);
			} else if (!movingRight) {
				inv.translate(-2, 0);
			}
		});
		
		if (invadersLeft == 0) {
			int listNum = 0;
			
			for (int x = 40; x <= 550; x += 65)
			{
				this.addSprite(listInv1.get(listNum), x, 100);
				this.addSprite(listInv2.get(listNum), x, 175);
				listNum++;
			}
			invadersLeft = 16;
		} else if (wallBottom.collided("enemy")) {
			listInv1.forEach(inv -> {
				removeSprite(inv);
			});
			listInv2.forEach(inv -> {
				removeSprite(inv);
			});
		}
		
		movingDown = false;
	}
	
	public void onPause () {
		
	}
	
	public void onResume () {
		
	}
}