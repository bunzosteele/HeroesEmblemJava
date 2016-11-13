package bunzosteele.heroesemblem.model.Units;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import bunzosteele.heroesemblem.model.BattleState;
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
	public int x = -1;
	public int y = -1;
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
	public int displayEmphasisFrame = 0;
	public int maxDisplayEmphasisFrame = 0;
	public int missedFrame = 1;
	public int deathFrame = 10;
	public int distanceMoved = 0;
	public boolean hasMoved;
	public boolean hasAttacked;
	public int damageDisplay = -1;
	public boolean isCrit = false;
	public static Sound hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
	public static Sound deathSound = Gdx.audio.newSound(Gdx.files.internal("death.wav"));
	public static Sound missSound = Gdx.audio.newSound(Gdx.files.internal("miss.wav"));
	public static Sound levelSound = Gdx.audio.newSound(Gdx.files.internal("level.wav"));
	public float animationSpeed = 1.1f;
	public boolean isMale;
	public String backStory;
	public Map<Tile, Integer> movementOptions = null;
	public int initialAttack = -1;
	public int initialDefense = -1;
	public int initialEvasion = -1;
	public int initialAccuracy = -1;
	public int initialMovement = -1;
	public int initialHealth = -1;

	public Unit(final int team, final String name, final int attack, final int defense, final int evasion, final int accuracy, final int movement, final int maximumHealth, final int maximumRange, final int minimumRange, final int cost, final int id, final float animationSpeed, final boolean isMale, final String backStory, int maxIdleFrame, int maxAttackFrame) throws IOException
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
		this.isMale = isMale;
		this.backStory = backStory;
		this.maxIdleFrame = maxIdleFrame;
		this.maxAttackFrame = maxAttackFrame;
		this.maxDisplayEmphasisFrame = maxIdleFrame + maxAttackFrame + 1;
		this.animationSpeed = animationSpeed;
		float frameDuration = 1.1f / (this.maxIdleFrame + 1f);
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				Unit.this.idleFrame++;
				Unit.this.displayEmphasisFrame++;
				if (Unit.this.idleFrame > Unit.this.maxIdleFrame)
				{
					Unit.this.idleFrame = 0;
				}
				if (Unit.this.displayEmphasisFrame > Unit.this.maxDisplayEmphasisFrame)
				{
					Unit.this.displayEmphasisFrame = 0;
				}
			}
		}, 0, frameDuration);
	}

	public Unit(final int team, final String name, final int attack, final int defense, final int evasion, final int accuracy, final int movement, final int maximumHealth, final int maximumRange, final int minimumRange, final int cost, final int id, final float gameSpeed, final boolean isMale, final String backStory, int maxIdleFrame, int maxAttackFrame, int x, int y, int level, int unitsKilled, int damageDealt, int currentHealth, int experience, int experienceNeeded, int distanceMoved, boolean hasMoved, boolean hasAttacked, int initialAttack, int initialDefense, int initialAccuracy, int initialEvasion, int initialMovement, int initialHealth) throws IOException
	{
		this(team, name, attack, defense, evasion, accuracy, movement, maximumHealth, maximumRange, minimumRange, cost, id, gameSpeed, isMale, backStory, maxIdleFrame, maxAttackFrame);
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
		this.initialAttack = initialAttack;
		this.initialDefense = initialDefense;
		this.initialAccuracy = initialAccuracy;
		this.initialEvasion = initialEvasion;
		this.initialMovement = initialMovement;
		this.initialHealth = initialHealth;
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
			if (bonusRoll >= 90)
			{
				bonusPoints = 5;
			} else if (bonusRoll >= 75)
			{
				bonusPoints = 4;
			} else if (bonusRoll >= 50)
			{
				bonusPoints = 3;
			} else if (bonusRoll >= 25)
			{
				bonusPoints = 2;
			} else
			{
				bonusPoints = 1;
			}
			
			if(this.team > 0)
				bonusPoints--;

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

	public void dealDamage(int damage, boolean isCrit)
	{
		this.currentHealth -= damage;
		this.damageDisplay = damage;
		this.isCrit = isCrit;
	}

	public boolean giveExperience(final int amount)
	{
		boolean leveled = this.AddExperience(amount);
		if(!this.isGettingExperience){
			Unit.this.isGettingExperience = true;
			float displayDuration = 1.1f;
			if(team != 0)
				displayDuration = this.animationSpeed;
			this.damageDisplay = amount;
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
					}
				}
			}, 0, displayDuration, 1);
		}else{
				this.damageDisplay += amount;
		}
		return leveled;
	}

	public void healDamage(final int heal)
	{
		this.currentHealth += heal;
		if (this.currentHealth > this.maximumHealth)
		{
			this.currentHealth = this.maximumHealth;
		}
		this.damageDisplay = heal;
	}

	public boolean isEquivalentTo(final Unit other)
	{
		return (other != null) && (this.id == other.id);
	}

	public void startAttack()
	{
		float displayDuration = 1.1f;
		if(team != 0)
			displayDuration = this.animationSpeed;
		Unit.this.isAttacking = true;
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
		}, displayDuration / (this.maxAttackFrame + 1), displayDuration / (this.maxAttackFrame + 1), this.maxAttackFrame + 1);
	}

	public void startDamage()
	{
		float displayDuration = 1.1f;
		if(team == 0)
			displayDuration = this.animationSpeed;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				Unit.this.isTakingDamage = true;
				++Unit.this.damageFrame;
				if (Unit.this.damageFrame > 2)
				{
					Unit.this.damageFrame = 1;
					Unit.this.isTakingDamage = false;
				}
			}
		}, 0, displayDuration, 1);
	}
	
	public boolean checkDeath(){
		return this.currentHealth <= 0 &&(this.ability == null || !this.ability.IsPreventingDeath(this));
	}
	
	public void killUnit(Unit attacker, BattleState state){
		int baseExperience = this.maximumHealth + this.attack + this.defense;
		float scale = 1 + state.roundsSurvived / 10f;
		attacker.giveExperience((int) (baseExperience * scale));
		this.startDeath();
		state.dyingUnits.add(this);
		if(this.team == 0){
			state.roster.remove(this);
		}else{
			state.enemies.remove(this);
		}
		++attacker.unitsKilled;
	}

	public void startDeath()
	{
		float displayDuration = 1.1f;
		if(team == 0)
			displayDuration = this.animationSpeed;
		this.isDying = true;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				Unit.this.deathFrame--;
			}
		}, 0, displayDuration / 10, 10);
	}

	public void startHeal()
	{
		float displayDuration = 1.1f;
		if(team != 0)
			displayDuration = this.animationSpeed;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				Unit.this.isHealing = true;
				Unit.this.healFrame++;
				if (Unit.this.healFrame > 2)
				{
					Unit.this.healFrame = 1;
					Unit.this.isHealing = false;
				}
			}
		}, 0, displayDuration, 1);
	}

	public void startMiss()
	{
		float displayDuration = 1.1f;
		if(team == 0)
			displayDuration = this.animationSpeed;
		this.isTakingDamage = false;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				Unit.this.isMissed = true;
				Unit.this.missedFrame++;
				if (Unit.this.missedFrame > 2)
				{
					Unit.this.missedFrame = 1;
					Unit.this.isMissed = false;
				}
			}
		}, 0, displayDuration, 1);
	}
}
