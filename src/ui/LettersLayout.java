package ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
			letters[i] = new LetterButton(list.get(i).getKey(), i, probs.get(list.get(i).getKey()));
			this.add(letters[i], (25 - i) % 13, (25 - i) / 13);
		}

		// Pane Customization
		setHgap(20.0);
		setVgap(20.0);
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
		private VBox letterAndProbability;
		private Text overlay;
		private Text probability;

		public LetterButton(Character s, int pos, Double p) {

			this.arrayPos = pos;
			letterAndProbability = new VBox(1);
			letterAndProbability.setAlignment(Pos.CENTER);
			Text letter = new Text(s.toString());
			letter.setFont(Font.loadFont("file:src/resources/fonts/CrayonCrumble.ttf", 50));
			letter.setFill(Paint.valueOf("#e0dbd1"));

			Text probability = new Text(String.format("%1.2f", p));
			probability.setFont(Font.loadFont("file:src/resources/fonts/CrayonCrumble.ttf", 15));
			probability.setFill(Paint.valueOf("#e0dbd1"));

			letterAndProbability.getChildren().addAll(letter, probability);

			overlay = new Text("");
			overlay.setFont(Font.loadFont("file:src/resources/fonts/CrayonCrumble.ttf", 50));
			getChildren().addAll(letterAndProbability);

			this.setOnMouseEntered(e -> {
				if (!isClicked && !layoutIsSolved) {
					setCursor(Cursor.HAND);
					letter.setOpacity(0.5);
				}

			});

			this.setOnMouseExited(e -> {
				if (!isClicked && !layoutIsSolved) {
					setCursor(Cursor.DEFAULT);
					letter.setOpacity(1);
				}

			});

			this.setOnMouseClicked(mouseClick -> {
				if (!isClicked && !layoutIsSolved) {
					letter.setOpacity(1);
					guess.setLetterArrayPos(arrayPos);
					guess.stringProperty.set(letter.getText());
					isClicked = true;
				}
			});

			isClicked = false;
		}

		protected void redCross() {
			overlay.setText("/");
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
