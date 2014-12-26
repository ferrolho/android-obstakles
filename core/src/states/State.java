package states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class State {

	public abstract void create();

	public abstract void resize(int width, int height);

	public abstract void update();

	public abstract void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch);

	public abstract void dispose();

}
