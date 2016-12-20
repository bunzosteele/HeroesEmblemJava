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
	int perkWidth;
	int perkHeight;
	int perkXOffset;
	int perkYOffset;
	int shopkeeperWidth;
	int shopkeeperHeight;
	
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
		this.shadowSize = chainSize / 3; 
		this.buttonSize = width / 3;
		int backdropWidth = (int) (width * .8);
		int backdropHeight = (int) Math.floor(backdropWidth * .353);
		int dividerWidth = width - 2 * chainSize;
		int dividerHeight = (int) (dividerWidth * .0547);
		int buttonRegionHeight =  height - chainSize - backdropHeight * 7 - shadowSize * 2 - chainSize;
		int buttonVerticalSpacing = (buttonRegionHeight - 2 * buttonSize - dividerHeight) / 4;
		this.purchaseX = xOffset + width - buttonSize - buttonSize / 3;
		this.purchaseY = yOffset + chainSize + shadowSize + buttonVerticalSpacing;
		this.mapX = xOffset + buttonSize / 3;
		this.mapY = yOffset + chainSize + shadowSize + buttonVerticalSpacing;
		this.shopkeeperWidth = tileWidth;
		this.shopkeeperHeight = shopkeeperWidth * 82 / 60;
		this.perkWidth = (width - (chainSize - shadowSize) * 2) * 9 / 10;
		this.perkHeight = perkWidth * 25 / 122;
		this.perkYOffset = Gdx.graphics.getHeight() - tileHeight / 2 - shopkeeperHeight - perkHeight * 2;
		this.perkXOffset = xOffset + (width - perkWidth) / 2;
	}

	public void draw() throws IOException
	{
		drawButtons();
		drawPerks();
	}
	
	private void drawPerks(){
		game.batcher.draw(game.sprites.Shopkeeper, (width - shopkeeperWidth) / 2, Gdx.graphics.getHeight() - tileHeight / 2 - shopkeeperHeight, shopkeeperWidth, shopkeeperHeight);
		
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
		int chainXOffset = xOffset;
		int chainYOffset = height + yOffset - chainSize * 2;
		
		while (chainXOffset < xOffset + width){
			if(chainXOffset > xOffset && chainXOffset < xOffset + width - chainSize)
				game.batcher.draw(game.sprites.ChainHorizontal, chainXOffset, yOffset + height - chainSize - shadowSize, chainSize, chainSize + shadowSize);
			game.batcher.draw(game.sprites.ChainHorizontal, chainXOffset, yOffset - shadowSize, chainSize, chainSize + shadowSize);
			chainXOffset += chainSize;
		}
		while (chainYOffset >= yOffset){
			if(chainYOffset > yOffset && chainYOffset < yOffset + height - chainSize)
				game.batcher.draw(game.sprites.ChainVertical, xOffset, chainYOffset, chainSize + shadowSize, chainSize);
			game.batcher.draw(game.sprites.ChainVertical, xOffset + width - chainSize, chainYOffset, chainSize + shadowSize, chainSize);
			chainYOffset -= chainSize;
		}
		
		game.batcher.draw(game.sprites.ChainNW, xOffset, yOffset + height - chainSize, chainSize, chainSize);
		game.batcher.draw(game.sprites.ChainNE, xOffset + width - chainSize, yOffset + height - chainSize, chainSize, chainSize);
		game.batcher.draw(game.sprites.ChainSW, xOffset, yOffset, chainSize, chainSize);
		game.batcher.draw(game.sprites.ChainSE, xOffset + width - chainSize, yOffset, chainSize, chainSize);
	}
	
	public void drawButtons(){		
		if(state.CanBuyPerk()){
			game.batcher.draw(game.sprites.PurchaseEnabled, purchaseX, purchaseY, buttonSize, buttonSize);
		}else{
			game.batcher.draw(game.sprites.PurchaseDisabled, purchaseX, purchaseY, buttonSize, buttonSize);
		}
		
		if(state.perksPurchased > 0){
			game.batcher.draw(game.sprites.MapEnabled, mapX, mapY, buttonSize, buttonSize);
		}else{
			game.batcher.draw(game.sprites.MapDisabled, mapX, mapY, buttonSize, buttonSize);
		}
		
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

	public void processTouch(float x,  float y) throws IOException
	{
		if((x > purchaseX && x < purchaseX + buttonSize) && (y > purchaseY && y < purchaseY + buttonSize)){
			processPurchaseTouch();
		}else if((x > mapX && x < mapX + buttonSize) && (y > mapY && y < mapY + buttonSize)){
			processMapTouch();
		}else{
			state.selected = null;
		}

	}
	
	public void processLongTouch(float x, float y, HelpPanel helpPanel){
		if(x >= (width - shopkeeperWidth) / 2 && x <= (width - shopkeeperWidth) / 2 + shopkeeperWidth && y >= Gdx.graphics.getHeight() - tileHeight / 2 - shopkeeperHeight && y <= Gdx.graphics.getHeight() - tileHeight / 2){
			helpPanel.title = "Shopkeeper";
			helpPanel.text = "Unlock useful bonuses by tipping the shopkeeper.";
			helpPanel.setHeight(tileHeight * 2);
			helpPanel.setWidth(tileWidth * 5);
			helpPanel.isVisible = true;		
		}else{
			if(x >= perkXOffset && x <= perkXOffset + perkWidth){
				for(int i = 0; i < 9; i++){
					if((state.perksPurchased == i && i != 8)|| (i == 7 && state.perksPurchased >= i)){					
						if(y >= perkYOffset - perkHeight * i && y < perkYOffset - perkHeight * (i - 1)){
							helpPanel.title = "";
							helpPanel.text = "Cost of the next perk to unlock.";
							helpPanel.setHeight(tileHeight);
							helpPanel.setWidth(tileWidth * 8);
							helpPanel.isVisible = true;		
							break;
						}
					}
					
					String perk = "";
					String helpText = "";
					if(state.perksPurchased > 7 && i == 8 && y >= perkYOffset - perkHeight * i && y < perkYOffset - perkHeight * (i-1)){
						perk = state.GetPerkName(state.perksPurchased + 1);
						helpText = state.GetPerkHelp(state.perksPurchased + 1);
					}else{
						if(state.perksPurchased > i){
							if(y >= perkYOffset - perkHeight * i && y < perkYOffset - perkHeight * (i-1)){
								perk = state.GetPerkName(i + 1);
								helpText = state.GetPerkHelp(i + 1);
							}
						}
						
						if(state.perksPurchased < i){
							if(y >= perkYOffset - perkHeight * i && y < perkYOffset - perkHeight * (i-1)){
								perk = state.GetPerkName(i);
								helpText = state.GetPerkHelp(i);	
							}
						}
					}
					if(!perk.equals("")){
						helpPanel.title = perk;
						helpPanel.text = helpText;
						helpPanel.setHeight(tileHeight * 2);
						helpPanel.setWidth(tileWidth * 5);
						helpPanel.isVisible = true;	
						break;
					}
				}
			}
		}
		state.isLongPressed = false;
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
		if(state.perksPurchased > 0)
			state.isMapOpen = !state.isMapOpen;
	}
}
