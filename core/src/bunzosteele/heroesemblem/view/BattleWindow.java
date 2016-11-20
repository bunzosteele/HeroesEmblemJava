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

	public BattleWindow(final HeroesEmblem game, final BattleState state, final int width, final int height, final int yOffset)
	{
		this.game = game;
		this.state = state;
		xOffset = 0;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		tileWidth = width / 13;
		tileHeight = height / 9;
	}

	public void draw() throws IOException
	{
		drawBattlefield();
		HashSet<Tile> overlap = drawHighlights();
		drawUnits();
		drawForeground(overlap);
		drawProjection();
	}

	private void drawBattlefield()
	{
		int rowOffset = 1;
		for (final List<Tile> row : state.battlefield)
		{
			int tileOffset = 0;
			for (final Tile tile : row)
			{
				final AtlasRegion tileRegion = game.textureAtlas.findRegion(tile.type);
				final Sprite tileSprite = new Sprite(tileRegion);
				game.batcher.draw(tileSprite, xOffset + (tileWidth * tileOffset), Gdx.graphics.getHeight() - (tileHeight * rowOffset), tileWidth, tileHeight);
				tileOffset++;
			}
			rowOffset++;
		}
	}
	
	private void drawForeground(HashSet<Tile> highlights)
	{
		int rowOffset = 1;
		for (final List<Tile> row : state.battlefield)
		{
			int tileOffset = 0;
			for (final Tile tile : row)
			{
				if(tile.foreground != null){
					final AtlasRegion foregroundRegion = game.textureAtlas.findRegion(tile.foreground);
					final Sprite foregroundSprite = new Sprite(foregroundRegion);
					if(highlights.contains(tile) || state.containsUnit(tile)){
						game.batcher.setColor(new Color(1f, 1f, 1f, .5f));
					}
					game.batcher.draw(foregroundSprite, xOffset + (tileWidth * tileOffset), Gdx.graphics.getHeight() - (tileHeight * rowOffset), tileWidth, tileHeight);
					game.batcher.setColor(new Color(1f, 1f, 1f, 1f));
				}
				tileOffset++;
			}
			rowOffset++;
		}
		
		for (final Unit unit : state.AllUnits())
		{
			if(state.selected != null && state.selected.equals(unit)){
				if(unit.team == 0){
					game.batcher.draw(game.sprites.BlueSelect, xOffset + (tileWidth * unit.x), Gdx.graphics.getHeight() - (tileHeight * (unit.y + 1)), tileWidth, tileHeight);
				}else{
					game.batcher.draw(game.sprites.RedSelect, xOffset + (tileWidth * unit.x), Gdx.graphics.getHeight() - (tileHeight * (unit.y + 1)), tileWidth, tileHeight);					
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

			game.shapeRenderer.setColor(color);
			game.shapeRenderer.rect((unit.x * tileWidth) + xOffset + (tileWidth * .21875f), Gdx.graphics.getHeight() - (tileHeight * (unit.y + 1)), (tileWidth - (tileWidth * .4375f)) * healthPercent, tileHeight / 10);
		}
	}

	public void drawHealthBars()
	{
		for (final Unit unit : state.AllUnits())
		{
			drawHealthBar(unit);
		}
	}

	public HashSet<Tile> drawHighlights() throws IOException
	{
		HashSet<Tile> highlightedTiles = new HashSet<Tile>();
		if(state.selected != null && state.selected.team != 0 && state.currentPlayer == 0){
			final HashSet<Tile> options = MovementHelper.GetMovementOptions(state, state.selected);		
			for (final Tile tile : options)
			{
				game.batcher.draw(game.sprites.BlueTile, (tile.x * tileWidth) + xOffset, Gdx.graphics.getHeight() - (tileHeight * (tile.y + 1)), tileWidth, tileHeight);
				highlightedTiles.add(tile);
			}
		}
		if (state.isUsingAbility)
		{
			final HashSet<Tile> options = state.selected.ability.GetTargetTiles(state, state.selected);
			for (final Tile tile : options)
			{
				game.batcher.draw(GetAbilityTile(state.selected.ability), (tile.x * tileWidth) + xOffset, Gdx.graphics.getHeight() - (tileHeight * (tile.y + 1)), tileWidth, tileHeight);
				highlightedTiles.add(tile);
			}
		}
		if (state.isMoving && ! state.isUsingAbility)
		{
			final HashSet<Tile> options = MovementHelper.GetMovementOptions(state);
			for (final Tile tile : options)
			{
				if(highlightedTiles.contains(tile)){
					game.batcher.setColor(new Color(1f, 1f, 1f, .25f));
				}
				game.batcher.draw(game.sprites.BlueTile, (tile.x * tileWidth) + xOffset, Gdx.graphics.getHeight() - (tileHeight * (tile.y + 1)), tileWidth, tileHeight);
				highlightedTiles.add(tile);
				game.batcher.setColor(new Color(1f, 1f, 1f, 1f));
			}
		}
		if (state.CanAttack(state.selected) && ! state.isUsingAbility && state.isMoving)
		{
			final HashSet<Tile> options = CombatHelper.GetAttackOptions(state, state.selected, true);
			for (final Tile tile : options)
			{
				if(!highlightedTiles.contains(tile) && state.containsEnemy(tile)){
					game.batcher.draw(game.sprites.RedTile, (tile.x * tileWidth) + xOffset, Gdx.graphics.getHeight() - (tileHeight * (tile.y + 1)), tileWidth, tileHeight);	
					highlightedTiles.add(tile);
				}
			}
		}
		if (state.selected != null && !state.isMoving && !state.isUsingAbility && !state.selected.hasAttacked && state.selected.team ==0)
		{
			final HashSet<Tile> options = CombatHelper.GetAttackOptions(state, state.selected, true);
			for (final Tile tile : options)
			{
				game.batcher.draw(game.sprites.RedTile, (tile.x * tileWidth) + xOffset, Gdx.graphics.getHeight() - (tileHeight * (tile.y + 1)), tileWidth, tileHeight);	
				highlightedTiles.add(tile);
			}
		}
		if (state.isInTactics){
			for(Spawn spawn : state.GetPlayerSpawns(state.battlefieldId)){
				boolean isAvailable = true;
				for(Unit unit : state.AllUnits()){
					if(unit.x == spawn.x && unit.y == spawn.y)
						isAvailable = false;
				}	
				game.batcher.draw(game.sprites.BlueTile, (spawn.x * tileWidth) + xOffset, Gdx.graphics.getHeight() - (tileHeight * (spawn.y + 1)), tileWidth, tileHeight);
			}
		}
		
		return highlightedTiles;
	}

	private void drawUnits()
	{
		for (final Unit unit : state.AllUnits())
		{
			boolean isFlipped = state.victimTile != null && unit.isAttacking && ((unit.team == 0 && unit.x > state.victimTile.x) || (unit.team == 1 && unit.x < state.victimTile.x));
			UnitRenderer.DrawUnit(game, unit, unit.x * tileWidth, Gdx.graphics.getHeight() - ((unit.y + 1) * tileHeight), tileWidth, unit.isAttacking, state.IsTapped(unit), isFlipped);
			if(state.selected != null && state.selected.equals(unit)){
				if(unit.team == 0){
					game.batcher.draw(game.sprites.BlueSelect, xOffset + (tileWidth * unit.x), Gdx.graphics.getHeight() - (tileHeight * (unit.y + 1)), tileWidth, tileHeight);
				}else{
					game.batcher.draw(game.sprites.RedSelect, xOffset + (tileWidth * unit.x), Gdx.graphics.getHeight() - (tileHeight * (unit.y + 1)), tileWidth, tileHeight);					
				}
			}
			
			game.batcher.draw(game.sprites.HealthBarBackground, xOffset + (tileWidth * unit.x) + (tileWidth * 14 / 50), Gdx.graphics.getHeight() - (tileHeight * (unit.y + 1)), tileWidth * 22 / 50, tileHeight * 4 / 50);	
			int healthPercent = (int) ((unit.currentHealth / (double) unit.maximumHealth) * 10);
			healthPercent = healthPercent == 0 ? 1 : healthPercent;
			int healthOffset = xOffset + (tileWidth * unit.x) + tileWidth * 15 / 50;
			for(int i = 1; i <= healthPercent; i++){
				AtlasRegion healthBarRegion = game.textureAtlas.findRegion("HealthBar" + i);
				Sprite healthBarSprite = new Sprite(healthBarRegion);
				game.batcher.draw(healthBarSprite, healthOffset, Gdx.graphics.getHeight() - (tileHeight * (unit.y + 1)) + (tileHeight / 50), tileWidth * 2 / 50, tileHeight * 2 / 50);	
				healthOffset += tileWidth * 2 / 50;
			}
		
			
		}
		for(final Unit unit : state.dyingUnits){
			UnitRenderer.DrawUnit(game, unit, unit.x * tileWidth, Gdx.graphics.getHeight() - ((unit.y + 1) * tileHeight), tileWidth, unit.isAttacking, state.IsTapped(unit), false);
		}
	}
	
	private void drawProjection(){
		if(state.selected != null && state.CanAttack(state.selected)){
			final HashSet<Tile> options = CombatHelper.GetAttackOptions(state, state.selected, false);
			for(Tile option : options){
				for(Unit enemy : state.enemies){
					if(option.x == enemy.x && option.y == enemy.y && (state.targeted == null || state.targeted != enemy)){
						game.batcher.draw(game.sprites.Crosshair, (option.x * tileWidth) + xOffset + (tileWidth / 4), Gdx.graphics.getHeight() - (tileHeight * (option.y + 1)) + (tileWidth / 4), tileWidth /2, tileHeight/2);
					}
				}
			}
			if(state.targeted != null){
				double projectionHeightRatio = .75;
				game.batcher.draw(game.sprites.ProjectionBackground, (state.targeted.x * tileWidth) + xOffset, Gdx.graphics.getHeight() - (tileHeight * (state.targeted.y + 1)) + (tileWidth / 8), tileWidth, (int) (tileHeight * projectionHeightRatio));
				game.batcher.draw(game.sprites.ProjectionBorder, (state.targeted.x * tileWidth) + xOffset, Gdx.graphics.getHeight() - (tileHeight * (state.targeted.y + 1)) + (tileWidth / 8), tileWidth, (int) (tileHeight * projectionHeightRatio));
				Tile enemyTile = state.battlefield.get(state.targeted.y).get(state.targeted.x);
				int expectedDamage = CombatHelper.GetExpectedDamage(state.selected, state.targeted, enemyTile);
				int hitChance = CombatHelper.GetHitPercent(state.selected, state.targeted, enemyTile);
				int critChance = CombatHelper.GetCritPercent(state.selected, state.targeted, enemyTile);
				String damageString = (expectedDamage > 0 ? ((expectedDamage - 1) + "-" + (expectedDamage + 1))  : "0-" + (expectedDamage + 1));
				String hitString = (hitChance > 0 ? (hitChance > 100 ? "100" : hitChance) : "0") + "%";
				String critString = (critChance > 0 ? (critChance > 100 ? "100" : critChance) : "0") + "%";
				game.projectionFont.setColor(new Color(0f, 0f, 0f, 1f));
				float borderOffset = .1363f;
				
				game.projectionFont.draw(game.batcher, "DMG", (state.targeted.x * tileWidth) + xOffset  + (int)(borderOffset * tileWidth), Gdx.graphics.getHeight() - (tileHeight * (state.targeted.y + 1)) + (tileWidth / 8) + game.projectionFont.getLineHeight() * 3 + (int)(borderOffset * (tileWidth * .75)), (tileWidth * 32 / 44), -1, false);
				game.projectionFont.draw(game.batcher, damageString, ((state.targeted.x + 1) * tileWidth) + xOffset - (int)(borderOffset * tileWidth) - (tileWidth * 32 / 44), Gdx.graphics.getHeight() - (tileHeight * (state.targeted.y + 1)) + (tileWidth / 8) + game.projectionFont.getLineHeight() * 3 + (int)(borderOffset * (tileWidth * .75)), (tileWidth * 32 / 44), 0, false);
	
				game.projectionFont.draw(game.batcher, "HIT", (state.targeted.x * tileWidth) + xOffset + (int)(borderOffset * tileWidth), Gdx.graphics.getHeight() - (tileHeight * (state.targeted.y + 1)) + (tileWidth / 8) + game.projectionFont.getLineHeight() * 2 + (int)(borderOffset * (tileWidth * .75)), (tileWidth * 32 / 44), -1, false);
				game.projectionFont.draw(game.batcher, hitString, ((state.targeted.x + 1) * tileWidth) + xOffset  - (int)(borderOffset * tileWidth) - (tileWidth * 32 / 44), Gdx.graphics.getHeight() - (tileHeight * (state.targeted.y + 1)) + (tileWidth / 8) + game.projectionFont.getLineHeight() * 2 + (int)(borderOffset * (tileWidth * .75)), (tileWidth * 32 / 44), 0, false);
				
				game.projectionFont.draw(game.batcher, "CRT", (state.targeted.x * tileWidth) + xOffset + (int)(borderOffset * tileWidth), Gdx.graphics.getHeight() - (tileHeight * (state.targeted.y + 1)) + (tileWidth / 8) + game.projectionFont.getLineHeight() + (int)(borderOffset * (tileWidth * .75)), (tileWidth * 32 / 44), -1, false);
				game.projectionFont.draw(game.batcher, critString, ((state.targeted.x + 1) * tileWidth) + xOffset - (int)(borderOffset * tileWidth) - (tileWidth * 32 / 44), Gdx.graphics.getHeight() - (tileHeight * (state.targeted.y + 1)) + (tileWidth / 8) + game.projectionFont.getLineHeight() + (int)(borderOffset * (tileWidth * .75)), (tileWidth * 32 / 44), 0, false);
			}
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

	public void processTouch(final float x, final float y) throws IOException
	{
		if(state.isInTactics){
			ProcessTacticsTouch(x, y);
			return;
		}		
		if ((state.selected != null) && !state.selected.hasAttacked && state.selected.team == 0)
			if(ProcessAttackTouch(x, y)) return;	

		if ((state.selected != null) && state.isUsingAbility)
			if(ProcessAbilityTouch(x, y)) return;	

		if(ProcessUnitTouch(x, y)) return;

		if ((state.selected != null) && state.isMoving)
			if (ProcessMoveTouch(x, y)) return;

		if ((state.selected != null) && (state.selected.ability != null) && (state.selected.ability.targets.size() > 0) && !state.selected.ability.areTargetsPersistent)
		{
			state.selected.ability.targets = new ArrayList<Integer>();
		}

		state.selected = null;
		state.targeted = null;
		state.isMoving = false;
		state.isUsingAbility = false;
	}
	
	private Sprite GetAbilityTile(Ability ability){
		if(ability.displayName.equals("Teleport")){
			return game.sprites.PurpleTile;
		}else if(ability.displayName.equals("Scholar")){
			return game.sprites.GoldTile;
		}else if(ability.displayName.equals("Vault")){
			return game.sprites.BlueTile;
		}else if(ability.displayName.equals("Heal") || ability.displayName.equals("Rebirth")){
			return game.sprites.GreenTile;
		}else{
			return game.sprites.RedTile;
		}
	}
	
	private void ProcessTacticsTouch(float x, float y) throws IOException{
		List<Spawn> spawns = state.GetPlayerSpawns(state.battlefieldId);
		Spawn touched = null;
		for(Spawn spawn : spawns){
			if (((spawn.x * tileWidth) < x) && (x <= ((spawn.x * tileWidth) + tileWidth)))
			{
				if (((Gdx.graphics.getHeight() - ((spawn.y + 1) * tileHeight)) < y) && (y <= (Gdx.graphics.getHeight() - ((spawn.y) * tileHeight)))){
					touched = spawn;
				}
			}
		}		
		if(touched != null){
			if(state.selected == null){
				for(Unit unit : state.roster){
					if(unit.x == touched.x && unit.y == touched.y){
						state.selected = unit;
					}
				}
			}else{
				Unit otherUnit = null;
				for(Unit unit : state.roster){
					if(unit.x == touched.x && unit.y == touched.y){
						otherUnit = unit;
					}
				}
				if(otherUnit != null){
					state.selected = otherUnit;
				}else{
					if(state.selected.team == 0){
						state.selected.x = touched.x;
						state.selected.y = touched.y;
					}else{
						state.selected = null;
					}
				}
			}
		}else{
			state.selected = null;
			for (final Unit unit : state.AllUnits())
			{
				if (((unit.x * tileWidth) < x) && (x <= ((unit.x * tileWidth) + tileWidth)))
				{
					if (((Gdx.graphics.getHeight() - ((unit.y + 1) * tileHeight)) < y) && (y <= (Gdx.graphics.getHeight() - ((unit.y) * tileHeight))))
					{
						state.selected = unit;
					}
				}
			}
		}
	}
	
	private boolean ProcessAttackTouch(float x, float y){
		final HashSet<Tile> options = CombatHelper.GetAttackOptions(state, state.selected, false);
		for (final Tile tile : options)
		{
			if (((tile.x * tileWidth) < x) && (x <= ((tile.x * tileWidth) + tileWidth)))
			{
				if (((Gdx.graphics.getHeight() - ((tile.y + 1) * tileHeight)) < y) && (y <= (Gdx.graphics.getHeight() - ((tile.y) * tileHeight))))
				{
					for (final Unit enemy : state.enemies)
					{
						if ((enemy.x == tile.x) && (enemy.y == tile.y))
						{
							if(state.targeted != null && state.targeted == enemy){
								state.victimTile = tile; 
								state.ConfirmAttack();
								return true;
							}
							state.targeted = enemy;
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean ProcessAbilityTouch(float x, float y){
		final HashSet<Tile> options = state.selected.ability.GetTargetTiles(state, state.selected);
		for (final Tile tile : options)
		{
			if (((tile.x * tileWidth) < x) && (x <= ((tile.x * tileWidth) + tileWidth)))
			{
				if (((Gdx.graphics.getHeight() - ((tile.y + 1) * tileHeight)) < y) && (y <= (Gdx.graphics.getHeight() - ((tile.y) * tileHeight))))
				{
					if (state.selected.ability.Execute(state, state.selected, tile))
					{
						state.victimTile = tile; 
						state.selected.ability.PlaySound(game.settings.getFloat("sfxVolume", .5f));
						state.isUsingAbility = false;
						if(state.selected.ability.IsAction())
							state.selected.hasAttacked = true;
						state.selected.ability.exhausted = true;
						state.ClearUndos();
						state.targeted = null;
						if(!state.selected.hasMoved)
							state.isMoving = true;
						return true;
					} else
					{
						if (!state.selected.ability.isMultiInput)
						{
							state.isUsingAbility = false;
							state.targeted = null;
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
		for (final Unit unit : state.AllUnits())
		{
			if (((unit.x * tileWidth) < x) && (x <= ((unit.x * tileWidth) + tileWidth)))
			{
				if (((Gdx.graphics.getHeight() - ((unit.y + 1) * tileHeight)) < y) && (y <= (Gdx.graphics.getHeight() - ((unit.y) * tileHeight))))
				{
					if ((state.selected != null) && (state.selected.ability != null) && (state.selected.ability.targets.size() > 0) && !state.selected.ability.areTargetsPersistent)
					{
						state.selected.ability.targets = new ArrayList<Integer>();
					}
					state.selected = unit;
					state.targeted = null;
					state.isMoving = (!state.selected.hasMoved && state.selected.team == 0);
					state.isUsingAbility = false;
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean ProcessMoveTouch(float x, float y){
		final HashSet<Tile> options = MovementHelper.GetMovementOptions(state);
		for (final Tile tile : options)
		{
			if (((tile.x * tileWidth) < x) && (x <= ((tile.x * tileWidth) + tileWidth)))
			{
				if (((Gdx.graphics.getHeight() - ((tile.y + 1) * tileHeight)) < y) && (y <= (Gdx.graphics.getHeight() - ((tile.y) * tileHeight))))
				{
					state.selected.distanceMoved = (Math.abs(state.selected.x - tile.x) + Math.abs(state.selected.y - tile.y));
					Move oldLocation = new Move();
					oldLocation.oldX = state.selected.x;
					oldLocation.oldY = state.selected.y;
					oldLocation.unitId = state.selected.id;
					state.selected.x = tile.x;
					state.selected.y = tile.y;
					state.isMoving = false;
					state.selected.hasMoved = true;
					state.targeted = null;
					state.undos.push(oldLocation);
					return true;
				}
			}
		}
		return false;
	}
}
