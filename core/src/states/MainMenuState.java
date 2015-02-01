package states;

import game.Game;
import utilities.Label;
import utilities.Touch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class MainMenuState extends State implements InputProcessor {

	private final static Label titleLbl = new Label("Obstakles",
			(int) Math.round(0.16 * Gdx.graphics.getWidth()), 0.8f);

	public static Label infoLbl = new Label("",
			(int) Math.round(0.06 * Gdx.graphics.getWidth()), 0.45f);

	private final static Label copyrightLbl = new Label(
			"© 2014 Henrique Ferrolho", (int) Math.round(0.04 * Gdx.graphics
					.getWidth()), 0.1f);

	private Touch touch;
	private Color filterColor, fontColor;

	private float obstacleSpawnProb;
	private float infoDisplacement, maxInfoDisplacement, infoSpeed;

	private boolean transitioningToPlayState;
	private Vector2 animationPos;
	private float animationRadius, maxRadius;

	@Override
	public void create() {
		Gdx.input.setInputProcessor(this);

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

		touch = new Touch();
		filterColor = new Color(1, 1, 1, 0.7f);
		fontColor = Color.DARK_GRAY;

		Game.clearObstacles();
		obstacleSpawnProb = 30;

		infoDisplacement = 0;
		maxInfoDisplacement = 0.04f * Game.screenDimension.y;
		infoSpeed = 0.0015f * Game.screenDimension.y;

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

		// update falling objects
		if (MathUtils.random(100) <= obstacleSpawnProb)
			Game.spawnObstacle();

		// update info animation
		infoDisplacement += infoSpeed;
		if (Math.abs(infoDisplacement) > maxInfoDisplacement)
			infoSpeed = -infoSpeed;

		// update transition animation
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

		renderFilter();
		renderTitle();
		renderInfo();
		renderCopyright();

		renderTransitionAnimation();
	}

	@Override
	public void dispose() {
		Game.fontManager.removeFont(titleLbl.size);
		Game.fontManager.removeFont(infoLbl.size);
		Game.fontManager.removeFont(copyrightLbl.size);
	}

	private void renderFilter() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		Game.shapeRenderer.begin(ShapeType.Filled);

		Game.shapeRenderer.setColor(filterColor);
		Game.shapeRenderer.rect(0, 0, Game.screenDimension.x,
				Game.screenDimension.y);

		Game.shapeRenderer.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	private void renderTitle() {
		BitmapFont font = Game.fontManager.getFont(titleLbl.size);

		int x = (int) (Game.screenDimension.x / 2 - font
				.getBounds(titleLbl.text).width / 2);
		int y = (int) (titleLbl.position * Game.screenDimension.y + font
				.getBounds(titleLbl.text).height / 2);

		Game.spriteBatch.begin();

		font.setColor(fontColor);
		font.draw(Game.spriteBatch, titleLbl.text, x, y);

		Game.spriteBatch.end();
	}

	private void renderInfo() {
		BitmapFont font = Game.fontManager.getFont(infoLbl.size);

		int x = (int) (Game.screenDimension.x / 2 - font
				.getBounds(infoLbl.text).width / 2);
		int y = (int) (infoLbl.position * Game.screenDimension.y
				+ font.getBounds(infoLbl.text).height / 2 + infoDisplacement);

		Game.spriteBatch.begin();

		font.setColor(fontColor);
		font.draw(Game.spriteBatch, infoLbl.text, x, y);

		Game.spriteBatch.end();
	}

	private void renderCopyright() {
		BitmapFont font = Game.fontManager.getFont(copyrightLbl.size);

		int x = (int) (Game.screenDimension.x / 2 - font
				.getBounds(copyrightLbl.text).width / 2);
		int y = (int) (copyrightLbl.position * Game.screenDimension.y + font
				.getBounds(copyrightLbl.text).height / 2);

		Game.spriteBatch.begin();

		font.setColor(fontColor);
		font.draw(Game.spriteBatch, copyrightLbl.text, x, y);

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
		if (Math.abs(touch.position.x - screenX) < Game.touchTolerance
				&& Math.abs(touch.position.y - screenY) < Game.touchTolerance) {
			if (!transitioningToPlayState) {
				animationPos.x = screenX;
				animationPos.y = Game.screenDimension.y - screenY;

				Game.clickSound.play();
				transitioningToPlayState = true;
			}
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
