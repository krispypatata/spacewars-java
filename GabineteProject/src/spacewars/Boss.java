package spacewars;

import java.util.ArrayList;

import gamewindow.GameStage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;

public class Boss extends Spacecraft{
	private String name;
	private int health;
	private boolean moveDown;
	private boolean hasBeenHit;
	private boolean canShoot;
	private ArrayList<Bullet> bullets;

	// constants for the width and height of the boss spacecraft
	public static int BOSS_WIDTH = 100;
	public static int BOSS_HEIGHT = 60;
	public static int BOSS_HEALTH = 3000;
	public static int BOSS_DAMAGE = 50;

	public final static String CLASS_NAME = "Boss";

	// images
	public final static Image BOSS_IMAGE_ONE = new Image("assets/spacecraft/boss/boss1.png", Boss.BOSS_WIDTH, Boss.BOSS_HEIGHT,false,false);
	public final static Image BOSS_IMAGE_TWO = new Image("assets/spacecraft/boss/boss2.png", Boss.BOSS_WIDTH, Boss.BOSS_HEIGHT,false,false);
	public final static Image BOSS_IMAGE_THREE = new Image("assets/spacecraft/boss/boss3.png", Boss.BOSS_WIDTH, Boss.BOSS_HEIGHT,false,false);
	public final static Image BOSS_IMAGE_FOUR = new Image("assets/spacecraft/boss/boss4.png", Boss.BOSS_WIDTH, Boss.BOSS_HEIGHT,false,false);
	public final static Image BOSS_IMAGE_FIVE = new Image("assets/spacecraft/boss/boss5.png", Boss.BOSS_WIDTH, Boss.BOSS_HEIGHT,false,false);
	public final static Image BOSS_IMAGE_SIX = new Image("assets/spacecraft/boss/boss6.png", Boss.BOSS_WIDTH, Boss.BOSS_HEIGHT,false,false);
	public final static Image BOSS_IMAGE_SEVEN = new Image("assets/spacecraft/boss/boss7.png", Boss.BOSS_WIDTH, Boss.BOSS_HEIGHT,false,false);
	public final static Image BOSS_IMAGE_EIGHT = new Image("assets/spacecraft/boss/boss8.png", Boss.BOSS_WIDTH, Boss.BOSS_HEIGHT,false,false);
	public final static Image BOSS_IMAGE_NINE = new Image("assets/spacecraft/boss/boss9.png", Boss.BOSS_WIDTH, Boss.BOSS_HEIGHT,false,false);
	public final static Image BOSS_IMAGE_TEN = new Image("assets/spacecraft/boss/boss10.png", Boss.BOSS_WIDTH, Boss.BOSS_HEIGHT,false,false);
	public final static Image BOSS_IMAGE_ELEVEN = new Image("assets/spacecraft/boss/boss11.png", Boss.BOSS_WIDTH, Boss.BOSS_HEIGHT,false,false);
	public final static Image BOSS_IMAGE_TWELVE = new Image("assets/spacecraft/boss/boss12.png", Boss.BOSS_WIDTH, Boss.BOSS_HEIGHT,false,false);

	private final static int MAX_FRAME_SPEED = Spacecraft.FRAME_SPEED*12;

	// constructor
	Boss(String name, int x, int y) {
		super(x, y);
		this.loadImage(Boss.BOSS_IMAGE_ONE);
		this.setName(name);
		this.health = BOSS_HEALTH;
		this.damage = BOSS_DAMAGE;
		this.moveDown = false;
		this.canShoot = true;
		this.visible = false;
		this.bullets = new ArrayList<Bullet>();
	}

	// ************************************************************************************************
	// can also move vertically
	@Override
	void move() {
		super.move(); 	// move the boss spacecraft horizontally

		// also move the boss spacecraft vertically
		// so now it moves diagonally
		if( (this.moveDown == true) && (this.y <= GameStage.WINDOW_HEIGHT) ){
			if (this.y <= GameStage.WINDOW_HEIGHT - this.height) {
				this.y += this.speed;
			} else {
				this.moveDown = false;
			}
		} else if ( (this.moveDown == false) && (this.y >= 0 - this.height) ) {
			if (this.y >= 0) {
				this.y -= this.speed;
			} else {
				this.moveDown = true;
			}
		}
	}

