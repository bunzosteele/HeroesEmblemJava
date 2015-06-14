package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.HashSet;
import java.util.List;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.graphics.Color;

public class Teleport extends Ability{
	public Teleport(){
		displayName = "Teleport";
		isDaily = true;
		isActive = true;
		isTargeted = true;
		abilityColor = new Color(.5f, 0f, .5f, .5f);
		isMultiInput = true;
	}
	
	@Override
	public boolean CanUse(BattleState state, Unit originUnit){
		if(originUnit.hasAttacked)
			return false;
		
		if(exhausted)
			return false;
		
		return true;
	}
	
	@Override
	public HashSet<Tile> GetTargetTiles(BattleState state, Unit originUnit){
		HashSet<Tile> targets = new HashSet<Tile>();
		if(target == null){
			for(Unit unit : state.AllUnits()){
				targets.add(state.battlefield.get(unit.y).get(unit.x));
			}
		}else{
			for(List<Tile> row : state.battlefield){
				for(Tile tile : row){
					if(tile.movementCost <= target.movement){
						targets.add(tile);
					}
				}
			}
			for(Unit unit : state.AllUnits()){
				Tile tile = state.battlefield.get(unit.y).get(unit.x);
				targets.remove(tile);
			}
		}
		return targets;
	}

	@Override
	public boolean Execute(BattleState state, Tile targetTile){
		if(target == null){
			for(Unit unit : GetTargetableUnits(state)){
				if(targetTile.x == unit.x && targetTile.y == unit.y){
					target = unit;
				}
			}
			return false;
		}else{
			if(!GetTargetTiles(state, state.selected).contains(targetTile)){
				target = null;
				return false;			
			}else{
				state.selected.startAttack();
				target.x = targetTile.x;
				target.y = targetTile.y;
				return true;
			}
		}
	}
	
	private List<Unit> GetTargetableUnits(BattleState state){
		return state.AllUnits();
	}
}
