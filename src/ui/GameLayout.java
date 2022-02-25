package ui;

import java.io.IOException;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import logic.Game;

public class GameLayout extends Parent {
	private HangLayout hangLayout;
	private LettersLayout lettersLayout;
	private PossibleSolutionsLayout possibleSolutionsLayout;
	private double width, height;
	private Game game;

	public GameLayout(Game g, Pane parent) {
		this.game = g;
		height = parent.getPrefHeight();
		width = parent.getPrefWidth();
		try {
			hangLayout = new HangLayout(this);
			//
			//
			//
			getChildren().addAll(hangLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
}
