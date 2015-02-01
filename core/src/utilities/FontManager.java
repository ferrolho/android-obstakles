package utilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public final class FontManager {

	private FreeTypeFontGenerator generator;
	private FreeTypeFontParameter param;
	private Map<Integer, BitmapFont> fonts;

	public FontManager() {
		generator = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/helvetica-neue-ultra-light.ttf"));
		param = new FreeTypeFontParameter();
		fonts = new HashMap<Integer, BitmapFont>();
	}

	public BitmapFont getFont(int size) {
		if (generator == null || param == null || fonts == null)
			return null;

		if (fonts.get(size) == null) {
			param.size = size;
			fonts.put(size, generator.generateFont(param));
		}

		return fonts.get(size);
	}

	public void removeFont(int size) {
		BitmapFont font = fonts.remove(size);

		if (font != null)
			font.dispose();
	}

	public void dispose() {
		generator.dispose();

		Iterator<Entry<Integer, BitmapFont>> it = fonts.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, BitmapFont> pairs = it.next();
			pairs.getValue().dispose();
		}
	}

}
