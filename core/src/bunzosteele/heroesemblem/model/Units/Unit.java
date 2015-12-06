package bunzosteele.heroesemblem.model.Units;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Abilities.Ability;
import bunzosteele.heroesemblem.view.BattleWindow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
	public int unitsKilled;
	public int damageDealt;
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
	public int attackFrame = 0;
	public int idleFrame = 0;
	public int maxAttackFrame;
	public int maxIdleFrame;
	public int missedFrame = 1;
	public int deathFrame = 10;
	public int distanceMoved = 0;
	public boolean hasMoved;
	public boolean hasAttacked;
	public String damageDisplay = "";
	public static Sound hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
	public static Sound deathSound = Gdx.audio.newSound(Gdx.files.internal("death.wav"));
	public static Sound missSound = Gdx.audio.newSound(Gdx.files.internal("miss.wav"));
	public static Sound levelSound = Gdx.audio.newSound(Gdx.files.internal("level.wav"));
	public float gameSpeed;
	public boolean isMale;
	public String backStory;
	public Map<Tile, Integer> movementOptions = null;

	public Unit(final int team, final String name, final int attack, final int defense, final int evasion, final int accuracy, final int movement, final int maximumHealth, final int maximumRange, final int minimumRange, final int cost, final int id, final float gameSpeed, final boolean isMale, final String backStory) throws IOException
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
		this.gameSpeed = gameSpeed;
		this.isMale = isMale;
		this.backStory = backStory;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				idleFrame++;
				if (idleFrame > maxIdleFrame)
				{
					idleFrame = 0;
				}
			}
		}, 0, 1 / 3f);
	}

	public Unit(final int team, final String name, final int attack, final int defense, final int evasion, final int accuracy, final int movement, final int maximumHealth, final int maximumRange, final int minimumRange, final int cost, final int id, final float gameSpeed, final boolean isMale, final String backStory, int x, int y, int level, int unitsKilled, int damageDealt, int currentHealth, int experience, int experienceNeeded, int distanceMoved, boolean hasMoved, boolean hasAttacked) throws IOException
	{
		this(team, name, attack, defense, evasion, accuracy, movement, maximumHealth, maximumRange, minimumRange, cost, id, gameSpeed, isMale, backStory);
		this.x = x;
		this.y = y;
		this.level = level;
		this.unitsKilled = unitsKilled;
		this.damageDealt = damageDealt;
		this.currentHealth = currentHealth;
		this.experience = experience;
		this.experienceNeeded = experienceNeeded;
		this.distanceMoved = distanceMoved;
		this.hasMoved = hasMoved;
		this.hasAttacked = hasAttacked;
	}

	public boolean AddExperience(final int experience)
	{
		boolean leveled = false;
		this.experience += experience;
		while (this.experience >= this.experienceNeeded)
		{
			leveled = true;
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
					if(this.movement < 9){
						this.movement += 1;
					}else{
						this.evasion += 4;
					}
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
		return leveled;
	}

	public void dealDamage(final int damage)
	{
		this.currentHealth -= damage;
		this.damageDisplay = "" + damage;
	}

	public boolean giveExperience(final int amount)
	{
		boolean leveled = this.AddExperience(amount);
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
		}, 0, this.gameSpeed, 1);
		return leveled;
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
				if (Unit.this.attackFrame > maxAttackFrame)
				{
					Unit.this.attackFrame = 0;
					Unit.this.isAttacking = false;
				}
			}
		}, this.gameSpeed / 2, this.gameSpeed / 2, 3);
	}

	public void startDamage()
	{
		this.isTakingDamage = true;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				++Unit.this.damageFrame;
				if (Unit.this.damageFrame > 2)
				{
					Unit.this.damageFrame = 1;
					Unit.this.isTakingDamage = false;
					Unit.this.damageDisplay = "";
				}
			}
		}, 0, this.gameSpeed, 1);
	}
	
	public boolean checkDeath(){
		return this.currentHealth <= 0 &&(this.ability == null || !this.ability.IsPreventingDeath(this));
	}
	
	public void killUnit(Unit attacker, int roundsSurvived){
		int baseExperience = this.maximumHealth + this.attack + this.defense;
		float scale = 1 + roundsSurvived / 10f;
		attacker.giveExperience((int) (baseExperience * scale));
		this.startDeath();
		++attacker.unitsKilled;
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
		}, 0, this.gameSpeed / 10, 10);
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
		}, 0, this.gameSpeed, 1);
	}

	public void startMiss()
	{
		this.isMissed = true;
		this.damageDisplay = "Missed!";
		this.isTakingDamage = false;
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
		}, 0, this.gameSpeed, 1);
	}
}
