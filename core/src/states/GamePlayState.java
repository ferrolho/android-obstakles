package states;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import utilities.FontManager;
import utilities.Touch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import entities.Obstacle;
import entities.Player;
import game.Game;

public class GamePlayState extends State implements InputProcessor {

	public static Sound bumpSound, thumpSound;

	private float obstacleSpawnProb;
	public static Array<Color> obstacleColors;

	private int scoreFontSize;
	private float elapsedTime;

	private Player player;
	private Array<Obstacle> obstacles;

	@Override
	public void create() {
		// load the shock sound effect
		bumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bump.wav"));
		thumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/thump.wav"));

		obstacleSpawnProb = 1;
		obstacleColors = new Array<Color>();
		obstacleColors.add(Color.BLUE);
		obstacleColors.add(Color.CYAN);
		obstacleColors.add(Color.GREEN);
		obstacleColors.add(Color.MAGENTA);
		obstacleColors.add(Color.ORANGE);
		obstacleColors.add(Color.RED);
		obstacleColors.add(Color.YELLOW);

		scoreFontSize = (int) (0.05 * Game.screenDimension.x);
		elapsedTime = 0;

		player = new Player(Color.BLACK);
		obstacles = new Array<Obstacle>();

		Gdx.input.setInputProcessor(this);
		for (int i = 0; i < 5; i++) {
			touches.put(i, new Touch());
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void update() {
		elapsedTime += Gdx.graphics.getDeltaTime();
		obstacleSpawnProb += 0.02;

		boolean movingLeft = false, movingRight = false;

		Iterator<Entry<Integer, Touch>> it = touches.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, Touch> pairs = it.next();
			Touch touch = pairs.getValue();

			if (touch.touched) {
				if (touch.position.x < Game.screenDimension.x / 2) {
					movingLeft = true;
					Gdx.app.debug("Touch", "move left");
				} else {
					movingRight = true;
					Gdx.app.debug("Touch", "move right");
				}
			}
		}

		player.update(movingLeft, movingRight);

		if (MathUtils.random(100) <= obstacleSpawnProb)
			obstacles.add(new Obstacle());

		Iterator<Obstacle> iterator = obstacles.iterator();
		while (iterator.hasNext()) {
			Obstacle obstacle = iterator.next();

			obstacle.update();

			// check player collision
			if (player.overlaps(obstacle)) {
				thumpSound.play();
				GameOverState.lastScore = elapsedTime;
				StateManager.changeState(new GameOverState());
			}

			if (Math.abs(obstacle.displacement.y) > 1.25 * Game.screenDimension.y)
				iterator.remove();
		}
	}

	@Override
	public void render() {
		Game.clearScreen(255, 255, 255, 1);

		player.draw();

		for (Obstacle obstacle : obstacles)
			obstacle.draw();

		renderScore();
	}

	private void renderScore() {
		BitmapFont font = FontManager.getFont(scoreFontSize);

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
		bumpSound.dispose();
		thumpSound.dispose();
	}

	private Map<Integer, Touch> touches = new HashMap<Integer, Touch>();

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.BACK)
			StateManager.changeState(new MainMenuState());

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
