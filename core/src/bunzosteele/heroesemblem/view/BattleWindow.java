package bunzosteele.heroesemblem.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.CombatHelper;
import bunzosteele.heroesemblem.model.Move;
import bunzosteele.heroesemblem.model.MovementHelper;
import bunzosteele.heroesemblem.model.Battlefield.Spawn;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class BattleWindow
{
	HeroesEmblem game;
	BattleState state;
	Texture img;
	int xOffset;
	int yOffset;
	int width;
	int height;
	int tileWidth;
	int tileHeight;
	Sprite blueSelect;
	Sprite redSelect;

	public BattleWindow(final HeroesEmblem game, final BattleState state, final int width, final int height, final int yOffset)
	{
		this.game = game;
		this.state = state;
		this.xOffset = 0;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		this.tileWidth = width / 13;
		this.tileHeight = height / 9;
		final AtlasRegion blueSelectRegion = this.game.textureAtlas.findRegion("BlueSelect");
		this.blueSelect = new Sprite(blueSelectRegion);
		final AtlasRegion redSelectRegion = this.game.textureAtlas.findRegion("RedSelect");
		this.redSelect = new Sprite(redSelectRegion);
	}

	public void draw()
	{
		this.drawBattlefield();
		this.drawUnits();
		this.drawForeground();
	}

	private void drawBattlefield()
	{
		int rowOffset = 1;
		for (final List<Tile> row : this.state.battlefield)
		{
			int tileOffset = 0;
			for (final Tile tile : row)
			{
				final AtlasRegion tileRegion = this.game.textureAtlas.findRegion(tile.type);
				final Sprite tileSprite = new Sprite(tileRegion);
				this.game.batcher.draw(tileSprite, this.xOffset + (this.tileWidth * tileOffset), Gdx.graphics.getHeight() - (this.tileHeight * rowOffset), this.tileWidth, this.tileHeight);
				tileOffset++;
			}
			rowOffset++;
		}
	}
	
	private void drawForeground()
	{
		int rowOffset = 1;
		for (final List<Tile> row : this.state.battlefield)
		{
			int tileOffset = 0;
			for (final Tile tile : row)
			{
				if(tile.foreground != null){
					final AtlasRegion foregroundRegion = this.game.textureAtlas.findRegion(tile.foreground);
					final Sprite foregroundSprite = new Sprite(foregroundRegion);
					this.game.batcher.draw(foregroundSprite, this.xOffset + (this.tileWidth * tileOffset), Gdx.graphics.getHeight() - (this.tileHeight * rowOffset), this.tileWidth, this.tileHeight);
				}
				tileOffset++;
			}
			rowOffset++;
		}
		
		for (final Unit unit : this.state.AllUnits())
		{
			if(state.selected != null && state.selected.equals(unit)){
				if(unit.team == 0){
					this.game.batcher.draw(blueSelect, this.xOffset + (this.tileWidth * unit.x), Gdx.graphics.getHeight() - (this.tileHeight * (unit.y + 1)), this.tileWidth, this.tileHeight);
				}else{
					this.game.batcher.draw(redSelect, this.xOffset + (this.tileWidth * unit.x), Gdx.graphics.getHeight() - (this.tileHeight * (unit.y + 1)), this.tileWidth, this.tileHeight);					
				}
			}
		}
	}

	private void drawHealthBar(final Unit unit)
	{
		final float healthPercent = unit.currentHealth / (float) unit.maximumHealth;
		if ((healthPercent > 0) && (healthPercent < 1))
		{
			Color color;
			if (healthPercent > .7)
			{
				color = new Color(0f, 1f, 0f, 1f);
			} else if (healthPercent > .3)
			{
				color = new Color(1f, 1f, 0f, 1f);
			} else
			{
				color = new Color(1f, 0f, 0f, 1f);
			}

			this.game.shapeRenderer.setColor(color);
			this.game.shapeRenderer.rect((unit.x * this.tileWidth) + this.xOffset + (this.tileWidth * .21875f), Gdx.graphics.getHeight() - (this.tileHeight * (unit.y + 1)), (this.tileWidth - (this.tileWidth * .4375f)) * healthPercent, this.tileHeight / 10);
		}
	}

	public void drawHealthBars()
	{
		for (final Unit unit : this.state.AllUnits())
		{
			this.drawHealthBar(unit);
		}
	}

	private void drawHighlight(final int x, final int y, final Color color)
	{
		this.game.shapeRenderer.setColor(color);
		this.game.shapeRenderer.rect((x * this.tileWidth) + this.xOffset, Gdx.graphics.getHeight() - (this.tileHeight * (y + 1)), this.tileWidth, this.tileHeight);
	}

	public void drawHighlights() throws IOException
	{
		if(this.state.selected != null && this.state.selected.team != 0 && this.state.currentPlayer == 0 && !this.state.isPreviewingAttack && !this.state.isPreviewingAbility){
			final HashSet<Tile> options = MovementHelper.GetMovementOptions(this.state, this.state.selected);
			final Color color = new Color(.2f, .2f, .7f, .3f);
			for (final Tile tile : options)
			{
				this.drawHighlight(tile.x, tile.y, color);
			}
		}
		if(this.state.selected != null && this.state.isPreviewingAttack){
			final HashSet<Tile> options = CombatHelper.GetAttackOptions(this.state, this.state.selected);
			final Color color = new Color(.7f, .2f, .2f, .3f);
			for (final Tile tile : options)
			{
				this.drawHighlight(tile.x, tile.y, color);
			}
		}
		if(this.state.selected != null && this.state.selected.ability != null && this.state.isPreviewingAbility){
			final HashSet<Tile> options = this.state.selected.ability.GetTargetTiles(this.state, this.state.selected);
			if(options != null){
				for (final Tile tile : options)
				{
					this.drawHighlight(tile.x, tile.y, this.state.selected.ability.abilityColor.add(0f, 0f, 0f, -.3f));
					this.state.selected.ability.abilityColor.add(0f, 0f, 0f, .3f);
				}
			}
		}
		if (this.state.isMoving)
		{
			final HashSet<Tile> options = MovementHelper.GetMovementOptions(this.state);
			final Color color = new Color(.2f, .2f, .7f, .6f);
			for (final Tile tile : options)
			{
				this.drawHighlight(tile.x, tile.y, color);
			}
		}
		if (this.state.isAttacking)
		{
			final HashSet<Tile> options = CombatHelper.GetAttackOptions(this.state, this.state.selected);
			final Color color = new Color(.7f, .2f, .2f, .6f);
			for (final Tile tile : options)
			{
				this.drawHighlight(tile.x, tile.y, color);
			}
		}
		if (this.state.isUsingAbility)
		{
			final HashSet<Tile> options = this.state.selected.ability.GetTargetTiles(this.state, this.state.selected);
			for (final Tile tile : options)
			{
				this.drawHighlight(tile.x, tile.y, this.state.selected.ability.abilityColor);
			}
		}
		if (this.state.isInTactics){
			for(Spawn spawn : this.state.GetPlayerSpawns(this.state.battlefieldId)){
				boolean isAvailable = true;
				for(Unit unit : this.state.AllUnits()){
					if(unit.x == spawn.x && unit.y == spawn.y)
						isAvailable = false;
				}
				Color color;
				if(isAvailable){
					color = new Color(.4f, .4f, .7f, .8f);
				}else{
					color = new Color(.4f, .4f, .7f, .3f);
				}			
				this.drawHighlight(spawn.x, spawn.y, color);
			}
		}
	}

	private void drawUnits()
	{
		for (final Unit unit : this.state.AllUnits())
		{
			boolean isFlipped = this.state.victimTile != null && unit.isAttacking && ((unit.team == 0 && unit.x > this.state.victimTile.x) || (unit.team == 1 && unit.x < this.state.victimTile.x));
			UnitRenderer.DrawUnit(this.game, unit, unit.x * this.tileWidth, Gdx.graphics.getHeight() - ((unit.y + 1) * this.tileHeight), this.tileWidth, unit.isAttacking, this.state.IsTapped(unit), isFlipped);
			if(state.selected != null && state.selected.equals(unit)){
				if(unit.team == 0){
					this.game.batcher.draw(blueSelect, this.xOffset + (this.tileWidth * unit.x), Gdx.graphics.getHeight() - (this.tileHeight * (unit.y + 1)), this.tileWidth, this.tileHeight);
				}else{
					this.game.batcher.draw(redSelect, this.xOffset + (this.tileWidth * unit.x), Gdx.graphics.getHeight() - (this.tileHeight * (unit.y + 1)), this.tileWidth, this.tileHeight);					
				}
			}
		}
		for(final Unit unit : this.state.dyingUnits){
			UnitRenderer.DrawUnit(this.game, unit, unit.x * this.tileWidth, Gdx.graphics.getHeight() - ((unit.y + 1) * this.tileHeight), this.tileWidth, unit.isAttacking, this.state.IsTapped(unit), false);
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
		this.state.isPreviewingAttack = false;
		this.state.isPreviewingAbility = false;
		if(this.state.isInTactics){
			List<Spawn> spawns = this.state.GetPlayerSpawns(this.state.battlefieldId);
			Spawn touched = null;
			for(Spawn spawn : spawns){
				if (((spawn.x * this.tileWidth) < x) && (x <= ((spawn.x * this.tileWidth) + this.tileWidth)))
				{
					if (((Gdx.graphics.getHeight() - ((spawn.y + 1) * this.tileHeight)) < y) && (y <= (Gdx.graphics.getHeight() - ((spawn.y) * this.tileHeight)))){
						touched = spawn;
					}
				}
			}		
			if(touched != null){
				if(this.state.selected == null){
					for(Unit unit : this.state.roster){
						if(unit.x == touched.x && unit.y == touched.y){
							this.state.selected = unit;
						}
					}
				}else{
					Unit otherUnit = null;
					for(Unit unit : this.state.roster){
						if(unit.x == touched.x && unit.y == touched.y){
							otherUnit = unit;
						}
					}
					if(otherUnit != null){
						this.state.selected = otherUnit;
					}else{
						if(this.state.selected.team == 0){
							this.state.selected.x = touched.x;
							this.state.selected.y = touched.y;
						}else{
							this.state.selected = null;
						}
					}
				}
			}else{
				this.state.selected = null;
				for (final Unit unit : this.state.AllUnits())
				{
					if (((unit.x * this.tileWidth) < x) && (x <= ((unit.x * this.tileWidth) + this.tileWidth)))
					{
						if (((Gdx.graphics.getHeight() - ((unit.y + 1) * this.tileHeight)) < y) && (y <= (Gdx.graphics.getHeight() - ((unit.y) * this.tileHeight))))
						{
							this.state.selected = unit;
						}
					}
				}
			}
		}else{			
			if ((this.state.selected != null) && this.state.isAttacking)
			{
				final HashSet<Tile> options = CombatHelper.GetAttackOptions(this.state, this.state.selected);
				for (final Tile tile : options)
				{
					if (((tile.x * this.tileWidth) < x) && (x <= ((tile.x * this.tileWidth) + this.tileWidth)))
					{
						if (((Gdx.graphics.getHeight() - ((tile.y + 1) * this.tileHeight)) < y) && (y <= (Gdx.graphics.getHeight() - ((tile.y) * this.tileHeight))))
						{
							for (final Unit enemy : this.state.enemies)
							{
								if ((enemy.x == tile.x) && (enemy.y == tile.y))
								{
									this.state.selected.startAttack();
									this.state.victimTile = this.state.battlefield.get(enemy.y).get(enemy.x);
									if (CombatHelper.Attack(this.state.selected, enemy, this.state.battlefield))
									{
										if(enemy.currentHealth >= 1){
											Unit.hitSound.play(this.game.settings.getFloat("sfxVolume", .5f));
										}else{
											Unit.deathSound.play(this.game.settings.getFloat("sfxVolume", .5f));
										}
										enemy.startDamage();
										if(enemy.checkDeath()){
											enemy.killUnit(this.state.selected, this.state);
											state.SaveGraveyard(enemy);
										}
									} else
									{
										Unit.missSound.play(this.game.settings.getFloat("sfxVolume", .5f));
										enemy.startMiss();
									}
	
									this.state.isAttacking = false;
									this.state.selected.hasAttacked = true;
									this.state.ClearUndos();
									if(this.state.IsTapped(this.state.selected))
										this.state.selected = null;
									return;
								}
							}
						}
					}
				}
			}
	
			if ((this.state.selected != null) && this.state.isUsingAbility)
			{
				final HashSet<Tile> options = this.state.selected.ability.GetTargetTiles(this.state, this.state.selected);
				for (final Tile tile : options)
				{
					if (((tile.x * this.tileWidth) < x) && (x <= ((tile.x * this.tileWidth) + this.tileWidth)))
					{
						if (((Gdx.graphics.getHeight() - ((tile.y + 1) * this.tileHeight)) < y) && (y <= (Gdx.graphics.getHeight() - ((tile.y) * this.tileHeight))))
						{
							if (this.state.selected.ability.Execute(this.state, this.state.selected, tile))
							{
								this.state.victimTile = tile; 
								this.state.selected.ability.PlaySound(this.game.settings.getFloat("sfxVolume", .5f));
								this.state.isUsingAbility = false;
								if(this.state.selected.ability.IsAction())
									this.state.selected.hasAttacked = true;
								this.state.selected.ability.exhausted = true;
								this.state.ClearUndos();
								if(this.state.IsTapped(this.state.selected))
									this.state.selected = null;		
								return;
							} else
							{
								if (!this.state.selected.ability.isMultiInput)
								{
									this.state.isUsingAbility = false;
									if(this.state.IsTapped(this.state.selected))
										this.state.selected = null;
								} else
								{
									return;
								}
							}
						}
					}
				}
			}
	
			for (final Unit unit : this.state.AllUnits())
			{
				if (((unit.x * this.tileWidth) < x) && (x <= ((unit.x * this.tileWidth) + this.tileWidth)))
				{
					if (((Gdx.graphics.getHeight() - ((unit.y + 1) * this.tileHeight)) < y) && (y <= (Gdx.graphics.getHeight() - ((unit.y) * this.tileHeight))))
					{
						if ((this.state.selected != null) && (this.state.selected.ability != null) && (this.state.selected.ability.targets.size() > 0) && !this.state.selected.ability.areTargetsPersistent)
						{
							this.state.selected.ability.targets = new ArrayList<Integer>();
						}
						this.state.selected = unit;
						this.state.isMoving = (!this.state.selected.hasMoved && this.state.selected.team == 0);
						this.state.isAttacking = false;
						this.state.isUsingAbility = false;
						return;
					}
				}
			}
	
			if ((this.state.selected != null) && this.state.isMoving)
			{
				final HashSet<Tile> options = MovementHelper.GetMovementOptions(this.state);
				for (final Tile tile : options)
				{
					if (((tile.x * this.tileWidth) < x) && (x <= ((tile.x * this.tileWidth) + this.tileWidth)))
					{
						if (((Gdx.graphics.getHeight() - ((tile.y + 1) * this.tileHeight)) < y) && (y <= (Gdx.graphics.getHeight() - ((tile.y) * this.tileHeight))))
						{
							this.state.selected.distanceMoved = (Math.abs(this.state.selected.x - tile.x) + Math.abs(this.state.selected.y - tile.y));
							Move oldLocation = new Move();
							oldLocation.oldX = state.selected.x;
							oldLocation.oldY = state.selected.y;
							oldLocation.unitId = this.state.selected.id;
							this.state.selected.x = tile.x;
							this.state.selected.y = tile.y;
							this.state.isMoving = false;
							this.state.selected.hasMoved = true;
							this.state.undos.push(oldLocation);
							return;
						}
					}
				}
			}
	
			if ((this.state.selected != null) && (this.state.selected.ability != null) && (this.state.selected.ability.targets.size() > 0) && !this.state.selected.ability.areTargetsPersistent)
			{
				this.state.selected.ability.targets = new ArrayList<Integer>();
			}
	
			this.state.selected = null;
			this.state.isAttacking = false;
			this.state.isMoving = false;
			this.state.isUsingAbility = false;
		}
	}
}
