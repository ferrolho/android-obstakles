package states;

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

	public static void renderState() {
		if (currentState != null)
			currentState.render();
	}

	public static void disposeState() {
		if (currentState != null)
			currentState.dispose();
	}

}
