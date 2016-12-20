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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class GameOverScreen extends MenuScreen
{
	int roundsSurvived;
	List<UnitDto> graveyard;
	UnitDto hero;
	int score;
	static boolean hasSubmitted = false;
	int tileSize;
	int chainSize;
	int shadowSize;
	int width;
	int height;
	int backdropWidth;
	int backdropHeight;	
	int buttonWidth;
	int buttonHeight;

	public GameOverScreen(final HeroesEmblem game, int roundsSurvived, List<UnitDto> graveyard)
	{
		super(game);
		this.game.battleState = null;
		this.game.shopState = null;
		this.roundsSurvived = roundsSurvived;
		this.graveyard = graveyard;
		this.hero = GetHero(graveyard);
		this.tileSize = Gdx.graphics.getWidth() / 16;
		this.chainSize = tileSize / 5;
		this.shadowSize = chainSize / 3;
		this.score = roundsSurvived * 100;
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		backdropWidth = tileSize * 12 / 5;
		backdropHeight = (int) Math.floor(backdropWidth * .353);
		buttonWidth = (width * 3 / 16 - (chainSize - shadowSize) * 2) * 9 / 10;
		buttonHeight = buttonWidth * 25 / 122;
		if(this.hero != null){
			try {
				this.hero.backStory = UnitGenerator.GenerateStoryEnding(this.hero);
			} catch (IOException e) {	
			}
		}

		for(UnitDto unit : graveyard){
			score += unit.unitsKilled;
		}
		
		if(score > 0)
			RecordGame();
	}

	public void draw() throws IOException
	{
		super.setupDraw();
		game.shapeRenderer.begin(ShapeType.Filled);
		drawBackground();
		game.shapeRenderer.end();
		this.game.batcher.begin();
		drawContent();
		drawBorder();
		this.game.batcher.end();
	}
	
	public void drawBackground()
	{
		Color backgroundColor = new Color(.227f, .204f, .157f, 1);
		game.shapeRenderer.rect(0, 0, width, height, backgroundColor, backgroundColor, backgroundColor, backgroundColor);
	}
	
	public void drawBorder(){
		int chainXOffset = 0;
		int chainYOffset = height  - chainSize;
		
		while (chainXOffset < width){
			if(chainXOffset > 0 && chainXOffset < width - chainSize)
				game.batcher.draw(game.sprites.ChainHorizontal, chainXOffset, height - chainSize - shadowSize, chainSize, chainSize + shadowSize);
			game.batcher.draw(game.sprites.ChainHorizontal, chainXOffset, -shadowSize, chainSize, chainSize + shadowSize);
			chainXOffset += chainSize;
		}
		while (chainYOffset >= 0){
			if(chainYOffset > 0 && chainYOffset < height - chainSize)
				game.batcher.draw(game.sprites.ChainVertical, 0, chainYOffset, chainSize + shadowSize, chainSize);
			game.batcher.draw(game.sprites.ChainVertical, width - chainSize, chainYOffset, chainSize + shadowSize, chainSize);
			chainYOffset -= chainSize;
		}
		
		game.batcher.draw(game.sprites.ChainNW, 0, height - chainSize, chainSize, chainSize);
		game.batcher.draw(game.sprites.ChainNE, width - chainSize, height - chainSize, chainSize, chainSize);
		game.batcher.draw(game.sprites.ChainSW, 0, 0, chainSize, chainSize);
		game.batcher.draw(game.sprites.ChainSE, width - chainSize, 0, chainSize, chainSize);
	}
	
	private void drawContent() throws IOException{	
		if(this.hero == null){
			this.game.font.getData().setScale(.5f);
			this.game.font.draw(this.game.batcher, "You resigned before doing anything.", width / 8, height * 8 / 9, width * 3 / 4, 1, false);
			this.game.font.draw(this.game.batcher, "Quitter.", width / 8, height * 8 / 9 - game.font.getData().lineHeight, width * 3 / 4, 1, false);
		}else{
			drawUnitDetails();
		}
		this.game.font.getData().setScale(.25f);
		if(CanSubmitScore()){
			this.game.batcher.draw(game.sprites.PerkEmphasis, tileSize / 2, height / 9, buttonWidth, buttonHeight);
		}else{
			this.game.batcher.draw(game.sprites.PerkDisabled, tileSize / 2, height / 9, buttonWidth, buttonHeight);
		}
		
		this.game.batcher.draw(game.sprites.PerkEnabled, tileSize * 7 / 2, height / 9, buttonWidth, buttonHeight);
		this.game.batcher.draw(game.sprites.PerkEnabled, tileSize * 13 / 2, height / 9, buttonWidth, buttonHeight);
		this.game.batcher.draw(game.sprites.PerkEnabled, tileSize * 19 / 2, height / 9, buttonWidth, buttonHeight);
		this.game.batcher.draw(game.sprites.PerkEmphasis, tileSize * 25 / 2, height / 9, buttonWidth, buttonHeight);
		this.game.font.draw(this.game.batcher, "Submit Highscore", tileSize / 2, height / 9 + tileSize / 8 + game.font.getLineHeight(), buttonWidth, 1, false);
		this.game.font.draw(this.game.batcher, "Leaderboards", tileSize * 7 / 2, height / 9 + tileSize / 8 + game.font.getLineHeight(), buttonWidth, 1, false);
		this.game.font.draw(this.game.batcher, "Highscores", tileSize * 13 / 2, height / 9 + tileSize / 8 + game.font.getLineHeight(), buttonWidth, 1, false);
		this.game.font.draw(this.game.batcher, "Main Menu", tileSize * 19 / 2, height / 9 + tileSize / 8 + game.font.getLineHeight(), buttonWidth, 1, false);
		this.game.font.draw(this.game.batcher, "New Game", tileSize * 25 / 2, height / 9 + tileSize / 8 + game.font.getLineHeight(), buttonWidth, 1, false);
	}
	
	public void drawUnitDetails() throws IOException{
		int contentHeight = height * 17 / 18;
		game.font.setColor(Color.BLACK);
		game.font.getData().setScale(.20f);	
		int portraitHeight = tileSize - chainSize - shadowSize;
		int nameBackdropWidth = tileSize * 2 - chainSize - shadowSize;
		int nameBackdropHeight = nameBackdropWidth * 44 / 92;
		int nameY =  contentHeight - chainSize - shadowSize- nameBackdropHeight;
		int contentXOffset = chainSize + shadowSize + (width - (nameBackdropWidth + chainSize * 2 + shadowSize * 2 + portraitHeight + backdropWidth * 2)) / 2;
		final AtlasRegion portraitRegion = game.textureAtlas.findRegion("Portrait" + this.hero.type + this.hero.team);
		game.batcher.draw(new Sprite(portraitRegion), contentXOffset, nameY + shadowSize, portraitHeight, portraitHeight);		
		final AtlasRegion nameBackdropRegion = game.textureAtlas.findRegion("NameBackdrop" + this.hero.team);
		game.batcher.draw(new Sprite(nameBackdropRegion), contentXOffset + portraitHeight, contentHeight - chainSize - shadowSize - nameBackdropHeight, nameBackdropWidth, nameBackdropHeight);
		game.font.draw(game.batcher, this.hero.name, contentXOffset + portraitHeight, nameY + (portraitHeight + shadowSize * 2) / 2 + game.font.getData().lineHeight, nameBackdropWidth - (nameBackdropWidth / 10), 1, false);
		game.font.draw(game.batcher, this.hero.type.toString(), contentXOffset + portraitHeight, nameY + (portraitHeight + shadowSize * 2) / 2 - game.font.getData().lineHeight /4 , nameBackdropWidth - (nameBackdropWidth / 10), 1, false);	
		game.font.getData().setScale(.25f);
		game.batcher.draw(game.sprites.ExperienceBackdrop, contentXOffset, nameY - backdropHeight, backdropWidth, backdropHeight);
		DrawExperienceBar(nameY, contentXOffset);
		game.batcher.draw(game.sprites.AttackBackdrop, contentXOffset + nameBackdropWidth + portraitHeight, contentHeight - chainSize - shadowSize - backdropHeight, backdropWidth, backdropHeight);
		game.batcher.draw(game.sprites.AccuracyBackdrop, contentXOffset + nameBackdropWidth + portraitHeight, contentHeight - chainSize - shadowSize - backdropHeight * 2, backdropWidth, backdropHeight);
		game.batcher.draw(game.sprites.DefenseBackdrop, contentXOffset + nameBackdropWidth + portraitHeight + backdropWidth, contentHeight - chainSize - shadowSize - backdropHeight, backdropWidth, backdropHeight);
		game.batcher.draw(game.sprites.EvasionBackdrop, contentXOffset + nameBackdropWidth + portraitHeight + backdropWidth, contentHeight - chainSize - shadowSize - backdropHeight * 2, backdropWidth, backdropHeight);
		int statTextXOffset = contentXOffset + backdropWidth * 57 / 116 + portraitHeight + nameBackdropWidth;
		int statTextWidth = backdropWidth * 32 / 116;
		int statRelativeYOffset = backdropHeight * 2 / 41 + (backdropHeight * 25 / 41) / 2 - backdropHeight;	
		game.font.setColor(Color.BLACK);
		game.font.draw(game.batcher, (this.hero.level < 10 ? "Lvl. 0" : "Lvl. ") + this.hero.level, contentXOffset + backdropHeight, nameY - game.font.getData().lineHeight / 2, backdropWidth - (backdropWidth * 2 / 29) - backdropHeight - (nameBackdropWidth / 10), 1, false);
		game.font.getData().setScale(.35f);
		game.font.draw(game.batcher, "" + this.hero.attack, statTextXOffset, nameY - statRelativeYOffset, statTextWidth, 1, false);
		game.font.draw(game.batcher, "" + this.hero.accuracy, statTextXOffset, nameY - backdropHeight - statRelativeYOffset, statTextWidth, 1, false);
		game.font.draw(game.batcher, "" + this.hero.defense, statTextXOffset + backdropWidth, nameY  - statRelativeYOffset, statTextWidth, 1, false);
		game.font.draw(game.batcher, "" + this.hero.evasion, statTextXOffset + backdropWidth, nameY - backdropHeight - statRelativeYOffset, statTextWidth, 1, false);
		
		game.font.draw(game.batcher, "Movement Speed: " + this.hero.movement, contentXOffset + backdropWidth, nameY - backdropHeight * 2 - statRelativeYOffset + game.font.getData().lineHeight / 2, backdropWidth * 2, 1, false);
		game.font.draw(game.batcher, "Ability: " + (this.hero.ability == null ? "None" : this.hero.ability), contentXOffset + backdropWidth, nameY - backdropHeight * 2 - statRelativeYOffset - game.font.getData().lineHeight * 3 / 4, backdropWidth * 2, 1, false);
		game.font.getData().setScale(.2f);
		game.font.draw(game.batcher, "Experience: " + this.hero.experience + "/" + this.hero.experienceNeeded, contentXOffset, nameY - backdropHeight * 2 - statRelativeYOffset + game.font.getData().lineHeight, backdropWidth, 1, false);
		game.font.draw(game.batcher, "Damage Dealt: " + this.hero.damageDealt, contentXOffset, nameY - backdropHeight * 2 - statRelativeYOffset, backdropWidth, 1, false);
		game.font.draw(game.batcher, "Units Killed: " + this.hero.unitsKilled, contentXOffset, nameY - backdropHeight * 2 - statRelativeYOffset - game.font.getData().lineHeight, backdropWidth, 1, false);
		game.font.getData().setScale(.5f);
		game.font.draw(game.batcher, "Biography:", chainSize + shadowSize, nameY - backdropHeight * 2 - statRelativeYOffset - game.font.getData().lineHeight * 5 / 2, width - chainSize * 2 - shadowSize * 2, 1, false);
		game.font.getData().setScale(.4f);
		game.font.draw(game.batcher, this.hero.backStory, width / 8, nameY - backdropHeight * 2 - statRelativeYOffset - game.font.getData().lineHeight * 9 / 2, width * 3 / 4, 1, true);
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
		try {
			this.draw();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void update() throws IOException
	{
		if (Gdx.input.justTouched())
		{
			int clickedY = Gdx.graphics.getHeight() - Gdx.input.getY();
			int clickedX = Gdx.input.getX();
			if(clickedY >= height / 9 && clickedY <= height / 9 + buttonHeight){
				if(clickedX >= tileSize / 2 && clickedX < tileSize / 2 + buttonWidth){
					if(CanSubmitScore()){
						this.game.gameServicesController.SubmitHighScore(score);
						GameOverScreen.hasSubmitted = true;
					}
				}else if(clickedX >= tileSize* 7 / 2 && clickedX < tileSize * 7 / 2 + buttonWidth){
					this.game.gameServicesController.ViewLeaderboard();
				}else if(clickedX >= tileSize * 13 / 2 && clickedX < tileSize * 13 / 2 + buttonWidth){
					GameOverScreen.hasSubmitted = false;
					this.game.setScreen(new HighscoreScreen(this.game));
					return;
				}else if(clickedX >= tileSize * 19 / 2 && clickedX < tileSize * 19 / 2 + buttonWidth){
					GameOverScreen.hasSubmitted = false;
					this.game.setScreen(new MainMenuScreen(this.game));
					return;
				}else if(clickedX >= tileSize * 25 / 2 && clickedX < tileSize * 25 / 2 + buttonWidth){
					GameOverScreen.hasSubmitted = false;
					this.game.setScreen(new ShopScreen(this.game));
					return;
				}
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
	
	private void DrawExperienceBar(int nameY, int contentXOffset){
		int leftOffset = (int) (backdropWidth * .396);
		int rightOffset = (int) (backdropWidth * .155);
		int barWidth = backdropWidth - leftOffset - rightOffset;
		int barHeight = (int) (barWidth * .302);
		
		if(this.hero != null){
			int experiencePercent =  this.hero.experience * 100 / this.hero.experienceNeeded;
			int experienceIndex = (int) (experiencePercent / (double) 5);
			
			if(experienceIndex == 20 && this.hero.experience != this.hero.experienceNeeded)
				experienceIndex--;
			
			int barYOffset = (int) (backdropHeight * .122);
			game.batcher.draw(new Sprite(game.textureAtlas.findRegion("Experience" + experienceIndex)), contentXOffset + leftOffset, nameY + barYOffset - backdropHeight, barWidth, barHeight);
		}	
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
