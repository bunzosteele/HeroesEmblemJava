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
	int tileWidth;
	int tileHeight;
	int chainSize;
	int shadowSize;
	int stockWindowYOffset;
	int dividerHeight;
	int dividerWidth;
	int stockHeight;
	int stockWidth;
	int stockXOffset;
	int stockYOffset;

	public StockWindow(final HeroesEmblem game, final ShopState state, final int width, final int height, final int xOffset, final int yOffset)
	{
		this.game = game;
		this.state = state;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		this.tileWidth = width / 10;
		this.tileHeight = height / 9;
		this.chainSize = 20;
		this.shadowSize = chainSize / 4; 
		this.stockWindowYOffset = yOffset + tileHeight * 2;
		this.dividerHeight = tileHeight * 4;
		this.dividerWidth = dividerHeight * 489 / 209;
		this.stockWidth = dividerWidth / 4;
		this.stockHeight = dividerHeight / 2;
		this.stockXOffset = xOffset + (width - dividerWidth) / 2;
		this.stockYOffset = stockWindowYOffset + (height - stockWindowYOffset - dividerHeight) / 2;
	}

	public void draw()
	{
		drawBackground();
		drawBorder();
		drawStock();
	}
	
	public void drawBorder(){
		int chainXOffset = 0;
		int chainYOffset = 0;
		while (chainYOffset < height){
			game.batcher.draw(game.sprites.ChainVertical, xOffset, chainYOffset, chainSize, chainSize);
			chainYOffset += chainSize;
		}
		while (chainXOffset < width){
			game.batcher.draw(game.sprites.ChainHorizontal, xOffset + chainXOffset, Gdx.graphics.getHeight() - chainSize, chainSize, chainSize);
			game.batcher.draw(game.sprites.ChainHorizontal, xOffset + chainXOffset, -shadowSize, chainSize, chainSize);
			game.batcher.draw(game.sprites.ChainHorizontal, xOffset + chainXOffset, 2 * tileHeight, chainSize, chainSize);
			chainXOffset += chainSize;
		}
		
		game.batcher.draw(game.sprites.ChainNW, xOffset, Gdx.graphics.getHeight() - (chainSize), chainSize, chainSize);
		game.batcher.draw(game.sprites.ChainNW, xOffset, 2 * tileHeight, chainSize, chainSize);
		game.batcher.draw(game.sprites.ChainSW, xOffset -1, 0, chainSize -1, chainSize -1);
	}

	public void drawBackground()
	{
		int i = 0;
		int j = 0;
		while(i + tileWidth <= width){
			while(j + tileWidth <= height){
				game.batcher.draw(game.sprites.ShopBackground, xOffset + i, yOffset + j, tileWidth, tileHeight);
				j += tileHeight;
			}
			i += tileWidth;
			j = 0;
		}
	}
	
	private void drawStock(){
		int labelWidth = dividerWidth / 2;
		int labelHeight= labelWidth * 53 / 268;
		
		game.batcher.draw(game.sprites.ShopLabel, xOffset + (width - labelWidth) / 2, Gdx.graphics.getHeight() - (chainSize + shadowSize) * 2 - labelHeight, labelWidth, labelHeight);	
		game.batcher.draw(game.sprites.ShopDivider, stockXOffset, stockYOffset, dividerWidth, dividerHeight);	
		int unitOffset = 0;
		for (Unit unit : state.stock)
		{
			if (unitOffset < 4)
			{
				int unitXOffset = stockXOffset + stockWidth * unitOffset;
				int unitYOffset = stockYOffset + dividerHeight / 2;
				drawStock(unitXOffset, unitYOffset, stockWidth, stockHeight, unit);
			} else
			{
				int unitXOffset = stockXOffset + stockWidth * (unitOffset - 4);
				int unitYOffset = stockYOffset;
				drawStock(unitXOffset, unitYOffset, stockWidth, stockHeight, unit);
			}
			unitOffset++;
		}
		game.font.getData().setScale(.33f);
		/*
		if(state.perksPurchased > 2){
			if(state.gold >= state.GetRerollCost() && state.roster.size() < 8){
				game.batcher.draw(game.sprites.RerollEnabled, xOffset + columnWidth / 4, Gdx.graphics.getHeight() - 3 * columnWidth / 4, columnWidth / 2, columnWidth / 2);
			}else{
				game.batcher.draw(game.sprites.RerollDisabled, xOffset + columnWidth / 4, Gdx.graphics.getHeight() - 3 * columnWidth / 4, columnWidth / 2, columnWidth / 2);
			}	
			if(state.GetRerollCost() > 0){
				game.batcher.draw(game.sprites.GoldCoin, xOffset + columnWidth, Gdx.graphics.getHeight() - 5 * columnWidth / 8, columnWidth / 4, columnWidth / 4);
				game.font.getData().setScale(.2f);
				game.font.draw(game.batcher, "" + state.GetRerollCost(), xOffset + columnWidth + columnWidth / 3, Gdx.graphics.getHeight() - 5 * columnWidth / 8 + game.font.getData().lineHeight * 3 / 4, columnWidth / 2, -1, false);
				game.font.getData().setScale(.33f);
			}
		}
		*/
	}
	
	private void drawStock(int xOffset, int yOffset, int width, int height, Unit unit){
		int nameBackdropWidth = width * 8 / 10;
		int nameBackdropHeight = nameBackdropWidth * 42 / 93;
		game.batcher.draw(game.sprites.StockNameBackdrop, xOffset + (width - nameBackdropWidth) / 2, yOffset + height / 20, nameBackdropWidth, nameBackdropHeight);
		game.font.getData().setScale(.25f);
		game.font.setColor(new Color(0f, 0f, 0f, 1f));
		game.font.draw(game.batcher, unit.name, xOffset + (width - nameBackdropWidth) / 2, yOffset + height / 10 + nameBackdropHeight - game.font.getLineHeight(), nameBackdropWidth, 1, false);
		game.font.draw(game.batcher, unit.type.toString(), xOffset + (width - nameBackdropWidth) / 2, yOffset + height / 10 + game.font.getLineHeight(), nameBackdropWidth, 1, false);
		
		if ((state.selected != null) && state.selected.isEquivalentTo(unit))
		{
			UnitRenderer.DrawUnit(game, unit, xOffset + (width - nameBackdropWidth) / 2, yOffset + height / 20 + nameBackdropHeight, tileWidth, true, false, false);
			game.batcher.draw(game.sprites.BlueSelect, xOffset + (width - nameBackdropWidth) / 2, yOffset + height / 20 + nameBackdropHeight, tileWidth, tileHeight);
		} else
		{
			UnitRenderer.DrawUnit(game, unit, xOffset + (width - nameBackdropWidth) / 2, yOffset + height / 20 + nameBackdropHeight, tileWidth, false, false, false);
		}
		
		game.batcher.draw(game.sprites.GoldCoin, xOffset + width * 9 / 10 - tileWidth / 4, yOffset + height / 20 + nameBackdropHeight + tileHeight / 2, tileHeight / 4, tileHeight / 4);
		game.font.setColor(new Color(1f, .8f, 0, 1f));
		game.font.draw(game.batcher, "" + unit.cost, xOffset + width * 9 / 10 - tileWidth * 17 / 16, yOffset + height / 20 + nameBackdropHeight + tileHeight / 2 + game.font.getLineHeight() * 4 / 5, tileWidth * 3 / 4, 0, false);
		game.font.setColor(new Color(1f, 1f, 1f, 1f));
		game.font.getData().setScale(.2f);
		game.font.draw(game.batcher, unit.ability != null ? unit.ability.displayName : "", xOffset + (width - nameBackdropWidth) / 2 + tileWidth, yOffset + height / 10 + nameBackdropHeight + game.font.getLineHeight(), width - ((width - nameBackdropWidth) / 2 + tileWidth), 1, false);
	}

	public boolean isTouched(final float x, final float y)
	{
		if ((x >= xOffset) && (x < (xOffset + width)))
		{
			if ((y >= yOffset) && (y < (yOffset + height)))
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
		for (Unit unit : state.stock)
		{
			if (unitOffset < 4)
			{
				int lowerXBound = stockXOffset + stockWidth * unitOffset;
				int upperXBound = stockXOffset + stockWidth * (unitOffset + 1);
				int lowerYBound = stockYOffset + dividerHeight / 2;
				int upperYBound = stockYOffset + dividerHeight / 2 + stockHeight;
				if ((x >= lowerXBound) && (x < upperXBound))
				{
					if ((y >= lowerYBound) && (y < upperYBound))
					{
						state.selected = state.stock.get(unitOffset);
						hit = true;
					}
				}
			} else
			{
				int lowerXBound = stockXOffset + stockWidth * (unitOffset - 4);
				int upperXBound = stockXOffset + stockWidth * (unitOffset - 3);
				int lowerYBound = stockYOffset;
				int upperYBound = stockYOffset + dividerHeight / 2;
				if ((x >= lowerXBound) && (x < upperXBound))
				{
					if ((y >= lowerYBound) && (y < upperYBound))
					{
						state.selected = state.stock.get(unitOffset);
						hit = true;
					}
				}
			}
			unitOffset++;
		}
		if (!hit)
		{
			state.selected = null;
			/*
			if(x >= xOffset && x <= xOffset + columnWidth && y >= Gdx.graphics.getHeight() - columnWidth && state.perksPurchased > 2 && state.gold >= state.GetRerollCost() && state.roster.size() < 8){
				state.Reroll();
				if(state.CanBuy()){
					ShopControls.buySound.play(game.settings.getFloat("sfxVolume", .5f));
				}else{
					ShopControls.finalBuySound.play(game.settings.getFloat("sfxVolume", .5f));
				}
			}
			if(x >= columnWidth * 13 / 2 && x <= columnWidth * 17 / 2 && y >= Gdx.graphics.getHeight() - columnWidth){
				state.isShopkeeperPanelDisplayed = true;
			}
			*/
		}
	}
}
