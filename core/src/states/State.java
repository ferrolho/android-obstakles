package states;

public abstract class State {

	public abstract void create();

	public abstract void resize(int width, int height);

	public abstract void update();

	public abstract void render();

	public abstract void dispose();

}
