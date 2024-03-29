package ui;

import java.util.Random;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
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

		VBox menu = new VBox(40);
		menu.setTranslateX(0.7 * width);
		menu.setTranslateY(height / 2);
		menu.setAlignment(Pos.CENTER);

		// TITLE
		Text title = new Text("HANGMAN");
		title.setFont(Font.loadFont("file:src/resources/fonts/CrayonCrumble.ttf", 90));
		title.setUnderline(true);
		title.setUnderline(true);
		title.setFill(Paint.valueOf("#e0dbd1"));

		// START BUTTON
		MenuButton startButton = new MenuButton("START");

		startButton.setOnMouseClicked(mouseEvent -> {
			TextInputDialog inputDialog= new TextInputDialog("");
			inputDialog.setHeaderText("Enter DICTIONARY-ID");
			inputDialog.showAndWait();
			
			String dictionaryId = inputDialog.getResult();

			if (dictionaryId != null) {
				Dictionary d = null;
				Boolean everythingok = true;
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
					Game g = new Game(d, d.getWords().get(rand.nextInt(d.getWords().size())));
					GameLayout gl = new GameLayout(g);
					root.getChildren().removeAll(MainMenu.this);
					root.getChildren().addAll(gl);
				}
			}
		});

		// CREATE BUTTON
		MenuButton createButton = new MenuButton("CREATE DICTIONARY");
		createButton.setOnMouseClicked(e -> {
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
			// b.setAlignment(Pos.CENTER);
			b.setTranslateX(200 - b.getWidth() / 2);
			dialogVbox.getChildren().addAll(openLibIdLabel, openLibIdTextArea, textNameLabel, fileIdTextArea, b);
			Scene dialogScene = new Scene(dialogVbox, 400, 200);
			dialog.setScene(dialogScene);
			dialog.setResizable(false);
			dialog.showAndWait();

		});


		// EXIT BUTTON
		MenuButton exitButton = new MenuButton("EXIT");
		exitButton.setOnMouseClicked(mouseEvent -> {
			System.exit(0);
		});

		
		menu.getChildren().addAll(title, startButton, createButton, exitButton);
		
		getChildren().addAll(menu);
	}

	protected class MenuButton extends StackPane {
		private Text text;

		public MenuButton(String s) {

			text = new Text(s);
			Font font = Font.loadFont("file:src/resources/fonts/CrayonCrumble.ttf", 50);
			text.setFont(font);
			text.setFill(Paint.valueOf("#e0dbd1"));
			text.setTextAlignment(TextAlignment.CENTER);
			getChildren().addAll(text);

			this.setOnMouseEntered(mouseEvent -> {
				text.setFontSmoothingType(FontSmoothingType.LCD);
				text.setOpacity(0.5);
				setCursor(Cursor.HAND);
			});

			this.setOnMouseExited(mouseEvent -> {
				text.setFontSmoothingType(null);
				setCursor(Cursor.DEFAULT);
				text.setOpacity(1.0);

			});

		}

	}

}
