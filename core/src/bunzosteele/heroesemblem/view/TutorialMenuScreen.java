package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.HighscoreManager;
import bunzosteele.heroesemblem.model.MusicManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class TutorialMenuScreen extends ScreenAdapter
{
	HeroesEmblem game;
	Sprite buttonSprite;
	Sprite backgroundSprite;
	int xOffset;
	int yOffset;
	int buttonHeight;

	public TutorialMenuScreen(final HeroesEmblem game)
	{
		this.game = game;
		final AtlasRegion buttonRegion = this.game.textureAtlas.findRegion("Button");	
		final AtlasRegion backgroundRegion = this.game.textureAtlas.findRegion("Grass");
		this.buttonHeight = Gdx.graphics.getHeight() / 6;
		this.xOffset = (Gdx.graphics.getWidth()) / 4;
		this.yOffset = Gdx.graphics.getHeight() / 4;
		this.buttonSprite = new Sprite(buttonRegion);
		this.backgroundSprite = new Sprite(backgroundRegion);
		game.adsController.hideBannerAd();
	}

	public void draw()
	{
		this.game.font.getData().setScale(.33f);
		final GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.game.batcher.begin();
		for(int i = 0; i < 33; i++){
			for(int j = 0; j < 19; j++){
				this.game.batcher.draw(backgroundSprite, (Gdx.graphics.getWidth() / 32) * i, (Gdx.graphics.getHeight() / 18) * j, (Gdx.graphics.getWidth() / 32), (Gdx.graphics.getHeight() / 18));
			}
		}
		this.game.batcher.draw(buttonSprite, this.xOffset * 3 - this.game.font.getData().lineHeight, this.yOffset * 4 - this.buttonHeight - this.game.font.getData().lineHeight, this.xOffset, this.buttonHeight);
		this.game.batcher.draw(buttonSprite, this.xOffset / 4, this.yOffset, this.xOffset, this.buttonHeight);
		this.game.batcher.draw(buttonSprite, this.xOffset * 3 / 2, this.yOffset, this.xOffset, this.buttonHeight);
		this.game.batcher.draw(buttonSprite, this.xOffset * 11 / 4, this.yOffset, this.xOffset, this.buttonHeight);
		this.game.font.getData().setScale(.66f);
		this.game.font.draw(this.game.batcher, "How To Play:", this.xOffset, this.yOffset * 4 - this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.getData().setScale(.33f);
		this.game.font.draw(this.game.batcher, "Main Menu", this.xOffset * 3 - this.game.font.getData().lineHeight, this.yOffset * 4 - 2 * this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.draw(this.game.batcher, "Shop", this.xOffset / 4, this.yOffset + (buttonHeight - this.game.font.getData().lineHeight), (float) this.xOffset, 1, false);
		this.game.font.draw(this.game.batcher, "Units", this.xOffset * 3 / 2, this.yOffset + (buttonHeight - this.game.font.getData().lineHeight), (float) this.xOffset, 1, false);
		this.game.font.draw(this.game.batcher, "Battle", this.xOffset * 11 / 4, this.yOffset + (buttonHeight - this.game.font.getData().lineHeight), (float) this.xOffset, 1, false);
		this.game.font.draw(this.game.batcher, "Build your army. Survive.", this.xOffset, this.yOffset * 3 -  this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
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
			}	
			if ((Gdx.input.getX() > xOffset / 4 && Gdx.input.getX() < xOffset / 4 + this.xOffset) && (Gdx.graphics.getHeight() - Gdx.input.getY() > this.yOffset && Gdx.graphics.getHeight() - Gdx.input.getY() < this.yOffset + this.buttonHeight))
			{
				this.game.setScreen(new TutorialShopFirstScreen(this.game));
				return;
			}
			
			if ((Gdx.input.getX() > this.xOffset * 3 / 2 && Gdx.input.getX() < this.xOffset * 3 / 2 + this.xOffset) && (Gdx.graphics.getHeight() - Gdx.input.getY() > this.yOffset && Gdx.graphics.getHeight() - Gdx.input.getY() < this.yOffset + this.buttonHeight))
			{
				this.game.setScreen(new TutorialUnitFirstScreen(this.game));
				return;
			}
			
			if ((Gdx.input.getX() > this.xOffset * 11 / 4 && Gdx.input.getX() < this.xOffset * 11 / 4 + this.xOffset) && (Gdx.graphics.getHeight() - Gdx.input.getY() > this.yOffset && Gdx.graphics.getHeight() - Gdx.input.getY() < this.yOffset + this.buttonHeight))
			{
				this.game.setScreen(new TutorialBattleFirstScreen(this.game));
				return;
			}
		}
	}


	private void checkMainMenuTouch(int x, int y){
		if((x >= this.xOffset * 3 - this.game.font.getData().lineHeight && x <= (this.xOffset * 3 - this.game.font.getData().lineHeight) + this.xOffset) && (y >= this.yOffset * 4 - this.buttonHeight - this.game.font.getData().lineHeight && y <= this.yOffset * 4 - this.game.font.getData().lineHeight))
		this.game.setScreen(new MainMenuScreen(this.game));
	}
	

}
