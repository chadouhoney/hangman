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

		VBox menu = new VBox(50);
		menu.setTranslateX(0.7 * width);
		menu.setTranslateY(height / 2);
		menu.setAlignment(Pos.CENTER);

		// START BUTTON
		MenuButton startButton = new MenuButton("START");

		startButton.setOnMouseClicked(mouseEvent -> {
			TextInputDialog inputDialog= new TextInputDialog("");
			inputDialog.setHeaderText("Enter DICTIPONARY-ID");
			inputDialog.showAndWait();
			
			String dictionaryId = inputDialog.getResult();

			if (dictionaryId != null) {
				Dictionary d = null;
				Boolean everythingok = true;
				try {
					d = new Dictionary(dictionaryId);
					System.out.println(d.getWords());
				} catch (Exception e) {
					Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
					alert.show();
					everythingok = false;
				}

				if (everythingok) {
					// start new game with random word from d
					Random rand = new Random();
					Game g = new Game(d, d.getWords().get(rand.nextInt(d.getWords().size())));
					GameLayout gl = new GameLayout(g);
					root.getChildren().removeAll(this);
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

		
		menu.getChildren().addAll(startButton, createButton, exitButton);
		
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
