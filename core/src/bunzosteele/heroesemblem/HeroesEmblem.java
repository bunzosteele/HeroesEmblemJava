package bunzosteele.heroesemblem;

import bunzosteele.heroesemblem.view.MainMenuScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HeroesEmblem extends Game
{
	public SpriteBatch batcher;
	public ShapeRenderer shapeRenderer;
	public BitmapFont font;
	public TextureAtlas textureAtlas;

	@Override
	public void create()
	{
		this.batcher = new SpriteBatch();
		this.shapeRenderer = new ShapeRenderer();
		final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("LeagueGothic-CondensedRegular.otf"));
		final FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = Gdx.graphics.getHeight() / 4;
		this.font = generator.generateFont(parameter);
		generator.dispose();
		this.textureAtlas = new TextureAtlas(Gdx.files.internal("HeroesEmblem.pack"));
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render()
	{
		super.render();
	}
}
