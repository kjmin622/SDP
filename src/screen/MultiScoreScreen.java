package screen;

import engine.Cooldown;
import engine.Core;
import engine.GameState;
import engine.Score;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Implements the score screen.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class MultiScoreScreen extends Screen {

	/** Milliseconds between changes in user selection. */
	private static final int SELECTION_TIME = 200;
	/** Maximum number of high scores. */
	private static final int MAX_HIGH_SCORE_NUM = 7;
	/** Code of first mayus character. */
	private static final int FIRST_CHAR = 65;
	/** Code of last mayus character. */
	private static final int LAST_CHAR = 90;

	/** Current score. */
	private int score1, score2;
	/** Player lives left. */
	private int livesRemaining1, livesRemaining2;
	/** Total bullets shot by the player. */
	private int bulletsShot1, bulletsShot2;
	/** Total ships destroyed by the player. */
	private int shipsDestroyed1, shipsDestroyed2;
	/** List of past high scores. */
	private List<Score> highScores;
	/** Checks if current score is a new high score. */
	private boolean isNewRecord1,isNewRecord2;
	/** Player name for record input. */
	private char[] name1, name2;
	/** Character of players name selected for change. */
	private int nameCharSelected1, nameCharSelected2;
	/** Time between changes in user selection. */
	private Cooldown selectionCooldown;

	private int select;
	private int gamemode;

	/**
	 * Constructor, establishes the properties of the screen.
	 *
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 * @param gameState
	 *            Current game state.
	 */
	public MultiScoreScreen(final int width, final int height, final int fps, final GameState gameState, final GameState gameState2, int playermode, int difficulty) {
		super(width, height, fps);
		this.score1 = gameState.getScore();
		this.score2 = gameState2.getScore();
		this.livesRemaining1 = gameState.getLivesRemaining();
		this.livesRemaining2 = gameState2.getLivesRemaining();
		this.bulletsShot1 = gameState.getBulletsShot();
		this.bulletsShot2 = gameState2.getBulletsShot();
		this.shipsDestroyed1 = gameState.getShipsDestroyed();
		this.shipsDestroyed2 = gameState2.getShipsDestroyed();
		this.isNewRecord1 = false;
		this.isNewRecord2 = false;
		this.name1 = "AAA".toCharArray();
		this.name2 = "AAA".toCharArray();
		this.nameCharSelected1 = 0;
		this.nameCharSelected2 = 0;
		this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
		this.selectionCooldown.reset();
		this.gamemode = playermode * 4 + difficulty;
		this.select = 0;
		try {
			this.highScores = Core.getFileManager().loadHighScores(gamemode);
			if(highScores.size()+1 < MAX_HIGH_SCORE_NUM){
				this.isNewRecord2 = this.isNewRecord1 = true;
			}
			else if(highScores.size() < MAX_HIGH_SCORE_NUM && (score1 > score2)){
				this.isNewRecord1 = true;
			}
			else if(highScores.size() < MAX_HIGH_SCORE_NUM && (score1 < score2)){
				this.isNewRecord2 = true;
				select = 1;
			}
			else if(highScores.get(highScores.size() - 1).getScore() < this.score1){
				this.isNewRecord1 = true;
			}
			else if(highScores.get(highScores.size() - 1).getScore() < this.score2){
				this.isNewRecord2 = true;
				select = 1;
			}

		} catch (IOException e) {
			logger.warning("Couldn't load high scores!");
		}
	}

	/**
	 * Starts the action.
	 * 
	 * @return Next screen code.
	 */
	public final int run() {
		super.run();

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected final void update() {
		super.update();

		draw();
		if (this.inputDelay.checkFinished()) {
			if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
				// Return to main menu.
				this.returnCode = 1;
				this.isRunning = false;
				if (this.isNewRecord1 || this.isNewRecord2) {
					saveScore();
				}
			}
			else if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
				// Play again.
				this.returnCode = 2;
				this.isRunning = false;
				if (this.isNewRecord1 || this.isNewRecord2) {
					saveScore();
				}
			}

			// 플레이어 1 기록
			if (this.select==0 && this.isNewRecord1 && this.selectionCooldown.checkFinished()) {
				if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
					if(this.nameCharSelected1 >= 2){
						if(isNewRecord2){
							select = 1;
							nameCharSelected2=0;
						}
						else{
							nameCharSelected1=0;
						}
					}
					else{
						nameCharSelected1++;
					}
					this.selectionCooldown.reset();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_LEFT)) {
					if(this.nameCharSelected1<=0){
						if(isNewRecord2){
							select=1;
							nameCharSelected2=2;
						}
						else{
							nameCharSelected1=2;
						}
					}
					else{
						nameCharSelected1--;
					}
					this.selectionCooldown.reset();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_UP)) {
					this.name1[this.nameCharSelected1] =
							(char) (this.name1[this.nameCharSelected1]
									== LAST_CHAR ? FIRST_CHAR
							: this.name1[this.nameCharSelected1] + 1);
					this.selectionCooldown.reset();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_DOWN)) {
					this.name1[this.nameCharSelected1] =
							(char) (this.name1[this.nameCharSelected1]
									== FIRST_CHAR ? LAST_CHAR
							: this.name1[this.nameCharSelected1] - 1);
					this.selectionCooldown.reset();
				}
			}

			// 플레이어 2 기록
			if (this.select==1 && this.isNewRecord2 && this.selectionCooldown.checkFinished()) {
				if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
					if(nameCharSelected2>=2){
						if(isNewRecord1){
							select=0;
							nameCharSelected1=0;
						}
						else{
							nameCharSelected2=0;
						}
					}
					else{
						nameCharSelected2++;
					}
					this.selectionCooldown.reset();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_LEFT)) {
					if(nameCharSelected2 <= 0){
						if(isNewRecord1){
							select = 0;
							nameCharSelected1=2;
						}
						else{
							nameCharSelected2=2;
						}
					}
					else{
						nameCharSelected2--;
					}
					this.selectionCooldown.reset();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_UP)) {
					this.name2[this.nameCharSelected2] =
							(char) (this.name2[this.nameCharSelected2]
									== LAST_CHAR ? FIRST_CHAR
									: this.name2[this.nameCharSelected2] + 1);
					this.selectionCooldown.reset();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_DOWN)) {
					this.name2[this.nameCharSelected2] =
							(char) (this.name2[this.nameCharSelected2]
									== FIRST_CHAR ? LAST_CHAR
									: this.name2[this.nameCharSelected2] - 1);
					this.selectionCooldown.reset();
				}
			}

		}

	}

	/**
	 * Saves the score as a high score.
	 */
	private void saveScore() {
		if(this.isNewRecord1){
			highScores.add(new Score(new String(this.name1), score1));
			Collections.sort(highScores);
			if (highScores.size() > MAX_HIGH_SCORE_NUM) {
				highScores.remove(highScores.size() - 1);
			}

			try {
				Core.getFileManager().saveHighScores(highScores,gamemode);
			} catch (IOException e) {
				logger.warning("Couldn't load high scores!");
			}
		}
		if(this.isNewRecord2){
			highScores.add(new Score(new String(this.name2), score2));
			Collections.sort(highScores);
			if (highScores.size() > MAX_HIGH_SCORE_NUM) {
				highScores.remove(highScores.size() - 1);
			}

			try {
				Core.getFileManager().saveHighScores(highScores,gamemode);
			} catch (IOException e) {
				logger.warning("Couldn't load high scores!");
			}
		}

	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawGameOver(this, this.inputDelay.checkFinished(), this.isNewRecord1, this.isNewRecord2, this.score1, this.score2);
		drawManager.drawResults(this, this.score1, this.livesRemaining1, this.shipsDestroyed1, this.bulletsShot1!=0 ? (float) this.shipsDestroyed1 / this.bulletsShot1 : 0, this.isNewRecord1,
									this.score2,this.livesRemaining2,this.shipsDestroyed2,this.bulletsShot2!=0 ? (float)this.shipsDestroyed2/this.bulletsShot2 : 0, this.isNewRecord2);

		if (this.isNewRecord1 || this.isNewRecord2) {
			drawManager.drawNameInput(this, this.name1,this.name2, this.nameCharSelected1, this.nameCharSelected2, this.isNewRecord1, this.isNewRecord2, this.select);
		}

		drawManager.completeDrawing(this);
	}
}
