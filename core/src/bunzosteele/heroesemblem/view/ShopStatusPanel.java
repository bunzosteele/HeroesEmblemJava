package bunzosteele.heroesemblem.view;

import java.util.List;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.ShopState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;

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
	int currentFrame = 0;
	int xOffset;
	int yOffset;
	int width;
	int height;
	Sprite backdrop;
	Sprite infoOpen;
	Sprite infoClose;
	Sprite buttonSprite;

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
				if (ShopStatusPanel.this.currentFrame > 2)
				{
					ShopStatusPanel.this.currentFrame = 0;
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
		this.state.isInspecting = false;
		if(x >= xOffset + this.width / 2 && x <= xOffset + this.width * 9 / 10 && y >= Gdx.graphics.getHeight() - width / 3 && y <= Gdx.graphics.getHeight() - width / 6){
			this.state.isShopkeeperPanelDisplayed = !this.state.isShopkeeperPanelDisplayed;
		}else{
			this.state.isShopkeeperPanelDisplayed = false;
		}

	}
}
