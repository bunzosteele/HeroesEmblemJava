package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.HashSet;
import java.util.List;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;

public class Thrust extends Ability
{
	public Thrust()
	{
		this.displayName = "Thrust";
		this.isActive = true;
		this.isTargeted = true;
		this.abilityColor = new Color(1f, 0f, 0f, .5f);
		this.isAction = true;
	}
	
	public Thrust(boolean exhausted, boolean canUse, List<Integer> abilityTargets){
		this();
		this.exhausted = exhausted;
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
				final int originX = executor.x;
				final int originY = executor.y;
				final int dx = unit.x - originX;
				final int dy = unit.y - originY;
				final int nextX = originX + (2 * dx);
				final int nextY = originY + (2 * dy);
				final boolean canKnockBack = this.isInBounds(nextX, nextY, state.battlefield) && this.isEmpty(nextX, nextY, state.AllUnits()) && (unit.movement >= state.battlefield.get(originY + (2 * dy)).get(originX + (2 * dx)).movementCost);
				if (canKnockBack)
				{
					executor.startAttack();
					executor.x += dx;
					executor.y += dy;
					unit.x += dx;
					unit.y += dy;
					unit.dealDamage(executor.attack);
					executor.damageDealt += executor.attack;
					unit.startDamage();
					if(unit.checkDeath()){
						unit.killUnit(executor, state);
						if(unit.team == 0)
							state.SaveGraveyard(unit);
					}
				} else if (this.isInBounds(nextX, nextY, state.battlefield) && !this.isEmpty(nextX, nextY, state.AllUnits()))
				{
					executor.startAttack();
					unit.dealDamage(executor.attack);
					executor.damageDealt += executor.attack;
					unit.startDamage();
					if(unit.checkDeath()){
						unit.killUnit(executor, state);
						if(unit.team == 0)
							state.SaveGraveyard(unit);
					}
					for (final Unit nextUnit : state.AllUnits())
					{
						if ((nextUnit.x == nextX) && (nextUnit.y == nextY))
						{
							nextUnit.dealDamage(executor.attack / 2);
							executor.damageDealt += executor.attack / 2;
							nextUnit.startDamage();
							if(nextUnit.checkDeath()){
								nextUnit.killUnit(executor, state);
								if(nextUnit.team == 0)
									state.SaveGraveyard(nextUnit);
							}
						}
					}
				} else
				{
					executor.startAttack();
					unit.dealDamage(executor.attack * 2);
					executor.damageDealt += executor.attack * 2;
					unit.startDamage();
					if(unit.checkDeath()){
						unit.killUnit(executor, state);
						if(unit.team == 0)
							state.SaveGraveyard(unit);
					}
				}
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
	
	@Override
	public void PlaySound(float volume){
		Sound sound = Gdx.audio.newSound(Gdx.files.internal("thrust.wav"));
		sound.play(volume);
	}

}
