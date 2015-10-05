package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.HashSet;
import java.util.List;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;

public class ShieldBash extends Ability
{
	private static Sound sound = Gdx.audio.newSound(Gdx.files.internal("bash.wav"));
	
	public ShieldBash()
	{
		this.displayName = "Shield Bash";
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
	public boolean Execute(final BattleState state, Unit executor, final Tile targetTile)
	{
		for (final Unit unit : this.GetTargetableUnits(state))
		{
			if ((unit.x == targetTile.x) && (unit.y == targetTile.y))
			{
				executor.startAttack();
				ShieldBash.sound.play();
				unit.hasAttacked = true;
				unit.hasMoved = true;
				unit.dealDamage(executor.attack);
				unit.startDamage();
				unit.checkDeath(executor);
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Unit> GetTargetableUnits(final BattleState state)
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
