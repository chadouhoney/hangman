package ui;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WantedLetterLayout extends StackPane {

	private Text underline;
	private Text letter;

	public WantedLetterLayout() {
		underline = new Text("_");
		letter = new Text("");

		Font font = Font.loadFont("file:src/resources/fonts/CrayonCrumble.ttf", 50);
		letter.setFont(font);

		underline.setFont(Font.loadFont("file:src/resources/fonts/CrayonCrumble.ttf", 80));

		getChildren().addAll(letter, underline);
	}

	public void setLetterString(String s) {
		letter.setText(s);
	}
}
