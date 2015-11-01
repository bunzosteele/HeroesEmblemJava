package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.HighscoreManager;
import bunzosteele.heroesemblem.model.HighscoreManager.HighscoreDto;
import bunzosteele.heroesemblem.model.HighscoreManager.HighscoresDto;
import bunzosteele.heroesemblem.model.Units.UnitDto;
import bunzosteele.heroesemblem.model.Units.UnitType;
import bunzosteele.heroesemblem.model.MusicManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class HighscoreScreen extends ScreenAdapter
{
	HeroesEmblem game;
	Sprite buttonSprite;
	Sprite pedestalSprite;
	Sprite backgroundSprite;
	HighscoresDto highscores;
	int xOffset;
	int yOffset;
	float headerOffset;
	float tableRowHeight;
	float pedestalSize;
	int buttonHeight;
	int idleFrame;
	int attackFrame;
	int selected;

	public HighscoreScreen(final HeroesEmblem game)
	{
		this.game = game;
		final AtlasRegion buttonRegion = this.game.textureAtlas.findRegion("Button");	
		final AtlasRegion pedestalRegion = this.game.textureAtlas.findRegion("Pedestal");
		final AtlasRegion backgroundRegion = this.game.textureAtlas.findRegion("Grass");
		this.buttonHeight = Gdx.graphics.getHeight() / 6;
		this.xOffset = (Gdx.graphics.getWidth()) / 4;
		this.yOffset = Gdx.graphics.getHeight() / 4;
		this.headerOffset = this.yOffset * 4 - this.buttonHeight - this.game.font.getData().lineHeight;
		this.tableRowHeight = (this.headerOffset - 2 * this.game.font.getData().lineHeight) / 3;
		this.pedestalSize = this.tableRowHeight - this.game.font.getData().lineHeight;
		this.buttonSprite = new Sprite(buttonRegion);
		this.pedestalSprite = new Sprite(pedestalRegion);
		this.backgroundSprite = new Sprite(backgroundRegion);
		this.highscores = HighscoreManager.GetExistingHighscores();
		this.selected = -1;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				++idleFrame;
				++attackFrame;
				if (attackFrame > 2){
					attackFrame = 1;
				}
				if (idleFrame > 3)
				{
					idleFrame = 1;
				}
			}
		}, 0, 1 / 3f);
		game.adsController.hideBannerAd();
	}

	public void draw()
	{
		final GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.game.batcher.begin();
		for(int i = 0; i < 33; i++){
			for(int j = 0; j < 19; j++){
				this.game.batcher.draw(backgroundSprite, (Gdx.graphics.getWidth() / 32) * i, (Gdx.graphics.getHeight() / 18) * j, (Gdx.graphics.getWidth() / 32), (Gdx.graphics.getHeight() / 18));
			}
		}
		this.game.batcher.draw(buttonSprite, this.xOffset * 3 - this.game.font.getData().lineHeight, headerOffset, this.xOffset, this.buttonHeight);
		this.game.batcher.draw(this.pedestalSprite, 2 * this.xOffset + this.pedestalSize, this.tableRowHeight * 2 + this.game.font.getData().lineHeight / 2, this.pedestalSize, this.pedestalSize);
		this.game.batcher.draw(this.pedestalSprite, 2 * this.xOffset + this.pedestalSize, this.tableRowHeight + this.game.font.getData().lineHeight / 2, this.pedestalSize, this.pedestalSize);
		this.game.batcher.draw(this.pedestalSprite, 2 * this.xOffset + this.pedestalSize, this.game.font.getData().lineHeight / 2, this.pedestalSize, this.pedestalSize);
		this.game.font.draw(this.game.batcher, "Main Menu", this.xOffset * 3 - this.game.font.getData().lineHeight, headerOffset + this.buttonHeight - this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.getData().setScale(.66f);
		this.game.font.draw(this.game.batcher, "Highscores", this.xOffset, this.yOffset * 4 - this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.draw(this.game.batcher, "1", 0, this.tableRowHeight * 3 - this.game.font.getData().lineHeight / 2, (float) this.xOffset, 1, false);
		this.game.font.draw(this.game.batcher, "2", 0, this.tableRowHeight * 2 - this.game.font.getData().lineHeight / 2, (float) this.xOffset, 1, false);
		this.game.font.draw(this.game.batcher, "3", 0, this.tableRowHeight * 1 - this.game.font.getData().lineHeight / 2, (float) this.xOffset, 1, false);	
		this.game.font.getData().setScale(.33f);
		this.game.font.draw(this.game.batcher, "Score:", this.xOffset, this.headerOffset - this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.draw(this.game.batcher, "Hero:", 2 * this.xOffset, this.headerOffset - this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);

		if(highscores != null){
			int heightOffset = 0;
			for(HighscoreDto highscore : highscores.highscores){
				this.game.font.getData().setScale(.66f);
				this.game.font.draw(this.game.batcher, "" + highscore.roundsSurvived, this.xOffset, this.tableRowHeight * (3 - heightOffset) - this.game.font.getData().lineHeight / 2, (float) this.xOffset, 1, false);
				this.game.font.getData().setScale(.33f);
				if(heightOffset == selected){
					final AtlasRegion region = game.textureAtlas.findRegion(highscore.heroUnit.type + "-Attack-" + attackFrame + "-0");
					final Sprite sprite = new Sprite(region);
					this.game.batcher.draw(sprite, 2 * this.xOffset + this.pedestalSize+ pedestalSize / 10,  (3 - heightOffset - 1) * this.tableRowHeight + pedestalSize / 10 + this.game.font.getData().lineHeight / 2, this.pedestalSize * 8 / 10, this.pedestalSize * 8 / 10);
				}
				else{
					final AtlasRegion region = game.textureAtlas.findRegion(highscore.heroUnit.type + "-Idle-" + idleFrame + "-0");
					final Sprite sprite = new Sprite(region);
					this.game.batcher.draw(sprite, 2 * this.xOffset + this.pedestalSize + pedestalSize / 10,  (3 - heightOffset - 1) * this.tableRowHeight + pedestalSize / 10 + this.game.font.getData().lineHeight / 2, this.pedestalSize * 8 / 10, this.pedestalSize * 8 / 10);
				}
				++heightOffset;
			}
		}
		
		if(this.selected > -1){
			if(this.highscores != null && this.selected < this.highscores.highscores.size() && this.highscores.highscores.get(selected) != null){
				UnitDto unit = this.highscores.highscores.get(selected).heroUnit;
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
				this.game.font.getData().setScale(.33f);
			}
		}
		
		this.game.batcher.end();
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
			if(flippedY >= this.yOffset * 4 - this.buttonHeight - this.game.font.getData().lineHeight)
			{
				checkMainMenuTouch(Gdx.input.getX(), flippedY);
				return;
			}else{
				checkHeroTouch(Gdx.input.getX(), flippedY);
			}
		}
	}

	private void checkMainMenuTouch(int x, int y){
		if((x >= this.xOffset * 3 - this.game.font.getData().lineHeight && x <= (this.xOffset * 3 - this.game.font.getData().lineHeight) + this.xOffset) && (y >= this.yOffset * 4 - this.buttonHeight - this.game.font.getData().lineHeight && y <= this.yOffset * 4 - this.game.font.getData().lineHeight))
		this.game.setScreen(new MainMenuScreen(this.game));
	}
	
	private void checkHeroTouch(int x, int y){
		if(x >= 2 * this.xOffset + this.pedestalSize && x <= 2 * this.xOffset + 2 *this.pedestalSize){
			if(y >= this.game.font.getData().lineHeight / 2 && y <= (this.game.font.getData().lineHeight / 2) + this.pedestalSize){
				this.selected = 2;
			}
			if(y >= this.tableRowHeight + this.game.font.getData().lineHeight / 2 && y <= (this.tableRowHeight + this.game.font.getData().lineHeight / 2) + this.pedestalSize){
				this.selected = 1;
			}
			if(y >= this.tableRowHeight * 2 + this.game.font.getData().lineHeight / 2 && y <= (this.tableRowHeight * 2 + this.game.font.getData().lineHeight / 2) + this.pedestalSize){
				this.selected = 0;
			}
		}
	}
	

}
