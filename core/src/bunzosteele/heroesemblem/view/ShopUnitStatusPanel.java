package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.ShopState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class ShopUnitStatusPanel
{
	HeroesEmblem game;
	ShopState state;
	int xOffset;
	int yOffset;
	int width;
	int height;
	Sprite backdrop;
	Sprite settingsIcon;
	Sprite infoOpen;
	Sprite infoClose;

	public ShopUnitStatusPanel(final HeroesEmblem game, final ShopState state, final int width, final int height, final int xOffset, final int yOffset)
	{
		this.game = game;
		this.state = state;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		final AtlasRegion backdropRegion = this.game.textureAtlas.findRegion("BackdropRight");
		this.backdrop = new Sprite(backdropRegion);
		final AtlasRegion settingsRegion = this.game.textureAtlas.findRegion("settingsIcon");
		this.settingsIcon = new Sprite(settingsRegion);
		final AtlasRegion infoOpenRegion = this.game.textureAtlas.findRegion("InfoOpen");
		this.infoOpen = new Sprite(infoOpenRegion);
		final AtlasRegion infoCloseRegion = this.game.textureAtlas.findRegion("InfoClose");
		this.infoClose = new Sprite(infoCloseRegion);
	}

	public void draw() throws IOException
	{
		drawBackground();
		if(this.state.isShopkeeperPanelDisplayed){
			drawPerkInfo();
		}else{
			drawUnitInfo();
		}
	}
	
	private void drawUnitInfo() throws IOException{
		final int scaledSize = this.width / 3;
		this.game.batcher.draw(this.settingsIcon, xOffset + scaledSize * 17 / 8, Gdx.graphics.getHeight() - scaledSize * 7 / 8, scaledSize / 2, scaledSize / 2);
		if (this.state.selected != null)
		{
			if(this.state.isInspecting){
			this.game.batcher.draw(this.infoClose, xOffset + scaledSize / 4, Gdx.graphics.getHeight() - this.height + scaledSize / 2, scaledSize / 2, scaledSize / 2);
			}else{
				this.game.batcher.draw(this.infoOpen, xOffset + scaledSize / 4, Gdx.graphics.getHeight() - this.height + scaledSize / 2, scaledSize / 2, scaledSize / 2);				
			}
			UnitRenderer.DrawUnit(this.game, this.state.selected, this.xOffset + scaledSize / 4, Gdx.graphics.getHeight() - scaledSize * 7 / 4, scaledSize, false, false, false);
			if (!this.state.roster.contains(this.state.selected))
			{
				UnitRenderer.DrawStockStats(this.game, this.state.selected, this.xOffset + scaledSize / 4, Gdx.graphics.getHeight() - scaledSize * 6 / 4, scaledSize);
			} else
			{
				UnitRenderer.DrawOwnedStats(this.game, null, this.state.selected, this.xOffset + scaledSize / 4, Gdx.graphics.getHeight() -  scaledSize * 6 / 4, scaledSize);
			}
		}
	}
	
	private void drawPerkInfo(){
		switch(this.state.perksPurchased){
		case 0:	
			this.game.font.getData().setScale(.25f);
			this.game.font.draw(this.game.batcher, "Map:", xOffset, Gdx.graphics.getHeight() - this.width / 4, width, 1, false);
			this.game.font.getData().setScale(.18f);
			this.game.font.draw(this.game.batcher, "Shows a map of the next battlefield.", xOffset + width / 10, Gdx.graphics.getHeight() - this.width / 2, width * 8 / 10, 1, true);
			this.game.font.getData().setScale(.33f);
			break;
		case 1:
			this.game.font.getData().setScale(.25f);
			this.game.font.draw(this.game.batcher, "Spies:", xOffset, Gdx.graphics.getHeight() - this.width / 4, width, 1, false);
			this.game.font.getData().setScale(.18f);
			this.game.font.draw(this.game.batcher, "Reveals more information about enemy units.", xOffset + width / 10, Gdx.graphics.getHeight() - this.width / 2, width * 8 / 10, 1, true);
			this.game.font.getData().setScale(.33f);
			break;
		case 2:
			this.game.font.getData().setScale(.25f);
			this.game.font.draw(this.game.batcher, "Reroll:", xOffset, Gdx.graphics.getHeight() - this.width / 4, width, 1, false);
			this.game.font.getData().setScale(.18f);
			this.game.font.draw(this.game.batcher, "Allows you to generate new units to draft from. The first use of reroll each round is free.", xOffset + width / 10, Gdx.graphics.getHeight() - this.width / 2, width * 8 / 10, 1, true);
			this.game.font.getData().setScale(.33f);
			break;
		case 3:
			this.game.font.getData().setScale(.25f);
			this.game.font.draw(this.game.batcher, "Sabotage:", xOffset, Gdx.graphics.getHeight() - this.width / 4, width, 1, false);
			this.game.font.getData().setScale(.18f);
			this.game.font.draw(this.game.batcher, "Reduces the strength of enemy forces. Enemies may arrive to battle damaged.", xOffset + width / 10, Gdx.graphics.getHeight() - this.width / 2, width * 8 / 10, 1, true);
			this.game.font.getData().setScale(.33f);
			break;
		case 4:
			this.game.font.getData().setScale(.25f);
			this.game.font.draw(this.game.batcher, "Tactics:", xOffset, Gdx.graphics.getHeight() - this.width / 4, width, 1, false);
			this.game.font.getData().setScale(.18f);
			this.game.font.draw(this.game.batcher, "Choose where your units spawn at the start of each combat.", xOffset + width / 10, Gdx.graphics.getHeight() - this.width / 2, width * 8 / 10, 1, true);
			this.game.font.getData().setScale(.33f);
			break;
		case 5:
			this.game.font.getData().setScale(.25f);
			this.game.font.draw(this.game.batcher, "Renew:", xOffset, Gdx.graphics.getHeight() - this.width / 4, width, 1, false);
			this.game.font.getData().setScale(.18f);
			this.game.font.draw(this.game.batcher, "After each round, your most heavily damaged unit is restored to full health.", xOffset + width / 10, Gdx.graphics.getHeight() - this.width / 2, width * 8 / 10, 1, true);
			this.game.font.getData().setScale(.33f);
			break;
		case 6:
			this.game.font.getData().setScale(.25f);
			this.game.font.draw(this.game.batcher, "Learning:", xOffset, Gdx.graphics.getHeight() - this.width / 4, width, 1, false);
			this.game.font.getData().setScale(.18f);
			this.game.font.draw(this.game.batcher, "After each round, all units gain experience.", xOffset + width / 10, Gdx.graphics.getHeight() - this.width / 2, width * 8 / 10, 1, true);
			this.game.font.getData().setScale(.33f);
			break;
		default:
			this.game.font.getData().setScale(.25f);
			this.game.font.draw(this.game.batcher, "Training:", xOffset, Gdx.graphics.getHeight() - this.width / 4, width, 1, false);
			this.game.font.getData().setScale(.18f);
			this.game.font.draw(this.game.batcher, "Increases the level of draftable units by the number of times this perk has been unlocked.", xOffset + width / 10, Gdx.graphics.getHeight() - this.width / 2, width * 8 / 10, 1, true);
			this.game.font.getData().setScale(.33f);	
		}
	}

	public void drawBackground()
	{
		this.game.batcher.draw(this.backdrop, this.xOffset, this.yOffset, this.width, this.height);
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
		int clickedX = Gdx.input.getX();
		int clickedY = Gdx.input.getY();
		int xBoundSetting = xOffset +  (width * 2 / 3);
		int yBoundSetting = (width / 3);
		if ((clickedX > xBoundSetting) && (clickedY < yBoundSetting))
		{
			this.game.setScreen(new SettingsScreen(this.game, this.game.getScreen(), false));
			return;
		}
		int xBoundInfo = xOffset + (width / 3);
		int yBoundInfo= this.height - (width / 2);
		if ((clickedX < xBoundInfo) && (clickedY > yBoundInfo) && state.selected != null)
		{
			this.state.isInspecting = !this.state.isInspecting;
			return;
		}
		this.state.selected = null;
		this.state.isInspecting = false;
		this.state.isShopkeeperPanelDisplayed = false;
	}
}
