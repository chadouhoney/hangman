package ui;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Random;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Dictionary;
import logic.Game;

public class GameLayout extends VBox {
	// Layout Variables
	private HangLayout hangLayout;
	private LettersLayout[] lettersLayout;
	private WantedLetterLayout[] wantedLetterLayout;
	private HBox wantedLetterHBox;
	private HBox mainHBox;
	private VBox mainVBox;
	private MenuBar menuBar;
	private StatsLayout statsLayout;
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

			populateMenu();
			createLettersLayouts(this.game, this.guess);
			// Wanted Letters
			fillWantedLettersArray(this.game);
			wantedLetterHBox = new HBox(40);
			wantedLetterHBox.getChildren().addAll(wantedLetterLayout);

			// Stats
			statsLayout = new StatsLayout(0, game.getWordsLengths().get(game.getTargetWord().length()).size(), 0.0);

			Region r0 = new Region();
			Region r1 = new Region();
			Region r2 = new Region();
			Region r3 = new Region();
			mainVBox = new VBox();
			VBox.setVgrow(r0, Priority.ALWAYS);
			VBox.setVgrow(r1, Priority.ALWAYS);
			VBox.setVgrow(r2, Priority.ALWAYS);
			VBox.setVgrow(r3, Priority.ALWAYS);
			mainVBox.getChildren().addAll(r0, statsLayout, r1, wantedLetterHBox, r2, lettersLayout[0], r3);
			currentLetterLayout = 0;
			//
			//
			//
			System.out.println(GameLayout.this.getParent());
			System.out.println(GameLayout.this.getScene());
			mainHBox = new HBox();
			mainHBox.getChildren().addAll(hangLayout, mainVBox);
			getChildren().addAll(menuBar, mainHBox);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void putLetterOnHang() {
		wantedLetterLayout[guess.getForPosition()].setLetterString(guess.getStringProperty());
		wantedLetterLayout[guess.getForPosition()].setLetterColor("#e0dbd1");
	}

	private void putLetterOnHang(int i, char c) {
		wantedLetterLayout[i].setLetterString(String.valueOf(c));
		wantedLetterLayout[i].setLetterColor("#e0dbd1");
	}

	private void initializeGuess() {
		this.guess = new Guess();
		this.guess.setForPosition(0);
		this.guess.stringProperty.addListener(t -> {
			if (!this.guess.stringProperty.get().equals("")) {
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
				statsLayout.updateFields(game.getPlayerPoints(),
						game.getWordsLengths().get(game.getTargetWord().length()).size(),
						game.getSussessfulPercentage());
				try {
					checkEndgame();
				} catch (IOException e) {
					e.printStackTrace();
				}
				lettersLayout[currentLetterLayout]
						.updateProbabilities(this.game.getLettersProbabilities().get(currentLetterLayout));
			}
		});
	}

