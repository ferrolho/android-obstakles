package states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class StateManager {

	private static State currentState;

	public static void changeState(State state) {
		if (currentState != null)
			currentState.dispose();

		currentState = state;
		state.create();
	}

	public static void resizeState(int width, int height) {
		if (currentState != null)
			currentState.resize(width, height);
	}

	public static void updateState() {
		if (currentState != null)
			currentState.update();
	}

	public static void renderState(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
		if (currentState != null)
			currentState.render(shapeRenderer, spriteBatch);
	}

	public static void disposeState() {
		if (currentState != null)
			currentState.dispose();
	}

}
