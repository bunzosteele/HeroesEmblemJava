package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;

public class Heal extends Ability
{
	
	public Heal()
	{
		this.displayName = "Heal";
		this.isActive = true;
		this.isTargeted = true;
		this.isAction = true;
		this.description = "Heals an adjacent allied unit. Scales with this unit's Attack stat. Steals experience equal to the amount healed. Unlimited uses.";
	}
	
	public Heal(boolean exhausted, boolean canUse, List<Integer> abilityTargets){
		this();
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
			for (final Unit unit : state.CurrentPlayerUnits())
			{
				if ((unit.x == tile.x) && (unit.y == tile.y) && (unit.currentHealth != unit.maximumHealth))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean CouldUse(final BattleState state, final Unit originUnit, int x, int y)
	{
		if (originUnit.hasAttacked)
		{
			return false;
		}
		
		final HashSet<Tile> targets = GetTargetTiles(state, originUnit);

		for (final Tile tile : targets)
		{
			for (final Unit unit : state.CurrentPlayerUnits())
			{
				if ((unit.x == tile.x) && (unit.y == tile.y) && (unit.currentHealth != unit.maximumHealth))
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
			int missingHealth = unit.maximumHealth - unit.currentHealth;
			if ((unit.x == targetTile.x) && (unit.y == targetTile.y) && missingHealth > 0)
			{
				executor.startAttack();
				int heal = executor.attack;
				final Random random = new Random();
				final int roll = random.nextInt(101);
				if (roll <= 10)
				{
					heal -= 1;
				} else if (roll == 100)
				{
					heal = heal * 2;
				} else if (roll > 90)
				{
					heal += 1;
				}
				if (heal < 0)
				{
					heal = 0;
				}
				if (heal > (missingHealth))
				{
					heal = missingHealth;
				}
				int priestExp = heal;
				if(priestExp > unit.experience)
					priestExp = unit.experience;
				
				unit.healDamage(heal);
				unit.startHeal();
				unit.experience = unit.experience - priestExp;
				if(unit.experience < 0)
					unit.experience = 0;
				executor.giveExperience(priestExp);
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Unit> GetTargetableUnits(final BattleState state)
	{
		return state.CurrentPlayerUnits();
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
		Sound sound = Gdx.audio.newSound(Gdx.files.internal("heal.wav"));
		sound.play(volume);
	}
}
