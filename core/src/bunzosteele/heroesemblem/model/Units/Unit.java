package bunzosteele.heroesemblem.model.Units;

import java.io.IOException;

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
	
	public boolean isEquivalentTo(Unit other){
		return other != null && this.id == other.id;
	}
}
