package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;

public class Teleport extends Ability
{
	private static Sound sound = Gdx.audio.newSound(Gdx.files.internal("teleport.wav"));
	
	public Teleport()
	{
		this.displayName = "Teleport";
		this.isActive = true;
		this.isTargeted = true;
		this.abilityColor = new Color(.5f, 0f, .5f, .5f);
		this.isMultiInput = true;
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
		if (this.targets.size() == 0)
		{
			for (final Unit unit : this.GetTargetableUnits(state))
			{
				if ((targetTile.x == unit.x) && (targetTile.y == unit.y))
				{
					this.targets.add(unit);
				}
			}
			return false;
		} else
		{
			if (!this.GetTargetTiles(state, executor).contains(targetTile))
			{
				this.targets = new ArrayList<Unit>();
				return false;
			} else
			{
				executor.startAttack();
				Teleport.sound.play();
				this.targets.get(0).x = targetTile.x;
				this.targets.get(0).y = targetTile.y;
				return true;
			}
		}
	}

	@Override
	public List<Unit> GetTargetableUnits(final BattleState state)
	{
		return state.AllUnits();
	}

	@Override
	public HashSet<Tile> GetTargetTiles(final BattleState state, final Unit originUnit)
	{
		final HashSet<Tile> targets = new HashSet<Tile>();
		if (this.targets.size() == 0)
		{
			for (final Unit unit : state.AllUnits())
			{
				targets.add(state.battlefield.get(unit.y).get(unit.x));
			}
		} else
		{
			for (final List<Tile> row : state.battlefield)
			{
				for (final Tile tile : row)
				{
					if (tile.movementCost <= this.targets.get(0).movement)
					{
						targets.add(tile);
					}
				}
			}
			for (final Unit unit : state.AllUnits())
			{
				final Tile tile = state.battlefield.get(unit.y).get(unit.x);
				targets.remove(tile);
			}
		}
		return targets;
	}
}
