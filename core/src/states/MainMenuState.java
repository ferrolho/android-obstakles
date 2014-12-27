package states;

import game.Game;
import utilities.FontManager;
import utilities.Touch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

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

	private float infoDisplacement, maxInfoDisplacement, infoSpeed;

	private Touch touch;

	@Override
	public void create() {
		Gdx.input.setInputProcessor(this);

		infoDisplacement = 0;
		maxInfoDisplacement = 0.02f * Game.screenDimension.y;
		infoSpeed = 0.0005f * Game.screenDimension.y;

		touch = new Touch();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void update() {
		infoDisplacement += infoSpeed;
		if (Math.abs(infoDisplacement) > maxInfoDisplacement)
			infoSpeed = -infoSpeed;
	}

	@Override
	public void render() {
		Game.clearScreen(255, 255, 255, 1);

		renderTitle();
		renderInfo();
		renderCopyright();
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

		// TODO change this
		StateManager.changeState(new GamePlayState());

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
