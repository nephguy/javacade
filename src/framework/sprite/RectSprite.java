package framework.sprite;

import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;

/**
 * @author Nick Hansen
 */

public class RectSprite extends Rectangle {
	
	boolean constrainToScene;
	
	private Timeline scale = new Timeline();
	private Timeline translate = new Timeline();
	private Timeline rotate = new Timeline();
	private boolean interruptTranslate = false;
	private boolean interruptScale = false;
	private boolean interruptRotate = false;
	
	
	
	
	public RectSprite (double height, double width) {
		super(height,width);
	}
	public RectSprite (double height, double width, Paint fill) {
		super (height,width,fill);
	}
	
	/**Create a rectangular sprite. A corner radius can be defined to round the corners.
	 * It may also be defined as proportional to the height, width, or neither.
	 * If it is defined as proportional to both, nothing happens.
	 * @param height the height of the sprite
	 * @param width the width of the sprite
	 * @param fill the color of the sprite
	 * @param cornerRadii the radius of the circle used to round the sprite's corners
	 * @param proportionalHeight defines if the corner radii is proportional to the sprite's height
	 * @param proportionalWidth defines if the corner radii is proportional to the sprite's width
	 * **/
	public RectSprite (double height, double width, Paint fill, double cornerRadii, boolean proportionalHeight, boolean proportionalWidth) {
		this.setHeight(height);
		this.setWidth(width);
		this.setFill(fill);
		
		
		if (proportionalHeight && proportionalWidth) {
			return;
		}
		if (proportionalHeight) {
			//provides protection for if the proportional corner radii is improperly defined
			if (cornerRadii <= 0.5) {
				this.arcHeightProperty().bind(this.heightProperty().multiply(cornerRadii));
				this.arcWidthProperty().bind(this.heightProperty().multiply(cornerRadii));
			}
			else {
				this.arcHeightProperty().bind(this.heightProperty().multiply(0.5));
				this.arcWidthProperty().bind(this.heightProperty().multiply(0.5));
			}
		}
		else if (proportionalWidth) {
			//provides protection for if the proportional corner radii is improperly defined
			if (cornerRadii <= 0.5) {
				this.arcHeightProperty().bind(this.widthProperty().multiply(cornerRadii));
				this.arcWidthProperty().bind(this.widthProperty().multiply(cornerRadii));
			}
			else {
				this.arcHeightProperty().bind(this.widthProperty().multiply(0.5));
				this.arcWidthProperty().bind(this.widthProperty().multiply(0.5));
			}
		}
		else {
			// provides protection for if the absolute corner radii is improperly defined
			if (cornerRadii*2 > height) {
				this.setArcHeight(height/2);
				this.setArcWidth(height/2);
			}
			else if (cornerRadii*2 > width) {
				this.setArcHeight(width/2);
				this.setArcWidth(width/2);
			}
			else {
				this.setArcHeight(cornerRadii);
				this.setArcWidth(cornerRadii);
			}
		}
	
	}
	
/****************************************************************************************************/
	
	/**Immediately scales the sprite by a specified factor.
	 * @param scaleBy the factor by which to scale the sprite to
	 * **/
	public void scale (double scaleBy) {
		if (interruptScale && scale.getCurrentRate() != 0) scale.pause();
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
	
	/**Pauses any currently running scale animation.<p>HIGHLY SUGGESTED to be run in onPause().**/
	public void pauseScale () {
		scale.pause();
	}
	/**Resumes any currently running scale animation.<p>HIGHLY SUGGESTED to be run in onResume().**/
	public void resumeScale () {
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
		if (interruptTranslate && translate.getCurrentRate() != 0) translate.pause();
		if (constrainToScene) {
			Bounds bounds = this.getBoundsInParent();
			Scene pScene = this.getScene();
			if (bounds.getMaxX() + x > pScene.getWidth() ||
				bounds.getMinX() + x < 0 ||
				bounds.getMaxY() + y > pScene.getHeight() ||
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
	
	/**Pauses any currently running translate animation.<p>HIGHLY SUGGESTED to be run in onPause().**/
	public void stopTranslate () {
		translate.pause();
	}
	/**Resumes any currently running scale animation.<p>HIGHLY SUGGESTED to be run in onResume().**/
	public void resumeTranslate () {
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
		if (interruptTranslate && translate.getCurrentRate() != 0) translate.pause();
		this.setTranslateX(x);
		this.setTranslateY(y);
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
		translate.getKeyFrames().add(new KeyFrame(Duration.millis(timeInMs),new KeyValue(this.translateXProperty(),x)));
		translate.getKeyFrames().add(new KeyFrame(Duration.millis(timeInMs),new KeyValue(this.translateYProperty(),y)));
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
	
	/**Pauses any currently running rotate animation.<p>HIGHLY SUGGESTED to be run in onPause().**/
	public void stopRotate () {
		rotate.pause();
	}
	/**Pauses any currently running rotate animation.<p>HIGHLY SUGGESTED to be run in onResume().**/
	public void resumeRotate () {
		rotate.play();
	}
	
/****************************************************************************************************/
	
	/**Immediately rotates the sprite to the specified angle on the unit circle.
	 * Interrupts any current rotation animation if allowed to.
	 * <p>
	 * It is recommended to use this function ONLY to initialize a sprite's rotation.
	 * @param deg the degree mark, on the unit circle, to which the sprite will be rotated
	 * **/
	public void rotateAbsolute (double deg) {
		if (interruptRotate && rotate.getCurrentRate() != 0) rotate.pause();
		this.setRotate(deg);
	}
	
	/**Smoothly rotates the sprite to an angle on the unit circle over a specified period of time.
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
		if (interruptRotate && rotate.getCurrentRate() != 0) rotate.pause();
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
	
	/**Checks if this sprite collides with ANY other sprite with the specified id.
	 * Used to check for collisions with a family of nodes
	 * **/
	public boolean collides (String id) {
		ObservableList<Node> children = this.getParent().getChildrenUnmodifiable();
		for (int i = 0; i < children.size(); i++)
			if (this.collides(children.get(i)) && children.get(i).getId().equals(id)) return true;
		return false;
	}
	
	/**Checks if this sprite collides with the ONE other sprite specified**/
	public boolean collides (Node other) {
		return this.getBoundsInParent().intersects(other.getBoundsInParent());
	}
	
/****************************************************************************************************/
	
	/**Set if the sprite is to be prevented from moving outside of the scene or not.**/
	public void setConstrainToScene (boolean bool) {
		constrainToScene = bool;
	}
	
	/**Get if the sprite is to be prevented from moving outside of the scene or not.**/
	public boolean getConstrainToScene () {
		return constrainToScene;
	}
	
}
