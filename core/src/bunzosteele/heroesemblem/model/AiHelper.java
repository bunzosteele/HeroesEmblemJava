package bunzosteele.heroesemblem.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.CombatHelper;

public final class AiHelper
{
	public static void ExecuteMove(Tile destination, Unit unit){
		unit.x = destination.x;
		unit.y = destination.y;
		unit.hasMoved = true;
	}
	
	public static void ExecuteAction(BattleState state, Unit unit){
		if(unit.ability != null && unit.ability.CanUse(state, unit)){
			UseAbility(state, unit);
		}else{
			Attack(state, unit);
		}
		unit.hasAttacked = true;
	}

	public static HashSet<Unit> GetUnitsThatCanAttackTile(BattleState state, Tile tile){
		List<Unit> playerUnits = state.roster;
		HashSet<Unit> threats = new HashSet<Unit>();
		for(Unit playerUnit : playerUnits){
			if(CombatHelper.GetAttackableTargets(playerUnit.x, playerUnit.y, playerUnit, state).contains(tile)){
				threats.add(playerUnit);
			}
		}
		return threats;
	}
	
	private static void UseAbility(BattleState state, Unit unit){
		List<Tile> targetTiles = new ArrayList<Tile>(unit.ability.GetTargetTiles(state, unit));
		List<Unit> targetableUnits = unit.ability.GetTargetableUnits(state);
		List<Unit> validTargets = new ArrayList<Unit>();
		for(Unit potentialTarget : targetableUnits){
			if(targetTiles.contains(state.GetTileForUnit(potentialTarget)))
				validTargets.add(potentialTarget);
		}
		validTargets.sort(Unit.UnitHeathComparator);
		unit.ability.Execute(state, unit, state.GetTileForUnit(validTargets.get(0)));
		unit.ability.exhausted = true;
	}
	
	private static void Attack(BattleState state, Unit unit){
		HashSet<Unit> targets = CombatHelper.GetAttackableTargets(unit.x, unit.y, unit, state);
		if(targets.size() > 0){
			Unit target = SelectAttackTarget(state, unit, targets);
			unit.startAttack();
			if (CombatHelper.Attack(unit, target, state.battlefield))
			{
				Unit.hitSound.play();
				target.startDamage();
				target.checkDeath(unit);
			} else
			{
				Unit.missSound.play();
				target.startMiss();
			}
		}
	}
	
	private static Unit SelectAttackTarget(BattleState state, Unit attacker, HashSet<Unit> targets){
		List<Unit> orderedTargets = new ArrayList<Unit>(targets);
		orderedTargets.sort(Unit.UnitHeathComparator);
		List<Unit> killableUnits = new ArrayList<Unit>();
		for(Unit target : orderedTargets){
			Tile targetTile = state.battlefield.get(target.y).get(target.x);
			if(target.currentHealth + targetTile.defenseModifier < attacker.attack){
				killableUnits.add(target);
			}
		}
		Unit finalTarget = null;
		for(Unit killableUnit : killableUnits){
			Tile targetTile = state.battlefield.get(killableUnit.y).get(killableUnit.x);
			if(finalTarget == null || (killableUnit.evasion + targetTile.accuracyModifier) < (finalTarget.evasion + state.battlefield.get(finalTarget.y).get(finalTarget.x).accuracyModifier)){
				finalTarget = killableUnit;
			}
		}
		
		if(finalTarget == null){
			finalTarget = orderedTargets.get(0);
		}
		
		return finalTarget;
	}

	public static Map<Tile, Integer> GetMovementOptions(BattleState state, Unit unit){
		Map<Tile, Integer> optionsWithScore = new HashMap<Tile, Integer>();
		HashSet<Tile> options = MovementHelper.GetMovementOptions(state, unit);
		for(Tile tile : options){
			int score = unit.GetTileScore(tile, state);
			optionsWithScore.put(tile, score);
		}
		return optionsWithScore;
	}
	
	public static Tile GetDestination(BattleState state, Unit unit, int priority){
		Map<Tile, Integer> options = GetMovementOptions(state, unit);
		for(int i = 0; i < priority; i++){
			options.remove(GetHighestValueTile(options));
		}
		return GetHighestValueTile(options);
	}
	
	private static Tile GetHighestValueTile(Map<Tile, Integer> options){
		Tile highestValueTile = null;
		for(Tile tile : options.keySet()){
			if(highestValueTile == null || options.get(tile) > options.get(highestValueTile))
				highestValueTile = tile;
		}
		return highestValueTile;
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
		options.put(state.battlefield.get(tile.y).get(tile.x), 0);
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