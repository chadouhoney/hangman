package ui;

import java.util.Random;

import exception.HangmanException;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import logic.Dictionary;
import logic.Game;

public class MainMenu extends Parent {
	private StackPane root;
	private Stage stg;
	private double width, height;

	public MainMenu(StackPane root) {
		super();
		this.root = root;
		this.width = root.getPrefWidth();
		this.height = root.getPrefHeight();

		VBox menu = new VBox(50);
		menu.setTranslateX(0.7 * width);
		menu.setTranslateY(height / 2);
		menu.setAlignment(Pos.CENTER);

		// START BUTTON
		MenuButton startButton = new MenuButton("START");
		startButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				TextInputDialog inputDialog = new TextInputDialog("");
				inputDialog.setHeaderText("Enter OPEN_LIBRARY_ID");
				inputDialog.showAndWait();

				String openLibraryId = inputDialog.getResult();

				if (openLibraryId != null) {
					Dictionary d = null;
					boolean everythingok = true;
					try {
						d = new Dictionary(openLibraryId);
						System.out.println(d.getWords());
					} catch (HangmanException e) {
						Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
						everythingok = false;
					}

					if (everythingok) {
						// start new game with random word from d
						Random rand = new Random();
						Game g = new Game(d, d.getWords().get(rand.nextInt(d.getWords().size())));
						GameLayout gl = new GameLayout(g);
						root.getChildren().removeAll(MainMenu.this);
						root.getChildren().addAll(gl);
						// VBox vBox = new VBox(10.0);
						// try {
						// HangLayout hangLayout = new HangLayout();
						// LettersLayout lettersLayout = new
						// LettersLayout(g.getLettersProbabilities().get(0));
						// vBox.getChildren().addAll(hangLayout, lettersLayout);
						// } catch (IOException e) {
						// e.printStackTrace();
						// }
						//
						// Stage stg = (Stage) root.getScene().getWindow();
						// stg.setScene(new Scene(gl, 900, 600));
						// System.out.println(vBox.getChildren());
					}

					System.out.println("KREMALA FASISTA");
				}
			}
		});

		// EXIT BUTTON
		MenuButton exitButton = new MenuButton("EXIT");
		exitButton.setOnMouseClicked(mouseEvent -> {
			System.exit(0);
		});

		menu.getChildren().addAll(startButton, exitButton);

		getChildren().addAll(menu);
	}

	protected class MenuButton extends StackPane {
		private Text text;

		public MenuButton(String s) {
			text = new Text(s);
			Font font = Font.loadFont("file:src/resources/fonts/EraserRegular.ttf", 40);
			text.setFont(font);
			text.setFill(Paint.valueOf("#e0dbd1"));
			text.setTextAlignment(TextAlignment.CENTER);
			getChildren().addAll(text);

			this.setOnMouseEntered(mouseEvent -> {
				text.setFontSmoothingType(FontSmoothingType.LCD);
				text.setStroke(Paint.valueOf("#000000"));
				setCursor(Cursor.HAND);
				text.setUnderline(true);
			});

			this.setOnMouseExited(mouseEvent -> {
				text.setFontSmoothingType(null);
				text.setStroke(null);
				setCursor(Cursor.DEFAULT);
				text.setUnderline(false);

			});

		}

	}

}
