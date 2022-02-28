package ui;

import java.io.IOException;
import java.util.HashMap;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import logic.Game;

public class GameLayout extends HBox {
	// Layout Variables
	private HangLayout hangLayout;
	private LettersLayout[] lettersLayout;
	private WantedLetterLayout[] wantedLetterLayout;
	private HBox wantedLetterHBox;
	private VBox mainVBox;
	// For Layout Communication
	private Guess guess;
	// Different variables
	private Game game;
	private int currentLetterLayout;
	private int phase = 1;

	// OL31390631M
	public GameLayout(Game g) {
		super(10.0);
		this.game = g;
		try {
			guess = new Guess();
			guess.setForPosition(currentLetterLayout);
			guess.stringProperty.addListener(t -> {
				boolean res = this.game.guess(this.guess);
				if (res) {
					greenTickOnLetter();
					wantedLetterLayout[guess.getForPosition()].setLetterString(guess.getStringProperty());
				} else {
					try {
						redCrossOnLetter();
						hangLayout.updateHang(phase++);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});

			guess.playerWon.addListener(t -> {
				ButtonType mainMenuBut = new ButtonType("Back to Main Menu");
				ButtonType playAgainBut = new ButtonType("Play Again!");
				Alert gameOver = new Alert(Alert.AlertType.NONE, "Game Over!", mainMenuBut, playAgainBut);
				gameOver.setHeaderText("GGWP");
				gameOver.initStyle(StageStyle.UNDECORATED);
				gameOver.showAndWait();
			});

			guess.playerLost.addListener(t -> {
				ButtonType mainMenuBut = new ButtonType("Back to Main Menu");
				ButtonType playAgainBut = new ButtonType("Play Again!");
				Alert gameOver = new Alert(Alert.AlertType.NONE, "Game Over!", mainMenuBut, playAgainBut);
				gameOver.setHeaderText("YOU LOSE, GIT GUD NOOB");
				gameOver.initStyle(StageStyle.UNDECORATED);
				gameOver.showAndWait();
			});

			hangLayout = new HangLayout();
			lettersLayout = new LettersLayout[g.getTargetWord().length()];
			createLettersLayouts(g.getLettersProbabilities(), guess);
			// Wanted Letters
			wantedLetterLayout = new WantedLetterLayout[g.getTargetWord().length()];
			fillWantedLettersArray(g.getTargetWord());
			wantedLetterHBox = new HBox(20);
			wantedLetterHBox.getChildren().addAll(wantedLetterLayout);

			Region r1 = new Region();
			Region r2 = new Region();
			Region r3 = new Region();
			mainVBox = new VBox();
			VBox.setVgrow(r1, Priority.ALWAYS);
			VBox.setVgrow(r2, Priority.ALWAYS);
			VBox.setVgrow(r3, Priority.ALWAYS);
			mainVBox.getChildren().addAll(r1, wantedLetterHBox, r2, lettersLayout[0], r3);
			currentLetterLayout = 0;
			//
			//
			//
			getChildren().addAll(hangLayout, mainVBox);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void fillWantedLettersArray(String word) {
		for (int i = 0; i < word.length(); i++) {
			wantedLetterLayout[i] = new WantedLetterLayout();
			int finalI = i;
			wantedLetterLayout[i].setOnMouseClicked(mouseEvent -> {
				setActiveLetterLayout(finalI);
			});
		}
	}

	private void createLettersLayouts(HashMap<Integer, HashMap<Character, Double>> hashMap, Guess guess) {
		for (int i = 0; i < game.getTargetWord().length(); i++) {
			lettersLayout[i] = new LettersLayout(hashMap.get(i), guess);
		}
	}

	private void setActiveLetterLayout(int layout) {
		mainVBox.getChildren().removeAll(lettersLayout[currentLetterLayout]);
		mainVBox.getChildren().add(3, lettersLayout[layout]);
		guess.setForPosition(layout);
		currentLetterLayout = layout;
	}

	private void redCrossOnLetter() {
		int layout = guess.getForPosition();
		lettersLayout[layout].addRedCross();
	}

	private void greenTickOnLetter() {
		int layout = guess.getForPosition();
		lettersLayout[layout].addGreenTick();
	}

}
