package game.desktop;

import game.Game;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Obstakles";

		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		config.width = screenDimension.width;
		config.height = screenDimension.height;

		// fullscreen
		config.fullscreen = true;

		// vSync
		config.vSyncEnabled = true;

		new LwjglApplication(new Game(), config);
	}
}
