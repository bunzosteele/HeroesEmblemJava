package bunzosteele.heroesemblem.model.Units;

import java.io.IOException;
import java.util.Random;

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
	
	public Unit(int team, String name, int attack, int defense, int evasion, int accuracy, int movement, int maximumHealth, int maximumRange, int minimumRange, int cost, int id) throws IOException{		
		this.type = type;
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
				if(bonusRoll >= 95){
					this.movement += 1;
				}else if(bonusRoll >= 70){
					if(isCriticalPoint)
						this.maximumHealth += 2;
					else
						this.maximumHealth += 1;
				}else if(bonusRoll >= 50){
					if(isCriticalPoint)
						this.attack += 2;
					else
						this.attack += 1;
				}else if(bonusRoll >= 30){
					if(isCriticalPoint)
						this.defense += 2;
					else
						this.defense += 1;
				}else if(bonusRoll >= 15){
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
}
