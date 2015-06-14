package bunzosteele.heroesemblem.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import bunzosteele.heroesemblem.model.Battlefield.BattlefieldGenerator;
import bunzosteele.heroesemblem.model.Battlefield.Spawn;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitGenerator;

public class BattleState{
	public List<Unit> enemies;
	public List<Unit> roster;
	public Unit selected;
	public int gold;
	public int roundsSurvived;
	public int difficulty;
	public List<List<Tile>> battlefield;
	public boolean isMoving;
	public boolean isAttacking;
	public boolean isUsingAbility;
	public int currentPlayer;
	public int turnCount;
	
	public BattleState(ShopState shopState) throws IOException{
		turnCount = 1;
		currentPlayer = 0;
		roundsSurvived = shopState.roundsSurvived;
		difficulty = (int) Math.pow(2, roundsSurvived);
		this.roster = shopState.roster;
		selected = null;
		this.gold = shopState.gold;
		Random random = new Random();
		int battlefieldId = random.nextInt(difficulty + 6) - 6;
		battlefield = BattlefieldGenerator.GenerateBattlefield(battlefieldId);
		List<Spawn> spawns = BattlefieldGenerator.GenerateSpawns(battlefieldId);
		List<Spawn> playerSpawns = new ArrayList<Spawn>();
		List<Spawn> enemySpawns = new ArrayList<Spawn>();
		for(Spawn spawn : spawns){
			if(spawn.isPlayer){
				playerSpawns.add(spawn);
			}else{
				enemySpawns.add(spawn);
			}
		}
		enemies = UnitGenerator.GenerateEnemies(enemySpawns.size(), difficulty - battlefieldId);
		SpawnUnits(roster, playerSpawns);
		SpawnUnits(enemies, enemySpawns);
	}
	
	public boolean CanMove(){
		if(this.selected != null && this.selected.team == 0){
			if(!this.selected.hasMoved){
				return true;
			}
		}
		return false;
	}
	
	public boolean CanAttack(Unit unit){
		if(unit != null && unit.team == 0){
			if(!unit.hasAttacked){
				for(Tile tile: CombatHelper.GetAttackOptions(this, unit)){
					for(Unit enemy : enemies){
						if(enemy.x == tile.x && enemy.y == tile.y){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean CanUseAbility(Unit unit){
		if(unit != null && unit.ability != null){
			return unit.ability.CanUse(this, unit);
		}else{
			return false;
		}
	}
	
	public List<Unit> AllUnits(){
		List<Unit> allUnits = new ArrayList<Unit>();
		for(Unit unit : roster){
			allUnits.add(unit);
		}
		for(Unit unit : enemies){
			allUnits.add(unit);
		}	
		return allUnits;
	}
	
	public void EndTurn(){
		this.isAttacking = false;
		this.isMoving = false;
		this.isUsingAbility = false;
		this.selected = null;
		this.currentPlayer = (this.currentPlayer + 1) % 2;
		if(currentPlayer == 0){
			this.turnCount++;
		}
		for(Unit unit : AllUnits()){
			unit.hasMoved = false;
			unit.hasAttacked = false;
			unit.distanceMoved = 0;
		}
	}
	
	public void EndBattle(){
		for(Unit unit : AllUnits()){
			if(unit.ability != null){
				unit.ability.exhausted = false;					
			}
			unit.isAttacking = false;
			unit.isDying = false;
			unit.isGettingExperience = false;
			unit.isHealing = false;
			unit.isMissed = false;
			unit.isTakingDamage = false;
			unit.experienceFrame = 1;
			unit.damageFrame = 1;
			unit.healFrame = 1;
			unit.attackFrame = 1;
			unit.missedFrame = 1;
			unit.deathFrame = 10;
			unit.distanceMoved = 0;
			unit.hasAttacked = false;
			unit.hasMoved = false;
			unit.damageDisplay = "";
		}
		this.selected = null;
		this.isAttacking = false;
		this.isMoving = false;
		this.isUsingAbility = false;		
	}
	
	public void CleanBoard(){
		for(int i = roster.size()-1; i >= 0; i--){
			if(roster.get(i).isDying){
				if(roster.get(i).deathFrame < 1){
					roster.remove(i);
				}	
			}
		}
		for(int i = enemies.size()-1; i >= 0; i--){
			if(enemies.get(i).isDying){
				if(enemies.get(i).deathFrame < 1){
					enemies.remove(i);
				}	
			}
		}
	}
	
	public boolean IsTapped(Unit unit){
		if(!unit.hasMoved)
			return false;
		
		if(CanAttack(unit) && !unit.hasAttacked)
			return false;
		
		if(CanUseAbility(unit))
			return false;
		
		return true;
	}
	
	private void SpawnUnits(List<Unit> units, List<Spawn> spawns){
		Random random = new Random();
		for(Unit unit : units){
			int spawnRoll = random.nextInt(spawns.size());
			Spawn spawn = spawns.get(spawnRoll);
			unit.x = spawn.x;
			unit.y = spawn.y;
			spawns.remove(spawn);
		}
	}
}
