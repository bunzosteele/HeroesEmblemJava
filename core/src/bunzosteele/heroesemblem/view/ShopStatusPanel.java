package bunzosteele.heroesemblem.view;

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

public class ShopStatusPanel
{
	HeroesEmblem game;
	ShopState state;
	int currentFrame = 1;
	int xOffset;
	int yOffset;
	int width;
	int height;
	Sprite backdrop;
	Sprite infoOpen;
	Sprite infoClose;
	Sprite buttonSprite;

	public ShopStatusPanel(final HeroesEmblem game, final ShopState state, final int width, final int height, final int yOffset)
	{
		this.game = game;
		this.state = state;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				ShopStatusPanel.this.currentFrame++;
				if (ShopStatusPanel.this.currentFrame > 3)
				{
					ShopStatusPanel.this.currentFrame = 1;
				}
			}
		}, 0, 1 / 3.0f);
		this.xOffset = 0;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		final AtlasRegion backdropRegion = this.game.textureAtlas.findRegion("BackdropLeft");
		this.backdrop = new Sprite(backdropRegion);
		final AtlasRegion infoOpenRegion = this.game.textureAtlas.findRegion("infoClose");
		this.infoOpen = new Sprite(infoOpenRegion);
		final AtlasRegion infoCloseRegion = this.game.textureAtlas.findRegion("infoOpen");
		this.infoClose = new Sprite(infoCloseRegion);
		final AtlasRegion buttonRegion = this.game.textureAtlas.findRegion("Button");
		this.buttonSprite = new Sprite(buttonRegion);
	}

	public void draw()
	{
		drawBackground();		
		final AtlasRegion shopkeeperRegion = this.game.textureAtlas.findRegion("Shopkeeper-" + this.currentFrame);
		final Sprite shopkeeperSprite = new Sprite(shopkeeperRegion);
		final AtlasRegion goldRegion = this.game.textureAtlas.findRegion("Gold");
		final Sprite goldSprite = new Sprite(goldRegion);
		final float scaledSize = (this.width / 3);
		this.game.font.setColor(Color.WHITE);
		this.game.batcher.draw(buttonSprite, this.width * 5 / 10, Gdx.graphics.getHeight() - scaledSize, this.width * 4 / 10, scaledSize /2);
		this.game.batcher.draw(shopkeeperSprite, scaledSize / 4, Gdx.graphics.getHeight() - scaledSize * 5 / 4, scaledSize, scaledSize);
		this.game.font.getData().setScale(.18f);
		this.game.font.draw(this.game.batcher, "Perks", xOffset + this.width / 2, Gdx.graphics.getHeight() - this.game.font.getData().lineHeight * 7 / 6 - scaledSize / 4, this.width * 4 / 10, 1, false);
		this.game.font.getData().setScale(.33f);
		this.game.batcher.draw(goldSprite, scaledSize / 4, yOffset + scaledSize / 4, scaledSize * 3 / 4, scaledSize * 3 / 4);
		this.game.font.draw(this.game.batcher, "" + this.state.gold, scaledSize * 11 / 10, this.yOffset + this.game.font.getData().lineHeight * 4 / 3, this.width - scaledSize * 6 / 4, 1, false);
		/*
		if(this.state.isShopkeeperPanelDisplayed){
			this.game.batcher.draw(this.infoClose, xOffset + scaledSize * 2, Gdx.graphics.getHeight() -  scaledSize, scaledSize / 2, scaledSize / 2);
		}else{
			this.game.batcher.draw(this.infoOpen, xOffset + scaledSize * 2, Gdx.graphics.getHeight() -  scaledSize, scaledSize / 2, scaledSize / 2);				
		}*/
		if(this.state.perksPurchased > 0)	
			drawMap();
	}

	public void drawBackground()
	{
		this.game.batcher.draw(this.backdrop, this.xOffset, this.yOffset, this.width, this.height);
	}
	
	private void drawMap(){
		this.game.font.getData().setScale(.2f);
		this.game.font.draw(this.game.batcher, "Next Battle:", this.width / 18, Gdx.graphics.getHeight() - this.width * 3 / 4 + this.game.font.getData().lineHeight, this.width * 8 / 9, 1, false);
		this.game.font.getData().setScale(.33f);
		int rowOffset = 1;
		for (final List<Tile> row : this.state.nextBattlefield)
		{
			int tileOffset = 0;
			for (final Tile tile : row)
			{
				final AtlasRegion tileRegion = this.game.textureAtlas.findRegion(tile.type);
				final Sprite tileSprite = new Sprite(tileRegion);
				this.game.batcher.draw(tileSprite, this.xOffset + (this.width / 18 * (tileOffset + 1)), Gdx.graphics.getHeight() - this.width * 3 / 4 -  (this.width / 18  * rowOffset), this.width / 18 , this.width / 18);
				tileOffset++;
			}
			rowOffset++;
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

	public void processTouch(final float x, final float y)
	{
		this.state.selected = null;
		this.state.isInspecting = false;
		if(x >= xOffset + this.width / 2 && x <= xOffset + this.width * 9 / 10 && y >= Gdx.graphics.getHeight() - width / 3 && y <= Gdx.graphics.getHeight() - width / 6){
			this.state.isShopkeeperPanelDisplayed = !this.state.isShopkeeperPanelDisplayed;
		}else{
			this.state.isShopkeeperPanelDisplayed = false;
		}

	}
}
