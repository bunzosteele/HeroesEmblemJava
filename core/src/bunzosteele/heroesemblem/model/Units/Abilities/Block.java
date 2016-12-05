package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.List;
import java.util.Random;

public class Block extends Ability
{
	public Block()
	{
		this.displayName = "Block";
		this.isActive = false;
		this.isTargeted = false;
		this.isAction = false;
		this.description = "This unit has a chance to block all incoming damage when attacked.";
	}
	
	public Block(boolean exhausted, boolean canUse, List<Integer> abilityTargets){
		this();
	}

	@Override
	public boolean IsBlockingDamage()
	{
		final Random rand = new Random();
		final int roll = rand.nextInt(101);
		return roll > 90;
	}
}
