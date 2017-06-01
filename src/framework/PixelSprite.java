package framework;

import java.util.ArrayList;

import javafx.scene.shape.Path;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.VLineTo;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.paint.Paint;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.geometry.Bounds;

/**
 * @author Nick Hansen
 */

public class PixelSprite extends Sprite {
	
	boolean constrainToScene;
	
	private Timeline scale = new Timeline();
	private Timeline translate = new Timeline();
	private Timeline rotate = new Timeline();
	private boolean interruptTranslate = false;
	private boolean interruptScale = false;
	private boolean interruptRotate = false;
	
	// needed for moveTo
	double height;
	double width;
	
	ArrayList<Rectangle> pixels;
	
	/**Creates a pixelated sprite.
	 * A 2-D array of integers is passed in to define which pixels to draw.
	 * For each element of the array, if it is equal to anything except 0, a pixel will be drawn there.
	 * Sprites can be more than one color. Each number in the int array specifies a unique color.
	 * The integers in the array must count up from 0, and 
	 * the number of colors passed in must be equal to the highest number in the int array.
	 * <p>
	 * For example, 
	 * <pre><code> int[][] tSprite = new int[][] {{1,1,1,1,1},
	 * 				{0,0,1,0,0},
	 * 				{0,0,1,0,0},
	 * 				{0,0,1,0,0},
	 * 				{0,0,1,0,0}};
	 * </code></pre>
	 * would create a monochrome T-shaped sprite.
	 * <p>
	 * The sprite does not have to be continuous, i.e. the individual pixels do not need to be connected, so.
	 * 
	 * <pre><code> int[][] iSprite = new int[][] {{0,0,1,0,0},
	 * 				{0,0,0,0,0},
	 * 				{0,0,2,0,0},
	 * 				{0,0,2,0,0},
	 * 				{0,0,2,0,0}};
	 * </code></pre>
	 * would be valid, and create a multi-colored, i-shaped sprite.
	 * <p> 
	 * Despite whatever pixels you specify, the sprite will always have the height <code>realHeight</code> and the width <code>realWidth</code>.
	 * If this sprite is one of a family of sprites, set the id equal to the family’s id, to allow for checking collision with all of them simultaneously.
	 * 
	 * @param pixels an integer array to define the sprite
	 * @param realHeight height of the entire sprite, NOT each pixel
	 * @param realWidth width of the entire sprite, NOT each pixel
	 * @param id id of this sprite, used in checking collisions
	 * @param fills the colors of the sprite.
	 * **/
	public PixelSprite (int[][] pixels, double realHeight, double realWidth, String id, Paint... fills) {
		double pHeight = realHeight / pixels.length;
		double pWidth = realWidth / pixels[0].length;
		height = realHeight;
		width = realWidth;
		
		Path[] paths = new Path[fills.length]; 
		for (int i = 0; i < paths.length; i++) {
			paths[i] = new Path();
			paths[i].setFill(fills[i]);
			paths[i].setId(id);
			paths[i].setStrokeWidth(0);
		}
		
		for (int y = 0; y < pixels.length; y++) {
			for (int x = 0; x < pixels[y].length; x++) {
				final int pixelNum = pixels[y][x];
				if (pixelNum != 0) {
					paths[pixelNum-1].getElements().add(new MoveTo(pWidth*x,pHeight*y));
					paths[pixelNum-1].getElements().add(new VLineTo(pHeight*(y+1)));
					paths[pixelNum-1].getElements().add(new HLineTo(pWidth*(x+1)));
					paths[pixelNum-1].getElements().add(new VLineTo(pHeight*y));
					paths[pixelNum-1].getElements().add(new HLineTo(pWidth*x));
				}
			}
		}
		
		constrainToScene = true;
		setId(id);
		this.setMaxSize(realWidth, realHeight);
		this.setMinSize(realWidth, realHeight);
		for (Path p : paths) {this.getChildren().add(p);}
	}
	
