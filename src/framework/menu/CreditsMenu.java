package framework.menu;

import framework.Util;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * @author Nick Hansen
 */

public class CreditsMenu extends Menu{

	String font = "press-start.ttf";
	
	VBox titles;
	VBox authors;
	
	public CreditsMenu (StackPane parent) {
		super(parent);
		
		titles = new VBox();
		authors = new VBox();
		titles.setAlignment(Pos.TOP_LEFT);
		authors.setAlignment(Pos.TOP_RIGHT);
		titles.setSpacing(10);
		authors.setSpacing(10);
		setLeft(titles);
		setRight(authors);
		
		addCredit("Game Engine","Nick Hansen");
		addCredit("Music","Alvin Chu");
		addCredit("Documentation","Jackson Chui","Nick Hansen");
		titles.getChildren().add(Util.styleLabel(font, 20, false, ""));
		authors.getChildren().add(Util.styleLabel(font, 20, false, ""));
		
		/**ADD YOUR CREDIT HERE**/
		addCredit("Curveball","Nick Hansen");
		
		
		/**ADD YOUR CREDIT HERE**/
	}
	
	public void addCredit (String gameTitle, String... gameAuthors) {
		String title = gameTitle + " by:";
		String author = "";
		for (int i = 0; i < gameAuthors.length; i++) {
			author += gameAuthors[i];
			if (i+1 != gameAuthors.length) {
				author += " &\n";
				title += "\n ";
			}
		}
		authors.getChildren().add(Util.styleLabel(font, 15, false, author));
		titles.getChildren().add(Util.styleLabel(font, 15, false, title));
	}
}
