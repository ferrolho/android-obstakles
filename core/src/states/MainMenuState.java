package states;

import game.Game;
import utilities.FontManager;
import utilities.Touch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class MainMenuState extends State implements InputProcessor {

	private final static int TITLE_SIZE = (int) Math
			.round(0.15 * Game.screenDimension.x);
	private final static int INFO_SIZE = (int) Math
			.round(0.05 * Game.screenDimension.x);
	private final static int CREDITS_SIZE = (int) Math
			.round(0.03 * Game.screenDimension.x);

	private final static float TITLE_POS = 0.8f;
	private final static float INFO_POS = 0.45f;
	private final static float CREDITS_POS = 0.1f;

	private float obstacleSpawnProb;

	private float infoDisplacement, maxInfoDisplacement, infoSpeed;

	private Touch touch;

	private boolean transitioningToPlayState;
	private Vector2 animationPos;
	private float animationRadius, maxRadius;

	@Override
	public void create() {
		Gdx.input.setInputProcessor(this);

		Game.clearObstacles();
		obstacleSpawnProb = 30;

		infoDisplacement = 0;
		maxInfoDisplacement = 0.04f * Game.screenDimension.y;
		infoSpeed = 0.0015f * Game.screenDimension.y;

		touch = new Touch();

		transitioningToPlayState = false;
		animationPos = new Vector2();
		animationRadius = 0;
		maxRadius = (float) Math.sqrt(Game.screenDimension.x
				* Game.screenDimension.x + Game.screenDimension.y
				* Game.screenDimension.y);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void update() {
		Game.updateObstacles();

		if (MathUtils.random(100) <= obstacleSpawnProb)
			Game.spawnObstacle();

		infoDisplacement += infoSpeed;
		if (Math.abs(infoDisplacement) > maxInfoDisplacement)
			infoSpeed = -infoSpeed;

		if (transitioningToPlayState) {
			animationRadius += 0.01 * Game.screenDimension.x + 0.15
					* animationRadius;

			if (animationRadius > maxRadius)
				StateManager.changeState(new GamePlayState());
		}
	}

	@Override
	public void render() {
		Game.clearScreen(255, 255, 255, 1);

		Game.drawObstacles();

		renderTitle();
		renderInfo();
		renderCopyright();

		renderTransitionAnimation();
	}

	@Override
	public void dispose() {
	}

	private void renderTitle() {
		BitmapFont font = FontManager.getFont(TITLE_SIZE);

		float x = Game.screenDimension.x / 2 - font.getBounds(Game.TITLE).width
				/ 2;
		float y = TITLE_POS * Game.screenDimension.y
				+ font.getBounds(Game.TITLE).height / 2;

		Game.spriteBatch.begin();

		font.setColor(0, 0, 0, 1);
		font.draw(Game.spriteBatch, Game.TITLE, x, y);

		Game.spriteBatch.end();
	}

	private void renderInfo() {
		BitmapFont font = FontManager.getFont(INFO_SIZE);

		float x = Game.screenDimension.x / 2 - font.getBounds(Game.INFO).width
				/ 2;
		float y = INFO_POS * Game.screenDimension.y
				+ font.getBounds(Game.INFO).height / 2 + infoDisplacement;

		Game.spriteBatch.begin();

		font.setColor(0, 0, 0, 1);
		font.draw(Game.spriteBatch, Game.INFO, x, y);

		Game.spriteBatch.end();
	}

	private void renderCopyright() {
		BitmapFont font = FontManager.getFont(CREDITS_SIZE);

		float x = Game.screenDimension.x / 2
				- font.getBounds(Game.CREDITS).width / 2;
		float y = CREDITS_POS * Game.screenDimension.y
				+ font.getBounds(Game.CREDITS).height / 2;

		Game.spriteBatch.begin();

		font.setColor(0, 0, 0, 1);
		font.draw(Game.spriteBatch, Game.CREDITS, x, y);

		Game.spriteBatch.end();
	}

	private void renderTransitionAnimation() {
		Game.shapeRenderer.begin(ShapeType.Filled);

		Game.shapeRenderer.setColor(Color.WHITE);
		Game.shapeRenderer.circle(animationPos.x, animationPos.y,
				animationRadius);

		Game.shapeRenderer.end();
	}

	@Override
	public boolean keyDown(int keycode) {

		switch (Gdx.app.getType()) {
		case Android:
			if (keycode == Keys.BACK)
				Gdx.app.exit();
			break;

		case Desktop:
			if (keycode == Keys.SPACE)
				transitioningToPlayState = true;
			else if (keycode == Keys.ESCAPE)
				Gdx.app.exit();
			break;

		default:
			break;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		touch.position.x = screenX;
		touch.position.y = screenY;
		touch.touched = true;

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (!transitioningToPlayState) {
			animationPos.x = screenX;
			animationPos.y = Game.screenDimension.y - screenY;

			transitioningToPlayState = true;
		}

		touch.position.x = 0;
		touch.position.y = 0;
		touch.touched = false;

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
