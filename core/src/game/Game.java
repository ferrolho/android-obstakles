package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import states.MainMenuState;
import states.StateManager;
import utilities.FontManager;
import utilities.RGBA;

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

import entities.Obstacle;
import entities.Player;

public class Game extends ApplicationAdapter {

	private static final String PREFERENCES_ID = "scores";
	private static final String BEST_ID = "best";
	private static final String BEST_SUBMITTED_ID = "best-submitted";
	private static final String TOTAL_WALL_COLLISIONS_ID = "total-wall-collisions";

	public static final int FPS = 60;
	public static final float SPF = 1.0f / FPS;

	public static ActionResolver actionResolver;

	public static Vector2 screenDimension;
	public static float gravity;
	public static int touchTolerance;

	public static FontManager fontManager;
	public static PolygonSpriteBatch polygonSpriteBatch;
	public static ShapeRenderer shapeRenderer;
	public static SpriteBatch spriteBatch;

	public static Sound bumpSound, thumpSound, clickSound;

	public static List<Color> obstacleColors;
	public static List<Obstacle> obstacles, spareObstacles;

	public static float lastScore, bestScore, bestScoreSubmitted;
	public static int lastWallCollisions, totalWallCollisions;

	public Game(ActionResolver actionResolver) {
		Game.actionResolver = actionResolver;
	}

	@Override
	public void create() {
		Gdx.input.setCatchBackKey(true);

		updateScreenDimension();
		gravity = 0.002f * screenDimension.x;
		touchTolerance = (int) (0.01f * Game.screenDimension.x);

		fontManager = new FontManager();
		polygonSpriteBatch = new PolygonSpriteBatch();
		shapeRenderer = new ShapeRenderer();
		spriteBatch = new SpriteBatch();

		// load the shock sound effect
		bumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bump.wav"));
		thumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/thump.wav"));
		clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/click.wav"));

		setColorScheme();
		obstacles = new ArrayList<Obstacle>();
		spareObstacles = new ArrayList<Obstacle>();

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

		obstacleColors = null;
		obstacles = null;
		spareObstacles = null;

		bumpSound.dispose();
		thumpSound.dispose();
		clickSound.dispose();

		fontManager.dispose();
		polygonSpriteBatch.dispose();
		shapeRenderer.dispose();
		spriteBatch.dispose();

		screenDimension = null;
	}

	private void setColorScheme() {
		Player.color = new RGBA(33, 50, 64, 1);

		obstacleColors = new ArrayList<Color>();

		obstacleColors.add(new RGBA(16, 200, 205, 1)); // blue
		obstacleColors.add(new RGBA(255, 51, 51, 1)); // red
		obstacleColors.add(new RGBA(255, 204, 0, 1)); // yellow
		obstacleColors.add(new RGBA(51, 255, 51, 1)); // green
	}

	public static void udpateScore() {
		Preferences prefs = Gdx.app.getPreferences(PREFERENCES_ID);
		bestScore = prefs.getFloat(BEST_ID, 0);
		bestScoreSubmitted = prefs.getFloat(BEST_SUBMITTED_ID, 0);
		totalWallCollisions = prefs.getInteger(TOTAL_WALL_COLLISIONS_ID, 0);

		// update local best score
		if (lastScore > bestScore) {
			bestScore = lastScore;

			prefs.putFloat(BEST_ID, bestScore);
		}

		// update local number of collisions with the walls
		totalWallCollisions += lastWallCollisions;
		prefs.putInteger(TOTAL_WALL_COLLISIONS_ID, totalWallCollisions);

		// if signed in game services
		if (actionResolver.getSignedInGPGS()) {
			// submit last score
			actionResolver.submitScoreGPGS(lastScore, lastWallCollisions,
					totalWallCollisions);

			// if local best is better than online best
			if (bestScore > bestScoreSubmitted) {
				// submit local best score
				actionResolver.submitScoreGPGS(bestScore, lastWallCollisions,
						totalWallCollisions);

				bestScoreSubmitted = bestScore;
				prefs.putFloat(BEST_SUBMITTED_ID, bestScoreSubmitted);
			}
		}

		prefs.flush();
	}

	private void updateScreenDimension() {
		if (screenDimension == null)
			screenDimension = new Vector2();

		screenDimension.x = Gdx.graphics.getWidth();
		screenDimension.y = Gdx.graphics.getHeight();
	}

	public static final void clearScreen(int red, int green, int blue,
			float alpha) {
		Gdx.gl.glClearColor((float) red / 255, (float) green / 255,
				(float) blue / 255, alpha);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}

	public static void clearObstacles() {
		obstacles.clear();
		spareObstacles.clear();
	}

	public static void spawnObstacle() {
		if (spareObstacles.isEmpty())
			obstacles.add(new Obstacle());
		else {
			Obstacle obstacle = spareObstacles.remove(0);
			obstacle.reset();

			obstacles.add(obstacle);
		}
	}

	public static void updateObstacles() {
		Iterator<Obstacle> iterator = obstacles.iterator();

		while (iterator.hasNext()) {
			Obstacle obstacle = iterator.next();

			obstacle.update();

			if (-obstacle.positionRelativeToSpawn.y > Obstacle.lifeSpanDistance) {
				spareObstacles.add(obstacle);
				iterator.remove();
			}
		}
	}

	public static void drawObstacles() {
		for (Obstacle obstacle : obstacles)
			obstacle.draw();
	}

}
