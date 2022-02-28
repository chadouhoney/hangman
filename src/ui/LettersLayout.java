package ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.Cursor;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LettersLayout extends GridPane {

	private LetterButton[] letters;
	private Guess guess;
	private boolean layoutIsSolved;

	public LettersLayout(HashMap<Character, Double> probs, Guess g) {
		super();
		this.guess = g;
		layoutIsSolved = false;
		// Letter-Button Creation
		letters = new LetterButton[26];
		List<Map.Entry<Character, Double>> list = new ArrayList<>(probs.entrySet());
		list.sort(Map.Entry.comparingByValue());
		for (int i = 25; i >= 0; i--) {
			letters[i] = new LetterButton(list.get(i).getKey(), i);
			this.add(letters[i], (25 - i) % 13, (25 - i) / 13);
		}

		// Pane Customization
		setHgap(10.0);
		setVgap(10.0);
	}

	public void addRedCross() {
		letters[guess.getLetterArrayPos()].redCross();
	}

	public void addGreenTick() {
		this.layoutIsSolved = true;
		letters[guess.getLetterArrayPos()].greenTick();
	}


	protected class LetterButton extends StackPane {

		private boolean isClicked;
		private int arrayPos;
		private Text text;
		private Text overlay;

		public LetterButton(Character s, int pos) {
			this.arrayPos = pos;
			text = new Text(s.toString());
			Font font = Font.loadFont("file:src/resources/fonts/EraserRegular.ttf", 40);
			text.setFont(font);
			text.setFill(Paint.valueOf("#e0dbd1"));
			overlay = new Text("");
			overlay.setFont(font);
			getChildren().addAll(text);

			this.setOnMouseEntered(e -> {
				if (!isClicked && !layoutIsSolved) {
					setCursor(Cursor.HAND);
					text.setOpacity(0.5);
				}

			});

			this.setOnMouseExited(e -> {
				if (!isClicked && !layoutIsSolved) {
					setCursor(Cursor.DEFAULT);
					text.setOpacity(1);
				}

			});

			this.setOnMouseClicked(mouseClick -> {
				if (!isClicked && !layoutIsSolved) {
					guess.setLetterArrayPos(arrayPos);
					guess.stringProperty.set(text.getText());
					isClicked = true;
				}
			});

			isClicked = false;
		}

		protected void redCross() {
			overlay.setText("X");
			overlay.setFill(Paint.valueOf("#ff0000"));
			getChildren().addAll(overlay);
		}

		protected void greenTick() {
			overlay.setText("v");
			overlay.setFill(Paint.valueOf("#00ff00"));
			getChildren().addAll(overlay);
		}

	}
}
