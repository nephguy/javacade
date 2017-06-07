package curveball;

import framework.PixelSprite;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class Paddle extends PixelSprite {

	public Paddle (double scale, double lowBoundX, double highBoundX, double lowBoundY, double highBoundY, Color color) {
		super(PixelSprite.parseSprite("curveball", "paddle.txt", 45, 29),45*scale,29*scale,"paddle",
				color,Paint.valueOf("#d3d3d340")/*color.deriveColor(1, 0.5, 1.25, 0.25)*/,color);
		this.setSpriteBounds(lowBoundX, highBoundX, lowBoundY, highBoundY);
	}
	
	public void flash () {
		Path p = (Path)this.getChildren().get(1);
		Color defaultFill = (Color)p.getFill();
		Paint flashFill = defaultFill.deriveColor(1, 1, 1.5, 1.5);
		Timeline flash = new Timeline(new KeyFrame(Duration.millis(0),new KeyValue(p.fillProperty(),flashFill)),
									  new KeyFrame(Duration.millis(350),new KeyValue(p.fillProperty(),defaultFill)));
		flash.play();
	}
	
}
