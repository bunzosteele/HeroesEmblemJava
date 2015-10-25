package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.HighscoreManager;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class SplashScreen extends ScreenAdapter
{
	HeroesEmblem game;
	boolean touched = false;
	Sprite glitchSprite;

	public SplashScreen(final HeroesEmblem game)
	{
		this.game = game;
		final AtlasRegion glitchRegion = this.game.textureAtlas.findRegion("GlitchIcon_white");	
		glitchSprite = new Sprite(glitchRegion);
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				if(!touched)
					game.setScreen(new MainMenuScreen(game));
			}
		}, 3f, 1f, 0);
	}

	public void draw()
	{
		
		final GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.game.batcher.begin();
		this.game.font.getData().setScale(.45f);
		this.game.font.draw(this.game.batcher, "This game was made live on Twitch!", 0, 3 * Gdx.graphics.getHeight() / 4, Gdx.graphics.getWidth(), 1, false);
		this.game.batcher.draw(glitchSprite, (Gdx.graphics.getWidth() / 2) - ((Gdx.graphics.getHeight() / 2) - 2 * this.game.font.getData().lineHeight) / 2 , Gdx.graphics.getHeight() / 4 + 1 * this.game.font.getData().lineHeight, ((Gdx.graphics.getHeight() / 2) - 2 * this.game.font.getData().lineHeight), ((Gdx.graphics.getHeight() / 2) - 2 * this.game.font.getData().lineHeight));
		this.game.font.draw(this.game.batcher, "Watch future development at", 0, Gdx.graphics.getHeight() / 4 + this.game.font.getData().lineHeight, Gdx.graphics.getWidth(), 1, false);
		this.game.font.draw(this.game.batcher, "www.Twitch.tv/Bunzosteele", 0, Gdx.graphics.getHeight() / 4, Gdx.graphics.getWidth(), 1, false);
		this.game.font.getData().setScale(.33f);
		this.game.batcher.end();
	}

	@Override
	public void render(final float delta)
	{
		try
		{
			this.update();
		} catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.draw();
	}

	public void update() throws IOException
	{		
		if (Gdx.input.justTouched())
		{
			this.touched = true;
			game.setScreen(new MainMenuScreen(game));
		}
	}
}
