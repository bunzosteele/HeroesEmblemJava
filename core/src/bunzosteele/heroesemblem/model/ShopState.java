package bunzosteele.heroesemblem.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.utils.Json;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.Battlefield.BattlefieldGenerator;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
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
	public int perksPurchased;
	public int rerollCount;
	public List<UnitDto> graveyard;
	public Unit heroUnit;
	public HeroesEmblem game;
	public boolean isInspecting = false;
	public boolean isShopkeeperPanelDisplayed = false;
	public int nextBattlefieldId;
	public List<List<Tile>> nextBattlefield;

	public ShopState(HeroesEmblem game) throws IOException
	{
		this.roster = new ArrayList<Unit>();
		this.stock = UnitGenerator.GenerateStock(this.roster, game, this.GetTrainingPerkLevel());
		this.game = game;
		this.selected = null;
		this.gold = 5000;
		this.graveyard = new ArrayList<UnitDto>();
		this.roundsSurvived = 0;
		this.perksPurchased = 0;
		final Random random = new Random();
		int maxMap = BattlefieldGenerator.DetectMaximumMap();
		int minMap = BattlefieldGenerator.DetectMinimumMap();	
		this.nextBattlefieldId = (random.nextInt((int) Math.pow(2, this.roundsSurvived) + Math.abs(minMap)) % (maxMap + Math.abs(minMap) + 1)) + minMap;
		this.nextBattlefield = BattlefieldGenerator.GenerateBattlefield(this.nextBattlefieldId);
		this.rerollCount = 0;
	}

	public ShopState(final BattleState battleState) throws IOException
	{
		this.game = battleState.game;
		this.roster = battleState.roster;
		this.stock = UnitGenerator.GenerateStock(this.roster, battleState.game, this.GetTrainingPerkLevel());
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
		this.perksPurchased = battleState.perksPurchased;
		final Random random = new Random();
		int maxMap = BattlefieldGenerator.DetectMaximumMap();
		int minMap = BattlefieldGenerator.DetectMinimumMap();	
		this.nextBattlefieldId = (random.nextInt((int) Math.pow(2, this.roundsSurvived) + Math.abs(minMap)) % (maxMap + Math.abs(minMap) + 1)) + minMap;
		this.nextBattlefield = BattlefieldGenerator.GenerateBattlefield(this.nextBattlefieldId);
		this.rerollCount = 0;
		if(perksPurchased > 5){
			for(Unit unit : roster){
				unit.giveExperience(25);
			}
		}
		if(perksPurchased > 4){
			Unit mostDamaged = roster.get(0);
			for(Unit unit : roster){
				if ((unit.maximumHealth - unit.currentHealth) > (mostDamaged.maximumHealth - mostDamaged.currentHealth)){
					mostDamaged = unit;
				}
			}
			mostDamaged.currentHealth = mostDamaged.maximumHealth;
		}
	}
	
	public void BuyUnit() throws IOException{
		this.roster.add(this.selected);
		if(this.roster.size() == 1){
			this.heroUnit = this.selected;
		}
		String action = "" + this.roundsSurvived;
		UnitDto unitDto = new UnitDto();
		unitDto.type = this.selected.type.toString();
		unitDto.attack = this.selected.attack;
		unitDto.defense = this.selected.defense;
		unitDto.evasion = this.selected.evasion;
		unitDto.accuracy = this.selected.accuracy;
		unitDto.movement = this.selected.movement;
		unitDto.maximumHealth = this.selected.maximumHealth;
		if(this.selected.ability == null){
			unitDto.ability = "None";
		}else{
			unitDto.ability = this.selected.ability.displayName;
		}
		Json json = new Json();
		String parsedUnit = json.toJson(unitDto);
		this.game.gameServicesController.RecordAnalyticsEvent("UnitPurchased", action, parsedUnit, this.selected.cost);
		this.gold -= this.selected.cost;
		this.selected = null;
		this.isInspecting = false;
		this.isShopkeeperPanelDisplayed = false;
		this.stock = UnitGenerator.GenerateStock(this.roster, this.game, this.GetTrainingPerkLevel());
	}
	
	public boolean CanBuy(){
		if(this.roster.size() == 8 && (this.gold <= GetNextPerkCost() || this.perksPurchased > this.roundsSurvived))
			return false;
		
		boolean canBuy = this.gold >= GetNextPerkCost() || (this.perksPurchased > 2 && this.gold >= GetRerollCost());
		for(Unit unit : this.stock){
			if (unit.cost <= this.gold && this.roster.size() < 8)
				canBuy = true;
		}
		return canBuy;
	}
	
	public boolean CanBuyPerk(){
		return perksPurchased <= roundsSurvived && this.gold >= GetNextPerkCost();
	}
	
	public int GetNextPerkCost(){
		switch(this.perksPurchased){
			case 0: return 100;
			case 1: return 250;
			case 2: return 500;
			case 3: return 750;
			case 4: return 1000;
			case 5: return 1500;
			default: return 2500 * (this.perksPurchased - 5);		
		}		
	}
	
	public int GetTrainingPerkLevel(){
		int trainingLevel = this.perksPurchased - 6;
		if(trainingLevel < 0)
			trainingLevel = 0;
		return trainingLevel;
	}
	
	public void BuyPerk() throws IOException{
		this.gold -= GetNextPerkCost();
		this.game.gameServicesController.RecordAnalyticsEvent("PerkPurchased", "Perk:" + this.perksPurchased, "Round:" + this.roundsSurvived, GetNextPerkCost());
		this.perksPurchased++;
		if(GetTrainingPerkLevel() > 0)
			this.stock = UnitGenerator.GenerateStock(this.roster, this.game, this.GetTrainingPerkLevel());
	}
	
	public int GetRerollCost(){
		if(rerollCount == 0)
			return 0;
		return rerollCount * roundsSurvived * 100;
	}
	
	public void Reroll() throws IOException{
		this.gold -= this.GetRerollCost();
		this.rerollCount++;
		this.stock = UnitGenerator.GenerateStock(this.roster, this.game, this.GetTrainingPerkLevel());
	}
}
