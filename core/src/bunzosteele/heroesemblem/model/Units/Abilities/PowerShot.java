package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.HashSet;
import java.util.List;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.graphics.Color;

public class PowerShot extends Ability {
	public PowerShot(){
		displayName = "PowerShot";
		isDaily = true;
		isActive = true;
		isTargeted = true;
		abilityColor = new Color(1f, 0f, 0f, .5f);
	}
	
	@Override
	public boolean CanUse(BattleState state, Unit originUnit){
		if(originUnit.hasAttacked)
			return false;
		
		if(exhausted)
			return false;
		
		for(Tile tile : GetTargetTiles(state, originUnit)){
			for(Unit unit : state.enemies){
				if(unit.x == tile.x && unit.y == tile.y)
					return true;
			}
		}
		return false;
	}
	
	@Override
	public HashSet<Tile> GetTargetTiles(BattleState state, Unit originUnit){
		HashSet<Tile> targets = new HashSet<Tile>();
		int x = originUnit.x;
		int y = originUnit.y -1;
		int originAltitude = state.battlefield.get(originUnit.y).get(originUnit.x).altitude;
		while(isInBounds(x, y, state.battlefield)){
			targets.add(state.battlefield.get(y).get(x));
			if(isHigherAltitude(originAltitude, x, y, state.battlefield))
				break;
			y--;
		}
		
		x = originUnit.x + 1;
		y = originUnit.y;
		while(isInBounds(x, y, state.battlefield)){
			targets.add(state.battlefield.get(y).get(x));
			if(isHigherAltitude(originAltitude, x, y, state.battlefield))
				break;
			x++;
		}
		
		x = originUnit.x;
		y = originUnit.y + 1;
		while(isInBounds(x, y, state.battlefield)){
			targets.add(state.battlefield.get(y).get(x));
			if(isHigherAltitude(originAltitude, x, y, state.battlefield))
				break;
			y++;
		}
		
		x = originUnit.x - 1;
		y = originUnit.y;
		while(isInBounds(x, y, state.battlefield)){
			targets.add(state.battlefield.get(y).get(x));
			if(isHigherAltitude(originAltitude, x, y, state.battlefield))
				break;
			x--;
		}

		return targets;
	}

	@Override
	public boolean Execute(BattleState state, Tile targetTile){
		HashSet<Tile> targetTiles = GetTargetedTiles(state, targetTile);
		boolean hit = false;
		for(Unit unit : GetTargetableUnits(state)){
			for(Tile tile : targetTiles){
				if(unit.x == tile.x && unit.y == tile.y){
					hit = true;
					unit.dealDamage(state.selected.attack);
					unit.startDamage();
				}
			}
		}
		if(hit){
			state.selected.startAttack();
		}
		return hit;
	}
	
	private HashSet<Tile> GetTargetedTiles(BattleState state, Tile targetTile){
		int originX = state.selected.x;
		int originY = state.selected.y;
		int originAltitude = state.battlefield.get(state.selected.y).get(state.selected.x).altitude;
		HashSet<Tile> targetTiles = new HashSet<Tile>();
		if(targetTile.x > originX){
			int x = originX + 1;
			int y = originY;
			while(isInBounds(x, y, state.battlefield)){
				targetTiles.add(state.battlefield.get(y).get(x));
				if(isHigherAltitude(originAltitude, x, y, state.battlefield))
					break;
				x++;
			
			}
		}else if(targetTile.x < originX){
			int x = originX - 1;
			int y = originY;
			while(isInBounds(x, y, state.battlefield)){
				targetTiles.add(state.battlefield.get(y).get(x));
				if(isHigherAltitude(originAltitude, x, y, state.battlefield))
					break;
				x--;
			}
		}else if(targetTile.y > originY){
			int x = originX;
			int y = originY + 1;
			while(isInBounds(x, y, state.battlefield)){
				targetTiles.add(state.battlefield.get(y).get(x));
				if(isHigherAltitude(originAltitude, x, y, state.battlefield))
					break;
				y++;
			}
		}else if(targetTile.y < originY){
			int x = originX;
			int y = originY - 1;
			while(isInBounds(x, y, state.battlefield)){
				targetTiles.add(state.battlefield.get(y).get(x));
				if(isHigherAltitude(originAltitude, x, y, state.battlefield))
					break;
				y--;
			}
		}
		return targetTiles;
	}
	
	private boolean isInBounds(int x, int y, List<List<Tile>> battlefield){
		int height = battlefield.size();
		int width = battlefield.get(0).size();
		
		return x >= 0 && x < width && y >= 0 && y < height;
	}
	
	private boolean isHigherAltitude(int originAltitude, int x, int y, List<List<Tile>> battlefield){
		return originAltitude < battlefield.get(y).get(x).altitude;
	}
	
	private List<Unit> GetTargetableUnits(BattleState state){
		return state.AllUnits();
	}
}
