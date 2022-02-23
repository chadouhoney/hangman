package ui;

import exception.HangmanException;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import sample.Dictionary;

public class MainMenu extends Parent {
	private Pane root;
	private double width, height;
	
	public MainMenu(Pane root) {
		this.root = root;
		this.width = root.getPrefWidth();
		this.height = root.getPrefHeight();
		
		VBox menu = new VBox(50);
		menu.setTranslateX(width / 2);
		menu.setTranslateY(height / 2);
		menu.setAlignment(Pos.CENTER);
		
		MenuButton startButton = new MenuButton("START");
		startButton.setOnMouseClicked(mouseEvent -> {
			TextInputDialog inputDialog= new TextInputDialog("");
			inputDialog.setHeaderText("Enter OPEN_LIBRARY_ID");
			inputDialog.showAndWait();
			
			String openLibraryId = inputDialog.getResult();

			if (openLibraryId != null) {
				Dictionary d;
				Boolean everythingok = true;
				try {
					d = new Dictionary(openLibraryId);
					System.out.println(d.getWords());
				} catch (HangmanException e) {
					Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
					everythingok = false;
				}

				if (everythingok) {
					// start new game with random word from d
				}

				System.out.println("KREMALA FASISTA");
			}
		});
		
		MenuButton exitButton = new MenuButton("EXIT");
		exitButton.setOnMouseClicked(mouseEvent -> {
			System.exit(0);
		});
		
		menu.getChildren().addAll(startButton,exitButton);
		
		
		getChildren().addAll(menu);
	}
	

	
	protected class MenuButton extends StackPane {
		private Text text;
		
		public MenuButton(String s) {
			text = new Text(s);
			Font font = Font.loadFont("file:src/resources/fonts/EraserRegular.ttf", 40);
			text.setFont(font);
			text.setFill(Paint.valueOf("#e0dbd1"));
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
