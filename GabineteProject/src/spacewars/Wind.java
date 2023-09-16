package spacewars;

import javafx.scene.image.Image;

public class Wind extends Orb {
	private static int collected; // will track the number of light orbs collected by the warship

	// images
	public final static Image WIND_IMAGE_ONE = new Image("assets/orb/green/frame1.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);
	private final static Image WIND_IMAGE_TWO = new Image("assets/orb/green/frame2.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);
	private final static Image WIND_IMAGE_THREE = new Image("assets/orb/green/frame3.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);
	private final static Image WIND_IMAGE_FOUR = new Image("assets/orb/green/frame4.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);
	private final static Image WIND_IMAGE_FIVE = new Image("assets/orb/green/frame5.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);
	private final static Image WIND_IMAGE_SIX = new Image("assets/orb/green/frame6.png", Orb.ORB_WIDTH, Orb.ORB_WIDTH, false, false);

	Wind (int xPos, int yPos) {
		super(xPos, yPos);
		this.loadImage(Wind.WIND_IMAGE_ONE);

		this.frameOne = Wind.WIND_IMAGE_ONE;
		this.frameTwo = Wind.WIND_IMAGE_TWO;
		this.frameThree =Wind.WIND_IMAGE_THREE;
		this.frameFour = Wind.WIND_IMAGE_FOUR;
		this.frameFive = Wind.WIND_IMAGE_FIVE;
		this.frameSix = Wind.WIND_IMAGE_SIX;
	}

	@Override
	protected void checkCollision(Warship warship) {
		super.checkCollision(warship);
		if (this.collidesWith(warship)) {
			Wind.collected++;
			Orb.collected++;
		}
	}

	@Override
	protected void buffGame(Warship warship) {
		warship.enableTripleShooting();

	}

	@Override
	protected void debuffGame(Warship warship) {
		warship.disableTripleShooting();
	}

	// getter and setter
	public static int totalAmountCollected() {
		return Wind.collected;
	}

	public static void resetOrbCounter() {
		Wind.collected = 0;
	}
}