	/**SEE OTHER CONSTRUCTOR FOR DETAILED EXPLANATION OF THIS CLASS
	 * <p>
	 * Creates a pixel sprite without an id.
	 * **/
	public PixelSprite (int[][] pixels, double realHeight, double realWidth, Paint... fills) {
		this(pixels,realHeight,realWidth,"",fills);
	}
	
/****************************************************************************************************/
	
	/**Immediately scales the sprite by a specified factor.
	 * @param scaleBy the factor by which to scale the sprite to
	 * **/
	public void scale (double scaleBy) {
		if (interruptScale && scale.getCurrentRate() != 0) scale.stop();
		this.setScaleX(scaleBy);
		this.setScaleY(scaleBy);
	}
	/**Smoothly scales the sprite by a specified factor over a specified period of time.
	 * @param scaleBy the factor by which the sprite is to be scaled
	 * @param timeInMs set the length of one cycle (initial -> specified scale)
	 * @param cycle set if the animation is to cycle indefinitely between the initial and specified scale
	 * @param allowInterrupt set if the animation can be stopped when a manual scale is requested
	 * **/
	public void scaleAnimation (double scaleBy, double timeInMs, boolean cycle, boolean allowInterrupt) {
		interruptScale = allowInterrupt;
		scale.getKeyFrames().clear();	// APPARENTLY, to stop the animation, you NEED
		scale = new Timeline ();		// these two lines in EXACTLY this order. I have no idea why.
		scale.getKeyFrames().add(new KeyFrame(Duration.millis(timeInMs),new KeyValue(this.scaleXProperty(),scaleBy)));
		scale.getKeyFrames().add(new KeyFrame(Duration.millis(timeInMs),new KeyValue(this.scaleYProperty(),scaleBy)));
		if (cycle) {
			scale.setAutoReverse(cycle);
			scale.setCycleCount(Animation.INDEFINITE);
		}
		scale.play();
	}
	
/****************************************************************************************************/
	
	/**Immediately translate the sprite by the specified amounts.
	 * Interrupts any current translation animation if allowed to.
	 * <p>
	 * It is recommended to use this function for ANY AND ALL player-controlled movement.
	 * Do so by setting the translation to a small value, like +-5 pixels.
	 * <p>
	 * If constrainToScene = true, this prevents possible movement outside of the scene.
	 * @param x the horizontal translation
	 * @param y the vertical translation
	 * **/
	public void translate (double x, double y) {
		if (interruptTranslate && translate.getCurrentRate() != 0) translate.stop();
		if (constrainToScene) {
			double maxWidth = this.getScene().getWidth();
			double maxHeight = this.getScene().getHeight();
			Bounds bounds = this.getBoundsInParent();
			if (bounds.getMaxX() + x > maxWidth ||
				bounds.getMinX() + x < 0 ||
				bounds.getMaxY() + y > maxHeight ||
				bounds.getMinY() + y < 0) return;
		}
		this.setTranslateX(this.getTranslateX() + x);
		this.setTranslateY(this.getTranslateY() + y);
	}
	
	/**Smoothly translates the sprite from its current position to the specified position over a period of time.
	 * @param x the distance by which the sprite is to be moved horizontally from its current position
	 * @param y the distance by which the sprite is to be moved vertically from its current position
	 * @param timeInMs set the length of one cycle (initial -> specified translation)
	 * @param cycle set if the animation is to cycle indefinitely between the initial and specified location
	 * @param allowInterrupt set if the animation can be stopped when a manual translate is requested
	 * **/
	public void translateAnimation (double x, double y, double timeInMs, boolean cycle, boolean allowInterrupt) {
		interruptTranslate = allowInterrupt;
		translate.getKeyFrames().clear();
		translate = new Timeline ();
		translate.getKeyFrames().add(new KeyFrame(Duration.millis(timeInMs),new KeyValue(this.translateXProperty(),x + this.getTranslateX())));
		translate.getKeyFrames().add(new KeyFrame(Duration.millis(timeInMs),new KeyValue(this.translateYProperty(),y + this.getTranslateY())));
		if (cycle) {
			translate.setAutoReverse(cycle);
			translate.setCycleCount(Animation.INDEFINITE);
		}
		translate.play();
	}
	
/****************************************************************************************************/
	
