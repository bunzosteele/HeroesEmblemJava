package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.ShopState;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitGenerator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class ShopControls
{
	HeroesEmblem game;
	ShopState state;
	int idleFrame = 1;
	int attackFrame = 1;
	int xOffset;
	int yOffset;
	int buttonWidth;
	int rosterWidth;
	int height;
	public static Sound buySound = Gdx.audio.newSound(Gdx.files.internal("buy.wav"));
	public static Sound finalBuySound = Gdx.audio.newSound(Gdx.files.internal("finalbuy.wav"));

	public ShopControls(final HeroesEmblem game, final ShopState state, final int buttonWidth, final int rosterWidth, final int height)
	{
		this.game = game;
		this.state = state;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				ShopControls.this.idleFrame++;
				ShopControls.this.attackFrame++;
				if (ShopControls.this.idleFrame > 3)
				{
					ShopControls.this.idleFrame = 1;
				}
				if (ShopControls.this.attackFrame > 2)
				{
					ShopControls.this.attackFrame = 1;
				}
			}
		}, 0, 1 / 3.0f);
		this.xOffset = 0;
		this.yOffset = 0;
		this.buttonWidth = buttonWidth;
		this.rosterWidth = rosterWidth;
		this.height = height;
	}

	private boolean canPurchaseSelected()
	{
		return (this.state.selected != null) && (this.state.gold >= this.state.selected.cost) && (this.state.roster.size() < 8) && !this.state.roster.contains(this.state.selected);
	}

	private boolean canStartGame()
	{
		return this.state.roster.size() > 0;
	}

	public void draw()
	{
		this.drawBuy();
		this.drawRoster();
		this.drawComplete();
	}

	public void drawBackground()
	{
		this.drawBuyBackground();
		this.drawRosterBackground();
		this.drawCompleteBackground();
	}

	private void drawBuy()
	{
		this.game.font.setColor(Color.WHITE);
		this.game.font.getData().setScale(.4f);
		this.game.font.draw(this.game.batcher, "Buy", this.xOffset, this.yOffset + ((3 * this.height) / 4), this.buttonWidth, 1, false);
	}

	private void drawBuyBackground()
	{
		Color color = new Color(.3f, .3f, .3f, 1);
		if (this.canPurchaseSelected())
		{
			color = new Color(.3f, .8f, .3f, 1);
		}
		this.game.shapeRenderer.setColor(color);
		this.game.shapeRenderer.rect(this.xOffset, this.yOffset, this.rosterWidth, this.height);
	}

	private void drawComplete()
	{
		this.game.font.setColor(Color.WHITE);
		this.game.font.getData().setScale(.4f);
		this.game.font.draw(this.game.batcher, "Complete", this.buttonWidth + this.rosterWidth, this.yOffset + ((3 * this.height) / 4), this.buttonWidth, 1, false);
	}

	private void drawCompleteBackground()
	{
		Color color = new Color(.3f, .3f, .3f, 1);
		if (this.canStartGame())
		{
			color = new Color(.3f, .8f, .3f, 1);
		}

		boolean canAffordMoreUnits = false;
		for (final Unit unit : this.state.stock)
		{
			if (unit.cost < this.state.gold)
			{
				canAffordMoreUnits = true;
			}
		}

		if (!canAffordMoreUnits || (this.state.roster.size() >= 8))
		{
			color = new Color(.4f, 1f, .4f, 1);
		}

		this.game.shapeRenderer.setColor(color);
		this.game.shapeRenderer.rect(this.buttonWidth + this.rosterWidth, this.yOffset, this.buttonWidth, this.height);
	}

	private void drawRoster()
	{
		int unitOffset = 0;
		final int columnWidth = this.rosterWidth / 10;
		final int gapWidth = ((2 * this.rosterWidth) / 10) / 9;

		for (final Unit unit : this.state.roster)
		{
			if ((this.state.selected != null) && this.state.selected.isEquivalentTo(unit))
			{
				UnitRenderer.DrawUnit(this.game, unit, this.xOffset + this.buttonWidth + gapWidth + ((gapWidth * unitOffset) + 1) + (columnWidth * unitOffset), (this.height - columnWidth) / 2, columnWidth, "Attack", this.attackFrame);
			} else
			{
				UnitRenderer.DrawUnit(this.game, unit, this.xOffset + this.buttonWidth + gapWidth + ((gapWidth * unitOffset) + 1) + (columnWidth * unitOffset), (this.height - columnWidth) / 2, columnWidth, "Idle", this.idleFrame);
			}
			unitOffset++;
		}
	}

	private void drawRosterBackground()
	{
		this.game.shapeRenderer.setColor(.6f, .3f, .1f, 1);
		this.game.shapeRenderer.rect(this.buttonWidth, this.yOffset, this.rosterWidth, this.height);
	}

	public boolean isTouched(final float x, final float y)
	{
		if ((x >= this.xOffset) && (x < (this.xOffset + (2 * this.buttonWidth) + this.rosterWidth)))
		{
			if ((y >= this.yOffset) && (y < (this.yOffset + this.height)))
			{
				return true;
			}
		}
		return false;
	}

	private void processBuyTouch() throws IOException
	{
		if (this.canPurchaseSelected())
		{
			ShopControls.buySound.play();
			this.state.roster.add(this.state.selected);
			this.state.gold -= this.state.selected.cost;
			this.state.selected = null;
			this.state.stock = UnitGenerator.GenerateStock();
			boolean canBuy = false;
			for(Unit unit : this.state.stock){
				if (unit.cost < this.state.gold)
					canBuy = true;
			}			
			if(canBuy){
				ShopControls.buySound.play();
			}else{
				ShopControls.finalBuySound.play();
			}
		}
	}

	private void processCompleteTouch() throws IOException
	{
		if (this.canStartGame())
		{
			this.game.setScreen(new BattleScreen(this.game, this.state));
			return;
		}
	}

	private void processRosterTouch(final float x)
	{
		int unitOffset = 0;
		final int columnWidth = this.rosterWidth / 10;
		final int gapWidth = ((2 * this.rosterWidth) / 10) / 9;
		boolean isHit = false;

		for (final Unit unit : this.state.roster)
		{
			final int lowerXBound = this.xOffset + this.buttonWidth + gapWidth + ((gapWidth * unitOffset) + 1) + (columnWidth * unitOffset);
			final int upperXBound = this.xOffset + this.buttonWidth + gapWidth + ((gapWidth * unitOffset) + 1) + (columnWidth * unitOffset) + columnWidth;

			if ((x >= lowerXBound) && (x <= upperXBound))
			{
				isHit = true;
				this.state.selected = this.state.roster.get(unitOffset);
			}
			unitOffset++;
		}
		if (!isHit)
		{
			this.state.selected = null;
		}
	}

	public void processTouch(final float x, final float y) throws IOException
	{
		if ((x >= this.xOffset) && (x < (this.xOffset + this.buttonWidth)))
		{
			if ((y >= this.yOffset) && (y < (this.yOffset + this.height)))
			{
				this.processBuyTouch();
			}
		}
		if ((x >= (this.xOffset + this.buttonWidth)) && (x < (this.xOffset + this.buttonWidth + this.rosterWidth)))
		{
			if ((y >= this.yOffset) && (y < (this.yOffset + this.height)))
			{
				this.processRosterTouch(x);
			}
		}
		if ((x >= (this.xOffset + this.buttonWidth + this.rosterWidth)) && (x < (this.xOffset + (2 * this.buttonWidth) + this.rosterWidth)))
		{
			if ((y >= this.yOffset) && (y < (this.yOffset + this.height)))
			{
				this.processCompleteTouch();
			}
		}
	}
}
