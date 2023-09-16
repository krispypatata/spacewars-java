package spacewars;

import javafx.scene.image.Image;

public class Light extends Orb {
	private static int collected; // will track the number of light orbs collected by the warship

	// images
	public final static Image LIGHT_IMAGE_ONE = new Image("assets/orb/yellow/frame1.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);
	private final static Image LIGHT_IMAGE_TWO = new Image("assets/orb/yellow/frame2.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);
	private final static Image LIGHT_IMAGE_THREE = new Image("assets/orb/yellow/frame3.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);
	private final static Image LIGHT_IMAGE_FOUR = new Image("assets/orb/yellow/frame4.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);
	private final static Image LIGHT_IMAGE_FIVE = new Image("assets/orb/yellow/frame5.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);
	private final static Image LIGHT_IMAGE_SIX = new Image("assets/orb/yellow/frame6.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);

	Light(int xPos, int yPos) {
		super(xPos, yPos);
		this.loadImage(Light.LIGHT_IMAGE_ONE);

		this.frameOne = Light.LIGHT_IMAGE_ONE;
		this.frameTwo = Light.LIGHT_IMAGE_TWO;
		this.frameThree = Light.LIGHT_IMAGE_THREE;
		this.frameFour = Light.LIGHT_IMAGE_FOUR;
		this.frameFive = Light.LIGHT_IMAGE_FIVE;
		this.frameSix = Light.LIGHT_IMAGE_SIX;
	}

	@Override
	protected void checkCollision(Warship warship) {
		super.checkCollision(warship);
		if (this.collidesWith(warship)) {
			Light.collected++;
			Orb.collected++;
		}
	}

	@Override
	protected void buffGame(Warship warship) {
		warship.setInvulnerable(true);
	}

	@Override
	protected void debuffGame(Warship warship) {
		warship.setInvulnerable(false);

	}

	// getter and setter
	public static int totalAmountCollected() {
		return Light.collected;
	}

	public static void resetOrbCounter() {
		Light.collected = 0;
	}
}
