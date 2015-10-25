package bunzosteele.heroesemblem.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

public final class CombatHelper
{
	public static boolean Attack(final Unit attacker, final Unit victim, final List<List<Tile>> battlefield)
	{
		final Tile tile = battlefield.get(victim.y).get(victim.x);
		final boolean hit = CombatHelper.CheckHit(attacker, victim, tile);
		if (hit)
		{
			CombatHelper.DealDamage(attacker, victim, tile);
		}
		return hit;
	}
	
	public static HashSet<Unit> GetAttackableTargets(int x, int y, Unit attacker, BattleState state){
		HashSet<Tile> options = GetAttackOptionsFromPosition(x, y, attacker, state);
		HashSet<Unit> attackableUnits = new HashSet<Unit>();
		for(Unit unit : state.roster){
			for(Tile option : options){
				if(unit.x == option.x && unit.y == option.y && !unit.isDying){
					attackableUnits.add(unit);
				}
			}
		}
		return attackableUnits;
	}
	
	public static boolean CanAttackFromPosition(int x, int y, Unit attacker, BattleState state){
		HashSet<Tile> options = GetAttackOptionsFromPosition(x, y, attacker, state);
		for(Unit unit : state.roster){
			for(Tile option : options){
				if(unit.x == option.x && unit.y == option.y){
					return true;
				}
			}
		}
		return false;
	}
	
	public static HashSet<Tile> GetAttackOptionsFromPosition(int x, int y, Unit attacker, BattleState state){
		HashSet<Tile> maxOptions = new HashSet<Tile>();
		HashSet<Tile> minOptions = new HashSet<Tile>();
		final List<Unit> friendlies = new ArrayList<Unit>();
		for (Unit unit : state.enemies)
		{
			if (!unit.isEquivalentTo(attacker))
			{
				friendlies.add(unit);
			}
		}
		maxOptions = CombatHelper.GetAttackOptionsCore(x, y, friendlies, state.enemies, state.battlefield.get(y).get(x).altitude, state.battlefield, maxOptions, attacker.maximumRange, 0);
		minOptions = CombatHelper.GetAttackOptionsCore(x, y, friendlies, state.enemies, state.battlefield.get(y).get(x).altitude, state.battlefield, minOptions, attacker.minimumRange, 0);
		maxOptions.removeAll(minOptions);
		return maxOptions;
	}

	private static boolean CheckHit(final Unit attacker, final Unit victim, final Tile tile)
	{
		int chanceToHit = attacker.accuracy;
		chanceToHit -= victim.evasion;
		chanceToHit -= tile.accuracyModifier;
		final Random random = new Random();
		final int roll = random.nextInt(101);
		return roll < chanceToHit;
	}

	private static void DealDamage(final Unit attacker, final Unit victim, final Tile tile)
	{
		int damage = attacker.attack;
		damage -= victim.defense;
		damage -= tile.defenseModifier;
		if (attacker.ability != null)
		{
			damage += attacker.ability.AttackModifier(attacker);
		}

		final Random random = new Random();
		final int roll = random.nextInt(101);
		if (roll <= 10)
		{
			damage -= 1;
		} else if (roll == 100)
		{
			damage = damage * 2;
		} else if (roll > 90)
		{
			damage += 1;
		}

		if ((victim.ability != null) && victim.ability.IsBlockingDamage())
		{
			damage = 0;
		}

		if (damage < 0)
		{
			damage = 0;
		}

		victim.dealDamage(damage);
		attacker.damageDealt += damage;
	}

	public static HashSet<Tile> GetAttackOptions(final BattleState state, final Unit attackingUnit)
	{
		if (attackingUnit == null)
		{
			return new HashSet<Tile>();
		}
		HashSet<Tile> maxOptions = new HashSet<Tile>();
		HashSet<Tile> minOptions = new HashSet<Tile>();
		final List<Unit> friendlies = new ArrayList<Unit>();
		for (Unit unit : (state.currentPlayer == 0 ? state.roster : state.enemies))
		{
			if (!unit.isEquivalentTo(attackingUnit))
			{
				friendlies.add(unit);
			}
		}
		maxOptions = CombatHelper.GetAttackOptionsCore(attackingUnit.x, attackingUnit.y, friendlies, state.enemies, state.battlefield.get(attackingUnit.y).get(attackingUnit.x).altitude, state.battlefield, maxOptions, attackingUnit.maximumRange, 0);
		minOptions = CombatHelper.GetAttackOptionsCore(attackingUnit.x, attackingUnit.y, friendlies, state.enemies,  state.battlefield.get(attackingUnit.y).get(attackingUnit.x).altitude, state.battlefield, minOptions, attackingUnit.minimumRange, 0);
		maxOptions.removeAll(minOptions);
		return maxOptions;
	}

	public static HashSet<Tile> GetAttackOptionsCore(final int x, final int y, final List<Unit> friendlyUnits, final List<Unit> enemies, final int initialAltitude, final List<List<Tile>> battlefield, final HashSet<Tile> options, final int range, final int distance)
	{
		if (!CombatHelper.isInBounds(x, y, battlefield) || (distance > range))
		{
			return options;
		}

		if (!CombatHelper.isOccupied(x, y, friendlyUnits))
		{
			final Tile newOption = battlefield.get(y).get(x);
			options.add(newOption);
		}

		if (battlefield.get(y).get(x).altitude > initialAltitude)
		{
			return options;
		}

		if (CombatHelper.isInBounds(x, y - 1, battlefield))
		{
			CombatHelper.GetAttackOptionsCore(x, y - 1, friendlyUnits, enemies, initialAltitude, battlefield, options, range, distance + 1);
		}
		if (CombatHelper.isInBounds(x + 1, y, battlefield))
		{
			CombatHelper.GetAttackOptionsCore(x + 1, y, friendlyUnits, enemies, initialAltitude, battlefield, options, range, distance + 1);
		}
		if (CombatHelper.isInBounds(x, y + 1, battlefield))
		{
			CombatHelper.GetAttackOptionsCore(x, y + 1, friendlyUnits, enemies, initialAltitude, battlefield, options, range, distance + 1);
		}
		if (CombatHelper.isInBounds(x - 1, y, battlefield))
		{
			CombatHelper.GetAttackOptionsCore(x - 1, y, friendlyUnits, enemies, initialAltitude, battlefield, options, range, distance + 1);
		}

		return options;
	}

	private static boolean isInBounds(final int x, final int y, final List<List<Tile>> battlefield)
	{
		final int height = battlefield.size();
		final int width = battlefield.get(0).size();

		return (x >= 0) && (x < width) && (y >= 0) && (y < height);
	}

	private static boolean isOccupied(final int x, final int y, final List<Unit> units)
	{
		boolean occupied = false;
		for (Unit unit : units)
		{
			if ((unit.x == x) && (unit.y == y))
			{
				occupied = true;
			}
		}
		return occupied;
	}
}
