package spacewars;

import java.util.Random;

import gamewindow.GameStage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;

public class Spacecraft extends Sprite {
	public final static int MAX_SPACECRAFT_SPEED = 5;
	public final static int MINIMUM_SPACECRAFT_SPEED = 1;
	public final static int MINIMUM_SPACECRAFT_DAMAGE = 30;
	public final static int RANGE_SPACECRAFT_DAMAGE = 11; 	// [30-40]
	public final static int SPACECRAFT_WIDTH = 50;
	public final static int SPACECRAFT_SPAWN_INTERVAL = 5;
	protected boolean alive;
	protected boolean moveRight; 		// attribute that will determine if a Spacecraft will initially move to the right
	protected int randomGeneratedSpeed;	// must be random from 1-5 (true speed of the spacecraft)
	protected int speed; 				// can be manipulated (set to Maximum/Minimum)
	protected int damage; 				// must be random from 30-40

	// images
	public final static Image SPACECRAFT_IMAGE_ONE = new Image("assets/spacecraft/leftface/spacecraft1.png",Spacecraft.SPACECRAFT_WIDTH,Spacecraft.SPACECRAFT_WIDTH,false,false);
	public final static Image SPACECRAFT_IMAGE_TWO = new Image("assets/spacecraft/leftface/spacecraft2.png",Spacecraft.SPACECRAFT_WIDTH,Spacecraft.SPACECRAFT_WIDTH,false,false);
	public final static Image SPACECRAFT_IMAGE_THREE = new Image("assets/spacecraft/leftface/spacecraft3.png",Spacecraft.SPACECRAFT_WIDTH,Spacecraft.SPACECRAFT_WIDTH,false,false);
	public final static Image SPACECRAFT_IMAGE_FOUR = new Image("assets/spacecraft/leftface/spacecraft4.png",Spacecraft.SPACECRAFT_WIDTH,Spacecraft.SPACECRAFT_WIDTH,false,false);
	public final static Image SPACECRAFT_IMAGE_FIVE = new Image("assets/spacecraft/leftface/spacecraft5.png",Spacecraft.SPACECRAFT_WIDTH,Spacecraft.SPACECRAFT_WIDTH,false,false);

	// Images flipped horizontally
	public final static Image SPACECRAFT_IMAGE_ONE_FLIPX = new Image("assets/spacecraft/rightface/spacecraft1-flipx.png",Spacecraft.SPACECRAFT_WIDTH,Spacecraft.SPACECRAFT_WIDTH,false,false);
	public final static Image SPACECRAFT_IMAGE_TWO_FLIPX = new Image("assets/spacecraft/rightface/spacecraft2-flipx.png",Spacecraft.SPACECRAFT_WIDTH,Spacecraft.SPACECRAFT_WIDTH,false,false);
	public final static Image SPACECRAFT_IMAGE_THREE_FLIPX = new Image("assets/spacecraft/rightface/spacecraft3-flipx.png",Spacecraft.SPACECRAFT_WIDTH,Spacecraft.SPACECRAFT_WIDTH,false,false);
	public final static Image SPACECRAFT_IMAGE_FOUR_FLIPX = new Image("assets/spacecraft/rightface/spacecraft4-flipx.png",Spacecraft.SPACECRAFT_WIDTH,Spacecraft.SPACECRAFT_WIDTH,false,false);
	public final static Image SPACECRAFT_IMAGE_FIVE_FLIPX = new Image("assets/spacecraft/rightface/spacecraft5-flipx.png",Spacecraft.SPACECRAFT_WIDTH,Spacecraft.SPACECRAFT_WIDTH,false,false);

	// for sprite animation
	protected final static int FRAME_SPEED = 6;
	private final static int MAX_FRAME_SPEED = Spacecraft.FRAME_SPEED*5;
	protected int frameCount; 						// for sprite animation

	// for game conditions
	protected static boolean isMaxSpeed; 			// for changing spacecraft's speed to maximum
	protected static boolean isMinimumSpeed; 		// for changing spacecraft's speed to minimum

	// constructor
	Spacecraft(int x, int y){
		super(x,y);
		this.alive = true;
		this.loadImage(Spacecraft.SPACECRAFT_IMAGE_ONE);
		/*
		 *TODO: Randomize speed of Spacecraft and moveRight's initial value
		 */
		Random r = new Random();
		this.randomGeneratedSpeed = r.nextInt(Spacecraft.MAX_SPACECRAFT_SPEED) + 1;
		this.moveRight = r.nextBoolean();

		// randomize damage of Spacecraft
		this.damage = r.nextInt(Spacecraft.RANGE_SPACECRAFT_DAMAGE) + Spacecraft.MINIMUM_SPACECRAFT_DAMAGE;

	}

