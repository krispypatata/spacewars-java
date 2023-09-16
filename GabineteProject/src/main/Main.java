/**********************************************************************************************************************************************************
 * CMSC 22: Introduction to Object-Oriented Programming
 * Laboratory MiniProject (JavaFX)
 *
 * This is a simple Space shooter Game
 *
 * Specifications:
 * Upon creation, the warship appears on the leftmost side of the screen with an arbitrary y position (i.e. random y).
 * It is given an initial strength level whose value is a random number between 100-150. The warship can move 10 pixels at
 * a time. The warship can also shoot bullets when the spacebar is pressed. The bullets shot by the warship move towards the
 * right side of the screen in a straight line and disappear when the end of the screen is reached. The bullet’s damage
 * is equal to the current warship strength.
 *
 * Many disturbed spacecraft appear from the right side of the screen in random positions. When a spacecraft is spawned, its damage
 * is randomized from 30 to 40. The spacecraft glides towards the left side and back (back and forth the screen). A spacecraft is
 * initially alive and has a random movement speed between 1-5, inclusive. At the start, there are 7 spacecraft but 3 more are
 * spawned every 5 seconds. The spacecrafts’ movement speeds up to maximum speed every 15 seconds for 3 seconds.
 *
 * When a spacecraft hits the warship, the warship’s strength is reduced by the spacecraft’s damage, and the spacecraft dies and disappears.
 * When the warship’s strength reaches 0, the game is over and the warship loses. When a spacecraft is hit by the warship’s bullet, it
 * immediately dies and disappears from the screen.
 *
 * The game lasts for one minute. If the warship is still alive, the warship wins. A screen appears to display if the warship wins or loses.
 * The total number of spacecraft that were killed are also displayed and the the total number of orbs collected
 * (also the total number of orbs collected per type).
 *
 * Orbs (Power-ups)
 * Throughout the game, orbs appear at random locations on the left half of the screen at 10-second intervals. These can be
 * collected by the warship. Orbs disappear after 5 seconds if uncollected. The game should implement these three orbs:
 * Fire Orb - doubles the current strength of the warship
 * Light Orb - provides immortality to the warship for 5 secs (change warship Sprite to show immortality)
 * Ice Orb- slows down all the spacecrafts movement to the minimum speed for 5 secs
 * Wind Orb - Wind Orb enables the warship to shoot three bullets at at a time. Buff-effect also lasts for 5 seconds.
 *
 * (BONUS) Boss
 * The boss spawns at the 30-second mark of gameplay. Just like a regular spacecraft, it appears at random (x,y) coordinates on the right side
 *  of the screen. The boss moves diagonally across the screen and shoots two bullets simultaneously (one on each side - left and right).
 *  The boss spacecraft is bigger than a regular spacecraft and has an initial damage value - of 50. The warship's strength will be reduced by
 *  the said damage value when (1) the boss unit itself hits the warship or (2) the boss' bullets hit the warship. Initially, the boss is alive,
 *  has a random movement speed between 1-5, inclusive, and has a health of 3000. Every time the warship’s bullet hits the boss spacecraft, boss'
 *  health is reduced by the warship’s current strength. The Boss dies and disappears when its health reaches 0.
 *
 * (c) Institute of Computer Science, CAS, UPLB
 *
 * @author Keith Ginoel S. Gabinete
 * @date 2023-05-20 21:31
 *
 **********************************************************************************************************************************************************/

package main;

import gamewindow.GameStage;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) throws Exception {
		GameStage theGameStage = new GameStage();
		theGameStage.setStage(stage);
	}

}
