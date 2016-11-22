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

public class RosterPanel
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
	int rosterSlotWidth;
	int rosterXOffset;
	int dividerHeight;
	int dividerWidth;
	int dividerShadow;
	int labelWidth;
	int labelHeight;
	int summaryWidth;
	int summaryHeight;

	public RosterPanel(final HeroesEmblem game, final ShopState state, final int width, final int height, final int xOffset, final int yOffset)
	{
		this.game = game;
		this.state = state;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		this.tileWidth = width / 10;
		this.tileHeight = height / 2;
		this.chainSize = tileWidth / 5;
		this.shadowSize = chainSize / 4; 
		this.rosterSlotWidth = width / 9;
		this.dividerHeight = height - chainSize;
		this.dividerWidth = dividerHeight * 7 / 87;	
		this.dividerShadow = dividerWidth * 2 / 7;
		this.rosterXOffset = xOffset + (width - rosterSlotWidth * 9) + dividerWidth;
		this.labelWidth = tileWidth * 3 / 2;
		this.labelHeight = labelWidth * 45 / 83;
		this.summaryWidth = labelWidth * 47 / 83;
		this.summaryHeight = summaryWidth * 40 / 47;
	}

	public void draw()
	{
		drawRoster();
	}
	
	public void drawBorder(){
		int chainXOffset = 0;
		while (chainXOffset < width){
			game.batcher.draw(game.sprites.ChainHorizontal, xOffset + chainXOffset, height, chainSize - shadowSize, chainSize);
			game.batcher.draw(game.sprites.ChainHorizontal, xOffset + chainXOffset, -shadowSize, chainSize - shadowSize, chainSize);
			chainXOffset += chainSize - shadowSize;
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
	
	private void drawRoster(){
		int tuck = labelHeight * 6 / 45;
		int rosterYOffset = chainSize;
		int levelBackdropWidth = rosterSlotWidth - dividerWidth + dividerShadow;
		int levelBackdropHeight = levelBackdropWidth * 20 / 52;
		
		for(int i = 0; i < 8; i++){
			game.batcher.draw(game.sprites.RosterDivider, xOffset + width - rosterSlotWidth * (i + 1), rosterYOffset, dividerWidth, dividerHeight);
			game.batcher.draw(game.sprites.LevelBackdrop, xOffset + width - rosterSlotWidth * (i + 1) + dividerWidth - dividerShadow, rosterYOffset, levelBackdropWidth, levelBackdropHeight);
		}
		
		for(int i = state.roster.size() - 1; i >= 0; i--){
			Unit unit = state.roster.get(i);
			if ((this.state.selected != null) && this.state.selected.isEquivalentTo(unit))
			{
				UnitRenderer.DrawUnit(game, unit, rosterXOffset + rosterSlotWidth * (i + 1), rosterYOffset + levelBackdropHeight * 3 / 2, tileWidth, true, false, false);
				game.batcher.draw(game.sprites.BlueSelect, rosterXOffset + rosterSlotWidth * (i + 1), rosterYOffset + levelBackdropHeight * 3 / 2, tileWidth, tileHeight);
			} else
			{
				UnitRenderer.DrawUnit(game, unit, rosterXOffset + rosterSlotWidth * (i + 1), rosterYOffset + levelBackdropHeight * 3 / 2, tileWidth, false, false, false);
			}
			game.font.setColor(Color.BLACK);
			game.font.getData().setScale(.25f);
			game.font.draw(game.batcher, (unit.level < 10 ? "Lvl. 0" : "Lvl. ") + unit.level, rosterXOffset + rosterSlotWidth * (i + 1), rosterYOffset + levelBackdropHeight * 8 / 10, levelBackdropWidth, 1, false);			
		}
		
		int summaryX = xOffset + chainSize + (labelWidth - summaryWidth) / 2;
		int summaryY = height - labelHeight / 2 - summaryHeight + tuck;
		
		game.batcher.draw(game.sprites.RosterSummary, summaryX, summaryY, summaryWidth, summaryHeight);
		for(int i = 0; i < state.roster.size(); i++){
			int slotWidth = summaryWidth * 7 / 47;
			int slotHeight = slotWidth * 6 / 7;
			int slotXOffset = getSlotXOffset(i);
			int slotYOffset = getSlotYOffset(i);
			game.batcher.draw(game.sprites.RosterSummarySlot, summaryX + slotXOffset, summaryY + slotYOffset, slotWidth, slotHeight);
		}
		game.batcher.draw(game.sprites.RosterLabel, xOffset + chainSize, height - labelHeight / 2, labelWidth, labelHeight);	
	}
	
	private int getSlotXOffset(int slot){
		if(slot == 0 || slot == 7){
			return summaryWidth * 8 / 47;
		}else if(slot == 1 || slot == 6){
			return summaryWidth * 16 / 47;
		}else if(slot == 2 || slot == 5){
			return summaryWidth * 25 / 47;
		}else{
			return summaryWidth * 33 / 47;
		}
	}
	
	private int getSlotYOffset(int slot){
		if(slot == 6 || slot == 5){
			return summaryHeight * 7 / 40;
		}else if(slot == 7 || slot == 4){
			return summaryHeight * 12 / 40;
		}else if(slot == 0 || slot == 3){
			return summaryHeight * 23 / 40;
		}else{
			return summaryHeight * 27 / 40;
		}
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

	public void processTouch(float x, float y) throws IOException
	{
		int rosterSlot = -1;
		x = x - rosterXOffset;
		x -= rosterSlotWidth;
		while(x > 0){
			x -= rosterSlotWidth;
			rosterSlot++;
		}
		
		if(rosterSlot >= 0 && rosterSlot < state.roster.size()){
			state.selected = state.roster.get(rosterSlot);
		}else{
			state.selected = null;
		}
		
		
	}
}
