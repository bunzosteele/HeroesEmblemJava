package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.MusicManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class MainMenuScreen extends ScreenAdapter
{
	HeroesEmblem game;
	Sprite buttonSprite;
	Sprite blueTeamSprite;
	Sprite redTeamSprite;
	Sprite backgroundSprite;
	int xOffset;
	int yOffset;
	int buttonHeight;

	public MainMenuScreen(final HeroesEmblem game)
	{
		this.game = game;
		this.game.isQuitting = false;
		final AtlasRegion buttonRegion = this.game.textureAtlas.findRegion("Button");	
		final AtlasRegion blueRegion = this.game.textureAtlas.findRegion("BlueTeam");
		final AtlasRegion redRegion = this.game.textureAtlas.findRegion("RedTeam");
		final AtlasRegion backgroundRegion = this.game.textureAtlas.findRegion("Grass");
		buttonHeight = Gdx.graphics.getHeight() / 6;
		xOffset = (Gdx.graphics.getWidth()) / 4;
		yOffset = Gdx.graphics.getHeight() / 4;
		buttonSprite = new Sprite(buttonRegion);
		blueTeamSprite = new Sprite(blueRegion);
		redTeamSprite = new Sprite(redRegion);
		backgroundSprite = new Sprite(backgroundRegion);
		MusicManager.PlayMenuMusic(this.game.settings.getFloat("musicVolume", .25f));
		if(game.adsController.isWifiConnected())
			game.adsController.showBannerAd();
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
		this.game.batcher.draw(blueTeamSprite, this.xOffset / 4, this.yOffset * 2, this.xOffset, this.xOffset * 66 / 130);
		this.game.batcher.draw(redTeamSprite, this.xOffset * 11 / 4, this.yOffset * 2, this.xOffset, this.xOffset * 62 / 129);
		this.game.batcher.draw(buttonSprite, this.xOffset * 3 / 2, this.yOffset * 2, this.xOffset, this.buttonHeight);
		this.game.batcher.draw(buttonSprite, this.xOffset / 4, this.yOffset, this.xOffset, this.buttonHeight);
		this.game.batcher.draw(buttonSprite, this.xOffset * 3 / 2, this.yOffset, this.xOffset, this.buttonHeight);
		this.game.batcher.draw(buttonSprite, this.xOffset * 11 / 4, this.yOffset, this.xOffset, this.buttonHeight);
		this.game.font.getData().setScale(1f);
		this.game.font.draw(this.game.batcher, "Heroes Emblem", 0, Gdx.graphics.getHeight() - this.game.font.getData().lineHeight / 2, Gdx.graphics.getWidth(), 1, false);
		this.game.font.getData().setScale(.33f);
		this.game.font.draw(this.game.batcher, "New Game", this.xOffset * 3 / 2, this.yOffset * 2 + (buttonHeight - this.game.font.getData().lineHeight), (float) this.xOffset, 1, false);
		this.game.font.draw(this.game.batcher, "Settings", this.xOffset / 4, this.yOffset + (buttonHeight - this.game.font.getData().lineHeight), (float) this.xOffset, 1, false);
		this.game.font.draw(this.game.batcher, "Highscores", this.xOffset * 3 / 2, this.yOffset + (buttonHeight - this.game.font.getData().lineHeight), (float) this.xOffset, 1, false);
		this.game.font.draw(this.game.batcher, "Tutorial", this.xOffset * 11 / 4, this.yOffset + (buttonHeight - this.game.font.getData().lineHeight), (float) this.xOffset, 1, false);
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
			if ((Gdx.input.getX() > this.xOffset * 3 / 2 && Gdx.input.getX() < this.xOffset * 3 / 2 + this.xOffset) && (Gdx.graphics.getHeight() - Gdx.input.getY() > this.yOffset * 2 && Gdx.graphics.getHeight() - Gdx.input.getY() < this.yOffset * 2 + buttonHeight))
			{
				this.game.setScreen(new ShopScreen(this.game));
				return;
			}
			
			if ((Gdx.input.getX() > xOffset / 4 && Gdx.input.getX() < xOffset / 4 + this.xOffset) && (Gdx.graphics.getHeight() - Gdx.input.getY() > this.yOffset && Gdx.graphics.getHeight() - Gdx.input.getY() < this.yOffset + this.buttonHeight))
			{
				this.game.setScreen(new SettingsScreen(this.game));
				return;
			}
			
			if ((Gdx.input.getX() > this.xOffset * 3 / 2 && Gdx.input.getX() < this.xOffset * 3 / 2 + this.xOffset) && (Gdx.graphics.getHeight() - Gdx.input.getY() > this.yOffset && Gdx.graphics.getHeight() - Gdx.input.getY() < this.yOffset + this.buttonHeight))
			{
				this.game.setScreen(new HighscoreScreen(this.game));
				return;
			}
			
			if ((Gdx.input.getX() > this.xOffset * 11 / 4 && Gdx.input.getX() < this.xOffset * 11 / 4 + this.xOffset) && (Gdx.graphics.getHeight() - Gdx.input.getY() > this.yOffset && Gdx.graphics.getHeight() - Gdx.input.getY() < this.yOffset + this.buttonHeight))
			{
				this.game.setScreen(new TutorialMenuScreen(this.game));
				return;
			}
		}
	}
}
