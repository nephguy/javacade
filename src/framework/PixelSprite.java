package framework;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.shape.Path;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.VLineTo;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint;

/**
 * @author Nick Hansen
 */

public class PixelSprite extends Sprite {
	
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
	 */
	public PixelSprite (int[][] pixels, double realHeight, double realWidth, String id , Paint... fills) {
		super(realHeight,realWidth);
		double pHeight = realHeight / pixels.length;
		double pWidth = realWidth / pixels[0].length;
		
		Path[] paths = new Path[fills.length]; 
		for (int i = 0; i < paths.length; i++) {
			paths[i] = new Path();
			paths[i].setFill(fills[i]);
			paths[i].setId(id);
			paths[i].setStrokeWidth(0);
			// aligns all the paths so when adding them to the stack pane they are where they should be, not in the center.
			paths[i].getElements().addAll(new MoveTo(0,0),
					new HLineTo(realWidth),new HLineTo(0),
					new VLineTo(realHeight), new VLineTo(0));
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
		
		setId(id);
		for (Path p : paths) {this.getChildren().add(p);}
	}
	
	/**SEE OTHER CONSTRUCTOR FOR DETAILED EXPLANATION OF THIS CLASS
	 * <p>
	 * Constructs a pixel sprite based on the individual pixels' dimentions, instead of the entire sprite's dimentions.
	 */
	public PixelSprite (int[][] pixels, String id, double pixelHeight, double pixelWidth, Paint... fills) {
		this(pixels,pixelHeight*pixels.length,pixelWidth*pixels[0].length,id,fills);
	}
	
	/**SEE OTHER CONSTRUCTOR FOR DETAILED EXPLANATION OF THIS CLASS
	 * <p>
	 * Creates a pixel sprite without an id.
	 */
	public PixelSprite (int[][] pixels, double realHeight, double realWidth, Paint... fills) {
		this(pixels,realHeight,realWidth,"",fills);
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
}
