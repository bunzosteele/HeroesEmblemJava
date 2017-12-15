package bunzosteele.heroesemblem.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Spawn;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;


public class TacticsPanel extends PopupPanel
{
	BattleState state;
	boolean startingWithBenched = false;
	int dividerHeight;
	int dividerWidth;
	int dividerShadow;
	int tileWidth;
	int rosterSlotWidth;
	int rosterWidth;

	public TacticsPanel(final HeroesEmblem game, final BattleState state, int width, int height, int xOffset, int yOffset)
	{
		super(game, width, height, xOffset, yOffset);
		this.state = state;
		this.dividerHeight = height - chainSize * 2 - shadowSize * 2;
		this.dividerWidth = dividerHeight * 7 / 87;	
		this.dividerShadow = dividerWidth * 2 / 7;
		this.tileWidth = width / 9;
		this.rosterSlotWidth = tileWidth + dividerWidth - dividerShadow;
		this.rosterWidth = rosterSlotWidth * 7 + tileWidth;
	}

	public void draw()
	{
		if (state.isTacticsOpen){
			super.drawBorder();
			int levelBackdropWidth = tileWidth;
			int levelBackdropHeight = levelBackdropWidth * 20 / 52;
			int rosterYOffset = chainSize + shadowSize + yOffset;
			
			for(int i = 0; i < 8; i++){
				if(i > 0)
					game.batcher.draw(game.sprites.RosterDivider, xOffset + (width - rosterWidth) / 2 + rosterSlotWidth * i - dividerWidth + dividerShadow, rosterYOffset, dividerWidth, dividerHeight);
				game.batcher.draw(game.sprites.LevelBackdrop, xOffset + (width - rosterWidth) / 2 + rosterSlotWidth * i, rosterYOffset, levelBackdropWidth, levelBackdropHeight);
			}
			
			for(int i = state.unplacedUnits.size() - 1; i >= 0; i--){
				Unit unit = state.unplacedUnits.get(i);
				if ((this.state.selected != null) && this.state.selected.isEquivalentTo(unit))
				{
					UnitRenderer.DrawUnit(game, unit, xOffset + (width - rosterWidth) / 2 + rosterSlotWidth * i, rosterYOffset + levelBackdropHeight, tileWidth, true, false, false);
					game.batcher.draw(game.sprites.BlueSelect, xOffset + (width - rosterWidth) / 2 + rosterSlotWidth * i, rosterYOffset + levelBackdropHeight, tileWidth, tileWidth);
				} else
				{
					UnitRenderer.DrawUnit(game, unit, xOffset + (width - rosterWidth) / 2 + rosterSlotWidth * i, rosterYOffset + levelBackdropHeight, tileWidth, false, false, false);
				}
				game.font.setColor(Color.BLACK);
				game.font.getData().setScale(.25f);
				game.font.draw(game.batcher, (unit.level < 10 ? "Lvl. 0" : "Lvl. ") + unit.level, xOffset + (width - rosterWidth) / 2 + rosterSlotWidth * i, rosterYOffset + levelBackdropHeight * 8 / 10, levelBackdropWidth, 1, false);			
			}
		}
	}
	
	public void drawBackground(){
		super.drawBackground(false);
	}

	public boolean isTouched(final float x, final float y)
	{
		if(state.isInTactics){
			if ((x >= xOffset) && (x < (xOffset + width)))
			{
				if ((y >= yOffset) && (y < (yOffset + height)))
				{
					return true;
				}
			}
		}
		return false;
	}

	private void processRosterTouch(final float x)
	{
		int unitOffset = 0;
		boolean isHit = false;

		for (final Unit unit : state.unplacedUnits)
		{
			final int lowerXBound = xOffset + (width - rosterWidth) / 2 + (rosterSlotWidth * unitOffset);
			final int upperXBound = lowerXBound + rosterSlotWidth;

			if ((x >= lowerXBound) && (x <= upperXBound))
			{
				isHit = true;
				state.selected = state.unplacedUnits.get(unitOffset);
			}
			unitOffset++;
		}
		if (!isHit)
		{
			if(state.selected != null && state.selected.team == 0 && state.selected.x >=0){
				state.selected.x = -1;
				state.selected.y = -1;
				state.unplacedUnits.add(state.selected);
			}
			state.selected = null;
		}
	}

	public void processTouch(final float x, final float y) throws IOException
	{
		processRosterTouch(x);
	}

	public void processLongTouch(int x, int i, HelpPanel helpPanel) {
		int unitOffset = 0;
		for (final Unit unit : state.unplacedUnits)
		{
			final int lowerXBound = xOffset + (width - rosterWidth) / 2 + (rosterSlotWidth * unitOffset);
			final int upperXBound = lowerXBound + rosterSlotWidth;

			if ((x >= lowerXBound) && (x <= upperXBound))
			{
				state.selected = state.unplacedUnits.get(unitOffset);
				state.isUnitDetailsOpen = !state.isUnitDetailsOpen;
			}
			unitOffset++;
		}
		state.isLongPressed = false;
	}
}
