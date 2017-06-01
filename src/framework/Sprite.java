package framework;

import javafx.collections.ObservableList;
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
	public Node getCollided (String id) {
		ObservableList<Node> children = this.getParent().getChildrenUnmodifiable();
		for (int i = 0; i < children.size(); i++) {
			try {
				if (this.collided(children.get(i)) && children.get(i).getId().equals(id)) return children.get(i);
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
