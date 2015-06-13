package bunzosteele.heroesemblem.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

public final class MovementHelper {
	
	public static HashSet<Tile> GetMovementOptions(BattleState state){
		if(state.selected == null){
			return null;
		}
		HashSet<Tile> options = new HashSet<Tile>();
		List<Unit> allUnits = state.AllUnits();
		allUnits.remove(state.selected);
		return GetMovementOptionsCore(state.selected.x, state.selected.y, allUnits, state.enemies, state.selected, state.battlefield, state.selected.movement, options);
	}
	
	public static HashSet<Tile> GetMovementOptionsCore(int x, int y, List<Unit> allUnits, List<Unit> enemies, Unit movingUnit, List<List<Tile>> battlefield, int movement, HashSet<Tile> options){
			if(!isVisited(x, y, options) && !(x == movingUnit.x && y == movingUnit.y) && !isOccupied(x, y, allUnits)){
				Tile newOption = battlefield.get(y).get(x);
				options.add(newOption);
			}
		
			if(isInBounds(x, y-1, battlefield) && !isOccupied(x, y-1, enemies)){				
				int remainingMovement = movement - getMovementCost(x, y -1, battlefield);
				if (remainingMovement >= 0){
					GetMovementOptionsCore(x, y-1, allUnits, enemies, movingUnit, battlefield, remainingMovement, options);		
				}
			}
			if(isInBounds(x+1, y, battlefield) && !isOccupied(x+1, y, enemies)){				
				int remainingMovement = movement - getMovementCost(x+1, y, battlefield);
				if (remainingMovement >= 0){
					GetMovementOptionsCore(x+1, y, allUnits, enemies, movingUnit, battlefield, remainingMovement, options);		
				}
			}
			if(isInBounds(x, y+1, battlefield) && !isOccupied(x, y+1, enemies)){				
				int remainingMovement = movement - getMovementCost(x, y+1, battlefield);
				if (remainingMovement >= 0){
					GetMovementOptionsCore(x, y+1, allUnits, enemies, movingUnit, battlefield, remainingMovement, options);		
				}
			}
			if(isInBounds(x-1, y, battlefield) && !isOccupied(x-1, y, enemies)){				
				int remainingMovement = movement - getMovementCost(x-1, y, battlefield);
				if (remainingMovement >= 0){
					GetMovementOptionsCore(x-1, y, allUnits, enemies, movingUnit, battlefield, remainingMovement, options);		
				}
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
	
	private static boolean isVisited(int x, int y, HashSet<Tile> visited){
		boolean isVisited = false;
		for(Tile tile: visited){
			if(tile.x == x && tile.y == y)
				isVisited = true;
		}
		return isVisited;
	}
	
	private static int getMovementCost(int x, int y, List<List<Tile>> battlefield){
		return battlefield.get(y).get(x).movementCost;
	}
}
