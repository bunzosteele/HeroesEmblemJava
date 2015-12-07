package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.AiProcessor;
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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class BattleScreen extends ScreenAdapter
{
	HeroesEmblem game;
	BattleState state;
	BattleWindow battleWindow;
	BattleUnitStatusPanel unitStatus;
	BattleControls battleControls;
	TacticsControls tacticsControls;
	AiProcessor aiProcessor;
	boolean spoofAiThinking = false;

	public BattleScreen(final HeroesEmblem game, final ShopState shopState) throws IOException
	{
		this.state = new BattleState(shopState);
		this.game = game;
		game.battleState = this.state;
		InitializeBattleScreen();
	}
	
	public BattleScreen(final HeroesEmblem game, final BattleState battleState) throws IOException
	{
		this.state = battleState;
		this.game = game;
		game.battleState = this.state;
		InitializeBattleScreen();
	}
	
	private void InitializeBattleScreen(){
		int sideWidth = Gdx.graphics.getWidth() / 6;
		int controlHeight = Gdx.graphics.getHeight() / 6;
		int windowWidth = Gdx.graphics.getWidth() - sideWidth;
		int windowHeight = Gdx.graphics.getHeight() - controlHeight;
		final double desiredRatio = 9 / (double) 16;
		double actualRatio = windowHeight / (double) windowWidth;
		while (actualRatio != desiredRatio)
		{
			if (actualRatio > desiredRatio)
			{
				windowHeight--;
				controlHeight++;
				actualRatio = windowHeight / (double) windowWidth;
			} else
			{
				windowWidth--;
				sideWidth++;
				actualRatio = windowHeight / (double) windowWidth;
			}
		}
		this.battleWindow = new BattleWindow(game, this.state, windowWidth, windowHeight, controlHeight);
		this.unitStatus = new BattleUnitStatusPanel(game, this.state, sideWidth, windowHeight, windowWidth, controlHeight);
		this.battleControls = new BattleControls(game, this.state, controlHeight, windowWidth, sideWidth);
		this.tacticsControls = new TacticsControls(game, this.state, sideWidth, windowWidth - sideWidth, controlHeight);
		this.aiProcessor = new AiProcessor(state);
		if(state.battlefieldId < 5){
			MusicManager.PlayEasyBattleMusic(this.game.settings.getFloat("musicVolume", .25f));
		}else{
			MusicManager.PlayHardBattleMusic(this.game.settings.getFloat("musicVolume", .25f));	
		}
		game.adsController.hideBannerAd();
	}

	public void draw() throws IOException
	{
		final GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.game.batcher.begin();
		this.battleWindow.draw();
		this.unitStatus.draw();
		if(this.state.isInTactics){
			this.tacticsControls.draw();
		}else{
			this.battleControls.drawBackground();
			this.battleControls.draw();
		}

		this.game.batcher.end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		this.game.shapeRenderer.begin(ShapeType.Filled);
		this.battleWindow.drawHighlights();
		this.game.shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		this.game.shapeRenderer.begin(ShapeType.Filled);
		this.battleWindow.drawHealthBars();
		this.game.shapeRenderer.end();
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
		float cpuSpeed = this.game.settings.getFloat("cpuSpeed", 1.1f);
		for(Unit unit : this.state.AllUnits()){
			unit.animationSpeed = cpuSpeed;
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
		if((Gdx.input.isKeyPressed(Keys.BACK) || Gdx.input.isKeyJustPressed(Keys.BACKSPACE)) && SettingsScreen.backEnabled){
			this.game.setScreen(new SettingsScreen(this.game, this.game.getScreen(), true));
		}
		this.state.CleanBoard();
		if (this.state.HasPlayerLost())
		{
			game.shopState = null;
			this.game.setScreen(new GameOverScreen(this.game, this.state.roundsSurvived, this.state.graveyard));
		}else if (this.state.enemies.size() == 0 && this.state.dyingUnits.size() == 0)
		{
			this.state.EndBattle();
			game.battleState = null;
			this.game.setScreen(new ShopScreen(this.game, this.state));
		}else if(this.state.currentPlayer > 0){
			if(!spoofAiThinking){
				spoofAiThinking = true;
				Timer.schedule(new Task()
				{
					@Override
					public void run()
					{
						spoofAiThinking = false;
					}
				}, this.game.settings.getFloat("cpuSpeed", 1.1f), 0, 0);
				aiProcessor.MakeMove(this.game.settings.getFloat("sfxVolume", .5f));
			}
		}else if (this.state.currentPlayer == 0 && Gdx.input.justTouched())
		{
			if (this.battleWindow.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
			{
				this.battleWindow.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
				this.tacticsControls.startingWithBenched = false;
			} else if (this.unitStatus.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
			{
				this.unitStatus.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
				this.tacticsControls.startingWithBenched = false;
			} else if (this.battleControls.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
			{
				this.battleControls.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			}else if(this.tacticsControls.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())){
				this.tacticsControls.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			}
			if(this.state.isInTactics){
				this.tacticsControls.updateState();
			}
		}
	}
}