package spacewars;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import gamewindow.GameOverStage;
import gamewindow.GameScene;
import gamewindow.GameStage;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * The GameTimer is a subclass of the AnimationTimer class. It must override the handle method.
 */

public class GameTimer extends GameScene {
	private Stage theStage;
	private Scene theScene;
	private GraphicsContext gc;
	private Warship myWarship;
	private Boss boss;
	private Orb orb;
	private ArrayList<Spacecraft> spacecrafts;
	private ArrayList<Explosion> explosionEffects; 	// visual effects
	private ArrayList<Warp> warpEffects;			// visual effects

	private final static int START_NUM_SPACECRAFTS = 7;
	private final static int SPAWN_NUM_SPACECRAFTS = 3;
	private final static int SPEED_TO_MAXIMUM_INTERVAL = 15;
	private final static int SPEED_TO_MAXIMUM_DURATION = 3;

	// for animation handle
	private long gameStartTime; 						// stores the time the gameTimer has been started
	private boolean gameIsOngoing; 				// for spawning spacecrafts
	private boolean canSpawnSpacecrafts =  true;
	private boolean canSpawnOrb =  true;
	private boolean canSpawnBoss = true;
	private boolean isIncreaseSpeedExecuted;
	private boolean isResetSpeedExecuted;
	private boolean isGameOver;

	// flags for more seamless movement of our warship every time an arrow key or a spacebar key is being pressed by the user
	private boolean myWarshipGoLeft;
	private boolean myWarshipGoRight;
	private boolean myWarshipGoUp;
	private boolean myWarshipGoDown;
	private boolean myWarshipGoShoot;

	// background source image assets
	private Image bgBack = new Image("assets/background/bg-back.png",GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,false,false);
	private Image bgPlanet = new Image("assets/background/bg-planet.png",GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,false,false);
	private Image bgStars = new Image("assets/background/bg-stars.png",GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,false,false);

	// for moving background
	private final static double BACKGROUND_SPEED = 0.25;
	private double bgBackX, bgPlanetX, bgStarsX;

	// for Game status
	private int spacecraftsKilled; 				// counter for spacecrafts killed
	private boolean isBossKilled;
	private boolean isGameWon;

	// constructor
	public GameTimer(GraphicsContext gc, Stage stage){
		this.theStage = stage;
		this.theScene = stage.getScene();
		this.gc = gc;

		this.gameStartTime = System.nanoTime();

		// warship should spawn at the leftmost side of the screen (x=0)
		// warship's y position is random
		Random r = new Random();
		int tempY = r.nextInt(GameStage.WINDOW_HEIGHT-Warship.WARSHIP_WIDTH);
		this.myWarship = new Warship("Mordecai",0,tempY);

		//instantiate the ArrayList of Spacecraft
		this.spacecrafts = new ArrayList<Spacecraft>();

		//instantiate the ArrayList of Explosion effects
		this.explosionEffects = new ArrayList<Explosion>();

		//instantiate the ArrayList of Warp/spawn effects
		this.warpEffects = new ArrayList<Warp>();

		// spawn 7 spacecrafts at the start of the game
		this.spawnspacecrafts();

		// instantiate a boss object
		this.spawnBoss();

		//call method to handle mouse click event
		this.handleKeyPressEvent();
	}


