package bunzosteele.heroesemblem.model.Units;

import java.io.IOException;
import java.util.Random;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import bunzosteele.heroesemblem.model.Units.Abilities.Ability;

public abstract class Unit {
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
	public boolean isMissed = false;
	public boolean isDying = false;
	public int experienceFrame = 1;
	public int damageFrame = 1;
	public int attackFrame = 1;
	public int missedFrame = 1;
	public int deathFrame = 10;
	public boolean hasMoved;
	public boolean hasAttacked;
	public String damageDisplay = "";
	
	public Unit(int team, String name, int attack, int defense, int evasion, int accuracy, int movement, int maximumHealth, int maximumRange, int minimumRange, int cost, int id) throws IOException{		
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
	
	public void AddExperience(int experience){
		this.experience += experience;
		while(this.experience >= this.experienceNeeded){
			this.level += 1;
			this.experience -= this.experienceNeeded;
			this.experienceNeeded += 50;
			
			Random random = new Random();
			int bonusRoll = random.nextInt(101);
			int bonusPoints = 0;
			if(bonusRoll >= 95){
				bonusPoints = 5;
			}else if(bonusRoll >= 85){
				bonusPoints = 4;
			}else if(bonusRoll >= 75){
				bonusPoints = 3;
			}else if(bonusRoll >= 50){
				bonusPoints = 2;
			}else{
				bonusPoints = 1;
			}
			
			this.maximumHealth += 1;
			for(int i = 0; i <bonusPoints; i++){
				int statRoll = random.nextInt(101);
				boolean isCriticalPoint = random.nextInt(101) >= 95;
				if(statRoll >= 95){
					this.movement += 1;
				}else if(statRoll >= 70){
					if(isCriticalPoint)
						this.maximumHealth += 2;
					else
						this.maximumHealth += 1;
				}else if(statRoll >= 50){
					if(isCriticalPoint)
						this.attack += 2;
					else
						this.attack += 1;
				}else if(statRoll >= 30){
					if(isCriticalPoint)
						this.defense += 2;
					else
						this.defense += 1;
				}else if(statRoll >= 15){
					if(isCriticalPoint)
						this.evasion += 3;
					else
						this.evasion += 2;
				}else{
					if(isCriticalPoint)
						this.accuracy += 3;
					else
						this.accuracy += 2;
				}
			}
			this.currentHealth = maximumHealth;
		}
	}
	
	public boolean isEquivalentTo(Unit other){
		return other != null && this.id == other.id;
	}
	
	public void dealDamage(int damage){
		this.currentHealth -= damage;
		this.damageDisplay = "" + damage;
	}
	
	public void startAttack(){
		isAttacking = true;
		Timer.schedule(new Task(){
			@Override
			public void run(){
				attackFrame++;
				if(attackFrame > 2){
					attackFrame = 1;
					isAttacking = false;
				}	
			}}, 0 , 1f, 3);
	}
	
	public void startDamage(){
		isTakingDamage = true;
		Timer.schedule(new Task(){
			@Override
			public void run(){
				damageFrame++;
				if(damageFrame > 2){
					damageFrame = 1;
					isTakingDamage = false;
					damageDisplay = "";
				}
			}}, 0 , 1f, 1);
	}
	
	public void startMiss(){
		isMissed = true;
		damageDisplay = "Missed!";
		Timer.schedule(new Task(){
			@Override
			public void run(){
				missedFrame++;
				if(missedFrame > 2){
					missedFrame = 1;
					isMissed = false;
					damageDisplay = "";
				}
			}}, 0 , 1f, 1);
	}
	
	public void giveExperience(int amount){
		AddExperience(amount);
		damageDisplay = "" + amount;
		isGettingExperience = true;
		Timer.schedule(new Task(){
			@Override
			public void run(){
				experienceFrame++;
				if(experienceFrame > 2){
					experienceFrame = 1;
					isGettingExperience = false;
					damageDisplay = "";
				}
			}}, 0 , 1f, 1);
	}
	
	public void startDeath(){
		isDying = true;
		Timer.schedule(new Task(){
			@Override
			public void run(){
				deathFrame--;
			}}, 0 , .1f, 10);
	}
}
