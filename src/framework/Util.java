package framework;

import java.util.ArrayList;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.Font;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.image.ImageView;

/**
 * @author Nick Hansen
 */

/**This class contains entirely static functions and cannot be instantiated.
 * It contains miscellaneous utility functions used throughout the program.**/
public class Util {
	
	public static Label styleLabel (String fontFileName, double fontSize, boolean clickable, String text) {
		return styleLabel(fontFileName,fontSize,Color.BLACK,null,clickable,false,text);
	}
	
	public static Label styleLabel (String fontFileName, double fontSize, Paint fontColor, Color dropShadowColor, boolean clickable, boolean wrap, String text) {
		Label newLabel = new Label (text);
		newLabel.setFont(Util.getFont(fontFileName, fontSize));
		newLabel.setWrapText(wrap);
		if (clickable) newLabel.setCursor(Cursor.HAND);
		if (dropShadowColor != null) newLabel.setEffect(new DropShadow(BlurType.ONE_PASS_BOX,dropShadowColor,1,1,fontSize/8,fontSize/8));
		newLabel.setTextFill(fontColor);
		return newLabel;
	}
	
	/**Returns a linear gradient, i.e. a gradient moving in a line from left to right or top to bottom.
	 * All colors are given equal weight.
	 * @param horizontal true of the gradient is horizontal, false if the gradient is vertical
	 * @param colors the array of colors in the gradient, in order
	 * **/
	public static LinearGradient linearGradient (boolean horizontal, Color... colors) {
		ArrayList<Stop> stops = new ArrayList<Stop> ();
		double delta = 1 / colors.length;
		for (int i = 0; i < colors.length; i++)
			stops.add(new Stop(i*delta,colors[i]));
		if (horizontal)
			return new LinearGradient (0,0.5,1,0.5,true,CycleMethod.NO_CYCLE,stops);
		else
			return new LinearGradient (0.5,0,0.5,1,true,CycleMethod.NO_CYCLE,stops);
	}
	
	/**Returns a radial gradient, i.e. a gradient moving out in a circle from a center point.
	 * All colors are given equal weight
	 * @param centerX the x position of the center of the gradient, 0.0 to 1.0
	 * @param centerY the y position of the center of the gradient, 0.0 to 1.0
	 * @param colors the array of colors in the gradient, in order
	 * **/
	public static RadialGradient radialGradient (double centerX, double centerY, Color... colors) {
		ArrayList<Stop> stops = new ArrayList<Stop> ();
		double delta = 1 / colors.length;
		for (int i = 0; i < colors.length; i++)
			stops.add(new Stop(i*delta,colors[i]));
		return new RadialGradient (0,0,Util.clamp(centerX),Util.clamp(centerY),1,true,CycleMethod.NO_CYCLE,stops);
	}
	
	public static void playSfx (String gamePackageName, String sfxFileName, double volume) {
		MediaPlayer sfx = new MediaPlayer(getSfx(gamePackageName, sfxFileName));
		sfx.setVolume(volume/100);
		sfx.play();
	}
	
	
	/****************************************ADVANCED FUNCTIONS****************************************/

	public static Font getFont (String fontFileName, double size) {
		return Font.loadFont(Main.class.getResourceAsStream("/fonts/" + fontFileName),size);
	}
	
	public static Media getMusic (String gamePackageName, String musicFileName) {
		return new Media(Main.class.getResource("/" + gamePackageName + "/music/" + musicFileName).toString());
	}
	
	public static Media getSfx (String gamePackageName, String sfxFileName) {
		return new Media(Main.class.getResource("/" + gamePackageName + "/sfx/" + sfxFileName).toString());
	}
	
	public static ImageView getImage (String gamePackageName, String imageFileName) {
		try {
			return new ImageView (Main.class.getResource("/" + gamePackageName + "/sfx/" + imageFileName).toString());
		} catch (NullPointerException e) {
			return new ImageView (Main.class.getResource("/framework/defaultImage.png").toString());
		}
	}
	
	/** Format.clamp clamps a double between 0.0 and 1.0 <p>
	 * if value < 0, returns 0 <p>
	 * if value > 1, returns 1 <p>
	 * else, returns original value
	 * @param value double to clamp
	 * @return a double between 0.0 and 1.0
	 */
	public static double clamp(double value) { 
	    return value < 0 ? 0 : value > 1 ? 1 : value;
	}
	
	/**Returns the maximum value of a 2d array of ints**/
	public static int maxVal (int[][] a) {
		int max = 0;
		for (int[] row : a) {
			for (int col : row) {
				max = (col > max) ? col : max;
			}
		}
		return max;
	}
	
	/**Returns the maximum value of an array of ints**/
	public static int maxVal (int[] a) {
		int max = 0;
		for (int val : a) {
			max = (val > max) ? val : max;
		}
		return max;
	}
	
}
