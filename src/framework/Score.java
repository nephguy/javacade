package framework;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;

/**
 * @author Nick Hansen
 */

public class Score extends Label{

	private double score;
	private double scoreBeforeDisabled;
	private boolean disabled;
	
	/** A simpler constructor. Same as the other, but with the colors set to default values
	 *  <pre><code>
	 *  fontColor = Color.BLACK;
	 *  dropShadowColor = Color.TRANSPARENT;
	 *  </pre></code>
	 *  
	 * @param parent the game containing this score counter. Since you should only declare a score counter in your primary file, just pass in <code>this</code>.
	 * @param fontSize the point size of the score counter’s font (e.x. 12 point font)
	 * @param position the position of the timer in the game window, as defined by the enumerator <code>javafx.geometry.Pos</code>.
	 */
	public Score (GameRootPane parent, double fontSize, Pos position) {
		this(parent,fontSize,Color.BLACK,Color.TRANSPARENT,position);
	}
	
	/** The score object is a label containing and displaying score within your game.
	 *  Since it is an object and not a variable, multiple score counters may be instantiated.
	 * 
	 * @param parent the game containing this score counter. Since you should only declare a score counter in your primary file, just pass in <code>this</code>.
	 * @param fontSize the point size of the score counter’s font (e.x. 12 point font)
	 * @param fontColor the color of the score counter’s text, defined either by the enumerator <code>javafx.scene.paint.Color</code>, or the <code>radialGradient()</code> and <code>linearGradient()</code> functions in <code>framework.Util</code>.
	 * @param dropShadowColor the color of the score counter’s drop shadow, as defined only by the enumerator <code>javafx.scene.paint.Color</code>
	 * @param position the position of the timer in the game window, as defined by the enumerator <code>javafx.geometry.Pos</code>.
	 */
	public Score (GameRootPane parent, double fontSize, Paint fontColor, Color dropShadowColor, Pos position) {
		setFont(Util.getFont(parent.getFont(), fontSize));
		setEffect(new DropShadow(BlurType.ONE_PASS_BOX,dropShadowColor,1,1,5,5));
		setTextFill(fontColor);
		this.setPadding(new Insets(20));
		StackPane.setAlignment(this, position);
		updateScore();
		disabled = false;
		parent.addPaneAbove(this);
	}
	
	/**Temporarily disables the score counter after it has been enabled*/
	public void disableScore () {
		if (!disabled) {
			scoreBeforeDisabled = score;
			this.setVisible(false);
		}
	}
	
	/**Re-enables the score counter after it has been disabled*/
	public void enableScore () {
		if (disabled) {
			score = scoreBeforeDisabled;
			updateScore();
			this.setVisible(true);
		}
	}
	
	
	private void updateScore () {
		this.setText("Score: " + (int)score);
	}
	
	/**Adds the double passed to the score and updates the score counter.**/
	public void addToScore (double score) {
		this.score += score;
		updateScore();
	}
	/**Removes the double passed in from the score and updates the score counter.**/
	public void removeFromScore (double score) {
		this.score -= score;
		updateScore();
	}
	/**Returns a double equal to the score.**/
	public double getScore () {
		return score;
	}
}
