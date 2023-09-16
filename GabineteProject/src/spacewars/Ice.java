package spacewars;

import javafx.scene.image.Image;

public class Ice extends Orb {
	private static int collected; // will track the number of ice orbs collected by the warship

	// images
	public final static Image ICE_IMAGE_ONE = new Image("assets/orb/blue/frame1.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);
	private final static Image ICE_IMAGE_TWO = new Image("assets/orb/blue/frame2.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);
	private final static Image ICE_IMAGE_THREE = new Image("assets/orb/blue/frame3.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);
	private final static Image ICE_IMAGE_FOUR = new Image("assets/orb/blue/frame4.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);
	private final static Image ICE_IMAGE_FIVE = new Image("assets/orb/blue/frame5.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);
	private final static Image ICE_IMAGE_SIX = new Image("assets/orb/blue/frame6.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);
	public final static Image ICE_EFFECT_SHIELD = new Image("assets/orb/blue/shield.png", Warship.WARSHIP_WIDTH*1.2, Warship.WARSHIP_WIDTH*1.2, false, false);

	Ice(int xPos, int yPos) {
		super(xPos, yPos);
		this.loadImage(Ice.ICE_IMAGE_ONE);

		this.frameOne = Ice.ICE_IMAGE_ONE;
		this.frameTwo = Ice.ICE_IMAGE_TWO;
		this.frameThree = Ice.ICE_IMAGE_THREE;
		this.frameFour = Ice.ICE_IMAGE_FOUR;
		this.frameFive = Ice.ICE_IMAGE_FIVE;
		this.frameSix = Ice.ICE_IMAGE_SIX;
	}

	@Override
	protected void checkCollision(Warship warship) {
		super.checkCollision(warship);
		if (this.collidesWith(warship)) {
			Spacecraft.setIsMaxSpeed(false);
			Ice.collected++;
			Orb.collected++;
		}
	}

	@Override
	protected void buffGame(Warship warship) {
		Spacecraft.setIsMinimumSpeed(true);

	}

	@Override
	protected void debuffGame(Warship warship) {
		Spacecraft.setIsMinimumSpeed(false);
	}

	// getter and setter
	public static int totalAmountCollected() {
		return Ice.collected;
	}

	public static void resetOrbCounter() {
		Ice.collected = 0;
	}
}
