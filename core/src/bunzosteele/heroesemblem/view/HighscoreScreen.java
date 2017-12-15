package bunzosteele.heroesemblem.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.HighscoreManager;
import bunzosteele.heroesemblem.model.HighscoreManager.HighscoreDto;
import bunzosteele.heroesemblem.model.HighscoreManager.HighscoresDto;
import bunzosteele.heroesemblem.model.Units.LocationDto;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitDto;
import bunzosteele.heroesemblem.model.Units.UnitGenerator;
import bunzosteele.heroesemblem.model.Units.UnitType;
import bunzosteele.heroesemblem.model.MusicManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.XmlReader.Element;

public class HighscoreScreen extends MenuScreen
{
	HighscoresDto highscores;
	int backdropWidth;
	int backdropHeight;	
	int buttonWidth;
	int buttonHeight;
	boolean isUnitDetailsOpen = false;
	Screen previous;
	int scrollHeight;
	int portraitHeight ;
	int nameBackdropWidth;
	int nameBackdropHeight;
	UnitDetailsPanel unitDetailsPanel;
	int inspectedIndex = -1;
	boolean isDeleting;

	public HighscoreScreen(final HeroesEmblem game, Screen previous)
	{
		super(game);
		backdropWidth = tileSize * 12 / 5;
		backdropHeight = (int) Math.floor(backdropWidth * .353);
		buttonWidth = (width * 3 / 16 - (chainSize - shadowSize) * 2) * 9 / 10;
		buttonHeight = buttonWidth * 25 / 122;
		this.highscores = HighscoreManager.GetExistingHighscores();
		if(!ValidateHighscores())
		{
			HighscoreManager.EraseHighscores();
			this.highscores = null;
		}
		
		MusicManager.PlayMenuMusic(this.game.settings.getFloat("musicVolume", .25f));
		this.previous = previous;
		this.scrollHeight = buttonWidth * 42 / 93;
		this.portraitHeight = tileSize - chainSize - shadowSize;
		this.nameBackdropWidth = tileSize * 2 - chainSize - shadowSize;
		this.nameBackdropHeight = nameBackdropWidth * 44 / 92;
		this.unitDetailsPanel = new UnitDetailsPanel(game, Gdx.graphics.getWidth() * 8 / 16, Gdx.graphics.getHeight() * 5 / 9, Gdx.graphics.getWidth() * 4 / 16, Gdx.graphics.getHeight() * 2 / 9);
	}

	public void draw() throws IOException
	{
		super.setupDraw();
		game.shapeRenderer.begin(ShapeType.Filled);
		super.drawBackground();
		game.shapeRenderer.end();
		this.game.batcher.begin();
		drawContent();
		super.drawBorder();
		this.game.batcher.end();
		
		if(isUnitDetailsOpen){
			game.shapeRenderer.begin(ShapeType.Filled);
			unitDetailsPanel.drawBackground();
			game.shapeRenderer.end();
			game.batcher.begin();
			unitDetailsPanel.draw(highscores.highscores.get(inspectedIndex).heroUnit);
			game.batcher.end();
		}
	}
	
