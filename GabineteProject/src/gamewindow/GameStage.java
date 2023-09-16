package gamewindow;

import java.util.ArrayList;
import java.util.Random;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import spacewars.Explosion;
import spacewars.GameTimer;

public class GameStage extends GameScene {
	public static final int WINDOW_HEIGHT = 500;
	public static final int WINDOW_WIDTH = 800;
	public final static String PIXEL_FOUNT_SRC = "file:src/assets/04B03.TTF";
	public final static String RICH_BLACK = "#000418";
	public final static Image BACKGROUND_STARS = new Image("assets/background/bg-stars.png",GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,false,false);
	public final static Image MENU_TITLE = new Image("assets/props/menu-title.gif",600,0,true,false);
	public final static Image MENU_TITLE_ON_HOVER = new Image("assets/props/menu-title-on-hover.gif",600,0,true,false);
	public final static double BACKGROUND_SPEED = 0.25;

	// game components
	private Group gameRoot;
	private Canvas gameCanvas;
	private GraphicsContext gameGc;
	private GameTimer gametimer;

	// Main Menu Components
	private StackPane menuRoot;
	private Canvas menuCanvas; 						// first child
	private GraphicsContext menuGc;
	private BorderPane menuBorderPane;  			// second child
	private ImageView menuTitle;					// first grandchild
	private VBox menuVBox;							// second grandchild
	private ArrayList<Explosion> explosionEffects; 	// for some visual effects

	//the class constructor
	public GameStage() {
		this.gameRoot = new Group();
		this.scene = new Scene(this.gameRoot, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,Color.WHITE);

		// for designing javafx controls via css
		this.scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());

		// loading custom font for css styling (designing javafx UIs)
		Font.loadFont("file:src/assets/04B03.TTF", 10);

		this.gameCanvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.gameGc = this.gameCanvas.getGraphicsContext2D();
	}

	// ************************************************************************************************
	// Background Animation (Splash Screen)
	@Override
	public void handle(long nocurrentNanoTimew) {
		// moving background
		this.redrawBackground(this.menuGc);

		// render explosion effects
		this.renderExplosionEffects();
	}

	// ************************************************************************************************
	//method to add the stage elements
	public void setStage(Stage stage) {
		this.stage = stage;

		//set stage elements here
		this.gameRoot.getChildren().add(gameCanvas);

		this.stage.setScene(this.scene);

		// main menu
		this.initializeMainMenu();
		this.scene.setRoot(this.menuRoot); 		// setting main menu as the new root of the scene

		this.stage.setResizable(false);
		this.stage.getIcons().add(new Image("assets/warship/warship2.png"));
		this.stage.setTitle("Space Wars");
		this.stage.show();
	}

	// ************************************************************************************************
	// Splash screen
	private void initializeMainMenu() {
		this.menuRoot = new StackPane();

		// child 1 - canvas
		this.menuCanvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		this.menuGc = this.menuCanvas.getGraphicsContext2D();
		this.menuRoot.getChildren().add(this.menuCanvas); 		// adding canvas to menuRoot

		// child 2 - border pane
		this.menuBorderPane = new BorderPane();
		this.menuRoot.getChildren().add(this.menuBorderPane); 	// adding border pane to menuRoot

		// grandchild 1 - image view
		this.menuTitle = new ImageView(GameStage.MENU_TITLE);
		this.menuBorderPane.setCenter(this.menuTitle);
		this.initializeImageView();

		// grandchild 2 - VBox for buttons
		this.menuVBox = new VBox();
		this.menuBorderPane.setBottom(this.menuVBox); 			// adding vBox to bottom region of the border pane
		this.initializeVBox();

		// for some visual effects
		this.explosionEffects = new ArrayList<Explosion>();

		// start animation
		this.start();
	}

	// ************************************************************************************************
	// ImageView MenuTitle - grandchild of root - child of borderpane
	private void initializeImageView() {
		BorderPane.setMargin(this.menuTitle, new Insets(100, 0, 0, 0));

		this.menuTitle.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				menuTitle.setImage(GameStage.MENU_TITLE_ON_HOVER);
			}
		});

		this.menuTitle.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				menuTitle.setImage(GameStage.MENU_TITLE);
			}
		});

		// some visual effects
		this.menuTitle.setOnMouseClicked(new EventHandler<MouseEvent>() {
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
	// VBox with buttons - grandchild of root - child of borderPane
	private void initializeVBox() {
		this.menuVBox.setAlignment(Pos.BOTTOM_CENTER); 	// aligning vBox to the center
		this.menuVBox.setSpacing(5); 					// spacing between vBox contents

		// (1) create a play game button
		Button playBtn = this.createButton("PLAY");

		// listener for when the play game button is clicked
		playBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				setGame();
			}
		});

		// add the newly created button to our vBox
		this.menuVBox.getChildren().add(playBtn);

		// (2) create a button that will allow the user to go to the instructions menu
		Button instructionsBtn = this.createButton("INSTRUCTIONS");
		instructionsBtn.setId("instructions-button"); 		// for css styling

		// listener for when the instructions button is clicked
		instructionsBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				goToInstruction();
			}
		});

		// add the newly created button to our vBox
		this.menuVBox.getChildren().add(instructionsBtn);


		// (3) create a button that will allow the user to go to the about menu
		Button aboutBtn = this.createButton("ABOUT");
		aboutBtn.setId("about-button");				// for css styling

		// listener for when the about button is clicked
		aboutBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				goToAbout();
			}
		});

		// add the newly created button to our vBox
		this.menuVBox.getChildren().add(aboutBtn);


		// (4) create an exit button
		Button exitBtn = this.createButton("EXIT");
		exitBtn.setId("exit-button");
		// listener for when the exit button is clicked
		exitBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				// terminate the program
				System.exit(0);
			}
		});

		// add the newly created button to our vBox
		this.menuVBox.getChildren().add(exitBtn);


		// margin for the exit button (so it's not that too close to the bottom window)
		VBox.setMargin(exitBtn, new Insets(0, 0, 70, 0));
	}


	// ************************************************************************************************
	// setting new game (when the play button is clicked)
	private void setGame() {
		this.stop();
		this.scene.setRoot(this.gameRoot);
		this.gametimer = new GameTimer(this.gameGc, this.stage); 	// to reset the timer
		this.gametimer.start();										// start the game
	}

	// ************************************************************************************************
	// when the instructions button is clicked
	private void goToInstruction() {
		GameStageInstruction instructionMenu = new GameStageInstruction(this.menuRoot, this.scene);
		this.scene.setRoot(instructionMenu.getRoot());
	}

	// ************************************************************************************************
	// when the about button is clicked
	private void goToAbout() {
		GameStageAbout aboutMenu = new GameStageAbout(this.menuRoot, this.scene);
		this.scene.setRoot(aboutMenu.getRoot());
	}

	// ************************************************************************************************
	// method that will render explosions
	private void renderExplosionEffects() {
		for(int i = 0; i < this.explosionEffects.size(); i++) {
			Explosion explosion = this.explosionEffects.get(i);

			if (explosion.isAnimationComplete()) this.explosionEffects.remove(explosion);
			else {
				explosion.applyEffect();
				explosion.render(this.menuGc);
			}
		}
	}

}

