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
		this.chainSize = 20;
		this.shadowSize = chainSize / 4; 
		this.buttonSize = width * 4 / 10;
		int backdropWidth = (int) (width * .8);
		int backdropHeight = (int) Math.floor(backdropWidth * .353);
		int dividerWidth = width - 2 * chainSize;
		int dividerHeight = (int) (dividerWidth * .0547);
		int buttonRegionHeight =  height - chainSize - backdropHeight * 7 - shadowSize * 2 - chainSize;
		int buttonVerticalSpacing = (buttonRegionHeight - 2 * buttonSize - dividerHeight) / 4;
		this.purchaseX = xOffset + width - buttonSize - (chainSize - shadowSize) * 2;
		this.purchaseY = yOffset + chainSize + shadowSize + buttonVerticalSpacing;
		this.mapX = xOffset + (chainSize - shadowSize) * 2;
		this.mapY = yOffset + chainSize + shadowSize + buttonVerticalSpacing;
	}

	public void draw() throws IOException
	{
		drawBackground();
		drawBorder();
		drawButtons();
		drawPerks();
	}
	
	private void drawPerks(){
		int shopkeeperWidth = tileWidth;
		int shopkeeperHeight = shopkeeperWidth * 82 / 60;
		game.batcher.draw(game.sprites.Shopkeeper, (width - shopkeeperWidth)/2, Gdx.graphics.getHeight() - tileHeight / 2 - shopkeeperHeight, shopkeeperWidth, shopkeeperHeight);
		/*
		game.font.setColor(Color.WHITE);
		game.font.getData().setScale(.28f);
		if(state.perksPurchased == 0)
			game.font.setColor(Color.GRAY);	
		game.font.draw(game.batcher, "Map", xOffset + columnWidth / 2, Gdx.graphics.getHeight() - columnWidth * 5 / 2, columnWidth * 2, 1, false);
		if(state.perksPurchased < 2)
			game.font.setColor(Color.GRAY);	
		game.font.draw(game.batcher, "Spies", xOffset + columnWidth / 2, Gdx.graphics.getHeight() - columnWidth * 5 / 2 - game.font.getData().lineHeight , columnWidth * 2, 1, false);
		if(state.perksPurchased < 3)
			game.font.setColor(Color.GRAY);
		game.font.draw(game.batcher, "Reroll", xOffset + columnWidth / 2, Gdx.graphics.getHeight() - columnWidth * 5 / 2 - game.font.getData().lineHeight * 2, columnWidth * 2, 1, false);
		if(state.perksPurchased < 4)
			game.font.setColor(Color.GRAY);
		game.font.draw(game.batcher, "Sabotage", xOffset + columnWidth / 2, Gdx.graphics.getHeight() - columnWidth * 5 / 2 - game.font.getData().lineHeight * 3, columnWidth * 2, 1, false);
		if(state.perksPurchased < 5)
			game.font.setColor(Color.GRAY);
		game.font.draw(game.batcher, "Tactics", xOffset + columnWidth / 2, Gdx.graphics.getHeight() - columnWidth * 5 / 2 - game.font.getData().lineHeight * 4, columnWidth * 2, 1, false);
		if(state.perksPurchased < 6)
			game.font.setColor(Color.GRAY);
		game.font.draw(game.batcher, "Renew", xOffset + columnWidth / 2, Gdx.graphics.getHeight() - columnWidth * 5 / 2 - game.font.getData().lineHeight * 5, columnWidth * 2, 1, false);
		if(state.perksPurchased < 7)
			game.font.setColor(Color.GRAY);
		game.font.draw(game.batcher, "Learning", xOffset + columnWidth / 2, Gdx.graphics.getHeight() - columnWidth * 5 / 2 - game.font.getData().lineHeight * 6, columnWidth * 2, 1, false);
		game.font.setColor(Color.GRAY);
		game.font.draw(game.batcher, "Training "+ (state.GetTrainingPerkLevel() + 1), xOffset + columnWidth / 2, Gdx.graphics.getHeight() - columnWidth * 5 / 2 - game.font.getData().lineHeight * 7, columnWidth * 2, 1, false);
		game.font.setColor(Color.WHITE);*/
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
		int chainXOffset = 0;
		int chainYOffset = 0;
		while (chainYOffset < height){
			game.batcher.draw(game.sprites.ChainVertical, xOffset, chainYOffset, chainSize, chainSize);
			chainYOffset += chainSize;
		}
		while (chainXOffset < width){
			game.batcher.draw(game.sprites.ChainHorizontal, xOffset + chainXOffset, Gdx.graphics.getHeight() - chainSize, chainSize, chainSize);
			game.batcher.draw(game.sprites.ChainHorizontal, xOffset + chainXOffset, -shadowSize, chainSize, chainSize);
			chainXOffset += chainSize;
		}
		
		game.batcher.draw(game.sprites.ChainNW, xOffset, 2 * tileHeight, chainSize, chainSize);
		game.batcher.draw(game.sprites.ChainSW, xOffset -1, 0, chainSize -1, chainSize -1);
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
		/*
		if(x >= columnWidth / 2
				&& x <= columnWidth * 5 / 2
				&& y >= Gdx.graphics.getHeight() - columnWidth * 2
				&& y <= Gdx.graphics.getHeight() - columnWidth * 3 / 2){
			if(state.CanBuyPerk()){
				state.BuyPerk();
				if(state.CanBuy()){
					ShopControls.buySound.play(game.settings.getFloat("sfxVolume", .5f));
				}else{
					ShopControls.BuySound.play(game.settings.getFloat("sfxVolume", .5f));
				}
			}
		}
		if(x >= columnWidth * 13 / 2 && x <= columnWidth * 17 / 2 && y >= Gdx.graphics.getHeight() - columnWidth && state.isShopkeeperPanelDisplayed){
			state.isShopkeeperPanelDisplayed = false;
		}
		*/
	}
}
