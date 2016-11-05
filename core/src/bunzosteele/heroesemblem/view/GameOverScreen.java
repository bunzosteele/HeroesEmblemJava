package bunzosteele.heroesemblem.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.HighscoreManager;
import bunzosteele.heroesemblem.model.Units.LocationDto;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitDto;
import bunzosteele.heroesemblem.model.Units.UnitGenerator;
import bunzosteele.heroesemblem.model.Units.UnitType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class GameOverScreen extends MenuScreen
{
	int roundsSurvived;
	List<UnitDto> graveyard;
	UnitDto hero;
	int xOffset;
	int headerOffset;
	int score;
	Sprite pedestalSprite;
	Sprite buttonSprite;
	Sprite inactiveButton;
	float yOffset;
	float buttonHeight;
	static boolean hasSubmitted = false;

	public GameOverScreen(final HeroesEmblem game, int roundsSurvived, List<UnitDto> graveyard)
	{
		super(game);
		this.game.battleState = null;
		this.game.shopState = null;
		this.roundsSurvived = roundsSurvived;
		this.graveyard = graveyard;
		this.hero = GetHero(graveyard);
		if(this.hero != null){
			try {
				this.hero.backStory = UnitGenerator.GenerateStoryEnding(this.hero);
			} catch (IOException e) {	
			}
		}
		this.xOffset = Gdx.graphics.getWidth() / 8;
		this.headerOffset = Gdx.graphics.getHeight() - xOffset / 16;
		final AtlasRegion pedestalRegion = this.game.textureAtlas.findRegion("Pedestal");
		this.pedestalSprite = new Sprite(pedestalRegion);
		final AtlasRegion buttonRegion = this.game.textureAtlas.findRegion("Button");	
		buttonSprite = new Sprite(buttonRegion);
		final AtlasRegion inactiveRegion = this.game.textureAtlas.findRegion("InactiveButton");
		this.inactiveButton = new Sprite(inactiveRegion);
		score = roundsSurvived * 100;
		for(UnitDto unit : graveyard){
			score += unit.unitsKilled;
		}
		if(score > 0)
			RecordGame();
	}

	public void draw()
	{
		super.setupDraw();
		this.game.batcher.begin();
		drawContent();
		this.game.batcher.end();
	}
	
	private void drawContent(){
		super.drawBackground();
		this.game.font.getData().setScale(.25f);
		if(this.hero == null){
			this.game.font.draw(this.game.batcher, "You resigned before anyone even died.", this.xOffset * 2, this.headerOffset - this.xOffset, xOffset * 4, 1, false);
			this.game.font.draw(this.game.batcher, "Quitter.", this.xOffset * 3, this.headerOffset - this.game.font.getData().lineHeight - this.xOffset, xOffset * 2, 1, false);
		}else{
			this.game.font.draw(this.game.batcher, "Rounds Survived: " + this.roundsSurvived, this.xOffset * 2, this.headerOffset, xOffset * 4, 1, false);
			this.game.font.draw(this.game.batcher, "Final Score: " + this.score, this.xOffset * 2, this.headerOffset - this.game.font.getData().lineHeight, xOffset * 4, 1, false);
			final AtlasRegion region = UnitRenderer.UnitSheets.get(UnitType.valueOf(this.hero.type)).findRegion("Idle-0-" + idleFrame);
			final Sprite sprite = new Sprite(region);
			this.game.font.draw(this.game.batcher, "Your hero: " + this.hero.name, this.xOffset * 2, this.headerOffset - 2 * this.game.font.getData().lineHeight, xOffset * 4, 1, false);
			this.game.font.getData().setScale(.2f);
			this.game.batcher.draw(pedestalSprite, 3 * this.xOffset - (6 * this.game.font.getData().lineHeight / 2) , this.headerOffset - 10 * this.game.font.getData().lineHeight, 6 * this.game.font.getData().lineHeight, 6 * this.game.font.getData().lineHeight);
			this.game.batcher.draw(sprite, 3 * this.xOffset - (6 * this.game.font.getData().lineHeight / 2) + (this.game.font.getData().lineHeight * 3 / 10) , this.headerOffset - 10 * this.game.font.getData().lineHeight + (this.game.font.getData().lineHeight * 3 / 10), (6 * this.game.font.getData().lineHeight) * 8 / 10, (6 * this.game.font.getData().lineHeight) * 8 / 10);
			this.game.font.draw(this.game.batcher, "Level " + this.hero.level + " " + this.hero.type, this.xOffset * 3, this.headerOffset - 4 * this.game.font.getData().lineHeight, xOffset * 4, 1, false);
			this.game.font.draw(this.game.batcher, "Kills:" + this.hero.unitsKilled, this.xOffset * 4, this.headerOffset - 5 * this.game.font.getData().lineHeight, xOffset, 1, false);
			this.game.font.draw(this.game.batcher, "Damage:" + this.hero.damageDealt, this.xOffset * 5, this.headerOffset - 5 * this.game.font.getData().lineHeight, xOffset, 1, false);
			this.game.font.draw(this.game.batcher, "HP:" + this.hero.maximumHealth, this.xOffset * 4, this.headerOffset - 6 * this.game.font.getData().lineHeight, xOffset, 1, false);
			this.game.font.draw(this.game.batcher, "MOVE:" + this.hero.movement, this.xOffset * 5, this.headerOffset - 6 * this.game.font.getData().lineHeight, xOffset, 1, false);
			this.game.font.draw(this.game.batcher, "ATK:" + this.hero.attack, this.xOffset * 4, this.headerOffset - 7 * this.game.font.getData().lineHeight, xOffset, 1, false);
			this.game.font.draw(this.game.batcher, "DEF:" + this.hero.defense, this.xOffset * 5, this.headerOffset - 7 * this.game.font.getData().lineHeight, xOffset, 1, false);
			this.game.font.draw(this.game.batcher, "EVP:" + this.hero.evasion, this.xOffset * 4, this.headerOffset - 8 * this.game.font.getData().lineHeight, xOffset, 1, false);
			this.game.font.draw(this.game.batcher, "ACC:" + this.hero.accuracy, this.xOffset * 5, this.headerOffset - 8 * this.game.font.getData().lineHeight, xOffset, 1, false);
			if(this.hero.ability != null){
				this.game.font.draw(this.game.batcher, "Ability:" + this.hero.ability, this.xOffset * 3, this.headerOffset - 9 * this.game.font.getData().lineHeight, xOffset * 4, 1, false);
			}else{
				this.game.font.draw(this.game.batcher, "Ability: None", this.xOffset * 3, this.headerOffset - 9 * this.game.font.getData().lineHeight, xOffset * 4, 1, false);
			}
			this.game.font.draw(this.game.batcher, this.hero.backStory, this.xOffset / 2, this.headerOffset - 11 * this.game.font.getData().lineHeight, xOffset * 7, 1, true);
		}
		this.game.font.getData().setScale(.25f);
		this.yOffset = this.headerOffset - 15 * this.game.font.getData().lineHeight;
		this.buttonHeight = this.game.font.getData().lineHeight * 3 / 2;
		if(CanSubmitScore()){
			this.game.batcher.draw(buttonSprite, this.xOffset * 2 / 3, this.yOffset - 2 * this.game.font.getData().lineHeight, this.xOffset * 3, this.buttonHeight);
		}else{
			this.game.batcher.draw(inactiveButton, this.xOffset * 2 / 3, this.yOffset - 2 * this.game.font.getData().lineHeight, this.xOffset * 3, this.buttonHeight);	
		}
		this.game.batcher.draw(buttonSprite, this.xOffset * 13 / 3, this.yOffset - 2 * this.game.font.getData().lineHeight, this.xOffset * 3, this.buttonHeight);
		this.game.batcher.draw(buttonSprite, this.xOffset * 3 / 4, this.yOffset, this.xOffset * 3 / 2, this.buttonHeight);
		this.game.batcher.draw(buttonSprite, this.xOffset * 13 / 4, this.yOffset, this.xOffset * 3 / 2, this.buttonHeight);
		this.game.batcher.draw(buttonSprite, this.xOffset * 23 / 4, this.yOffset, this.xOffset * 3 / 2, this.buttonHeight);
		this.game.font.draw(this.game.batcher, "Submit Highscore", this.xOffset * 2 / 3, this.yOffset - this.game.font.getData().lineHeight, xOffset * 3, 1, false);
		this.game.font.draw(this.game.batcher, "View Leaderboards", this.xOffset * 13 / 3, this.yOffset - this.game.font.getData().lineHeight, xOffset * 3, 1, false);
		this.game.font.draw(this.game.batcher, "Highscores", this.xOffset * 3 / 4, this.yOffset + this.game.font.getData().lineHeight, xOffset * 3 / 2, 1, false);
		this.game.font.draw(this.game.batcher, "Main Menu", this.xOffset * 13 / 4, this.yOffset + this.game.font.getData().lineHeight, xOffset * 3 / 2, 1, false);
		this.game.font.draw(this.game.batcher, "New Game", this.xOffset * 23 / 4, this.yOffset + this.game.font.getData().lineHeight, xOffset * 3 / 2, 1, false);
		this.game.font.getData().setScale(.33f);
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
			if ((Gdx.input.getX() > this.xOffset * 23 / 4 && Gdx.input.getX() < this.xOffset * 23 / 4 + this.xOffset * 3 / 2) && (Gdx.graphics.getHeight() - Gdx.input.getY() > this.yOffset && Gdx.graphics.getHeight() - Gdx.input.getY() < this.yOffset + buttonHeight))
			{
				GameOverScreen.hasSubmitted = false;
				this.game.setScreen(new ShopScreen(this.game));
				return;
			}
			if ((Gdx.input.getX() > this.xOffset * 13 / 4 && Gdx.input.getX() < this.xOffset * 13 / 4 + this.xOffset * 3 / 2) && (Gdx.graphics.getHeight() - Gdx.input.getY() > this.yOffset && Gdx.graphics.getHeight() - Gdx.input.getY() < this.yOffset + buttonHeight))
			{
				GameOverScreen.hasSubmitted = false;
				this.game.setScreen(new MainMenuScreen(this.game));
				return;
			}
			if ((Gdx.input.getX() > this.xOffset * 3 / 4 && Gdx.input.getX() < this.xOffset * 3 / 4 + this.xOffset * 3 / 2) && (Gdx.graphics.getHeight() - Gdx.input.getY() > this.yOffset && Gdx.graphics.getHeight() - Gdx.input.getY() < this.yOffset + buttonHeight))
			{
				GameOverScreen.hasSubmitted = false;
				this.game.setScreen(new HighscoreScreen(this.game));
				return;
			}
			if ((Gdx.input.getX() > this.xOffset * 2 / 3 && Gdx.input.getX() < this.xOffset * 2 / 3 + this.xOffset * 3) && (Gdx.graphics.getHeight() - Gdx.input.getY() < this.yOffset))
			{
				if(CanSubmitScore()){
					this.game.gameServicesController.SubmitHighScore(score);
					GameOverScreen.hasSubmitted = true;
				}
			}
			if ((Gdx.input.getX() > this.xOffset * 13 / 3 && Gdx.input.getX() < this.xOffset * 13 / 3 + this.xOffset * 3) && (Gdx.graphics.getHeight() - Gdx.input.getY() < this.yOffset))
			{
				this.game.gameServicesController.ViewLeaderboard();
			}
		}else if(Gdx.input.isKeyPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.BACKSPACE)){
			this.game.setScreen(new MainMenuScreen(this.game));
		}
	}
	
	private void RecordGame(){
		String action = this.game.isQuitting ? "Quit" : "Defeated";
		Json json = new Json();
		String parsedGraveyard = json.toJson(GetAnalyticsDtos(this.graveyard));			
		this.game.gameServicesController.RecordAnalyticsEvent("GameOver", action, parsedGraveyard, (long) roundsSurvived);
		if(this.hero != null)
			HighscoreManager.RecordGame(score, hero);
	}
	
	private boolean CanSubmitScore(){
		return score > 0 && !hasSubmitted;
	}
	
	private UnitDto GetHero(List<UnitDto> graveyard){
		UnitDto hero = null;
		if(graveyard.size() == 0){
			return hero;
		}
		for(UnitDto unit : graveyard){
			if(hero == null || unit.damageDealt > hero.damageDealt){
				hero = unit;
			}
		}
		return hero;
	}
	
	private List<AnalyticsDto> GetAnalyticsDtos(List<UnitDto> units){
		List<AnalyticsDto> analyticsDtos = new ArrayList<AnalyticsDto>();
		for(UnitDto unit : units){
			AnalyticsDto analyticsDto = new AnalyticsDto();
			analyticsDto.type = unit.type;
			analyticsDto.attack = unit.attack;
			analyticsDto.defense = unit.defense;
			analyticsDto.evasion = unit.evasion;
			analyticsDto.accuracy = unit.accuracy;
			analyticsDto.movement = unit.movement;
			analyticsDto.maximumHealth = unit.maximumHealth;
			analyticsDto.level = unit.level;
			analyticsDto.ability = unit.ability;
			analyticsDto.unitsKilled = unit.unitsKilled;
			analyticsDto.damageDealt = unit.damageDealt;
			analyticsDto.roundKilled = unit.roundKilled;
			analyticsDto.locationKilled = unit.locationKilled;
			analyticsDtos.add(analyticsDto);
		}
		return analyticsDtos;
	}
	
	private class AnalyticsDto{
		public String type;
		public int attack;
		public int defense;
		public int evasion;
		public int accuracy;
		public int movement;
		public int maximumHealth;
		public int level;
		public String ability;
		public int unitsKilled;
		public int damageDealt;
		public int roundKilled;
		public LocationDto locationKilled;
	}
}
