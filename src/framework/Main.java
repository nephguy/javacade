package framework;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * @author Nick Hansen
 */

public class Main extends Application {
	
	MainMenu mainMenu;
	public static String appDataPath;
	
	public void init () {
		mainMenu = new MainMenu();
		appDataPath = mainMenu.appDataPath;
	}
	
	public void start (Stage stage) {
		Scene scene = new Scene (mainMenu);
		
		stage.setResizable(false);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.setTitle("Javacade");
		stage.show();
		//TODO - SET ICON
		//stage.getIcons().add(image)
	}
	
	public static void main (String[] args) {
		launch(args);
	}
}
