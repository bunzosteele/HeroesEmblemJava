package bunzosteele.heroesemblem.view;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.MusicManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class MainMenuScreen extends MenuScreen
{
	Sprite buttonSprite;
	int xOffset;
	int yOffset;
	int buttonHeight;

	public MainMenuScreen(final HeroesEmblem game)
	{
		super(game);
		this.game.isQuitting = false;
		final AtlasRegion buttonRegion = this.game.textureAtlas.findRegion("Button");	
		buttonHeight = Gdx.graphics.getHeight() / 6;
		xOffset = (Gdx.graphics.getWidth()) / 4;
		yOffset = Gdx.graphics.getHeight() / 4;
		buttonSprite = new Sprite(buttonRegion);
		MusicManager.PlayMenuMusic(this.game.settings.getFloat("musicVolume", .25f));
		game.adsController.showBannerAd();
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
		drawBlueUnits();
		drawRedUnits();
	}
	
	private void drawBlueUnits(){
		int scaledSize = Gdx.graphics.getHeight() / 11;
		final AtlasRegion miregion = game.textureAtlas.findRegion("Mage-Idle-" + idleFrame + "-0");
		final Sprite misprite = new Sprite(miregion);	
		final AtlasRegion maregion = game.textureAtlas.findRegion("Mage-Attack-" + attackFrame + "-0");
		final Sprite masprite = new Sprite(maregion);	
		final AtlasRegion airegion = game.textureAtlas.findRegion("Archer-Idle-" + idleFrame + "-0");
		final Sprite aisprite = new Sprite(airegion);	
		final AtlasRegion aaregion = game.textureAtlas.findRegion("Archer-Attack-" + attackFrame + "-0");
		final Sprite aasprite = new Sprite(aaregion);	
		final AtlasRegion karegion = game.textureAtlas.findRegion("Knight-Attack-" + attackFrame + "-0");
		final Sprite kasprite = new Sprite(karegion);	
		final AtlasRegion firegion = game.textureAtlas.findRegion("Footman-Idle-" + idleFrame + "-0");
		final Sprite fisprite = new Sprite(firegion);	
		final AtlasRegion siregion = game.textureAtlas.findRegion("Spearman-Idle-" + idleFrame + "-0");
		final Sprite sisprite = new Sprite(siregion);	
		final AtlasRegion paregion = game.textureAtlas.findRegion("Priest-Attack-" + attackFrame + "-0");
		final Sprite pasprite = new Sprite(paregion);	
		this.game.batcher.draw(aasprite, this.xOffset / 2 + scaledSize / 2, this.yOffset * 2 + scaledSize * 3 / 2, scaledSize, scaledSize);
		this.game.batcher.draw(aasprite, this.xOffset / 2 + scaledSize * 3 / 2, this.yOffset * 2 + scaledSize * 5 / 4, scaledSize, scaledSize);
		this.game.batcher.draw(masprite, this.xOffset / 3, this.yOffset * 2 + scaledSize * 6 / 5, scaledSize, scaledSize);
		this.game.batcher.draw(aisprite, this.xOffset / 2 + scaledSize, this.yOffset * 2 + scaledSize / 2, scaledSize, scaledSize);
		this.game.batcher.draw(misprite, this.xOffset / 2, this.yOffset * 2 + scaledSize / 2, scaledSize, scaledSize);
		this.game.batcher.draw(misprite, this.xOffset / 4, this.yOffset * 2 + scaledSize / 3, scaledSize, scaledSize);
		this.game.batcher.draw(aisprite, this.xOffset / 4 + scaledSize / 2, this.yOffset * 2 - scaledSize / 3, scaledSize, scaledSize);
		this.game.batcher.draw(aisprite, this.xOffset / 2 + scaledSize / 2, this.yOffset * 2 - scaledSize / 4, scaledSize, scaledSize);
		this.game.batcher.draw(fisprite, this.xOffset * 4 / 5 + scaledSize / 2, this.yOffset * 2 + scaledSize / 4, scaledSize, scaledSize);
		this.game.batcher.draw(fisprite, this.xOffset * 4 / 5, this.yOffset * 2 - scaledSize / 4, scaledSize, scaledSize);
		this.game.batcher.draw(sisprite, this.xOffset * 4 / 5 + scaledSize, this.yOffset * 2 + scaledSize * 4 / 5, scaledSize, scaledSize);
		this.game.batcher.draw(kasprite, this.xOffset + scaledSize * 3 / 4, this.yOffset * 2 + scaledSize / 2, scaledSize, scaledSize);
		this.game.batcher.draw(fisprite, this.xOffset + scaledSize / 3, this.yOffset * 2 - scaledSize / 5, scaledSize, scaledSize);
		this.game.batcher.draw(pasprite, scaledSize, this.yOffset * 2 - scaledSize / 3, scaledSize, scaledSize);
	}
	
	private void drawRedUnits(){
		int scaledSize = Gdx.graphics.getHeight() / 11;
		final AtlasRegion miregion = game.textureAtlas.findRegion("Mage-Idle-" + idleFrame + "-1");
		final Sprite misprite = new Sprite(miregion);	
		final AtlasRegion maregion = game.textureAtlas.findRegion("Mage-Attack-" + attackFrame + "-1");
		final Sprite masprite = new Sprite(maregion);	
		final AtlasRegion airegion = game.textureAtlas.findRegion("Archer-Idle-" + idleFrame + "-1");
		final Sprite aisprite = new Sprite(airegion);	
		final AtlasRegion aaregion = game.textureAtlas.findRegion("Archer-Attack-" + attackFrame + "-1");
		final Sprite aasprite = new Sprite(aaregion);	
		final AtlasRegion karegion = game.textureAtlas.findRegion("Knight-Attack-" + attackFrame + "-1");
		final Sprite kasprite = new Sprite(karegion);	
		final AtlasRegion firegion = game.textureAtlas.findRegion("Footman-Idle-" + idleFrame + "-1");
		final Sprite fisprite = new Sprite(firegion);	
		final AtlasRegion siregion = game.textureAtlas.findRegion("Spearman-Idle-" + idleFrame + "-1");
		final Sprite sisprite = new Sprite(siregion);	
		final AtlasRegion paregion = game.textureAtlas.findRegion("Priest-Attack-" + attackFrame + "-1");
		final Sprite pasprite = new Sprite(paregion);
		this.game.batcher.draw(aasprite, this.xOffset * 4 - (this.xOffset / 2 + scaledSize * 3 / 2), this.yOffset * 2 + scaledSize * 3 / 2, scaledSize, scaledSize);
		this.game.batcher.draw(aasprite, this.xOffset * 4 - (this.xOffset / 2 + scaledSize * 5 / 2), this.yOffset * 2 + scaledSize * 5 / 4, scaledSize, scaledSize);
		this.game.batcher.draw(masprite, this.xOffset * 4 - (this.xOffset / 3 + scaledSize), this.yOffset * 2 + scaledSize * 6 / 5, scaledSize, scaledSize);
		this.game.batcher.draw(aisprite, this.xOffset * 4 - (this.xOffset / 2 + 2 * scaledSize), this.yOffset * 2 + scaledSize / 2, scaledSize, scaledSize);
		this.game.batcher.draw(misprite, this.xOffset * 4 - (this.xOffset / 2 + scaledSize), this.yOffset * 2 + scaledSize / 2, scaledSize, scaledSize);
		this.game.batcher.draw(misprite, this.xOffset * 4 - (this.xOffset / 4 + scaledSize), this.yOffset * 2 + scaledSize / 3, scaledSize, scaledSize);
		this.game.batcher.draw(aisprite, this.xOffset * 4 - (this.xOffset / 4 + scaledSize * 3 / 2), this.yOffset * 2 - scaledSize / 3, scaledSize, scaledSize);
		this.game.batcher.draw(aisprite, this.xOffset * 4 - (this.xOffset / 2 + scaledSize * 3 / 2), this.yOffset * 2 - scaledSize / 4, scaledSize, scaledSize);
		this.game.batcher.draw(fisprite, this.xOffset * 4 - (this.xOffset * 4 / 5 + scaledSize * 3 / 2), this.yOffset * 2 + scaledSize / 4, scaledSize, scaledSize);
		this.game.batcher.draw(fisprite, this.xOffset * 4 - (this.xOffset * 4 / 5 + scaledSize), this.yOffset * 2 - scaledSize / 4, scaledSize, scaledSize);
		this.game.batcher.draw(sisprite, this.xOffset * 4 - (this.xOffset * 4 / 5 + 2 * scaledSize), this.yOffset * 2 + scaledSize * 4 / 5, scaledSize, scaledSize);
		this.game.batcher.draw(kasprite, this.xOffset * 4 - (this.xOffset + scaledSize * 7 / 4), this.yOffset * 2 + scaledSize / 2, scaledSize, scaledSize);
		this.game.batcher.draw(fisprite, this.xOffset * 4 - (this.xOffset + scaledSize * 4 / 3), this.yOffset * 2 - scaledSize / 5, scaledSize, scaledSize);
		this.game.batcher.draw(pasprite, this.xOffset * 4 - scaledSize * 2, this.yOffset * 2 - scaledSize / 3, scaledSize, scaledSize);
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
