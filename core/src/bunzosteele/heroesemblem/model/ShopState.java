package bunzosteele.heroesemblem.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitGenerator;

public class ShopState
{
	public List<Unit> stock;
	public List<Unit> roster;
	public Unit selected;
	public int gold;
	public int roundsSurvived;

	public ShopState() throws IOException
	{
		this.stock = UnitGenerator.GenerateStock();
		this.roster = new ArrayList<Unit>();
		this.selected = null;
		this.gold = 5000;
	}

	public ShopState(final BattleState battleState) throws IOException
	{
		this.stock = UnitGenerator.GenerateStock();
		this.roster = battleState.roster;
		this.selected = null;
		this.gold = battleState.gold + (battleState.difficulty * 50);
		this.roundsSurvived = battleState.roundsSurvived + 1;
	}
}
