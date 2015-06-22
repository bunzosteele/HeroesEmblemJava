package bunzosteele.heroesemblem.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

public final class AiHelper
{
	public static void ExecuteMove(Tile destination, Unit unit){
		unit.x = destination.x;
		unit.y = destination.y;
		unit.hasMoved = true;
	}
	
	public static void ExecuteAction(Unit unit){
		
	}
	
	public static Map<Tile, Integer> GetMovementOptions(BattleState state, Unit unit){
		Map<Tile, Integer> optionsWithScore = new HashMap<Tile, Integer>();
		List<Unit> unmovedAllies = new ArrayList<Unit>();
		for(Unit ally : state.enemies){
			if(!ally.hasMoved){
				unmovedAllies.add(ally);
			}
		}	
		HashSet<Tile> options = MovementHelper.GetMovementOptions(state, unit, unmovedAllies);
		for(Tile tile : options){
			int score = unit.GetTileScore(tile, state);
			optionsWithScore.put(tile, score);
		}
		return optionsWithScore;
	}
	
	public static Tile GetDesiredDestination(BattleState state, Unit unit){
		Map<Tile, Integer> options = GetMovementOptions(state, unit);
		Tile destination = null;
		for(Tile tile : options.keySet()){
			if(destination == null || options.get(tile) > options.get(destination))
				destination = tile;
		}
		return destination;
	}
	
	public static Map<Unit, Integer> GetActionOptions(BattleState state, Unit unit){
		return null;
	}
	
	public static int GetPriority(BattleState state, Unit unit){
		Map<Tile, Integer> options = GetMovementOptions(state, unit);
		int score = -1;
		for(Tile tile : options.keySet()){
			if(options.get(tile) > score)
				score = options.get(tile);
		}
		return score;
	}
	
	public static int GetCostToCombat(Tile tile, BattleState state, Unit unit){
		Map<Tile, Integer> options = new HashMap<Tile, Integer>();
		List<Tile> visited = new ArrayList<Tile>();
		return GetCostToCombatCore(tile.x, tile.y, state, unit, 0, options, visited);
	}
	
	private static int GetCostToCombatCore(int x, int y, BattleState state, Unit unit, int cost, Map<Tile, Integer> options, List<Tile> visited){
		visited.add(state.battlefield.get(y).get(x));
		if(CombatHelper.CanAttackFromPosition(x, y, unit, state)){
			return cost;
		}
		
		if (IsInBounds(x, y - 1, state.battlefield))
		{
			Tile option = state.battlefield.get(y - 1).get(x);
			if(!visited.contains(option) && option.movementCost <= unit.movement)
				options.put(option, option.movementCost + cost);
		}
		if (IsInBounds(x + 1, y, state.battlefield))
		{
			Tile option = state.battlefield.get(y).get(x + 1);
			if(!visited.contains(option) && option.movementCost <= unit.movement)
				options.put(option, option.movementCost + cost);
		}
		if (IsInBounds(x, y + 1, state.battlefield))
		{
			Tile option = state.battlefield.get(y + 1).get(x);
			if(!visited.contains(option) && option.movementCost <= unit.movement)
				options.put(option, option.movementCost + cost);
		}
		if (IsInBounds(x - 1, y, state.battlefield))
		{
			Tile option = state.battlefield.get(y).get(x - 1);
			if(!visited.contains(option) && option.movementCost <= unit.movement)
				options.put(option, option.movementCost + cost);
		}
		Tile nextStep = GetCheapestOption(options);
		int nextCost = options.get(nextStep);
		options.remove(nextStep);
		return GetCostToCombatCore(nextStep.x, nextStep.y, state, unit, nextCost, options, visited);		
	}
	
	private static Tile GetCheapestOption(Map<Tile, Integer> options){
		Tile cheapestOption = null;
		for(Tile tile : options.keySet()){
			if(cheapestOption == null || options.get(tile) < options.get(cheapestOption)){
				cheapestOption = tile;
			}
		}
		return cheapestOption;
	}
	
	private static boolean IsInBounds(final int x, final int y, final List<List<Tile>> battlefield)
	{
		final int height = battlefield.size();
		final int width = battlefield.get(0).size();

		return (x >= 0) && (x < width) && (y >= 0) && (y < height);
	}
}
