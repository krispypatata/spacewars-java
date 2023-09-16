package spacewars;

import javafx.scene.image.Image;

public class Explosion extends Sprite implements VisualEffect{
	private int frameCount;
	private static int maxFrameCount;
	private Image frame1, frame2, frame3, frame4, frame5;
	private boolean animationComplete;

	public final static String EXPLOSION_IMAGE_ONE = "assets/explosion/explosion1.png";
	public final static String EXPLOSION_IMAGE_TWO = "assets/explosion/explosion2.png";
	public final static String EXPLOSION_IMAGE_THREE = "assets/explosion/explosion3.png";
	public final static String EXPLOSION_IMAGE_FOUR = "assets/explosion/explosion4.png";
	public final static String EXPLOSION_IMAGE_FIVE = "assets/explosion/explosion5.png";

	// constructor
	public Explosion(int xPos, int yPos, int explosion_width, int explosion_height) {
		super(xPos, yPos);
		this.frame1 = new Image(Explosion.EXPLOSION_IMAGE_ONE, explosion_width, explosion_height,false,false);
		this.frame2 = new Image(Explosion.EXPLOSION_IMAGE_TWO, explosion_width, explosion_height,false,false);
		this.frame3 = new Image(Explosion.EXPLOSION_IMAGE_THREE, explosion_width, explosion_height,false,false);
		this.frame4 = new Image(Explosion.EXPLOSION_IMAGE_FOUR, explosion_width, explosion_height,false,false);
		this.frame5 = new Image(Explosion.EXPLOSION_IMAGE_FIVE, explosion_width, explosion_height,false,false);

		this.loadImage(frame1);
		Explosion.maxFrameCount = VisualEffect.FRAME_SPEED*5;
	}

	// ************************************************************************************************
	@Override
	public void applyEffect() {
		this.frameCount++;
		this.animate();
	}

	@Override
	public void animate() {
		if (this.frameCount%Explosion.maxFrameCount==VisualEffect.FRAME_SPEED*0) this.loadImage(this.frame1);
		else if (this.frameCount%Explosion.maxFrameCount==VisualEffect.FRAME_SPEED*1) this.loadImage(this.frame2);
		else if (this.frameCount%Explosion.maxFrameCount==VisualEffect.FRAME_SPEED*2) this.loadImage(this.frame3);
		else if (this.frameCount%Explosion.maxFrameCount==VisualEffect.FRAME_SPEED*3) this.loadImage(this.frame4);
		else if (this.frameCount%Explosion.maxFrameCount==VisualEffect.FRAME_SPEED*4) this.loadImage(this.frame5);

		if (this.frameCount==Explosion.maxFrameCount-1) this.animationComplete = true;
	}

	// ************************************************************************************************
	// setters and getters
	public void setframeCount(int value) {
		this.frameCount = value;
	}

	public int getframeCount() {
		return this.frameCount;
	}

	public boolean isAnimationComplete() {
		return this.animationComplete;
	}
}
