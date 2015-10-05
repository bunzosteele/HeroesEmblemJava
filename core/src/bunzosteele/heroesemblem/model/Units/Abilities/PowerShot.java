package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.HashSet;
import java.util.List;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;

public class PowerShot extends Ability
{
	private static Sound sound = Gdx.audio.newSound(Gdx.files.internal("powershot.wav"));
	
	public PowerShot()
	{
		this.displayName = "Power Shot";
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
		final HashSet<Tile> targetTiles = this.GetTargetedTiles(state, targetTile);
		boolean hit = false;
		for (final Unit unit : this.GetTargetableUnits(state))
		{
			for (final Tile tile : targetTiles)
			{
				if ((unit.x == tile.x) && (unit.y == tile.y))
				{
					hit = true;
					unit.dealDamage(executor.attack);
					unit.startDamage();
					unit.checkDeath(executor);
				}
			}
		}
		if (hit)
		{
			executor.startAttack();
			PowerShot.sound.play();
		}
		return hit;
	}

	@Override
	public List<Unit> GetTargetableUnits(final BattleState state)
	{
		return state.AllUnits();
	}

	private HashSet<Tile> GetTargetedTiles(final BattleState state, final Tile targetTile)
	{
		final int originX = state.selected.x;
		final int originY = state.selected.y;
		final int originAltitude = state.battlefield.get(state.selected.y).get(state.selected.x).altitude;
		final HashSet<Tile> targetTiles = new HashSet<Tile>();
		if (targetTile.x > originX)
		{
			int x = originX + 1;
			final int y = originY;
			while (this.isInBounds(x, y, state.battlefield))
			{
				targetTiles.add(state.battlefield.get(y).get(x));
				if (this.isHigherAltitude(originAltitude, x, y, state.battlefield))
				{
					break;
				}
				x++;

			}
		} else if (targetTile.x < originX)
		{
			int x = originX - 1;
			final int y = originY;
			while (this.isInBounds(x, y, state.battlefield))
			{
				targetTiles.add(state.battlefield.get(y).get(x));
				if (this.isHigherAltitude(originAltitude, x, y, state.battlefield))
				{
					break;
				}
				x--;
			}
		} else if (targetTile.y > originY)
		{
			final int x = originX;
			int y = originY + 1;
			while (this.isInBounds(x, y, state.battlefield))
			{
				targetTiles.add(state.battlefield.get(y).get(x));
				if (this.isHigherAltitude(originAltitude, x, y, state.battlefield))
				{
					break;
				}
				y++;
			}
		} else if (targetTile.y < originY)
		{
			final int x = originX;
			int y = originY - 1;
			while (this.isInBounds(x, y, state.battlefield))
			{
				targetTiles.add(state.battlefield.get(y).get(x));
				if (this.isHigherAltitude(originAltitude, x, y, state.battlefield))
				{
					break;
				}
				y--;
			}
		}
		return targetTiles;
	}

	@Override
	public HashSet<Tile> GetTargetTiles(final BattleState state, final Unit originUnit)
	{
		final HashSet<Tile> targets = new HashSet<Tile>();
		int x = originUnit.x;
		int y = originUnit.y - 1;
		final int originAltitude = state.battlefield.get(originUnit.y).get(originUnit.x).altitude;
		while (this.isInBounds(x, y, state.battlefield))
		{
			targets.add(state.battlefield.get(y).get(x));
			if (this.isHigherAltitude(originAltitude, x, y, state.battlefield))
			{
				break;
			}
			y--;
		}

		x = originUnit.x + 1;
		y = originUnit.y;
		while (this.isInBounds(x, y, state.battlefield))
		{
			targets.add(state.battlefield.get(y).get(x));
			if (this.isHigherAltitude(originAltitude, x, y, state.battlefield))
			{
				break;
			}
			x++;
		}

		x = originUnit.x;
		y = originUnit.y + 1;
		while (this.isInBounds(x, y, state.battlefield))
		{
			targets.add(state.battlefield.get(y).get(x));
			if (this.isHigherAltitude(originAltitude, x, y, state.battlefield))
			{
				break;
			}
			y++;
		}

		x = originUnit.x - 1;
		y = originUnit.y;
		while (this.isInBounds(x, y, state.battlefield))
		{
			targets.add(state.battlefield.get(y).get(x));
			if (this.isHigherAltitude(originAltitude, x, y, state.battlefield))
			{
				break;
			}
			x--;
		}

		return targets;
	}

	private boolean isHigherAltitude(final int originAltitude, final int x, final int y, final List<List<Tile>> battlefield)
	{
		return originAltitude < battlefield.get(y).get(x).altitude;
	}
}
