package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.HashSet;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;

public class ChainLightning extends Ability
{
	private static Sound sound = Gdx.audio.newSound(Gdx.files.internal("lightning.wav"));
	
	public ChainLightning()
	{
		this.displayName = "Chain Lightning";
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
	public boolean Execute(final BattleState state, Unit executor, final Tile targetTile)
	{
		final HashSet<Unit> damagedUnits = new HashSet<Unit>();
		if (this.GetTargetTiles(state, executor).contains(targetTile))
		{
			int damage = executor.attack;
			int originX = targetTile.x;
			int originY = targetTile.y;
			ChainLightning.sound.play();
			executor.startAttack();
			while (damage > 0)
			{
				Unit damagedUnit = null;
				for (final Unit unit : state.AllUnits())
				{
					if ((unit.x == originX) && (unit.y == originY) && !damagedUnits.contains(unit))
					{
						damagedUnit = unit;
					}
				}
				if (damagedUnit != null)
				{
					damagedUnit.dealDamage(damage);
					damagedUnit.startDamage();
					damagedUnit.checkDeath(executor);
					damagedUnits.add(damagedUnit);
					damage = damage / 2;
					Unit nextUnit = null;
					for (final Unit potentialTarget : state.AllUnits())
					{
						if ((nextUnit == null) && !damagedUnits.contains(potentialTarget))
						{
							nextUnit = potentialTarget;
						} else
						{
							if (!damagedUnits.contains(potentialTarget))
							{
								final int potentialTargetDistance = Math.abs(potentialTarget.x - originX) + Math.abs(potentialTarget.y - originY);
								final int previousDistance = Math.abs(nextUnit.x - originX) + Math.abs(nextUnit.y - originY);
								if (potentialTargetDistance < previousDistance)
								{
									nextUnit = potentialTarget;
								}
							}
						}
					}
					if (nextUnit == null)
					{
						return true;
					} else
					{
						originX = nextUnit.x;
						originY = nextUnit.y;
					}
				} else
				{
					return true;
				}
			}
		}

		return damagedUnits.size() > 0;
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
