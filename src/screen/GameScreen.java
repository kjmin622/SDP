package screen;

import engine.Cooldown;
import engine.Core;
import engine.GameSettings;
import engine.GameState;
import entity.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * Implements the game screen, where the action happens.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class GameScreen extends Screen {

	/**
	 * Milliseconds until the screen accepts user input.
	 */
	private static final int INPUT_DELAY = 6000;
	/**
	 * Bonus score for each life remaining at the end of the level.
	 */
	private static final int LIFE_SCORE = 100;
	/**
	 * Minimum time between bonus ship's appearances.
	 */
	private static final int BONUS_SHIP_INTERVAL = 20000;
	/**
	 * Maximum variance in the time between bonus ship's appearances.
	 */
	private static final int BONUS_SHIP_VARIANCE = 10000;
	/**
	 * Time until bonus ship explosion disappears.
	 */
	private static final int BONUS_SHIP_EXPLOSION = 500;
	/**
	 * Time from finishing the level to screen change.
	 */
	private static final int SCREEN_CHANGE_INTERVAL = 1500;
	/**
	 * Height of the interface separation line.
	 */
	private static final int SEPARATION_LINE_HEIGHT = 40;


	/**
	 * Current game difficulty settings.
	 */
	private GameSettings gameSettings;
	/**
	 * Current difficulty level number.
	 */
	private int level;
	/**
	 * Formation of enemy ships.
	 */
	private EnemyShipFormation enemyShipFormation;
	/**
	 * Bonus enemy ship that appears sometimes.
	 */
	private EnemyShip enemyShipSpecial;
	/**
	 * Minimum time between bonus ship appearances.
	 */
	private Cooldown enemyShipSpecialCooldown;
	/**
	 * Time until bonus ship explosion disappears.
	 */
	private Cooldown enemyShipSpecialExplosionCooldown;
	/**
	 * Time from finishing the level to screen change.
	 */
	private Cooldown screenFinishedCooldown;
	/**
	 * Set of all bullets fired by on screen ships.
	 */
	private Set<Bullet> bullets;
	private Set<Bullet> bullets2;
	/**
	 * Player's ship.
	 */
	private Ship ship;
	private Ship ship2; // 2인용인 경우 사용
	/**
	 * Current score.
	 */

	private int score;
	private int score2;
	/**
	 * Player lives left.
	 */
	private int lives;
	private int lives2;
	/**
	 * Total bullets shot by the player.
	 */
	private int bulletsShot;
	private int bulletsShot2;
	/**
	 * Total ships destroyed by the player.
	 */
	private int shipsDestroyed;
	private int shipsDestroyed2;
	/**
	 * Moment the game starts.
	 */
	private long gameStartTime;
	/**
	 * Checks if the level is finished.
	 */
	private boolean levelFinished;
	/**
	 * Checks if a bonus life is received.
	 */
	private boolean bonusLife;
	private boolean bonusLife2;

	private int gamemode;
	private Color color, color2;


	/**
	 * Constructor, establishes the properties of the screen.
<<<<<<< HEAD
	 *
	 * @param gameState    Current game state.
	 * @param gameSettings Current game settings.
	 * @param bonusLife    Checks if a bonus life is awarded this level.
	 * @param width        Screen width.
	 * @param height       Screen height.
	 * @param fps          Frames per second, frame rate at which the game is run.
=======
	 * 
	 * @param gameState
	 *            Current game state.
	 * @param gameSettings
	 *            Current game settings.
	 * @param bonusLife
	 *            Checks if a bonus life is awarded this level.
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
>>>>>>> develop
	 */
	public GameScreen(GameState gameState, final GameSettings gameSettings, final boolean bonusLife, final int width, final int height, final int fps, final int gamemode, final Color color) {
		super(width, height, fps);
		this.gameSettings = gameSettings;
		this.bonusLife = bonusLife;
		this.level = gameState.getLevel();
		this.score = gameState.getScore();
		this.gamemode = gamemode;
		this.lives = gameState.getLivesRemaining();
		this.color = color;
		if (this.bonusLife) {
			this.lives++;
		}
		this.bulletsShot = gameState.getBulletsShot();
		this.shipsDestroyed = gameState.getShipsDestroyed();
	}
	public GameScreen(GameState gameState1, final boolean bonusLife1,GameState gameState2, final boolean bonusLife2, final GameSettings gameSettings, final int width, final int height, final int fps, final int gamemode, final Color color, final Color color2) {
		super(width, height, fps);
		this.gameSettings = gameSettings;
		this.bonusLife = bonusLife1;
		this.bonusLife2 = bonusLife2;
		this.level = gameState1.getLevel();
		this.score = gameState1.getScore();
		this.score2 = gameState2.getScore();
		this.gamemode = gamemode;
		this.lives = gameState1.getLivesRemaining();
		this.color = color;
		this.color2 = color2;
		if (this.bonusLife && this.lives>0) {
			this.lives++;
		}
		this.lives2 = gameState2.getLivesRemaining();
		if (this.bonusLife2 && this.lives2>0) {
			this.lives2++;
		}
		this.bulletsShot = gameState1.getBulletsShot();
		this.bulletsShot2 = gameState2.getBulletsShot();
		this.shipsDestroyed = gameState1.getShipsDestroyed();
		this.shipsDestroyed2 = gameState2.getShipsDestroyed();
	}

	/**
	 * Initializes basic screen properties, and adds necessary elements.
	 */
	public final void initialize() {
		super.initialize();

		enemyShipFormation = new EnemyShipFormation(this.gameSettings);
		enemyShipFormation.attach(this);

		if(gamemode == 0){ // 1인용 게임일 경우
			this.ship = new Ship(this.width / 2, this.height - 30, color);
		}
		
		else if(gamemode == 1){ // 2인용 게임일 경우
			this.ship = new Ship(this.width / 2 - 15, this.height - 30, color);
			this.ship2 = new Ship(this.width / 2 + 15, this.height - 30, color2);
		}

		// Appears each 10-30 seconds.
		this.enemyShipSpecialCooldown = Core.getVariableCooldown(
				BONUS_SHIP_INTERVAL, BONUS_SHIP_VARIANCE);
		this.enemyShipSpecialCooldown.reset();
		this.enemyShipSpecialExplosionCooldown = Core.getCooldown(BONUS_SHIP_EXPLOSION);
		this.screenFinishedCooldown = Core.getCooldown(SCREEN_CHANGE_INTERVAL);
		this.bullets = new HashSet<Bullet>();
		this.bullets2 = new HashSet<Bullet>();

		// Special input delay / countdown.
		this.gameStartTime = System.currentTimeMillis();
		this.inputDelay = Core.getCooldown(INPUT_DELAY);
		this.inputDelay.reset();
	}

	/**
	 * Starts the action.
	 *
	 * @return Next screen code.
	 */
	public final int run() {
		super.run();

		this.score += LIFE_SCORE * (this.lives - 1);
		this.logger.info("Screen cleared with a score of " + this.score);
		if(gamemode==1){
			this.score2 += LIFE_SCORE * (this.lives2 - 1);
			this.logger.info("Screen cleared with a score of " + this.score2);
		}

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected final void update() {
		super.update();

		// pause button
		boolean pauseBtn = inputManager.isKeyDown(KeyEvent.VK_ESCAPE);
		if(pauseBtn && this.pauseDelay.checkFinished()){
			pause = !pause;
			this.pauseDelay.reset();
			this.ship.resetShootingCooldown();
			if(gamemode==1){
				this.ship2.resetShootingCooldown();
			}
		}
		if(pause && inputManager.isKeyDown(KeyEvent.VK_Q) && this.pauseDelay.checkFinished()){
			pause = !pause;
			lives2 = lives = 0;
			this.pauseDelay.reset();
		}

		if(!pause) {
			if (this.inputDelay.checkFinished() && !this.levelFinished) {

				if(gamemode == 0){ // 1인용 게임인 경우
					if (!this.ship.isDestroyed()) {
						boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_RIGHT) || inputManager.isKeyDown(KeyEvent.VK_D);

						boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_LEFT) || inputManager.isKeyDown(KeyEvent.VK_A);


						boolean isRightBorder = this.ship.getPositionX()
								+ this.ship.getWidth() + this.ship.getSpeed() > this.width - 1;
						boolean isLeftBorder = this.ship.getPositionX()
								- this.ship.getSpeed() < 1;

						if (moveRight && !isRightBorder) {
							this.ship.moveRight();
						}
						if (moveLeft && !isLeftBorder) {
							this.ship.moveLeft();
						}
						if (inputManager.isKeyDown(KeyEvent.VK_SPACE) || inputManager.isKeyDown(KeyEvent.VK_W) || inputManager.isKeyDown(KeyEvent.VK_UP)) {
							if (this.ship.shoot(this.bullets)) {
								this.bulletsShot++;
							}
						}
					}

				}

				else if(gamemode == 1){ // 2인용 게임인 경우
					if (!this.ship.isDestroyed() && this.lives > 0) {
						boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_RIGHT);
						boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_LEFT);
						boolean isRightBorder = this.ship.getPositionX() + this.ship.getWidth() + this.ship.getSpeed() > this.width - 1;
						boolean isLeftBorder = this.ship.getPositionX() - this.ship.getSpeed() < 1;

						if (moveRight && !isRightBorder) {
							this.ship.moveRight();
						}
						if (moveLeft && !isLeftBorder) {
							this.ship.moveLeft();
						}
						if (inputManager.isKeyDown(KeyEvent.VK_SPACE) || inputManager.isKeyDown(KeyEvent.VK_UP)) {
							if (this.ship.shoot(this.bullets)) {
								this.bulletsShot++;
							}
						}
					}

					if (!this.ship2.isDestroyed() && this.lives2 > 0) {
						boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_D);
						boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_A);
						boolean isRightBorder = this.ship2.getPositionX() + this.ship2.getWidth() + this.ship2.getSpeed() > this.width - 1;
						boolean isLeftBorder = this.ship2.getPositionX() - this.ship2.getSpeed() < 1;
						if (moveRight && !isRightBorder) {
							this.ship2.moveRight();
						}
						if (moveLeft && !isLeftBorder) {
							this.ship2.moveLeft();
						}
						if (inputManager.isKeyDown(KeyEvent.VK_W)) {
							if (this.ship2.shoot(this.bullets2)) {
								this.bulletsShot2++;
							}
						}
					}
				}


				if (this.enemyShipSpecial != null) {
					if (!this.enemyShipSpecial.isDestroyed()) {
						this.enemyShipSpecial.move(2, 0);
					}
					else if (this.enemyShipSpecialExplosionCooldown.checkFinished()) {
						this.enemyShipSpecial = null;
					}

				}
				if (this.enemyShipSpecial == null
						&& this.enemyShipSpecialCooldown.checkFinished()) {
					this.enemyShipSpecial = new EnemyShip();
					this.enemyShipSpecialCooldown.reset();
					this.logger.info("A special ship appears");
				}
				if (this.enemyShipSpecial != null
						&& this.enemyShipSpecial.getPositionX() > this.width) {
					this.enemyShipSpecial = null;
					this.logger.info("The special ship has escaped");
				}

				this.ship.update();

				if(gamemode == 1){
					this.ship2.update();
				}

				this.enemyShipFormation.update();
				this.enemyShipFormation.shoot(this.bullets);
			}
			manageCollisions();
			cleanBullets();
		}
		draw();
		if(this.lives <= 0){
			this.lives = 0;
		}
		if(gamemode==1 && this.lives2 <= 0){
			this.lives2 = 0;
		}
		if(!pause){
			if ((this.enemyShipFormation.isEmpty() || (this.lives <= 0 && this.lives2 <= 0)) && !this.levelFinished) {

				this.levelFinished = true;
				this.screenFinishedCooldown.reset();
			}

			if (this.levelFinished && this.screenFinishedCooldown.checkFinished()) {
				this.isRunning = false;
			}
		}
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		if(gamemode == 0){ // 1인용 게임인 경우
			drawManager.drawEntity(this.ship, this.ship.getPositionX(),
					this.ship.getPositionY());
		}

		else if(gamemode == 1){ // 2인용 게임인 경우
			if(lives>0 || this.ship.isDestroyed()){
				drawManager.drawEntity(this.ship, this.ship.getPositionX(), this.ship.getPositionY());
			}
			if(lives2>0 || this.ship2.isDestroyed()){
				drawManager.drawEntity(this.ship2, this.ship2.getPositionX(), this.ship2.getPositionY());
			}
		}


		if (this.enemyShipSpecial != null) {
			drawManager.drawEntity(this.enemyShipSpecial,
					this.enemyShipSpecial.getPositionX(),
					this.enemyShipSpecial.getPositionY());
		}

		enemyShipFormation.draw();

		for (Bullet bullet : this.bullets) {
			drawManager.drawEntity(bullet, bullet.getPositionX(),
					bullet.getPositionY());
		}
		for (Bullet bullet : this.bullets2) {
			drawManager.drawEntity(bullet, bullet.getPositionX(),
					bullet.getPositionY());
		}

		// Interface.
		if(gamemode==0){
			drawManager.drawScore(this, this.score);
			drawManager.drawLives(this, this.lives, ship.getColor(), gamemode);
		}
		if(gamemode == 1){
			drawManager.drawScore(this, this.score, this.score2);
			drawManager.drawLives(this, this.lives, this.lives2,ship.getColor(),ship2.getColor(),gamemode);
		}
		drawManager.drawHorizontalLine(this, SEPARATION_LINE_HEIGHT - 1);

		// Countdown to game start.
		if (!this.inputDelay.checkFinished()) {
			int countdown = (int) ((INPUT_DELAY
					- (System.currentTimeMillis()
					- this.gameStartTime)) / 1000);
			drawManager.drawCountDown(this, this.level, countdown,
					this.bonusLife);
			drawManager.drawHorizontalLine(this, this.height / 2 - this.height
					/ 12);
			drawManager.drawHorizontalLine(this, this.height / 2 + this.height
					/ 12);
		}
		if(this.pause){
			drawManager.drawPause(this);
			drawManager.drawHorizontalLine(this, this.height / 2 - this.height / 12);
			drawManager.drawHorizontalLine(this, this.height / 2 + this.height / 12);
		}
		drawManager.completeDrawing(this);
	}

	/**
	 * Cleans bullets that go off screen.
	 */

	private void cleanBullets() {
		Set<Bullet> recyclable = new HashSet<Bullet>();
		for (Bullet bullet : this.bullets) {
			bullet.update();
			if (bullet.getPositionY() < SEPARATION_LINE_HEIGHT
					|| bullet.getPositionY() > this.height) {
				recyclable.add(bullet);
			}
		}
		this.bullets.removeAll(recyclable);
		BulletPool.recycle(recyclable);

		recyclable = new HashSet<Bullet>();
		for (Bullet bullet : this.bullets2) {
			bullet.update();
			if (bullet.getPositionY() < SEPARATION_LINE_HEIGHT
					|| bullet.getPositionY() > this.height) {
				recyclable.add(bullet);
			}
		}
		this.bullets2.removeAll(recyclable);
		BulletPool.recycle(recyclable);
	}

	/**
	 * Manages collisions between bullets and ships.
	 */

	private void manageCollisions() {
		Set<Bullet> recyclable = new HashSet<Bullet>();
		for (Bullet bullet : this.bullets) {
			if (bullet.getSpeed() > 0) {

				if (gamemode == 0) { // 1인용 게임인 경우
					if (checkCollision(bullet, this.ship) && !this.levelFinished) {
						recyclable.add(bullet);
						if (!this.ship.isDestroyed()) {
							this.ship.destroy();
							this.lives--;
							this.logger.info("Hit on player ship, " + this.lives
									+ " lives remaining.");
						}
					}
				}

				if (gamemode == 1) { // 2인용 게임인 경우
					if (checkCollision(bullet, this.ship) && !this.levelFinished) {
						recyclable.add(bullet);
						if (!this.ship.isDestroyed() && this.lives > 0) {
							this.ship.destroy();
							this.lives--;
							this.logger.info("Hit on player ship, " + this.lives
									+ " lives remaining.");
						}
					}

					if (checkCollision(bullet, this.ship2) && !this.levelFinished) {
						recyclable.add(bullet);
						if (!this.ship2.isDestroyed() && this.lives2 > 0) {
							this.ship2.destroy();
							this.lives2--;
							this.logger.info("Hit on player ship2, " + this.lives
									+ " lives remaining.");
						}
					}

				}

			} else {
				for (EnemyShip enemyShip : this.enemyShipFormation) {
					if (!enemyShip.isDestroyed() && checkCollision(bullet, enemyShip)) {
						this.score += enemyShip.getPointValue();
						this.shipsDestroyed++;
						this.enemyShipFormation.destroy(enemyShip);
						recyclable.add(bullet);
					}
				}

				if (this.enemyShipSpecial != null && !this.enemyShipSpecial.isDestroyed() && checkCollision(bullet, this.enemyShipSpecial)) {
					this.score += this.enemyShipSpecial.getPointValue();
					this.shipsDestroyed++;
					this.enemyShipSpecial.destroy();
					this.enemyShipSpecialExplosionCooldown.reset();
					recyclable.add(bullet);
				}
			}
		}
		this.bullets.removeAll(recyclable);
		BulletPool.recycle(recyclable);

		recyclable = new HashSet<Bullet>();
		for(Bullet bullet : this.bullets2){
			for (EnemyShip enemyShip : this.enemyShipFormation) {
				if (!enemyShip.isDestroyed() && checkCollision(bullet, enemyShip)) {
					this.score2 += enemyShip.getPointValue();
					this.shipsDestroyed2++;
					this.enemyShipFormation.destroy(enemyShip);
					recyclable.add(bullet);
				}
			}

			if (this.enemyShipSpecial != null && !this.enemyShipSpecial.isDestroyed() && checkCollision(bullet, this.enemyShipSpecial)) {
				this.score2 += this.enemyShipSpecial.getPointValue();
				this.shipsDestroyed2++;
				this.enemyShipSpecial.destroy();
				this.enemyShipSpecialExplosionCooldown.reset();
				recyclable.add(bullet);
			}
		}
		this.bullets2.removeAll(recyclable);
		BulletPool.recycle(recyclable);
	}

	/**
	 * Checks if two entities are colliding.
	 *
	 * @param a First entity, the bullet.
	 * @param b Second entity, the ship.
	 * @return Result of the collision test.
	 */
	private boolean checkCollision(final Entity a, final Entity b) {
		// Calculate center point of the entities in both axis.
		int centerAX = a.getPositionX() + a.getWidth() / 2;
		int centerAY = a.getPositionY() + a.getHeight() / 2;
		int centerBX = b.getPositionX() + b.getWidth() / 2;
		int centerBY = b.getPositionY() + b.getHeight() / 2;
		// Calculate maximum distance without collision.
		int maxDistanceX = a.getWidth() / 2 + b.getWidth() / 2;
		int maxDistanceY = a.getHeight() / 2 + b.getHeight() / 2;
		// Calculates distance.
		int distanceX = Math.abs(centerAX - centerBX);
		int distanceY = Math.abs(centerAY - centerBY);

		return distanceX < maxDistanceX && distanceY < maxDistanceY;
	}

	/**
	 * Returns a GameState object representing the status of the game.
	 *
	 * @return Current game state.
	 */
	public final GameState getGameState() {
		return new GameState(this.level, this.score, this.lives, this.bulletsShot, this.shipsDestroyed);
	}
	public final GameState getGameState2(){
		return new GameState(this.level, this.score2, this.lives2, this.bulletsShot2, this.shipsDestroyed2);
	}
}