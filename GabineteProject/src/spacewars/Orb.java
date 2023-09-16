package spacewars;

import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.util.Duration;
import java.util.Random;

import gamewindow.GameStage;

public abstract class Orb extends Sprite {
	protected static int collected; // will track the total number of orbs collected by the warship
	protected int frameCount;
	protected Image frameOne;
	protected Image frameTwo;
	protected Image frameThree;
	protected Image frameFour;
	protected Image frameFive;
	protected Image frameSix;

	private final static int FRAME_SPEED = 7;
	private final static int MAX_FRAME_SPEED = FRAME_SPEED*6;
	public final static int ORB_EFFECT_DURATION = 5;
	public final static int SPAWN_INTERVAL = 10;
	public final static int ORB_WIDTH = 30;
	public final static int TOTAL_ORB_TYPES = 4;

	Orb (int xPos, int yPos) {
		super(xPos, yPos);
	}

	// ************************************************************************************************
	// the orb will only appear for 5 seconds
	// will spawn on random locations every 10 seconds
	void spawn() {
		Random r = new Random();
		this.x = r.nextInt(GameStage.WINDOW_WIDTH);
		this.y = r.nextInt(GameStage.WINDOW_HEIGHT);
	}

	// ************************************************************************************************
	void displayAnimation(Warship warship) {

		this.frameCount +=1;

		if (this.frameCount%Orb.MAX_FRAME_SPEED==Orb.FRAME_SPEED*0) this.loadImage(frameOne);
		else if (this.frameCount%Orb.MAX_FRAME_SPEED==Orb.FRAME_SPEED*1) this.loadImage(frameTwo);
		else if (this.frameCount%Orb.MAX_FRAME_SPEED==Orb.FRAME_SPEED*2) this.loadImage(frameThree);
		else if (this.frameCount%Orb.MAX_FRAME_SPEED==Orb.FRAME_SPEED*3) this.loadImage(frameFour);
		else if (this.frameCount%Orb.MAX_FRAME_SPEED==Orb.FRAME_SPEED*4) this.loadImage(frameFive);
		else if (this.frameCount%Orb.MAX_FRAME_SPEED==Orb.FRAME_SPEED*5) this.loadImage(frameSix);

		this.checkCollision(warship);

	}

	// ************************************************************************************************
	// check if the orb was collected by the warship
	protected void checkCollision(Warship warship) {
		if (this.collidesWith(warship)) {
			this.visible = false; 		// vanish the orb if it was collected
			this.buffGame(warship); 	// apply appropriate buff to the game (depending on the orb type)

			// <https://stackoverflow.com/questions/30543619/how-to-use-pausetransition-method-in-javafx>
			PauseTransition buff = new PauseTransition(Duration.seconds(Orb.ORB_EFFECT_DURATION));
			buff.setOnFinished(event -> this.debuffGame(warship));
			buff.play();
		}
	}

	// ************************************************************************************************
	// abstract methods (to be implemented by the subclasses)
	protected abstract void buffGame(Warship warship);
	protected abstract void debuffGame(Warship warship);

	// ************************************************************************************************
	// getter and setter
	// getter for the collected class attribute
	public static int totalAmountCollected() {
		return Orb.collected;
	}

	// method to reset the values of static variable/s
	public static void resetOrbCounters() {
		Orb.collected = 0;
		Fire.resetOrbCounter();
		Ice.resetOrbCounter();
		Light.resetOrbCounter();
		Wind.resetOrbCounter();
	}

}
