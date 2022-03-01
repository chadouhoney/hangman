package logic;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ui.MainMenu;

public class Main extends Application {

	private final int height = 600, width = 1200;

	@Override
	public void start(Stage primaryStage) throws Exception {
		StackPane root = new StackPane();
		root.setPrefSize(width, height);
		root.setAlignment(Pos.CENTER_LEFT);
		primaryStage.setTitle("Hangman");
		primaryStage.setResizable(false);

		InputStream is = Files.newInputStream(Paths.get("src/resources/images/chalkboard.jpg"));
		Image img = new Image(is, width, height, false, false);
		is.close();
		ImageView imgView = new ImageView(img);
		imgView.fitHeightProperty().bind(primaryStage.heightProperty());
		imgView.fitWidthProperty().bind(primaryStage.widthProperty());

		System.out.println(root.getAlignment());

		MainMenu mainMenu = new MainMenu(root);
		root.getChildren().addAll(imgView, mainMenu);

		Scene s = new Scene(root, width, height);
		primaryStage.setScene(s);
		primaryStage.setMinWidth(width);
		primaryStage.setMinHeight(height);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
