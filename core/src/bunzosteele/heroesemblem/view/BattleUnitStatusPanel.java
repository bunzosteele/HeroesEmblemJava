package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.BattleState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class BattleUnitStatusPanel
{
	HeroesEmblem game;
	BattleState state;
	int xOffset;
	int yOffset;
	int width;
	int height;
	Sprite backdrop;
	Sprite settingsIcon;

	public BattleUnitStatusPanel(final HeroesEmblem game, final BattleState state, final int width, final int height, final int xOffset, final int yOffset)
	{
		this.game = game;
		this.state = state;
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
			UnitRenderer.DrawUnit(this.game, this.state.selected, this.xOffset + scaledSize / 4, Gdx.graphics.getHeight() - scaledSize * 5 / 4, scaledSize, "Attack", false);
			if (!this.state.roster.contains(this.state.selected))
			{
				UnitRenderer.DrawEnemyStats(this.game, this.state.selected, this.xOffset + scaledSize / 4, Gdx.graphics.getHeight() - scaledSize, scaledSize, this.state.perksPurchased > 1);
			} else
			{
				UnitRenderer.DrawOwnedStats(this.game, this.state.selected, this.xOffset + scaledSize / 4, Gdx.graphics.getHeight() - scaledSize, scaledSize);
			}
		}
		this.game.font.setColor(Color.WHITE);
		this.game.font.getData().setScale(.25f);
		this.game.font.draw(this.game.batcher, "Round: " + this.state.turnCount, this.xOffset+ scaledSize / 4, this.yOffset + 2 * this.game.font.getData().lineHeight, this.width - scaledSize / 2, 1, false);
		this.game.font.getData().setScale(.33f);
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
			this.game.setScreen(new SettingsScreen(this.game, this.game.getScreen(), false));
			return;
		}
		this.state.selected = null;
		this.state.isMoving = false;
		this.state.isUsingAbility = false;
		this.state.isAttacking = false;
	}
}
