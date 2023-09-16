package gamewindow;

import java.util.ArrayList;
import java.util.Random;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import spacewars.Explosion;
import spacewars.GameTimer;

public class GameOverStage extends GameScene {
	private StackPane root;

	// children
	private Canvas canvas;				// child 1
	private GraphicsContext gc;
	private BorderPane borderPane;		// child 2
	private ImageView msgImage;			// child 3

	// grandchildren (children of borderPane
	private TextArea textArea;			// grandchild 1
	private VBox vBox; 					// grandchild 2

	// game stats
	private boolean isGameWon;
	private boolean isBossKilled;
	private int score;
	private int fireCollected;
	private int iceCollected;
	private int lightCollected;
	private int windCollected;
	private int totalOrbsCollected;

	private boolean setToTop = false; // for setting scroll button back to top in text Area

	// for game over ImageView
	private final static Image GAME_OVER = new Image("assets/props/game-over.gif", 0, 80, true, false);
	private final static Image GAME_OVER_ON_HOVER = new Image("assets/props/game-over-on-hover.gif", 0, 80, true, false);

	private ArrayList<Explosion> explosionEffects; 	// for some visual effects

	// constructor
	public GameOverStage(Stage stage, boolean isGameWon, boolean isBossKilled, int score, int fireCollected, int iceCollected, int lightCollected, int windCollected, int totalOrbsCollected) {
		this.stage = stage;
		this.scene = stage.getScene();
		this.isGameWon = isGameWon;
		this.isBossKilled = isBossKilled;
		this.score = score;
		this.fireCollected = fireCollected;
		this.iceCollected = iceCollected;
		this.lightCollected = lightCollected;
		this.windCollected = windCollected;
		this.totalOrbsCollected = totalOrbsCollected;

		// instantiate root
		this.root = new StackPane();

		// child 1 - canvas
		// instantiate the canvas and the graphics context
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();
		this.root.getChildren().add(canvas); 		// adding child 1 to root node

		// child 2 - BorderPane
		this.borderPane = new BorderPane();
		this.root.getChildren().add(this.borderPane); // adding child 2 to the root node

		// grandchild 1 - child of borderPane - Read-only Scrollable TextArea
		this.initializeTextArea();
		this.displayScoreboard();

		// grandchild 2 - child of borderPane - VBox for buttons
		this.initializeVBox();

		// child 3 - ImageView
		this.initializeGameOverImageView();

		// for some visual effects
		this.explosionEffects = new ArrayList<Explosion>();

		// start the animation
		this.start();
	}

	// ************************************************************************************************
	// Background animation
	@Override
	public void handle(long currentNanoTime) {
		// moving background
		this.redrawBackground(this.gc);

		// forcing the vertical scroll button to point at the top of the text right after the text updates (in text area)
		if (this.textArea.getScrollTop()!=0 &&  this.setToTop == false) {
			this.textArea.setScrollTop(Double.MIN_VALUE);
			if (this.textArea.getScrollTop() == 0) this.setToTop = true;
		}

		// render explosion effects
		this.renderExplosionEffects();
	}

	// ************************************************************************************************
	// Grandchild 1
	private void initializeTextArea() {
		this.textArea = this.createTextArea();
		this.borderPane.setCenter(textArea); 								// adding textArea to borderPane
		BorderPane.setMargin(this.textArea, new Insets(100, 50, 20, 50)); 	// margin constraints
	}

	// ************************************************************************************************
	private void displayScoreboard() {
		// Add the message to the canvas
		if (this.isGameWon == true && this.isBossKilled == true) {
			this.textArea.setText(
				"\tYOU COMPLETED THE GAME AND \n     DEFEATED THE BOSS. " +
				"EXCELLENT JOB!"
			);
		} else if (this.isGameWon == true && this.isBossKilled == false) {
			this.textArea.setText(
				"       YOU DIDN'T DEFEAT THE BOSS BUT\n  STILL COMPLETED THE GAME. " +
				"GOOD JOB!" +
				"\n  DON'T WORRY. MAYBE YOU'LL GET THEM\n  NEXT TIME."
			);
		} else if (this.isGameWon == false && this.isBossKilled == false) {
			this.textArea.setText(
				"      AWW... YOU DIED BEFORE THE TIMER \n RAN OUT :( . " +
				"DON'T GIVE UP! " +
				"MAYBE IT'LL\n BE DIFFERENT THE NEXT TIME AROUND."
			);
		}

		// gameStats
		this.textArea.appendText(
			"\n\nGAME STATS" +
			"\n\t SPACECRAFTS KILLED: " + this.score +
			"\n\t     ORBS COLLECTED " +
			"\n\tFIRE: " + this.fireCollected +
			"\t\tLIGHT: " + this.lightCollected +
			"\n\tICE: " + this.iceCollected +
			"\t\tWIND: " + this.windCollected +
			"\n\t\tTOTAL: " + this.totalOrbsCollected
		);

		System.out.println(this.isGameWon);
		System.out.println(this.isBossKilled);
	}


