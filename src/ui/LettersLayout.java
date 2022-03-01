package ui;

import java.util.*;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Pair;

public class LettersLayout extends GridPane {

	private LetterButton[] letters;
	private Guess guess;
	private boolean layoutIsSolved;
	private HashMap<Character, Double> currentProbs;

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

	public void updateProbabilities(HashMap<Character, Double> probs) {
		System.out.println(probs);
		// (C, d) -> Letter C has probability d to be correct
		List<Map.Entry<Character, Double>> list = new ArrayList<>(probs.entrySet());
		// Sorting. Most likely letter at list[25]
		list.sort(Map.Entry.comparingByValue());

		List<Pair<Double, Pair<Integer, Integer>>> probIndex = new ArrayList<>();
		getChildren().removeAll(letters);
		// (x, (i,j))
		// x -> prob
		// i -> new index
		// j -> old index
		// Finding the position of letter C at letters
		for (int i = 0; i < 26; i++) {
			for (int j = 0; j < 26; j++) {
				if (list.get(i).getKey().toString().equals(letters[j].letter.getText())) {
					probIndex.add(new Pair<>(list.get(i).getValue(), new Pair<>(i, j)));
					letters[j].probability.setText(String.format("%1.2f", list.get(i).getValue()));
					break;
				}
			}
		}
		probIndex.sort(Comparator.comparingDouble(Pair::getKey));
		LetterButton[] newLetters = new LetterButton[26];
		for (int i = 0; i < 26; i++) {
			// Breaking down probIndex
			int oldIndex = probIndex.get(i).getValue().getValue();
			int newIndex = probIndex.get(i).getValue().getKey();
			newLetters[newIndex] = letters[oldIndex];
			newLetters[newIndex].arrayPos = newIndex;
			letters[oldIndex] = null;
		}

		for (int i = 25; i >= 0; i--) {
			this.add(newLetters[i], (25 - i) % 13, (25 - i) / 13);
		}

		letters = newLetters;
	}

	protected class LetterButton extends StackPane {

		private boolean isClicked;
		private int arrayPos;
		private VBox letterAndProbability;
		private Text overlay;
		private Text probability;
		private Text letter;

		public LetterButton(Character s, int pos, Double p) {

			this.arrayPos = pos;
			letterAndProbability = new VBox(1);
			letterAndProbability.setAlignment(Pos.CENTER);
			letter = new Text(s.toString());
			letter.setFont(Font.loadFont("file:src/resources/fonts/CrayonCrumble.ttf", 50));
			letter.setFill(Paint.valueOf("#e0dbd1"));

			probability = new Text(String.format("%1.2f", p));
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
					System.out.println("Pressed " + letter.getText() + " for Position " + guess.getForPosition());
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

		@Override
		public String toString() {
			return "LetterButton{" + "probability=" + probability.getText() + ", letter=" + letter.getText() + '}';
		}
	}
}
