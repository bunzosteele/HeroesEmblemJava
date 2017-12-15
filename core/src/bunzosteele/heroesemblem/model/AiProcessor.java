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
	
	public void MakeMove(float volume){
		List<Unit> unitsToMove = new ArrayList<Unit>();
		for(Unit unit: state.enemies){
			if(!unit.hasMoved){
				Tile desiredDestination = AiHelper.GetDestination(state, unit, 0);
				if(desiredDestination != null && (unit.x != desiredDestination.x || unit.y != desiredDestination.y))
					unitsToMove.add(unit);
			}
		}
		
		List<Unit> unitsToAttack = new ArrayList<Unit>();
		for(Unit unit: state.enemies){
			if(!unit.hasAttacked && (state.CanAttack(unit) || state.CanUseAbility(unit))){
				unitsToAttack.add(unit);
			}
		}
		
		List<Unit> moveableUnits = new ArrayList<Unit>();
		for(Unit unit: state.enemies){
			if(!unit.hasMoved){
				moveableUnits.add(unit);
			}
		}
		
		if(unitsToMove.size() == 0){
			if(unitsToAttack.size() == 0){
				this.state.EndTurn();
				return;
			}else{
				state.selected = unitsToAttack.get(0);
				AiHelper.ExecuteAction(state, unitsToAttack.get(0), volume);
			}
		}else{			
			Unit highestPriorityUnit = unitsToMove.get(0);
			int highestScore = AiHelper.GetPriority(state, highestPriorityUnit);
			for(Unit unit : unitsToMove){
				int thisScore = AiHelper.GetPriority(state, unit);
				if (thisScore > highestScore){
					highestPriorityUnit = unit;
					highestScore = thisScore;
				}
			}
			
			Tile destination = AiHelper.GetDestination(state, highestPriorityUnit, 0);
			if(destination == null){
				destination = state.battlefield.get(highestPriorityUnit.y).get(highestPriorityUnit.x);
			}
			Unit conflictedUnit = GetConflictedUnit(destination, moveableUnits, highestPriorityUnit);
			if(conflictedUnit != null){
				List<Unit> backedUnits = new ArrayList<Unit>();
				backedUnits.add(highestPriorityUnit);
				Unit blockingUnit = ResolveConflict(backedUnits, conflictedUnit, moveableUnits);
				highestPriorityUnit = blockingUnit;
				destination = GetHighestPriorityDestination(GetNonConflictingDestinations(highestPriorityUnit, moveableUnits));
			}		
			if(destination == null){
				destination = state.battlefield.get(highestPriorityUnit.y).get(highestPriorityUnit.x);
			}		
			int conflictCount = 0;
			while(GetConflictedUnit(destination, state.AllUnits(), highestPriorityUnit) != null){
				conflictCount++;
				Tile nextDestination = AiHelper.GetDestination(state, highestPriorityUnit, conflictCount);
				if(nextDestination != null){
					destination = nextDestination;
				}else{
					destination = state.battlefield.get(highestPriorityUnit.y).get(highestPriorityUnit.x);
				}
			}
			
			if(!unitsToMove.contains(highestPriorityUnit)){
				if(!highestPriorityUnit.hasAttacked){
					state.selected = highestPriorityUnit;
					AiHelper.ExecuteAction(state, highestPriorityUnit, volume);	
				}
				if(!highestPriorityUnit.hasMoved){
					state.selected = highestPriorityUnit;
					AiHelper.ExecuteMove(destination, highestPriorityUnit);
				}
			}else{	
				if(!highestPriorityUnit.hasMoved){
					state.selected = highestPriorityUnit;
					AiHelper.ExecuteMove(destination, highestPriorityUnit);
				}
				if(!highestPriorityUnit.hasAttacked){
					state.selected = highestPriorityUnit;
					AiHelper.ExecuteAction(state, highestPriorityUnit, volume);
				}
			}
		}
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
		Tile destination = AiHelper.GetDestination(state, currentUnit, 0);
		Unit nextConflict = GetConflictedUnit(destination, allUnits, currentUnit);
		
		int conflictCount = 1;
		while(nextConflict != null && backedUnits.contains(nextConflict)){
			destination = AiHelper.GetDestination(state, currentUnit, conflictCount);
			nextConflict = GetConflictedUnit(destination, allUnits, currentUnit);
			conflictCount++;
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
