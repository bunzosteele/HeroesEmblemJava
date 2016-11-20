package bunzosteele.heroesemblem;

import java.io.IOException;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.SaveManager;
import bunzosteele.heroesemblem.model.ShopState;
import bunzosteele.heroesemblem.view.SplashScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class HeroesEmblem extends Game
{
	public SpriteBatch batcher;
	public ShapeRenderer shapeRenderer;
	public SpriteHolder sprites;
	public BitmapFont font;
	public BitmapFont projectionFont;
	public TextureAtlas textureAtlas;
	public Preferences settings;
	public AdsController adsController;
	public GameServicesController gameServicesController;
	public boolean isQuitting = false;
	public BattleState battleState = null;
	public ShopState shopState = null;
	
	public HeroesEmblem(AdsController adsController, GameServicesController analyticsController){
        if (adsController != null) {
            this.adsController = adsController;
        } else {
            this.adsController = new DummyAdsController();
        }
        if (analyticsController != null) {
            this.gameServicesController = analyticsController;
        } else {
            this.gameServicesController = new DummyGameServicesController();
        }
	}

	@Override
	public void create()
	{
		Gdx.input.setCatchBackKey(true);
		this.batcher = new SpriteBatch();
		this.shapeRenderer = new ShapeRenderer();
		final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("alagard.ttf"));
		final FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = Gdx.graphics.getHeight() / 9;
		this.font = generator.generateFont(parameter);
		generator.dispose();
		final FreeTypeFontGenerator projectionGenerator = new FreeTypeFontGenerator(Gdx.files.internal("slkscr.ttf"));
		final FreeTypeFontParameter projectionParameter = new FreeTypeFontParameter();
		projectionParameter.size = (int) (Gdx.graphics.getHeight() * .01767f);
		this.projectionFont = projectionGenerator.generateFont(projectionParameter);
		projectionGenerator.dispose();
		FileHandle textureFile = Gdx.files.internal("HeroesEmblem.pack");
		this.textureAtlas = new TextureAtlas(textureFile);
        try {
			this.sprites = new SpriteHolder(this);
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (ReflectionException e1) {
			e1.printStackTrace();
		}
		settings = Gdx.app.getPreferences("HeroesEmblemSettings");
		boolean dataLoaded = false;
		try {
			dataLoaded = this.LoadData();
		} catch (IOException e) {
			e.printStackTrace();
			SaveManager.EraseSaveData();
		}
		if(!dataLoaded)
			this.setScreen(new SplashScreen(this));
	}

	@Override
	public void render()
	{
		super.render();
	}
	
	public void SaveData(){
		if(this.battleState != null){
			SaveManager.SaveGame(this.battleState);
		}else if(this.shopState != null){
			SaveManager.SaveGame(this.shopState);
		}else{
			SaveManager.EraseSaveData();
		}
	}
	
	public boolean LoadData() throws IOException{
		return SaveManager.LoadGame(this);
	}
}
