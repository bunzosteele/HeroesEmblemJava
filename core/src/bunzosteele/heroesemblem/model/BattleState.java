package bunzosteele.heroesemblem.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.Battlefield.BattlefieldGenerator;
import bunzosteele.heroesemblem.model.Battlefield.Spawn;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.LocationDto;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitDto;
import bunzosteele.heroesemblem.model.Units.UnitGenerator;
import bunzosteele.heroesemblem.view.ShopControls;

public class BattleState
{
	public List<Unit> enemies;
	public List<Unit> roster;
	public Unit selected;
	public int gold;
	public int roundsSurvived;
	public int perksPurchased;
	public int difficulty;
	public int battlefieldId;
	public List<List<Tile>> battlefield;
	public boolean isMoving;
	public boolean isAttacking;
	public boolean isUsingAbility;
	public int currentPlayer;
	public int turnCount;
	public HeroesEmblem game;
	public List<UnitDto> graveyard;
	public Stack<Move> undos;

	public BattleState(final ShopState shopState) throws IOException
	{
		this.game = shopState.game;
		this.turnCount = 1;
		this.currentPlayer = 0;
		this.roundsSurvived = shopState.roundsSurvived;
		this.perksPurchased = shopState.perksPurchased;
		this.difficulty = (int) Math.pow(2, roundsSurvived);
		this.roster = shopState.roster;
		this.selected = null;
		this.gold = shopState.gold;
		this.battlefieldId = shopState.nextBattlefieldId;
		this.battlefield = shopState.nextBattlefield;
		final List<Spawn> spawns = BattlefieldGenerator.GenerateSpawns(battlefieldId);
		final List<Spawn> playerSpawns = new ArrayList<Spawn>();
		final List<Spawn> enemySpawns = new ArrayList<Spawn>();
		for (final Spawn spawn : spawns)
		{
			if (spawn.isPlayer)
			{
				playerSpawns.add(spawn);
			} else
			{
				enemySpawns.add(spawn);
			}
		}
		if(this.perksPurchased > 3 && battlefieldId < 0){
			this.enemies = UnitGenerator.GenerateEnemies(enemySpawns.size(), this.difficulty, this.roster, this.game);	
		}else{
			this.enemies = UnitGenerator.GenerateEnemies(enemySpawns.size(), this.difficulty - battlefieldId, this.roster, this.game);
		}
		this.graveyard = shopState.graveyard;
		this.undos = new Stack<Move>();
		this.SpawnUnits(this.roster, playerSpawns);
		this.SpawnUnits(this.enemies, enemySpawns);
		this.StartBattle();
	}

	public List<Unit> AllUnits()
	{
		final List<Unit> allUnits = new ArrayList<Unit>();
		for (final Unit unit : this.roster)
		{
			allUnits.add(unit);
		}
		for (final Unit unit : this.enemies)
		{
			allUnits.add(unit);
		}
		return allUnits;
	}

