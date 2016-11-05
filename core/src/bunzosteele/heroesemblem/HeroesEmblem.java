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

public class HeroesEmblem extends Game
{
	public SpriteBatch batcher;
	public ShapeRenderer shapeRenderer;
	public SpriteHolder sprites;
	public BitmapFont font;
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
		parameter.size = Gdx.graphics.getHeight() / 4;
		this.font = generator.generateFont(parameter);
		generator.dispose();
		FileHandle textureFile = Gdx.files.internal("HeroesEmblem.pack");
		this.textureAtlas = new TextureAtlas(textureFile);
        this.sprites = new SpriteHolder(this);
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
