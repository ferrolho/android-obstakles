package states;

import entities.Obstacle;
import entities.Player;
import game.Game;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import utilities.Touch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class GamePlayState extends State implements InputProcessor {

	private float obstacleSpawnProb;

	private int scoreFontSize;
	private float elapsedTime;

	private boolean gameOver;

	@Override
	public void create() {
		obstacleSpawnProb = 1;

		scoreFontSize = (int) (0.05 * Game.screenDimension.x);
		elapsedTime = 0;

		Game.player = new Player(Color.BLUE);
		Game.clearObstacles();

		Gdx.input.setInputProcessor(this);
		for (int i = 0; i < 5; i++) {
			touches.put(i, new Touch());
		}

		gameOver = false;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void update() {
		if (gameOver)
			StateManager.changeState(new GameOverState());
		else {
			elapsedTime += Gdx.graphics.getDeltaTime();
			obstacleSpawnProb += 0.008;

			boolean movingLeft = false, movingRight = false;

			switch (Gdx.app.getType()) {
			case Android:
				Iterator<Entry<Integer, Touch>> it;

				it = touches.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<Integer, Touch> pairs = it.next();
					Touch touch = pairs.getValue();

					if (touch.touched) {
						if (touch.position.x < Game.screenDimension.x / 2)
							movingLeft = true;
						else
							movingRight = true;
					}
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

			Game.player.update(movingLeft, movingRight);

			Game.updateObstacles();

			if (MathUtils.random(100) <= obstacleSpawnProb)
				Game.spawnObstacle();

			// check player collision
			for (Obstacle obstacle : Game.obstacles) {
				// skip obstacles that are clearly not colliding
				if (Obstacle.spawnHeight + obstacle.distanceTraveled.y
						- Obstacle.maxDistToCenter > Game.player.y
						+ Game.player.height)
					continue;
				else if (obstacle.centerSpawn.x + Obstacle.maxDistToCenter < Game.player.x)
					continue;
				else if (obstacle.centerSpawn.x - Obstacle.maxDistToCenter > Game.player.x
						+ Game.player.width)
					continue;

				if (Game.player.overlaps(obstacle)) {
					Game.thumpSound.play();

					Game.lastScore = elapsedTime;
					gameOver = true;

					break;
				}
			}
		}
	}

	@Override
	public void render() {
		Game.clearScreen(255, 255, 255, 1);

		Game.player.draw();

		Game.drawObstacles();

		renderScore();
	}

	private void renderScore() {
		BitmapFont font = Game.fontManager.getFont(scoreFontSize);

		DecimalFormat f = new DecimalFormat("##0.00");
		String elapsedTimeStr = f.format(elapsedTime);

		float x = Game.screenDimension.x - 1.1f
				* font.getBounds(elapsedTimeStr).width;
		float y = Game.screenDimension.y - 0.3f
				* font.getBounds(elapsedTimeStr).height;

		Game.spriteBatch.begin();
		font.setColor(0, 0, 0, 1);
		font.draw(Game.spriteBatch, elapsedTimeStr, x, y);
		Game.spriteBatch.end();
	}

	@Override
	public void dispose() {
	}

	private Map<Integer, Touch> touches = new HashMap<Integer, Touch>();
	private Array<Integer> keys = new Array<Integer>();

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
			break;

		default:
			break;
		}

		if (!keys.contains(keycode, true))
			keys.add(keycode);

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
		if (pointer < 5) {
			touches.get(pointer).position.x = screenX;
			touches.get(pointer).position.y = screenY;
			touches.get(pointer).touched = true;
		}

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (pointer < 5) {
			touches.get(pointer).position.x = 0;
			touches.get(pointer).position.y = 0;
			touches.get(pointer).touched = false;
		}

		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
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
