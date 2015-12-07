package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.ShopState;

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
		this.columnWidth = width * 2 / 15;
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
	}

	public void draw() throws IOException
	{
		drawBackground();
		final int scaledSize = this.width / 9;
		final AtlasRegion shopkeeperRegion = this.game.textureAtlas.findRegion("Shopkeeper-" + this.currentFrame);
		Sprite shopkeeperSprite = new Sprite(shopkeeperRegion);
		this.game.batcher.draw(pedestalSprite, xOffset + width / 4 - scaledSize / 2, Gdx.graphics.getHeight() - scaledSize * 5 / 4, scaledSize, scaledSize);
		this.game.batcher.draw(shopkeeperSprite, xOffset + width / 4 - scaledSize / 2 + scaledSize / 10, Gdx.graphics.getHeight()  + scaledSize / 10 - scaledSize * 5 / 4, scaledSize * 8 / 10, scaledSize * 8 / 10);
		if(this.state.CanBuyPerk()){
			this.game.batcher.draw(activeButton, xOffset + width / 4 - scaledSize * 3 / 2, Gdx.graphics.getHeight() - scaledSize * 2, scaledSize * 3, scaledSize / 2);
		}else{
			this.game.batcher.draw(inactiveButton, xOffset + width / 4 - scaledSize * 3 / 2, Gdx.graphics.getHeight() - scaledSize * 2, scaledSize * 3, scaledSize / 2);
		}

		this.game.font.setColor(Color.WHITE);
		switch(this.state.perksPurchased){
		case 0:	
			this.game.font.draw(this.game.batcher, "Map", xOffset + width / 2, Gdx.graphics.getHeight() - scaledSize * 2 + this.game.font.getData().lineHeight * 3 / 4, width * 4 / 10, 1, false);
			this.game.font.getData().setScale(.2f);
			this.game.font.draw(this.game.batcher, "Shows a map of the next battlefield.", xOffset + width / 2, Gdx.graphics.getHeight() - scaledSize * 11 / 4 + this.game.font.getData().lineHeight * 3 / 4, width * 4 / 10, 1, true);
			this.game.font.getData().setScale(.33f);
			break;
		case 1:
			this.game.font.draw(this.game.batcher, "Spies", xOffset + width / 2, Gdx.graphics.getHeight() - scaledSize * 2 + this.game.font.getData().lineHeight * 3 / 4, width * 4 / 10, 1, false);
			this.game.font.getData().setScale(.2f);
			this.game.font.draw(this.game.batcher, "Reveals more information about enemy units.", xOffset + width / 2, Gdx.graphics.getHeight() - scaledSize * 11 / 4 + this.game.font.getData().lineHeight * 3 / 4, width * 4 / 10, 1, true);
			this.game.font.getData().setScale(.33f);
			break;
		case 2:
			this.game.font.draw(this.game.batcher, "Reroll", xOffset + width / 2, Gdx.graphics.getHeight() - scaledSize * 2 + this.game.font.getData().lineHeight * 3 / 4, width * 4 / 10, 1, false);
			this.game.font.getData().setScale(.2f);
			this.game.font.draw(this.game.batcher, "Allows you to generate new units to draft from. The first use of reroll each round is free.", xOffset + width / 2, Gdx.graphics.getHeight() - scaledSize * 11 / 4 + this.game.font.getData().lineHeight * 3 / 4, width * 4 / 10, 1, true);
			this.game.font.getData().setScale(.33f);
			break;
		case 3:
			this.game.font.draw(this.game.batcher, "Sabotage", xOffset + width / 2, Gdx.graphics.getHeight() - scaledSize * 2 + this.game.font.getData().lineHeight * 3 / 4, width * 4 / 10, 1, false);
			this.game.font.getData().setScale(.2f);
			this.game.font.draw(this.game.batcher, "Prevents your enemies from bringing extra reinforcements when you have advantageous terrain. Enemies may arrive to battle damaged.", xOffset + width / 2, Gdx.graphics.getHeight() - scaledSize * 11 / 4 + this.game.font.getData().lineHeight * 3 / 4, width * 4 / 10, 1, true);
			this.game.font.getData().setScale(.33f);
			break;
		case 4:
			this.game.font.draw(this.game.batcher, "Tactics", xOffset + width / 2, Gdx.graphics.getHeight() - scaledSize * 2 + this.game.font.getData().lineHeight * 3 / 4, width * 4 / 10, 1, false);
			this.game.font.getData().setScale(.2f);
			this.game.font.draw(this.game.batcher, "Choose where your units spawn at the start of each combat.", xOffset + width / 2, Gdx.graphics.getHeight() - scaledSize * 11 / 4 + this.game.font.getData().lineHeight * 3 / 4, width * 4 / 10, 1, true);
			this.game.font.getData().setScale(.33f);
			break;
		case 5:
			this.game.font.draw(this.game.batcher, "Renew", xOffset + width / 2, Gdx.graphics.getHeight() - scaledSize * 2 + this.game.font.getData().lineHeight * 3 / 4, width * 4 / 10, 1, false);
			this.game.font.getData().setScale(.2f);
			this.game.font.draw(this.game.batcher, "After each round, your most heavily damaged unit is restored to full health.", xOffset + width / 2, Gdx.graphics.getHeight() - scaledSize * 11 / 4 + this.game.font.getData().lineHeight * 3 / 4, width * 4 / 10, 1, true);
			this.game.font.getData().setScale(.33f);
			break;
		case 6:
			this.game.font.draw(this.game.batcher, "Learning", xOffset + width / 2, Gdx.graphics.getHeight() - scaledSize * 2 + this.game.font.getData().lineHeight * 3 / 4, width * 4 / 10, 1, false);
			this.game.font.getData().setScale(.2f);
			this.game.font.draw(this.game.batcher, "After each round, all units gain experience.", xOffset + width / 2, Gdx.graphics.getHeight() - scaledSize * 11 / 4 + this.game.font.getData().lineHeight * 3 / 4, width * 4 / 10, 1, true);
			this.game.font.getData().setScale(.33f);
			break;
		default:
			this.game.font.draw(this.game.batcher, "Training", xOffset + width / 2, Gdx.graphics.getHeight() - scaledSize * 2 + this.game.font.getData().lineHeight * 3 / 4, width * 4 / 10, 1, false);
			this.game.font.getData().setScale(.2f);
			this.game.font.draw(this.game.batcher, "Increases the level of draftable units by the number of times this perk has been unlocked.", xOffset + width / 2, Gdx.graphics.getHeight() - scaledSize * 11 / 4 + this.game.font.getData().lineHeight * 3 / 4, width * 4 / 10, 1, true);
			this.game.font.getData().setScale(.33f);	
		}
		this.game.font.getData().setScale(.28f);
		this.game.font.draw(this.game.batcher, "Buy Perk", xOffset + width / 4 - scaledSize * 3 / 2, Gdx.graphics.getHeight() - scaledSize * 2 + this.game.font.getData().lineHeight * 3 / 4, scaledSize * 3, 1, false);
		this.game.batcher.draw(goldSprite,  xOffset + width / 4 - scaledSize * 3 / 4, Gdx.graphics.getHeight() - scaledSize * 11 / 4, scaledSize / 2, scaledSize / 2);
		this.game.font.draw(this.game.batcher, "" + this.state.GetNextPerkCost(), xOffset + width / 4 - scaledSize / 4, Gdx.graphics.getHeight() - scaledSize * 11 / 4 + this.game.font.getData().lineHeight * 3 / 4, scaledSize * 3 / 2, 1, false);
		if(this.state.perksPurchased == 0)
			this.game.font.setColor(Color.GRAY);	
		this.game.font.draw(this.game.batcher, "Map", xOffset + width / 4 - scaledSize * 3 / 2, Gdx.graphics.getHeight() - scaledSize * 3, scaledSize * 3, 1, false);
		if(this.state.perksPurchased < 2)
			this.game.font.setColor(Color.GRAY);	
		this.game.font.draw(this.game.batcher, "Spies", xOffset + width / 4 - scaledSize * 3 / 2, Gdx.graphics.getHeight() - scaledSize * 3 - this.game.font.getData().lineHeight , scaledSize * 3, 1, false);
		if(this.state.perksPurchased < 3)
			this.game.font.setColor(Color.GRAY);
		this.game.font.draw(this.game.batcher, "Reroll", xOffset + width / 4 - scaledSize * 3 / 2, Gdx.graphics.getHeight() - scaledSize * 3 - this.game.font.getData().lineHeight * 2, scaledSize * 3, 1, false);
		if(this.state.perksPurchased < 4)
			this.game.font.setColor(Color.GRAY);
		this.game.font.draw(this.game.batcher, "Sabotage", xOffset + width / 4 - scaledSize * 3 / 2, Gdx.graphics.getHeight() - scaledSize * 3 - this.game.font.getData().lineHeight * 3, scaledSize * 3, 1, false);
		if(this.state.perksPurchased < 5)
			this.game.font.setColor(Color.GRAY);
		this.game.font.draw(this.game.batcher, "Tactics", xOffset + width / 4 - scaledSize * 3 / 2, Gdx.graphics.getHeight() - scaledSize * 3 - this.game.font.getData().lineHeight * 4, scaledSize * 3, 1, false);
		if(this.state.perksPurchased < 6)
			this.game.font.setColor(Color.GRAY);
		this.game.font.draw(this.game.batcher, "Renew", xOffset + width / 4 - scaledSize * 3 / 2, Gdx.graphics.getHeight() - scaledSize * 3 - this.game.font.getData().lineHeight * 5, scaledSize * 3, 1, false);
		if(this.state.perksPurchased < 7)
			this.game.font.setColor(Color.GRAY);
		this.game.font.draw(this.game.batcher, "Learning", xOffset + width / 4 - scaledSize * 3 / 2, Gdx.graphics.getHeight() - scaledSize * 3 - this.game.font.getData().lineHeight * 6, scaledSize * 3, 1, false);
		this.game.font.setColor(Color.GRAY);
		this.game.font.draw(this.game.batcher, "Training "+ (this.state.GetTrainingPerkLevel() + 1), xOffset + width / 4 - scaledSize * 3 / 2, Gdx.graphics.getHeight() - scaledSize * 3 - this.game.font.getData().lineHeight * 7, scaledSize * 3, 1, false);
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
		if(x >= xOffset + width / 4 - width / 6
				&& x <= xOffset + width / 4 + width / 6
				&& y >= Gdx.graphics.getHeight() - width * 2 / 9
				&& y <= Gdx.graphics.getHeight() - width * 2 / 9 + width / 18){
			if(this.state.CanBuyPerk()){
				this.state.BuyPerk();
				if(state.CanBuy()){
					ShopControls.buySound.play(this.game.settings.getFloat("sfxVolume", .5f));
				}else{
					ShopControls.finalBuySound.play(this.game.settings.getFloat("sfxVolume", .5f));
				}
			}
		}
	}
}
