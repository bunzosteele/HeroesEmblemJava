package bunzosteele.heroesemblem.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitDto;
import bunzosteele.heroesemblem.model.Units.UnitGenerator;

public class ShopState
{
	public List<Unit> stock;
	public List<Unit> roster;
	public Unit selected;
	public int gold;
	public int roundsSurvived;
	public List<UnitDto> graveyard;
	public Unit heroUnit;
	public HeroesEmblem game;

	public ShopState(HeroesEmblem game) throws IOException
	{
		this.roster = new ArrayList<Unit>();
		this.stock = UnitGenerator.GenerateStock(this.roster, game);
		this.game = game;
		this.selected = null;
		this.gold = 5000;
		this.graveyard = new ArrayList<UnitDto>();
	}

	public ShopState(final BattleState battleState) throws IOException
	{
		this.game = battleState.game;
		this.roster = battleState.roster;
		this.stock = UnitGenerator.GenerateStock(this.roster, battleState.game);
		this.selected = null;
		int bonusGold = 300 + battleState.difficulty * 25;
		int totalLevel = 0;
		for(Unit unit : roster){
			totalLevel+= unit.level;
		}
		float avgLevel = (float) totalLevel / roster.size();
		bonusGold = (int) (bonusGold * avgLevel);
		this.gold = battleState.gold + bonusGold;
		this.roundsSurvived = battleState.roundsSurvived + 1;
		this.graveyard = battleState.graveyard;
	}
}
