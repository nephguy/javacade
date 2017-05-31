package framework;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * @author Nick Hansen
 */

abstract class Sprite extends StackPane{

	boolean constrainToScene;
	
	public abstract void scale(double scaleBy);
	public abstract void scaleAnimation(double scaleBy, double timeInMs, boolean cycle, boolean allowInterrupt);
	public abstract void translate(double x, double y);
	public abstract void translateAnimation(double x, double y, double timeInMs, boolean cycle, boolean allowInterrupt);
	public abstract void moveTo(double x, double y);
	public abstract void moveToAnimation(double x, double y, double timeInMs, boolean cycle, boolean allowInterrupt);
	public abstract void rotate(double deg);
	public abstract void rotateAnimation(double deg, double timeInMs, boolean cycle, boolean allowInterrupt);
	public abstract void rotateAbsolute(double deg);
	public abstract void rotateAbsoluteAnimation(double deg, double timeInMs, boolean cycle, boolean allowInterrupt);
	public abstract void rotateToNearest90();
	public abstract void rotateToNearest90Animation(double timeInMs, boolean cycle, boolean allowInterrupt);
	public abstract void pause();
	public abstract void resume();
	
	public abstract boolean collided (Node other);
	public abstract boolean collided (String id);
	public abstract Node getCollided (String id);
	
	public abstract void setConstrainToScene(boolean bool);
	public abstract boolean getConstrainToScene();
	
}
