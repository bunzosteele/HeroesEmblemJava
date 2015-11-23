package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.MusicManager;
import bunzosteele.heroesemblem.model.ShopState;
import bunzosteele.heroesemblem.model.Units.LocationDto;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitDto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class ShopScreen extends ScreenAdapter
{

	HeroesEmblem game;
	ShopState state;
	ShopStatusPanel shopStatus;
	StockWindow stockWindow;
	ShopUnitStatusPanel unitStatus;
	ShopControls shopControls;
	ShopUnitInfoPanel unitInfo;
	ShopkeeperPanel shopkeeperPanel;

	public ShopScreen(final HeroesEmblem game) throws IOException
	{
		this.state = new ShopState(game);
		this.InitializeShopScreen(game);
		game.adsController.hideBannerAd();
	}

	public ShopScreen(final HeroesEmblem game, final ShopState state)
	{
		this.state = state;
		this.InitializeShopScreen(game);
		game.adsController.hideBannerAd();
	}

	public void draw() throws IOException
	{
		final GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.game.batcher.begin();
		if(this.state.isInspecting){
			this.unitInfo.draw();
		}else if(this.state.isShopkeeperPanelDisplayed){
			this.shopkeeperPanel.draw();
		}else{
			this.stockWindow.draw();
		}
		this.shopStatus.draw();
		this.unitStatus.draw();
		this.shopControls.draw();
		this.game.batcher.end();
	}

	private void InitializeShopScreen(final HeroesEmblem game)
	{
		this.game = game;
		this.game.isQuitting = false;
		int sideWidth = Gdx.graphics.getWidth() / 6;
		int controlHeight = Gdx.graphics.getHeight() / 6;
		int windowWidth = Gdx.graphics.getWidth() - sideWidth;
		int windowHeight = Gdx.graphics.getHeight() - controlHeight;
		this.shopStatus = new ShopStatusPanel(game, this.state, sideWidth, windowHeight, controlHeight);
		this.stockWindow = new StockWindow(game, this.state, windowWidth - sideWidth, windowHeight, sideWidth, controlHeight);
		this.unitStatus = new ShopUnitStatusPanel(game, this.state, sideWidth, windowHeight, windowWidth, controlHeight);
		this.shopControls = new ShopControls(game, this.state, sideWidth, windowWidth - sideWidth, controlHeight);
		this.unitInfo = new ShopUnitInfoPanel(game, this.state, windowWidth - sideWidth, windowHeight, sideWidth, controlHeight);
		this.shopkeeperPanel= new ShopkeeperPanel(game, this.state, windowWidth - sideWidth, windowHeight, sideWidth, controlHeight);
		MusicManager.PlayShopMusic(this.game.settings.getFloat("musicVolume", .25f));
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
		try
		{
			this.draw();
		} catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void show()
	{
		if(this.game.isQuitting){
			for(Unit unit : state.roster){
				state.graveyard.add(generateUnitDto(unit));
			}
			this.game.setScreen(new GameOverScreen(game, state.roundsSurvived, state.graveyard));
		}
	}
	
	private UnitDto generateUnitDto(Unit deceased){
		UnitDto unitDto = new UnitDto();
		unitDto.type = deceased.type.toString();
		unitDto.name = deceased.name;
		unitDto.attack = deceased.attack;
		unitDto.defense = deceased.defense;
		unitDto.evasion = deceased.evasion;
		unitDto.accuracy = deceased.accuracy;
		unitDto.movement = deceased.movement;
		unitDto.maximumHealth = deceased.maximumHealth;
		unitDto.level = deceased.level;
		if(deceased.ability == null){
			unitDto.ability = "None";
		}else{
			unitDto.ability = deceased.ability.displayName;
		}
		unitDto.unitsKilled = deceased.unitsKilled;
		unitDto.damageDealt = deceased.damageDealt;
		LocationDto location = new LocationDto();
		location.battlefieldId = Integer.MIN_VALUE;
		location.x = -1;
		location.y = -1;
		unitDto.locationKilled = location;
		unitDto.roundKilled = this.state.roundsSurvived;
		unitDto.isMale = deceased.isMale;
		unitDto.backStory = deceased.backStory;
		return unitDto;
	}


	public void update() throws IOException
	{
		if (Gdx.input.justTouched())
		{
			if (!this.state.isInspecting && !this.state.isShopkeeperPanelDisplayed && this.stockWindow.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
			{
				this.stockWindow.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			}else if (this.shopStatus.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
			{
				this.shopStatus.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			} else if (this.unitStatus.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
			{
				this.unitStatus.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			} else if (this.shopControls.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
			{
				this.shopControls.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			} else if (this.state.isInspecting && this.unitInfo.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
			{
				this.unitInfo.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			} else if (this.state.isShopkeeperPanelDisplayed && this.unitInfo.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
			{
				this.shopkeeperPanel.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			}	
		}else if((Gdx.input.isKeyPressed(Keys.BACK) || Gdx.input.isKeyJustPressed(Keys.BACKSPACE)) && SettingsScreen.backEnabled){
			this.game.setScreen(new SettingsScreen(this.game, this.game.getScreen(), true));
		}
	}
}
