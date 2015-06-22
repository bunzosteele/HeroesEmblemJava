package bunzosteele.heroesemblem.model.Units.Abilities;

import bunzosteele.heroesemblem.model.Units.Unit;

public class Sturdy extends Ability
{
	public Sturdy()
	{
		this.displayName = "Sturdy";
		this.isActive = false;
		this.isTargeted = false;
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
