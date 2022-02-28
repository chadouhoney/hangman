package ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LettersLayout extends GridPane {

	private LetterButton[] letters;

	public LettersLayout(HashMap<Character, Double> probs) {
		super();
		// Letter-Button Creation
		letters = new LetterButton[26];
		List<Map.Entry<Character, Double>> list = new ArrayList<>(probs.entrySet());
		list.sort(Map.Entry.comparingByValue());
		for (int i = 25; i >= 0; i--) {
			letters[i] = new LetterButton(list.get(i).getKey());
			this.add(letters[i], (25 - i) % 13, (25 - i) / 13);
		}

		// Pane Customization
		setBorder(new Border(new BorderStroke(Color.RED, Color.RED, Color.RED, Color.RED, BorderStrokeStyle.SOLID,
				BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
				new BorderWidths(5), Insets.EMPTY)));
		setHgap(10.0);
		setVgap(10.0);
	}

	protected class LetterButton extends StackPane {

		private boolean isClicked;
		private Text text;
		private Text overlay;

		public LetterButton(Character s) {
			text = new Text(s.toString());
			Font font = Font.loadFont("file:src/resources/fonts/EraserRegular.ttf", 40);
			text.setFont(font);
			text.setFill(Paint.valueOf("#e0dbd1"));
			getChildren().addAll(text);

			this.setOnMouseClicked(mouseClick -> {
				overlay = new Text("X");
				overlay.setFont(font);
				overlay.setFill(Paint.valueOf("#ff0000"));
				getChildren().addAll(overlay);
			});

			isClicked = false;
		}

	}
}