	// ***********************************************************************************************************************
	// Animation
	@Override
	public void handle(long currentNanoTime) {
		// for tracking the elapsed time since the game has been started
		long currentSec = TimeUnit.NANOSECONDS.toSeconds(currentNanoTime);
		long startSec = TimeUnit.NANOSECONDS.toSeconds(this.gameStartTime);
		int elapsedTime = (int) (currentSec - startSec);

		// display the moving background image
		this.redrawBackgroundImage();

		// move the warship if it is still alive
		if (this.myWarship.isAlive()) this.myWarship.move();

		// TODO: Call the moveBullets and movespacecrafts methods
		if (this.myWarship.isAlive() && elapsedTime!= 60) this.moveBullets();
		this.movespacecrafts();

		// render orbs
		if (this.myWarship.isAlive() && elapsedTime!= 60) this.renderOrbs();

		//render the warship if it is still alive
		if (this.myWarship.isAlive()) this.myWarship.render(this.gc);

		// if the warship is immortal, give it a shield
		if (this.myWarship.getInvulnerabe()) this.myWarship.applyShield(this.gc);

		 // TODO: Call the renderspacecrafts and renderBullets methods
		if (this.myWarship.isAlive() && elapsedTime!= 60) {
			this.renderspacecrafts();
			this.renderBullets();
		}

		// bullet's shooting effect
		if (this.myWarshipGoShoot) this.myWarship.applyShootEffect(this.gc);

		// render explosions
		this.renderExplosionEffects();


		// allows our warship to shoot bullets when the spacebar key is pressed (even when on hold)
        if (this.myWarshipGoShoot) {
        	// delay effect for bullets
			PauseTransition shootingDelay = new PauseTransition(Duration.seconds(0.2));
			shootingDelay.setOnFinished(event -> this.myWarship.enableShooting());

			// this.myWarship.shoot(); // uncomment for rapid shooting
        	if (this.myWarship.getCanShoot()) {
        		this.myWarship.shoot();

        		// prevent the warship from shooting bullets like a machine gun
        		this.myWarship.disableShooting();
        		shootingDelay.play();
        	}
        }

        // allows our boss spacecraft to shoot bullets right after it spawns
        if (this.boss.isAlive() && this.boss.isVisible()) {

        	// delay effect for bullets
			PauseTransition shootingDelay = new PauseTransition(Duration.seconds(1.0));
			shootingDelay.setOnFinished(event -> this.boss.enableShooting());

			// this.boss.shoot(); // uncomment for rapid shooting
        	if (this.boss.getCanShoot()) {
        		this.boss.shoot();

        		// prevent the boss from shooting bullets like a machine gun
        		this.boss.disableShooting();
        		shootingDelay.play();
        	}
        }

		// update Game Status Bar
		this.updateStatusBar(elapsedTime);

		// Game Logics
		// (1) spawn 3 spacecrafts every 5 seconds
		if (elapsedTime>4 && elapsedTime%Spacecraft.SPACECRAFT_SPAWN_INTERVAL == 0 && this.canSpawnSpacecrafts && elapsedTime!=60) {
			this.spawnspacecrafts();
			this.canSpawnSpacecrafts = false;
		}
		// to prevent iteration of calling the spawnspacecrafts function in the previous block of code
		if (elapsedTime>4 && elapsedTime%Spacecraft.SPACECRAFT_SPAWN_INTERVAL == 1 && this.canSpawnSpacecrafts == false) {
			this.canSpawnSpacecrafts = true;
		}


		// (2) increase spacecrafts' speed to maximum every 15 seconds for 3 seconds
		if (elapsedTime > 14 && elapsedTime%GameTimer.SPEED_TO_MAXIMUM_INTERVAL == 0 && (this.isIncreaseSpeedExecuted == false) ) {
			this.increaseSpacecraftSpeed();
			this.isIncreaseSpeedExecuted = true;

		}
		// reset Spacecrafts' speed after 3 seconds
		if (elapsedTime>14 && elapsedTime%GameTimer.SPEED_TO_MAXIMUM_INTERVAL == GameTimer.SPEED_TO_MAXIMUM_DURATION && (this.isResetSpeedExecuted == false) ) {
			this.resetSpacecraftSpeed();
			this.isResetSpeedExecuted = true;
		}

		// to prevent iteration of calling the functions in the previous blocks of code
		if (elapsedTime>14 && elapsedTime%GameTimer.SPEED_TO_MAXIMUM_INTERVAL == (GameTimer.SPEED_TO_MAXIMUM_DURATION+1) && this.isIncreaseSpeedExecuted == true && this.isResetSpeedExecuted == true ) {
			this.isIncreaseSpeedExecuted = false;
			this.isResetSpeedExecuted = false;
		}


		// (3) spawn orbs (powerups)
		if (elapsedTime>9 && elapsedTime%Orb.SPAWN_INTERVAL == 0 && this.canSpawnOrb && elapsedTime!=60) {
			this.spawnOrb();
			this.canSpawnOrb = false;
		}

		// vanish orb after 5 seconds
		if (elapsedTime>9 && elapsedTime%Orb.SPAWN_INTERVAL == Orb.ORB_EFFECT_DURATION && this.canSpawnOrb == false) {
			this.orb = null;
			this.canSpawnOrb = true;
		}

		// (4) Spawn enemy boss when the game time reaches 30 seconds
		if (elapsedTime>=30 && this.canSpawnBoss) {
			// adds a warp/spawn effect
			if (this.boss.isVisible() == false) this.warpEffects.add(new Warp(this.boss.getX()-Boss.BOSS_WIDTH*5/2, this.boss.getY()-Boss.BOSS_HEIGHT*1/2, Boss.BOSS_WIDTH*6, Boss.BOSS_HEIGHT*2, Boss.BOSS_WIDTH, Boss.BOSS_HEIGHT, this.boss.getMoveRight(), this.boss.getSpeed(), this.boss.getMoveDown(), this.boss.getSpeed()));
			// move and render the enemy boss spacecraft
	        this.moveBoss();
	        this.boss.setVisible(true); 		// for shooting bullets
	        if (this.boss.isAlive()) this.boss.render(gc);
		}

		// warp/spawn effects of spacecrafts (inclusive of the boss object)
		if (this.myWarship.isAlive() && elapsedTime != 60) this.renderWarpEffects();


		// (5) if the warship isn't alive anymore then, stop the program
        if(this.myWarship.isAlive()==false || elapsedTime == 60) {
        	// force explode all objects in the game
        	if (this.isGameOver == false) {
        		this.explodeAll();
        		this.isGameOver = true;
        	}

        	if (this.explosionEffects.isEmpty()) this.displayGameOverScene();

        	// if the warship survives for 60 seconds
        	if (elapsedTime == 60) this.isGameWon = true;
        }

	} // end of the animation handle


