package framework;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * @author Nick Hansen
 */

public abstract class Sprite extends StackPane{

	boolean constrainToScene;
	
	private Timeline scale = new Timeline();
	private Timeline translate = new Timeline();
	private Timeline rotate = new Timeline();
	private boolean interruptTranslate = false;
	private boolean interruptScale = false;
	private boolean interruptRotate = false;
	
	// used for moveTo();
	//private double height;
	//private double width;
	
	public Sprite (double height, double width) {
		setMaxSize(width, height);
		setMinSize(width, height);
		//this.height = height;
		//this.width = width;
		constrainToScene = true;
	}
	
	public static int[][] spriteFromFile (GameRootPane calledFrom, String spriteFileName, int heightInPixels, int widthInPixels) {
		int[][] vals = new int[heightInPixels][widthInPixels];
		String line;
		int row = 0;
		
		// extracts integers from the file.
		// gets the line, then iterates along it, adding the parsed ints to the 2d int array;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					"src/" + calledFrom.getClass().getPackage().getName() + "/sprite/" + spriteFileName));
			while ((line = reader.readLine()) != null) {
				final String[] lineArray = line.trim().split(",");
				for (int col = 0; col < widthInPixels; col++) {
					vals[row][col] = Integer.parseInt(lineArray[col]);
				}
				row++;
			}
			reader.close();
			return vals;
		}
		catch (FileNotFoundException e) {e.printStackTrace();}
		catch (IOException e) {e.printStackTrace();}
		return null;
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
	
	/**Immediately moves the sprite's center to the specified coordinates.
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
		this.setTranslateX(Util.clamp(x/maxX)*maxX - maxX/2);
		this.setTranslateY(Util.clamp(y/maxY)*maxY - maxY/2);
	}
	
	/**Smoothly moves the sprite's center from its current coordinates to the specified coordinates over a period of time. 
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

	
/****************************************************************************************************/
	
	/**Returns a boolean representing if this sprite has collided with the ONE other sprite specified**/
	public boolean collided (Node other) {
		return this.getBoundsInParent().intersects(other.getBoundsInParent());
	}
	
	/**Returns a boolean representing if this sprite has collided with ANY other sprite with the specified id.
	 * Used to check for collisions with a family of nodes
	 * @param id the id of the group of nodes for which to check collision
	 * **/
	public boolean collided (String id) {
		ObservableList<Node> children = this.getParent().getChildrenUnmodifiable();
		for (int i = 0; i < children.size(); i++) {
			try {
				if (this.collided(children.get(i)) && children.get(i).getId().equals(id)) return true;
			} catch (NullPointerException e) {} // is thrown if the node has no id. this then skips that node.
		}
		return false;
	}
	
	/**Returns the node with the given id that this sprite has collided with.
	 * If it has not collided with any node with the specified id, this will return null.
	 * <p>
	 * If you want to delete nodes on collision, use this function to get the node, then run 
	 * <pre><code>
	 * removePane(Node other);
	 * </pre></code>
	 * in your game's update() function.
	 * @param id the id for the group of nodes for which to check collision
	 * **/
	public Sprite getCollided (String id) {
		ObservableList<Node> children = this.getParent().getChildrenUnmodifiable();
		for (int i = 0; i < children.size(); i++) {
			try {
				if (this.collided(children.get(i)) && children.get(i).getId().equals(id)) return (Sprite)children.get(i);
			} catch (NullPointerException e) {} // is thrown if the node has no id. this then skips that node.
		}
		return null;
	}
	
/****************************************************************************************************/
	
	/**Set if the sprite is to be prevented from moving outside of the scene or not.**/
	public void setConstrainToScene (boolean bool) {
		constrainToScene = bool;
	}
	
	/**Returns a boolean representing if the sprite is to be prevented from moving outside of the scene or not.**/
	public boolean getConstrainToScene () {
		return constrainToScene;
	}
	
}
