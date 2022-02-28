package ui;

import java.io.IOException;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import logic.Game;

public class GameLayout extends HBox {
	private HangLayout hangLayout;
	private LettersLayout lettersLayout;
	private PossibleSolutionsLayout possibleSolutionsLayout;
	private double width, height;
	private Game game;

	public GameLayout(Game g) {
		super(10.0);
		this.game = g;
		try {
			hangLayout = new HangLayout();
			lettersLayout = new LettersLayout(g.getLettersProbabilities().get(0));
			Region r1 = new Region();
			VBox vBox = new VBox();
			VBox.setVgrow(r1, Priority.ALWAYS);
			vBox.getChildren().addAll(r1, lettersLayout);
			//
			//
			//
			getChildren().addAll(hangLayout, vBox);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
