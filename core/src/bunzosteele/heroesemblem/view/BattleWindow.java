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
import bunzosteele.heroesemblem.model.Units.Abilities.Ability;

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

	public void draw() throws IOException
	{
		this.drawBattlefield();
		HashSet<Tile> overlap = this.drawHighlights();
		this.drawUnits();
		this.drawForeground(overlap);
		this.drawProjection();
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
	
	private void drawForeground(HashSet<Tile> highlights)
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
					if(highlights.contains(tile) || state.containsUnit(tile)){
						this.game.batcher.setColor(new Color(1f, 1f, 1f, .5f));
					}
					this.game.batcher.draw(foregroundSprite, this.xOffset + (this.tileWidth * tileOffset), Gdx.graphics.getHeight() - (this.tileHeight * rowOffset), this.tileWidth, this.tileHeight);
					game.batcher.setColor(new Color(1f, 1f, 1f, 1f));
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

	public HashSet<Tile> drawHighlights() throws IOException
	{
		HashSet<Tile> highlightedTiles = new HashSet<Tile>();
		if(this.state.selected != null && this.state.selected.team != 0 && this.state.currentPlayer == 0){
			final HashSet<Tile> options = MovementHelper.GetMovementOptions(this.state, this.state.selected);		
			for (final Tile tile : options)
			{
				this.game.batcher.draw(game.sprites.blueTile, (tile.x * this.tileWidth) + this.xOffset, Gdx.graphics.getHeight() - (this.tileHeight * (tile.y + 1)), this.tileWidth, this.tileHeight);
				highlightedTiles.add(tile);
			}
		}
		if (this.state.isUsingAbility)
		{
			final HashSet<Tile> options = this.state.selected.ability.GetTargetTiles(this.state, this.state.selected);
			for (final Tile tile : options)
			{
				this.game.batcher.draw(GetAbilityTile(this.state.selected.ability), (tile.x * this.tileWidth) + this.xOffset, Gdx.graphics.getHeight() - (this.tileHeight * (tile.y + 1)), this.tileWidth, this.tileHeight);
				highlightedTiles.add(tile);
			}
		}
		if (this.state.isMoving && ! this.state.isUsingAbility)
		{
			final HashSet<Tile> options = MovementHelper.GetMovementOptions(this.state);
			for (final Tile tile : options)
			{
				if(highlightedTiles.contains(tile)){
					this.game.batcher.setColor(new Color(1f, 1f, 1f, .25f));
				}
				this.game.batcher.draw(game.sprites.blueTile, (tile.x * this.tileWidth) + this.xOffset, Gdx.graphics.getHeight() - (this.tileHeight * (tile.y + 1)), this.tileWidth, this.tileHeight);
				highlightedTiles.add(tile);
				this.game.batcher.setColor(new Color(1f, 1f, 1f, 1f));
			}
		}
		if (this.state.CanAttack(this.state.selected) && ! this.state.isUsingAbility && this.state.isMoving)
		{
			final HashSet<Tile> options = CombatHelper.GetAttackOptions(this.state, this.state.selected, true);
			for (final Tile tile : options)
			{
				if(!highlightedTiles.contains(tile) && state.containsEnemy(tile)){
					this.game.batcher.draw(game.sprites.redTile, (tile.x * this.tileWidth) + this.xOffset, Gdx.graphics.getHeight() - (this.tileHeight * (tile.y + 1)), this.tileWidth, this.tileHeight);	
					highlightedTiles.add(tile);
				}
			}
		}
		if (this.state.selected != null && !this.state.isMoving && !this.state.isUsingAbility && !this.state.selected.hasAttacked && this.state.selected.team ==0)
		{
			final HashSet<Tile> options = CombatHelper.GetAttackOptions(this.state, this.state.selected, true);
			for (final Tile tile : options)
			{
				this.game.batcher.draw(game.sprites.redTile, (tile.x * this.tileWidth) + this.xOffset, Gdx.graphics.getHeight() - (this.tileHeight * (tile.y + 1)), this.tileWidth, this.tileHeight);	
				highlightedTiles.add(tile);
			}
		}
		if (this.state.isInTactics){
			for(Spawn spawn : this.state.GetPlayerSpawns(this.state.battlefieldId)){
				boolean isAvailable = true;
				for(Unit unit : this.state.AllUnits()){
					if(unit.x == spawn.x && unit.y == spawn.y)
						isAvailable = false;
				}	
				this.game.batcher.draw(game.sprites.blueTile, (spawn.x * this.tileWidth) + this.xOffset, Gdx.graphics.getHeight() - (this.tileHeight * (spawn.y + 1)), this.tileWidth, this.tileHeight);
			}
		}
		
		return highlightedTiles;
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
			
			this.game.batcher.draw(game.sprites.healthBarBackground, this.xOffset + (this.tileWidth * unit.x) + (this.tileWidth * 14 / 50), Gdx.graphics.getHeight() - (this.tileHeight * (unit.y + 1)), this.tileWidth * 22 / 50, this.tileHeight * 4 / 50);	
			int healthPercent = (int) ((unit.currentHealth / (double) unit.maximumHealth) * 10);
			healthPercent = healthPercent == 0 ? 1 : healthPercent;
			int healthOffset = this.xOffset + (this.tileWidth * unit.x) + this.tileWidth * 15 / 50;
			for(int i = 1; i <= healthPercent; i++){
				AtlasRegion healthBarRegion = this.game.textureAtlas.findRegion("HealthBar" + i);
				Sprite healthBarSprite = new Sprite(healthBarRegion);
				this.game.batcher.draw(healthBarSprite, healthOffset, Gdx.graphics.getHeight() - (this.tileHeight * (unit.y + 1)) + (this.tileHeight / 50), this.tileWidth * 2 / 50, this.tileHeight * 2 / 50);	
				healthOffset += this.tileWidth * 2 / 50;
			}
		
			
		}
		for(final Unit unit : this.state.dyingUnits){
			UnitRenderer.DrawUnit(this.game, unit, unit.x * this.tileWidth, Gdx.graphics.getHeight() - ((unit.y + 1) * this.tileHeight), this.tileWidth, unit.isAttacking, this.state.IsTapped(unit), false);
		}
	}
	
	private void drawProjection(){
		if(state.selected != null && state.CanAttack(state.selected)){
			final HashSet<Tile> options = CombatHelper.GetAttackOptions(this.state, this.state.selected, false);
			for(Tile option : options){
				for(Unit enemy : state.enemies){
					if(option.x == enemy.x && option.y == enemy.y && (state.targeted == null || state.targeted != enemy)){
						this.game.batcher.draw(game.sprites.crosshair, (option.x * this.tileWidth) + this.xOffset + (tileWidth / 4), Gdx.graphics.getHeight() - (this.tileHeight * (option.y + 1)) + (tileWidth / 4), this.tileWidth /2, this.tileHeight/2);
					}
				}
			}
			if(state.targeted != null){
				double projectionHeightRatio = .75;
				this.game.batcher.draw(game.sprites.projectionBackground, (state.targeted.x * this.tileWidth) + this.xOffset, Gdx.graphics.getHeight() - (this.tileHeight * (state.targeted.y + 1)) + (tileWidth / 8), this.tileWidth, (int) (this.tileHeight * projectionHeightRatio));
				this.game.batcher.draw(game.sprites.projectionBorder, (state.targeted.x * this.tileWidth) + this.xOffset, Gdx.graphics.getHeight() - (this.tileHeight * (state.targeted.y + 1)) + (tileWidth / 8), this.tileWidth, (int) (this.tileHeight * projectionHeightRatio));
				Tile enemyTile = state.battlefield.get(state.targeted.y).get(state.targeted.x);
				int expectedDamage = CombatHelper.GetExpectedDamage(state.selected, state.targeted, enemyTile);
				int hitChance = CombatHelper.GetHitPercent(state.selected, state.targeted, enemyTile);
				int critChance = CombatHelper.GetCritPercent(state.selected, state.targeted, enemyTile);
				String damageString = (expectedDamage > 0 ? ((expectedDamage - 1) + "-" + (expectedDamage + 1))  : "0-" + (expectedDamage + 1));
				String hitString = (hitChance > 0 ? (hitChance > 100 ? "100" : hitChance) : "0") + "%";
				String critString = (critChance > 0 ? (critChance > 100 ? "100" : critChance) : "0") + "%";
				game.projectionFont.setColor(new Color(0f, 0f, 0f, 1f));
				float borderOffset = .1363f;
				
				game.projectionFont.draw(game.batcher, "DMG", (state.targeted.x * this.tileWidth) + this.xOffset  + (int)(borderOffset * tileWidth), Gdx.graphics.getHeight() - (this.tileHeight * (state.targeted.y + 1)) + (tileWidth / 8) + this.game.projectionFont.getLineHeight() * 3 + (int)(borderOffset * (tileWidth * .75)), (tileWidth * 32 / 44), -1, false);
				game.projectionFont.draw(game.batcher, damageString, ((state.targeted.x + 1) * this.tileWidth) + this.xOffset - (int)(borderOffset * tileWidth) - (tileWidth * 32 / 44), Gdx.graphics.getHeight() - (this.tileHeight * (state.targeted.y + 1)) + (tileWidth / 8) + this.game.projectionFont.getLineHeight() * 3 + (int)(borderOffset * (tileWidth * .75)), (tileWidth * 32 / 44), 0, false);
	
				game.projectionFont.draw(game.batcher, "HIT", (state.targeted.x * this.tileWidth) + this.xOffset + (int)(borderOffset * tileWidth), Gdx.graphics.getHeight() - (this.tileHeight * (state.targeted.y + 1)) + (tileWidth / 8) + this.game.projectionFont.getLineHeight() * 2 + (int)(borderOffset * (tileWidth * .75)), (tileWidth * 32 / 44), -1, false);
				game.projectionFont.draw(game.batcher, hitString, ((state.targeted.x + 1) * this.tileWidth) + this.xOffset  - (int)(borderOffset * tileWidth) - (tileWidth * 32 / 44), Gdx.graphics.getHeight() - (this.tileHeight * (state.targeted.y + 1)) + (tileWidth / 8) + this.game.projectionFont.getLineHeight() * 2 + (int)(borderOffset * (tileWidth * .75)), (tileWidth * 32 / 44), 0, false);
				
				game.projectionFont.draw(game.batcher, "CRT", (state.targeted.x * this.tileWidth) + this.xOffset + (int)(borderOffset * tileWidth), Gdx.graphics.getHeight() - (this.tileHeight * (state.targeted.y + 1)) + (tileWidth / 8) + this.game.projectionFont.getLineHeight() + (int)(borderOffset * (tileWidth * .75)), (tileWidth * 32 / 44), -1, false);
				game.projectionFont.draw(game.batcher, critString, ((state.targeted.x + 1) * this.tileWidth) + this.xOffset - (int)(borderOffset * tileWidth) - (tileWidth * 32 / 44), Gdx.graphics.getHeight() - (this.tileHeight * (state.targeted.y + 1)) + (tileWidth / 8) + this.game.projectionFont.getLineHeight() + (int)(borderOffset * (tileWidth * .75)), (tileWidth * 32 / 44), 0, false);
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
		if(this.state.isInTactics){
			ProcessTacticsTouch(x, y);
			return;
		}		
		if ((this.state.selected != null) && !this.state.selected.hasAttacked && this.state.selected.team == 0)
			if(ProcessAttackTouch(x, y)) return;	

		if ((this.state.selected != null) && this.state.isUsingAbility)
			if(ProcessAbilityTouch(x, y)) return;	

		if(ProcessUnitTouch(x, y)) return;

		if ((this.state.selected != null) && this.state.isMoving)
			if (ProcessMoveTouch(x, y)) return;

		if ((this.state.selected != null) && (this.state.selected.ability != null) && (this.state.selected.ability.targets.size() > 0) && !this.state.selected.ability.areTargetsPersistent)
		{
			this.state.selected.ability.targets = new ArrayList<Integer>();
		}

		this.state.selected = null;
		this.state.targeted = null;
		this.state.isMoving = false;
		this.state.isUsingAbility = false;
	}
	
	private Sprite GetAbilityTile(Ability ability){
		if(ability.displayName.equals("Teleport")){
			return game.sprites.purpleTile;
		}else if(ability.displayName.equals("Scholar")){
			return game.sprites.goldTile;
		}else if(ability.displayName.equals("Vault")){
			return game.sprites.blueTile;
		}else if(ability.displayName.equals("Heal") || ability.displayName.equals("Rebirth")){
			return game.sprites.greenTile;
		}else{
			return game.sprites.redTile;
		}
	}
	
	private void ProcessTacticsTouch(float x, float y) throws IOException{
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
	}
	
	private boolean ProcessAttackTouch(float x, float y){
		final HashSet<Tile> options = CombatHelper.GetAttackOptions(this.state, this.state.selected, false);
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
							if(this.state.targeted != null && this.state.targeted == enemy){
								this.state.victimTile = tile; 
								this.state.ConfirmAttack();
								return true;
							}
							this.state.targeted = enemy;
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean ProcessAbilityTouch(float x, float y){
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
						this.state.targeted = null;
						if(!this.state.selected.hasMoved)
							this.state.isMoving = true;
						return true;
					} else
					{
						if (!this.state.selected.ability.isMultiInput)
						{
							this.state.isUsingAbility = false;
							this.state.targeted = null;
						} else
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean ProcessUnitTouch(float x, float y){
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
					this.state.targeted = null;
					this.state.isMoving = (!this.state.selected.hasMoved && this.state.selected.team == 0);
					this.state.isUsingAbility = false;
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean ProcessMoveTouch(float x, float y){
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
					this.state.targeted = null;
					this.state.undos.push(oldLocation);
					return true;
				}
			}
		}
		return false;
	}
}
