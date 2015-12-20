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
	
	public static void ExecuteAction(BattleState state, Unit unit, float volume){
		if(unit.ability != null && unit.ability.CanUse(state, unit)){
			UseAbility(state, unit, volume);
		}else{
			Attack(state, unit, volume);
		}
		unit.hasAttacked = true;
		for(Unit enemy : state.enemies){
			enemy.movementOptions = null;
		}
	}

	public static HashSet<Unit> GetUnitsThatCanAttackTile(BattleState state, Tile tile){
		List<Unit> playerUnits = state.roster;
		HashSet<Unit> threats = new HashSet<Unit>();
		for(Unit playerUnit : playerUnits){
			if(playerUnit.x >= 0 && playerUnit.y >= 0){
				if(CombatHelper.GetAttackableTargets(playerUnit.x, playerUnit.y, playerUnit, state).contains(tile)){
					threats.add(playerUnit);
				}
			}
		}
		return threats;
	}
	
	private static void UseAbility(BattleState state, Unit unit, float volume){
		List<Tile> targetTiles = new ArrayList<Tile>(unit.ability.GetTargetTiles(state, unit));
		List<Unit> targetableUnits = unit.ability.GetTargetableUnits(state);
		List<Unit> validTargets = new ArrayList<Unit>();
		for(Unit potentialTarget : targetableUnits){
			if(targetTiles.contains(state.GetTileForUnit(potentialTarget)))
				validTargets.add(potentialTarget);
		}
		Unit weakestTarget = null;
		for(Unit target : validTargets){
			if(weakestTarget == null || (weakestTarget.maximumHealth - weakestTarget.currentHealth) < (target.maximumHealth - target.currentHealth)){
				weakestTarget = target;
			}
		}
		unit.ability.Execute(state, unit, state.GetTileForUnit(weakestTarget));
		state.victimTile = state.GetTileForUnit(weakestTarget);
		unit.ability.PlaySound(volume);
		unit.ability.exhausted = true;
	}
	
	private static void Attack(BattleState state, Unit unit, float volume){
		HashSet<Unit> targets = CombatHelper.GetAttackableTargets(unit.x, unit.y, unit, state);
		if(targets.size() > 0){
			Unit target = SelectAttackTarget(state, unit, targets);
			unit.startAttack();
			state.victimTile = state.GetTileForUnit(target);
			if (CombatHelper.Attack(unit, target, state.battlefield))
			{
				Unit.hitSound.play(volume);
				target.startDamage();
				if(target.checkDeath()){
					target.killUnit(unit, state);
					if(target.team == 0)
						state.SaveGraveyard(target);
				}
			} else
			{
				Unit.missSound.play(volume);
				target.startMiss();
			}
		}
	}
	
	private static Unit SelectAttackTarget(BattleState state, Unit attacker, HashSet<Unit> targets){
		List<Unit> killableUnits = new ArrayList<Unit>();
		for(Unit target : targets){
			Tile targetTile = state.battlefield.get(target.y).get(target.x);
			if(target.currentHealth + targetTile.defenseModifier < attacker.attack){
				killableUnits.add(target);
			}
		}
		Unit finalTarget = null;
		for(Unit killableUnit : killableUnits){
			Tile targetTile = state.battlefield.get(killableUnit.y).get(killableUnit.x);
			if(finalTarget == null || (killableUnit.evasion + targetTile.evasionModifier) < (finalTarget.evasion + state.battlefield.get(finalTarget.y).get(finalTarget.x).evasionModifier)){
				finalTarget = killableUnit;
			}
		}
		
		if(finalTarget == null){
			for(Unit target : targets){
				if(finalTarget == null || finalTarget.currentHealth > target.currentHealth){
					finalTarget = target;
				}
			}
		}
		
		return finalTarget;
	}

	public static Map<Tile, Integer> GetMovementOptions(BattleState state, Unit unit){
		if(unit.movementOptions == null){
			Map<Tile, Integer> optionsWithScore = new HashMap<Tile, Integer>();
			HashSet<Tile> options = MovementHelper.GetMovementOptions(state, unit);
			for(Tile tile : options){
				int score = unit.GetTileScore(tile, state);
				optionsWithScore.put(tile, score);
			}
			unit.movementOptions = optionsWithScore;
			return optionsWithScore;
		}else{
			return unit.movementOptions;
		}
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
		if(options.size() <= 0)
			return -1;
		Tile nextStep = GetCheapestOption(options);
		int nextCost = options.get(nextStep);
		options.remove(nextStep);
		return GetCostToCombatCore(nextStep.x, nextStep.y, state, unit, nextCost, options, visited);		
	}
	
	public static int GetCostToHeal(Tile tile, BattleState state, Unit unit){
		Map<Tile, Integer> options = new HashMap<Tile, Integer>();
		options.put(state.battlefield.get(tile.y).get(tile.x), 0);
		List<Tile> visited = new ArrayList<Tile>();
		return GetCostToHealCore(tile.x, tile.y, state, unit, 0, options, visited);
	}
	
	private static int GetCostToHealCore(int x, int y, BattleState state, Unit unit, int cost, Map<Tile, Integer> options, List<Tile> visited){
		visited.add(state.battlefield.get(y).get(x));
		if(unit.ability.CouldUse(state, unit, x, y)){
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
		if(options.size() <= 0)
			return -1;
		Tile nextStep = GetCheapestOption(options);
		int nextCost = options.get(nextStep);
		options.remove(nextStep);
		return GetCostToHealCore(nextStep.x, nextStep.y, state, unit, nextCost, options, visited);		
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
