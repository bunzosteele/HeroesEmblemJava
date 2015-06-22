package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.Random;

public class Block extends Ability
{
	public Block()
	{
		this.displayName = "Block";
		this.isActive = false;
		this.isTargeted = false;
	}

	@Override
	public boolean IsBlockingDamage()
	{
		final Random rand = new Random();
		final int roll = rand.nextInt(101);
		return roll > 90;
	}
}
