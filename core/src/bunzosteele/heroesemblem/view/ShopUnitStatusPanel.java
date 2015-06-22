package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.ShopState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class ShopUnitStatusPanel
{
	HeroesEmblem game;
	ShopState state;
	int currentFrame = 1;
	int xOffset;
	int yOffset;
	int width;
	int height;

	public ShopUnitStatusPanel(final HeroesEmblem game, final ShopState state, final int width, final int height, final int xOffset, final int yOffset)
	{
		this.game = game;
		this.state = state;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				ShopUnitStatusPanel.this.currentFrame++;
				if (ShopUnitStatusPanel.this.currentFrame > 3)
				{
					ShopUnitStatusPanel.this.currentFrame = 1;
				}
			}
		}, 0, 1 / 3.0f);
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
	}

	public void draw() throws IOException
	{
		final int scaledSize = this.height / 12;
		if (this.state.selected != null)
		{
			UnitRenderer.DrawUnit(this.game, this.state.selected, this.xOffset, Gdx.graphics.getHeight() - scaledSize, scaledSize, "Idle", this.currentFrame);
			if (!this.state.roster.contains(this.state.selected))
			{
				UnitRenderer.DrawStockStats(this.game, this.state.selected, this.xOffset, Gdx.graphics.getHeight() - scaledSize, scaledSize);
			} else
			{
				UnitRenderer.DrawOwnedStats(this.game, this.state.selected, this.xOffset, Gdx.graphics.getHeight() - scaledSize, scaledSize);
			}
		}
	}

	public void drawBackground()
	{
		this.game.shapeRenderer.setColor(.6f, .3f, .1f, 1);
		this.game.shapeRenderer.rect(this.xOffset, this.yOffset, this.width, this.height);
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
