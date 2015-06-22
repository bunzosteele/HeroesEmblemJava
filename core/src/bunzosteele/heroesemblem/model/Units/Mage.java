package bunzosteele.heroesemblem.model.Units;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

import bunzosteele.heroesemblem.model.AiHelper;
import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.CombatHelper;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Abilities.ChainLightning;
import bunzosteele.heroesemblem.model.Units.Abilities.Teleport;

public class Mage extends Unit
{
	public Mage(final int team, final String name, final int attack, final int defense, final int evasion, final int accuracy, final int movement, final int maximumHealth, final int maximumRange, final int minimumRange, final int ability, final int cost, final int id) throws IOException
	{
		super(team, name, attack, defense, evasion, accuracy, movement, maximumHealth, maximumRange, minimumRange, cost, id);
		this.type = UnitType.Mage;
		if (ability == 1)
		{
			this.ability = new ChainLightning();
		} else if (ability == 2)
		{
			this.ability = new Teleport();
		} else
		{
			this.ability = null;
		}
	}

	@Override
	public int GetTileScore(Tile tile, BattleState state)
	{
		int score = 0;
		int costToCombat = AiHelper.GetCostToCombat(tile, state, this);
		if(costToCombat == 0){
			HashSet<Unit> attackableUnits = CombatHelper.GetAttackableTargets(tile.x, tile.y, this, state);
			if(attackableUnits.size() > 0){
				score += 50;
				score += this.attack;
				for(Unit unit : attackableUnits){
					if(this.currentHealth / (float) this.maximumHealth <= .5){
						score -= 10;
					}else{
						score -= 5;
					}
					if(unit.currentHealth <= this.attack){
						score += 25;
					}					
				}
			}
		}
		score += (100 - costToCombat);
		return score;
	}
	@Override
	public HashSet<Unit> GetTargets(BattleState state)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int GetTargetScore(Unit target, BattleState state)
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
