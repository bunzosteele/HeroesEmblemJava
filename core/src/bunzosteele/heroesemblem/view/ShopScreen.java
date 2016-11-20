package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.BattleState;
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
	StockWindow stockWindow;
	ShopkeeperPanel shopkeeperPanel;
	ShopPanel shopPanel;

	public ShopScreen(final HeroesEmblem game) throws IOException
	{
		this.state = new ShopState(game);
		game.shopState = state;
		game.battleState = null;
		game.adsController.showInterstitialAd();
		InitializeShopScreen(game);
	}

	public ShopScreen(final HeroesEmblem game, final BattleState battleState) throws IOException
	{
		this.state = new ShopState(battleState);
		game.shopState = this.state;
		game.battleState = null;
		InitializeShopScreen(game);
	}
	
	public ShopScreen(final HeroesEmblem game, final ShopState shopState) throws IOException
	{
		this.state = shopState;
		game.shopState = state;
		game.battleState = null;
		InitializeShopScreen(game);
	}

	public void draw() throws IOException
	{
		final GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.shapeRenderer.begin(ShapeType.Filled);
		shopPanel.drawBackground();
		game.shapeRenderer.end();
		
		game.batcher.begin();
		shopkeeperPanel.draw();
		stockWindow.draw();
		shopPanel.draw();
		game.batcher.end();
	}

	private void InitializeShopScreen(final HeroesEmblem game)
	{
		this.game = game;
		game.isQuitting = false;
		int sideWidth = (Gdx.graphics.getWidth() / 16) * 3;
		int windowWidth = Gdx.graphics.getWidth() - sideWidth * 2;
		int windowHeight = Gdx.graphics.getHeight();
		this.stockWindow = new StockWindow(game, state, windowWidth, windowHeight, sideWidth, 0);
		this.shopkeeperPanel= new ShopkeeperPanel(game, state, sideWidth, windowHeight, 0, 0);
		this.shopPanel = new ShopPanel(game, state, sideWidth, windowHeight, windowWidth + sideWidth, 0);
		MusicManager.PlayShopMusic(game.settings.getFloat("musicVolume", .25f));
	}

	@Override
	public void render(final float delta)
	{
		try
		{
			update();
		} catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			draw();
		} catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void show()
	{
		if(game.isQuitting){
			for(Unit unit : state.roster){
				state.graveyard.add(generateUnitDto(unit));
			}
			game.setScreen(new GameOverScreen(game, state.roundsSurvived, state.graveyard));
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
		unitDto.roundKilled = state.roundsSurvived;
		unitDto.isMale = deceased.isMale;
		unitDto.backStory = deceased.backStory;
		return unitDto;
	}


	public void update() throws IOException
	{
		if (Gdx.input.justTouched())
		{
			if (!state.isInspecting && !state.isShopkeeperPanelDisplayed && stockWindow.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
			{
				stockWindow.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			}else if (shopPanel.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
			{
				shopPanel.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			} else if (state.isShopkeeperPanelDisplayed && shopkeeperPanel.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
			{
				shopkeeperPanel.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			}	
		}else if((Gdx.input.isKeyPressed(Keys.BACK) || Gdx.input.isKeyJustPressed(Keys.BACKSPACE)) && SettingsScreen.backEnabled){
			game.setScreen(new SettingsScreen(game, game.getScreen(), true));
		}
	}
}
