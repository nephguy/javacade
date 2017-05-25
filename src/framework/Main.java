package framework;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * @author Nick Hansen
 */

public class Main extends Application {

	MainMenu mainMenu;
	
	public void init () {
		mainMenu = new MainMenu ();
	}
	
	public void start (Stage stage) {
		Scene scene = new Scene (mainMenu);
		
		stage.setResizable(false);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.setTitle("Javacade");
		stage.show();
	}
	
	public static void main (String[] args) {
		launch(args);
	}
}
