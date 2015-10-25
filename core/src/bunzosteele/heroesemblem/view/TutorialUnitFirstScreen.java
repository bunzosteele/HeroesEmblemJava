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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class TutorialUnitFirstScreen extends ScreenAdapter
{
	HeroesEmblem game;
	Sprite buttonSprite;
	Sprite backgroundSprite;
	int xOffset;
	int yOffset;
	int buttonHeight;
	int idleFrame;

	public TutorialUnitFirstScreen(final HeroesEmblem game)
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
		final AtlasRegion region = game.textureAtlas.findRegion("Footman-Idle-" + idleFrame + "-0");
		final Sprite sprite = new Sprite(region);
		
		this.game.batcher.draw(buttonSprite, this.xOffset * 3 - this.game.font.getData().lineHeight, this.yOffset * 4 - this.buttonHeight - this.game.font.getData().lineHeight, this.xOffset, this.buttonHeight);
		this.game.batcher.draw(buttonSprite, this.xOffset * 3 / 2, this.yOffset - this.buttonHeight, this.xOffset, this.buttonHeight);
		this.game.font.getData().setScale(.66f);
		this.game.font.draw(this.game.batcher, "Units: Footman", this.xOffset, this.yOffset * 4 - this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.getData().setScale(.33f);
		this.game.font.draw(this.game.batcher, "Abilities:", this.xOffset * 3 / 2, this.yOffset * 3 - this.game.font.getData().lineHeight * 5 / 2, this.xOffset * 5 / 2, -1, true);
		this.game.font.draw(this.game.batcher, "Back", this.xOffset * 3 - this.game.font.getData().lineHeight, this.yOffset * 4 - 2 * this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.draw(this.game.batcher, "Next", this.xOffset * 3 / 2, this.yOffset - this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.getData().setScale(.25f);
		this.game.batcher.draw(sprite, this.xOffset / 2, this.yOffset + this.buttonHeight / 5,  this.xOffset, this.xOffset);
		this.game.font.draw(this.game.batcher, "The footman is a beefy melee unit.", this.xOffset * 3 / 2, this.yOffset * 3, 2 * this.xOffset, 1, true);
		this.game.font.draw(this.game.batcher, "It has high defense, and the highest base attack.", this.xOffset * 3 / 2, this.yOffset * 3 - 1 * this.game.font.getData().lineHeight, 2 * this.xOffset, 1, true);
		this.game.font.draw(this.game.batcher, "Block: Chance to block all incoming damage.", this.xOffset * 3 / 2, this.yOffset * 3 - 5 * this.game.font.getData().lineHeight, this.xOffset * 5 / 2, -1, true);
		this.game.font.draw(this.game.batcher, "Shield Bash: Deal high damage, and stun target for one turn.", this.xOffset * 3 / 2, this.yOffset * 3 - 7 * this.game.font.getData().lineHeight, this.xOffset * 5 / 2, -1, true);
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
				checkBackTouch(Gdx.input.getX(), flippedY);
				return;
			}	
			if(flippedY <= this.yOffset)
			{
				checkNextTouch(Gdx.input.getX(), flippedY);
				return;
			}		
		}
	}

	private void checkBackTouch(int x, int y){
		if((x >= this.xOffset * 3 - this.game.font.getData().lineHeight && x <= (this.xOffset * 3 - this.game.font.getData().lineHeight) + this.xOffset) && (y >= this.yOffset * 4 - this.buttonHeight - this.game.font.getData().lineHeight && y <= this.yOffset * 4 - this.game.font.getData().lineHeight))
			this.game.setScreen(new TutorialMenuScreen(this.game));
	}
	
	private void checkNextTouch(int x, int y){
		if((x >= (this.xOffset * 3 / 2))
				&& (x <= (this.xOffset * 3 / 2) + this.xOffset)
				&& (y <= this.yOffset)
				&& (y >= this.yOffset - this.buttonHeight))
			this.game.setScreen(new TutorialUnitSecondScreen(this.game));
	}
}
