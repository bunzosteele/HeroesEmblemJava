package bunzosteele.heroesemblem.model;

import java.util.ArrayList;
import java.util.List;

import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitGenerator;

public class ShopState {
	public List<Unit> stock;
	public List<Unit> roster;
	public Unit selected;
	public int gold;
	
	public ShopState(){
		stock = UnitGenerator.GenerateStock();
		roster = new ArrayList<Unit>();
		selected = null;
		gold = 5000;
	}
	
	public ShopState(int gold, List<Unit> roster){
		stock = UnitGenerator.GenerateStock();
		this.roster = roster;
		selected = null;
		this.gold = gold;
	}
}
