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
	public ShieldBash()
	{
		this.displayName = "Shield Bash";
		this.isActive = true;
		this.isTargeted = true;
		this.isAction = true;
		this.description = "Deals damage to target adjacent unit equal to this unit's Attack. Stuns the target for one turn. Can be used once per battle.";
	}
	
	public ShieldBash(boolean exhausted, boolean canUse, List<Integer> abilityTargets){
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
				executor.startAttack();
				unit.hasAttacked = true;
				unit.hasMoved = true;
				unit.dealDamage(executor.attack, false);
				executor.damageDealt += executor.attack;
				unit.startDamage();
				if(unit.checkDeath()){
					unit.killUnit(executor, state);
					if(unit.team == 0)
						state.SaveGraveyard(unit);
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
		Sound sound = Gdx.audio.newSound(Gdx.files.internal("bash.wav"));
		sound.play(volume);
	}
}
