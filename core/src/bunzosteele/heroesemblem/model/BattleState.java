package bunzosteele.heroesemblem.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bunzosteele.heroesemblem.model.Battlefield.BattlefieldGenerator;
import bunzosteele.heroesemblem.model.Battlefield.Spawn;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitGenerator;

public class BattleState
{
	public List<Unit> enemies;
	public List<Unit> roster;
	public Unit selected;
	public int gold;
	public int roundsSurvived;
	public int difficulty;
	public List<List<Tile>> battlefield;
	public boolean isMoving;
	public boolean isAttacking;
	public boolean isUsingAbility;
	public int currentPlayer;
	public int turnCount;

	public BattleState(final ShopState shopState) throws IOException
	{
		this.turnCount = 1;
		this.currentPlayer = 0;
		this.roundsSurvived = shopState.roundsSurvived;
		this.difficulty = (int) Math.pow(2, this.roundsSurvived);
		this.roster = shopState.roster;
		this.selected = null;
		this.gold = shopState.gold;
		final Random random = new Random();
		final int battlefieldId = random.nextInt(this.difficulty + 6) - 6;
		this.battlefield = BattlefieldGenerator.GenerateBattlefield(battlefieldId);
		final List<Spawn> spawns = BattlefieldGenerator.GenerateSpawns(battlefieldId);
		final List<Spawn> playerSpawns = new ArrayList<Spawn>();
		final List<Spawn> enemySpawns = new ArrayList<Spawn>();
		for (final Spawn spawn : spawns)
		{
			if (spawn.isPlayer)
			{
				playerSpawns.add(spawn);
			} else
			{
				enemySpawns.add(spawn);
			}
		}
		this.enemies = UnitGenerator.GenerateEnemies(enemySpawns.size(), this.difficulty - battlefieldId);
		this.SpawnUnits(this.roster, playerSpawns);
		this.SpawnUnits(this.enemies, enemySpawns);
	}

	public List<Unit> AllUnits()
	{
		final List<Unit> allUnits = new ArrayList<Unit>();
		for (final Unit unit : this.roster)
		{
			allUnits.add(unit);
		}
		for (final Unit unit : this.enemies)
		{
			allUnits.add(unit);
		}
		return allUnits;
	}

	public boolean CanAttack(final Unit unit)
	{
		if ((unit != null) && (unit.team == this.currentPlayer))
		{
			if (!unit.hasAttacked)
			{
				for (final Tile tile : CombatHelper.GetAttackOptions(this, unit))
				{
					for (final Unit enemy : this.enemies)
					{
						if ((enemy.x == tile.x) && (enemy.y == tile.y))
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public boolean CanMove()
	{
		if ((this.selected != null) && (this.selected.team == this.currentPlayer))
		{
			if (!this.selected.hasMoved)
			{
				return true;
			}
		}
		return false;
	}

	public boolean CanUseAbility(final Unit unit)
	{
		if ((unit != null) && (unit.ability != null) && (unit.team == this.currentPlayer))
		{
			return unit.ability.CanUse(this, unit);
		} else
		{
			return false;
		}
	}

	public void CleanBoard()
	{
		for (int i = this.roster.size() - 1; i >= 0; i--)
		{
			if (this.roster.get(i).isDying)
			{
				if (this.roster.get(i).deathFrame < 1)
				{
					this.roster.remove(i);
				}
			}
		}
		for (int i = this.enemies.size() - 1; i >= 0; i--)
		{
			if (this.enemies.get(i).isDying)
			{
				if (this.enemies.get(i).deathFrame < 1)
				{
					this.enemies.remove(i);
				}
			}
		}
	}

	public List<Unit> CurrentPlayerUnits()
	{
		if (this.currentPlayer == 0)
		{
			return this.roster;
		} else
		{
			return this.enemies;
		}
	}

	public void EndBattle()
	{
		for (final Unit unit : this.AllUnits())
		{
			if (unit.ability != null)
			{
				unit.ability.exhausted = false;
				unit.ability.targets = new ArrayList<Unit>();
			}
			unit.isAttacking = false;
			unit.isDying = false;
			unit.isGettingExperience = false;
			unit.isHealing = false;
			unit.isMissed = false;
			unit.isTakingDamage = false;
			unit.experienceFrame = 1;
			unit.damageFrame = 1;
			unit.healFrame = 1;
			unit.attackFrame = 1;
			unit.missedFrame = 1;
			unit.deathFrame = 10;
			unit.distanceMoved = 0;
			unit.hasAttacked = false;
			unit.hasMoved = false;
			unit.damageDisplay = "";
		}
		this.selected = null;
		this.isAttacking = false;
		this.isMoving = false;
		this.isUsingAbility = false;
	}

	public void EndTurn()
	{
		this.isAttacking = false;
		this.isMoving = false;
		this.isUsingAbility = false;
		this.selected = null;
		this.currentPlayer = (this.currentPlayer + 1) % 2;
		if (this.currentPlayer == 0)
		{
			this.turnCount++;
		}
		for (final Unit unit : this.CurrentPlayerUnits())
		{
			unit.hasMoved = false;
			unit.hasAttacked = false;
			unit.distanceMoved = 0;
		}
	}

	public boolean IsTapped(final Unit unit)
	{
		if (!unit.hasMoved)
		{
			return false;
		}

		if (this.CanAttack(unit) && !unit.hasAttacked)
		{
			return false;
		}

		if (this.CanUseAbility(unit))
		{
			return false;
		}

		return true;
	}

	private void SpawnUnits(final List<Unit> units, final List<Spawn> spawns)
	{
		final Random random = new Random();
		for (final Unit unit : units)
		{
			final int spawnRoll = random.nextInt(spawns.size());
			final Spawn spawn = spawns.get(spawnRoll);
			unit.x = spawn.x;
			unit.y = spawn.y;
			spawns.remove(spawn);
		}
	}
}
