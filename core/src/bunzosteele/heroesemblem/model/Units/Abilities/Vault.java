package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.HashSet;
import java.util.List;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.graphics.Color;

public class Vault extends Ability {
	public Vault(){
		displayName = "Vault";
		isDaily = false;
		isActive = true;
		isTargeted = true;
		abilityColor = new Color(0f, 0f, 1f, .5f);
	}
	
	@Override
	public boolean CanUse(BattleState state, Unit originUnit){
		return false;
	}
	
	@Override
	public HashSet<Tile> GetTargetTiles(BattleState state, Unit originUnit){
		return null;
	}
	
	@Override
	public boolean Execute(BattleState state, Tile targetTile){
		return false;
	}
}
