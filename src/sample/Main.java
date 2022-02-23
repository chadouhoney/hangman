package sample;

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

    
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        Pane root = new Pane();
        root.setPrefSize(900,600);
        primaryStage.setTitle("Hangman");
        
        InputStream is= Files.newInputStream(Paths.get("src/resources/background.jpeg"));
        Image img = new Image(is, 900,600,false,false);
        is.close();
        ImageView imgView = new ImageView(img);
        imgView.fitHeightProperty();
        imgView.fitWidthProperty();
    
        MainMenu mainMenu = new MainMenu();
        root.getChildren().addAll(imgView, mainMenu);
        
        Scene s = new  Scene(root, 900, 600);
        
        
        
        primaryStage.setScene(s);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
