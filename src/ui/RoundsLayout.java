package ui;

import java.io.*;
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

public class RoundsLayout extends StackPane {

	public RoundsLayout() throws IOException {
		InputStream is = Files.newInputStream(Paths.get("src/resources/images/chalkboard.jpg"));
		Image img = new Image(is, 600, 200, false, false);
		is.close();
		ImageView imgView = new ImageView(img);

		GridPane gp = new GridPane();
		gp.setAlignment(Pos.CENTER);

		Font font = Font.loadFont("file:src/resources/fonts/CrayonCrumble.ttf", 20);
		File file = new File("src/logging/history.txt");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line;
		int i = 0;
		while ((line = br.readLine()) != null) {
			Text txt = new Text(line);
			txt.setFont(font);
			txt.setFill(Paint.valueOf("#e0dbd1"));
			gp.addRow(i, txt);
			i++;
		}

		getChildren().addAll(imgView, gp);
	}
}
