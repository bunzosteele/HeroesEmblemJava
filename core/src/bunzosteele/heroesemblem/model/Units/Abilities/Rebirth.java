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

public class Rebirth extends Ability
{
	public Rebirth()
	{
		this.displayName = "Rebirth";
		this.isActive = true;
		this.isTargeted = true;
		this.abilityColor = new Color(0f, 1f, 0f, .5f);
		this.areTargetsPersistent = true;
		this.isAction = true;
	}
	
	public Rebirth(boolean exhausted, boolean canUse, List<Integer> abilityTargets){
		this();
		this.targets = abilityTargets;
	}

	@Override
	public boolean CanUse(final BattleState state, final Unit originUnit)
	{
		if (originUnit.hasAttacked)
		{
			return false;
		}

		for (final Tile tile : this.GetTargetTiles(state, originUnit))
		{
			for (final Unit unit : GetTargetableUnits(state))
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
			if ((unit.x == targetTile.x) && (unit.y == targetTile.y) && unit.currentHealth != unit.maximumHealth && !this.targets.contains(unit.id))
			{
				executor.startAttack();
				int heal = unit.maximumHealth - unit.currentHealth;
				
				if(executor.attack * 4 < heal){
					heal = executor.attack * 4;
				}
				
				unit.experience -= heal;
				if(unit.experience < 0)
					unit.experience = 0;
				
				executor.giveExperience(heal);
				unit.healDamage(heal);
				unit.startHeal();
				this.targets.add(unit.id);
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Unit> GetTargetableUnits(final BattleState state)
	{
		final List<Unit> targetables = new ArrayList<Unit>();
		for (final Unit unit : state.roster)
		{
			if (!this.targets.contains(unit) && (unit.currentHealth != unit.maximumHealth))
			{
				targetables.add(unit);
			}
		}
		return targetables;
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
		if (this.isInBounds(originUnit.x, originUnit.y - 2, state.battlefield))
		{
			targets.add(state.battlefield.get(originUnit.y - 2).get(originUnit.x));
		}
		if (this.isInBounds(originUnit.x + 2, originUnit.y, state.battlefield))
		{
			targets.add(state.battlefield.get(originUnit.y).get(originUnit.x + 2));
		}
		if (this.isInBounds(originUnit.x, originUnit.y + 2, state.battlefield))
		{
			targets.add(state.battlefield.get(originUnit.y + 2).get(originUnit.x));
		}
		if (this.isInBounds(originUnit.x - 2, originUnit.y, state.battlefield))
		{
			targets.add(state.battlefield.get(originUnit.y).get(originUnit.x - 2));
		}
		if (this.isInBounds(originUnit.x - 1, originUnit.y - 1, state.battlefield))
		{
			targets.add(state.battlefield.get(originUnit.y - 1).get(originUnit.x - 1));
		}
		if (this.isInBounds(originUnit.x + 1, originUnit.y + 1, state.battlefield))
		{
			targets.add(state.battlefield.get(originUnit.y + 1).get(originUnit.x + 1));
		}
		if (this.isInBounds(originUnit.x - 1, originUnit.y + 1, state.battlefield))
		{
			targets.add(state.battlefield.get(originUnit.y + 1).get(originUnit.x - 1));
		}
		if (this.isInBounds(originUnit.x + 1, originUnit.y - 1, state.battlefield))
		{
			targets.add(state.battlefield.get(originUnit.y - 1).get(originUnit.x + 1));
		}
		return targets;
	}
	
	@Override
	public void PlaySound(float volume){
		Sound sound = Gdx.audio.newSound(Gdx.files.internal("rebirth.wav"));
		sound.play(volume);
	}

}
