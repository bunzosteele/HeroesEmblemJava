package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen extends ScreenAdapter
{
	HeroesEmblem game;
	OrthographicCamera camera;
	Rectangle newGameBounds;
	Vector3 touchpoint;

	public MainMenuScreen(final HeroesEmblem game)
	{
		this.game = game;
		this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.newGameBounds = new Rectangle(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		this.touchpoint = new Vector3();
	}

	public void draw()
	{
		final GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.camera.update();
		this.game.shapeRenderer.begin(ShapeType.Filled);
		this.game.shapeRenderer.setColor(0, 1, 0, 1);
		this.game.shapeRenderer.rect(this.newGameBounds.x, this.newGameBounds.y, this.newGameBounds.width, this.newGameBounds.height);
		this.game.shapeRenderer.end();
		final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("LeagueGothic-CondensedRegular.otf"));
		final FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 144;
		final BitmapFont font = generator.generateFont(parameter);
		generator.dispose();

		this.game.batcher.begin();
		font.draw(this.game.batcher, "New Game", this.newGameBounds.x, this.newGameBounds.y + ((3 * this.newGameBounds.height) / 4), this.newGameBounds.width, 1, false);
		this.game.batcher.end();
		font.dispose();
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
			this.touchpoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			if (this.newGameBounds.contains(this.touchpoint.x, this.touchpoint.y))
			{
				this.game.setScreen(new ShopScreen(this.game));
				return;
			}
		}
	}
}
