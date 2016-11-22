package bunzosteele.heroesemblem.view;

import java.io.IOException;
import java.util.List;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.ShopState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class ShopkeeperPanel
{
	HeroesEmblem game;
	ShopState state;
	int currentFrame = 0;
	int xOffset;
	int yOffset;
	int width;
	int height;
	int tileWidth;
	int tileHeight;
	int chainSize;
	int shadowSize;
	int buttonSize;
	int purchaseX;
	int purchaseY;
	int mapX;
	int mapY;
	
	public ShopkeeperPanel( HeroesEmblem game,  ShopState state,  int width,  int height,  int xOffset,  int yOffset)
	{
		this.game = game;
		this.state = state;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		this.tileWidth = width / 3;
		this.tileHeight = height / 9;
		this.chainSize = tileWidth / 5;
		this.shadowSize = chainSize / 4; 
		this.buttonSize = width * 4 / 10;
		int backdropWidth = (int) (width * .8);
		int backdropHeight = (int) Math.floor(backdropWidth * .353);
		int dividerWidth = width - 2 * chainSize;
		int dividerHeight = (int) (dividerWidth * .0547);
		int buttonRegionHeight =  height - chainSize - backdropHeight * 7 - shadowSize * 2 - chainSize;
		int buttonVerticalSpacing = (buttonRegionHeight - 2 * buttonSize - dividerHeight) / 4;
		this.purchaseX = xOffset + width - buttonSize - chainSize;;
		this.purchaseY = yOffset + chainSize + shadowSize + buttonVerticalSpacing;
		this.mapX = xOffset + chainSize;
		this.mapY = yOffset + chainSize + shadowSize + buttonVerticalSpacing;
	}

	public void draw() throws IOException
	{
		drawButtons();
		drawPerks();
	}
	
	private void drawPerks(){
		int shopkeeperWidth = tileWidth;
		int shopkeeperHeight = shopkeeperWidth * 82 / 60;
		game.batcher.draw(game.sprites.Shopkeeper, (width - shopkeeperWidth)/2, Gdx.graphics.getHeight() - tileHeight / 2 - shopkeeperHeight, shopkeeperWidth, shopkeeperHeight);
		
		int perkWidth = (width - (chainSize - shadowSize) * 2) * 9 / 10;
		int perkHeight = perkWidth * 25 / 122;
		int perkYOffset = Gdx.graphics.getHeight() - tileHeight / 2 - shopkeeperHeight - perkHeight * 2;
		int perkXOffset = xOffset + (width - perkWidth) / 2;
		int dividerWidth = perkWidth / 4;
		int dividerHeight = dividerWidth * 7 / 30; 
		game.font.getData().setScale(.25f);
		for(int i = 0; i < 8; i++){
			if(state.perksPurchased == i || ( i == 7 && state.perksPurchased >= i)){
				game.batcher.draw(game.sprites.PerkDivider, perkXOffset, perkYOffset - perkHeight * i + (perkHeight - dividerHeight) / 2, dividerWidth, dividerHeight);
				game.batcher.draw(game.sprites.PerkDivider, perkXOffset + perkWidth - dividerWidth - (perkWidth * 2 / 122), perkYOffset - perkHeight * i + (perkHeight - dividerHeight) / 2, dividerWidth, dividerHeight);
				game.batcher.draw(game.sprites.GoldCoin, perkXOffset + perkWidth - dividerWidth - (perkWidth * 2 / 122) - tileWidth * 3 / 8, perkYOffset - perkHeight * i + tileHeight / 8 + dividerHeight / 7, tileHeight / 4, tileHeight / 4);
				game.font.setColor(new Color(1f, .8f, 0, 1f));
				game.font.draw(game.batcher, "" + state.GetNextPerkCost(), perkXOffset + dividerWidth, perkYOffset - perkHeight * i + tileHeight / 8 + game.font.getLineHeight() * 4 / 5 + dividerHeight / 7, perkWidth - 2 * dividerWidth - tileWidth * 3 / 8, 1, false);
				continue;
			}
			if(state.perksPurchased > i){
				game.batcher.draw(game.sprites.PerkEnabled, perkXOffset, perkYOffset - perkHeight * i, perkWidth, perkHeight);
				game.font.setColor(new Color(1f, .8f, 0, 1f));
				game.font.draw(game.batcher, state.GetPerkName(i + 1), perkXOffset, perkYOffset - perkHeight * i + tileHeight / 8 + game.font.getLineHeight(), perkWidth - perkWidth * 2 /122, 1, false);

			}
			
			if(state.perksPurchased < i){
				if(state.perksPurchased + 1 == i){
					game.batcher.draw(game.sprites.PerkEmphasis, perkXOffset, perkYOffset - perkHeight * i, perkWidth, perkHeight);
					game.font.setColor(new Color(0, 0, 0, 1f));
				}else{
					game.font.setColor(new Color(0, 0, 0, .6f));
					game.batcher.draw(game.sprites.PerkDisabled, perkXOffset, perkYOffset - perkHeight * i, perkWidth, perkHeight);
				}
				game.font.draw(game.batcher, state.GetPerkName(i), perkXOffset, perkYOffset - perkHeight * i + tileHeight / 8 + game.font.getLineHeight(), perkWidth - perkWidth * 2 /122, 1, false);
			}
		}
		
		if(state.perksPurchased >= 7){
			game.font.setColor(new Color(0, 0, 0, 1f));
			game.batcher.draw(game.sprites.PerkEmphasis, perkXOffset, perkYOffset - perkHeight * 8, perkWidth, perkHeight);
		}else{
			game.font.setColor(new Color(0, 0, 0, .6f));
			game.batcher.draw(game.sprites.PerkDisabled, perkXOffset, perkYOffset - perkHeight * 8, perkWidth, perkHeight);
		}
		game.font.draw(game.batcher, state.GetPerkName(state.perksPurchased < 8 ? 8 : state.perksPurchased + 1), perkXOffset, perkYOffset - perkHeight * 8 + tileHeight / 8 + game.font.getLineHeight(), perkWidth - perkWidth * 2 /122, 1, false);
	}
	/*
	private void drawMap(){
		if(state.perksPurchased > 0){
			game.font.draw(game.batcher, "Next Battle:", xOffset + columnWidth * 3, Gdx.graphics.getHeight() - columnWidth * 3 / 2 + game.font.getData().lineHeight, columnWidth * 6, 1, false);
			int rowOffset = 1;
			for ( List<Tile> row : state.nextBattlefield)
			{
				int tileOffset = 0;
				for ( Tile tile : row)
				{
					 AtlasRegion tileRegion = game.textureAtlas.findRegion(tile.type);
					 Sprite tileSprite = new Sprite(tileRegion);
					game.batcher.draw(tileSprite, xOffset + columnWidth * 9 / 2 + ((columnWidth * 3) / 16 * (tileOffset)), Gdx.graphics.getHeight() - columnWidth * 3 / 2 -  ((columnWidth * 3) / 16  * rowOffset), (columnWidth * 3) / 16, (columnWidth * 3) / 16);
					tileOffset++;
				}
				rowOffset++;
			}
			rowOffset = 1;
			for ( List<Tile> row : state.nextBattlefield)
			{
				int tileOffset = 0;
				for ( Tile tile : row)
				{
					if(tile.foreground != null){
						 AtlasRegion foregroundRegion = game.textureAtlas.findRegion(tile.foreground);
						 Sprite foregroundSprite = new Sprite(foregroundRegion);
						game.batcher.draw(foregroundSprite, xOffset + columnWidth * 9 / 2 + ((columnWidth * 3) / 16 * (tileOffset)), Gdx.graphics.getHeight() - columnWidth * 3 / 2 -  ((columnWidth * 3) / 16  * rowOffset), (columnWidth * 3) / 16, (columnWidth * 3) / 16);
					}
					tileOffset++;
				}
				rowOffset++;
			}
		}
	}*/

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
	
	public void drawBorder(){
		int chainXOffset = chainSize - shadowSize;
		int chainYOffset = chainSize - shadowSize;
		while (chainXOffset < width - (chainSize - shadowSize)){
			game.batcher.draw(game.sprites.ChainHorizontal, xOffset + chainXOffset, Gdx.graphics.getHeight() - chainSize, chainSize - shadowSize, chainSize);
			game.batcher.draw(game.sprites.ChainHorizontal, xOffset + chainXOffset, -shadowSize, chainSize - shadowSize, chainSize);
			chainXOffset += chainSize - shadowSize;
		}
		while (chainYOffset < height - (chainSize - shadowSize)){
			game.batcher.draw(game.sprites.ChainVertical, xOffset, chainYOffset, chainSize, chainSize - shadowSize);
			game.batcher.draw(game.sprites.ChainVertical, xOffset + width - (chainSize - shadowSize), chainYOffset, chainSize, chainSize - shadowSize);
			chainYOffset += chainSize - shadowSize;
		}
		
		game.batcher.draw(game.sprites.NewChainNW, xOffset, Gdx.graphics.getHeight() - (chainSize - shadowSize), chainSize - shadowSize, chainSize - shadowSize);
		game.batcher.draw(game.sprites.NewChainNE, xOffset + width - (chainSize - shadowSize), Gdx.graphics.getHeight() - (chainSize - shadowSize), chainSize - shadowSize, chainSize - shadowSize);
		game.batcher.draw(game.sprites.NewChainSW, xOffset, 0, chainSize - shadowSize, chainSize - shadowSize);
		game.batcher.draw(game.sprites.NewChainSE, xOffset + width - (chainSize - shadowSize), 0, chainSize - shadowSize, chainSize - shadowSize);
	}
	
	public void drawButtons(){		
		if(state.CanBuyPerk()){
			game.batcher.draw(game.sprites.PurchaseEnabled, purchaseX, purchaseY, buttonSize, buttonSize);
		}else{
			game.batcher.draw(game.sprites.PurchaseDisabled, purchaseX, purchaseY, buttonSize, buttonSize);
		}
		
		game.batcher.draw(game.sprites.MapDisabled, mapX, mapY, buttonSize, buttonSize);
	}

	public boolean isTouched( float x,  float y)
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

	public void processTouch( float x,  float y) throws IOException
	{
		int clickedX = Gdx.input.getX();
		int clickedY = Gdx.graphics.getHeight() - Gdx.input.getY();
		if((clickedX > purchaseX && clickedX < purchaseX + buttonSize) && (clickedY > purchaseY && clickedY < purchaseY + buttonSize)){
			processPurchaseTouch();
		}else if((clickedX > mapX && clickedX < mapX + buttonSize) && (clickedY > mapY && clickedY < mapY + buttonSize)){
			processMapTouch();
		}else{
			state.selected = null;
		}

	}
	
	private void processPurchaseTouch() throws IOException{
		if(state.CanBuyPerk()){
			state.BuyPerk();
			if(state.CanBuy()){
				ShopScreen.buySound.play(game.settings.getFloat("sfxVolume", .5f));
			}else{
				ShopScreen.finalBuySound.play(game.settings.getFloat("sfxVolume", .5f));
			}
		}
	}
	
	private void processMapTouch(){
		
	}
}
