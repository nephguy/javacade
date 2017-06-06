package curveball;

import framework.PixelSprite;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Ball extends PixelSprite{

	public Ball () {
		super(PixelSprite.parseSprite("curveball", "ball.txt", 16, 16),16*4.5,16*4.5,"ball",Color.DARKORANGE,Color.ORANGE,Color.GOLD);
	}
	
	public void crashed () {
		stop();
		Paint fadedRed = Paint.valueOf("80000080");
		this.getChildren().add(new PixelSprite(PixelSprite.parseSprite("curveball", "ball.txt", 16, 16),16*4.5,16*4.5,"ball",fadedRed,fadedRed,fadedRed));
	}
}
