package ui;

import exception.HangmanException;
import javafx.scene.Parent;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import sample.Dictionary;

public class MainMenu extends Parent {
	
	public MainMenu(){
		
		VBox menu = new VBox(10);
		menu.setTranslateX(500);
		menu.setTranslateY(500);
		
		MenuButton startButton = new MenuButton("START");
		startButton.setOnMouseClicked(mouseEvent -> {
			TextInputDialog inputDialog= new TextInputDialog("");
			inputDialog.setHeaderText("Enter OPEN_LIBRARY_ID");
			inputDialog.showAndWait();
			
			String openLibraryId = inputDialog.getResult();

			if (openLibraryId != null) {
				try {
					Dictionary d = new Dictionary(openLibraryId);
					System.out.println(d.getWords());
				} catch (HangmanException e) {
					e.printStackTrace();
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
			Font font = Font.loadFont("C:/Users/andre/Desktop/Multimedia/hangman/src/resources/fonts/Eraser.ttf", 60);
			text.setFont(font);
			text.setFill(null);
			text.setStroke(Color.WHITESMOKE);
			getChildren().addAll(text);
			
			this.setOnMouseEntered(mouseEvent -> {
				text.setStrikethrough(true);
				text.setFontSmoothingType(FontSmoothingType.LCD);
			});
			
			this.setOnMouseExited(mouseEvent -> {
				text.setStrikethrough(false);
				text.setFontSmoothingType(null);
			});
			
			
		}
		
	}
	
	
}
