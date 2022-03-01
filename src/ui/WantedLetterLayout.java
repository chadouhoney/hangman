package ui;

import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WantedLetterLayout extends StackPane {

	private Text underline;
	private Text letter;
	private boolean isSelected;

	public void setSelected(boolean s) {
		isSelected = s;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public WantedLetterLayout() {
		underline = new Text("_");
		letter = new Text("?");

		Font font = Font.loadFont("file:src/resources/fonts/CrayonCrumble.ttf", 50);
		letter.setFont(font);
		underline.setFont(Font.loadFont("file:src/resources/fonts/CrayonCrumble.ttf", 80));

		this.setOnMouseEntered(e -> {
			if (!isSelected && letter.getText().equals("?")) {
				setCursor(Cursor.HAND);
				// setBorder(new Border(new BorderStroke(Color.DARKRED, BorderStrokeStyle.SOLID,
				// new CornerRadii(5),
				// BorderWidths.DEFAULT)));
				setLetterColor("#e0dbd1");
				setOpacity(0.5);
			}
			// setBlendMode(BlendMode.DARKEN);
		});

		this.setOnMouseExited(e -> {
			// underline.setOpacity(1.0);

			if (!isSelected && letter.getText().equals("?")) {
				setCursor(Cursor.DEFAULT);
				// setBorder(Border.EMPTY);
				setLetterColor("#000000");
				setOpacity(1.0);
			}
			// setBlendMode(null);
		});

		getChildren().addAll(letter, underline);
	}

	public void setLetterString(String s) {
		letter.setText(s);
	}

	public void setLetterColor(String colorHex) {
		letter.setFill(Paint.valueOf(colorHex));
	}

	public String getLetterString() {
		return letter.getText();
	}
}
