package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.HighscoreManager;
import bunzosteele.heroesemblem.model.HighscoreManager.HighscoreDto;
import bunzosteele.heroesemblem.model.HighscoreManager.HighscoresDto;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitDto;
import bunzosteele.heroesemblem.model.Units.UnitType;
import bunzosteele.heroesemblem.model.MusicManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.XmlReader.Element;

public class HighscoreScreen extends MenuScreen
{
	HighscoresDto highscores;
	int xOffset;
	int yOffset;
	float headerOffset;
	float tableRowHeight;
	float pedestalSize;
	int buttonHeight;
	int selected;
	boolean readingLegend = false;
	float legendHeight;

	public HighscoreScreen(final HeroesEmblem game)
	{
		super(game);
		this.buttonHeight = Gdx.graphics.getHeight() / 6;
		this.xOffset = (Gdx.graphics.getWidth()) / 4;
		this.yOffset = Gdx.graphics.getHeight() / 4;
		this.headerOffset = this.yOffset * 4 - this.buttonHeight - this.game.font.getData().lineHeight;
		this.tableRowHeight = (this.headerOffset - 3 * this.game.font.getData().lineHeight) / 3;
		this.pedestalSize = this.tableRowHeight - this.game.font.getData().lineHeight;
		this.highscores = HighscoreManager.GetExistingHighscores();
		MusicManager.PlayMenuMusic(this.game.settings.getFloat("musicVolume", .25f));
		this.selected = -1;
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
		//this.game.batcher.draw(buttonSprite, this.xOffset * 3 - this.game.font.getData().lineHeight, headerOffset, this.xOffset, this.buttonHeight);
		this.game.font.draw(this.game.batcher, "Main Menu", this.xOffset * 3 - this.game.font.getData().lineHeight, headerOffset + this.buttonHeight - this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.getData().setScale(.66f);
		this.game.font.draw(this.game.batcher, "Highscores", this.xOffset, this.yOffset * 4 - this.game.font.getData().lineHeight / 2, (float) this.xOffset, 1, false);
		this.game.font.getData().setScale(.33f);
		//this.game.batcher.draw(buttonSprite, this.xOffset / 2, this.yOffset * 3 - this.buttonHeight * 1 / 6, this.xOffset * 2, this.buttonHeight * 2 / 3);
		this.game.font.draw(this.game.batcher, "View Leaderboards", this.xOffset / 2, this.yOffset * 3 + this.buttonHeight * 3 / 12, xOffset * 2, 1, false);
		if(!this.readingLegend){
			//this.game.batcher.draw(this.pedestalSprite, 2 * this.xOffset + this.pedestalSize, this.tableRowHeight * 2 + this.game.font.getData().lineHeight * 3 / 2, this.pedestalSize, this.pedestalSize);
			//this.game.batcher.draw(this.pedestalSprite, 2 * this.xOffset + this.pedestalSize, this.tableRowHeight + this.game.font.getData().lineHeight * 3 / 2, this.pedestalSize, this.pedestalSize);
			//this.game.batcher.draw(this.pedestalSprite, 2 * this.xOffset + this.pedestalSize, this.game.font.getData().lineHeight * 3 / 2, this.pedestalSize, this.pedestalSize);
			this.game.font.getData().setScale(.66f);
			this.game.font.draw(this.game.batcher, "1", 0, this.tableRowHeight * 3, (float) this.xOffset, 1, false);
			this.game.font.draw(this.game.batcher, "2", 0, this.tableRowHeight * 2, (float) this.xOffset, 1, false);
			this.game.font.draw(this.game.batcher, "3", 0, this.tableRowHeight * 1, (float) this.xOffset, 1, false);	
			this.game.font.getData().setScale(.33f);
			this.game.font.draw(this.game.batcher, "Score:", this.xOffset, this.headerOffset - this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
			this.game.font.draw(this.game.batcher, "Hero:", 2 * this.xOffset, this.headerOffset - this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
			if(highscores != null){
				int heightOffset = 0;
				for(HighscoreDto highscore : highscores.highscores){
					this.game.font.getData().setScale(.66f);
					this.game.font.draw(this.game.batcher, "" + highscore.score, this.xOffset, this.tableRowHeight * (3 - heightOffset), (float) this.xOffset, 1, false);
					this.game.font.getData().setScale(.33f);
					if(heightOffset == selected){
						final AtlasRegion region = UnitRenderer.UnitSheets.get(UnitType.valueOf(highscore.heroUnit.type)).findRegion("Idle-0-" + idleFrame);
						final Sprite sprite = new Sprite(region);
						this.game.batcher.draw(sprite, 2 * this.xOffset + this.pedestalSize+ pedestalSize / 10,  (3 - heightOffset - 1) * this.tableRowHeight + pedestalSize / 10 + this.game.font.getData().lineHeight * 3 / 2, this.pedestalSize * 8 / 10, this.pedestalSize * 8 / 10);
					}
					else{
						final AtlasRegion region = UnitRenderer.UnitSheets.get(UnitType.valueOf(highscore.heroUnit.type)).findRegion("Idle-0-" + idleFrame);
						final Sprite sprite = new Sprite(region);
						this.game.batcher.draw(sprite, 2 * this.xOffset + this.pedestalSize + pedestalSize / 10,  (3 - heightOffset - 1) * this.tableRowHeight + pedestalSize / 10 + this.game.font.getData().lineHeight * 3 / 2, this.pedestalSize * 8 / 10, this.pedestalSize * 8 / 10);
					}
					++heightOffset;
				}
			}
		}
		
		if(this.selected > -1){
			if(this.highscores != null && this.selected < this.highscores.highscores.size() && this.highscores.highscores.get(selected) != null){
				UnitDto unit = this.highscores.highscores.get(selected).heroUnit;
				if(this.readingLegend){
					this.game.font.getData().setScale(.25f);
					this.game.font.draw(this.game.batcher, unit.backStory, this.xOffset / 2, this.headerOffset - 2 * this.game.font.getData().lineHeight, (float) this.xOffset * 2, 1, true);
					this.game.font.getData().setScale(.33f);
				}
				this.game.font.draw(this.game.batcher, unit.name, 3 * this.xOffset, this.headerOffset - this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
				this.game.font.getData().setScale(.2f);
				this.game.font.draw(this.game.batcher, "Level " + unit.level + " " + unit.type, 3 * this.xOffset, this.headerOffset - 5 * this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
				this.game.font.draw(this.game.batcher, "Kills:" + unit.unitsKilled, 3 * this.xOffset, this.headerOffset - 6 * this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
				this.game.font.draw(this.game.batcher, "Damage:" + unit.damageDealt, 3 * this.xOffset, this.headerOffset - 7 * this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
				this.game.font.draw(this.game.batcher, "HP:" + unit.maximumHealth, 3 * this.xOffset, this.headerOffset - 8 * this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
				this.game.font.draw(this.game.batcher, "ATK:" + unit.attack, 3 * this.xOffset, this.headerOffset - 9 * this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
				this.game.font.draw(this.game.batcher, "DEF:" + unit.defense, 3 * this.xOffset, this.headerOffset - 10 * this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
				this.game.font.draw(this.game.batcher, "EVP:" + unit.evasion, 3 * this.xOffset, this.headerOffset - 11 * this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
				this.game.font.draw(this.game.batcher, "ACC:" + unit.accuracy, 3 * this.xOffset, this.headerOffset - 12 * this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
				this.game.font.draw(this.game.batcher, "MOVE:" + unit.movement, 3 * this.xOffset, this.headerOffset - 13 * this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
				this.game.font.draw(this.game.batcher, "Ability:" + unit.ability, 3 * this.xOffset, this.headerOffset - 14 * this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
				this.legendHeight = this.headerOffset - 17 * this.game.font.getData().lineHeight;
				if(this.readingLegend){
					//this.game.batcher.draw(activeButton, this.xOffset * 13 / 4, this.headerOffset - 17 * this.game.font.getData().lineHeight, this.xOffset / 2, this.buttonHeight / 2);
				}else{
					if(unit.backStory == null){
						//this.game.batcher.draw(inactiveButton, this.xOffset * 13 / 4, this.headerOffset - 17 * this.game.font.getData().lineHeight, this.xOffset / 2, this.buttonHeight / 2);						
					}else{
						//this.game.batcher.draw(buttonSprite, this.xOffset * 13 / 4, this.headerOffset - 17 * this.game.font.getData().lineHeight, this.xOffset / 2, this.buttonHeight / 2);						
					}
				}
				this.game.font.draw(this.game.batcher, "Legend", 3 * this.xOffset, this.headerOffset - this.game.font.getData().lineHeight * 31 / 2, (float) this.xOffset, 1, false);
				this.game.font.getData().setScale(.33f);
			}
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
		this.draw();
	}

	public void update() throws IOException
	{
		if (Gdx.input.justTouched())
		{
			int flippedY = Gdx.graphics.getHeight() - Gdx.input.getY();
			int x = Gdx.input.getX();
			if((x >= this.xOffset * 3 - this.game.font.getData().lineHeight && x <= (this.xOffset * 3 - this.game.font.getData().lineHeight) + this.xOffset) && (flippedY >= this.yOffset * 4 - this.buttonHeight - this.game.font.getData().lineHeight && flippedY <= this.yOffset * 4 - this.game.font.getData().lineHeight)){
				mainMenuTouch();
				return;
			}else if(x >= 2 * this.xOffset + this.pedestalSize && x <= 2 * this.xOffset + 2 * this.pedestalSize){
				checkHeroTouch(x, flippedY);
				return;
			}else if(this.selected >= 0 && this.highscores != null && this.highscores.highscores.size() > selected && this.highscores.highscores.get(selected) != null && this.highscores.highscores.get(selected).heroUnit.backStory != null && x >= this.xOffset * 13 / 4 && x <= this.xOffset * 13 / 4 + this.xOffset / 2 && flippedY >= this.legendHeight && flippedY <= this.legendHeight + this.buttonHeight){
				legendTouch();
				return;
			}else if(x >= xOffset /2 && x <= xOffset / 2 + 2 * xOffset && flippedY >= this.yOffset * 3 - this.buttonHeight * 1 / 6 && flippedY <= this.yOffset * 3 - this.buttonHeight * 1 / 6 + this.buttonHeight * 2 / 3){
				leaderboardTouch();
				return;
			}
			
			this.selected = -1;
			this.readingLegend = false;
		}else if(Gdx.input.isKeyPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.BACKSPACE)){
			this.game.setScreen(new MainMenuScreen(this.game));
		}
	}

	private void mainMenuTouch(){
		this.game.setScreen(new MainMenuScreen(this.game));
	}
	
	private void checkHeroTouch(int x, int y){
		if(!readingLegend){
			if(y >= this.game.font.getData().lineHeight * 3 / 2 && y <= (this.game.font.getData().lineHeight * 3 / 2) + this.pedestalSize){
				this.selected = 2;
			}else if(y >= this.tableRowHeight + this.game.font.getData().lineHeight * 3 / 2 && y <= (this.tableRowHeight + this.game.font.getData().lineHeight * 3 / 2) + this.pedestalSize){
				this.selected = 1;
			}else if(y >= this.tableRowHeight * 2 + this.game.font.getData().lineHeight * 3 / 2 && y <= (this.tableRowHeight * 2 + this.game.font.getData().lineHeight * 3 / 2) + this.pedestalSize){
				this.selected = 0;
			}
		}
	}
	
	private void legendTouch(){
		this.readingLegend = !this.readingLegend;
	}
	
	private void leaderboardTouch(){
		this.game.gameServicesController.ViewLeaderboard();
	}
	

}
