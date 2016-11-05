package bunzosteele.heroesemblem.view;

import java.io.IOException;
import java.util.List;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.ShopState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class StockWindow
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
	Sprite activeReroll;
	Sprite inactiveReroll;
	Sprite goldSprite;
	Sprite blueSelect;
	Sprite buttonSprite;

	public StockWindow(final HeroesEmblem game, final ShopState state, final int width, final int height, final int xOffset, final int yOffset)
	{
		this.game = game;
		this.state = state;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		this.columnWidth = width / 9;
		final AtlasRegion backgroundRegion = this.game.textureAtlas.findRegion("Flooring-1");
		this.backgroundSprite = new Sprite(backgroundRegion);
		final AtlasRegion pedestalRegion = this.game.textureAtlas.findRegion("Pedestal");
		this.pedestalSprite = new Sprite(pedestalRegion);
		final AtlasRegion activeRerollRegion = this.game.textureAtlas.findRegion("RerollActive");
		this.activeReroll = new Sprite(activeRerollRegion);
		final AtlasRegion inactiveRerollRegion = this.game.textureAtlas.findRegion("RerollInactive");
		this.inactiveReroll = new Sprite(inactiveRerollRegion);
		final AtlasRegion goldRegion = this.game.textureAtlas.findRegion("Gold");
		this.goldSprite = new Sprite(goldRegion);
		final AtlasRegion blueSelectRegion = this.game.textureAtlas.findRegion("BlueSelect");
		this.blueSelect = new Sprite(blueSelectRegion);
		final AtlasRegion buttonRegion = this.game.textureAtlas.findRegion("Button");
		this.buttonSprite = new Sprite(buttonRegion);
	}

	public void draw()
	{
		drawBackground();
		drawStock();
		this.game.batcher.draw(buttonSprite, this.columnWidth * 13 / 2, Gdx.graphics.getHeight() - this.columnWidth * 3 / 4, this.columnWidth * 2, this.columnWidth / 2);
		this.game.font.getData().setScale(.18f);
		this.game.font.draw(this.game.batcher, "Visit Shopkeeper", this.columnWidth * 13 / 2, Gdx.graphics.getHeight() - this.columnWidth * 3 / 7, this.columnWidth * 2, 1, false);
		this.game.font.getData().setScale(.4f);
		this.game.batcher.draw(goldSprite, this.xOffset + this.columnWidth * 7 / 2, Gdx.graphics.getHeight() - this.columnWidth * 3 / 4, this.columnWidth / 2, this.columnWidth / 2);
		this.game.font.draw(this.game.batcher, "" + this.state.gold, this.xOffset + this.columnWidth * 4, Gdx.graphics.getHeight() - this.columnWidth * 3 / 4 + this.game.font.getData().lineHeight * 4 / 5, this.columnWidth * 3 / 2, 0, false);
		this.game.font.getData().setScale(.33f);
	}

	private void drawBackground()
	{
		for(int i = 0; i < 20; i++){
			for(int j = 0; j < 12; j++){
				this.game.batcher.draw(backgroundSprite, (Gdx.graphics.getWidth() / 19) * i, (Gdx.graphics.getHeight() / 11) * j, (Gdx.graphics.getWidth() / 19), (Gdx.graphics.getHeight() / 11));
			}
		}
	}
	
	private void drawStock(){
		int unitOffset = 0;
		this.game.font.getData().setScale(.2f);
		for (final Unit unit : this.state.stock)
		{
			if (unitOffset < 4)
			{
				this.game.batcher.draw(pedestalSprite, this.xOffset + this.columnWidth + (2 * this.columnWidth * unitOffset), Gdx.graphics.getHeight() - 2 * this.columnWidth, this.columnWidth, this.columnWidth);
				if ((this.state.selected != null) && this.state.selected.isEquivalentTo(unit))
				{
					this.game.batcher.draw(blueSelect, this.xOffset + this.columnWidth + (2 * this.columnWidth * unitOffset), (Gdx.graphics.getHeight() - 2 * this.columnWidth), this.columnWidth, this.columnWidth);
					UnitRenderer.DrawUnit(this.game, unit, this.xOffset + this.columnWidth + (2 * this.columnWidth * unitOffset) + this.columnWidth / 10, (Gdx.graphics.getHeight() - 2 * this.columnWidth) + this.columnWidth / 10, this.columnWidth * 8 / 10, true, false, false);
				} else
				{
					UnitRenderer.DrawUnit(this.game, unit, this.xOffset + this.columnWidth + (2 * this.columnWidth * unitOffset) + this.columnWidth / 10, (Gdx.graphics.getHeight() - 2 * this.columnWidth) + this.columnWidth / 10, this.columnWidth * 8 / 10, false, false, false);
				}
				this.game.batcher.draw(goldSprite, this.xOffset + this.columnWidth + (2 * this.columnWidth * unitOffset), Gdx.graphics.getHeight() - this.columnWidth * 9 / 4, this.columnWidth / 4, this.columnWidth / 4);
				this.game.font.draw(this.game.batcher, "" + unit.cost, this.xOffset + this.columnWidth * 5 / 4 + (2 * this.columnWidth * unitOffset), Gdx.graphics.getHeight() - this.columnWidth * 9 / 4 + this.game.font.getData().lineHeight * 4 / 5, this.columnWidth * 3 / 4, 1, false);
			} else
			{
				this.game.batcher.draw(pedestalSprite, this.xOffset + this.columnWidth + (2 * this.columnWidth * (unitOffset - 4)), this.yOffset + this.columnWidth, this.columnWidth, this.columnWidth);
				if ((this.state.selected != null) && this.state.selected.isEquivalentTo(unit))
				{
					this.game.batcher.draw(blueSelect, this.xOffset + this.columnWidth + (2 * this.columnWidth * (unitOffset - 4)), (this.yOffset + this.columnWidth), this.columnWidth, this.columnWidth);

					UnitRenderer.DrawUnit(this.game, unit, this.xOffset + this.columnWidth + (2 * this.columnWidth * (unitOffset - 4)) + this.columnWidth / 10, (this.yOffset + this.columnWidth) + this.columnWidth / 10, this.columnWidth * 8 / 10, true, false, false);
				} else
				{
					UnitRenderer.DrawUnit(this.game, unit, this.xOffset + this.columnWidth + (2 * this.columnWidth * (unitOffset - 4)) + this.columnWidth / 10, (this.yOffset + this.columnWidth) + this.columnWidth / 10, this.columnWidth * 8 / 10, false, false, false);
				}
				this.game.batcher.draw(goldSprite, this.xOffset + this.columnWidth + (2 * this.columnWidth * (unitOffset - 4)), this.yOffset + this.columnWidth * 3 / 4, this.columnWidth / 4, this.columnWidth / 4);
				this.game.font.draw(this.game.batcher, "" + unit.cost, this.xOffset + this.columnWidth * 5 / 4 + (2 * this.columnWidth * (unitOffset - 4)), this.yOffset + this.columnWidth * 3 / 4 + this.game.font.getData().lineHeight * 4 / 5, this.columnWidth * 3 / 4, 1, false);
			}
			unitOffset++;
		}
		this.game.font.getData().setScale(.33f);
		if(this.state.perksPurchased > 2){
			if(this.state.gold >= this.state.GetRerollCost() && this.state.roster.size() < 8){
				this.game.batcher.draw(activeReroll, xOffset + this.columnWidth / 4, Gdx.graphics.getHeight() - 3 * this.columnWidth / 4, this.columnWidth / 2, this.columnWidth / 2);
			}else{
				this.game.batcher.draw(inactiveReroll, xOffset + this.columnWidth / 4, Gdx.graphics.getHeight() - 3 * this.columnWidth / 4, this.columnWidth / 2, this.columnWidth / 2);
			}	
			if(this.state.GetRerollCost() > 0){
				this.game.batcher.draw(goldSprite, xOffset + this.columnWidth, Gdx.graphics.getHeight() - 5 * this.columnWidth / 8, this.columnWidth / 4, this.columnWidth / 4);
				this.game.font.getData().setScale(.2f);
				this.game.font.draw(this.game.batcher, "" + this.state.GetRerollCost(), xOffset + this.columnWidth + this.columnWidth / 3, Gdx.graphics.getHeight() - 5 * this.columnWidth / 8 + this.game.font.getData().lineHeight * 3 / 4, this.columnWidth / 2, -1, false);
				this.game.font.getData().setScale(.33f);
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

	public void processTouch(final float x, final float y) throws IOException
	{
		int unitOffset = 0;
		boolean hit = false;
		for (final Unit unit : this.state.stock)
		{
			if (unitOffset < 4)
			{
				final int lowerXBound = this.xOffset + this.columnWidth + (2 * this.columnWidth * unitOffset);
				final int upperXBound = lowerXBound + this.columnWidth;
				final int lowerYBound = (this.yOffset - (this.columnWidth / 2)) + ((3 * this.height) / 4);
				final int upperYBound = lowerYBound + this.columnWidth;
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
				final int lowerXBound = this.xOffset + this.columnWidth + (2 * this.columnWidth * (unitOffset-4));
				final int upperXBound = lowerXBound + this.columnWidth;
				final int lowerYBound = (this.yOffset - (this.columnWidth / 2)) + (this.height / 4);
				final int upperYBound = lowerYBound + this.columnWidth;
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
			if(x >= xOffset && x <= xOffset + columnWidth && y >= Gdx.graphics.getHeight() - columnWidth && this.state.perksPurchased > 2 && this.state.gold >= this.state.GetRerollCost() && this.state.roster.size() < 8){
				this.state.Reroll();
				if(state.CanBuy()){
					ShopControls.buySound.play(this.game.settings.getFloat("sfxVolume", .5f));
				}else{
					ShopControls.finalBuySound.play(this.game.settings.getFloat("sfxVolume", .5f));
				}
			}
			if(x >= this.columnWidth * 13 / 2 && x <= this.columnWidth * 17 / 2 && y >= Gdx.graphics.getHeight() - columnWidth){
				this.state.isShopkeeperPanelDisplayed = true;
			}
		}
	}
}
