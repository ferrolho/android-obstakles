package game;

import java.util.Iterator;

import states.MainMenuState;
import states.StateManager;
import utilities.FontManager;
import utilities.RGB;

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

	public final static int FPS = 60;
	public final static float SPF = 1.0f / FPS;

	private final static String PREFERENCES_ID = "scores";
	private final static String BEST_ID = "best";
	private final static String BEST_SUBMITTED_ID = "best-submitted";

	public static Vector2 screenDimension;
	public static float GRAVITY;
	public static int touchTolerance;

	public static ActionResolver actionResolver;

	public static FontManager fontManager;
	public static PolygonSpriteBatch polygonSpriteBatch;
	public static ShapeRenderer shapeRenderer;
	public static SpriteBatch spriteBatch;

	public static Sound bumpSound, thumpSound, clickSound;

	public static float lastScore, bestScore, bestScoreSubmitted;

	public static Player player;

	public static Array<Color> obstacleColors;
	public static Array<Obstacle> obstacles;

	public Game(ActionResolver actionResolver) {
		Game.actionResolver = actionResolver;
	}

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

		updateScreenDimension();
		GRAVITY = 0.002f * screenDimension.x;
		touchTolerance = (int) (0.01 * Game.screenDimension.x);

		fontManager = new FontManager();
		polygonSpriteBatch = new PolygonSpriteBatch();
		shapeRenderer = new ShapeRenderer();
		spriteBatch = new SpriteBatch();

		// load the shock sound effect
		bumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bump.wav"));
		thumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/thump.wav"));
		clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/click.wav"));

		useColorScheme(3);
		obstacles = new Array<Obstacle>();

		// go to main menu
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

		bumpSound.dispose();
		thumpSound.dispose();
		clickSound.dispose();

		fontManager.dispose();
		polygonSpriteBatch.dispose();
		shapeRenderer.dispose();
		spriteBatch.dispose();
	}

	private void useColorScheme(int colorSchemeNo) {
		switch (colorSchemeNo) {
		case 3:
			Player.color = new RGB(33, 50, 64, 1);

			obstacleColors = new Array<Color>();

			obstacleColors.add(new RGB(16, 200, 205, 1)); // blue
			obstacleColors.add(new RGB(255, 51, 51, 1)); // red
			obstacleColors.add(new RGB(255, 204, 0, 1)); // yellow
			obstacleColors.add(new RGB(51, 255, 51, 1)); // green

			break;

		case 2:
			Player.color = new RGB(66, 60, 64, 1);

			obstacleColors = new Array<Color>();

			obstacleColors.add(new RGB(197, 170, 245, 1));
			obstacleColors.add(new RGB(163, 203, 241, 1));
			obstacleColors.add(new RGB(121, 191, 161, 1));
			obstacleColors.add(new RGB(245, 163, 82, 1));
			obstacleColors.add(new RGB(251, 115, 116, 1));

			break;

		case 1:
		default:
			Player.color = Color.BLUE;

			obstacleColors = new Array<Color>();

			obstacleColors.add(Color.CYAN);
			obstacleColors.add(Color.GREEN);
			obstacleColors.add(Color.MAGENTA);
			obstacleColors.add(Color.ORANGE);
			obstacleColors.add(Color.RED);
			obstacleColors.add(Color.YELLOW);

			break;
		}
	}

	public static void udpateScore() {
		Preferences prefs = Gdx.app.getPreferences(PREFERENCES_ID);
		bestScore = prefs.getFloat(BEST_ID, 0);
		bestScoreSubmitted = prefs.getFloat(BEST_SUBMITTED_ID, 0);

		// update local best score
		if (lastScore > bestScore) {
			bestScore = lastScore;

			prefs.putFloat(BEST_ID, bestScore);
			prefs.flush();
		}

		// if signed in game services
		if (actionResolver.getSignedInGPGS()) {
			// submit last score
			actionResolver.submitScoreGPGS(lastScore);

			// if local best is better than online best
			if (bestScore > bestScoreSubmitted) {
				// submit local best score
				actionResolver.submitScoreGPGS(bestScore);

				bestScoreSubmitted = bestScore;
				prefs.putFloat(BEST_SUBMITTED_ID, bestScoreSubmitted);
				prefs.flush();
			}
		}
	}

	private void updateScreenDimension() {
		if (screenDimension == null)
			screenDimension = new Vector2();

		screenDimension.x = Gdx.graphics.getWidth();
		screenDimension.y = Gdx.graphics.getHeight();
	}

	public static final void clearScreen(float red, float green, float blue,
			float alpha) {
		Gdx.gl.glClearColor(red / 255.0f, green / 255.0f, blue / 255.0f, alpha);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
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

			if (Math.abs(obstacle.distanceTraveled.y) > screenDimension.y
					+ 0.2f * Game.screenDimension.x)
				iterator.remove();
		}
	}

	public static void drawObstacles() {
		for (Obstacle obstacle : obstacles)
			obstacle.draw();
	}

}
