package bunzosteele.heroesemblem.view;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.ShopState;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class StockWindow
{
	HeroesEmblem game;
	ShopState state;
	int idleFrame = 1;
	int attackFrame = 1;
	int xOffset;
	int yOffset;
	int width;
	int height;
	int columnWidth;

	public StockWindow(final HeroesEmblem game, final ShopState state, final int width, final int height, final int xOffset, final int yOffset)
	{
		this.game = game;
		this.state = state;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				StockWindow.this.idleFrame++;
				StockWindow.this.attackFrame++;
				if (StockWindow.this.idleFrame > 3)
				{
					StockWindow.this.idleFrame = 1;
				}
				if (StockWindow.this.attackFrame > 2)
				{
					StockWindow.this.attackFrame = 1;
				}
			}
		}, 0, 1 / 3f);
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		this.columnWidth = width / 9;
	}

	public void draw()
	{
		int unitOffset = 0;
		final AtlasRegion pedestalRegion = this.game.textureAtlas.findRegion("Pedestal");
		final Sprite pedestalSprite = new Sprite(pedestalRegion);

		for (final Unit unit : this.state.stock)
		{
			if (unitOffset < 4)
			{
				this.game.batcher.draw(pedestalSprite, this.xOffset + this.columnWidth + (this.columnWidth * unitOffset * 2), (this.yOffset - (this.columnWidth / 2)) + ((3 * this.height) / 4), this.columnWidth, this.columnWidth);
				if ((this.state.selected != null) && this.state.selected.isEquivalentTo(unit))
				{
					UnitRenderer.DrawUnit(this.game, unit, this.xOffset + this.columnWidth + (this.columnWidth * unitOffset * 2), (this.yOffset - (this.columnWidth / 2)) + ((3 * this.height) / 4), this.columnWidth, "Attack", this.attackFrame);
				} else
				{
					UnitRenderer.DrawUnit(this.game, unit, this.xOffset + this.columnWidth + (this.columnWidth * unitOffset * 2), (this.yOffset - (this.columnWidth / 2)) + ((3 * this.height) / 4), this.columnWidth, "Idle", this.idleFrame);
				}
			} else
			{
				this.game.batcher.draw(pedestalSprite, this.xOffset + this.columnWidth + (this.columnWidth * (unitOffset - 4) * 2), (this.yOffset - (this.columnWidth / 2)) + (this.height / 4), this.columnWidth, this.columnWidth);
				if ((this.state.selected != null) && this.state.selected.isEquivalentTo(unit))
				{
					UnitRenderer.DrawUnit(this.game, unit, this.xOffset + this.columnWidth + (this.columnWidth * (unitOffset - 4) * 2), (this.yOffset - (this.columnWidth / 2)) + (this.height / 4), this.columnWidth, "Attack", this.attackFrame);
				} else
				{
					UnitRenderer.DrawUnit(this.game, unit, this.xOffset + this.columnWidth + (this.columnWidth * (unitOffset - 4) * 2), (this.yOffset - (this.columnWidth / 2)) + (this.height / 4), this.columnWidth, "Idle", this.idleFrame);
				}
			}
			unitOffset++;
		}
	}

	public void drawBackground()
	{

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
		int unitOffset = 0;
		boolean hit = false;
		for (final Unit unit : this.state.stock)
		{
			if (unitOffset < 4)
			{
				final int lowerXBound = this.xOffset + this.columnWidth + (this.columnWidth * unitOffset * 2);
				final int upperXBound = lowerXBound + this.columnWidth;
				final int lowerYBound = (this.yOffset - (this.columnWidth / 2)) + ((3 * this.height) / 4);
				final int upperYBound = (this.yOffset - (this.columnWidth / 2)) + ((3 * this.height) / 4) + this.columnWidth;
				if ((x >= lowerXBound) && (x < upperXBound))
				{
					if ((y >= lowerYBound) && (y < upperYBound))
					{
						this.state.selected = this.state.stock.get(unitOffset);
						hit = true;
					}
				}
			} else
			{
				final int lowerXBound = this.xOffset + this.columnWidth + (this.columnWidth * (unitOffset - 4) * 2);
				final int upperXBound = lowerXBound + this.columnWidth;
				final int lowerYBound = (this.yOffset - (this.columnWidth / 2)) + (this.height / 4);
				final int upperYBound = ((this.yOffset - (this.columnWidth / 2)) + (this.height / 4)) + this.columnWidth;
				if ((x >= lowerXBound) && (x < upperXBound))
				{
					if ((y >= lowerYBound) && (y < upperYBound))
					{
						this.state.selected = this.state.stock.get(unitOffset);
						hit = true;
					}
				}
			}
			unitOffset++;
		}
		if (!hit)
		{
			this.state.selected = null;
		}
	}
}
