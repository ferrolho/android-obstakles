package states;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import utilities.Touch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import entities.Obstacle;
import entities.Player;
import game.Game;

public class GamePlayState extends State implements InputProcessor {

	public static Sound bumpSound, thumpSound;

	private float obstacleSpawnProb;
	private Array<Color> obstacleColors;

	private Player player;
	// private Rectangle teste2, teste3;
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
		obstacleColors.add(Color.NAVY);
		obstacleColors.add(Color.ORANGE);
		obstacleColors.add(Color.PINK);
		obstacleColors.add(Color.PURPLE);
		obstacleColors.add(Color.RED);
		obstacleColors.add(Color.TEAL);
		obstacleColors.add(Color.YELLOW);

		player = new Player(Color.BLACK);
		// teste2 = new Rectangle(200, 200, 200, 200, Color.BLACK);
		// teste3 = new Rectangle(500, 500, 200, 200, Color.GREEN);
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
		obstacleSpawnProb += 0.02;

		boolean movingLeft = false, movingRight = false;

		Iterator<Entry<Integer, Touch>> it = touches.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, Touch> pairs = it.next();
			Touch touch = pairs.getValue();

			if (touch.touched) {
				// teste2.position.x = touch.touchX;
				// teste2.position.y = Game.screenDimension.y - touch.touchY;

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

		// teste2.update();
		// teste3.update();

		if (MathUtils.random(100) <= obstacleSpawnProb)
			spawnObstacle();

		Rectangle playerRectangle = new Rectangle(player.position.x,
				player.position.y, player.size, player.size);

		Iterator<Obstacle> iterator = obstacles.iterator();
		while (iterator.hasNext()) {
			Obstacle obstacle = iterator.next();

			obstacle.update();

			// check player collision
			if (playerRectangle.overlaps(obstacle)) {
				thumpSound.play();
				StateManager.changeState(new GameOverState());
			}

			if (obstacle.y + obstacle.height < -0.25 * Game.screenDimension.y)
				iterator.remove();
		}
	}

	@Override
	public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
		Game.clearScreen(255, 255, 255, 1);

		player.draw(shapeRenderer);
		// teste2.draw(shapeRenderer);
		// teste3.draw(shapeRenderer);

		for (Obstacle obstacle : obstacles)
			obstacle.draw(shapeRenderer);
	}

	@Override
	public void dispose() {
		bumpSound.dispose();
		thumpSound.dispose();
	}

	private void spawnObstacle() {
		float x = MathUtils.random(Game.screenDimension.x);
		float w = MathUtils.random(0.01f * Game.screenDimension.x,
				0.2f * Game.screenDimension.x);
		float h = MathUtils.random(0.01f * Game.screenDimension.x,
				0.2f * Game.screenDimension.x);
		Color color = obstacleColors.get(MathUtils
				.random(obstacleColors.size - 1));

		obstacles.add(new Obstacle(x, Game.screenDimension.y, w, h, color));
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
