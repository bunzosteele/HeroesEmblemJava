package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.graphics.Color;

public class Block extends Ability {
	public Block(){
		displayName = "Block";
		isDaily = false;
		isActive = false;
		isTargeted = false;
	}
	
	@Override
	public boolean IsBlockingDamage(){
		Random rand = new Random();
		int roll = rand.nextInt(101);
		return roll > 90;
	}
}
