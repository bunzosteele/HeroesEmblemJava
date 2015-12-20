package bunzosteele.heroesemblem.model.Units;

import java.util.List;

public class UnitDto {
	public String type;
	public String name;
	public int attack;
	public int defense;
	public int evasion;
	public int accuracy;
	public int movement;
	public int maximumHealth;
	public int initialAttack;
	public int initialDefense;
	public int initialEvasion;
	public int initialAccuracy;
	public int initialMovement;
	public int initialHealth;
	public int level;
	public String ability;
	public int unitsKilled;
	public int damageDealt;
	public int roundKilled;
	public LocationDto locationKilled;
	public boolean isMale;
	public String backStory;
	public int team;
	public int currentHealth;
	public int experience;
	public int experienceNeeded;
	public int cost;
	public int id;
	public int x;
	public int y;
	public int maximumRange;
	public int minimumRange;
	public int distanceMoved;
	public boolean hasMoved;
	public boolean hasAttacked;
	public float animationSpeed;
	public boolean isAbilityExhausted;
	public boolean canUseAbility;
	public List<Integer> abilityTargets;
}
