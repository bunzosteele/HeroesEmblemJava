package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.ShopState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class ShopUnitInfoPanel
{
	HeroesEmblem game;
	ShopState state;
	int currentFrame = 1;
	int xOffset;
	int yOffset;
	int width;
	int height;
	int columnWidth;
	Sprite backgroundSprite;
	Sprite pedestalSprite;

	public ShopUnitInfoPanel(final HeroesEmblem game, final ShopState state, final int width, final int height, final int xOffset, final int yOffset)
	{
		this.game = game;
		this.state = state;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				ShopUnitInfoPanel.this.currentFrame++;
				if (ShopUnitInfoPanel.this.currentFrame > 3)
				{
					ShopUnitInfoPanel.this.currentFrame = 1;
				}
			}
		}, 0, 1 / 3f);
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		this.columnWidth = width * 2 / 15;
		final AtlasRegion backgroundRegion = this.game.textureAtlas.findRegion("WoodFloor");
		this.backgroundSprite = new Sprite(backgroundRegion);
		final AtlasRegion pedestalRegion = this.game.textureAtlas.findRegion("Pedestal");
		this.pedestalSprite = new Sprite(pedestalRegion);
	}

	public void draw() throws IOException
	{
		drawBackground();
		final int scaledSize = this.width / 6;
		if (this.state.selected != null)
		{
			this.game.batcher.draw(pedestalSprite, this.xOffset + scaledSize / 4, Gdx.graphics.getHeight() - this.xOffset + scaledSize / 4, scaledSize, scaledSize);
			UnitRenderer.DrawUnit(this.game, this.state.selected, this.xOffset + scaledSize / 4 + scaledSize / 10, Gdx.graphics.getHeight() - this.xOffset + scaledSize / 4 + scaledSize / 10, scaledSize * 8 / 10, "Idle", this.currentFrame);
			if (!this.state.roster.contains(this.state.selected))
			{
				UnitRenderer.DrawStockInfo(this.game, this.state.selected, this.xOffset + scaledSize * 11 / 8, Gdx.graphics.getHeight() - scaledSize * 3 / 8, scaledSize, this.width, this.xOffset);
			} else
			{
				UnitRenderer.DrawOwnedInfo(this.game, this.state.selected, this.xOffset + scaledSize * 11 / 8, Gdx.graphics.getHeight() - scaledSize * 3 / 8, scaledSize, this.width, this.xOffset);
			}
		}
	}

	public void drawBackground()
	{
		for(int i = 0; i < 20; i++){
			for(int j = 0; j < 12; j++){
				this.game.batcher.draw(backgroundSprite, (Gdx.graphics.getWidth() / 19) * i, (Gdx.graphics.getHeight() / 11) * j, (Gdx.graphics.getWidth() / 19), (Gdx.graphics.getHeight() / 11));
			}
		}
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
	}
}
