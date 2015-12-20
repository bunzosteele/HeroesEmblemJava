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
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		this.columnWidth = width / 9;
		final AtlasRegion backgroundRegion = this.game.textureAtlas.findRegion("WoodFloor");
		this.backgroundSprite = new Sprite(backgroundRegion);
		final AtlasRegion pedestalRegion = this.game.textureAtlas.findRegion("Pedestal");
		this.pedestalSprite = new Sprite(pedestalRegion);
	}

	public void draw() throws IOException
	{
		drawBackground();
		if (this.state.selected != null)
		{
			this.game.batcher.draw(pedestalSprite, this.xOffset + this.columnWidth / 2, Gdx.graphics.getHeight() - this.columnWidth * 3 / 2, this.columnWidth, this.columnWidth);
			UnitRenderer.DrawUnit(this.game, this.state.selected, this.xOffset + this.columnWidth / 2 + this.columnWidth / 10, Gdx.graphics.getHeight() - this.columnWidth * 3 / 2 + this.columnWidth / 10, this.columnWidth * 8 / 10, false, false, false);
			if (!this.state.roster.contains(this.state.selected))
			{
				UnitRenderer.DrawStockInfo(this.game, this.state.selected, this.xOffset + this.columnWidth * 3 / 2, Gdx.graphics.getHeight() - this.columnWidth / 2, this.columnWidth * 7 / 3, this.width, this.xOffset);
			} else
			{
				UnitRenderer.DrawOwnedInfo(this.game, null, this.state.selected, this.xOffset + this.columnWidth * 3 / 2, Gdx.graphics.getHeight() - this.columnWidth / 2, this.columnWidth * 7 / 3, this.width, this.xOffset);
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
