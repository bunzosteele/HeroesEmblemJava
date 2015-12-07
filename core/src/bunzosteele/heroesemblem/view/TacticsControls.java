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


public class TacticsControls
{
	HeroesEmblem game;
	BattleState state;
	int xOffset;
	int yOffset;
	int buttonWidth;
	int rosterWidth;
	int height;
	Sprite inactiveButton;
	Sprite activeButton;
	Sprite emphasisButton;
	Sprite backdrop;
	Sprite blueSelect;
	List<Unit> unplacedUnits;
	boolean startingWithBenched = false;

	public TacticsControls(final HeroesEmblem game, final BattleState state, final int buttonWidth, final int rosterWidth, final int height)
	{
		this.game = game;
		this.state = state;
		this.xOffset = 0;
		this.yOffset = 0;
		this.buttonWidth = buttonWidth;
		this.rosterWidth = rosterWidth;
		this.height = height;
		final AtlasRegion inactiveRegion = this.game.textureAtlas.findRegion("InactiveButton");
		this.inactiveButton = new Sprite(inactiveRegion);
		final AtlasRegion activeRegion = this.game.textureAtlas.findRegion("ActiveButton");
		this.activeButton = new Sprite(activeRegion);
		final AtlasRegion emphasisRegion = this.game.textureAtlas.findRegion("EmphasisButton");
		this.emphasisButton = new Sprite(emphasisRegion);
		final AtlasRegion backdropRegion = this.game.textureAtlas.findRegion("BackdropBottom");
		this.backdrop = new Sprite(backdropRegion);
		final AtlasRegion blueSelectRegion = this.game.textureAtlas.findRegion("BlueSelect");
		this.blueSelect = new Sprite(blueSelectRegion);
		unplacedUnits = new ArrayList<Unit>();
		updateState();
	}
	
	public void updateState(){
		List<Unit> unitsToRemove = new ArrayList<Unit>();
		for(Unit unit : unplacedUnits){
			if(unit.x >= 0 || unit.y >= 0)
				unitsToRemove.add(unit);
		}
		this.unplacedUnits.removeAll(unitsToRemove);
		
		for(Unit unit: this.state.roster){
			if(!unplacedUnits.contains(unit) && unit.x == -1 && unit.y == -1){
				unplacedUnits.add(unit);
			}
		}
		
		this.state.hasBenchedUnits = this.unplacedUnits.size() > 0;
	}

	private boolean canStartGame()
	{
		return this.state.roster.size() > unplacedUnits.size();
	}

	public void draw()
	{
		this.drawPlaceAllBackground();
		this.drawRosterBackground();
		this.drawStartGameBackground();
		this.game.font.getData().setScale(.25f);
		this.drawPlaceAll();
		this.drawRoster();
		this.drawStartGame();
		this.game.font.getData().setScale(.33f);
	}

	private void drawPlaceAll()
	{
		this.game.font.setColor(Color.WHITE);
		this.game.font.draw(this.game.batcher, "Place All", this.xOffset, this.yOffset + ((3 * this.height) / 5), this.buttonWidth, 1, false);
	}

	private void drawPlaceAllBackground()
	{
		if (this.unplacedUnits.size() > 0)
		{
			this.game.batcher.draw(activeButton, this.xOffset, this.yOffset, this.buttonWidth, this.height);
		}else{
			this.game.batcher.draw(inactiveButton, this.xOffset, this.yOffset, this.buttonWidth, this.height);
		}
	}

	private void drawStartGame()
	{
		this.game.font.setColor(Color.WHITE);
		if(this.startingWithBenched){
			this.game.font.getData().setScale(.2f);
			this.game.font.draw(this.game.batcher, "Are you sure?", this.buttonWidth + this.rosterWidth, this.yOffset + ((3 * this.height) / 5), this.buttonWidth, 1, false);
			this.game.font.getData().setScale(.25f);
		}else{
			this.game.font.draw(this.game.batcher, "Complete", this.buttonWidth + this.rosterWidth, this.yOffset + ((3 * this.height) / 5), this.buttonWidth, 1, false);
		}
	}

	private void drawStartGameBackground()
	{	
		if(this.canStartGame()){
			if (this.state.hasBenchedUnits)
			{
				this.game.batcher.draw(activeButton, this.buttonWidth + this.rosterWidth, this.yOffset, this.buttonWidth, this.height);
			}else{
				this.game.batcher.draw(emphasisButton, this.buttonWidth + this.rosterWidth, this.yOffset, this.buttonWidth, this.height);
			}
		}else{
			this.game.batcher.draw(inactiveButton, this.buttonWidth + this.rosterWidth, this.yOffset, this.buttonWidth, this.height);
		}
	}

