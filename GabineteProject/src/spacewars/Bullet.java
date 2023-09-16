package spacewars;

import gamewindow.GameStage;
import javafx.scene.image.Image;

public class Bullet extends Sprite {
	private String ownerType;
	private boolean moveRight;
	private int frameCount; 			// for sprite animation
	private int bulletSpeed;

	// for triple-bullet-shooting buff effect
	private boolean moveNorthEast;
	private boolean moveSouthEast;

	private final static int WARSHIP_BULLET_SPEED = 20;
	private final static int BOSS_BULLET_SPEED = 10;
	private final static int BULLET_WIDTH = 20;

	// images
	private final static Image BULLET_IMAGE_ONE = new Image("assets/bullet/bullet1.png",Bullet.BULLET_WIDTH,Bullet.BULLET_WIDTH,false,false);
	private final static Image BULLET_IMAGE_TWO = new Image("assets/bullet/bullet2.png",Bullet.BULLET_WIDTH,Bullet.BULLET_WIDTH,false,false);
	public final static Image BULLET_IMAGE_FLASH = new Image("assets/bullet/bullet-flash.png",Bullet.BULLET_WIDTH-8,Bullet.BULLET_WIDTH+15,false,false);

	private final static Image BOSS_BULLET_RIGHT_ONE = new Image("assets/bullet/bossbullet/rightface/bullet1.png",Bullet.BULLET_WIDTH,Bullet.BULLET_WIDTH,false,false);
	private final static Image BOSS_BULLET_RIGHT_TWO = new Image("assets/bullet/bossbullet/rightface/bullet2.png",Bullet.BULLET_WIDTH,Bullet.BULLET_WIDTH,false,false);
	public final static Image BOSS_BULLET_RIGHT_FLASH = new Image("assets/bullet/bossbullet/rightface/bullet-flash.png",Bullet.BULLET_WIDTH-8,Bullet.BULLET_WIDTH+15,false,false);

	private final static Image BOSS_BULLET_LEFT_ONE = new Image("assets/bullet/bossbullet/leftface/bullet1-flipx.png",Bullet.BULLET_WIDTH,Bullet.BULLET_WIDTH,false,false);
	private final static Image BOSS_BULLET_LEFT_TWO = new Image("assets/bullet/bossbullet/leftface/bullet2-flipx.png",Bullet.BULLET_WIDTH,Bullet.BULLET_WIDTH,false,false);
	public final static Image BOSS_BULLET_LEFT_FLASH = new Image("assets/bullet/bossbullet/leftface/bullet-flash-flipx.png",Bullet.BULLET_WIDTH-8,Bullet.BULLET_WIDTH+15,false,false);

	// having multiple constructors <https://stackoverflow.com/questions/581873/best-way-to-handle-multiple-constructors-in-java>
	// default constructor (when there's no triple-bullet-shooting-buff
	Bullet(int x, int y, String ownerClassName, boolean leftToRight){
		this(x, y, ownerClassName, leftToRight, false, false);
	}

	// constructor (for when the triple-bullet-shooting buff is in effect
	Bullet (int x, int y, String ownerClassName, boolean leftToRight, boolean moveNorthEast, boolean moveSouthEast) {
		super(x,y);

		this.ownerType = ownerClassName;
		this.moveRight = leftToRight;

		// initializing the image of the object
		if (this.ownerType.equals(Warship.CLASS_NAME)) {
			this.loadImage(Bullet.BULLET_IMAGE_ONE);
			this.bulletSpeed = Bullet.WARSHIP_BULLET_SPEED;
		}
		else if (this.ownerType.equals(Boss.CLASS_NAME)) {
			this.bulletSpeed = Bullet.BOSS_BULLET_SPEED;
			if (this.moveRight) this.loadImage(BOSS_BULLET_RIGHT_ONE);
			else this.loadImage(BOSS_BULLET_LEFT_ONE);
		}

		this.moveNorthEast = moveNorthEast;
		this.moveSouthEast = moveSouthEast;
	}

	// ************************************************************************************************
	//method that will move/change the x position of the bullet
	void move(){
		/*
		 * TODO: Change the x position of the bullet depending on the bullet speed.
		 * 					If the x position has reached the right boundary of the screen,
		 * 						set the bullet's visibility to false.
		 */
		// changing the bullet's x position
		if (moveRight) this.x += this.bulletSpeed;
		else this.x -= this.bulletSpeed;

		// triple-bullet-shooting buff effect
		if (this.moveNorthEast) this.y += this.bulletSpeed/5;
		if (this.moveSouthEast) this.y -= this.bulletSpeed/5;

		this.frameCount += 1; // for animation

		// change bullet's visibility when it reaches the end of the screen
		if (this.x >= GameStage.WINDOW_WIDTH || this.x <= 0 || this.y >= GameStage.WINDOW_HEIGHT || this.y <= 0){
			this.setVisible(false);
		}

		// change bullet image for animation
		if (this.frameCount%30 == 0) {
			if (this.ownerType.equals(Warship.CLASS_NAME)) this.loadImage(Bullet.BULLET_IMAGE_ONE);
			else if (this.ownerType.equals(Boss.CLASS_NAME)) {
				// if bullet is moving from left to right
				if (this.moveRight) this.loadImage(BOSS_BULLET_RIGHT_ONE);
				else this.loadImage(BOSS_BULLET_LEFT_ONE);
			}
		} else if (this.frameCount%30 == 15) {
			if (this.ownerType.equals(Warship.CLASS_NAME)) this.loadImage(Bullet.BULLET_IMAGE_TWO);
			else if (this.ownerType.equals(Boss.CLASS_NAME)) {
				// if bullet is moving from left to right
				if (this.moveRight) this.loadImage(BOSS_BULLET_RIGHT_TWO);
				else this.loadImage(BOSS_BULLET_LEFT_TWO);
			}
		}
	}
}
