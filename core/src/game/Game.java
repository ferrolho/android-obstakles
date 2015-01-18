package game;

import java.util.Iterator;

import states.MainMenuState;
import states.StateManager;
import utilities.FontManager;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
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

	public static float GRAVITY;
	public static Vector2 screenDimension;

	public static ShapeRenderer shapeRenderer;
	public static SpriteBatch spriteBatch;
	public static PolygonSpriteBatch polygonSpriteBatch;

	FontManager fontManager;

	private static Preferences prefs;
	private final static String PREFERENCES_ID = "scores";
	private final static String BEST_ID = "best";

	public static float lastScore;
	public static float bestScore;

	public static Sound bumpSound, thumpSound;

	public static Array<Color> obstacleColors;
	public static Array<Obstacle> obstacles;

	public static Player player;

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Gdx.input.setCatchBackKey(true);

		switch (Gdx.app.getType()) {
		case Android:
			MainMenuState.infoLbl.text = "Tap anywhere to begin";
			break;

		case Desktop:
			MainMenuState.infoLbl.text = "Click anywhere to begin";
			break;

		default:
			break;
		}

		screenDimension = new Vector2();
		updateScreenDimension();
		GRAVITY = 0.002f * screenDimension.x;

		shapeRenderer = new ShapeRenderer();
		spriteBatch = new SpriteBatch();
		polygonSpriteBatch = new PolygonSpriteBatch();

		fontManager = new FontManager();

		// load the shock sound effect
		bumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bump.wav"));
		thumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/thump.wav"));

		obstacleColors = new Array<Color>();
		obstacleColors.add(Color.CYAN);
		obstacleColors.add(Color.GREEN);
		obstacleColors.add(Color.MAGENTA);
		obstacleColors.add(Color.ORANGE);
		obstacleColors.add(Color.RED);
		obstacleColors.add(Color.YELLOW);

		obstacles = new Array<Obstacle>();

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
		StateManager.disposeState();

		spriteBatch.dispose();
		fontManager.dispose();
		bumpSound.dispose();
		thumpSound.dispose();
	}

	public static void udpateScore() {
		prefs = Gdx.app.getPreferences(PREFERENCES_ID);
		bestScore = prefs.getFloat(BEST_ID, 0);

		if (lastScore > bestScore) {
			bestScore = lastScore;

			prefs.putFloat(BEST_ID, bestScore);
			prefs.flush();
		}
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
