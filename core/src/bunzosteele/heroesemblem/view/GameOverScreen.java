package bunzosteele.heroesemblem.view;

import java.io.IOException;
import java.util.List;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.HighscoreManager;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitDto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class GameOverScreen extends ScreenAdapter
{
	HeroesEmblem game;
	int roundsSurvived;
	List<UnitDto> graveyard;
	UnitDto hero;
	int xOffset;
	int headerOffset;
	int idleFrame;

	public GameOverScreen(final HeroesEmblem game, int roundsSurvived, List<UnitDto> graveyard)
	{
		this.game = game;
		this.roundsSurvived = roundsSurvived;
		this.graveyard = graveyard;
		this.hero = GetHero(graveyard);
		this.xOffset = Gdx.graphics.getWidth() / 4;
		this.headerOffset = 5 * Gdx.graphics.getHeight() / 6;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				++idleFrame;
				if (idleFrame > 3)
				{
					idleFrame = 1;
				}
			}
		}, 0, 1 / 3f);
		if(game.adsController.isWifiConnected())
			game.adsController.showBannerAd();
	}

	public void draw()
	{
		final GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.game.batcher.begin();
		if(this.hero == null){
			this.game.font.draw(this.game.batcher, "You resigned before anyone even died.", this.xOffset, this.headerOffset, xOffset * 2, 1, false);
			this.game.font.draw(this.game.batcher, "Quitter.", this.xOffset, this.headerOffset - this.game.font.getData().lineHeight, xOffset * 2, 1, false);
		}else{
			this.game.font.draw(this.game.batcher, "Rounds Survived: " + this.roundsSurvived, this.xOffset, this.headerOffset, xOffset * 2, 1, false);
			final AtlasRegion region = game.textureAtlas.findRegion(this.hero.type + "-Idle-" + idleFrame + "-0");
			final Sprite sprite = new Sprite(region);
			this.game.font.draw(this.game.batcher, "Your hero: " + this.hero.name, this.xOffset, this.headerOffset - this.game.font.getData().lineHeight, xOffset * 2, 1, false);
			this.game.font.getData().setScale(.2f);
			this.game.batcher.draw(sprite, 2 * this.xOffset - (3 * this.game.font.getData().lineHeight / 2) , this.headerOffset - 6 * this.game.font.getData().lineHeight, 3 * this.game.font.getData().lineHeight, 3 * this.game.font.getData().lineHeight);
			this.game.font.draw(this.game.batcher, "Level " + this.hero.level + " " + this.hero.type,this.xOffset, this.headerOffset - 7 * this.game.font.getData().lineHeight, xOffset * 2, 1, false);
			this.game.font.draw(this.game.batcher, "Kills:" + this.hero.unitsKilled, this.xOffset, this.headerOffset - 8 * this.game.font.getData().lineHeight, xOffset * 2, 1, false);
			this.game.font.draw(this.game.batcher, "Damage:" + this.hero.damageDealt, this.xOffset, this.headerOffset - 9 * this.game.font.getData().lineHeight, xOffset * 2, 1, false);
			this.game.font.draw(this.game.batcher, "HP:" + this.hero.maximumHealth, this.xOffset, this.headerOffset - 10 * this.game.font.getData().lineHeight, xOffset * 2, 1, false);
			this.game.font.draw(this.game.batcher, "ATK:" + this.hero.attack, this.xOffset, this.headerOffset - 11 * this.game.font.getData().lineHeight, xOffset * 2, 1, false);
			this.game.font.draw(this.game.batcher, "DEF:" + this.hero.defense, this.xOffset, this.headerOffset - 12 * this.game.font.getData().lineHeight, xOffset * 2, 1, false);
			this.game.font.draw(this.game.batcher, "EVP:" + this.hero.evasion, this.xOffset, this.headerOffset - 13 * this.game.font.getData().lineHeight, xOffset * 2, 1, false);
			this.game.font.draw(this.game.batcher, "ACC:" + this.hero.accuracy, this.xOffset, this.headerOffset - 14 * this.game.font.getData().lineHeight, xOffset * 2, 1, false);
			this.game.font.draw(this.game.batcher, "MOVE:" + this.hero.movement, this.xOffset, this.headerOffset - 15 * this.game.font.getData().lineHeight, xOffset * 2, 1, false);
			if(this.hero.ability != null){
				this.game.font.draw(this.game.batcher, "Ability:" + this.hero.ability, this.xOffset, this.headerOffset - 16 * this.game.font.getData().lineHeight, xOffset * 2, 1, false);
			}else{
				this.game.font.draw(this.game.batcher, "Ability: None", this.xOffset, this.headerOffset - 16 * this.game.font.getData().lineHeight, xOffset * 2, 1, false);
			}
			this.game.font.getData().setScale(.33f);	
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
			String action = this.game.isQuitting ? "Quit" : "Defeated";
			Json json = new Json();
			String parsedGraveyard = json.toJson(this.graveyard);			
			this.game.analyticsController.RecordEvent("GameOver", action, parsedGraveyard, (long) roundsSurvived);
			if(this.hero != null)
				HighscoreManager.RecordGame(roundsSurvived, hero);
			this.game.setScreen(new MainMenuScreen(this.game));
		}
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
}