	private void drawContent(){
		game.font.setColor(Color.WHITE);
		this.game.font.getData().setScale(.66f);
		this.game.font.draw(this.game.batcher, "Highscores", 0, height - game.font.getData().lineHeight, width, 1, false);
		this.game.font.getData().setScale(.25f);
		this.game.batcher.draw(game.sprites.PerkEnabled, (width - buttonWidth) / 2, height * 7 / 9, buttonWidth, buttonHeight);
		this.game.font.draw(this.game.batcher, "Leaderboards",  (width - buttonWidth) / 2, height * 7 / 9 + tileSize / 8 + game.font.getLineHeight(), buttonWidth, 1, false);
		int yOffset = height * 5 /9;
		if(highscores != null){
			this.game.font.getData().setScale(.25f);
			for(HighscoreDto highscore : highscores.highscores){
				UnitDto unit = highscore.heroUnit;
	
				game.batcher.draw(game.sprites.StockNameBackdrop, width * 4 / 16, yOffset, buttonWidth, scrollHeight);	
				
				game.font.setColor(Color.BLACK);
				this.game.font.getData().setScale(.66f);
				game.font.draw(game.batcher, "" + highscore.score, width * 4 / 16, yOffset + (scrollHeight - game.font.getData().lineHeight) / 2 + game.font.getData().lineHeight * 4 / 5, buttonWidth, 1, false);
				
				game.font.setColor(Color.WHITE);
				this.game.font.getData().setScale(.25f);
				
				final AtlasRegion portraitRegion = game.textureAtlas.findRegion("Portrait" + unit.type + unit.team);
				game.batcher.draw(new Sprite(portraitRegion), (width - portraitHeight - nameBackdropWidth) / 2, yOffset + (scrollHeight * 85 / 93) - portraitHeight, portraitHeight, portraitHeight);		
				final AtlasRegion nameBackdropRegion = game.textureAtlas.findRegion("NameBackdrop" + unit.team);
				game.batcher.draw(new Sprite(nameBackdropRegion), (width - portraitHeight - nameBackdropWidth) / 2 + portraitHeight, yOffset + (scrollHeight * 85 / 93) - nameBackdropHeight, nameBackdropWidth, nameBackdropHeight);
				game.font.draw(game.batcher, unit.name, (width - portraitHeight - nameBackdropWidth) / 2 + portraitHeight, yOffset + (scrollHeight * 85 / 93) - game.font.getData().lineHeight / 2, nameBackdropWidth - (nameBackdropWidth / 10), 1, false);
				game.font.draw(game.batcher, unit.type.toString(), (width - portraitHeight - nameBackdropWidth) / 2 + portraitHeight, yOffset + (scrollHeight * 85 / 93) - nameBackdropHeight + game.font.getData().lineHeight * 3 / 2, nameBackdropWidth - (nameBackdropWidth / 10), 1, false);
				game.batcher.draw(game.sprites.InfoOpen, (width - portraitHeight - nameBackdropWidth) / 2 + portraitHeight * 2 + nameBackdropWidth, yOffset + (scrollHeight - tileSize) / 2, tileSize, tileSize);
				yOffset -= height * 3 / 18;
			}
		}

		game.batcher.draw(game.sprites.UndoEnabled, width - shadowSize - chainSize - tileSize, height - shadowSize - chainSize - tileSize, tileSize, tileSize);
		
		if(!isDeleting){
			game.batcher.draw(game.sprites.DeleteButton, (width - buttonWidth) / 2, height / 9, buttonWidth, buttonHeight);
			this.game.font.draw(this.game.batcher, "Delete Highscores",  (width - buttonWidth) / 2, height / 9 + tileSize / 8 + game.font.getLineHeight(), buttonWidth, 1, false);
		}else{
			this.game.font.draw(this.game.batcher, "Delete Highscores?",  (width - buttonWidth) / 2, height * 3 / 18 + tileSize / 8 + game.font.getLineHeight(), buttonWidth, 1, false);
			game.batcher.draw(game.sprites.UndoEnabled, (width - buttonWidth) / 2 - tileSize, height / 18, tileSize, tileSize);
			game.batcher.draw(game.sprites.ConfirmEnabled, (width + buttonWidth) / 2, height / 18, tileSize, tileSize);
		}
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
			if(isUnitDetailsOpen){
				inspectedIndex = -1;
				isUnitDetailsOpen = false;
			}else{
				int y = Gdx.graphics.getHeight() - Gdx.input.getY();
				int x = Gdx.input.getX();
				int unitInfoX = (width - portraitHeight - nameBackdropWidth) / 2 + portraitHeight * 2 + nameBackdropWidth;
				int unitInfoYOffset = (scrollHeight - tileSize) / 2;
				if(x >= width - shadowSize - chainSize - tileSize && x <= width - shadowSize - chainSize && y >= height - shadowSize - chainSize - tileSize && y <= height - shadowSize - chainSize){
					backTouch();
					return;
				}else if(x >= unitInfoX && x <= unitInfoX + tileSize && y >= height * 5 / 9 + unitInfoYOffset && y <= height * 5 / 9 + unitInfoYOffset + tileSize){
					openUnitDetails(0);
					return;
				}else if(x >= unitInfoX && x <= unitInfoX + tileSize && y >= height * 7 / 18 + unitInfoYOffset && y <= height * 13 / 18 + unitInfoYOffset + tileSize){
					openUnitDetails(1);
					return;
				}else if(x >= unitInfoX && x <= unitInfoX + tileSize && y >= height * 2 / 9 + unitInfoYOffset && y <= height * 8 / 9 + unitInfoYOffset + tileSize){
					openUnitDetails(2);
					return;
				}else if(x >= (width - buttonWidth) / 2 && x <= (width + buttonWidth) / 2 && y >= height * 7 / 9 && y <= height * 7 / 9 + buttonHeight){
					leaderboardTouch();
					return;
				}else if(!isDeleting && x >= (width - buttonWidth) / 2 && x <= (width + buttonWidth) / 2 && y >= height / 9 && y <= height / 9 + buttonHeight){
					deleteTouch();
					return;
				}else if(isDeleting && x >= (width - buttonWidth) / 2 - tileSize && x <= (width - buttonWidth) / 2 && y >= height / 18 && y <= height / 18 + tileSize){
					isDeleting = false;
				}else if(isDeleting && x >= (width + buttonWidth) / 2 && x <= (width + buttonWidth) / 2 + tileSize && y >= height / 18 && y <= height / 18 + tileSize){
					confirmDelete();
				}
			}
		}else if(Gdx.input.isKeyPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.BACKSPACE)){
			this.game.setScreen(new MainMenuScreen(this.game));
		}
	}
	
	private void backTouch(){
		this.game.setScreen(previous);
	}
	
	private void openUnitDetails(int index){
		if(highscores != null && highscores.highscores.size() > index){
			inspectedIndex = index;
			isUnitDetailsOpen = !isUnitDetailsOpen;
		}
	}
	
	private void leaderboardTouch(){
		this.game.gameServicesController.ViewLeaderboard();
	}
	
	private void deleteTouch(){
		isDeleting = true;
	}
	
	private void confirmDelete(){
		HighscoreManager.EraseHighscores();
		this.highscores = null;
		isDeleting = false;
	}
	
	private boolean ValidateHighscores(){
		if(highscores == null)
			return true;
		
		for(HighscoreDto highscore : highscores.highscores){
			UnitDto unit = highscore.heroUnit;
			
			if(unit.experienceNeeded <= 0)
				return false;
		}
		
		return true;
	}
	
	public String type;
	public String name;
	public int attack;
	public int defense;
	public int evasion;
	public int accuracy;
	public int movement;
	public int maximumHealth;
	public int initialAttack;
	public int initialDefense;
	public int initialEvasion;
	public int initialAccuracy;
	public int initialMovement;
	public int initialHealth;
	public int level;
	public String ability;
	public int unitsKilled;
	public int damageDealt;
	public int roundKilled;
	public LocationDto locationKilled;
	public boolean isMale;
	public String backStory;
	public int team;
	public int currentHealth;
	public int experience;
	public int experienceNeeded;
	public int cost;
	public int id;
	public int x;
	public int y;
	public int maximumRange;
	public int minimumRange;
	public int distanceMoved;
	public boolean hasMoved;
	public boolean hasAttacked;
	public float animationSpeed;
	public boolean isAbilityExhausted;
	public boolean canUseAbility;
	public List<Integer> abilityTargets;

}
