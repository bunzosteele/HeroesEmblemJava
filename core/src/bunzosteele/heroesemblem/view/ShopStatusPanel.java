package bunzosteele.heroesemblem.view;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.ShopState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class ShopStatusPanel
{
	HeroesEmblem game;
	ShopState state;
	int currentFrame = 1;
	int xOffset;
	int yOffset;
	int width;
	int height;
	Sprite backdrop;

	public ShopStatusPanel(final HeroesEmblem game, final ShopState state, final int width, final int height, final int yOffset)
	{
		this.game = game;
		this.state = state;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				ShopStatusPanel.this.currentFrame++;
				if (ShopStatusPanel.this.currentFrame > 3)
				{
					ShopStatusPanel.this.currentFrame = 1;
				}
			}
		}, 0, 1 / 3.0f);
		this.xOffset = 0;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		final AtlasRegion backdropRegion = this.game.textureAtlas.findRegion("BackdropLeft");
		this.backdrop = new Sprite(backdropRegion);
	}

	public void draw()
	{
		drawBackground();
		
		final AtlasRegion shopkeeperRegion = this.game.textureAtlas.findRegion("Shopkeeper-" + this.currentFrame);
		final Sprite shopkeeperSprite = new Sprite(shopkeeperRegion);
		final AtlasRegion goldRegion = this.game.textureAtlas.findRegion("Gold");
		final Sprite goldSprite = new Sprite(goldRegion);
		final float scaledSize = (this.width / 3);
		this.game.batcher.draw(shopkeeperSprite, scaledSize / 4, Gdx.graphics.getHeight() - scaledSize * 5 / 4, scaledSize, scaledSize);
		this.game.batcher.draw(goldSprite, scaledSize / 4, yOffset + scaledSize / 4, scaledSize * 3 / 4, scaledSize * 3 / 4);
		this.game.font.setColor(Color.WHITE);
		this.game.font.draw(this.game.batcher, "" + this.state.gold, scaledSize * 11 / 10, this.yOffset + this.game.font.getData().lineHeight * 4 / 3, this.width - scaledSize * 6 / 4, 1, false);
	}

	public void drawBackground()
	{
		this.game.batcher.draw(this.backdrop, this.xOffset, this.yOffset, this.width, this.height);
	}

	public boolean isTouched(final float x, final float y)
	{
		if ((x >= this.xOffset) && (x < (this.xOffset + this.width)))
		{
			if ((y >= this.yOffset) && (y < (this.yOffset + this.height)))
			{
				return true;
			}
		}
		return false;
	}

	public void processTouch(final float x, final float y)
	{
		this.state.selected = null;
	}
}
