package states;

import game.Game;
import utilities.FontManager;
import utilities.Touch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameOverState extends State implements InputProcessor {

	public final static String GAME_OVER = "Game Over";
	private final static float GAME_OVER_POS = 0.8f;

	private Touch touch;

	@Override
	public void create() {
		Gdx.input.setInputProcessor(this);
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
	public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
		Game.clearScreen(255, 255, 255, 1);

		renderTitle(spriteBatch);
	}

	private void renderTitle(SpriteBatch spriteBatch) {
		int fontSize = (int) (0.1 * Game.screenDimension.x);
		BitmapFont font = FontManager.getFont(fontSize);

		float x = Game.screenDimension.x / 2 - font.getBounds(GAME_OVER).width
				/ 2;
		float y = GAME_OVER_POS * Game.screenDimension.y
				+ font.getBounds(GAME_OVER).height / 2;

		spriteBatch.begin();
		font.setColor(0, 0, 0, 1);
		font.draw(spriteBatch, GAME_OVER, x, y);
		spriteBatch.end();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
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

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		touch.position.x = 0;
		touch.position.y = 0;
		touch.touched = false;

		// TODO temp, change this
		StateManager.changeState(new MainMenuState());

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
