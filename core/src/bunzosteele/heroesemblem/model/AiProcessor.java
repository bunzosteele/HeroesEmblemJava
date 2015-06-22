package bunzosteele.heroesemblem.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

public class AiProcessor
{
	BattleState state;
	public AiProcessor(BattleState state){
		this.state = state;
	}
	
	public void MakeMove(){
		List<Unit> units = new ArrayList<Unit>();
		for(Unit unit: state.enemies){
			if(!state.IsTapped(unit)){
				units.add(unit);
			}
		}
		
		if(units.size() == 0){
			this.state.EndTurn();
			return;
		}
		
		Unit highestPriorityUnit = units.get(0);
		int highestScore = AiHelper.GetPriority(state, highestPriorityUnit);
		for(Unit unit : units){
			int thisScore = AiHelper.GetPriority(state, unit);
			if (thisScore > highestScore){
				highestPriorityUnit = unit;
				highestScore = thisScore;
			}
		}
		
		Tile destination = AiHelper.GetDesiredDestination(state, highestPriorityUnit);
		Unit conflictedUnit = GetConflictedUnit(destination, units, highestPriorityUnit);
		if(conflictedUnit != null){
			List<Unit> backedUnits = new ArrayList<Unit>();
			backedUnits.add(highestPriorityUnit);
			Unit blockingUnit = ResolveConflict(backedUnits, conflictedUnit, units);
			highestPriorityUnit = blockingUnit;
			destination = GetHighestPriorityDestination(GetNonConflictingDestinations(highestPriorityUnit, units));
		}
		AiHelper.ExecuteMove(destination, highestPriorityUnit);
		AiHelper.ExecuteAction(highestPriorityUnit);
	}
	
	private Tile GetHighestPriorityDestination(Map<Tile, Integer> options){
		Tile destination = null;
		for(Tile tile : options.keySet()){
			if(destination == null || options.get(tile) > options.get(destination))
				destination = tile;
		}
		return destination;
	}
	
	private Map<Tile, Integer> GetNonConflictingDestinations(Unit currentUnit, List<Unit> allUnits){
		Map<Tile, Integer> movementOptions = AiHelper.GetMovementOptions(state, currentUnit);
		for(Unit unit : allUnits){
			Tile tileToRemove = null;
			for(Tile tile : movementOptions.keySet()){
				if(unit.x == tile.x && unit.y == tile.y){
					tileToRemove = tile;
				}
			}
			if(tileToRemove != null)
				movementOptions.remove(tileToRemove);
		}
		return movementOptions;
	}
	
	private Unit ResolveConflict(List<Unit> backedUnits, Unit currentUnit, List<Unit> allUnits){
		Tile destination = AiHelper.GetDesiredDestination(state, currentUnit);
		Unit nextConflict = GetConflictedUnit(destination, allUnits, currentUnit);
		
		while(nextConflict != null && backedUnits.contains(nextConflict)){
			currentUnit.blockedSpaces.add(destination);
			destination = AiHelper.GetDesiredDestination(state, currentUnit);
			nextConflict = GetConflictedUnit(destination, allUnits, currentUnit);
		}
			
		if(nextConflict != null){
			Map<Tile, Integer> options = GetNonConflictingDestinations(currentUnit, allUnits);
			if(options.size() > 0){
				return currentUnit;
			}else{
				backedUnits.add(currentUnit);
				return ResolveConflict(backedUnits, nextConflict, allUnits);
			}
		}else{
			return currentUnit;
		}
	}
	
	private Unit GetConflictedUnit(Tile tile, List<Unit> units, Unit currentUnit){
		for(Unit unit : units){
			if (unit.x == tile.x && unit.y == tile.y && !unit.isEquivalentTo(currentUnit)){
				return unit;
			}
		}	
		return null;
	}
}
