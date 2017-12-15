package bunzosteele.heroesemblem.view;

import java.io.IOException;
import java.util.List;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.ShopState;
import bunzosteele.heroesemblem.model.TipManager;
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
	int dividerHeight;
	int dividerWidth;
	int stockHeight;
	int stockWidth;
	int stockXOffset;
	int stockYOffset;
	int rerollX;
	int rerollY;
	String tip;

	public StockWindow(final HeroesEmblem game, final ShopState state, final int width, final int height, final int xOffset, final int yOffset)
	{
		this.game = game;
		this.state = state;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		this.tileWidth = width / 10;
		this.tileHeight = height / 7;
		this.chainSize = tileWidth / 5;
		this.shadowSize = chainSize / 4; 
		this.dividerHeight = tileHeight * 4;
		this.dividerWidth = dividerHeight * 489 / 209;
		this.stockWidth = dividerWidth / 4;
		this.stockHeight = dividerHeight / 2;
		this.stockXOffset = xOffset + (width - dividerWidth) / 2;
		this.stockYOffset = yOffset + (height - dividerHeight) / 2;
		this.rerollX = xOffset + width - tileWidth - chainSize - shadowSize;
		this.rerollY = yOffset + chainSize * 2;
		try {
			tip = TipManager.GetTip();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void draw()
	{
		drawStock();
		drawButtons();
		drawStockpile();
		drawTip();
	}
	
	public void drawBorder(){
		int chainXOffset = xOffset;
		
		while (chainXOffset < xOffset + width){
			game.batcher.draw(game.sprites.ChainHorizontal, chainXOffset, yOffset + height - chainSize - shadowSize, chainSize, chainSize + shadowSize);
			chainXOffset += chainSize;
		}
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
				drawUnitPane(unitXOffset, unitYOffset, stockWidth, stockHeight, unit);
			} else
			{
				int unitXOffset = stockXOffset + stockWidth * (unitOffset - 4);
				int unitYOffset = stockYOffset;
				drawUnitPane(unitXOffset, unitYOffset, stockWidth, stockHeight, unit);
			}
			unitOffset++;
		}
		game.font.getData().setScale(.33f);
	}
	
	private void drawUnitPane(int xOffset, int yOffset, int width, int height, Unit unit){
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
		
		game.batcher.draw(game.sprites.GoldCoin, xOffset + width * 9 / 10 - tileWidth / 4, yOffset + height / 10 + nameBackdropHeight + tileHeight / 2, tileHeight / 4, tileHeight / 4);
		game.font.setColor(new Color(1f, .8f, 0, 1f));
		game.font.draw(game.batcher, "" + unit.cost, xOffset + width * 9 / 10 - tileWidth * 17 / 16, yOffset + height / 10 + nameBackdropHeight + tileHeight / 2 + game.font.getLineHeight() * 4 / 5, tileWidth * 3 / 4, 0, false);
		if(unit.ability != null)
			game.batcher.draw(unit.ability.GetAbilityIcon(game), xOffset + tileWidth + tileWidth / 4 + (width - nameBackdropWidth) / 2, yOffset + height / 20 + nameBackdropHeight, tileWidth /2, tileWidth/2);
	}
	
	private void drawButtons(){
		if(state.perksPurchased > 2){
			if(state.gold >= state.GetRerollCost() && state.roster.size() < 8){
				game.batcher.draw(game.sprites.RerollEnabled, rerollX, rerollY, tileWidth, tileHeight);
			}else{
				game.batcher.draw(game.sprites.RerollDisabled, rerollX, rerollY, tileWidth, tileHeight);
			}	
			if(state.GetRerollCost() > 0){
				game.batcher.draw(game.sprites.GoldCoin, rerollX - tileWidth * 3 / 8, rerollY + tileWidth * 4 / 5, tileWidth / 4, tileHeight / 4);
				game.font.getData().setScale(.25f);
				game.font.setColor(new Color(1f, .8f, 0, 1f));
				game.font.draw(game.batcher, "" + state.GetRerollCost(), rerollX - tileWidth * 19 / 16, rerollY + tileWidth, tileWidth * 3 / 4, 0, false);
			}
		}
	}
	
	private void drawStockpile(){
		int stockpileOffset = xOffset + (width - stockWidth) / 2;
		int chestWidth = tileWidth * 28 / 50;
		int chestHeight = chestWidth * 21 / 28;
		int goldWidth = chestWidth * 26 / 28;
		int goldHeight = goldWidth * 19 / 26;	
		game.batcher.draw(game.sprites.GoldChest, stockpileOffset, rerollY + tileWidth / 2, chestWidth, chestHeight);
		game.font.getData().setScale(.35f);
		game.font.setColor(new Color(1f, .8f, 0, 1f));
		game.font.draw(game.batcher, "" + state.gold, stockpileOffset + chestWidth, rerollY + game.font.getData().lineHeight * 3 / 4 + tileWidth / 2, stockWidth - goldWidth - chestWidth, 1, false);
		game.batcher.draw(game.sprites.Gold, stockpileOffset + stockWidth - goldWidth, rerollY + tileWidth / 2, goldWidth, goldHeight);
	}
	
	private void drawTip(){
		int labelOffset = xOffset + chainSize;
		int tipOffset = xOffset + chainSize * 2 + tileWidth * 3 / 2;
		game.font.getData().setScale(.25f);
		game.font.setColor(Color.WHITE);
		// game.font.draw(game.batcher, "Overheard Whispers:", labelOffset, yOffset + chainSize + game.font.getData().lineHeight * 3, tileWidth * 3 / 2, 1, true);
		game.font.draw(game.batcher, tip, tipOffset, yOffset + chainSize + game.font.getData().lineHeight * 2, (stockWidth * 3) - chainSize, 1, true);
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
			if(state.perksPurchased > 2 && x >= rerollX && x <= rerollX + tileWidth && y >= rerollY && y <= rerollY + tileHeight){
				processRerollTouch();
			}
		
		}
	}
	
	public void processLongTouch(float x, float y, HelpPanel helpPanel){	
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
						state.isUnitDetailsOpen = !state.isUnitDetailsOpen;
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
						state.isUnitDetailsOpen = !state.isUnitDetailsOpen;
						hit = true;
					}
				}
			}
			unitOffset++;
		}
		if (!hit)
		{
			state.selected = null;
			int stockpileOffset = xOffset + (width - stockWidth) / 2;
			if(x >= stockpileOffset && x < stockpileOffset + stockWidth && y < yOffset + tileHeight && y > yOffset){
				helpPanel.title = "Stockpile";
				helpPanel.text = "You have " + state.gold + " gold to spend.";
				helpPanel.setHeight(tileHeight * 2);
				helpPanel.setWidth(tileWidth * 5);
				helpPanel.isVisible = true;		
			}	
			
			if(x >= xOffset && x < xOffset + tileWidth * 3 /2 && y >= yOffset && y < yOffset + tileWidth / 2){
				helpPanel.title = "Roster";
				helpPanel.text = "Your team of heroes.";
				helpPanel.setHeight(tileHeight * 2);
				helpPanel.setWidth(tileWidth * 4);
				helpPanel.isVisible = true;		
			}
		}

		state.isLongPressed = false;
	}
	
	private void processRerollTouch() throws IOException{
		if(state.gold > state.GetRerollCost() && state.roster.size() < 8){
			state.Reroll();
			if(state.CanBuy()){
				ShopScreen.buySound.play(game.settings.getFloat("sfxVolume", .5f));
			}else{
				ShopScreen.finalBuySound.play(game.settings.getFloat("sfxVolume", .5f));
			}
		}
	}
}
