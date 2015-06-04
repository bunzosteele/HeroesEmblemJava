package bunzosteele.heroesemblem.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bunzosteele.heroesemblem.model.Battlefield.BattlefieldGenerator;
import bunzosteele.heroesemblem.model.Battlefield.Spawn;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitGenerator;

public class BattleState extends State {
	public List<Unit> enemies;
	public List<Unit> roster;
	public Unit selected;
	public int gold;
	public int roundsSurvived;
	public int difficulty;
	public List<List<Tile>> battlefield;
	
	public BattleState(ShopState shopState) throws IOException{
		this.roster = shopState.roster;
		selected = null;
		this.gold = shopState.gold;
		Random random = new Random();
		//int battlefieldId = random.nextInt(difficulty + 6) -6;
		int battlefieldId = -6;
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
