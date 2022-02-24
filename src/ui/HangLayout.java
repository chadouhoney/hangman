package ui;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class HangLayout extends Pane {

	private GameLayout parent;
	private ImageView imgView;

	public HangLayout(GameLayout gl) throws IOException {
		this.parent = gl;
		InputStream is = Files.newInputStream(Paths.get("src/resources/images/stage0.png"));
		Image img = new Image(is, 0.3 * parent.getWidth(), 0.7 * parent.getHeight(), false, false);
		is.close();
		this.imgView = new ImageView(img);
		getChildren().addAll(imgView);
	}

	public void updateHang(int i) throws IOException {
		InputStream is = Files.newInputStream(Paths.get("src/resources/images/stage" + i + ".png"));
		Image img = new Image(is, getPrefWidth(), getPrefHeight(), false, false);
		is.close();
		imgView.setImage(img);
	}
}
