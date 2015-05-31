package bunzosteele.heroesemblem.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitGenerator;

public class ShopState extends State {
	public List<Unit> stock;
	public List<Unit> roster;
	public Unit selected;
	public int gold;
	public int roundsSurvived;
	
	public ShopState() throws IOException{
		stock = UnitGenerator.GenerateStock();
		roster = new ArrayList<Unit>();
		selected = null;
		gold = 5000;
	}
	
	public ShopState(BattleState battleState) throws IOException{
		stock = UnitGenerator.GenerateStock();
		this.roster = battleState.roster;
		selected = null;
		this.gold = battleState.gold + battleState.difficulty * 50;
		roundsSurvived = battleState.roundsSurvived + 1;
		
	}
}
