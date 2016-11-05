package bunzosteele.heroesemblem.model.Units;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

import bunzosteele.heroesemblem.model.AiHelper;
import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.CombatHelper;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Abilities.Ability;
import bunzosteele.heroesemblem.model.Units.Abilities.Thrust;
import bunzosteele.heroesemblem.model.Units.Abilities.Vault;

public class Spearman extends Unit
{
	final static int maxIdleFrame = 7;
	final static int maxAttackFrame = 18;
	
	public Spearman(final int team, final String name, final int attack, final int defense, final int evasion, final int accuracy, final int movement, final int maximumHealth, final int maximumRange, final int minimumRange, final int ability, final int cost, final int id, final float gameSpeed, final boolean isMale, final String backStory) throws IOException
	{
		super(team, name, attack, defense, evasion, accuracy, movement, maximumHealth, maximumRange, minimumRange, cost, id, gameSpeed, isMale, backStory, maxIdleFrame, maxAttackFrame);
		this.type = UnitType.Spearman;
		if (ability == 1)
		{
			this.ability = new Thrust();
		} else if (ability == 2)
		{
			this.ability = new Vault();
		} else
		{
			this.ability = null;
		}
	}
	
	public Spearman(UnitDto unitDto) throws IOException{
		super(unitDto.team, unitDto.name, unitDto.attack, unitDto.defense, unitDto.evasion, unitDto.accuracy, unitDto.movement, unitDto.maximumHealth, unitDto.maximumRange, unitDto.minimumRange, unitDto.cost, unitDto.id, unitDto.animationSpeed, unitDto.isMale, unitDto.backStory, maxIdleFrame, maxAttackFrame, unitDto.x, unitDto.y, unitDto.level, unitDto.unitsKilled, unitDto.damageDealt, unitDto.currentHealth, unitDto.experience, unitDto.experienceNeeded, unitDto.distanceMoved, unitDto.hasMoved, unitDto.hasAttacked, unitDto.initialAttack, unitDto.initialDefense, unitDto.initialAccuracy, unitDto.initialEvasion, unitDto.initialMovement, unitDto.initialHealth);
		this.type = UnitType.Spearman;
		this.ability = Ability.GenerateAbilityByName(unitDto.ability, unitDto.isAbilityExhausted, unitDto.canUseAbility, unitDto.abilityTargets);
	}

	@Override
	public int GetTileScore(Tile tile, BattleState state)
	{
		int score = 0;
		int costToCombat = AiHelper.GetCostToCombat(tile, state, this);
		score += (1000 - costToCombat * 10);
		if(costToCombat == 0){
			HashSet<Unit> attackableUnits = CombatHelper.GetAttackableTargets(tile.x, tile.y, this, state);
			if(attackableUnits.size() > 0){
				score += 50;
				score += this.attack;
				for(Unit unit : attackableUnits){
					score += 10 - state.GetTileForUnit(unit).defenseModifier;
					if(unit.currentHealth + state.GetTileForUnit(unit).defenseModifier <= this.attack){
						score += 30;
					}					
				}
			}
		}
		
		HashSet<Unit> threateningUnits = AiHelper.GetUnitsThatCanAttackTile(state, tile);
		for(Unit unit : threateningUnits){
			if(this.currentHealth / (float) this.maximumHealth <= .3){
				score -= 15;
			}else{
				score -= 10;
			}
		}
		
		score += tile.defenseModifier * 5;
		score += tile.evasionModifier;
		score += tile.altitude * 2;

		return score;
	}
}
