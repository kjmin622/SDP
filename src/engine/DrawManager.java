package engine;

import entity.Entity;
import entity.Ship;
import screen.Screen;

import java.awt.Frame;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Manages screen drawing. // 화면에 띄우는 것 관리
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public final class DrawManager {

	/** Singleton instance of the class. // 오직 한 개의 클래스 인스턴스만 갖도록  */
	private static DrawManager instance;
	/** Current frame. // 현재 프레임 */
	private static Frame frame;
	/** FileManager instance. // */
	private static FileManager fileManager;
	/** Application logger. */
	private static Logger logger;
	/** Graphics context. */
	private static Graphics graphics;
	/** Buffer Graphics. */
	private static Graphics backBufferGraphics;
	/** Buffer image. */
	private static BufferedImage backBuffer;
	/** Normal sized font. */
	private static Font fontRegular;
	/** Normal sized font properties. */
	private static FontMetrics fontRegularMetrics;
	/** Big sized font. */
	private static Font fontBig;
	/** Big sized font properties. */
	private static FontMetrics fontBigMetrics;

	/** Sprite types mapped to their images. */
	private static Map<SpriteType, boolean[][]> spriteMap;
	private static Map<SpriteType, boolean[][]> spriteMap2;

	/** Sprite types. */
	public enum SpriteType {
		/** Player ship. */
		Ship,

		/** Destroyed player ship. */
		ShipDestroyed,
		/** Player bullet. */
		Bullet,
		/** Enemy bullet. */
		EnemyBullet,
		/** First enemy ship - first form. */
		EnemyShipA1,
		/** First enemy ship - second form. */
		EnemyShipA2,
		/** Second enemy ship - first form. */
		EnemyShipB1,
		/** Second enemy ship - second form. */
		EnemyShipB2,
		/** Third enemy ship - first form. */
		EnemyShipC1,
		/** Third enemy ship - second form. */
		EnemyShipC2,
		/** Bonus ship. */
		EnemyShipSpecial,
		/** Destroyed enemy ship. */
		Explosion
	}

	/**
	 * Private constructor.
	 */
	private DrawManager() {
		fileManager = Core.getFileManager();
		logger = Core.getLogger();
		logger.info("Started loading resources.");

		try {
			spriteMap = new LinkedHashMap<SpriteType, boolean[][]>();

			spriteMap.put(SpriteType.Ship, new boolean[13][8]);

			spriteMap.put(SpriteType.ShipDestroyed, new boolean[13][8]);
			spriteMap.put(SpriteType.Bullet, new boolean[3][5]);
			spriteMap.put(SpriteType.EnemyBullet, new boolean[3][5]);
			spriteMap.put(SpriteType.EnemyShipA1, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipA2, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipB1, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipB2, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipC1, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipC2, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipSpecial, new boolean[16][7]);
			spriteMap.put(SpriteType.Explosion, new boolean[13][7]);

//			spriteMap2 = new LinkedHashMap<SpriteType, boolean[][]>();
//
//			spriteMap2.put(SpriteType.Ship, new boolean[13][8]);
//
//			spriteMap2.put(SpriteType.ShipDestroyed, new boolean[13][8]);
//			spriteMap2.put(SpriteType.Bullet, new boolean[3][5]);
//			spriteMap2.put(SpriteType.EnemyBullet, new boolean[3][5]);
//			spriteMap2.put(SpriteType.EnemyShipA1, new boolean[12][8]);
//			spriteMap2.put(SpriteType.EnemyShipA2, new boolean[12][8]);
//			spriteMap2.put(SpriteType.EnemyShipB1, new boolean[12][8]);
//			spriteMap2.put(SpriteType.EnemyShipB2, new boolean[12][8]);
//			spriteMap2.put(SpriteType.EnemyShipC1, new boolean[12][8]);
//			spriteMap2.put(SpriteType.EnemyShipC2, new boolean[12][8]);
//			spriteMap2.put(SpriteType.EnemyShipSpecial, new boolean[16][7]);
//			spriteMap2.put(SpriteType.Explosion, new boolean[13][7]);

			fileManager.loadSprite(spriteMap);
			// fileManager.loadSprite(spriteMap2);
			logger.info("Finished loading the sprites.");

			// Font loading.
			fontRegular = fileManager.loadFont(14f);
			fontBig = fileManager.loadFont(24f);
			logger.info("Finished loading the fonts.");

		} catch (IOException e) {
			logger.warning("Loading failed.");
		} catch (FontFormatException e) {
			logger.warning("Font formating failed.");
		}
	}

	/**
	 * Returns shared instance of DrawManager.
	 *
	 * @return Shared instance of DrawManager.
	 */
	protected static DrawManager getInstance() {
		if (instance == null){
			instance = new DrawManager();
		}
		return instance;
	}

	/**
	 * Sets the frame to draw the image on.
	 *
	 * @param currentFrame
	 *            Frame to draw on.
	 */
	public void setFrame(final Frame currentFrame) {
		frame = currentFrame;
	}

	/**
	 * First part of the drawing process. Initialices buffers, draws the
	 * background and prepares the images.
	 *
	 * @param screen
	 *            Screen to draw in.
	 */
	public void initDrawing(final Screen screen) {
		backBuffer = new BufferedImage(screen.getWidth(), screen.getHeight(),
				BufferedImage.TYPE_INT_RGB);

		graphics = frame.getGraphics();
		backBufferGraphics = backBuffer.getGraphics();

		backBufferGraphics.setColor(Color.BLACK);
		backBufferGraphics
				.fillRect(0, 0, screen.getWidth(), screen.getHeight());

		fontRegularMetrics = backBufferGraphics.getFontMetrics(fontRegular);
		fontBigMetrics = backBufferGraphics.getFontMetrics(fontBig);

		// drawBorders(screen);
		// drawGrid(screen);
	}

	/**
	 * Draws the completed drawing on screen.
	 *
	 * @param screen
	 *            Screen to draw on.
	 */
	public void completeDrawing(final Screen screen) {
		graphics.drawImage(backBuffer, frame.getInsets().left,
				frame.getInsets().top, frame);
	}

	/**
	 * Draws an entity, using the apropiate image.
	 *
	 * @param entity
	 *            Entity to be drawn.
	 * @param positionX
	 *            Coordinates for the left side of the image.
	 * @param positionY
	 *            Coordinates for the upper side of the image.
	 */
	public void drawEntity(final Entity entity, final int positionX,
						   final int positionY) {
		boolean[][] image = spriteMap.get(entity.getSpriteType());

		backBufferGraphics.setColor(entity.getColor());

		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[i].length; j++) {
				if (image[i][j]) {
					backBufferGraphics.drawRect(positionX + i * 2, positionY + j * 2, 1, 1);
				}
			}
		}
	}

	/**
	 * For debugging purpouses, draws the canvas borders.
	 *
	 * @param screen
	 *            Screen to draw in.
	 */
	@SuppressWarnings("unused")
	private void drawBorders(final Screen screen) {
		backBufferGraphics.setColor(Color.GREEN);
		backBufferGraphics.drawLine(0, 0, screen.getWidth() - 1, 0);
		backBufferGraphics.drawLine(0, 0, 0, screen.getHeight() - 1);
		backBufferGraphics.drawLine(screen.getWidth() - 1, 0,
				screen.getWidth() - 1, screen.getHeight() - 1);
		backBufferGraphics.drawLine(0, screen.getHeight() - 1,
				screen.getWidth() - 1, screen.getHeight() - 1);
	}

	/**
	 * For debugging purpouses, draws a grid over the canvas.
	 *
	 * @param screen
	 *            Screen to draw in.
	 */

	@SuppressWarnings("unused")
	private void drawGrid(final Screen screen) {
		backBufferGraphics.setColor(Color.DARK_GRAY);
		for (int i = 0; i < screen.getHeight() - 1; i += 2)
			backBufferGraphics.drawLine(0, i, screen.getWidth() - 1, i);
		for (int j = 0; j < screen.getWidth() - 1; j += 2)
			backBufferGraphics.drawLine(j, 0, j, screen.getHeight() - 1);
	}

	/**
	 * Draws current score on screen.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param score
	 *            Current score.
	 */
	public void drawScore(final Screen screen, final int score) {
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.setColor(Color.WHITE);
		String scoreString = String.format("%04d", score);
		backBufferGraphics.drawString(scoreString, screen.getWidth() - 60, 25);
	}

	public void drawScore(final Screen screen, final int score1, final int score2) {
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.setColor(Color.WHITE);
		String scoreString = String.format("%04d", score1);
		String scoreString2 = String.format("%04d", score2);
		backBufferGraphics.drawString(scoreString+"/"+scoreString2, screen.getWidth() - 130, 25);
	}

	/**
	 * Draws number of remaining lives on screen.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param lives
	 *            Current lives.
	 */
	public void drawLives(final Screen screen, final int lives, Color color, final int gamemode) {
		backBufferGraphics.setFont(fontRegular);

		backBufferGraphics.setColor(Color.WHITE);
		backBufferGraphics.drawString(Integer.toString(lives), 20, 25);

		Ship dummyShip = new Ship(0, 0, new Color((int)(color.getRed()*0.7),(int)(color.getGreen()*0.7),(int)(color.getBlue()*0.7)));
		int insert = 40;
		if(gamemode == 1){
			insert += 15;
		}
		int i = 0;
		for (; i < lives; i++) {
			drawEntity(dummyShip, insert + 35 * i, 10);
		}
	}
	public void drawLives(final Screen screen, final int lives, final int lives2, Color color1, Color color2, final int gamemode) {
		backBufferGraphics.setFont(fontRegular);
		if(gamemode == 0){
			backBufferGraphics.setColor(Color.WHITE);
			backBufferGraphics.drawString(Integer.toString(lives), 20, 25);
		}
		else{
			backBufferGraphics.setColor(Color.WHITE);
			backBufferGraphics.drawString(Integer.toString(lives)+"/"+Integer.toString(lives2), 20, 25);
		}

		Ship dummyShip = new Ship(0, 0, new Color((int)(color1.getRed()*0.7),(int)(color1.getGreen()*0.7),(int)(color1.getBlue()*0.7)));
		Ship dummyShip2 = new Ship(0, 0,new Color((int)(color2.getRed()*0.7),(int)(color2.getGreen()*0.7),(int)(color2.getBlue()*0.7)));
		int insert = 40;
		if(gamemode == 1){
			insert += 15;
		}
		int i = 0;
		for (; i < lives; i++) {
			drawEntity(dummyShip, insert + 35 * i, 10);
		}
		for(int j=i;j<i+lives2;j++){
			drawEntity(dummyShip2, insert + 35 * j, 10);
		}

	}

	/**
	 * Draws a thick line from side to side of the screen.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param positionY
	 *            Y coordinate of the line.
	 */
	public void drawHorizontalLine(final Screen screen, final int positionY) {
		backBufferGraphics.setColor(Color.GREEN);
		backBufferGraphics.drawLine(0, positionY, screen.getWidth(), positionY);
		backBufferGraphics.drawLine(0, positionY + 1, screen.getWidth(),
				positionY + 1);
	}

	/**
	 * Draws game title.
	 *
	 * @param screen
	 *            Screen to draw on.
	 */
	public void drawTitle(final Screen screen) {
		String titleString = "Invaders";
		String instructionsString =
				"select with w+s / arrows, confirm with space";

		backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen, instructionsString,
				screen.getHeight() / 2);

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, titleString, screen.getHeight() / 3);
	}

	/**
	 * Draws main menu.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param option
	 *            Option selected.
	 */
	public void drawMenu(final Screen screen, final int option) {
		String playString = "Play";
		String highScoresString = "High scores";
		String settingString = "Setting";
		String exitString = "exit";

		if (option == 2) {
			backBufferGraphics.setColor(Color.GREEN);
		}
		else{
			backBufferGraphics.setColor(Color.WHITE);
		}
		drawCenteredRegularString(screen, playString, screen.getHeight() / 4 * 2 + screen.getHeight()/9);

		if (option == 3) {
			backBufferGraphics.setColor(Color.GREEN);
		}
		else {
			backBufferGraphics.setColor(Color.WHITE);
		}
		drawCenteredRegularString(screen, highScoresString, screen.getHeight() / 4 * 2 + screen.getHeight()/9 + fontRegularMetrics.getHeight() * 2);

		if (option == 4) {
			backBufferGraphics.setColor(Color.GREEN);
		}
		else {
			backBufferGraphics.setColor(Color.WHITE);
		}
		drawCenteredRegularString(screen, settingString, screen.getHeight() / 4 * 2 + screen.getHeight()/9 + fontRegularMetrics.getHeight() * 4);

		if (option == 0) {
			backBufferGraphics.setColor(Color.GREEN);
		}
		else {
			backBufferGraphics.setColor(Color.WHITE);
		}
		drawCenteredRegularString(screen, exitString, screen.getHeight() / 4 * 2 + screen.getHeight()/9 + fontRegularMetrics.getHeight() * 6);
	}

	/**
	 * Draws game results.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param score
	 *            Score obtained.
	 * @param livesRemaining
	 *            Lives remaining when finished.
	 * @param shipsDestroyed
	 *            Total ships destroyed.
	 * @param accuracy
	 *            Total accuracy.
	 * @param isNewRecord
	 *            If the score is a new high score.
	 */
	public void drawResults(final Screen screen, final int score, final int livesRemaining, final int shipsDestroyed, final float accuracy, final boolean isNewRecord) {
		String scoreString = String.format("score %04d", score);
		String livesRemainingString = "lives remaining " + livesRemaining;
		String shipsDestroyedString = "enemies destroyed " + shipsDestroyed;
		String accuracyString = String
				.format("accuracy %.2f%%", accuracy * 100);

		int height = isNewRecord ? 4 : 2;

		backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, scoreString, screen.getHeight()
				/ height);
		drawCenteredRegularString(screen, livesRemainingString,
				screen.getHeight() / height + fontRegularMetrics.getHeight()
						* 2);
		drawCenteredRegularString(screen, shipsDestroyedString,
				screen.getHeight() / height + fontRegularMetrics.getHeight()
						* 4);
		drawCenteredRegularString(screen, accuracyString, screen.getHeight()
				/ height + fontRegularMetrics.getHeight() * 6);
	}

	public void drawResults(final Screen screen, final int score, final int livesRemaining, final int shipsDestroyed, final float accuracy, final boolean isNewRecord,
							final int score2, final int livesRemaining2, final int shipsDestroyed2, final float accuracy2, final boolean isNewRecord2) {
		String scoreString = String.format("score %04d", score);
		String livesRemainingString = "lives remaining " + livesRemaining;
		String shipsDestroyedString = "enemies destroyed " + shipsDestroyed;
		String accuracyString = String.format("accuracy %.2f%%", accuracy * 100);
		String scoreString2 = String.format("score %04d", score2);
		String livesRemainingString2 = "lives remaining " + livesRemaining2;
		String shipsDestroyedString2 = "enemies destroyed " + shipsDestroyed2;
		String accuracyString2 = String.format("accuracy %.2f%%", accuracy2 * 100);

		int height = isNewRecord || isNewRecord2 ? 5 : 3;
		backBufferGraphics.setColor(Color.WHITE);
		double indent = 1.2;
		drawCenteredRegularString(screen, scoreString, screen.getHeight() / height);
		drawCenteredRegularString(screen, livesRemainingString, screen.getHeight() / height + (int)(fontRegularMetrics.getHeight() * 1 * indent));
		drawCenteredRegularString(screen, shipsDestroyedString, screen.getHeight() / height + (int)(fontRegularMetrics.getHeight() * 2 * indent));
		drawCenteredRegularString(screen, accuracyString, screen.getHeight() / height + (int)(fontRegularMetrics.getHeight() * 3 * indent));

		drawCenteredRegularString(screen, scoreString2, screen.getHeight() / height + (int)(fontRegularMetrics.getHeight() * 5 * indent));
		drawCenteredRegularString(screen, livesRemainingString2, screen.getHeight() / height + (int)(fontRegularMetrics.getHeight() * 6 * indent));
		drawCenteredRegularString(screen, shipsDestroyedString2, screen.getHeight() / height + (int)(fontRegularMetrics.getHeight() * 7 * indent));
		drawCenteredRegularString(screen, accuracyString2, screen.getHeight() / height + (int)(fontRegularMetrics.getHeight() * 8 * indent));
	}

	/**
	 * Draws interactive characters for name input.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param name
	 *            Current name selected.
	 * @param nameCharSelected
	 *            Current character selected for modification.
	 */
	public void drawNameInput(final Screen screen, final char[] name, final int nameCharSelected) {
		String newRecordString = "New Record!";
		String introduceNameString = "Introduce name:";

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredRegularString(screen, newRecordString, screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 10);
		backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, introduceNameString, screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 12);

		// 3 letters name.
		int positionX = screen.getWidth()
				/ 2
				- (fontRegularMetrics.getWidths()[name[0]]
				+ fontRegularMetrics.getWidths()[name[1]]
				+ fontRegularMetrics.getWidths()[name[2]]
				+ fontRegularMetrics.getWidths()[' ']) / 2;

		for (int i = 0; i < 3; i++) {
			if (i == nameCharSelected)
				backBufferGraphics.setColor(Color.GREEN);
			else
				backBufferGraphics.setColor(Color.WHITE);

			positionX += fontRegularMetrics.getWidths()[name[i]] / 2;
			positionX = i == 0 ? positionX
					: positionX
					+ (fontRegularMetrics.getWidths()[name[i - 1]]
					+ fontRegularMetrics.getWidths()[' ']) / 2;

			backBufferGraphics.drawString(Character.toString(name[i]),
					positionX,
					screen.getHeight() / 4 + fontRegularMetrics.getHeight()
							* 14);
		}
	}

	public void drawNameInput(final Screen screen, final char[] name1, final char[] name2, final int nameCharSelected1, final int nameCharSelected2, final boolean isNewRecord1, final boolean isNewRecord2, final int select) {

		if(isNewRecord1 && !isNewRecord2){
			String newRecordString = "New Record!";
			String introduceNameString = "Player1 name:";

			backBufferGraphics.setColor(Color.GREEN);
			drawCenteredRegularString(screen, newRecordString, screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 10);
			backBufferGraphics.setColor(Color.WHITE);
			drawCenteredRegularString(screen, introduceNameString, screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 12);

			// 3 letters name.
			int positionX = screen.getWidth()
					/ 2
					- (fontRegularMetrics.getWidths()[name1[0]]
					+ fontRegularMetrics.getWidths()[name1[1]]
					+ fontRegularMetrics.getWidths()[name1[2]]
					+ fontRegularMetrics.getWidths()[' ']) / 2;

			for (int i = 0; i < 3; i++) {
				if (i == nameCharSelected1)
					backBufferGraphics.setColor(Color.GREEN);
				else
					backBufferGraphics.setColor(Color.WHITE);

				positionX += fontRegularMetrics.getWidths()[name1[i]] / 2;
				positionX = i == 0 ? positionX
						: positionX
						+ (fontRegularMetrics.getWidths()[name1[i - 1]]
						+ fontRegularMetrics.getWidths()[' ']) / 2;

				backBufferGraphics.drawString(Character.toString(name1[i]),
						positionX, screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 14);
			}
		}
		if(isNewRecord2 && !isNewRecord1){
			String newRecordString = "New Record!";
			String introduceNameString2 = "Player2 name:";

			backBufferGraphics.setColor(Color.GREEN);
			drawCenteredRegularString(screen, newRecordString, screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 10);
			backBufferGraphics.setColor(Color.WHITE);
			drawCenteredRegularString(screen, introduceNameString2, screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 12);

			// 3 letters name.
			int positionX = screen.getWidth()
					/ 2
					- (fontRegularMetrics.getWidths()[name2[0]]
					+ fontRegularMetrics.getWidths()[name2[1]]
					+ fontRegularMetrics.getWidths()[name2[2]]
					+ fontRegularMetrics.getWidths()[' ']) / 2;

			for (int i = 0; i < 3; i++) {
				if (i == nameCharSelected2)
					backBufferGraphics.setColor(Color.GREEN);
				else
					backBufferGraphics.setColor(Color.WHITE);

				positionX += fontRegularMetrics.getWidths()[name2[i]] / 2;
				positionX = i == 0 ? positionX
						: positionX + (fontRegularMetrics.getWidths()[name2[i - 1]] + fontRegularMetrics.getWidths()[' ']) / 2;

				backBufferGraphics.drawString(Character.toString(name2[i]),
						positionX,
						screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 14);
			}
		}
		if(isNewRecord1 && isNewRecord2){
			String newRecordString = "New Record!";
			backBufferGraphics.setColor(Color.GREEN);
			drawCenteredRegularString(screen, newRecordString, screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 10);
			String introduceNameString1 = "Player1 name:";
			String introduceNameString2 = "Player2 name:";
			backBufferGraphics.drawString(introduceNameString1, (int)(screen.getWidth()*0.25)-fontRegularMetrics.getWidths()[' ']*14, screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 12);
			backBufferGraphics.drawString(introduceNameString2, (int)(screen.getWidth()*0.75)-fontRegularMetrics.getWidths()[' ']*18, screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 12);

			int positionX = screen.getWidth() / 2 - (fontRegularMetrics.getWidths()[name1[0]] + fontRegularMetrics.getWidths()[name1[1]] + fontRegularMetrics.getWidths()[name1[2]] + fontRegularMetrics.getWidths()[' ']) / 2;
			positionX = (int)(positionX*0.48);
			for (int i = 0; i < 3; i++) {
				if (i == nameCharSelected1 && select == 0)
					backBufferGraphics.setColor(Color.GREEN);
				else
					backBufferGraphics.setColor(Color.WHITE);
				positionX += fontRegularMetrics.getWidths()[name1[i]] / 2;
				positionX = i == 0 ? positionX : positionX + (fontRegularMetrics.getWidths()[name1[i - 1]] + fontRegularMetrics.getWidths()[' ']) / 2;
				backBufferGraphics.drawString(Character.toString(name1[i]), positionX, screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 14);
			}

			positionX = screen.getWidth()/2 - (fontRegularMetrics.getWidths()[name2[0]] + fontRegularMetrics.getWidths()[name2[1]] + fontRegularMetrics.getWidths()[name2[2]] + fontRegularMetrics.getWidths()[' ']) / 2;
			positionX = (int)(positionX*1.5);
			for (int i = 0; i < 3; i++) {
				if (i == nameCharSelected2 && select == 1)
					backBufferGraphics.setColor(Color.GREEN);
				else
					backBufferGraphics.setColor(Color.WHITE);
				positionX += fontRegularMetrics.getWidths()[name2[i]] / 2;
				positionX = i == 0 ? positionX : positionX + (fontRegularMetrics.getWidths()[name2[i - 1]] + fontRegularMetrics.getWidths()[' ']) / 2;
				backBufferGraphics.drawString(Character.toString(name2[i]), positionX, screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 14);
			}

		}

	}

	/**
	 * Draws basic content of game over screen.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param acceptsInput
	 *            If the screen accepts input.
	 * @param isNewRecord
	 *            If the score is a new high score.
	 */
	public void drawGameOver(final Screen screen, final boolean acceptsInput, final boolean isNewRecord, final boolean clear) {
		String gameOverString;
		if(clear){
			gameOverString = "Game Clear!";
		}
		else{
			gameOverString = "Game Over";
		}
		String continueOrExitString = "Press Space to play again, Escape to exit";

		int height = isNewRecord ? 4 : 2;

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, gameOverString, screen.getHeight() / height - fontBigMetrics.getHeight() * 2);

		if (acceptsInput) {
			backBufferGraphics.setColor(Color.GREEN);
		}
		else {
			backBufferGraphics.setColor(Color.GRAY);
		}
		drawCenteredRegularString(screen, continueOrExitString, screen.getHeight() / 2 + fontRegularMetrics.getHeight() * 10);
	}
	public void drawGameOver(final Screen screen, final boolean acceptsInput, final boolean isNewRecord1,final boolean isNewRecord2, final int score1, final int score2) {
		String gameOverString;
		if(score1>score2){
			gameOverString = "Player1 WIN!";
		}
		else if(score1<score2){
			gameOverString = "Player2 WIN!";
		}
		else{
			gameOverString = "draw";
		}
		String continueOrExitString = "Press Space to play again, Escape to exit";

		int height = isNewRecord1 || isNewRecord2 ? 4:3;

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, gameOverString, screen.getHeight() / height - fontBigMetrics.getHeight() * 2);

		if (acceptsInput) {
			backBufferGraphics.setColor(Color.GREEN);
		}
		else {
			backBufferGraphics.setColor(Color.GRAY);
		}
		drawCenteredRegularString(screen, continueOrExitString, screen.getHeight() / 2 + fontRegularMetrics.getHeight() * 10);
	}

	/**
	 * Draws high score screen title and instructions.
	 *
	 * @param screen
	 *            Screen to draw on.
	 */

	public void drawHighScoreMenu(final Screen screen,int select, int playermode, int difficulty) {
		String[] difficult = {"easy","normal","hard","extra hard"};
		String[] players = {"single player","two players"};
		String highScoreString = "High Scores";
		String instructionsString = "Space|ESC: Back / ARROWKEY: move";
		String instructionsString2 = "R: Delete Record"; // 리셋 설명 방법 추가
		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, highScoreString, screen.getHeight() / 8);
		backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen, instructionsString, screen.getHeight() / 5);
		drawCenteredRegularString(screen, instructionsString2, screen.getHeight() / 6 + fontRegularMetrics.getHeight()*2);
		if(select == 0) backBufferGraphics.setColor(Color.GREEN);
		else backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, players[playermode], screen.getHeight() / 5 + fontRegularMetrics.getHeight()*3);
		if(select == 1) backBufferGraphics.setColor(Color.GREEN);
		else backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, difficult[difficulty], screen.getHeight() / 5 + fontRegularMetrics.getHeight()*4);
	}

	/**
	 * Draws high scores.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param highScores
	 *            List of high scores.
	 */
	public void drawHighScores(final Screen screen,
							   final List<Score> highScores) {
		backBufferGraphics.setColor(Color.WHITE);
		int i = 0;
		String scoreString = "";

		// null인 경우를 고려해주지 않으면, nullpoint 오류 발생
		if(highScores != null){
			for (Score score : highScores) {
				scoreString = String.format("%s        %04d", score.getName(),
						score.getScore());
				drawCenteredRegularString(screen, scoreString, screen.getHeight() / 5 + fontRegularMetrics.getHeight()*4+ fontRegularMetrics.getHeight() * (i + 1) * 2);
				i++;
			}
		}
	}

	/**
	 * Draws a centered string on regular font.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param string
	 *            String to draw.
	 * @param height
	 *            Height of the drawing.
	 */
	public void drawCenteredRegularString(final Screen screen,
										  final String string, final int height) {
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.drawString(string, screen.getWidth() / 2
				- fontRegularMetrics.stringWidth(string) / 2, height);
	}

	/**
	 * Draws a centered string on big font.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param string
	 *            String to draw.
	 * @param height
	 *            Height of the drawing.
	 */
	public void drawCenteredBigString(final Screen screen, final String string,
									  final int height) {
		backBufferGraphics.setFont(fontBig);
		backBufferGraphics.drawString(string, screen.getWidth() / 2
				- fontBigMetrics.stringWidth(string) / 2, height);
	}

	/**
	 * Countdown to game start.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param level
	 *            Game difficulty level.
	 * @param number
	 *            Countdown number.
	 * @param bonusLife
	 *            Checks if a bonus life is received.
	 */
	public void drawCountDown(final Screen screen, final int level,
							  final int number, final boolean bonusLife) {
		int rectWidth = screen.getWidth();
		int rectHeight = screen.getHeight() / 6;
		backBufferGraphics.setColor(Color.BLACK);
		backBufferGraphics.fillRect(0, screen.getHeight() / 2 - rectHeight / 2,
				rectWidth, rectHeight);
		backBufferGraphics.setColor(Color.GREEN);
		if (number >= 4) {
			if (!bonusLife) {
				drawCenteredBigString(screen, "Level " + level,
						screen.getHeight() / 2
								+ fontBigMetrics.getHeight() / 3);
			} else {
				drawCenteredBigString(screen, "Level " + level
								+ " - Bonus life!",
						screen.getHeight() / 2
								+ fontBigMetrics.getHeight() / 3);
			}
		}
		else if (number != 0) {
			drawCenteredBigString(screen, Integer.toString(number),
					screen.getHeight() / 2 + fontBigMetrics.getHeight() / 3);
		}
		else {
			drawCenteredBigString(screen, "GO!", screen.getHeight() / 2
					+ fontBigMetrics.getHeight() / 3);
		}
	}

	public void drawPause(final Screen screen){
		int rectWidth = screen.getWidth();
		int rectHeight = screen.getHeight()/6;
		backBufferGraphics.setColor(Color.BLACK);
		backBufferGraphics.fillRect(0, screen.getHeight() / 2 - rectHeight / 2, rectWidth, rectHeight);
		backBufferGraphics.setColor(Color.WHITE);
		drawCenteredBigString(screen,"PAUSE",screen.getHeight()/2+fontBigMetrics.getHeight()/5);
		drawCenteredRegularString(screen,"Esc: continue / Q: exit",screen.getHeight()/2+fontBigMetrics.getHeight()/4*4);
	}

	public void drawSetting(final Screen screen){
		String highScoreString = "Setting";
		String instructionsString = "Space/Esc: Back / ArrowKey: Setting";

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, highScoreString, screen.getHeight() / 8);

		backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen, instructionsString,
				screen.getHeight() / 5);
	}
	public void drawSettingMenu(final Screen screen, int select,int difficult, int playermode, int coloridx){
		String[] difficulty = {"easy","normal","hard","extra hard"};
		String[] players = {"single player","two players"};
		Color[] colorSet = {Color.GREEN,Color.YELLOW,
				new Color(92,209,229),new Color(197,254,221),
				new Color(253, 254, 228),new Color(253, 219, 249),
				new Color(208, 219, 249),new Color(255, 251, 209),
				new Color(230, 230, 230),new Color(255,255,255)};
		if(select == 0){
			backBufferGraphics.setColor(Color.GREEN);
		}
		else{
			backBufferGraphics.setColor(Color.WHITE);
		}
		drawCenteredBigString(screen, "Difficulty", screen.getHeight() / 7 * 2 + screen.getHeight()/9 - fontRegularMetrics.getHeight()*1);
		drawCenteredRegularString(screen, difficulty[difficult], screen.getHeight() / 7 * 2 + screen.getHeight()/9 + fontRegularMetrics.getHeight()*1);
		if(select == 1){
			backBufferGraphics.setColor(Color.GREEN);
		}
		else{
			backBufferGraphics.setColor(Color.WHITE);
		}
		drawCenteredBigString(screen, "PlayerMode", screen.getHeight() / 7 * 2 + screen.getHeight()/9 + fontRegularMetrics.getHeight() * 4);
		drawCenteredRegularString(screen, players[playermode], screen.getHeight() / 7 * 2 + screen.getHeight()/9+ fontRegularMetrics.getHeight() * 6);
		if(select == 2){
			backBufferGraphics.setColor(Color.GREEN);
		}
		else{
			backBufferGraphics.setColor(Color.WHITE);
		}
		drawCenteredBigString(screen, "shipsColor", screen.getHeight() / 7 * 2 + screen.getHeight()/9+ fontRegularMetrics.getHeight() * 9);
		Ship dummyShip1 = new Ship(0, 0, new Color((int)(colorSet[coloridx*2].getRed()*0.7),(int)(colorSet[coloridx*2].getGreen()*0.7),(int)(colorSet[coloridx*2].getBlue()*0.7)));
		Ship dummyShip2 = new Ship(0, 0, new Color((int)(colorSet[coloridx*2+1].getRed()*0.7),(int)(colorSet[coloridx*2+1].getGreen()*0.7),(int)(colorSet[coloridx*2+1].getBlue()*0.7)));

		drawEntity(dummyShip1, (int)(screen.getWidth()*0.45)-13, screen.getHeight() / 7 * 2 + screen.getHeight()/9+ fontRegularMetrics.getHeight() * 11);
		drawEntity(dummyShip2, (int)(screen.getWidth()*0.55)-13, screen.getHeight() / 7 * 2 + screen.getHeight()/9+ fontRegularMetrics.getHeight() * 11);
	}
}
