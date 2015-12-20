package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.ShopState;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitDto;
import bunzosteele.heroesemblem.model.Units.UnitGenerator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class ShopControls
{
	HeroesEmblem game;
	ShopState state;
	int xOffset;
	int yOffset;
	int buttonWidth;
	int rosterWidth;
	int height;
	public static Sound buySound = Gdx.audio.newSound(Gdx.files.internal("buy.wav"));
	public static Sound finalBuySound = Gdx.audio.newSound(Gdx.files.internal("finalbuy.wav"));
	Sprite inactiveButton;
	Sprite activeButton;
	Sprite emphasisButton;
	Sprite backdrop;
	Sprite blueSelect;

	public ShopControls(final HeroesEmblem game, final ShopState state, final int buttonWidth, final int rosterWidth, final int height)
	{
		this.game = game;
		this.state = state;
		this.xOffset = 0;
		this.yOffset = 0;
		this.buttonWidth = buttonWidth;
		this.rosterWidth = rosterWidth;
		this.height = height;
		final AtlasRegion inactiveRegion = this.game.textureAtlas.findRegion("InactiveButton");
		this.inactiveButton = new Sprite(inactiveRegion);
		final AtlasRegion activeRegion = this.game.textureAtlas.findRegion("ActiveButton");
		this.activeButton = new Sprite(activeRegion);
		final AtlasRegion emphasisRegion = this.game.textureAtlas.findRegion("EmphasisButton");
		this.emphasisButton = new Sprite(emphasisRegion);
		final AtlasRegion backdropRegion = this.game.textureAtlas.findRegion("BackdropBottom");
		this.backdrop = new Sprite(backdropRegion);
		final AtlasRegion blueSelectRegion = this.game.textureAtlas.findRegion("BlueSelect");
		this.blueSelect = new Sprite(blueSelectRegion);
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
		this.drawBuyBackground();
		this.drawRosterBackground();
		this.drawCompleteBackground();
		this.game.font.getData().setScale(.25f);
		this.drawBuy();
		this.game.font.getData().setScale(.33f);
		this.drawRoster();
		this.game.font.getData().setScale(.25f);
		this.drawComplete();
		this.game.font.getData().setScale(.33f);
	}

	private void drawBuy()
	{
		this.game.font.setColor(Color.WHITE);
		this.game.font.draw(this.game.batcher, "Buy", this.xOffset, this.yOffset + ((3 * this.height) / 5), this.buttonWidth, 1, false);
	}

	private void drawBuyBackground()
	{
		if (this.canPurchaseSelected())
		{
			this.game.batcher.draw(activeButton, this.xOffset, this.yOffset, this.buttonWidth, this.height);
		}else{
			this.game.batcher.draw(inactiveButton, this.xOffset, this.yOffset, this.buttonWidth, this.height);
		}
	}

	private void drawComplete()
	{
		this.game.font.setColor(Color.WHITE);
		this.game.font.draw(this.game.batcher, "Complete", this.buttonWidth + this.rosterWidth, this.yOffset + ((3 * this.height) / 5), this.buttonWidth, 1, false);
	}

	private void drawCompleteBackground()
	{	
		if(this.canStartGame()){
			if (!state.CanBuy())
			{
				this.game.batcher.draw(emphasisButton, this.buttonWidth + this.rosterWidth, this.yOffset, this.buttonWidth, this.height);
			}else{
				this.game.batcher.draw(activeButton, this.buttonWidth + this.rosterWidth, this.yOffset, this.buttonWidth, this.height);
			}
		}else{
			this.game.batcher.draw(inactiveButton, this.buttonWidth + this.rosterWidth, this.yOffset, this.buttonWidth, this.height);
		}
	}

	private void drawRoster()
	{
		int unitOffset = 0;
		final int columnWidth = this.rosterWidth / 12;
		final int gapWidth = (this.rosterWidth - (columnWidth * 8)) / 9;
		for (final Unit unit : this.state.roster)
		{
			if ((this.state.selected != null) && this.state.selected.isEquivalentTo(unit))
			{
				this.game.batcher.draw(blueSelect, this.xOffset + this.buttonWidth + gapWidth + ((gapWidth * unitOffset) + 1) + (columnWidth * unitOffset),  (this.height - columnWidth) / 2, columnWidth, columnWidth);
				UnitRenderer.DrawUnit(this.game, unit, this.xOffset + this.buttonWidth + gapWidth + ((gapWidth * unitOffset) + 1) + (columnWidth * unitOffset), (this.height - columnWidth) / 2, columnWidth, true, false, false);
			} else
			{
				UnitRenderer.DrawUnit(this.game, unit, this.xOffset + this.buttonWidth + gapWidth + ((gapWidth * unitOffset) + 1) + (columnWidth * unitOffset), (this.height - columnWidth) / 2, columnWidth, false, false, false);
			}
			unitOffset++;
		}
	}
	
	public void drawHealthBars()
	{
		int unitOffset = 0;
		final int columnWidth = this.rosterWidth / 12;
		final int gapWidth = (this.rosterWidth - (columnWidth * 8)) / 9;
		for (final Unit unit : this.state.roster)
		{
			final float healthPercent = unit.currentHealth / (float) unit.maximumHealth;
			if ((healthPercent > 0) && (healthPercent < 1))
			{
				Color color;
				if (healthPercent > .7)
				{
					color = new Color(0f, 1f, 0f, 1f);
				} else if (healthPercent > .3)
				{
					color = new Color(1f, 1f, 0f, 1f);
				} else
				{
					color = new Color(1f, 0f, 0f, 1f);
				}
	
				this.game.shapeRenderer.setColor(color);
				this.game.shapeRenderer.rect(gapWidth + ((columnWidth + gapWidth) * unitOffset) + this.buttonWidth + (columnWidth * .21875f), (this.height - columnWidth) / 2, (columnWidth - (columnWidth * .4375f)) * healthPercent, this.height / 15);
			}
			unitOffset++;
		}
	}

	public void drawRosterBackground()
	{
		this.game.batcher.draw(backdrop, this.buttonWidth, this.yOffset, this.rosterWidth, this.height);
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
			this.state.BuyUnit();		
			if(this.state.CanBuy()){
				ShopControls.buySound.play(this.game.settings.getFloat("sfxVolume", .5f));
			}else{
				ShopControls.finalBuySound.play(this.game.settings.getFloat("sfxVolume", .5f));
			}
		}
	}

	private void processCompleteTouch() throws IOException
	{
		if (this.canStartGame())
		{
			game.shopState = null;
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
			this.state.isInspecting = false;
			this.state.isShopkeeperPanelDisplayed = false;
		}if(isHit && this.state.isShopkeeperPanelDisplayed){
			this.state.isInspecting = true;
			this.state.isShopkeeperPanelDisplayed = false;
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
