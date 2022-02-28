package ui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Guess {
	private int forPosition;
	private int letterArrayPos;
	public SimpleStringProperty stringProperty;
	public SimpleBooleanProperty booleanProperty;

	public Guess() {
		stringProperty = new SimpleStringProperty();
		booleanProperty = new SimpleBooleanProperty();
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

	public SimpleStringProperty stringPropertyProperty() {
		return stringProperty;
	}

	public void setStringProperty(String stringProperty) {
		this.stringProperty.set(stringProperty);
	}

	public boolean isBooleanProperty() {
		return booleanProperty.get();
	}

	public SimpleBooleanProperty booleanPropertyProperty() {
		return booleanProperty;
	}

	public void setBooleanProperty(boolean booleanProperty) {
		this.booleanProperty.set(booleanProperty);
	}

	public int getLetterArrayPos() {
		return letterArrayPos;
	}

	public void setLetterArrayPos(int letterArrayPos) {
		this.letterArrayPos = letterArrayPos;
	}
}
