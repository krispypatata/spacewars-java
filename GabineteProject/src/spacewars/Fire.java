package spacewars;

import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class Fire extends Orb {
	private static int collected; // will track the number of fire orbs collected by the warship

	// images
	public final static Image FIRE_IMAGE_ONE = new Image("assets/orb/red/frame1.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);
	public final static Image FIRE_IMAGE_TWO = new Image("assets/orb/red/frame2.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);
	public final static Image FIRE_IMAGE_THREE = new Image("assets/orb/red/frame3.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);
	public final static Image FIRE_IMAGE_FOUR = new Image("assets/orb/red/frame4.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);
	public final static Image FIRE_IMAGE_FIVE = new Image("assets/orb/red/frame5.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);
	public final static Image FIRE_IMAGE_SIX = new Image("assets/orb/red/frame6.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);

	Fire(int xPos, int yPos) {
		super(xPos, yPos);
		this.loadImage(Fire.FIRE_IMAGE_ONE);

		this.frameOne = Fire.FIRE_IMAGE_ONE;
		this.frameTwo = Fire.FIRE_IMAGE_TWO;
		this.frameThree = Fire.FIRE_IMAGE_THREE;
		this.frameFour = Fire.FIRE_IMAGE_FOUR;
		this.frameFive = Fire.FIRE_IMAGE_FIVE;
		this.frameSix = Fire.FIRE_IMAGE_SIX;
	}

	@Override
	protected void checkCollision(Warship warship) {
		super.checkCollision(warship);
		if (this.collidesWith(warship)) {
			// color change effect on the warship indicating that it has been hit by a spacecraft
			warship.setHasCollectedFireOrb(true);
			Fire.collected++;
			Orb.collected++;
			PauseTransition collectedFireOrb = new PauseTransition(Duration.seconds(0.5));
			collectedFireOrb.setOnFinished(event -> warship.setHasCollectedFireOrb(false));
			collectedFireOrb.play();
		}
	}

	@Override
	protected void buffGame(Warship warship) {
		// buff: doubles the current strength of the warship
		warship.doubleStrength();

	}

	@Override
	protected void debuffGame(Warship warship) {
		// Do nothing
	}

	// getter and setter
	public static int totalAmountCollected() {
		return Fire.collected;
	}

	public static void resetOrbCounter() {
		Fire.collected = 0;
	}
}