	private void drawRoster()
	{
		int unitOffset = 0;
		final int columnWidth = this.rosterWidth / 10;
		final int gapWidth = ((2 * this.rosterWidth) / 10) / 9;

		for (final Unit unit : this.unplacedUnits)
		{
			if ((this.state.selected != null) && this.state.selected.isEquivalentTo(unit))
			{
				this.game.batcher.draw(blueSelect, this.xOffset + this.buttonWidth + gapWidth + ((gapWidth * unitOffset) + 1) + (columnWidth * unitOffset),  (this.height - columnWidth) / 2, columnWidth, columnWidth);
				UnitRenderer.DrawUnit(this.game, unit, this.xOffset + this.buttonWidth + gapWidth + ((gapWidth * unitOffset) + 1) + (columnWidth * unitOffset), (this.height - columnWidth) / 2, columnWidth, true, false);
			} else
			{
				UnitRenderer.DrawUnit(this.game, unit, this.xOffset + this.buttonWidth + gapWidth + ((gapWidth * unitOffset) + 1) + (columnWidth * unitOffset), (this.height - columnWidth) / 2, columnWidth, false, false);
			}
			unitOffset++;
		}
	}

	public void drawRosterBackground()
	{
		this.game.batcher.draw(backdrop, this.buttonWidth, this.yOffset, this.rosterWidth, this.height);
	}

	public boolean isTouched(final float x, final float y)
	{
		if(state.isInTactics){
			if ((x >= this.xOffset) && (x < (this.xOffset + (2 * this.buttonWidth) + this.rosterWidth)))
			{
				if ((y >= this.yOffset) && (y < (this.yOffset + this.height)))
				{
					return true;
				}
			}
		}
		return false;
	}

	private void processPlaceAllTouch() throws IOException
	{
		this.startingWithBenched = false;
		List<Spawn> playerSpawns = this.state.GetPlayerSpawns(this.state.battlefieldId);
		List<Spawn> spawnOptions = new ArrayList<Spawn>();
		spawnOptions.addAll(playerSpawns);
		for(Unit unit : this.state.AllUnits()){
			for(Spawn spawn : playerSpawns){
				if(unit.x == spawn.x && unit.y == spawn.y && spawnOptions.contains(spawn))
					spawnOptions.remove(spawn);
			}
		}
		
		Random random = new Random();
		for(Unit unit : unplacedUnits){
			int rand = random.nextInt(spawnOptions.size());
			Spawn spawn = spawnOptions.get(rand);
			spawnOptions.remove(rand);
			unit.x = spawn.x;
			unit.y = spawn.y;
		}
		
		unplacedUnits.removeAll(unplacedUnits);
	}

	private void processStartGameTouch() throws IOException
	{
		this.state.selected = null;
		if (this.canStartGame())
		{
			if(this.state.hasBenchedUnits){
				if(this.startingWithBenched){
					this.state.isInTactics = false;
				}else{
					this.startingWithBenched = true;
				}
			}else{
				this.state.isInTactics = false;
			}
		}
	}

	private void processRosterTouch(final float x)
	{
		this.startingWithBenched = false;
		int unitOffset = 0;
		final int columnWidth = this.rosterWidth / 10;
		final int gapWidth = ((2 * this.rosterWidth) / 10) / 9;
		boolean isHit = false;

		for (final Unit unit : this.unplacedUnits)
		{
			final int lowerXBound = this.xOffset + this.buttonWidth + gapWidth + ((gapWidth * unitOffset) + 1) + (columnWidth * unitOffset);
			final int upperXBound = this.xOffset + this.buttonWidth + gapWidth + ((gapWidth * unitOffset) + 1) + (columnWidth * unitOffset) + columnWidth;

			if ((x >= lowerXBound) && (x <= upperXBound))
			{
				isHit = true;
				this.state.selected = this.unplacedUnits.get(unitOffset);
			}
			unitOffset++;
		}
		if (!isHit)
		{
			if(this.state.selected != null && this.state.selected.team == 0){
				this.state.selected.x = -1;
				this.state.selected.y = -1;
			}
			this.state.selected = null;
		}
	}

	public void processTouch(final float x, final float y) throws IOException
	{
		if ((x >= this.xOffset) && (x < (this.xOffset + this.buttonWidth)))
		{
			if ((y >= this.yOffset) && (y < (this.yOffset + this.height)))
			{
				this.processPlaceAllTouch();
			}
		}
		if ((x >= (this.xOffset + this.buttonWidth)) && (x < (this.xOffset + this.buttonWidth + this.rosterWidth)))
		{
			if ((y >= this.yOffset) && (y < (this.yOffset + this.height)))
			{
				this.processRosterTouch(x);
			}
		}
		if ((x >= (this.xOffset + this.buttonWidth + this.rosterWidth)) && (x < (this.xOffset + (2 * this.buttonWidth) + this.rosterWidth)))
		{
			if ((y >= this.yOffset) && (y < (this.yOffset + this.height)))
			{
				this.processStartGameTouch();
			}
		}
	}
}
