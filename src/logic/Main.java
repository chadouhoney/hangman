package logic;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ui.MainMenu;

public class Main extends Application {

	private final int height = 600, width = 900;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Pane root = new Pane();
		root.setPrefSize(width, height);
        primaryStage.setTitle("Hangman");
        
		InputStream is = Files.newInputStream(Paths.get("src/resources/images/chalkboard.jpg"));
		Image img = new Image(is, width, height, false, false);
        is.close();
        ImageView imgView = new ImageView(img);
        imgView.fitHeightProperty();
        imgView.fitWidthProperty();
    
		MainMenu mainMenu = new MainMenu(root);
        root.getChildren().addAll(imgView, mainMenu);
        
		Scene s = new Scene(root, width, height);
        primaryStage.setScene(s);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
