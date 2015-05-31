package bunzosteele.heroesemblem.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bunzosteele.heroesemblem.model.Battlefield.BattlefieldGenerator;
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
		int battlefieldId = random.nextInt(roundsSurvived + 6) - 6;
		battlefield = BattlefieldGenerator.GenerateBattlefield(battlefieldId);
		difficulty = roundsSurvived - battlefieldId;
	}
}
