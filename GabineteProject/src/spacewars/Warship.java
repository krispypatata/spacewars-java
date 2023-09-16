package spacewars;

import java.util.ArrayList;
import java.util.Random;

import gamewindow.GameStage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;

public class Warship extends Sprite{
	private String name;
	private int strength; 					// must be random from 100-150
	private boolean alive;
	private boolean invulnerable; 			// if the warship gets an light orb powerup
	private boolean hasBeenHit;
	private boolean hasCollectedFireOrb;	// for visual effect when collecting a fire orb powerup
	private boolean canShoot;
	private boolean shootTripleBullets;
	private ArrayList<Bullet> bullets;

	private final static int MINIMUM_WARSHIP_STRENGTH = 100;
	private final static int RANGE_WARSHIP_STRENGTH = 51; 	// [100, 150]
	public final static int WARSHIP_WIDTH = 50;
	public final static int SPEED = 10; // our warship can move 10 pixels at a time
	public final static String CLASS_NAME = "Warship";

	// images for our warship's sprite
	public final static Image WARSHIP_IMAGE_DEFAULT = new Image("assets/warship/warship1.png",Warship.WARSHIP_WIDTH,Warship.WARSHIP_WIDTH,false,false);
	public final static Image WARSHIP_IMAGE_DOWN = new Image("assets/warship/warship2.png",Warship.WARSHIP_WIDTH,Warship.WARSHIP_WIDTH,false,false);
	public final static Image WARSHIP_IMAGE_UP = new Image("assets/warship/warship3.png",Warship.WARSHIP_WIDTH,Warship.WARSHIP_WIDTH,false,false);

	// constructor
	Warship (String name, int x, int y){
		super(x,y);
		this.name = name;
		Random r = new Random();
		this.strength = r.nextInt(Warship.RANGE_WARSHIP_STRENGTH)+Warship.MINIMUM_WARSHIP_STRENGTH;
		this.alive = true;
		this.canShoot = true;
		this.bullets = new ArrayList<Bullet>();
		this.loadImage(Warship.WARSHIP_IMAGE_DEFAULT);
	}

	// ************************************************************************************************
	// Methods for game logics
	//method called if spacebar is pressed
	void shoot(){
		//compute for the x and y initial position of the bullet
		int x = (int) (this.x + this.width/2);
		int y = (int) (this.y + this.height/2.75);

		/*
		 * TODO: Instantiate a new bullet and add it to the bullets arraylist of ship
		 */
		this.bullets.add(new Bullet(x,y, Warship.CLASS_NAME, true));

		// for triple-bullet-shooting buff effect
		if (this.shootTripleBullets) {
			this.bullets.add(new Bullet(x,y, Warship.CLASS_NAME, true, true, false));
			this.bullets.add(new Bullet(x,y, Warship.CLASS_NAME, true, false, true));
		}
    }

	// ************************************************************************************************
	//method called if up/down/left/right arrow key is pressed.
	void move() {
		/*
		 *TODO: 		Only change the x and y position of the ship if the current x,y position
		 *				is within the gamestage width and height so that the ship won't exit the screen
		 */
		if ( (this.x + this.dx >= 0) && (this.x + this.dx <= GameStage.WINDOW_WIDTH - Warship.WARSHIP_WIDTH) ) {
			this.x += this.dx;
		}
		if ( (this.y + this.dy >=0) && (this.y + this.dy <= GameStage.WINDOW_HEIGHT - Warship.WARSHIP_WIDTH/1.25) ) {
			this.y += this.dy;
		}
	}

	// ************************************************************************************************
	@Override
	public void render(GraphicsContext gc) {
		ColorAdjust adjustToRed = new ColorAdjust();
		adjustToRed.setContrast(1);
		adjustToRed.setHue(-0.25);
		adjustToRed.setBrightness(0.1);
		adjustToRed.setSaturation(1);

		ColorAdjust adjustToGreen = new ColorAdjust();
		adjustToGreen.setContrast(0.1);
		adjustToGreen.setHue(0.5);
		adjustToGreen.setBrightness(0.1);
		adjustToGreen.setSaturation(0.2);

		if (this.hasBeenHit) gc.setEffect(adjustToRed);
		if (this.hasCollectedFireOrb) gc.setEffect(adjustToGreen);

		super.render(gc);
		gc.setEffect(null);
	}

	// ************************************************************************************************
	// decrease the warship's strength when damaged
	void receiveDamage(int damage) {
		// check if the warship is immortal/invulnerable or not
		if (this.invulnerable == false) {
			this.strength -= damage;

			// change the alive status of the warship when its strength already reaches 0
			if (this.strength <=0) {
				this.strength = 0;
				this.die();
			}
		}

		System.out.println("Warship's current strength: " + this.strength);// for checking
	}

	// ************************************************************************************************
	// apply effects on the bullet shot by the warship
	void applyShootEffect(GraphicsContext gc) {
		if (this.canShoot) gc.drawImage(Bullet.BULLET_IMAGE_FLASH, (this.x+45 ), this.y+10);
	}

	// ************************************************************************************************
	// apply shield effect on the warship
	void applyShield(GraphicsContext gc) {
		gc.drawImage(Ice.ICE_EFFECT_SHIELD, this.x - 8, this.y - 8);
	}

	// ************************************************************************************************
	// getters and setters
	public boolean isAlive(){
		if(this.alive) return true;
		return false;
	}

	public String getName(){
		return this.name;
	}

	public void die(){
    	this.alive = false;
    }

	public ArrayList<Bullet> getBullets(){
		return this.bullets;
	}

	public int getStrength() {
		return this.strength;
	}

	public void doubleStrength() {
		this.strength *= 2;
	}

	public void setInvulnerable(Boolean b) {
		this.invulnerable = b;
	}

	public void setHasBeenHit(Boolean b) {
		this.hasBeenHit = b;
	}

	public boolean isHit () {
		return this.hasBeenHit;
	}

	public void enableTripleShooting() {
		this.shootTripleBullets = true;
	}

	public void disableTripleShooting() {
		this.shootTripleBullets = false;
	}

	public boolean getHasCollectedFireOrb() {
		return this.hasCollectedFireOrb;
	}

	public void setHasCollectedFireOrb(Boolean b) {
		this.hasCollectedFireOrb = b;
	}

	public boolean getInvulnerabe() {
		return this.invulnerable;
	}

	public boolean getCanShoot() {
		return this.canShoot;
	}

	public void enableShooting() {
		this.canShoot = true;
	}

	public void disableShooting() {
		this.canShoot = false;
	}
}