	// ************************************************************************************************
	// Grandchild 2
	private void initializeVBox() {
		this.vBox = new VBox();
		this.borderPane.setBottom(this.vBox); 			// adding the vBox to borderPane Node

		this.vBox.setAlignment(Pos.BOTTOM_CENTER); 	// aligning vBox to the center
		this.vBox.setSpacing(5); 					// spacing between vBox contents

		// (1) create a new game button
		Button newGameBtn = this.createButton("PLAY AGAIN");

		// listener for when the new game button is clicked
		newGameBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				// print on the console for reference
				System.out.println("\nPlaying again!");

				// instantiate new game
				newGame();
			}
		});

		// add the newly created button to our vBox
		this.vBox.getChildren().add(newGameBtn);


		// (2) create a button that will allow the user to go back to the main menu
		Button menuBtn = this.createButton("MAIN MENU");
		menuBtn.setId("menu-button"); 		// for css styling

		// listener for when the menu button is clicked
		menuBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				// print on the console for reference
				System.out.println("\nProgram Resets...");
				mainMenu();
			}
		});

		// add the newly created button to our vBox
		this.vBox.getChildren().add(menuBtn);


		// (3) create an exit button
		Button exitBtn = this.createButton("EXIT");
		exitBtn.setId("exit-button"); 	// for css styling

		// listener for when the exit button is clicked
		exitBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent e) {
				// print on the console for reference
				System.out.println("\nProgram Exits...Bye!");

				// terminate the program
				System.exit(0);
			}
		});

		// add the newly created button to our vBox
		this.vBox.getChildren().add(exitBtn);


		// margin for the exit button (so it's not that too close to the bottom window)
		VBox.setMargin(exitBtn, new Insets(0, 0, 30, 0));
	}


	// ************************************************************************************************
	// Button on-clicked events
	// when the new game button is clicked
	private void newGame() {
		this.stop(); 					// stopping the animation handle

		// creating new root and contents
		Group newRoot = new Group();
		Canvas newCanvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		GraphicsContext newGc = newCanvas.getGraphicsContext2D();
		newRoot.getChildren().add(newCanvas);
		this.scene.setRoot(newRoot);

		GameTimer gameTimer = new GameTimer(newGc, this.stage);
		gameTimer.start(); 				// starting gameTimer animation handle
	}

	// ************************************************************************************************
	// when the menu button is clicked
	private void mainMenu() {
		this.stop();

		GameStage theGameStage = new GameStage();
		theGameStage.setStage(this.stage);
	}

	// ************************************************************************************************
	// child 3 - ImageView
	private void initializeGameOverImageView() {
		this.msgImage = new ImageView(GameOverStage.GAME_OVER);
		this.root.getChildren().add(this.msgImage);
		StackPane.setAlignment(this.msgImage, Pos.TOP_CENTER);
		StackPane.setMargin(this.msgImage, new Insets(15,0,0,0));

		this.msgImage.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				msgImage.setImage(GameOverStage.GAME_OVER_ON_HOVER);
			}
		});

		this.msgImage.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				msgImage.setImage(GameOverStage.GAME_OVER);
			}
		});

		// some visual effects
		this.msgImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				Random r = new Random();
				int x = r.nextInt(GameStage.WINDOW_WIDTH-50);
				int y = r.nextInt(GameStage.WINDOW_HEIGHT-50);
				int width = r.nextInt(51) + 50;
				explosionEffects.add(new Explosion(x, y, width, width));
			}
		});
	}

	// ************************************************************************************************
	// method that will render explosions
	private void renderExplosionEffects() {
		for(int i = 0; i < this.explosionEffects.size(); i++) {
			Explosion explosion = this.explosionEffects.get(i);

			if (explosion.isAnimationComplete()) this.explosionEffects.remove(explosion);
			else {
				explosion.applyEffect();
				explosion.render(this.gc);
			}
		}
	}

	// ************************************************************************************************
	// getter for the root node
	public StackPane getRoot(){
		return this.root;
	}

	public StackPane getRootZeroOpacity(){
		this.root.setOpacity(0);
		return this.root;
	}


}
