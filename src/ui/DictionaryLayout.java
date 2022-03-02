package ui;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import logic.Game;

public class DictionaryLayout extends StackPane {

	public DictionaryLayout(Game game) throws IOException {
		InputStream is = Files.newInputStream(Paths.get("src/resources/images/chalkboard.jpg"));
		Image img = new Image(is, 400, 300, false, false);
		is.close();
		ImageView imgView = new ImageView(img);

		GridPane gp = new GridPane();
		gp.setAlignment(Pos.CENTER);
		Font font = Font.loadFont("file:src/resources/fonts/CrayonCrumble.ttf", 20);
		Text six = new Text("Words with 6 Letters: ");
		six.setFont(font);
		six.setFill(Paint.valueOf("#e0dbd1"));
		Text sevenNine = new Text("Words with 7-9 Letters: ");
		sevenNine.setFont(font);
		sevenNine.setFill(Paint.valueOf("#e0dbd1"));
		Text tenMore = new Text("Words with 10 or more Letters: ");
		tenMore.setFont(font);
		tenMore.setFill(Paint.valueOf("#e0dbd1"));
		gp.addColumn(0, six, sevenNine, tenMore);

		Text sixNum = new Text(String.format("%1.2f", game.getWordsLength6()));
		sixNum.setFont(font);
		sixNum.setFill(Paint.valueOf("#e0dbd1"));
		Text sevenNineNum = new Text(String.format("%1.2f", game.getWordsLength7_9()));
		sevenNineNum.setFont(font);
		sevenNineNum.setFill(Paint.valueOf("#e0dbd1"));
		Text tenMoreNum = new Text(String.format("%1.2f", game.getWordsLength10plus()));
		tenMoreNum.setFont(font);
		tenMoreNum.setFill(Paint.valueOf("#e0dbd1"));
		gp.addColumn(1, sixNum, sevenNineNum, tenMoreNum);
		getChildren().addAll(imgView, gp);
	}

}