	/**Immediately moves the sprite to the specified coordinates.
	 * Interrupts any current translation animation if allowed to.
	 * <p>
	 * It is recommended to use this function ONLY to initialize a sprite's position.
	 * @param x the x-coordinate to which the sprite will be moved
	 * @param y the y-coordinate to which the sprite will be moved
	 * **/
	public void moveTo (double x, double y) {
		if (interruptTranslate && translate.getCurrentRate() != 0) translate.stop();
		double maxX = this.getParent().getScene().getWidth();
		double maxY = this.getParent().getScene().getHeight();
		this.setTranslateX(Util.clamp(x/maxX)*maxX - maxX/2 + width/2);
		this.setTranslateY(Util.clamp(y/maxY)*maxY - maxY/2 + height/2);
	}
	
	/**Smoothly moves the sprite from its current position to the specified position over a period of time. 
	 * @param x the x-coordinate to which the sprite is to be moved
	 * @param y the y-coordinate to which the sprite is to be moved
	 * @param timeInMs set the length of one cycle (initial -> specified coordinates)
	 * @param cycle set if the animation is to cycle indefinitely between the initial and specified location
	 * @param allowInterrupt set if the animation can be stopped when a manual translate is requested
	 * **/
	public void moveToAnimation (double x, double y, double timeInMs, boolean cycle, boolean allowInterrupt) {
		interruptTranslate = allowInterrupt;
		translate.getKeyFrames().clear();
		translate = new Timeline ();
		double maxX = this.getParent().getScene().getWidth();
		double maxY = this.getParent().getScene().getHeight();
		translate.getKeyFrames().add(new KeyFrame(Duration.millis(timeInMs),new KeyValue(this.translateXProperty(),Util.clamp(x/maxX)*maxX-maxX/2)));
		translate.getKeyFrames().add(new KeyFrame(Duration.millis(timeInMs),new KeyValue(this.translateYProperty(),Util.clamp(y/maxY)*maxY-maxY/2)));
		if (cycle) {
			translate.setAutoReverse(cycle);
			translate.setCycleCount(Animation.INDEFINITE);
		}
		translate.play();
	}
	
/****************************************************************************************************/
	
	/**Immediately rotates the sprite by a certain number of degrees from its current rotation.
	 * Interrupts any current rotation animation if allowed to.
	 * <p>
	 * It is recommended to use this function for ANY AND ALL player-controlled rotation.
	 * Do so by setting the rotation to a small angle, like +-5 degrees.
	 * @param deg the degrees to rotate from the sprite's current rotation
	 * **/
	public void rotate (double deg) {
		if (interruptRotate && rotate.getCurrentRate() != 0) rotate.stop();
		this.setRotate(this.getRotate() + deg);
	}
	
	/**Smoothly rotates the sprite the specified amount of degrees from its current rotation, over a specified period of time.
	 * @param deg the distance by which the sprite is to be rotated from its current position
	 * @param timeInMs set the length of one cycle (initial -> specified angle)
	 * @param cycle set if the animation is to cycle indefinitely between the initial and specified angle
	 * @param allowInterrupt set if the animation can be stopped when a manual rotation is requested
	 * **/
	public void rotateAnimation (double deg, double timeInMs, boolean cycle, boolean allowInterrupt) {
		interruptRotate = allowInterrupt;
		rotate.getKeyFrames().clear();
		rotate = new Timeline ();
		rotate.getKeyFrames().add(new KeyFrame(Duration.millis(timeInMs),new KeyValue(this.rotateProperty(),deg + this.getRotate())));
		if (cycle) {
			rotate.setAutoReverse(cycle);
			rotate.setCycleCount(Animation.INDEFINITE);
		}
		rotate.play();
	}
	
/****************************************************************************************************/
	
