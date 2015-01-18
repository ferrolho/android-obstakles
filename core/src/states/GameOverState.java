package states;

import game.Game;

import java.text.DecimalFormat;

import utilities.FontManager;
import utilities.Label;
import utilities.Touch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class GameOverState extends State implements InputProcessor {

	private final static Label gameOverLbl = new Label("Game Over",
			(int) Math.round(0.1 * Game.screenDimension.x), 0.8f);

	private final static Label scoreLbl = new Label("",
			(int) Math.round(0.06 * Game.screenDimension.x), 0.6f);

	private final static Label buttonLbl = new Label("Main Menu",
			(int) Math.round(0.05 * Game.screenDimension.x), 0.2f);

	private Touch touch;

	private Color filterColor;

	private Rectangle button;

	@Override
	public void create() {
		Gdx.input.setInputProcessor(this);

		Game.udpateScore();

		touch = new Touch();

		filterColor = new Color(0, 0, 0, 0.3f);

		createReturnToMainMenuButton();
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

		renderFilter();

		renderTitle();
		renderScore();
		renderButton();
	}

	@Override
	public void dispose() {
	}

	private void createReturnToMainMenuButton() {
		BitmapFont font = FontManager.getFont(buttonLbl.size);

		float w = 1.2f * font.getBounds(buttonLbl.text).width;
		float h = font.getBounds(buttonLbl.text).height + 0.2f
				* font.getBounds(buttonLbl.text).width;

		float x = Game.screenDimension.x / 2 - w / 2;
		float y = buttonLbl.position * Game.screenDimension.y - h / 2;

		button = new Rectangle(x, y, w, h);
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
		BitmapFont font = FontManager.getFont(gameOverLbl.size);

		float x = Game.screenDimension.x / 2
				- font.getBounds(gameOverLbl.text).width / 2;
		float y = gameOverLbl.position * Game.screenDimension.y
				+ font.getBounds(gameOverLbl.text).height / 2;

		Game.spriteBatch.begin();

		font.setColor(Color.WHITE);
		font.draw(Game.spriteBatch, gameOverLbl.text, x, y);

		Game.spriteBatch.end();
	}

	private void renderScore() {
		BitmapFont font = FontManager.getFont(scoreLbl.size);

		DecimalFormat f = new DecimalFormat("##0.00");
		String lastScoreStr = "Score: " + f.format(Game.lastScore);
		String bestScoreStr = "Best: " + f.format(Game.bestScore);

		float lastX = Game.screenDimension.x / 2
				- font.getBounds(lastScoreStr).width / 2;
		float lastY = scoreLbl.position * Game.screenDimension.y
				+ font.getBounds(lastScoreStr).height / 2;

		float bestX = Game.screenDimension.x / 2
				- font.getBounds(bestScoreStr).width / 2;
		float bestY = scoreLbl.position * Game.screenDimension.y
				+ font.getBounds(bestScoreStr).height / 2 - 2.5f
				* font.getBounds(lastScoreStr).height;

		Game.spriteBatch.begin();

		if (Game.lastScore == Game.bestScore)
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
		BitmapFont font = FontManager.getFont(buttonLbl.size);

		float x = Game.screenDimension.x / 2
				- font.getBounds(buttonLbl.text).width / 2;
		float y = buttonLbl.position * Game.screenDimension.y
				+ font.getBounds(buttonLbl.text).height / 2;

		Game.spriteBatch.begin();

		font.setColor(0, 0, 0, 1);
		font.draw(Game.spriteBatch, buttonLbl.text, x, y);

		Game.spriteBatch.end();

		// button borders
		Game.shapeRenderer.begin(ShapeType.Line);

		Game.shapeRenderer.setColor(Color.BLACK);
		Game.shapeRenderer
				.rect(button.x, button.y, button.width, button.height);

		Game.shapeRenderer.end();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (Gdx.app.getType()) {
		case Android:
			if (keycode == Keys.BACK)
				StateManager.changeState(new MainMenuState());
			break;

		case Desktop:
			if (keycode == Keys.ESCAPE || keycode == Keys.SPACE)
				StateManager.changeState(new MainMenuState());
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