	// ***********************************************************************************************************************
	// Background and Game Status Bar
	// moving background image effect
    private void redrawBackgroundImage() {
		// clears the canvas
		this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);

		// x dispositions for background components
        this.bgBackX += GameTimer.BACKGROUND_SPEED;
        this.bgStarsX += GameTimer.BACKGROUND_SPEED;
        this.bgPlanetX += GameTimer.BACKGROUND_SPEED*4;

        // moving back image
        this.gc.drawImage(this.bgBack, this.bgBack.getWidth()-this.bgBackX, 0);
        this.gc.drawImage(this.bgBack, -this.bgBackX, 0);

        // if entire image goes out of the screen
        if(this.bgBackX>=GameStage.WINDOW_WIDTH) this.bgBackX = GameStage.WINDOW_WIDTH-this.bgBack.getWidth();


        // moving stars
        this.gc.drawImage(this.bgStars, this.bgStars.getWidth()-this.bgStarsX, 0);
        this.gc.drawImage(this.bgStars, -this.bgStarsX, 0);

        if(this.bgStarsX>=GameStage.WINDOW_WIDTH) this.bgStarsX = GameStage.WINDOW_WIDTH-this.bgStars.getWidth();

        // moving planets
        this.gc.drawImage(bgPlanet, -bgPlanetX, 0);
        this.gc.drawImage(bgPlanet, ( GameStage.WINDOW_WIDTH+this.bgPlanet.getWidth() )-bgPlanetX, 0);

        if(this.bgPlanetX>= (GameStage.WINDOW_WIDTH+this.bgPlanet.getWidth()) ) this.bgPlanetX = GameStage.WINDOW_WIDTH-this.bgPlanet.getWidth();


