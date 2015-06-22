package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.HashSet;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.graphics.Color;

public class Snipe extends Ability
{
	public Snipe()
	{
		this.displayName = "Snipe";
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

		return true;
	}

	@Override
	public boolean Execute(final BattleState state, final Tile targetTile)
	{
		if (this.GetTargetTiles(state, state.selected).contains(targetTile))
		{
			for (final Unit unit : state.AllUnits())
			{
				if ((unit.x == targetTile.x) && (unit.y == targetTile.y))
				{
					state.selected.startAttack();
					unit.dealDamage(state.selected.attack);
					unit.startDamage();
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public HashSet<Tile> GetTargetTiles(final BattleState state, final Unit originUnit)
	{
		final HashSet<Tile> targets = new HashSet<Tile>();
		for (final Unit unit : state.enemies)
		{
			targets.add(state.battlefield.get(unit.y).get(unit.x));
		}
		return targets;
	}
}
