package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.HashSet;
import java.util.List;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.graphics.Color;

public class Joust extends Ability {
	public Joust(){
		displayName = "Joust";
		isDaily = false;
		isActive = false;
		isTargeted = false;
	}
		
	@Override
	public int AttackModifier(Unit attacker){
		return attacker.distanceMoved;
	}
}
