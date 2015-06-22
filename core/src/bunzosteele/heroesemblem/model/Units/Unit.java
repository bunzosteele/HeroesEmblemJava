package bunzosteele.heroesemblem.model.Units;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Abilities.Ability;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public abstract class Unit implements Ai
{
	public int team;
	public UnitType type;
	public String name;
	public int attack;
	public int defense;
	public int evasion;
	public int accuracy;
	public int movement;
	public int minimumRange;
	public int maximumRange;
	public int maximumHealth;
	public int currentHealth;
	public int experience;
	public int experienceNeeded;
	public int level;
	public Ability ability;
	public int cost;
	public int id;
	public int x;
	public int y;
	public boolean isAttacking = false;
	public boolean isTakingDamage = false;
	public boolean isGettingExperience = false;
	public boolean isHealing = false;
	public boolean isMissed = false;
	public boolean isDying = false;
	public int experienceFrame = 1;
	public int damageFrame = 1;
	public int healFrame = 1;
	public int attackFrame = 1;
	public int missedFrame = 1;
	public int deathFrame = 10;
	public int distanceMoved = 0;
	public boolean hasMoved;
	public boolean hasAttacked;
	public String damageDisplay = "";
	public List<Tile> blockedSpaces = new ArrayList<Tile>();

	public Unit(final int team, final String name, final int attack, final int defense, final int evasion, final int accuracy, final int movement, final int maximumHealth, final int maximumRange, final int minimumRange, final int cost, final int id) throws IOException
	{
		this.team = team;
		this.name = name;
		this.attack = attack;
		this.defense = defense;
		this.evasion = evasion;
		this.accuracy = accuracy;
		this.movement = movement;
		this.maximumHealth = maximumHealth;
		this.currentHealth = maximumHealth;
		this.maximumRange = maximumRange;
		this.minimumRange = minimumRange;
		this.cost = cost;
		this.id = id;
		this.level = 1;
		this.experienceNeeded = 50;
	}

	public void AddExperience(final int experience)
	{
		this.experience += experience;
		while (this.experience >= this.experienceNeeded)
		{
			this.level += 1;
			this.experience -= this.experienceNeeded;
			this.experienceNeeded += 50;

			final Random random = new Random();
			final int bonusRoll = random.nextInt(101);
			int bonusPoints = 0;
			if (bonusRoll >= 95)
			{
				bonusPoints = 5;
			} else if (bonusRoll >= 85)
			{
				bonusPoints = 4;
			} else if (bonusRoll >= 75)
			{
				bonusPoints = 3;
			} else if (bonusRoll >= 50)
			{
				bonusPoints = 2;
			} else
			{
				bonusPoints = 1;
			}

			this.maximumHealth += 1;
			for (int i = 0; i < bonusPoints; i++)
			{
				final int statRoll = random.nextInt(101);
				final boolean isCriticalPoint = random.nextInt(101) >= 95;
				if (statRoll >= 95)
				{
					this.movement += 1;
				} else if (statRoll >= 70)
				{
					if (isCriticalPoint)
					{
						this.maximumHealth += 2;
					} else
					{
						this.maximumHealth += 1;
					}
				} else if (statRoll >= 50)
				{
					if (isCriticalPoint)
					{
						this.attack += 2;
					} else
					{
						this.attack += 1;
					}
				} else if (statRoll >= 30)
				{
					if (isCriticalPoint)
					{
						this.defense += 2;
					} else
					{
						this.defense += 1;
					}
				} else if (statRoll >= 15)
				{
					if (isCriticalPoint)
					{
						this.evasion += 3;
					} else
					{
						this.evasion += 2;
					}
				} else
				{
					if (isCriticalPoint)
					{
						this.accuracy += 3;
					} else
					{
						this.accuracy += 2;
					}
				}
			}
			this.currentHealth = this.maximumHealth;
		}
	}

	public void dealDamage(final int damage)
	{
		this.currentHealth -= damage;
		this.damageDisplay = "" + damage;
	}

	public void giveExperience(final int amount)
	{
		this.AddExperience(amount);
		this.damageDisplay = "" + amount;
		this.isGettingExperience = true;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				Unit.this.experienceFrame++;
				if (Unit.this.experienceFrame > 2)
				{
					Unit.this.experienceFrame = 1;
					Unit.this.isGettingExperience = false;
					Unit.this.damageDisplay = "";
				}
			}
		}, 0, 1f, 1);
	}

	public void healDamage(final int heal)
	{
		this.currentHealth += heal;
		if (this.currentHealth > this.maximumHealth)
		{
			this.currentHealth = this.maximumHealth;
		}
		this.damageDisplay = "" + heal;
	}

	public boolean isEquivalentTo(final Unit other)
	{
		return (other != null) && (this.id == other.id);
	}

	public void startAttack()
	{
		this.isAttacking = true;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				Unit.this.attackFrame++;
				if (Unit.this.attackFrame > 2)
				{
					Unit.this.attackFrame = 1;
					Unit.this.isAttacking = false;
				}
			}
		}, 0, 1f, 3);
	}

	public void startDamage()
	{
		this.isTakingDamage = true;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				Unit.this.damageFrame++;
				if (Unit.this.damageFrame > 2)
				{
					Unit.this.damageFrame = 1;
					Unit.this.isTakingDamage = false;
					Unit.this.damageDisplay = "";
				}
			}
		}, 0, 1f, 1);
	}

	public void startDeath()
	{
		this.isDying = true;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				Unit.this.deathFrame--;
			}
		}, 0, .1f, 10);
	}

	public void startHeal()
	{
		this.isHealing = true;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				Unit.this.healFrame++;
				if (Unit.this.healFrame > 2)
				{
					Unit.this.healFrame = 1;
					Unit.this.isHealing = false;
					Unit.this.damageDisplay = "";
				}
			}
		}, 0, 1f, 1);
	}

	public void startMiss()
	{
		this.isMissed = true;
		this.damageDisplay = "Missed!";
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				Unit.this.missedFrame++;
				if (Unit.this.missedFrame > 2)
				{
					Unit.this.missedFrame = 1;
					Unit.this.isMissed = false;
					Unit.this.damageDisplay = "";
				}
			}
		}, 0, 1f, 1);
	}
}
