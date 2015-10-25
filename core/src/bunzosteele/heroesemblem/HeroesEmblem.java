package bunzosteele.heroesemblem;

import bunzosteele.heroesemblem.view.SplashScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
	public Preferences settings;
	public AdsController adsController;
	
	public HeroesEmblem(AdsController adsController){
            if (adsController != null) {
                this.adsController = adsController;
            } else {
                this.adsController = new DummyAdsController();
            }
	}

	@Override
	public void create()
	{
		this.batcher = new SpriteBatch();
		this.shapeRenderer = new ShapeRenderer();
		final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("madness.ttf"));
		final FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = Gdx.graphics.getHeight() / 4;
		this.font = generator.generateFont(parameter);
		generator.dispose();
		this.textureAtlas = new TextureAtlas(Gdx.files.internal("HeroesEmblem.pack"));
		settings = Gdx.app.getPreferences("HeroesEmblemSettings");
		this.setScreen(new SplashScreen(this));
	}

	@Override
	public void render()
	{
		super.render();
	}
}
