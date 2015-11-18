package bunzosteele.heroesemblem.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import bunzosteele.heroesemblem.HeroesEmblem;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 1440;
		config.width = 2560;
		new LwjglApplication(new HeroesEmblem(null, null), config);
	}
}
