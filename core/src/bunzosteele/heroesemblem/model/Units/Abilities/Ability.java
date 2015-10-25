package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;

public abstract class Ability
{

	public String displayName;
	public boolean isActive;
	public boolean isTargeted;
	public Color abilityColor;
	public boolean exhausted;
	public boolean isMultiInput;
	public List<Unit> targets = new ArrayList<Unit>();
	public boolean areTargetsPersistent;

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
		return state.selected.movement >= tile.movementCost;
	}
	
	public void PlaySound(float volume){
		
	}
}
