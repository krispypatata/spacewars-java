package gamewindow;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class GameStageInstruction extends GameScene{
	private StackPane menuRoot; 			// root to return to
	private StackPane root; 				// root node of this scene
	private Canvas canvas; 					// child 1
	private GraphicsContext gc;
	private BorderPane borderPane;			// child 2
	private ImageView instructionImage; 	// grandchild 1
	private TextArea instructionTxtArea;	// grandchild 2
	private HBox hBox; 						// grandchild 3
	private Button previousBtn;				// child of grandchild 3
	private Button nextBtn;					// child of grandchild 3

	private int instructionPage = 1; 		// page counter
											// for navigating through the instructions messages and images

	// instruction images
	private final static Image INSTRUCT_IMG_1 = new Image("assets/props/instruction1.png", 700, 140, false, false);
	private final static Image INSTRUCT_IMG_2 = new Image("assets/props/instruction2.png", 700, 140, false, false);
	private final static Image INSTRUCT_IMG_3 = new Image("assets/props/instruction3.png", 700, 140, false, false);
	private final static Image INSTRUCT_IMG_4 = new Image("assets/props/instruction4.png", 700, 140, false, false);
	private final static Image INSTRUCT_IMG_5 = new Image("assets/props/instruction5.png", 700, 140, false, false);
	private final static Image INSTRUCT_IMG_6 = new Image("assets/props/instruction6.png", 700, 140, false, false);
	private final static Image INSTRUCT_IMG_7 = new Image("assets/props/instruction7.png", 700, 140, false, false);
	private final static Image INSTRUCT_IMG_8 = new Image("assets/props/instruction8.png", 700, 140, false, false);
	private final static Image INSTRUCT_IMG_9 = new Image("assets/props/instruction9.png", 700, 140, false, false);
	private final static Image INSTRUCT_IMG_10 = new Image("assets/props/instruction10.png", 700, 140, false, false);
	private final static Image INSTRUCT_IMG_11 = new Image("assets/props/instruction11.png", 700, 140, false, false);
	private final static Image INSTRUCT_IMG_12 = new Image("assets/props/instruction12.png", 700, 140, false, false);

	// constructor
	GameStageInstruction (StackPane menuRoot, Scene theScene) {
		this.menuRoot = menuRoot;
		this.scene = theScene;

		// initializing the root node
		this.root = new StackPane();

		// child 1 - canvas
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.gc = this.canvas.getGraphicsContext2D();
		this.root.getChildren().add(this.canvas); 		// adding canvas to the root node

		// child 2 - border pane
		this.borderPane = new BorderPane();
		this.root.getChildren().add(this.borderPane); 	// adding borderPane to the root node

		// grandchild 1 - ImageView
		this.initializeImageView();

		// grandchild 2 - TextArea
		this.initializeTextArea();

		// grandchild 3
		this.initializeHBox();

		this.displayInstruction();

		// start animation
		this.start();
	}

	// ************************************************************************************************
	// background animation
	@Override
	public void handle(long now) {
		// moving background
		this.redrawBackground(this.gc);
	}

	// ************************************************************************************************
	// Grandchildren
	// grandchild 1 - child of borderPane
	private void initializeImageView() {
		this.instructionImage = new ImageView(GameStageInstruction.INSTRUCT_IMG_1);
		this.borderPane.setTop(this.instructionImage);
		this.instructionImage.setId("instruction-image");
		BorderPane.setAlignment(this.instructionImage, Pos.TOP_CENTER);
		BorderPane.setMargin(this.instructionImage, new Insets(20, 0, 0, 0));
	}

	// grandchild 2 - child of borderPane
	private void initializeTextArea() {
		this.instructionTxtArea = this.createTextArea();
		this.borderPane.setCenter(this.instructionTxtArea);
		this.instructionTxtArea.setMaxHeight(300);
		BorderPane.setMargin(this.instructionTxtArea, new Insets(20, 50, 10, 50));
		this.instructionTxtArea.setId("instruction-text-area");
	}

	// grandchild 3 - child of borderPane
	private void initializeHBox() {
		this.hBox = new HBox();
		this.borderPane.setBottom(this.hBox);
		this.hBox.setAlignment(Pos.BOTTOM_RIGHT);
		this.hBox.setPadding(new Insets(0, 20, 8, 20));

		// (1) create a button to go back to the previous instruction image
		this.previousBtn = this.createButton("<---");
		this.previousBtn.setId("left-arrow-button"); 	// for css styling

		// listener for when the previous button is clicked
		this.previousBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent e) {
				// decrement page counter
				instructionPage --;
				displayInstruction();
			}
		});

		// add the newly created button to our hBox
		this.hBox.getChildren().add(this.previousBtn);
		this.previousBtn.setMinWidth(120);

		// (2) create a button to change the instruction image to the next image
		this.nextBtn = this.createButton("--->");
		this.nextBtn.setId("right-arrow-button"); 	// for css styling

		// listener for when the next button is clicked
		this.nextBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				// increment page counter
				instructionPage ++;
				displayInstruction();
			}
		});

		// add the newly created button to our hBox
		this.hBox.getChildren().add(this.nextBtn);
		this.nextBtn.setMinWidth(120);
		HBox.setMargin(this.nextBtn, new Insets(0,135,0, 20));

		// (3) create a button to go back to main menu
		Button menuBtn = this.createButton("BACK");
		menuBtn.setId("menu-button"); 	// for css styling

		// listener for when the back button is clicked
		menuBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				mainMenu();
			}
		});

		// add the newly created button to our hBox
		this.hBox.getChildren().add(menuBtn);
		menuBtn.setMinWidth(120);
	}

	// ************************************************************************************************
	// instructions (when the next or previous button is clicked)
	private void displayInstruction() {
		if (this.instructionPage == 1) this.previousBtn.setVisible(false);
		else if (this.instructionPage == 12) this.nextBtn.setVisible(false);
		else {
			this.previousBtn.setVisible(true);
			this.nextBtn.setVisible(true);
		}

		switch (this.instructionPage) {
			case 1:
				this.instructionTxtArea.setText(""
						+ "At the beginning of the game, the warship appears on the leftmost side of the screen at a random y position. "
						+ "\n\nIts initial strength value is also randomly generated from 100 to 150. "
						+ "\n\nYou can use the arrow keys on your keyboard to move the warship across the screen 10 pixels at a time. "
				);
				this.instructionImage.setImage(GameStageInstruction.INSTRUCT_IMG_1);
				break;

			case 2:
				this.instructionTxtArea.setText(""
						+ "The warship shoots bullets by pressing/holding down the spacebar key. "
						+ "\n\nThe bullet's damage is equal to the current strength of the ship. "
						+ "\n\nThe bullets fired by the warship move toward the right side of the screen in a straight line and disappear as they reach the end of the screen."
				);
				this.instructionImage.setImage(GameStageInstruction.INSTRUCT_IMG_2);
				break;

			case 3:
				this.instructionTxtArea.setText(""
						+ "The game's main objective is to destroy as many spacecraft as possible. "
						+ "\n\nA spacecraft spawns on the right side of the screen at random (x,y) coordinates. "
						+ "\n\nWhen a spacecraft is spawned, its damage is randomized from 30 to 40. "
						+ "\n\nA spacecraft glides horizontally across the screen (back and forth). "
						+ "\n\nInitially, a spacecraft is alive and has a random movement speed between 1 and 5, inclusive. "
						+ "\n\nThe game starts with seven spacecrafts, but three more spawn every five seconds. "
						+ "\n\nAll spacecrafts alive will move at maximum speed for three seconds every fifteen seconds."
						+ "\n\nThe spacecrafts turn gold to indicate that their speed has been set to maximum speed."
				);
				this.instructionImage.setImage(GameStageInstruction.INSTRUCT_IMG_3);
				break;

			case 4:
				this.instructionTxtArea.setText(""
						+ "A spacecraft explodes and disappears when hit by a warship's bullet. "
						+ "\n\nWhen a spacecraft crashes into the warship, it will still die and disappear, but its damage will reduce the current strength of the warship. "
						+ "\n\nThe warship turns red for a brief moment to indicate that it has been hit by a spacecraft."
				);
				this.instructionImage.setImage(GameStageInstruction.INSTRUCT_IMG_4);
				break;

			case 5:
				this.instructionTxtArea.setText(""
						+ "Orbs appear at random locations on the left half of the screen at 10-second intervals. "
						+ "\n\nOrbs disappear if left uncollected for 5 seconds. "
						+ "\n\nThere are four orb types: Fire, Ice, Light, and Wind. "
						+ "\n\nEach orb type has a buff-game effect lasting up to 5 seconds when collected."
				);
				this.instructionImage.setImage(GameStageInstruction.INSTRUCT_IMG_5);
				break;

			case 6:
				this.instructionTxtArea.setText(""
						+ "The Fire Orb immediately doubles the current strength of the warship upon collection. "
						+ "\n\nWarship and the strength counter briefly turn green to indicate that the buff has taken effect."
				);
				this.instructionImage.setImage(GameStageInstruction.INSTRUCT_IMG_6);
				break;

			case 7:
				this.instructionTxtArea.setText(""
						+ "The Ice Orb sets all spacecrafts' movement speed to minimum speed for 5 seconds. "
						+ "\n\nSpacecrafts turn blue to indicate that their speed is at a minimum."
				);
				this.instructionImage.setImage(GameStageInstruction.INSTRUCT_IMG_7);
				break;

			case 8:
				this.instructionTxtArea.setText(""
						+ "The Light Orb provides immortality to the ship for 5 seconds. "
						+ "\n\nA Shield covering the warship will appear on the screen to indicate the warship's immortality."
				);
				this.instructionImage.setImage(GameStageInstruction.INSTRUCT_IMG_8);
				break;

			case 9:
				this.instructionTxtArea.setText(""
						+ "Wind Orb enables the warship to shoot three bullets at at a time. "
						+ "\n\nBuff-effect also lasts for 5 seconds."
				);
				this.instructionImage.setImage(GameStageInstruction.INSTRUCT_IMG_9);
				break;

			case 10:
				this.instructionTxtArea.setText(""
						+ "Game-status bar can be found at the stop of the screen during gameplay."
						+ "\n\nIt displays the following: "
						+ "\n- Countdown timer "
						+ "\n- Current strength of the warship "
						+ "\n- Number of spacecrafts killed "
						+ "\n- Number of orbs collected (per type)"
				);
				this.instructionImage.setImage(GameStageInstruction.INSTRUCT_IMG_10);
				break;

			case 11:
				this.instructionTxtArea.setText(""
						+ "The boss spawns at the 30-second mark of gameplay. "
						+ "\n\nJust like a regular spacecraft, it appears at random (x,y) coordinates on the right side of the screen. "
						+ "\n\nThe boss moves diagonally across the screen and shoots two bullets simultaneously (one on each side - left and right). "
						+ "\n\nThe boss spacecraft is bigger than a regular spacecraft and has an initial damage value - of 50. "
						+ "\n\nThe warship's strength will be reduced by the said damage value when (1) the boss unit itself hits the warship or (2) the boss' bullets hit the warship (warship turns red upon receiving damage). "
						+ "\n\nInitially, the boss is alive, has a random movement speed between 1-5, inclusive, and has a health of 3000. "
						+ "\n\nEvery time the warship’s bullet hits the boss spacecraft, boss' health is reduced by the warship’s current strength. "
						+ "\n\nThe Boss dies and disappears when its health reaches 0."
				);
				this.instructionImage.setImage(GameStageInstruction.INSTRUCT_IMG_11);
				break;

			case 12:
				this.instructionTxtArea.setText(""
						+ "The game ends after 1 minute or when the warship's strength falls to or below 0. "
						+ "\n\nSurvive within the allotted time and win the game. "
						+ "Enjoy!"
				);
				this.instructionImage.setImage(GameStageInstruction.INSTRUCT_IMG_12);
				break;
		}

		this.instructionTxtArea.setText(this.instructionTxtArea.getText().toUpperCase());
	}

	// ************************************************************************************************
	// when the menu button is clicked
	private void mainMenu() {
		this.stop();
		this.scene.setRoot(this.menuRoot);
	}

	// ************************************************************************************************
	// getter
	public Parent getRoot() {
		return this.root;
	}
}
