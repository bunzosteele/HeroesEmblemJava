package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Ability
{
	public String displayName;
	public String description;
	public boolean isActive;
	public boolean isTargeted;
	public boolean exhausted;
	public boolean isMultiInput;
	public List<Integer> targets = new ArrayList<Integer>();
	public boolean areTargetsPersistent;
	public boolean isAction;

	public int AttackModifier(final Unit attacker)
	{
		return 0;
	}

	public boolean CanUse(final BattleState state, final Unit originUnit)
	{
		return false;
	}
	
	public boolean CouldUse(final BattleState state, final Unit originUnit, int x, int y){
		return false;
	}

	public boolean Execute(final BattleState state, Unit executor, final Tile targetTile)
	{
		return false;
	}
	
	public List<Unit> GetTargetableUnits(final BattleState state)
	{
		return null;
	}
	public HashSet<Tile> GetTargetTiles(final BattleState state, final Unit originUnit)
	{
		return null;
	}

	public boolean IsBlockingDamage()
	{
		return false;
	}
	
	public boolean IsAction(){
		return this.isAction;
	}
	
	public void ResetAbility(){
	}

	protected boolean isEmpty(final int x, final int y, final List<Unit> units)
	{
		for (final Unit unit : units)
		{
			if ((unit.x == x) && (unit.y == y))
			{
				return false;
			}
		}
		return true;
	}

	protected boolean isInBounds(final int x, final int y, final List<List<Tile>> battlefield)
	{
		final int height = battlefield.size();
		final int width = battlefield.get(0).size();

		return (x >= 0) && (x < width) && (y >= 0) && (y < height);
	}

	public boolean IsPreventingDeath(final Unit unit)
	{
		return false;
	}

	protected boolean isValidTerrain(final int x, final int y, final BattleState state)
	{
		final Tile tile = state.battlefield.get(y).get(x);
		return tile != null && state.selected != null && state.selected.movement >= tile.movementCost;
	}
	
	public void PlaySound(float volume){
		
	}
	
	public static Ability GenerateAbilityByName(String abilityName, boolean exhausted, boolean canUse, List<Integer> abilityTargets){
		if(abilityName.equals("Block"))
			return new Block(exhausted, canUse, abilityTargets);
		if(abilityName.equals("Lightning"))
			return new ChainLightning(exhausted, canUse, abilityTargets);
		if(abilityName.equals("Heal"))
			return new Heal(exhausted, canUse, abilityTargets);
		if(abilityName.equals("Joust"))
			return new Joust(exhausted, canUse, abilityTargets);
		if(abilityName.equals("Power Shot"))
			return new PowerShot(exhausted, canUse, abilityTargets);
		if(abilityName.equals("Rebirth"))
			return new Rebirth(exhausted, canUse, abilityTargets);
		if(abilityName.equals("Scholar"))
			return new Scholar(exhausted, canUse, abilityTargets);
		if(abilityName.equals("Shield Bash"))
			return new ShieldBash(exhausted, canUse, abilityTargets);
		if(abilityName.equals("Snipe"))
			return new Snipe(exhausted, canUse, abilityTargets);
		if(abilityName.equals("Sturdy"))
			return new Sturdy(exhausted, canUse, abilityTargets);
		if(abilityName.equals("Teleport"))
			return new Teleport(exhausted, canUse, abilityTargets);
		if(abilityName.equals("Thrust"))
			return new Thrust(exhausted, canUse, abilityTargets);
		if(abilityName.equals("Vault"))
			return new Vault(exhausted, canUse, abilityTargets);
		return null;
	}
}
