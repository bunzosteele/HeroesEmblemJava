package bunzosteele.heroesemblem.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

public final class CombatHelper {
	
	public static boolean Attack(Unit attacker, Unit victim, List<List<Tile>> battlefield){
			Tile tile = battlefield.get(victim.y).get(victim.x);
			boolean hit = CheckHit(attacker, victim, tile);
			if(hit){
				DealDamage(attacker, victim, tile);
			}
			return hit;
	}
	
	private static boolean CheckHit(Unit attacker, Unit victim, Tile tile){
		int chanceToHit = attacker.accuracy;
		chanceToHit -= victim.evasion;
		chanceToHit -= tile.accuracyModifier;
		Random random = new Random();
		int roll = random.nextInt(101);		
		return roll < chanceToHit;
	}
	
	private static void DealDamage(Unit attacker, Unit victim, Tile tile){
		int damage = attacker.attack;
		damage -= victim.defense;
		damage -= tile.defenseModifier;
		Random random = new Random();
		int roll = random.nextInt(101);
		if (roll <= 10){
			damage -= 1;
		}else if(roll == 100){
			damage = damage *2;
		}else if (roll > 90){
			damage += 1;
		}
		
		if(damage < 0)
			damage = 0;
	
		victim.dealDamage(damage);
	}
	
	
	public static HashSet<Tile> GetAttackOptions(BattleState state, Unit attackingUnit){
		if(state.selected == null){
			return new HashSet<Tile>();
		}
		HashSet<Tile> maxOptions = new HashSet<Tile>();
		HashSet<Tile> minOptions = new HashSet<Tile>();
		List<Unit> friendlies = new ArrayList<Unit>();
		for (Unit unit: state.roster){
			if(!unit.isEquivalentTo(attackingUnit)){
				friendlies.add(unit);
			}
		}
		maxOptions = GetAttackOptionsCore(attackingUnit.x, attackingUnit.y, friendlies, state.enemies, attackingUnit, state.battlefield, maxOptions, attackingUnit.maximumRange, 0);
		minOptions = GetAttackOptionsCore(attackingUnit.x, attackingUnit.y, friendlies, state.enemies, attackingUnit, state.battlefield, minOptions, attackingUnit.minimumRange, 0);
		maxOptions.removeAll(minOptions);
		return maxOptions;
	}
	
	public static HashSet<Tile> GetAttackOptionsCore(int x, int y, List<Unit> friendlyUnits, List<Unit> enemies, Unit attackingUnit, List<List<Tile>> battlefield, HashSet<Tile> options, int range, int distance){
		if(!isInBounds(x, y, battlefield) || distance > range){
			return options;
		}
		
		if(!isOccupied(x, y, friendlyUnits)){
			Tile newOption = battlefield.get(y).get(x);
			options.add(newOption);
		}
		
		if(battlefield.get(y).get(x).altitude > startingAltitude(attackingUnit, battlefield)){
			return options;
		}
		
		if(isInBounds(x, y-1, battlefield)){				
			GetAttackOptionsCore(x, y-1, friendlyUnits, enemies, attackingUnit, battlefield, options, range, distance+1);		
		}
		if(isInBounds(x+1, y, battlefield)){				
			GetAttackOptionsCore(x+1, y, friendlyUnits, enemies, attackingUnit, battlefield, options, range, distance+1);		
		}
		if(isInBounds(x, y+1, battlefield)){				
			GetAttackOptionsCore(x, y+1, friendlyUnits, enemies, attackingUnit, battlefield, options, range, distance+1);		
		}
		if(isInBounds(x-1, y, battlefield)){				
			GetAttackOptionsCore(x-1, y, friendlyUnits, enemies, attackingUnit, battlefield, options, range, distance+1);			
		}
		
		return options;
	}
	
	private static boolean isInBounds(int x, int y, List<List<Tile>> battlefield){
		int height = battlefield.size();
		int width = battlefield.get(0).size();
		
		return x >= 0 && x < width && y >= 0 && y < height;
	}
	
	private static boolean isOccupied(int x , int y, List<Unit> units){
		boolean occupied = false;
		for(Unit unit : units){
			if (unit.x == x && unit.y == y)
				occupied = true;
		}
		return occupied;
	}
	
	private static boolean isVisited(int x, int y, List<Tile> visited){
		boolean isVisited = false;
		for(Tile tile: visited){
			if(tile.x == x && tile.y == y)
				isVisited = true;
		}
		return isVisited;
	}
	
	private static int startingAltitude(Unit unit, List<List<Tile>> battlefield){
		return battlefield.get(unit.y).get(unit.x).altitude;
	}
	
	private static boolean inRange(int targetX, int targetY, int originX, int originY, int range){
		int dx = Math.abs(originX - targetX);
		int dy = Math.abs(originY - targetY);
		return (dx + dy) <= range;
	}
}
