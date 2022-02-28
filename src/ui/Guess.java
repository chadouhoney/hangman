package ui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Guess {
	private int forPosition;
	private int letterArrayPos;
	public SimpleStringProperty stringProperty;
	public SimpleBooleanProperty playerWon;
	public SimpleBooleanProperty playerLost;

	public Guess() {
		stringProperty = new SimpleStringProperty();
		playerWon = new SimpleBooleanProperty(false);
		playerLost = new SimpleBooleanProperty(false);
	}

	public int getForPosition() {
		return forPosition;
	}

	public void setForPosition(int forPosition) {
		this.forPosition = forPosition;
	}

	public String getStringProperty() {
		return stringProperty.get();
	}

	public int getLetterArrayPos() {
		return letterArrayPos;
	}

	public void setLetterArrayPos(int letterArrayPos) {
		this.letterArrayPos = letterArrayPos;
	}
}
