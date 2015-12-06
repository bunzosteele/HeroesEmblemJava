package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.List;

import bunzosteele.heroesemblem.model.Units.Unit;

public class Sturdy extends Ability
{
	public Sturdy()
	{
		this.displayName = "Sturdy";
		this.isActive = false;
		this.isTargeted = false;
		this.isAction = false;
	}
	
	public Sturdy(boolean exhausted, boolean canUse, List<Integer> abilityTargets){
		this();
		this.exhausted = exhausted;
	}

	@Override
	public boolean IsPreventingDeath(final Unit unit)
	{
		if (!this.exhausted)
		{
			this.exhausted = true;
			unit.currentHealth = 1;
			return true;
		}
		return false;
	}
}
