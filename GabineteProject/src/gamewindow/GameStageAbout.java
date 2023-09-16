package gamewindow;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class GameStageAbout extends GameScene {
	private StackPane menuRoot; 		// root to return to

	private StackPane root; 			// root node of this scene
	private Canvas canvas; 				// child 1
	private GraphicsContext gc;
	private BorderPane borderPane;		// child 2
	private TextArea aboutTxtArea;		// grandchild 1
	private TextArea referencesTxtArea; // grandchild 2
	private HBox hBox; 					// grandchild 3

	// additional prop labels (children 3 & 4)
	Label aboutLabel;
	Label referencesLabel;

	// constructor
	GameStageAbout(StackPane menuRoot, Scene theScene) {
		this.menuRoot = menuRoot;
		this.scene = theScene;

		// initializing the root node
		this.root = new StackPane();

		// child 1 - canvas
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.gc = this.canvas.getGraphicsContext2D();
		this.root.getChildren().add(this.canvas); 		// adding canvas to the root node

		// child 2
		this.borderPane = new BorderPane();
		this.root.getChildren().add(this.borderPane); 	// adding borderPane to the root node

		// grandchildren 1 & 2 - children of borderPane - Read-only Scrollable TextAreas
		this.initializeTextAreas();
		this.setTextAreasContents();

		// grandchild 3
		this.initializeHBox();

		// additional prop labels (children 3 & 4)
		this.initializePropLabels();

		// start the animation of the background
		this.start();
	}

	// ************************************************************************************************
	// Background Animation
	@Override
	public void handle(long currentNanoTime) {
		// moving background
		this.redrawBackground(this.gc);
	}

	// ************************************************************************************************
	// TextAreas - grandchildren of root - children of borderPane
	private void initializeTextAreas() {
		this.aboutTxtArea = this.createTextArea();
		this.aboutTxtArea.setMaxHeight(170);
		this.borderPane.setTop(this.aboutTxtArea); 									// adding textArea to borderPane
		BorderPane.setMargin(this.aboutTxtArea, new Insets(50, 50, 0, 50)); 		// margin constraints
		this.aboutTxtArea.setId("about-text-area");

		this.referencesTxtArea = this.createTextArea();
		this.referencesTxtArea.setMaxHeight(170);
		this.borderPane.setCenter(this.referencesTxtArea);
		BorderPane.setMargin(this.referencesTxtArea, new Insets(50, 50, 10, 50)); 	// margin constraints
		this.referencesTxtArea.setId("about-text-area");
	}

	// ************************************************************************************************
	// text contents of the two TextAreas created
	private void setTextAreasContents() {
		this.aboutTxtArea.setText(""
				+ "This program is a simple space shooter game developed in partial fulfillment of the course requirements in CMSC 22 at the University of the Philippines - Los Baños. "
				+ "All text, data, art, graphics, or code on this mini-project are either original, referenced to original sources, used in good faith under academic \"fair use,\" or, to the best of the developer's knowledge, are non-copyright. "
				+ "In case of questions about copyright or intellectual property rights, don't hesitate to contact the developer, Keith Ginoel Gabinete, at his official UP e-mail address <ksgabinete@up.edu.ph>."
		);
		this.aboutTxtArea.setText(this.aboutTxtArea.getText().toUpperCase());

		this.referencesTxtArea.setText(""
				+ "MINI-PROJECT SPECS\n"
				+ "https://docs.google.com/document/d/1rhha1Asivqwb5G0yzLqAdRjRgeAAUIQNAzzXH5sQXDw/edit"
				+ "\n\n"
				+ "MINI-PROJECT TEMPLATE\n"
				+ "https://drive.google.com/file/d/1MqSoVPIefluwv723LomqAATdLzoIkG0v/view?usp=drive_link"
				+ "\n\n"
				+ "MINI-PROJECT CHECKLIST\n"
				+ "https://docs.google.com/document/d/105Nlc7JFf2ZkrP85WtF1wQ0K6PZ6t0Ww69-PtpMWDj8/edit"
				+ "\n\n"
				+ "CMSC 22 COURSE PACK\n"
				+ "https://drive.google.com/drive/folders/1hwDe2Mg1gcskJZR8tFyhEQ4_PQ8ruUMD"
				+ "\n\n"
				+ "JAVA 8 DOCUMENTATION (SE & JAVAFX)\n"
				+ "https://docs.oracle.com/javase/8/"
				+ "\n\n"
				+ "MINI-PROJECT SPACEWARS THEME\n"
				+ "https://ansimuz.itch.io/warped-space-shooter"
				+ "\n\n"
				+ "GAME ASSETS/PROPS\n"
				+ "https://opengameart.org/content/powerup-animated-orb"
				+ "\n\n"
				+ "https://www.spriters-resource.com/pc_computer/vibrantventuredemo/sheet/142748/"
				+ "\n\n"
				+ "https://opengameart.org/content/warp-effect-2"
				+ "\n\n"
				+ "https://www.dafont.com/04b-03.font"
				+ "\n\n"
				+ "https://www.spriters-resource.com/neo_geo_ngcd/ms3/sheet/46369/"
				+ "\n\n"
				+ "https://en.bloggif.com/text"
				+ "\n\n"
				+ "YOUTUBE TUTORIAL (PLAYLISTS INCLUDED)\n"
				+ "BRO CODE\n"
				+ "https://www.youtube.com/watch?v=_7OM-cMYWbQ&list=PLZPZq0r_RZOM-8vJA3NQFZB7JroDcMwev"
				+ "\n\n"
				+ "JAVA CODE JUNKIE\n"
				+ "https://www.youtube.com/watch?v=LUfzT2KcXcY&list=PL3bGLnkkGnuVBk6w7BkziScJTts0jCrrM"
				+ "\n\n"
				+ "https://www.youtube.com/watch?v=1qVEuRhx27Q&t=1229s"
				+ "\n\n"
				+ "https://www.youtube.com/watch?v=rMQrXSYHl8w&t=138s"
				+ "\n\n"
				+ "OTHERS\n"
				+ "https://stackoverflow.com/questions/35846449/explosion-effect-in-java-game"
				+ "\n\n"
				+ "https://stackoverflow.com/questions/48120739/how-to-prevent-repeated-actions-from-a-key-being-held-down"
				+ "\n\n"
				+ "https://www.tutorialspoint.com/how-to-add-custom-fonts-to-a-text-in-javafx"
				+ "\n\n"
				+ "https://stackoverflow.com/questions/30543619/how-to-use-pausetransition-method-in-javafx"
				+ "\n\n"
				+ "https://thecodinginterface.com/blog/javafx-animated-scene-transitions/#fade-in"
				+ "\n\n"
				+ "https://stackoverflow.com/questions/40253661/javafx-scrollpane-how-set-transparent-background"
				+ "\n\n"
				+ "https://stackoverflow.com/questions/17540137/javafx-scrollpane-border-and-background"
				+ "\n\n"
				+ "https://stackoverflow.com/questions/75701426/how-can-i-use-round-edges-on-a-scrollpane?noredirect=1&lq=1"
				+ "\n\n"
				+ "https://stackoverflow.com/questions/21936585/transparent-background-of-a-textarea-in-javafx-8"
				+ "\n\n"
				+ "https://stackoverflow.com/questions/30680570/javafx-button-border-and-hover"
				+ "\n\n"
				+ "https://stackoverflow.com/questions/581873/best-way-to-handle-multiple-constructors-in-java"
				+ "\n\n"
				+ "https://stackoverflow.com/questions/21493634/javafx-css-styling-of-textarea-does-not-work"
				+ "\n\n"
				+ "https://stackoverflow.com/questions/36423200/textarea-javafx-color"
				+ "\n\n"
				+ "https://stackoverflow.com/questions/48048943/javafx-8-scroll-bar-css"
				+ "\n\n"
				+ "\t\t    -_-"
		);
//		this.referencesTxtArea.setText(this.referencesTxtArea.getText().toUpperCase());
	}

	// ************************************************************************************************
	// HBox with button - grandchild of root - chld of borderPane
	private void initializeHBox() {
		this.hBox = new HBox();
		this.borderPane.setBottom(this.hBox);
		this.hBox.setAlignment(Pos.BOTTOM_RIGHT);
		this.hBox.setPadding(new Insets(0, 20, 8, 20));

		// (1) create a button that allows the user to go back to the main menu
		Button menuBtn = this.createButton("BACK");
		menuBtn.setId("menu-button"); 	// for css styling

		// listener for when the back button is clicked
		menuBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				mainMenu();
			}
		});

		// add the newly created button to our HBox
		this.hBox.getChildren().add(menuBtn);
	}

	// ************************************************************************************************
	// just some props (for design) children of root
	private void initializePropLabels() {
		this.aboutLabel = new Label("ABOUT");
		this.root.getChildren().add(this.aboutLabel);
		StackPane.setAlignment(this.aboutLabel, Pos.TOP_CENTER);
		StackPane.setMargin(this.aboutLabel, new Insets(10, 0, 0, 0));		// some margin constraints to keep the label from being too close to the top boundary
		this.aboutLabel.setId("about-prop-label"); 	// for css styling

		this.referencesLabel = new Label("REFERENCES");
		this.root.getChildren().add(this.referencesLabel);
		StackPane.setMargin(this.referencesLabel, new Insets(0, 0, 12, 0));	// some margin constraints to keep the label from being too close to the textAreas Boundary
		this.referencesLabel.setId("about-prop-label"); 	// for css styling
	}

	// ************************************************************************************************
	// when the back button is clicked
	private void mainMenu() {
		this.stop();
		this.scene.setRoot(this.menuRoot);
	}

	// ************************************************************************************************
	// getter
	public StackPane getRoot() {
		return this.root;
	}
}
