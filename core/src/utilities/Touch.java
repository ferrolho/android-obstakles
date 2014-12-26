package utilities;

import com.badlogic.gdx.math.Vector2;

public class Touch {

	public Vector2 position;
	public boolean touched;

	public Touch() {
		position = new Vector2();
		this.touched = false;
	}

	public Touch(float x, float y, boolean touched) {
		position = new Vector2(x, y);
		this.touched = touched;
	}

}
