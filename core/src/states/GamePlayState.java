package states;

import java.text.DecimalFormat;

import utilities.Touch;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import entities.Obstacle;
import entities.Player;
import game.Game;

public class GamePlayState extends State implements InputProcessor {

	private static final DecimalFormat decimalFormat = new DecimalFormat(
			"##0.00");

	public static Player player;

	private final int screenHalfWidth;

	private final BitmapFont scoreFont;
	private final int scoreRightBorderX, scoreY;

	private Touch touch;
	private Array<Integer> keys;

	private float timeAccumulator, elapsedTime;
	private float obstacleSpawnProb;

	private boolean gameOver;

	public GamePlayState() {
		final int screenWidth = (int) Game.screenDimension.x;

		screenHalfWidth = screenWidth / 2;

		scoreFont = Game.fontManager.getFont((int) (0.08f * screenWidth));

		final int scoreMargin = (int) (0.02f * screenWidth);
		scoreRightBorderX = screenWidth - scoreMargin;
		scoreY = (int) (Game.screenDimension.y - scoreMargin);
	}

	@Override
	public void create() {
		Gdx.input.setInputProcessor(this);

		if (Gdx.app.getType() == ApplicationType.Android)
			touch = new Touch();

		keys = new Array<Integer>();

		player = new Player();

		timeAccumulator = 0;
		elapsedTime = 0;
		obstacleSpawnProb = 1;
		Game.lastWallCollisions = 0;

		gameOver = false;

		Game.clearObstacles();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void update() {
		final float deltaTime = Gdx.graphics.getDeltaTime();

		timeAccumulator += deltaTime;
		elapsedTime += deltaTime;

		final int elapsedFrames = MathUtils.floor(timeAccumulator / Game.SPF);
		timeAccumulator %= Game.SPF;

		for (int i = 0; i < elapsedFrames; i++) {
			if (gameOver)
				StateManager.changeState(new GameOverState());
			else {
				obstacleSpawnProb += 0.008;

				boolean movingLeft = false, movingRight = false;

				switch (Gdx.app.getType()) {
				case Android:
					if (touch.touched) {
						if (touch.x < screenHalfWidth)
							movingLeft = true;
						else
							movingRight = true;
					}

					break;

				case Desktop:
					if (keys.contains(Keys.LEFT, true)
							|| keys.contains(Keys.A, true))
						movingLeft = true;

					if (keys.contains(Keys.RIGHT, true)
							|| keys.contains(Keys.D, true))
						movingRight = true;

					break;

				default:
					break;
				}

				player.update(movingLeft, movingRight);

				Game.updateObstacles();

				if (MathUtils.random(100) <= obstacleSpawnProb)
					Game.spawnObstacle();

				final float heightFilter = player.y + player.height
						+ Obstacle.maxDistToCenter;
				final float leftFilter = player.x - Obstacle.maxDistToCenter;
				final float rightFilter = player.x + player.width
						+ Obstacle.maxDistToCenter;

				// check player collision
				for (Obstacle obstacle : Game.obstacles) {
					// skip obstacles that are clearly not colliding
					if (Obstacle.spawnHeight
							+ obstacle.positionRelativeToSpawn.y > heightFilter)
						continue;
					else if (obstacle.centerSpawn.x < leftFilter)
						continue;
					else if (obstacle.centerSpawn.x > rightFilter)
						continue;
					else if (player.overlaps(obstacle)) {
						Game.thumpSound.play();

						Game.lastScore = elapsedTime;
						gameOver = true;

						break;
					}
				}
			}
		}
	}

	@Override
	public void render() {
		Game.clearScreen(255, 255, 255, 1);

		player.draw();

		Game.drawObstacles();

		renderScore();
	}

	@Override
	public void dispose() {
		touch = null;
		keys = null;
	}

	private void renderScore() {
		String elapsedTimeStr = decimalFormat.format(elapsedTime);

		final int scoreX = (int) (scoreRightBorderX - scoreFont
				.getBounds(elapsedTimeStr).width);

		Game.spriteBatch.begin();
		scoreFont.setColor(Color.BLACK);
		scoreFont.draw(Game.spriteBatch, elapsedTimeStr, scoreX, scoreY);
		Game.spriteBatch.end();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (Gdx.app.getType()) {
		case Android:
			if (keycode == Keys.BACK)
				StateManager.changeState(new MainMenuState());
			break;

		case Desktop:
			if (keycode == Keys.ESCAPE)
				StateManager.changeState(new MainMenuState());

			if (!keys.contains(keycode, true))
				keys.add(keycode);

			break;

		default:
			break;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		keys.removeValue(keycode, true);

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		touch.x = screenX;
		touch.y = screenY;
		touch.touched = true;

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		touch.touched = false;

		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		touch.x = screenX;
		touch.y = screenY;
		touch.touched = true;

		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
