package bunzosteele.heroesemblem.model.Units.Abilities;

import bunzosteele.heroesemblem.model.Units.Unit;

public class Joust extends Ability
{
	public Joust()
	{
		this.displayName = "Joust";
		this.isActive = false;
		this.isTargeted = false;
	}

	@Override
	public int AttackModifier(final Unit attacker)
	{
		return attacker.distanceMoved;
	}
}
