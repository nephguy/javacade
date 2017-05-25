package framework.menu;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import framework.Util;
import javafx.geometry.Insets;

/**
 * @author Nick Hansen
 */

public class CreditsMenu extends Menu{

	String font = "press-start.ttf";
	
	public CreditsMenu () {
		Label logic = Util.styleLabel(font, 15, false, "Logic by:");
		Label music = Util.styleLabel(font, 15, false, "Music by:");
		Label networking = Util.styleLabel(font, 15, false, "Networking by:");
		
		Label spacer = Util.styleLabel(font, 15, false, "");
		Label spacer2 = Util.styleLabel(font, 15, false, "");
		
		Label curveball = Util.styleLabel(font, 15, false, "Curveball by:");
		Label quadtetris = Util.styleLabel(font, 15, false, "Quad Tetrix by:");
		Label spaceInvaders = Util.styleLabel(font, 15, false, "Space Invaders by:");
		Label tronSnake = Util.styleLabel(font, 15, false, "Tron Snake by:");
		Label baseDefense = Util.styleLabel(font, 15, false, "Base Defense by:");
		Label avalanche = Util.styleLabel(font, 15, false, "Avalanche by:");
		
		Label nick = Util.styleLabel(font, 15, false, "Nick Hansen");
		Label nick2 = Util.styleLabel(font, 15, false, "Nick Hansen");
		Label nick3 = Util.styleLabel(font, 15, false, "Nick Hansen");
		Label nick4 = Util.styleLabel(font, 15, false, "Nick Hansen");
		Label alvin = Util.styleLabel(font, 15, false, "Alvin Chu");
		Label alvin2 = Util.styleLabel(font, 15, false, "Alvin Chu");
		Label jackson = Util.styleLabel(font, 15, false, "Jackson Chui");
		Label ben = Util.styleLabel(font, 15, false, "Benjamin Zhang");
		Label lovejit = Util.styleLabel(font, 15, false, "Lovejit Kharod");
		
		VBox titles = new VBox (logic,music,networking,spacer,curveball,quadtetris,spaceInvaders,tronSnake,baseDefense,avalanche);
		titles.setAlignment(Pos.TOP_LEFT);
		titles.setSpacing(10);
		VBox authors = new VBox (nick,alvin,ben,spacer2,nick2,alvin2,lovejit,jackson,nick3,nick4);
		authors.setAlignment(Pos.TOP_RIGHT);
		authors.setSpacing(10);
		
		this.setLeft(titles);
		this.setRight(authors);
		this.setPadding(new Insets(20));
		this.setMinHeight(600);
		this.setMinWidth(600);
	}
}
