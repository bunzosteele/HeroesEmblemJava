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
	int columnWidth;
	Sprite backgroundSprite;
	Sprite goldSprite;
	Sprite pedestalSprite;
	Sprite inactiveButton;
	Sprite activeButton;
	Sprite button;
	
	public ShopkeeperPanel(final HeroesEmblem game, final ShopState state, final int width, final int height, final int xOffset, final int yOffset)
	{
		this.game = game;
		this.state = state;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				ShopkeeperPanel.this.currentFrame++;
				if (ShopkeeperPanel.this.currentFrame > 2)
				{
					ShopkeeperPanel.this.currentFrame = 0;
				}
			}
		}, 0, 1 / 3f);
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		this.columnWidth = width / 9;
		final AtlasRegion backgroundRegion = this.game.textureAtlas.findRegion("WoodFloor");
		this.backgroundSprite = new Sprite(backgroundRegion);
		final AtlasRegion goldRegion = this.game.textureAtlas.findRegion("Gold");
		this.goldSprite = new Sprite(goldRegion);
		final AtlasRegion pedestalRegion = this.game.textureAtlas.findRegion("Pedestal");
		this.pedestalSprite = new Sprite(pedestalRegion);
		final AtlasRegion inactiveRegion = this.game.textureAtlas.findRegion("InactiveButton");
		this.inactiveButton = new Sprite(inactiveRegion);
		final AtlasRegion activeRegion = this.game.textureAtlas.findRegion("ActiveButton");
		this.activeButton = new Sprite(activeRegion);
		final AtlasRegion buttonRegion = this.game.textureAtlas.findRegion("Button");
		this.button = new Sprite(buttonRegion);
	}

	public void draw() throws IOException
	{
		drawBackground();
		final AtlasRegion shopkeeperRegion = this.game.textureAtlas.findRegion("Shopkeeper-" + this.currentFrame);
		Sprite shopkeeperSprite = new Sprite(shopkeeperRegion);
		this.game.batcher.draw(pedestalSprite, xOffset + this.columnWidth, Gdx.graphics.getHeight() - this.columnWidth * 3 / 2, this.columnWidth, this.columnWidth);
		this.game.batcher.draw(shopkeeperSprite, this.columnWidth + this.columnWidth / 10, Gdx.graphics.getHeight() - this.columnWidth * 3 / 2 + this.columnWidth / 10, this.columnWidth * 8 / 10, this.columnWidth * 8 / 10);
		if(this.state.CanBuyPerk()){
			this.game.batcher.draw(activeButton, xOffset + columnWidth / 2, Gdx.graphics.getHeight() - this.columnWidth * 2, this.columnWidth * 2, this.columnWidth / 2);
		}else{
			this.game.batcher.draw(inactiveButton, xOffset + columnWidth / 2, Gdx.graphics.getHeight() - this.columnWidth * 2, this.columnWidth * 2, this.columnWidth / 2);
		}
		this.game.font.draw(this.game.batcher, "Buy Perk", xOffset + columnWidth / 2, Gdx.graphics.getHeight() - this.columnWidth * 2 + this.game.font.getData().lineHeight  * 5 / 6, this.columnWidth * 2, 1, false);
		this.game.batcher.draw(button, this.columnWidth * 13 / 2, Gdx.graphics.getHeight() - this.columnWidth * 3 / 4, this.columnWidth * 2, this.columnWidth / 2);
		this.game.font.getData().setScale(.18f);
		this.game.font.draw(this.game.batcher, "Back", this.columnWidth * 13 / 2, Gdx.graphics.getHeight() - this.columnWidth * 3 / 7, this.columnWidth * 2, 1, false);
		this.game.font.getData().setScale(.4f);
		this.game.batcher.draw(goldSprite, this.xOffset + this.columnWidth * 7 / 2, Gdx.graphics.getHeight() - this.columnWidth * 3 / 4, this.columnWidth / 2, this.columnWidth / 2);
		this.game.font.draw(this.game.batcher, "" + this.state.gold, this.xOffset + this.columnWidth * 4, Gdx.graphics.getHeight() - this.columnWidth * 3 / 4 + this.game.font.getData().lineHeight * 4 / 5, this.columnWidth * 3 / 2, 0, false);
		this.game.font.getData().setScale(.33f);
		this.game.batcher.draw(goldSprite,  xOffset + columnWidth * 3 / 4, Gdx.graphics.getHeight() - this.columnWidth * 5 / 2, this.columnWidth / 2, this.columnWidth / 2);
		this.game.font.draw(this.game.batcher, "" + this.state.GetNextPerkCost(), xOffset + columnWidth * 5 / 4, Gdx.graphics.getHeight() - this.columnWidth * 5 / 2 + this.game.font.getData().lineHeight * 6 / 7, this.columnWidth, 1, false);
		drawPerks();
		drawMap();
	}
	
	private void drawPerks(){
		this.game.font.setColor(Color.WHITE);
		this.game.font.getData().setScale(.28f);
		if(this.state.perksPurchased == 0)
			this.game.font.setColor(Color.GRAY);	
		this.game.font.draw(this.game.batcher, "Map", xOffset + columnWidth / 2, Gdx.graphics.getHeight() - this.columnWidth * 5 / 2, this.columnWidth * 2, 1, false);
		if(this.state.perksPurchased < 2)
			this.game.font.setColor(Color.GRAY);	
		this.game.font.draw(this.game.batcher, "Spies", xOffset + columnWidth / 2, Gdx.graphics.getHeight() - this.columnWidth * 5 / 2 - this.game.font.getData().lineHeight , this.columnWidth * 2, 1, false);
		if(this.state.perksPurchased < 3)
			this.game.font.setColor(Color.GRAY);
		this.game.font.draw(this.game.batcher, "Reroll", xOffset + columnWidth / 2, Gdx.graphics.getHeight() - this.columnWidth * 5 / 2 - this.game.font.getData().lineHeight * 2, this.columnWidth * 2, 1, false);
		if(this.state.perksPurchased < 4)
			this.game.font.setColor(Color.GRAY);
		this.game.font.draw(this.game.batcher, "Sabotage", xOffset + columnWidth / 2, Gdx.graphics.getHeight() - this.columnWidth * 5 / 2 - this.game.font.getData().lineHeight * 3, this.columnWidth * 2, 1, false);
		if(this.state.perksPurchased < 5)
			this.game.font.setColor(Color.GRAY);
		this.game.font.draw(this.game.batcher, "Tactics", xOffset + columnWidth / 2, Gdx.graphics.getHeight() - this.columnWidth * 5 / 2 - this.game.font.getData().lineHeight * 4, this.columnWidth * 2, 1, false);
		if(this.state.perksPurchased < 6)
			this.game.font.setColor(Color.GRAY);
		this.game.font.draw(this.game.batcher, "Renew", xOffset + columnWidth / 2, Gdx.graphics.getHeight() - this.columnWidth * 5 / 2 - this.game.font.getData().lineHeight * 5, this.columnWidth * 2, 1, false);
		if(this.state.perksPurchased < 7)
			this.game.font.setColor(Color.GRAY);
		this.game.font.draw(this.game.batcher, "Learning", xOffset + columnWidth / 2, Gdx.graphics.getHeight() - this.columnWidth * 5 / 2 - this.game.font.getData().lineHeight * 6, this.columnWidth * 2, 1, false);
		this.game.font.setColor(Color.GRAY);
		this.game.font.draw(this.game.batcher, "Training "+ (this.state.GetTrainingPerkLevel() + 1), xOffset + columnWidth / 2, Gdx.graphics.getHeight() - this.columnWidth * 5 / 2 - this.game.font.getData().lineHeight * 7, this.columnWidth * 2, 1, false);
		this.game.font.setColor(Color.WHITE);
	}
	
	private void drawMap(){
		if(this.state.perksPurchased > 0){
			this.game.font.draw(this.game.batcher, "Next Battle:", this.xOffset + this.columnWidth * 3, Gdx.graphics.getHeight() - this.columnWidth * 3 / 2 + this.game.font.getData().lineHeight, this.columnWidth * 6, 1, false);
			int rowOffset = 1;
			for (final List<Tile> row : this.state.nextBattlefield)
			{
				int tileOffset = 0;
				for (final Tile tile : row)
				{
					final AtlasRegion tileRegion = this.game.textureAtlas.findRegion(tile.type);
					final Sprite tileSprite = new Sprite(tileRegion);
					this.game.batcher.draw(tileSprite, this.xOffset + this.columnWidth * 9 / 2 + ((this.columnWidth * 3) / 16 * (tileOffset)), Gdx.graphics.getHeight() - this.columnWidth * 3 / 2 -  ((this.columnWidth * 3) / 16  * rowOffset), (this.columnWidth * 3) / 16 , (this.columnWidth * 3) / 16);
					tileOffset++;
				}
				rowOffset++;
			}
		}
	}

	public void drawBackground()
	{
		for(int i = 0; i < 20; i++){
			for(int j = 0; j < 12; j++){
				this.game.batcher.draw(backgroundSprite, (Gdx.graphics.getWidth() / 19) * i, (Gdx.graphics.getHeight() / 11) * j, (Gdx.graphics.getWidth() / 19), (Gdx.graphics.getHeight() / 11));
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
		if(x >= this.columnWidth / 2
				&& x <= this.columnWidth * 5 / 2
				&& y >= Gdx.graphics.getHeight() - this.columnWidth * 2
				&& y <= Gdx.graphics.getHeight() - this.columnWidth * 3 / 2){
			if(this.state.CanBuyPerk()){
				this.state.BuyPerk();
				if(state.CanBuy()){
					ShopControls.buySound.play(this.game.settings.getFloat("sfxVolume", .5f));
				}else{
					ShopControls.finalBuySound.play(this.game.settings.getFloat("sfxVolume", .5f));
				}
			}
		}
		if(x >= this.columnWidth * 13 / 2 && x <= this.columnWidth * 17 / 2 && y >= Gdx.graphics.getHeight() - this.columnWidth && this.state.isShopkeeperPanelDisplayed){
			this.state.isShopkeeperPanelDisplayed = false;
		}
	}
}
