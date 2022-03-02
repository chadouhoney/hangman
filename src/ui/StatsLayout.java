package ui;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class StatsLayout extends VBox {
	private Text playerPoints;
	private Text availableWords;
	private Text successfulAttemptsPercentage;
	
	public StatsLayout(int pp, int aw, double sap){
		super(10);
		setAlignment(Pos.CENTER_RIGHT);
		this.playerPoints = new Text("Points: "+Integer.toString(pp));
		this.playerPoints.setFont(Font.loadFont("file:src/resources/fonts/CrayonCrumble.ttf", 25));
		this.playerPoints.setFill(Paint.valueOf("#e0dbd1"));

		this.availableWords = new Text("Number of candidate words: " + Integer.toString(aw));
		this.availableWords.setFont(Font.loadFont("file:src/resources/fonts/CrayonCrumble.ttf", 25));
		this.availableWords.setFill(Paint.valueOf("#e0dbd1"));

		this.successfulAttemptsPercentage = new Text("Successful attempts percentage: " + String.format("%1.3f", sap));
		this.successfulAttemptsPercentage.setFont(Font.loadFont("file:src/resources/fonts/CrayonCrumble.ttf", 25));
		this.successfulAttemptsPercentage.setFill(Paint.valueOf("#e0dbd1"));
		
		this.getChildren().addAll(playerPoints,availableWords,successfulAttemptsPercentage);
	}
	
	public void updateFields(int pp, int aw, double sap){
		this.playerPoints.setText("Points: " + Integer.toString(pp));
		this.availableWords.setText("Number of candidate words: " + Integer.toString(aw));
		this.successfulAttemptsPercentage.setText("Successful attempts percentage: " + String.format("%1.3f", sap));
	}
	
}