	/**Immediately rotates the sprite clockwise to the specified angle from the positive y-axis. 
	 * Interrupts any current rotation animation if allowed to.
	 * <p>
	 * It is recommended to use this function ONLY to initialize a sprite's rotation.
	 * @param deg the degree mark, on the unit circle, to which the sprite will be rotated
	 * **/
	public void rotateAbsolute (double deg) {
		if (interruptRotate && rotate.getCurrentRate() != 0) rotate.stop();
		this.setRotate(deg);
	}
	
	/**Smoothly rotates the sprite clockwise to an angle from the positive y-axis over a specified period of time.
	 * @param deg the degree mark to which the sprite is to be rotated to from its current angle
	 * @param timeInMs set the length of one cycle (initial -> specified angle)
	 * @param cycle set if the animation is to cycle indefinitely between the initial and specified angle
	 * @param allowInterrupt set if the animation can be stopped when a manual rotation is requested
	 * **/
	public void rotateAbsoluteAnimation (double deg, double timeInMs, boolean cycle, boolean allowInterrupt) {
		interruptRotate = allowInterrupt;
		rotate.getKeyFrames().clear();
		rotate = new Timeline ();
		rotate.getKeyFrames().add(new KeyFrame(Duration.millis(timeInMs),new KeyValue(this.rotateProperty(),deg)));
		if (cycle) {
			rotate.setAutoReverse(cycle);
			rotate.setCycleCount(Animation.INDEFINITE);
		}
		rotate.play();
	}
	
/****************************************************************************************************/
	
	/**Immediately rotates the sprite to the nearest 90 degree angle, squaring it up with the scene.
	 * Interrupts any current rotation animation if allowed to.
	 * **/
	public void rotateToNearest90 () {
		if (interruptRotate && rotate.getCurrentRate() != 0) rotate.stop();
		double degLessThan90 = this.getRotate()%90;
		double quadrant = (int)this.getRotate()/90;
		if (Math.abs(degLessThan90) < 45) this.setRotate(90*quadrant);
		else this.setRotate(90*(quadrant+1));
	}
	
	/**Smoothly rotates the sprite to the nearest 90 degree angle over a specified period of time
	 * @param timeInMs set the length of one cycle (initial -> final angle)
	 * @param cycle set if the animation is to cycle indefinitely between the initial and closest 90 degree angle
	 * @param allowInterrupt set if the animation can be stopped when a manual rotation is requested
	 * **/
	public void rotateToNearest90Animation (double timeInMs, boolean cycle, boolean allowInterrupt) {
		double degLessThan90 = this.getRotate()%90;
		double quadrant = (int)this.getRotate()/90;
		double finalDeg;
		if (Math.abs(degLessThan90) <= 0) return;
		else if (Math.abs(degLessThan90) < 45) finalDeg = 90*quadrant;
		else finalDeg = 90*(quadrant+1);
		
		interruptRotate = allowInterrupt;
		rotate.getKeyFrames().clear();
		rotate = new Timeline ();
		rotate.getKeyFrames().add(new KeyFrame(Duration.millis(timeInMs),new KeyValue(this.rotateProperty(),finalDeg)));
		if (cycle) {
			rotate.setAutoReverse(cycle);
			rotate.setCycleCount(Animation.INDEFINITE);
		}
		rotate.play();
	}
	
/****************************************************************************************************/
	
	/**Master pause function. Pauses all currently running animations on the sprite**/
	public void pause () {
		if (scale.getStatus() == Status.RUNNING) scale.pause();
		if (translate.getStatus() == Status.RUNNING) translate.pause();
		if (rotate.getStatus() == Status.RUNNING) rotate.pause();
	}
	
	/**Master resume function. Resumes all currently paused animations on the sprite**/
	public void resume () {
		if (scale.getStatus() == Status.PAUSED) scale.play();
		if (translate.getStatus() == Status.PAUSED) translate.play();
		if (rotate.getStatus() == Status.PAUSED) rotate.play();
	}	
	
}
