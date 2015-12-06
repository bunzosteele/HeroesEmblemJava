package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.List;

import bunzosteele.heroesemblem.model.Units.Unit;

public class Joust extends Ability
{
	public Joust()
	{
		this.displayName = "Joust";
		this.isActive = false;
		this.isTargeted = false;
		this.isAction = false;
	}
	
	public Joust(boolean exhausted, boolean canUse, List<Integer> abilityTargets){
		this();
	}

	@Override
	public int AttackModifier(final Unit attacker)
	{
		return attacker.distanceMoved;
	}
}
