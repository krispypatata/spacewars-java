package gamewindow;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public abstract class GameScene extends AnimationTimer{
	protected Stage stage;
	protected Scene scene;

	// for moving background
	protected static double bgStarsX;

	// Moving Background
	protected void redrawBackground(GraphicsContext gc) {
		// clears the canvas
		gc.clearRect(0, 0, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);

		// Add background to the canvas
		gc.setFill(Color.web(GameStage.RICH_BLACK));
		gc.fillRect(0, 0, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);

		GameScene.bgStarsX += GameStage.BACKGROUND_SPEED;
        // moving stars
        gc.drawImage(GameStage.BACKGROUND_STARS, GameStage.WINDOW_WIDTH-GameScene.bgStarsX, 0);
        gc.drawImage(GameStage.BACKGROUND_STARS, -GameScene.bgStarsX, 0);

        if(GameScene.bgStarsX>=GameStage.WINDOW_WIDTH) GameScene.bgStarsX = 0;
	}

	// creating textArea
	protected TextArea createTextArea() {
		TextArea textArea = new TextArea();							// adding textArea to borderPane
		textArea.setWrapText(true); 									// so there won't be a horizontal scroll bar
		textArea.setPadding(new Insets(20, 25, 20, 25)); 				// set padding because of round borders of text area
		textArea.setEditable(false);
		return textArea;
	}

	// for creating button
	protected Button createButton(String textLabel) {
		Button newBtn = new Button(textLabel);
		newBtn.setPadding(Insets.EMPTY); 				// remove button padding
		newBtn.setPadding(new Insets(0, 20, 0, 20));	// adding new padding
		return newBtn;
	}
}