		// flipping image <https://stackoverflow.com/questions/40582099/flip-javafx-image-horizontally-or-vertically>
		// (javafx.scene.image.Image, sourceX,sourceY, sourceWidth,sourceHeight, outputX,outputY,outputWidth,outputHeight);
		this.gc.drawImage(bgPlanet, GameStage.WINDOW_WIDTH-bgPlanetX, bgPlanet.getHeight(), bgPlanet.getWidth(),-bgPlanet.getHeight());

    }


    private void updateStatusBar(int elapsedTime) {
    	// Font size
    	double fontSize = 20;

    	// loading a font from local source <https://www.tutorialspoint.com/how-to-add-custom-fonts-to-a-text-in-javafx>
    	Font pixelFont = Font.loadFont(GameStage.PIXEL_FOUNT_SRC, fontSize);
    	this.gc.setFont(pixelFont); 	// setting the font style to be used in drawing text
    	this.gc.setFill(Color.SKYBLUE); // font color

    	// strength status
    	double strengthStatusX = (GameStage.WINDOW_WIDTH/8)*6.5;
    	this.gc.fillText("STRENGTH: ", strengthStatusX, fontSize+(fontSize/2));

    	// change color to green when collected fire orb
    	if (this.myWarship.getHasCollectedFireOrb()) this.gc.setFill(Color.LAWNGREEN);
    	else this.gc.setFill(Color.SKYBLUE);
    	// if less than 40 stregth, change color to red
    	if (this.myWarship.getStrength()<=40) this.gc.setFill(Color.RED);
    	// display strength
    	this.gc.fillText("          " + this.myWarship.getStrength(), strengthStatusX, fontSize+(fontSize/2));
    	this.gc.setFill(Color.SKYBLUE); // resets font color

    	double warshipStatusSize = fontSize+(fontSize/2);
    	this.gc.drawImage(Warship.WARSHIP_IMAGE_DEFAULT, strengthStatusX - warshipStatusSize-(fontSize/2), fontSize/2, warshipStatusSize, warshipStatusSize);

    	// score status
    	double scoreStatusX = (GameStage.WINDOW_WIDTH/8)*4.8;
    	this.gc.fillText("KILLED: " + this.spacecraftsKilled, scoreStatusX, fontSize+(fontSize/2));

    	double spacecraftStatusSize = fontSize+(fontSize/4);
    	this.gc.drawImage(Spacecraft.SPACECRAFT_IMAGE_ONE, scoreStatusX - (fontSize/2), fontSize/2, -spacecraftStatusSize, spacecraftStatusSize);

    	// collected orbs status
    	double orbStatusX = (GameStage.WINDOW_WIDTH/8)*1.8;
    	double orbStatusSize = fontSize;

    	// fire orb
    	this.gc.drawImage(Fire.FIRE_IMAGE_ONE, orbStatusX , fontSize/1.5, orbStatusSize, orbStatusSize);
    	this.gc.fillText("" + Fire.totalAmountCollected(), orbStatusX + orbStatusSize*1.5, fontSize+(fontSize/2));
    	// ice orb
    	this.gc.drawImage(Ice.ICE_IMAGE_ONE, orbStatusX + orbStatusSize*3, fontSize/1.5, orbStatusSize, orbStatusSize);
    	this.gc.fillText("" + Ice.totalAmountCollected(), orbStatusX + orbStatusSize*3 + orbStatusSize*1.5, fontSize+(fontSize/2));
    	// light orb
    	this.gc.drawImage(Light.LIGHT_IMAGE_ONE, orbStatusX + orbStatusSize*6, fontSize/1.5, orbStatusSize, orbStatusSize);
    	this.gc.fillText("" + Light.totalAmountCollected(), orbStatusX + orbStatusSize*6 + orbStatusSize*1.5, fontSize+(fontSize/2));
    	// wind orb
    	this.gc.drawImage(Wind.WIND_IMAGE_ONE, orbStatusX + orbStatusSize*9, fontSize/1.5, orbStatusSize, orbStatusSize);
    	this.gc.fillText("" + Wind.totalAmountCollected(), orbStatusX + orbStatusSize*9 + orbStatusSize*1.5, fontSize+(fontSize/2));

    	// timer status
    	double timerStatusX = (GameStage.WINDOW_WIDTH/8)*0.5;
    	this.gc.fillText("TIME: ", timerStatusX, fontSize+(fontSize/2));
    	if (60-elapsedTime <= 10) this.gc.setFill(Color.RED); // font color
    	this.gc.fillText("      " + (60-elapsedTime), timerStatusX, fontSize+(fontSize/2));

    	double timertatusSize = fontSize;
    	this.gc.drawImage(new Image("assets/props/timer.png"), timerStatusX - timertatusSize - (fontSize/2), fontSize/1.5, timertatusSize, timertatusSize);

    	// display the health of the enemy boss spacecraft
    	if (this.boss.isAlive() && this.boss.isVisible()) {
    		this.gc.setFill(Color.SKYBLUE); // font color
    		double bossStatusX = (GameStage.WINDOW_WIDTH/8)*6.0;
    		this.gc.fillText("BOSS HEALTH: ", bossStatusX, GameStage.WINDOW_HEIGHT - (fontSize/2)  );

    		if (this.boss.getHealth()>=500) this.gc.setFill(Color.LAWNGREEN); 	// font color
    		else this.gc.setFill(Color.RED);
    		this.gc.fillText("              " + this.boss.getHealth(), bossStatusX, GameStage.WINDOW_HEIGHT - (fontSize/2)  );
    	}
    }

    // ***********************************************************************************************************************
    // Render Objects on Canvas
	//method that will render/draw the spacecrafts to the canvas
	private void renderspacecrafts() {
		for (Spacecraft f : this.spacecrafts){
			f.render(this.gc);
		}
	}

	//method that will render/draw the bullets to the canvas
	private void renderBullets() {
		/*
		 *TODO: Loop through the bullets arraylist of myWarship
		 *				and render each bullet to the canvas
		 */
        for (Bullet b : this.myWarship.getBullets())
        	b.render(this.gc);

       // Loop through the bullets arraylist of boss spacecraft and render each bullet to the canvas
        for (Bullet b : this.boss.getBullets())
        	b.render(this.gc);
	}

	// method that will render orbs
	private void renderOrbs() {
		if (this.orb!=null && this.orb.isVisible()) {
			this.orb.displayAnimation(this.myWarship);;
			this.orb.render(this.gc);
		}
	}

	// method that will render explosions
	private void renderExplosionEffects() {
		for(int i = 0; i < this.explosionEffects.size(); i++) {
			Explosion explosion = this.explosionEffects.get(i);

			if (explosion.isAnimationComplete()) this.explosionEffects.remove(explosion);
			else {
				explosion.applyEffect();
				explosion.render(this.gc);
			}
		}
	}

	// method that will render warp/spawn effects
	private void renderWarpEffects() {
		for(int i = 0; i < this.warpEffects.size(); i++) {
			Warp warp = this.warpEffects.get(i);

			if (warp.isAnimationComplete()) this.warpEffects.remove(warp);
			else {
				warp.applyEffect();
				warp.render(this.gc);
			}
		}
	}

    // ***********************************************************************************************************************
	// Spawn Objects
	//method that will spawn/instantiate spacecrafts at a random x,y location (must be at the right side of the screen)
	private void spawnspacecrafts(){
		Random r = new Random();
		int numSpacecrafts;
		// spawn 7 spacecrafts if the game has just been started, else spawn 3 every 5 seconds
		if (this.gameIsOngoing) numSpacecrafts = GameTimer.SPAWN_NUM_SPACECRAFTS;
		else {
			numSpacecrafts = GameTimer.START_NUM_SPACECRAFTS;
			this.gameIsOngoing = true;
		}

		for(int i=0;i<numSpacecrafts;i++){
			int x = r.nextInt(GameStage.WINDOW_WIDTH/2 - Spacecraft.SPACECRAFT_WIDTH) + GameStage.WINDOW_WIDTH/2; 			// appears on the right side of the screen
			int y = r.nextInt( GameStage.WINDOW_HEIGHT - (Spacecraft.SPACECRAFT_WIDTH) ); 	// spawn only inside the screen

			/*
			 *TODO: Add a new object Spacecraft to the spacecrafts arraylist
			 */
			Spacecraft s = new Spacecraft(x, y);
			this.spacecrafts.add(s);

			// for warp/spawn effect
			if (numSpacecrafts == GameTimer.SPAWN_NUM_SPACECRAFTS)
			this.warpEffects.add(new Warp(x-Spacecraft.SPACECRAFT_WIDTH*5/2, y-Spacecraft.SPACECRAFT_WIDTH*1/2, Spacecraft.SPACECRAFT_WIDTH*6, Spacecraft.SPACECRAFT_WIDTH*2, Spacecraft.SPACECRAFT_WIDTH, Spacecraft.SPACECRAFT_WIDTH, s.getMoveRight(), s.getSpeed()));
		}
	}

	// method for spawning the boss spacecraft
	private void spawnBoss() {
		// will spawn at random (x,y) coordinates within the right half portion of the screen/game scene
		Random r = new Random();
		int x = r.nextInt(GameStage.WINDOW_WIDTH/2) + GameStage.WINDOW_WIDTH/2 - Boss.BOSS_WIDTH; 			// appears at the right side of the screen
		int y = r.nextInt( GameStage.WINDOW_HEIGHT - (Boss.BOSS_HEIGHT) ); 			// appears only within the screen
		this.boss = new Boss("Star Destroyer", x, y);
	}


	// method that will spawn orb randomly at the left side of the screen
	private void spawnOrb() {
		Random r = new Random();
		int x = r.nextInt(GameStage.WINDOW_WIDTH/2 - Orb.ORB_WIDTH);	// spawn only on the left side of the screen
		int y = r.nextInt(GameStage.WINDOW_HEIGHT - Orb.ORB_WIDTH); 	// spawn only inside the screen

		// generating a random orb
		int type = r.nextInt(Orb.TOTAL_ORB_TYPES);
		switch(type) {
			case 0: this.orb = new Fire(x, y);
			break;

			case 1: this.orb = new Light(x, y);
			break;

			case 2: this.orb = new Ice(x, y);
			break;

			case 3: this.orb = new Wind(x, y);
			break;
		}

	}


    // ***********************************************************************************************************************
	// Move Objects
	//method that will move the bullets shot by the warship
	private void moveBullets(){
		// (1) Warship's bullets
		// create a local arraylist of Bullets for the bullets 'shot' by the ship
		ArrayList<Bullet> bListWarship = this.myWarship.getBullets();

		//Loop through the bullet list and check whether a bullet is still visible.
		for(int i = 0; i < bListWarship.size(); i++){
			Bullet b = bListWarship.get(i);
			/*
			 * TODO:  If a bullet is visible, move the bullet, else, remove the bullet from the bullet array list.
			 */
			if(b.isVisible())
				b.move();
			else bListWarship.remove(i);
		}

		// (2) Boss Spacecraft's bullets
		//create a local arraylist of Bullets for the bullets 'shot' by the boss spacecraft
		ArrayList<Bullet> bListBoss = this.boss.getBullets();

		//Loop through the bullet list and check whether a bullet is still visible.
		for(int i = 0; i < bListBoss.size(); i++){
			Bullet b = bListBoss.get(i);
			// If a bullet is visible, move the bullet, else, remove the bullet from the bullet array list.
			if(b.isVisible()) {
				b.move();
				// if the bullet hits the warship, reduce the warship's strength
				if (b.collidesWith(this.myWarship)) {
					b.setVisible(false);
					this.myWarship.receiveDamage(Boss.BOSS_DAMAGE);

					// color change effect on the warship indicating that it has been hit by the boss spacecraft's bullet
					if (this.myWarship.getInvulnerabe() == false) {
						this.myWarship.setHasBeenHit(true);
						PauseTransition hitEffect = new PauseTransition(Duration.seconds(0.5));
						hitEffect.setOnFinished(event -> this.myWarship.setHasBeenHit(false));
						hitEffect.play();
					}
				}
			}

			else bListBoss.remove(i);
		}
	}


	//method that will move the spacecrafts during gameplay
	private void movespacecrafts(){
		//Loop through the spacecrafts arraylist
		for(int i = 0; i < this.spacecrafts.size(); i++){
			Spacecraft f = this.spacecrafts.get(i);
			/*
			 * TODO:  *If a Spacecraft is alive, move the Spacecraft. Else, remove the Spacecraft from the spacecrafts arraylist.
			 */

			//create a local arraylist of Bullets for the bullets 'shot' by the ship
			ArrayList<Bullet> bList = this.myWarship.getBullets();

			// check if the spacecraft is still alive
			if (f.isAlive()) {
				f.move(); 			// allows spacecraft to move
				if (f.collidesWith(this.myWarship)) {
					f.die(); 	// set alive to false if the spacecraft collides with our warship
					// reduce warship's strength according to the damage of the spacecraft
					this.myWarship.receiveDamage(f.getDamage());

					// color change effect on the warship indicating that it has been hit by a spacecraft
					if (this.myWarship.getInvulnerabe() == false) {
						this.myWarship.setHasBeenHit(true);
						PauseTransition hitEffect = new PauseTransition(Duration.seconds(0.5));
						hitEffect.setOnFinished(event -> this.myWarship.setHasBeenHit(false));
						hitEffect.play();
					}
				}

				//Loop through the bullet list and check whether the spacecraft collides with any of our warship's bullet
				for(int j = 0; j < bList.size(); j++){
					Bullet b = bList.get(j);

					// check if spacecraft crashes into a bullet
		        	if (f.collidesWith(b)) {
		        		f.die(); 					// set spacecraft's alive attribute to false
		        		b.setVisible(false); 		// make the bullet invisible (for deletion)
		        		bList.remove(j); 			// remove the bullet in our warship's arraylist for bullets
		        		this.spacecraftsKilled++; 			// increment score

		        		System.out.println("Score: " + this.spacecraftsKilled);// for checking

		        	}
				}

			} else {
				this.explosionEffects.add(new Explosion(f.getX(), f.getY(), Spacecraft.SPACECRAFT_WIDTH, Spacecraft.SPACECRAFT_WIDTH));
				this.spacecrafts.remove(f); 		// remove the spacecraft in the arrayList of spacecrafts when it isn't alive already

			}
		}
	}

	// method that will move the enemy Boss during gameplay
	private void moveBoss() {
		if (this.boss.isAlive()) {
			this.boss.move();

			//create a local arraylist of Bullets for the bullets 'shot' by the ship
			ArrayList<Bullet> bList = this.myWarship.getBullets();
			// reduce the warship's health when it collides with the enemy boss
			if (this.boss.collidesWith(this.myWarship)) {

				// color change effect on the warship indicating that it has been hit by a spacecraft
				if (this.myWarship.getInvulnerabe() == false) {
					if (this.myWarship.isHit()== false) this.myWarship.receiveDamage(this.boss.getDamage());
					this.myWarship.setHasBeenHit(true);
					PauseTransition hitEffect = new PauseTransition(Duration.seconds(1));
					hitEffect.setOnFinished(event -> this.myWarship.setHasBeenHit(false));
					hitEffect.play();
				}
			}

			//Loop through the bullet list and check whether the enemy boss collides with any of our warship's bullet
			for(int i = 0; i < bList.size(); i++){
				Bullet b = bList.get(i);

				// check if spacecraft crashes into a bullet
	        	if (this.boss.collidesWith(b)) {
	        		this.boss.receiveDamage(this.myWarship.getStrength());
	        		b.setVisible(false); 		// make the bullet invisible (for deletion)
	        		bList.remove(i); 			// remove the bullet in our warship's arraylist for bullets

					this.boss.setHasBeenHit(true);
					PauseTransition hitEffect = new PauseTransition(Duration.seconds(0.1));
					hitEffect.setOnFinished(event -> this.boss.setHasBeenHit(false));
					hitEffect.play();

	        		System.out.println("Boss Health: " + this.boss.getHealth());// for checking

	        	}
			}
		} else {
			this.explosionEffects.add(new Explosion(this.boss.getX(), this.boss.getY(), Boss.BOSS_WIDTH, Boss.BOSS_HEIGHT));
			this.canSpawnBoss = false;
			if (this.boss.getHealth() <=0) this.isBossKilled = true;
		}

	}


	//method that will move the warship depending on what key is pressed
	private void movemyWarship(KeyCode ke) {
		// change movement flags
		if(ke==KeyCode.UP) this.myWarshipGoUp = true;
		else if(ke==KeyCode.LEFT) this.myWarshipGoLeft  = true;
		else if(ke==KeyCode.DOWN) this.myWarshipGoDown = true;
		else if(ke==KeyCode.RIGHT) this.myWarshipGoRight = true;
		else if(ke==KeyCode.SPACE) this.myWarshipGoShoot = true;


		// move our warship 10 pixels up
		// update warship's sprite when moving up
        if (this.myWarshipGoUp) {
        	this.myWarship.setDY(-Warship.SPEED);
        	this.myWarship.loadImage(Warship.WARSHIP_IMAGE_UP);
        }

        // move our warship 10 pixels down
        // update warship's sprite when moving down
        else if (this.myWarshipGoDown) {
        	this.myWarship.setDY(Warship.SPEED);
        	this.myWarship.loadImage(Warship.WARSHIP_IMAGE_DOWN);
        }
        // this allows the warship to go down when the down arrow key is being hold by the user
        if(ke==KeyCode.DOWN && this.myWarshipGoUp) {
        	this.myWarship.setDY(Warship.SPEED);
        	this.myWarship.loadImage(Warship.WARSHIP_IMAGE_DOWN);	// change warship's sprite when down arrow key is being hold
        }

        // move our warship 10 pixels left
        if (this.myWarshipGoLeft) this.myWarship.setDX(-Warship.SPEED);
        // move our warship 10 pixels left
        else if (this.myWarshipGoRight)  this.myWarship.setDX(Warship.SPEED);

        // this allows the warship to go right when the left arrow key is being hold by the user
        if(ke==KeyCode.RIGHT && this.myWarshipGoLeft) this.myWarship.setDX(Warship.SPEED);


		System.out.println(ke+" key pressed."); 	// for checking

   	}

	//method that will stop the warship's movement; set the warship's DX and DY to 0
	private void stopmyWarship(KeyCode ke){

		System.out.println(ke+" key released."); 	// for checking

		// update movement flags
		if(ke==KeyCode.UP) this.myWarshipGoUp = false;
		else if(ke==KeyCode.LEFT) this.myWarshipGoLeft  = false;
		else if(ke==KeyCode.DOWN) this.myWarshipGoDown = false;
		else if(ke==KeyCode.RIGHT) this.myWarshipGoRight = false;
		else if(ke==KeyCode.SPACE) this.myWarshipGoShoot = false;

		// when Up arrow key is released but Down arrow key is on hold
        if (this.myWarshipGoUp == false && this.myWarshipGoDown == true) {
        	this.myWarship.setDY(Warship.SPEED);
        	this.myWarship.loadImage(Warship.WARSHIP_IMAGE_DOWN); // update sprite when moving down
        }

        // when Down arrow key is released but Up arrow key is on hold
        if (this.myWarshipGoUp == true && this.myWarshipGoDown == false) {
        	this.myWarship.setDY(-Warship.SPEED);
        	this.myWarship.loadImage(Warship.WARSHIP_IMAGE_UP); // update sprite when moving up
        }

        // when up and down arrow keys are not being pressed
        if (this.myWarshipGoUp == false && this.myWarshipGoDown == false) {
        	this.myWarship.setDY(0);
        	this.myWarship.loadImage(Warship.WARSHIP_IMAGE_DEFAULT); // change sprite back to default
        }

        // when Left arrow key is released but Right arrow key is on hold
        if (this.myWarshipGoLeft == false && this.myWarshipGoRight == true) this.myWarship.setDX(Warship.SPEED);

        // when Right arrow key is released but Left arrow key is on hold
        if (this.myWarshipGoLeft == true && this.myWarshipGoRight == false) this.myWarship.setDX(-Warship.SPEED);

        // when both left and right arrow keys are not being pressed
        if (this.myWarshipGoLeft == false && this.myWarshipGoRight == false) this.myWarship.setDX(0);

	}

    // ***********************************************************************************************************************
	// Miscs
	// Game Condition (changing spacecrafts' speed to maximum every 15 seconds for 3 seconds)
	// method that will temporarily increase the speed of spacecrafts to MAX
	private void increaseSpacecraftSpeed() {
		Spacecraft.setIsMaxSpeed(true);
	}

	// method that will temporarily decrease the speed of spacecrafts to MAX
	private void resetSpacecraftSpeed() {
		Spacecraft.setIsMaxSpeed(false);
	}

	// method to kill/explode all objects
    private void explodeAll() {
    	// force destroy the warship
    	this.myWarship.die();
    	// add an explosion effect on the warship's position
    	this.explosionEffects.add(new Explosion(this.myWarship.getX(), this.myWarship.getY(), Warship.WARSHIP_WIDTH, Warship.WARSHIP_WIDTH));
		// set the alive status of all spacecrafts to false
    	for (Spacecraft f : this.spacecrafts){
			f.die();
		}
		// set the alive status of enemy boss to false and add an explosion effect
		this.boss.die();

	}


    // ***********************************************************************************************************************
	// Event Handlers
	//method that will listen and handle the key press events
	private void handleKeyPressEvent() {
		this.theScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent e){
            	KeyCode code = e.getCode();
                movemyWarship(code);
			}
		});

		this.theScene.setOnKeyReleased(new EventHandler<KeyEvent>(){
            public void handle(KeyEvent e){
            	KeyCode code = e.getCode();
                stopmyWarship(code);
            }
        });
    }

    // ***********************************************************************************************************************
	// Game Over Scene
	private void displayGameOverScene(){
		this.stop();
	 	this.changeScene();
	}

	private void changeScene() {
		GameOverStage gameover = new GameOverStage(this.theStage, this.isGameWon, this.isBossKilled, this.spacecraftsKilled, Fire.totalAmountCollected(), Ice.totalAmountCollected(), Light.totalAmountCollected(), Wind.totalAmountCollected(), Orb.totalAmountCollected());

		// reset static variables from outside classes
		Spacecraft.resetSpeed();
		Orb.resetOrbCounters();

		// root fade-in/fade-out transition
		// <https://thecodinginterface.com/blog/javafx-animated-scene-transitions/>
	    Group rootToRemove = (Group) this.theScene.getRoot();
	    StackPane rootToAdd = gameover.getRootZeroOpacity();

	    FadeTransition fadeOut = new FadeTransition(Duration.millis(1000));
	    FadeTransition fadeIn = new FadeTransition(Duration.millis(1000));

	    fadeIn.setNode(rootToAdd);
	    fadeIn.setFromValue(0);
	    fadeIn.setToValue(1);

	    fadeOut.setNode(rootToRemove);
	    fadeOut.setFromValue(1);
	    fadeOut.setToValue(0);

	    fadeOut.play();

	    fadeOut.setOnFinished(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				fadeIn.play();
		    	theScene.setRoot(gameover.getRoot());
			}
		});
	}

}