	public boolean CanAttack(final Unit unit)
	{
		if ((unit != null) && (unit.team == this.currentPlayer))
		{
			if (!unit.hasAttacked)
			{
				for (final Tile tile : CombatHelper.GetAttackOptions(this, unit))
				{
					for (final Unit enemy : this.AllUnits())
					{
						if ((enemy.x == tile.x) && (enemy.y == tile.y) && (enemy.team != this.currentPlayer))
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public boolean CanMove()
	{
		if ((this.selected != null) && (this.selected.team == this.currentPlayer))
		{
			if (!this.selected.hasMoved)
			{
				return true;
			}
		}
		return false;
	}

	public boolean CanUseAbility(final Unit unit)
	{
		if ((unit != null) && (unit.ability != null) && (unit.team == this.currentPlayer))
		{
			return unit.ability.CanUse(this, unit);
		} else
		{
			return false;
		}
	}
	
	public boolean CanUndo(){
		return this.selected != null && this.undos.size() > 0 && this.selected == this.undos.peek().unit;
	}
	
	public void ClearUndos(){
		while(this.undos.size() > 0){
			this.undos.pop();
		}
	}

	public void CleanBoard()
	{
		for (int i = this.roster.size() - 1; i >= 0; i--)
		{
			if (this.roster.get(i).isDying)
			{
				if (this.roster.get(i).deathFrame < 1)
				{
					this.roster.remove(i);
				}
			}
		}
		for (int i = this.enemies.size() - 1; i >= 0; i--)
		{
			if (this.enemies.get(i).isDying)
			{
				if (this.enemies.get(i).deathFrame < 1)
				{
					this.enemies.remove(i);
				}
			}
		}
	}

	public List<Unit> CurrentPlayerUnits()
	{
		return this.currentPlayer == 0 ? this.roster : this.enemies;
	}
	
	public Tile GetTileForUnit(Unit unit){
		return this.battlefield.get(unit.y).get(unit.x);
	}

	public void EndBattle()
	{
		WipeUnitVariables();
		ClearUndos();
	}
	
	public void StartBattle()
	{
		WipeUnitVariables();
		ClearUndos();
	}
	
	public void SaveGraveyard(Unit deceased){
		UnitDto unitDto = new UnitDto();
		unitDto.type = deceased.type.toString();
		unitDto.name = deceased.name;
		unitDto.attack = deceased.attack;
		unitDto.defense = deceased.defense;
		unitDto.evasion = deceased.evasion;
		unitDto.accuracy = deceased.accuracy;
		unitDto.movement = deceased.movement;
		unitDto.maximumHealth = deceased.maximumHealth;
		unitDto.level = deceased.level;
		if(deceased.ability == null){
			unitDto.ability = "None";
		}else{
			unitDto.ability = deceased.ability.displayName;
		}
		unitDto.unitsKilled = deceased.unitsKilled;
		unitDto.damageDealt = deceased.damageDealt;
		LocationDto location = new LocationDto();
		location.battlefieldId = this.battlefieldId;
		location.x = deceased.x;
		location.y = deceased.y;
		unitDto.locationKilled = location;
		unitDto.roundKilled = roundsSurvived + 1;
		unitDto.isMale = deceased.isMale;
		unitDto.backStory = deceased.backStory;
		this.graveyard.add(unitDto);
	}
	
	private void WipeUnitVariables(){
		for (final Unit unit : this.AllUnits())
		{
			if (unit.ability != null)
			{
				unit.ability.exhausted = false;
				unit.ability.targets = new ArrayList<Unit>();
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

	public void EndTurn()
	{
		this.isAttacking = false;
		this.isMoving = false;
		this.isUsingAbility = false;
		this.selected = null;
		ClearUndos();
		for (final Unit unit : this.CurrentPlayerUnits())
		{
			unit.hasMoved = false;
			unit.hasAttacked = false;
			unit.distanceMoved = 0;
		}
		this.currentPlayer = (this.currentPlayer + 1) % 2;
		if (this.currentPlayer == 0)
		{
			this.turnCount++;
			Gdx.audio.newSound(Gdx.files.internal("newturn.wav")).play(this.game.settings.getFloat("sfxVolume", .5f));
		}
	}

	public boolean IsTapped(final Unit unit)
	{
		if (!unit.hasMoved)
		{
			return false;
		}

		if (this.CanAttack(unit) && !unit.hasAttacked)
		{
			return false;
		}

		if (this.CanUseAbility(unit))
		{
			return false;
		}

		return true;
	}

	private void SpawnUnits(final List<Unit> units, final List<Spawn> spawns)
	{
		final Random random = new Random();
		for (final Unit unit : units)
		{
			final int spawnRoll = random.nextInt(spawns.size());
			final Spawn spawn = spawns.get(spawnRoll);
			unit.x = spawn.x;
			unit.y = spawn.y;
			spawns.remove(spawn);
		}
	}
	
	public class Move{
		public Unit unit;
		public int oldX;
		public int oldY;
	}
}
