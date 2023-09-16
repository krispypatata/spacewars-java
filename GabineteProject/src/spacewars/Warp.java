package spacewars;

import gamewindow.GameStage;
import javafx.scene.image.Image;

public class Warp extends Sprite implements VisualEffect {
	private int frameCount;
	private static int maxFrameCount;
	private Image frame1, frame2, frame3, frame4, frame5, frame6, frame7, frame8, frame9;
	private boolean animationComplete;
	private boolean moveRight, moveDown;
	private int xSpeed, ySpeed, tempXSpeed, ownerWidth, ownerHeight;

	public final static String WARP_IMAGE_ONE = "assets/warp/warp1.png";
	public final static String WARP_IMAGE_TWO = "assets/warp/warp2.png";
	public final static String WARP_IMAGE_THREE = "assets/warp/warp3.png";
	public final static String WARP_IMAGE_FOUR = "assets/warp/warp4.png";
	public final static String WARP_IMAGE_FIVE = "assets/warp/warp5.png";
	public final static String WARP_IMAGE_SIX = "assets/warp/warp6.png";
	public final static String WARP_IMAGE_SEVEN = "assets/warp/warp7.png";
	public final static String WARP_IMAGE_EIGHT = "assets/warp/warp8.png";
	public final static String WARP_IMAGE_NINE = "assets/warp/warp9.png";

	// constructor for a normal spacecraft
	Warp (int xPos, int yPos, int warp_width, int warp_height, int ownerWidth, int ownerHeight, boolean getMoveRight, int xSpeed) {
		this(xPos, yPos, warp_width, warp_height, ownerWidth, ownerHeight, getMoveRight, xSpeed, false, 0);
	}

	// constructor for boss spacecraft
	Warp(int xPos, int yPos, int warpWidth, int warpHeight, int ownerWidth, int ownerHeight, boolean moveRight, int xSpeed, boolean moveDown, int ySpeed) {
		super(xPos, yPos);
		this.frame1 = new Image(Warp.WARP_IMAGE_ONE, warpWidth, warpHeight,false,false);
		this.frame2 = new Image(Warp.WARP_IMAGE_TWO, warpWidth, warpHeight,false,false);
		this.frame3 = new Image(Warp.WARP_IMAGE_THREE, warpWidth, warpHeight,false,false);
		this.frame4 = new Image(Warp.WARP_IMAGE_FOUR, warpWidth, warpHeight,false,false);
		this.frame5 = new Image(Warp.WARP_IMAGE_FIVE, warpWidth, warpHeight,false,false);
		this.frame6 = new Image(Warp.WARP_IMAGE_SIX, warpWidth, warpHeight,false,false);
		this.frame7 = new Image(Warp.WARP_IMAGE_SEVEN, warpWidth, warpHeight,false,false);
		this.frame8 = new Image(Warp.WARP_IMAGE_EIGHT, warpWidth, warpHeight,false,false);
		this.frame9 = new Image(Warp.WARP_IMAGE_NINE, warpWidth, warpHeight,false,false);

		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.moveRight = moveRight;
		this.moveDown = moveDown;
		this.ySpeed = ySpeed;
		this.ownerWidth = ownerWidth;
		this.ownerHeight = ownerHeight;
		Warp.maxFrameCount = VisualEffect.FRAME_SPEED*9;
		this.loadImage(frame1);
	}

	@Override
	public void applyEffect() {
		// pairs the movement to the movement of the spawned object
		if (Spacecraft.isMaxSpeed) this.tempXSpeed = Spacecraft.MAX_SPACECRAFT_SPEED;
		else if (Spacecraft.isMinimumSpeed) this.tempXSpeed = Spacecraft.MINIMUM_SPACECRAFT_SPEED;
		else this.tempXSpeed = this.xSpeed;

		if( (this.moveRight == true) && (this.x <= GameStage.WINDOW_WIDTH) ){
			if (this.x <= GameStage.WINDOW_WIDTH - this.ownerWidth) {
				this.x += this.tempXSpeed;
				this.frameCount += 1;
			} else {
				this.moveRight = false;
			}
		} else if ( (this.moveRight == false) && (this.x >= 0 - this.ownerWidth)) {
			if (this.x >= 0) {
				this.x -= this.tempXSpeed;
				this.frameCount += 1;
			} else {
				this.moveRight = true;
			}
		}


		// if the spawned object can move diagonally (boss spacecraft)
		if( (this.moveDown == true) && (this.y <= GameStage.WINDOW_HEIGHT) ){
			if (this.y <= GameStage.WINDOW_HEIGHT - this.ownerHeight) {
				this.y += this.ySpeed;
			} else {
				this.moveDown = false;
			}
		} else if ( (this.moveDown == false) && (this.y >= 0 - this.ownerHeight) ) {
			if (this.y >= 0) {
				this.y -= this.ySpeed;
			} else {
				this.moveDown = true;
			}
		}

		// animate the
		this.animate();
	}

	@Override
	public void animate() {
		if (this.frameCount%Warp.maxFrameCount==VisualEffect.FRAME_SPEED*0) this.loadImage(this.frame1);
		else if (this.frameCount%Warp.maxFrameCount==VisualEffect.FRAME_SPEED*1) this.loadImage(this.frame2);
		else if (this.frameCount%Warp.maxFrameCount==VisualEffect.FRAME_SPEED*2) this.loadImage(this.frame3);
		else if (this.frameCount%Warp.maxFrameCount==VisualEffect.FRAME_SPEED*3) this.loadImage(this.frame4);
		else if (this.frameCount%Warp.maxFrameCount==VisualEffect.FRAME_SPEED*4) this.loadImage(this.frame5);
		else if (this.frameCount%Warp.maxFrameCount==VisualEffect.FRAME_SPEED*5) this.loadImage(this.frame6);
		else if (this.frameCount%Warp.maxFrameCount==VisualEffect.FRAME_SPEED*6) this.loadImage(this.frame7);
		else if (this.frameCount%Warp.maxFrameCount==VisualEffect.FRAME_SPEED*7) this.loadImage(this.frame8);
		else if (this.frameCount%Warp.maxFrameCount==VisualEffect.FRAME_SPEED*8) this.loadImage(this.frame9);

		if (this.frameCount==Warp.maxFrameCount-1) this.animationComplete = true;
	}

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
