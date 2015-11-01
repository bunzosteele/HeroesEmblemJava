package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.ShopState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
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
	Sprite backdrop;
	Sprite settingsIcon;

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
		final AtlasRegion backdropRegion = this.game.textureAtlas.findRegion("BackdropRight");
		this.backdrop = new Sprite(backdropRegion);
		final AtlasRegion settingsRegion = this.game.textureAtlas.findRegion("settingsIcon");
		this.settingsIcon = new Sprite(settingsRegion);
	}

	public void draw() throws IOException
	{
		drawBackground();
		final int scaledSize = this.width / 3;
		this.game.batcher.draw(this.settingsIcon, xOffset + scaledSize * 17 / 8, Gdx.graphics.getHeight() - scaledSize * 7 / 8, scaledSize / 2, scaledSize / 2);
		if (this.state.selected != null)
		{
			UnitRenderer.DrawUnit(this.game, this.state.selected, this.xOffset + scaledSize / 4, Gdx.graphics.getHeight() - scaledSize * 7 / 4, scaledSize, "Idle", this.currentFrame);
			if (!this.state.roster.contains(this.state.selected))
			{
				UnitRenderer.DrawStockStats(this.game, this.state.selected, this.xOffset + scaledSize / 4, Gdx.graphics.getHeight() - scaledSize * 6 / 4, scaledSize);
			} else
			{
				UnitRenderer.DrawOwnedStats(this.game, this.state.selected, this.xOffset + scaledSize / 4, Gdx.graphics.getHeight() -  scaledSize * 6 / 4, scaledSize);
			}
		}
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
		int clickedX = Gdx.input.getX();
		int clickedY = Gdx.input.getY();
		int xBound = xOffset +  (width * 2 / 3);
		int yBound = (width / 3);
		if ((clickedX > xBound) && (clickedY < yBound))
		{
			this.game.setScreen(new SettingsScreen(this.game, this.game.getScreen()));
			return;
		}
		this.state.selected = null;
	}
}