	// ************************************************************************************************
	//method that changes the x position of the Spacecraft
	void move(){
		if (Spacecraft.isMaxSpeed) this.speed = Spacecraft.MAX_SPACECRAFT_SPEED;
		else if (Spacecraft.isMinimumSpeed) this.speed = Spacecraft.MINIMUM_SPACECRAFT_SPEED;
		else this.speed = this.randomGeneratedSpeed;
		/*
		 * TODO: 				If moveRight is true and if the Spacecraft hasn't reached the right boundary yet,
		 *    						move the Spacecraft to the right by changing the x position of the Spacecraft depending on its speed
		 *    					else if it has reached the boundary, change the moveRight value / move to the left
		 * 					 Else, if moveRight is false and if the Spacecraft hasn't reached the left boundary yet,
		 * 	 						move the Spacecraft to the left by changing the x position of the Spacecraft depending on its speed.
		 * 						else if it has reached the boundary, change the moveRight value / move to the right
		 */

		if( (this.moveRight == true) && (this.x <= GameStage.WINDOW_WIDTH) ){
			if (this.x <= GameStage.WINDOW_WIDTH - this.width) {
				this.x += this.speed;
				this.frameCount += 1;
			} else {
				this.moveRight = false;
			}
		} else if ( (this.moveRight == false) && (this.x >= 0 - this.width)) {
			if (this.x >= 0) {
				this.x -= this.speed;
				this.frameCount += 1;
			} else {
				this.moveRight = true;
			}
		}

		// call the method for animating the spacecraft's image
		this.animate();
	}

	// this is for the animation of the spacecraft's image
	void animate() {
		if (this.moveRight == false) {
			if (this.frameCount%(Spacecraft.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*0) this.loadImage(SPACECRAFT_IMAGE_ONE);
			else if (this.frameCount%(Spacecraft.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*1) this.loadImage(SPACECRAFT_IMAGE_TWO);
			else if (this.frameCount%(Spacecraft.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*2) this.loadImage(SPACECRAFT_IMAGE_THREE);
			else if (this.frameCount%(Spacecraft.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*3) this.loadImage(SPACECRAFT_IMAGE_FOUR);
			else if (this.frameCount%(Spacecraft.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*4) this.loadImage(SPACECRAFT_IMAGE_FIVE);
		} else {
			if (this.frameCount%(Spacecraft.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*0) this.loadImage(SPACECRAFT_IMAGE_ONE_FLIPX);
			else if (this.frameCount%(Spacecraft.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*1) this.loadImage(SPACECRAFT_IMAGE_TWO_FLIPX);
			else if (this.frameCount%(Spacecraft.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*2) this.loadImage(SPACECRAFT_IMAGE_THREE_FLIPX);
			else if (this.frameCount%(Spacecraft.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*3) this.loadImage(SPACECRAFT_IMAGE_FOUR_FLIPX);
			else if (this.frameCount%(Spacecraft.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*4) this.loadImage(SPACECRAFT_IMAGE_FIVE_FLIPX);
		}
	}

	// ************************************************************************************************
	@Override
	public void render(GraphicsContext gc) {
		ColorAdjust adjustToBlue = new ColorAdjust();
		adjustToBlue.setContrast(0.1);
		adjustToBlue.setHue(1.0);
		adjustToBlue.setBrightness(0.1);
		adjustToBlue.setSaturation(0.2);

		ColorAdjust adjustToYellow = new ColorAdjust();
		adjustToYellow.setContrast(0.5);
		adjustToYellow.setHue(0.2);
		adjustToYellow.setBrightness(0.1);
		adjustToYellow.setSaturation(0.2);

		// change the color of the spacecraft
		if (Spacecraft.isMinimumSpeed) gc.setEffect(adjustToBlue);
		if (Spacecraft.isMaxSpeed) gc.setEffect(adjustToYellow);

		// render the spacecraft
		super.render(gc);

		// disable applied effects
		gc.setEffect(null);
	}

	// ************************************************************************************************
	// setters and getters
	public boolean isAlive() {
		return this.alive;
	}

	public int getDamage() {
		return this.damage;
	}

	public void die() {
		this.alive = false;
	}

	public boolean getMoveRight() {
		return this.moveRight;
	}

	public int getSpeed() {
		return this.randomGeneratedSpeed;
	}

	public static void setIsMaxSpeed(Boolean b) {
		Spacecraft.isMaxSpeed = b;
	}

	public static void setIsMinimumSpeed(Boolean b) {
		Spacecraft.isMinimumSpeed = b;
	}

	public static boolean getIsMaxSpeed() {
		return Spacecraft.isMaxSpeed;
	}

	public static boolean getIsMinimumSpeed() {
		return Spacecraft.isMinimumSpeed;
	}

	public static void resetSpeed() {
		Spacecraft.isMaxSpeed = false;
		Spacecraft.isMinimumSpeed = false;
	}
}