	private void populateMenu() {
		// Menus declarations
		menuBar = new MenuBar();
		Menu applicationMenu = new Menu("Application");
		Menu detailsMenu = new Menu("Details");
		MenuItem start = new MenuItem("Start");
		MenuItem load = new MenuItem("Load");
		MenuItem create = new MenuItem("Create");
		MenuItem exit = new MenuItem("Exit");
		MenuItem dict = new MenuItem("Dictionary");
		MenuItem rounds = new MenuItem("Rounds");
		MenuItem sol = new MenuItem("Solution");
		// Menus population
		applicationMenu.getItems().addAll(start, load, create, exit);
		detailsMenu.getItems().addAll(dict, rounds, sol);
		// Actions
		start.setOnAction(ae -> {
			playAgain();
		});
		load.setOnAction(ae -> {
			TextInputDialog inputDialog = new TextInputDialog("");
			inputDialog.setHeaderText("Enter DICTIONARY-ID");
			inputDialog.showAndWait();

			String dictionaryId = inputDialog.getResult();

			if (dictionaryId != null) {
				Dictionary d = null;
				boolean everythingok = true;
				try {
					d = new Dictionary(dictionaryId);
				} catch (Exception e) {
					Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
					alert.setHeight(300);
					alert.show();
					everythingok = false;
				}

				if (everythingok) {
					// start new game with random word from d
					Random rand = new Random();
					this.game = new Game(d, d.getWords().get(rand.nextInt(d.getWords().size())));
					playAgain();
				}
			}
		});
		create.setOnAction(ae -> {
			final Stage dialog = new Stage();
			dialog.initModality(Modality.APPLICATION_MODAL);
			// dialog.initOwner(this.root.sta);
			VBox dialogVbox = new VBox(10);
			Label openLibIdLabel = new Label("Open Library Id");
			Label textNameLabel = new Label("Desired file name");

			TextArea openLibIdTextArea = new TextArea("");
			openLibIdTextArea.setPrefWidth(100);
			openLibIdTextArea.setPrefHeight(30);

			TextArea fileIdTextArea = new TextArea("");
			fileIdTextArea.setPrefWidth(100);
			fileIdTextArea.setPrefHeight(30);

			Button b = new Button("OK!");
			b.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					try {
						// create new dict
						Dictionary.createDictionaryTextFile(fileIdTextArea.getText().strip(),
								openLibIdTextArea.getText().strip());
					} catch (Exception e) {
						Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
						alert.show();
					}
					dialog.close();
				}
			});
		});
		exit.setOnAction(ae -> {
			Platform.exit();
		});

		dict.setOnAction(ae -> {
			Stage popup = new Stage();
			popup.setAlwaysOnTop(true);
			popup.setResizable(false);
			popup.setTitle("Percentages of Words");
			popup.initModality(Modality.APPLICATION_MODAL);

			DictionaryLayout dictLayout = null;
			try {
				dictLayout = new DictionaryLayout(this.game);
			} catch (IOException e) {
				e.printStackTrace();
			}
			assert dictLayout != null;
			Scene scn = new Scene(dictLayout, 400, 300);
			popup.setScene(scn);
			popup.showAndWait();
		});
		rounds.setOnAction(ae -> {
			Stage popup = new Stage();
			popup.setAlwaysOnTop(true);
			popup.setResizable(false);
			popup.setTitle("Latest Results");
			popup.initModality(Modality.APPLICATION_MODAL);

			RoundsLayout rl = null;
			try {
				rl = new RoundsLayout();
			} catch (IOException e) {
				e.printStackTrace();
			}
			assert rl != null;
			Scene scn = new Scene(rl, 600, 200);
			popup.setScene(scn);
			popup.showAndWait();
		});
		sol.setOnAction(ae -> {
			game.logGame("DEFEAT");
			String target = this.game.getTargetWord();
			for (int i = 0; i < target.length(); i++) {
				putLetterOnHang(i, target.charAt(i));
			}
			try {
				showGameOver("YOU ARE A FCKING QUITTER!!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		menuBar.getMenus().addAll(applicationMenu, detailsMenu);

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
		lettersLayout[layout].updateProbabilities(this.game.getLettersProbabilities().get(layout));
		mainVBox.getChildren().add(5, lettersLayout[layout]);
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
		gameOver.initOwner(GameLayout.this.getScene().getWindow());
		gameOver.setY(400.0);
		gameOver.setHeaderText(headerTxt);
		gameOver.initStyle(StageStyle.DECORATED);
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
		root.setPrefSize(1200, 600);
		root.setAlignment(Pos.CENTER);

		InputStream is = Files.newInputStream(Paths.get("src/resources/images/chalkboard.jpg"));
		Image img = new Image(is, 1200, 600, false, false);
		is.close();
		ImageView imgView = new ImageView(img);
		imgView.fitHeightProperty().bind(stg.heightProperty());
		imgView.fitWidthProperty().bind(stg.widthProperty());

		MainMenu mainMenu = new MainMenu(root);
		root.getChildren().addAll(imgView, mainMenu);

		Scene s = new Scene(root, 1200, 600);
		stg.setScene(s);
		stg.show();
	}

	private void playAgain() {
		this.game = this.game.newGame();
		phase = 0;
		mainVBox.getChildren().removeAll(lettersLayout[currentLetterLayout]);
		wantedLetterHBox.getChildren().removeAll(wantedLetterLayout);
		mainVBox.getChildren().removeAll(wantedLetterHBox);
		mainVBox.getChildren().removeAll(statsLayout);
		currentLetterLayout = 0;

		initializeGuess();
		fillWantedLettersArray(this.game);
		createLettersLayouts(this.game, this.guess);
		wantedLetterHBox.getChildren().addAll(wantedLetterLayout);
		mainVBox.getChildren().add(1, statsLayout = new StatsLayout(0,
				game.getWordsLengths().get(game.getTargetWord().length()).size(), 0.0));
		mainVBox.getChildren().add(3, wantedLetterHBox);
		mainVBox.getChildren().add(5, lettersLayout[0]);
		try {
			hangLayout.updateHang(phase);
			phase = 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