	// this is for the animation of the boss spacecraft
	@Override
	void animate() {
		if (this.moveRight == false) {
			if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*0) this.loadImage(Boss.BOSS_IMAGE_TWELVE);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*1) this.loadImage(Boss.BOSS_IMAGE_ELEVEN);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*2) this.loadImage(Boss.BOSS_IMAGE_TEN);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*3) this.loadImage(Boss.BOSS_IMAGE_NINE);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*4) this.loadImage(Boss.BOSS_IMAGE_EIGHT);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*5) this.loadImage(Boss.BOSS_IMAGE_SEVEN);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*6) this.loadImage(Boss.BOSS_IMAGE_SIX);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*7) this.loadImage(Boss.BOSS_IMAGE_FIVE);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*8) this.loadImage(Boss.BOSS_IMAGE_FOUR);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*9) this.loadImage(Boss.BOSS_IMAGE_THREE);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*10) this.loadImage(Boss.BOSS_IMAGE_TWO);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*11) this.loadImage(Boss.BOSS_IMAGE_ONE);
		} else {
			if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*0) this.loadImage(Boss.BOSS_IMAGE_ONE);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*1) this.loadImage(Boss.BOSS_IMAGE_TWO);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*2) this.loadImage(Boss.BOSS_IMAGE_THREE);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*3) this.loadImage(Boss.BOSS_IMAGE_FOUR);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*4) this.loadImage(Boss.BOSS_IMAGE_FIVE);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*5) this.loadImage(Boss.BOSS_IMAGE_SIX);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*6) this.loadImage(Boss.BOSS_IMAGE_SEVEN);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*7) this.loadImage(Boss.BOSS_IMAGE_EIGHT);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*8) this.loadImage(Boss.BOSS_IMAGE_NINE);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*9) this.loadImage(Boss.BOSS_IMAGE_TEN);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*10) this.loadImage(Boss.BOSS_IMAGE_ELEVEN);
			else if (this.frameCount%(Boss.MAX_FRAME_SPEED) == Spacecraft.FRAME_SPEED*11) this.loadImage(Boss.BOSS_IMAGE_TWELVE);
		}
	}

	// ************************************************************************************************
	@Override
	public void render(GraphicsContext gc) {
		super.render(gc);

		ColorAdjust adjustToRed = new ColorAdjust();
		adjustToRed.setContrast(1);
		adjustToRed.setHue(-0.25);
		adjustToRed.setBrightness(0.1);
		adjustToRed.setSaturation(1);

		// color changing effect when hit by a bullet
		if (this.hasBeenHit) {
			gc.setEffect(adjustToRed);
			gc.drawImage(this.img, this.x, this.y);
			gc.setEffect(null);
		}
	}

	// ************************************************************************************************
	// enables the boss spacecraft to shoot two bullets at a time (one that moves toward the east direction and the other one in the west direction)
	void shoot(){
		//compute for the x and y initial position of the bullet
		int x = (int) (this.x + this.width/2);
		int y = (int) (this.y + this.height/2.75);

		// Instantiate new bullets and add them to the bullets arraylist of boss
		this.bullets.add(new Bullet(x,y, Boss.CLASS_NAME, false));  // bullet that moves to the left
		this.bullets.add(new Bullet(x,y, Boss.CLASS_NAME, true));   // bullet that moves to the right
    }

	// ************************************************************************************************
	// decrease the health of the enemy boss when hit by a warship's bullet
	void receiveDamage(int damage) {
		this.health -= damage;

		// change the alive status of the boss when its health already reaches 0
		if (this.health <=0) {
			this.health = 0;
			this.die();
		}

		System.out.println("Boss current health: " + this.health); // for checking
	}

	// ************************************************************************************************
	// getters and setters
	public ArrayList<Bullet> getBullets(){
		return this.bullets;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getMoveDown() {
		return this.moveDown;
	}

	public int getHealth() {
		return this.health;
	}

	public void setHasBeenHit(boolean b) {
		this.hasBeenHit = b;
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
