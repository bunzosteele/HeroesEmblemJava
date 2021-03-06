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
import com.badlogic.gdx.audio.Sound;
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
	RosterPanel rosterPanel;
	SettingsPanel settingsPanel;
	MapPanel mapPanel;
	UnitDetailsPanel unitDetailsPanel;
	HelpPanel helpPanel;
	public static Sound buySound = Gdx.audio.newSound(Gdx.files.internal("buy.wav"));
	public static Sound finalBuySound = Gdx.audio.newSound(Gdx.files.internal("finalbuy.wav"));

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
		stockWindow.drawBackground();
		rosterPanel.drawBackground();
		shopkeeperPanel.drawBackground();
		stockWindow.drawBorder();
		shopkeeperPanel.drawBorder();
		rosterPanel.drawBorder();
		shopPanel.drawBorder();
		shopPanel.draw();
		stockWindow.draw();
		rosterPanel.draw();
		shopkeeperPanel.draw();
		game.batcher.end();
		if(helpPanel.isVisible){
			game.shapeRenderer.begin(ShapeType.Filled);
			helpPanel.drawBackground();
			game.shapeRenderer.end();
			game.batcher.begin();
			helpPanel.draw();
			game.batcher.end();	
		}else if(state.isSettingsOpen){
			game.shapeRenderer.begin(ShapeType.Filled);
			settingsPanel.drawBackground();
			game.shapeRenderer.end();
			game.batcher.begin();
			settingsPanel.draw();
			game.batcher.end();		
		}else if(state.isMapOpen){
			game.shapeRenderer.begin(ShapeType.Filled);
			mapPanel.drawBackground();
			game.shapeRenderer.end();
			game.batcher.begin();
			mapPanel.draw();
			game.batcher.end();
		}else if(state.isUnitDetailsOpen){
			game.shapeRenderer.begin(ShapeType.Filled);
			unitDetailsPanel.drawBackground();
			game.shapeRenderer.end();
			game.batcher.begin();
			unitDetailsPanel.draw();
			game.batcher.end();
		}
	}

	private void InitializeShopScreen(final HeroesEmblem game)
	{
		this.game = game;
		game.isQuitting = false;
		int sideWidth = (Gdx.graphics.getWidth() / 16) * 3;
		int windowWidth = Gdx.graphics.getWidth() - sideWidth * 2;
		int windowHeight = Gdx.graphics.getHeight();
		int rosterHeight = Gdx.graphics.getHeight() * 2 / 9;
		this.stockWindow = new StockWindow(game, state, windowWidth, Gdx.graphics.getHeight() - rosterHeight, sideWidth, rosterHeight);
		this.shopkeeperPanel= new ShopkeeperPanel(game, state, sideWidth, windowHeight, 0, 0);
		this.shopPanel = new ShopPanel(game, state, sideWidth, windowHeight, windowWidth + sideWidth, 0);
		this.rosterPanel = new RosterPanel(game, state, windowWidth, rosterHeight, sideWidth, 0);
		this.settingsPanel = new SettingsPanel(game, state, Gdx.graphics.getWidth() * 8 / 16, Gdx.graphics.getHeight() * 7 / 9, Gdx.graphics.getWidth() * 4 / 16, Gdx.graphics.getHeight() * 1 / 9);
		this.helpPanel = new HelpPanel(game, Gdx.graphics.getWidth() * 8 / 16, Gdx.graphics.getHeight() * 7 / 9, Gdx.graphics.getWidth() * 4 / 16, Gdx.graphics.getHeight() * 1 / 9);
		this.mapPanel = new MapPanel(game, state.nextBattlefield, Gdx.graphics.getWidth() * 7 / 16, Gdx.graphics.getHeight() * 5 / 9, Gdx.graphics.getWidth() * 9 / 32, Gdx.graphics.getHeight() * 2 / 9);
		this.unitDetailsPanel = new UnitDetailsPanel(game, state, Gdx.graphics.getWidth() * 8 / 16, Gdx.graphics.getHeight() * 5 / 9, Gdx.graphics.getWidth() * 4 / 16, Gdx.graphics.getHeight() * 2 / 9);
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

	public void update() throws IOException
	{
		if(state.isLongPressed){
			if(!state.isSettingsOpen && !state.isMapOpen && !state.isUnitDetailsOpen){			
				if (rosterPanel.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())){
					rosterPanel.processLongTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), helpPanel);
				}else if (stockWindow.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())){
					stockWindow.processLongTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), helpPanel);
				}else if (shopPanel.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())){
					shopPanel.processLongTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), helpPanel);
				}else if (shopkeeperPanel.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())){
					shopkeeperPanel.processLongTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), helpPanel);
				}	
			}
		}else if (Gdx.input.justTouched())
		{
			if(helpPanel.isVisible){
				helpPanel.isVisible = false;
			}else if(state.isSettingsOpen){
				if (settingsPanel.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())){
					settingsPanel.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
				}else{
					state.isSettingsOpen = false;
					state.isMapOpen = false;
					state.isUnitDetailsOpen = false;
				}
			}else if(state.isMapOpen){
				if (mapPanel.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())){
					mapPanel.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
				}else{
					state.isSettingsOpen = false;
					state.isMapOpen = false;
					state.isUnitDetailsOpen = false;
				}
			}else if(state.isUnitDetailsOpen){
				state.isSettingsOpen = false;
				state.isMapOpen = false;
				state.isUnitDetailsOpen = false;
			}else{			
				if (rosterPanel.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())){
					rosterPanel.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
				}else if (stockWindow.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())){
					stockWindow.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
				}else if (shopPanel.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())){
					shopPanel.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
				}else if (shopkeeperPanel.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())){
					shopkeeperPanel.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
				}	
			}
		}else if((Gdx.input.isKeyJustPressed(Keys.BACK) || Gdx.input.isKeyJustPressed(Keys.BACKSPACE)) && SettingsPanel.backEnabled){
			game.shopState.isSettingsOpen = !game.shopState.isSettingsOpen;
			SettingsPanel.isDisplayingConfirmation = game.shopState.isSettingsOpen;
		}
	}
}
