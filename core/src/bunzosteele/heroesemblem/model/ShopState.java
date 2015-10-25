package bunzosteele.heroesemblem.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitGenerator;

public class ShopState
{
	public List<Unit> stock;
	public List<Unit> roster;
	public Unit selected;
	public int gold;
	public int roundsSurvived;
	public Unit heroUnit;
	public HeroesEmblem game;

	public ShopState(HeroesEmblem game) throws IOException
	{
		this.stock = UnitGenerator.GenerateStock(game);
		this.game = game;
		this.roster = new ArrayList<Unit>();
		this.selected = null;
		this.gold = 5000;
	}

	public ShopState(final BattleState battleState) throws IOException
	{
		this.game = battleState.game;
		this.stock = UnitGenerator.GenerateStock(battleState.game);
		this.roster = battleState.roster;
		this.selected = null;
		int bonusGold = battleState.difficulty * 50;
		int totalLevel = 0;
		for(Unit unit : roster){
			totalLevel+= unit.level;
		}
		float avgLevel = (float) totalLevel / roster.size();
		bonusGold = (int) (bonusGold * avgLevel);
		this.gold = battleState.gold + bonusGold;
		this.roundsSurvived = battleState.roundsSurvived + 1;
		this.heroUnit = battleState.heroUnit;
	}
}
