package game;

import java.util.Iterator;

import states.GameOverState;
import states.MainMenuState;
import states.StateManager;
import utilities.FontManager;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import entities.Obstacle;
import entities.Player;

public class Game extends ApplicationAdapter {

	public final static String TITLE = "Obstakles";
	public final static String INFO = "Tap anywhere to begin";
	public final static String CREDITS = "© 2014 Henrique Ferrolho";

	public static float GRAVITY;
	public static Vector2 screenDimension;

	public static ShapeRenderer shapeRenderer;
	public static SpriteBatch spriteBatch;
	public static PolygonSpriteBatch polygonSpriteBatch;

	FontManager fontManager;

	public static Array<Color> obstacleColors;
	public static Array<Obstacle> obstacles;

	public static Player player;

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Gdx.input.setCatchBackKey(true);

		screenDimension = new Vector2();
		updateScreenDimension();
		GRAVITY = 0.002f * screenDimension.x;

		shapeRenderer = new ShapeRenderer();
		spriteBatch = new SpriteBatch();
		polygonSpriteBatch = new PolygonSpriteBatch();

		fontManager = new FontManager();

		obstacleColors = new Array<Color>();
		obstacleColors.add(Color.CYAN);
		obstacleColors.add(Color.GREEN);
		obstacleColors.add(Color.MAGENTA);
		obstacleColors.add(Color.ORANGE);
		obstacleColors.add(Color.RED);
		obstacleColors.add(Color.YELLOW);

		obstacles = new Array<Obstacle>();

		loadBestScore();

		StateManager.changeState(new MainMenuState());
	}

	@Override
	public void resize(int width, int height) {
		updateScreenDimension();

		StateManager.resizeState(width, height);
	}

	@Override
	public void render() {
		StateManager.updateState();

		StateManager.renderState();
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		fontManager.dispose();
		StateManager.disposeState();
	}

	private void loadBestScore() {
		GameOverState.bestScore = 0;
	}

	public static final void clearScreen(float red, float green, float blue,
			float alpha) {
		Gdx.gl.glClearColor(red / 255.0f, green / 255.0f, blue / 255.0f, alpha);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}

	private void updateScreenDimension() {
		screenDimension.x = Gdx.graphics.getWidth();
		screenDimension.y = Gdx.graphics.getHeight();
	}

	public static void clearObstacles() {
		obstacles.clear();
	}

	public static void spawnObstacle() {
		obstacles.add(new Obstacle());
	}

	public static void updateObstacles() {
		Iterator<Obstacle> iterator = obstacles.iterator();

		while (iterator.hasNext()) {
			Obstacle obstacle = iterator.next();

			obstacle.update();

			if (Math.abs(obstacle.displacement.y) > 1.25 * screenDimension.y)
				iterator.remove();
		}
	}

	public static void drawObstacles() {
		for (Obstacle obstacle : obstacles)
			obstacle.draw();
	}

}
