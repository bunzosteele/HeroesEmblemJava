package bunzosteele.heroesemblem.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Battlefield.TileDto;
import bunzosteele.heroesemblem.model.Units.Archer;
import bunzosteele.heroesemblem.model.Units.Footman;
import bunzosteele.heroesemblem.model.Units.Knight;
import bunzosteele.heroesemblem.model.Units.Mage;
import bunzosteele.heroesemblem.model.Units.Priest;
import bunzosteele.heroesemblem.model.Units.Spearman;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitDto;
import bunzosteele.heroesemblem.model.Units.UnitType;
import bunzosteele.heroesemblem.view.BattleScreen;
import bunzosteele.heroesemblem.view.ShopScreen;

public class SaveManager
{
	public static void SaveGame(BattleState state){
		StateDto saveData = SaveManager.GetBattleStateDto(state);
		Json json = new Json();
		SaveManager.WriteSave(json.toJson(saveData));
	}

	public static void SaveGame(ShopState state){
		StateDto saveData = SaveManager.GetShopStateDto(state);
		Json json = new Json();
		SaveManager.WriteSave(json.toJson(saveData));
	}
	
	public static boolean LoadGame(HeroesEmblem game) throws IOException{
		StateDto stateDto = GetStateData();
		if(stateDto != null){
			if(stateDto.stateKind == 0){
				BattleState state = new BattleState(game, stateDto);
				EraseSaveData();
				game.setScreen(new BattleScreen(game, state));
			}else{
				ShopState state = new ShopState(game, stateDto);	
				EraseSaveData();
				game.setScreen(new ShopScreen(game, state));
			}
			return true;
		}
		return false;
	}
	
	public static StateDto GetStateData(){
		String saveData = ReadSaveData();
		if(saveData == "")
			return null;
		
		Json json = new Json();
		StateDto stateDto = json.fromJson(SaveManager.StateDto.class, saveData);
		return stateDto;
	}
	
	public static void EraseSaveData(){
		WriteSave("");
	}
	
	private static void WriteSave(String jsonSave){
		FileHandle file = Gdx.files.local(SaveManager.fileName);
		file.writeString(Base64Coder.encodeString(jsonSave), false);
	}
	
	private static String ReadSaveData(){
		FileHandle file = Gdx.files.local(SaveManager.fileName);
		if(file != null && file.exists()){
			String jsonSave = file.readString();
			if(!jsonSave.isEmpty()){
				return Base64Coder.decodeString(jsonSave);
			}
		}
		return "";
	}	
	
	private static StateDto GetBattleStateDto(BattleState state){
		StateDto stateDto = new StateDto();
		stateDto.stateKind = 0;
		stateDto.enemies = SaveManager.MapUnitDtos(state.enemies, state);
		stateDto.roster = SaveManager.MapUnitDtos(state.roster, state);
		stateDto.gold = state.gold;
		stateDto.roundsSurvived = state.roundsSurvived;
		stateDto.perksPurchased = state.perksPurchased;
		stateDto.battlefieldId = state.battlefieldId;
		stateDto.battlefield = SaveManager.MapTileDtos(state.battlefield);
		stateDto.currentPlayer = state.currentPlayer;
		stateDto.turnCount = state.turnCount;
		stateDto.graveyard = state.graveyard;
		stateDto.undos = state.undos;
		return stateDto;
	}
	
	private static StateDto GetShopStateDto(ShopState state){
		StateDto stateDto = new StateDto();
		stateDto.stateKind = 1;
		stateDto.roster = SaveManager.MapUnitDtos(state.roster, null);
		stateDto.gold = state.gold;
		stateDto.roundsSurvived = state.roundsSurvived;
		stateDto.perksPurchased = state.perksPurchased;
		stateDto.battlefieldId = state.nextBattlefieldId;
		stateDto.battlefield = SaveManager.MapTileDtos(state.nextBattlefield);
		stateDto.graveyard = state.graveyard;
		stateDto.stock = SaveManager.MapUnitDtos(state.stock, null);
		stateDto.rerollCount = state.rerollCount;
		return stateDto;
	}
	
	public static class StateDto{
		public int stateKind;
		public List<UnitDto> enemies;
		public List<UnitDto> roster;
		public int gold;
		public int roundsSurvived;
		public int perksPurchased;
		public int difficulty;
		public int battlefieldId;
		public List<List<TileDto>> battlefield;
		public int currentPlayer;
		public int turnCount;
		public List<UnitDto> graveyard;
		public Stack<Move> undos;
		public List<UnitDto> stock;
		public int rerollCount;
	}
	
