package utilities;

import com.badlogic.gdx.graphics.Color;

public class RGB extends Color {

	/**
	 * Constructor, sets the components of the color.
	 * 
	 * @param r
	 *            Red component, in the range [0, 255]
	 * @param g
	 *            Green component, in the range [0, 255]
	 * @param b
	 *            Blue component, in the range [0, 255]
	 * @param a
	 *            Alpha component, in the range [0, 1]
	 */
	public RGB(int r, int g, int b, float a) {
		super((float) r / 255, (float) g / 255, (float) b / 255, a);
	}

}
