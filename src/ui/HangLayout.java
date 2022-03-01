package ui;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class HangLayout extends Pane {

	private ImageView imgView;

	public HangLayout() throws IOException {
		// this.parent = gl;
		InputStream is = Files.newInputStream(Paths.get("src/resources/images/stage0.png"));
		Image img = new Image(is, 270, 630, false, false);
		is.close();
		this.imgView = new ImageView(img);

		getChildren().addAll(imgView);
	}

	public void updateHang(int i) throws IOException {
		InputStream is = Files.newInputStream(Paths.get("src/resources/images/stage" + i + ".png"));
		Image img = new Image(is, 270, 630, false, false);
		is.close();
		imgView.setImage(img);
	}
}
