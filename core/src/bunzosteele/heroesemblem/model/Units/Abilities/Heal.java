package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.graphics.Color;

public class Heal extends Ability {
	public Heal(){
		displayName = "Heal";
		isDaily = false;
		isActive = true;
		isTargeted = true;
		abilityColor = new Color(0f, 1f, 0f, .5f);
	}
	
	@Override
	public boolean CanUse(BattleState state, Unit originUnit){
		if(originUnit.hasAttacked)
			return false;
		
		for(Tile tile : GetTargetTiles(state, originUnit)){
			for(Unit unit : state.roster){
				if(unit.x == tile.x && unit.y == tile.y && unit.currentHealth != unit.maximumHealth )
					return true;
			}
		}
		return false;
	}
	
	@Override
	public HashSet<Tile> GetTargetTiles(BattleState state, Unit originUnit){
		HashSet<Tile> targets = new HashSet<Tile>();
		if(isInBounds(originUnit.x, originUnit.y - 1, state.battlefield)){
			targets.add(state.battlefield.get(originUnit.y-1).get(originUnit.x));
		}
		if(isInBounds(originUnit.x + 1,originUnit.y, state.battlefield)){
			targets.add(state.battlefield.get(originUnit.y).get(originUnit.x+1));			
		}
		if(isInBounds(originUnit.x, originUnit.y + 1, state.battlefield)){
			targets.add(state.battlefield.get(originUnit.y+1).get(originUnit.x));			
		}
		if(isInBounds(originUnit.x - 1, originUnit.y, state.battlefield)){
			targets.add(state.battlefield.get(originUnit.y).get(originUnit.x-1));			
		}
		return targets;
	}

	@Override
	public boolean Execute(BattleState state, Tile targetTile){
		for(Unit unit : GetTargetableUnits(state)){
			if(unit.x == targetTile.x && unit.y == targetTile.y){
				state.selected.startAttack();
				int heal = state.selected.attack;
				Random random = new Random();
				int roll = random.nextInt(101);
				if (roll <= 10){
					heal -= 1;
				}else if(roll == 100){
					heal = heal *2;
				}else if (roll > 90){
					heal += 1;
				}
				
				if(heal < 0)
					heal = 0;
				
				if(heal > unit.maximumHealth - unit.currentHealth){
					heal = unit.maximumHealth - unit.currentHealth;
				}
			
				unit.healDamage(heal);
				unit.startHeal();
				return true;
			}
		}
		return false;
	}
		
	private boolean isInBounds(int x, int y, List<List<Tile>> battlefield){
		int height = battlefield.size();
		int width = battlefield.get(0).size();
		
		return x >= 0 && x < width && y >= 0 && y < height;
	}	

	private List<Unit> GetTargetableUnits(BattleState state){
		return state.roster;
	}
	
}
