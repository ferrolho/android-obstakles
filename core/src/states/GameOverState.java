package states;

import game.Game;

import java.text.DecimalFormat;

import utilities.FontManager;
import utilities.Touch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class GameOverState extends State implements InputProcessor {

	private final static String GAME_OVER = "Game Over";
	private final static int GAME_OVER_SIZE = (int) Math
			.round(0.1 * Game.screenDimension.x);
	private final static float GAME_OVER_POS = 0.8f;

	private final static int SCORE_SIZE = (int) Math
			.round(0.06 * Game.screenDimension.x);
	private final static float SCORE_POS = 0.6f;

	private final static String BUTTON_TEXT = "Main Menu";
	private final static int BUTTON_FONT_SIZE = (int) Math
			.round(0.05 * Game.screenDimension.x);
	private final static float BUTTON_POS = 0.2f;

	private final static String PREFERENCES_ID = "scores";
	private final static String BEST_ID = "best";

	private Preferences prefs;

	public static float lastScore;
	public static float bestScore;

	private Touch touch;

	private Color filterColor;

	private Rectangle button;

	@Override
	public void create() {
		Gdx.input.setInputProcessor(this);

		prefs = Gdx.app.getPreferences(PREFERENCES_ID);
		bestScore = prefs.getFloat(BEST_ID, 0);

		if (lastScore > bestScore) {
			bestScore = lastScore;

			prefs.putFloat(BEST_ID, bestScore);
			prefs.flush();
		}

		touch = new Touch();

		filterColor = new Color(0, 0, 0, 0.3f);

		// building button
		BitmapFont font = FontManager.getFont(BUTTON_FONT_SIZE);

		float w = 1.2f * font.getBounds(BUTTON_TEXT).width;
		float h = font.getBounds(BUTTON_TEXT).height + 0.2f
				* font.getBounds(BUTTON_TEXT).width;

		float x = Game.screenDimension.x / 2 - w / 2;
		float y = BUTTON_POS * Game.screenDimension.y - h / 2;

		button = new Rectangle(x, y, w, h);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void update() {
	}

	@Override
	public void render() {
		Game.clearScreen(255, 255, 255, 1);

		Game.player.draw();

		Game.drawObstacles();

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Game.shapeRenderer.begin(ShapeType.Filled);
		Game.shapeRenderer.setColor(filterColor);
		Game.shapeRenderer.rect(0, 0, Game.screenDimension.x,
				Game.screenDimension.y);
		Game.shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		renderTitle();
		renderScore();

		renderButton();
	}

	private void renderTitle() {
		BitmapFont font = FontManager.getFont(GAME_OVER_SIZE);

		float x = Game.screenDimension.x / 2 - font.getBounds(GAME_OVER).width
				/ 2;
		float y = GAME_OVER_POS * Game.screenDimension.y
				+ font.getBounds(GAME_OVER).height / 2;

		Game.spriteBatch.begin();
		font.setColor(Color.WHITE);
		font.draw(Game.spriteBatch, GAME_OVER, x, y);
		Game.spriteBatch.end();
	}

	private void renderScore() {
		BitmapFont font = FontManager.getFont(SCORE_SIZE);

		DecimalFormat f = new DecimalFormat("##0.00");
		String lastScoreStr = "Score: " + f.format(lastScore);
		String bestScoreStr = "Best: " + f.format(bestScore);

		float lastX = Game.screenDimension.x / 2
				- font.getBounds(lastScoreStr).width / 2;
		float lastY = SCORE_POS * Game.screenDimension.y
				+ font.getBounds(lastScoreStr).height / 2;

		float bestX = Game.screenDimension.x / 2
				- font.getBounds(bestScoreStr).width / 2;
		float bestY = SCORE_POS * Game.screenDimension.y
				+ font.getBounds(bestScoreStr).height / 2 - 2.5f
				* font.getBounds(lastScoreStr).height;

		Game.spriteBatch.begin();
		if (lastScore == bestScore)
			font.setColor(0, 1, 0, 1);
		else
			font.setColor(Color.WHITE);
		font.draw(Game.spriteBatch, lastScoreStr, lastX, lastY);
		font.draw(Game.spriteBatch, bestScoreStr, bestX, bestY);
		Game.spriteBatch.end();
	}

	private void renderButton() {
		// button background
		Game.shapeRenderer.begin(ShapeType.Filled);
		Game.shapeRenderer.setColor(Color.WHITE);
		Game.shapeRenderer
				.rect(button.x, button.y, button.width, button.height);
		Game.shapeRenderer.end();

		// button text
		BitmapFont font = FontManager.getFont(BUTTON_FONT_SIZE);

		float x = Game.screenDimension.x / 2
				- font.getBounds(BUTTON_TEXT).width / 2;
		float y = BUTTON_POS * Game.screenDimension.y
				+ font.getBounds(BUTTON_TEXT).height / 2;

		Game.spriteBatch.begin();
		font.setColor(0, 0, 0, 1);
		font.draw(Game.spriteBatch, BUTTON_TEXT, x, y);
		Game.spriteBatch.end();

		// button borders
		Game.shapeRenderer.begin(ShapeType.Line);
		Game.shapeRenderer.setColor(Color.BLACK);
		Game.shapeRenderer
				.rect(button.x, button.y, button.width, button.height);
		Game.shapeRenderer.end();
	}

	@Override
	public void dispose() {
	}

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
		touch.position.x = 0;
		touch.position.y = 0;
		touch.touched = false;

		if (this.button.contains(screenX, Game.screenDimension.y - screenY))
			StateManager.changeState(new MainMenuState());

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
