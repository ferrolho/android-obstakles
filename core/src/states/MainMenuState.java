package states;

import game.Game;
import utilities.FontManager;
import utilities.Touch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MainMenuState extends State implements InputProcessor {

	private final static float TITLE_POS = 0.8f;
	private final static float CREDITS_POS = 0.2f;

	private Touch touch;

	@Override
	public void create() {
		Gdx.input.setInputProcessor(this);
		touch = new Touch();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void update() {
	}

	@Override
	public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
		Game.clearScreen(255, 255, 255, 1);

		renderTitle(spriteBatch);
		renderCopyright(spriteBatch);
	}

	private void renderTitle(SpriteBatch spriteBatch) {
		int fontSize = (int) (0.1 * Game.screenDimension.x);
		BitmapFont font = FontManager.getFont(fontSize);

		float x = Game.screenDimension.x / 2 - font.getBounds(Game.TITLE).width
				/ 2;
		float y = TITLE_POS * Game.screenDimension.y
				+ font.getBounds(Game.TITLE).height / 2;

		spriteBatch.begin();
		font.setColor(0, 0, 0, 1);
		font.draw(spriteBatch, Game.TITLE, x, y);
		spriteBatch.end();
	}

	private void renderCopyright(SpriteBatch spriteBatch) {
		int fontSize = (int) (0.02 * Game.screenDimension.x);
		BitmapFont font = FontManager.getFont(fontSize);

		float x = Game.screenDimension.x / 2
				- font.getBounds(Game.CREDITS).width / 2;
		float y = CREDITS_POS * Game.screenDimension.y
				+ font.getBounds(Game.CREDITS).height / 2;

		spriteBatch.begin();
		font.setColor(0, 0, 0, 1);
		font.draw(spriteBatch, Game.CREDITS, x, y);
		spriteBatch.end();
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.BACK)
			Gdx.app.exit();

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
		StateManager.changeState(new GamePlayState());

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
