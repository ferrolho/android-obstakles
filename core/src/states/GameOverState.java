package states;

import game.Game;

import java.text.DecimalFormat;

import utilities.FontManager;
import utilities.Touch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class GameOverState extends State implements InputProcessor {

	private final static String GAME_OVER = "Game Over";
	private final static int GAME_OVER_SIZE = (int) Math
			.round(0.1 * Game.screenDimension.x);
	private final static float GAME_OVER_POS = 0.8f;

	private final static int SCORE_SIZE = (int) Math
			.round(0.05 * Game.screenDimension.x);
	private final static float SCORE_POS = 0.5f;

	private final static String PREFERENCES_ID = "scores";
	private final static String BEST_ID = "best";

	private Preferences prefs;

	public static float lastScore;
	public static float bestScore;

	private Touch touch;

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
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		Game.clearScreen(255, 255, 255, 1);

		renderTitle();
		renderScore();
	}

	private void renderTitle() {
		BitmapFont font = FontManager.getFont(GAME_OVER_SIZE);

		float x = Game.screenDimension.x / 2 - font.getBounds(GAME_OVER).width
				/ 2;
		float y = GAME_OVER_POS * Game.screenDimension.y
				+ font.getBounds(GAME_OVER).height / 2;

		Game.spriteBatch.begin();
		font.setColor(0, 0, 0, 1);
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
			font.setColor(0, 0, 0, 1);
		font.draw(Game.spriteBatch, lastScoreStr, lastX, lastY);
		font.draw(Game.spriteBatch, bestScoreStr, bestX, bestY);
		Game.spriteBatch.end();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		touch.position.x = screenX;
		touch.position.y = screenY;
		touch.touched = true;

		// TODO change this
		StateManager.changeState(new MainMenuState());

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		touch.position.x = 0;
		touch.position.y = 0;
		touch.touched = false;

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