	public static List<Unit> MapUnits(List<UnitDto> source) throws IOException{
		List<Unit> units = new ArrayList<Unit>();
		for(UnitDto unitDto : source){
			UnitType unitType = UnitType.valueOf(unitDto.type);
			Unit unit = null;
			switch(unitType){
			case Archer:
				unit = new Archer(unitDto);
				break;
			case Footman:
				unit = new Footman(unitDto);
				break;
			case Knight:
				unit = new Knight(unitDto);
				break;
			case Mage:
				unit = new Mage(unitDto);
				break;
			case Priest:
				unit = new Priest(unitDto);
				break;
			case Spearman:
				unit = new Spearman(unitDto);
				break;
			}
			units.add(unit);
		}
		return units;
	}
	
	public static List<List<Tile>> MapTiles(List<List<TileDto>> source){
		final List<List<Tile>> battlefield = new ArrayList<List<Tile>>();
		for(List<TileDto> dtoRow : source){
			List<Tile> row = new ArrayList<Tile>();
			for(TileDto tileDto : dtoRow){
				row.add(new Tile(tileDto));
			}
			battlefield.add(row);
		}
		return battlefield;
	}
	
	public static List<UnitDto> MapUnitDtos(List<Unit> source, BattleState battleState){
		List<UnitDto> unitDtos = new ArrayList<UnitDto>();
		for(Unit unit : source){
			UnitDto unitDto = new UnitDto();
			unitDto.type = unit.type.toString();
			unitDto.name = unit.name;
			unitDto.attack = unit.attack;
			unitDto.defense = unit.defense;
			unitDto.evasion = unit.evasion;
			unitDto.accuracy = unit.accuracy;
			unitDto.movement = unit.movement;
			unitDto.maximumHealth = unit.maximumHealth;
			unitDto.level = unit.level;
			unitDto.unitsKilled = unit.unitsKilled;
			unitDto.damageDealt = unit.damageDealt;
			unitDto.isMale = unit.isMale;
			unitDto.backStory = unit.backStory;			
			unitDto.team = unit.team;
			unitDto.currentHealth = unit.currentHealth;
			unitDto.experience = unit.experience;
			unitDto.experienceNeeded = unit.experienceNeeded;
			unitDto.cost = unit.cost;
			unitDto.id = unit.id;
			unitDto.x = unit.x;
			unitDto.y = unit.y;
			unitDto.maximumRange = unit.maximumRange;
			unitDto.minimumRange = unit.minimumRange;
			unitDto.distanceMoved = unit.distanceMoved;
			unitDto.hasMoved = unit.hasMoved;
			unitDto.hasAttacked = unit.hasAttacked;
			unitDto.gameSpeed = unit.gameSpeed;
			if(unit.ability != null){
				unitDto.isAbilityExhausted = unit.ability.exhausted;
				unitDto.ability = unit.ability.displayName;
				if(battleState != null){
					unitDto.canUseAbility = unit.ability.CanUse(battleState, unit);
				}else{
					unitDto.canUseAbility = false;			
				}
				unitDto.abilityTargets = unit.ability.targets;
			}else{
				unitDto.isAbilityExhausted = false;
				unitDto.ability = "None";
				unitDto.canUseAbility = false;
				unitDto.abilityTargets = new ArrayList<Integer>();
			}
			unitDtos.add(unitDto);
		}
		return unitDtos;
	}
	
	public static List<List<TileDto>> MapTileDtos(List<List<Tile>> source){
		final List<List<TileDto>> battlefield = new ArrayList<List<TileDto>>();
		for(List<Tile> row : source){
			List<TileDto> dtoRow = new ArrayList<TileDto>();
			for(Tile tile : row){
				TileDto tileDto = new TileDto();
				tileDto.type = tile.type;
				tileDto.defenseModifier = tile.defenseModifier;
				tileDto.evasionModifier = tile.evasionModifier;
				tileDto.altitude = tile.altitude;
				tileDto.movementCost = tile.movementCost;
				tileDto.type = tile.type;
				tileDto.x = tile.x;
				tileDto.y = tile.y;
				dtoRow.add(tileDto);
			}
			battlefield.add(dtoRow);
		}
		return battlefield;
	}
	
	private static final String fileName = "heroesemblemstate.sav";
}
