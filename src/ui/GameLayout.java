package ui;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
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
			initializeGuess();

			hangLayout = new HangLayout();

			createLettersLayouts(this.game, this.guess);
			// Wanted Letters
			fillWantedLettersArray(this.game);
			wantedLetterHBox = new HBox(40);
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

	private void putLetterOnHang() {
		wantedLetterLayout[guess.getForPosition()].setLetterString(guess.getStringProperty());
		wantedLetterLayout[guess.getForPosition()].setLetterColor("#e0dbd1");
	}

	private void initializeGuess() {
		this.guess = new Guess();
		this.guess.setForPosition(0);
		this.guess.stringProperty.addListener(t -> {
			boolean res = this.game.guess(this.guess);
			if (res) {
				greenTickOnLetter();
				putLetterOnHang();

			} else {
				try {
					redCrossOnLetter();
					hangLayout.updateHang(phase++);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				checkEndgame();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		// this.guess.playerWon.addListener(t -> {
		// try {
		// showGameOver("GGWP");
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// });
		//
		// this.guess.playerLost.addListener(t -> {
		// try {
		// showGameOver("YOU LOSE, GIT GUD NOOB");
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// });
	}

	private void fillWantedLettersArray(Game game) {
		wantedLetterLayout = new WantedLetterLayout[game.getTargetWord().length()];
		for (int i = 0; i < game.getTargetWord().length(); i++) {
			wantedLetterLayout[i] = new WantedLetterLayout();
			int finalI = i;
			wantedLetterLayout[i].setOnMouseClicked(mouseEvent -> {
				if (wantedLetterLayout[finalI].getLetterString().equals("?")
						&& !wantedLetterLayout[finalI].isSelected()) {
					setActiveLetterLayout(finalI);
					wantedLetterLayout[finalI].setSelected(true);
				}
			});
		}
		wantedLetterLayout[0].setSelected(true);
		wantedLetterLayout[0].setLetterColor("#e0dbd1");
	}

	private void createLettersLayouts(Game game, Guess guess) {
		lettersLayout = new LettersLayout[game.getTargetWord().length()];
		for (int i = 0; i < game.getTargetWord().length(); i++) {
			lettersLayout[i] = new LettersLayout(game.getLettersProbabilities().get(i), guess);
		}
	}

	private void setActiveLetterLayout(int layout) {
		System.out.println("Changing from " + currentLetterLayout + " to " + layout);
		mainVBox.getChildren().removeAll(lettersLayout[currentLetterLayout]);
		mainVBox.getChildren().add(3, lettersLayout[layout]);
		guess.setForPosition(layout);

		if (((WantedLetterLayout) wantedLetterHBox.getChildren().get(currentLetterLayout)).getLetterString()
				.equals("?")) {
			((WantedLetterLayout) wantedLetterHBox.getChildren().get(currentLetterLayout)).setLetterColor("#000000");
		}
		((WantedLetterLayout) wantedLetterHBox.getChildren().get(currentLetterLayout)).setSelected(false);
		((WantedLetterLayout) wantedLetterHBox.getChildren().get(currentLetterLayout)).setOpacity(1.0);

		currentLetterLayout = layout;
		((WantedLetterLayout) wantedLetterHBox.getChildren().get(currentLetterLayout)).setLetterColor("#e0dbd1");
		((WantedLetterLayout) wantedLetterHBox.getChildren().get(currentLetterLayout)).setSelected(true);
		((WantedLetterLayout) wantedLetterHBox.getChildren().get(currentLetterLayout)).setOpacity(1.0);
	}

	private void redCrossOnLetter() {
		int layout = guess.getForPosition();
		lettersLayout[layout].addRedCross();
	}

	private void greenTickOnLetter() {
		int layout = guess.getForPosition();
		lettersLayout[layout].addGreenTick();
	}

	private void checkEndgame() throws IOException {
		if (guess.isPlayerWin()) {
			showGameOver("GGWP");
		} else if (guess.isPlayerLose()) {
			showGameOver("YOU LOSE, GIT GUD NOOB");
		}
	}

	private void showGameOver(String headerTxt) throws IOException {
		ButtonType mainMenuBut = new ButtonType("Back to Main Menu");
		ButtonType playAgainBut = new ButtonType("Play Again!");
		Alert gameOver = new Alert(Alert.AlertType.NONE, "Game Over!", mainMenuBut, playAgainBut);
		gameOver.setHeaderText(headerTxt);
		gameOver.initStyle(StageStyle.UNDECORATED);
		Optional<ButtonType> res = gameOver.showAndWait();
		ButtonType resChoice = res.orElse(null);
		if (resChoice == mainMenuBut) {
			goToMainMenu();
		} else if (resChoice == playAgainBut) {
			playAgain();
		}
	}

	private void goToMainMenu() throws IOException {
		Stage stg = (Stage) GameLayout.this.getScene().getWindow();
		StackPane root = new StackPane();
		root.setPrefSize(1000, 600);
		root.setAlignment(Pos.CENTER_LEFT);

		InputStream is = Files.newInputStream(Paths.get("src/resources/images/chalkboard.jpg"));
		Image img = new Image(is, 1000, 600, false, false);
		is.close();
		ImageView imgView = new ImageView(img);
		imgView.fitHeightProperty().bind(stg.heightProperty());
		imgView.fitWidthProperty().bind(stg.widthProperty());

		MainMenu mainMenu = new MainMenu(root);
		root.getChildren().addAll(imgView, mainMenu);

		Scene s = new Scene(root, 1000, 600);
		stg.setScene(s);
		stg.show();
	}

	private void playAgain() {
		this.game = this.game.newGame();
		phase = 0;
		mainVBox.getChildren().removeAll(lettersLayout[currentLetterLayout]);
		wantedLetterHBox.getChildren().removeAll(wantedLetterLayout);
		mainVBox.getChildren().removeAll(wantedLetterHBox);
		currentLetterLayout = 0;

		initializeGuess();
		fillWantedLettersArray(this.game);
		createLettersLayouts(this.game, this.guess);
		wantedLetterHBox.getChildren().addAll(wantedLetterLayout);
		mainVBox.getChildren().add(1, wantedLetterHBox);
		mainVBox.getChildren().add(3, lettersLayout[0]);
		try {
			hangLayout.updateHang(phase);
			phase = 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
