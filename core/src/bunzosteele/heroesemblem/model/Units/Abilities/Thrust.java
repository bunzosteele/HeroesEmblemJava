package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.HashSet;
import java.util.List;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.graphics.Color;

public class Thrust extends Ability
{
	public Thrust()
	{
		this.displayName = "Thrust";
		this.isActive = true;
		this.isTargeted = true;
		this.abilityColor = new Color(1f, 0f, 0f, .5f);
	}

	@Override
	public boolean CanUse(final BattleState state, final Unit originUnit)
	{
		if (originUnit.hasAttacked)
		{
			return false;
		}

		if (this.exhausted)
		{
			return false;
		}

		for (final Tile tile : this.GetTargetTiles(state, originUnit))
		{
			for (final Unit unit : state.enemies)
			{
				if ((unit.x == tile.x) && (unit.y == tile.y))
				{
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean Execute(final BattleState state, final Tile targetTile)
	{
		for (final Unit unit : this.GetTargetableUnits(state))
		{
			if ((unit.x == targetTile.x) && (unit.y == targetTile.y))
			{
				final int originX = state.selected.x;
				final int originY = state.selected.y;
				final int dx = unit.x - originX;
				final int dy = unit.y - originY;
				final int nextX = originX + (2 * dx);
				final int nextY = originY + (2 * dy);
				final boolean canKnockBack = this.isInBounds(nextX, nextY, state.battlefield) && this.isEmpty(nextX, nextY, state.AllUnits()) && (unit.movement >= state.battlefield.get(originY + (2 * dy)).get(originX + (2 * dx)).movementCost);
				if (canKnockBack)
				{
					state.selected.startAttack();
					state.selected.x += dx;
					state.selected.y += dy;
					unit.x += dx;
					unit.y += dy;
					unit.dealDamage(state.selected.attack);
					unit.startDamage();
				} else if (this.isInBounds(nextX, nextY, state.battlefield) && !this.isEmpty(nextX, nextY, state.AllUnits()))
				{
					state.selected.startAttack();
					unit.dealDamage(state.selected.attack);
					unit.startDamage();
					for (final Unit nextUnit : state.AllUnits())
					{
						if ((nextUnit.x == nextX) && (nextUnit.y == nextY))
						{
							nextUnit.dealDamage(state.selected.attack / 2);
							nextUnit.startDamage();
						}
					}
				} else
				{
					state.selected.startAttack();
					unit.dealDamage(state.selected.attack * 2);
					unit.startDamage();
				}
				return true;
			}
		}
		return false;
	}

	private List<Unit> GetTargetableUnits(final BattleState state)
	{
		return state.enemies;
	}

	@Override
	public HashSet<Tile> GetTargetTiles(final BattleState state, final Unit originUnit)
	{
		final HashSet<Tile> targets = new HashSet<Tile>();
		if (this.isInBounds(originUnit.x, originUnit.y - 1, state.battlefield))
		{
			targets.add(state.battlefield.get(originUnit.y - 1).get(originUnit.x));
		}
		if (this.isInBounds(originUnit.x + 1, originUnit.y, state.battlefield))
		{
			targets.add(state.battlefield.get(originUnit.y).get(originUnit.x + 1));
		}
		if (this.isInBounds(originUnit.x, originUnit.y + 1, state.battlefield))
		{
			targets.add(state.battlefield.get(originUnit.y + 1).get(originUnit.x));
		}
		if (this.isInBounds(originUnit.x - 1, originUnit.y, state.battlefield))
		{
			targets.add(state.battlefield.get(originUnit.y).get(originUnit.x - 1));
		}
		return targets;
	}

}
