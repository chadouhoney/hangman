package ui;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class StatsLayout extends VBox {
	private Text playerPoints;
	private Text availableWords;
	private Text successfulAttemptsPercentage;
	
	public StatsLayout(int pp, int aw, double sap){
		super(5.0);
		setAlignment(Pos.CENTER_LEFT);
		
		this.playerPoints = new Text("Points: "+Integer.toString(pp));
		this.availableWords = new Text("Number of candidate words: "+Integer.toString(pp));
		this.successfulAttemptsPercentage = new Text("Successful attempts percentage: "+Double.toString(pp*100)+"%");
		
		this.getChildren().addAll(playerPoints,availableWords,successfulAttemptsPercentage);
	}
	
	public void updateFields(int pp, int aw, double sap){
		this.playerPoints = new Text("Points: "+Integer.toString(pp));
		this.availableWords = new Text("Number of candidate words: "+Integer.toString(pp));
		this.successfulAttemptsPercentage = new Text("Successful attempts percentage: "+Double.toString(pp*100)+"%");
	}
	
}
