package bunzosteele.heroesemblem.model;

import java.util.HashSet;
import java.util.List;

import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

public final class MovementHelper
{

	private static int getMovementCost(final int x, final int y, final List<List<Tile>> battlefield)
	{
		return battlefield.get(y).get(x).movementCost;
	}

	public static HashSet<Tile> GetMovementOptions(final BattleState state)
	{
		if (state.selected == null)
		{
			return null;
		}
		final HashSet<Tile> options = new HashSet<Tile>();
		final List<Unit> allUnits = state.AllUnits();
		allUnits.remove(state.selected);
		return MovementHelper.GetMovementOptionsCore(state.selected.x, state.selected.y, allUnits, state.enemies, state.selected, state.battlefield, state.selected.movement, options);
	}
	
	public static HashSet<Tile> GetMovementOptions(final BattleState state, Unit unit, List<Unit> unmovedAllies){
		final HashSet<Tile> options = new HashSet<Tile>();
		final List<Unit> allUnits = state.AllUnits();
		allUnits.remove(unit);
		allUnits.removeAll(unmovedAllies);
		return MovementHelper.GetMovementOptionsCore(unit.x, unit.y, allUnits, state.enemies, null, state.battlefield, unit.movement, options);
	}

	public static HashSet<Tile> GetMovementOptionsCore(final int x, final int y, final List<Unit> allUnits, final List<Unit> enemies, final Unit movingUnit, final List<List<Tile>> battlefield, final int movement, final HashSet<Tile> options)
	{
		boolean isSelfBlocked = movingUnit != null && ((x == movingUnit.x) && (y == movingUnit.y));
		if (!MovementHelper.isVisited(x, y, options) && !isSelfBlocked && !MovementHelper.isOccupied(x, y, allUnits))
		{
			final Tile newOption = battlefield.get(y).get(x);
			options.add(newOption);
		}

		if (MovementHelper.isInBounds(x, y - 1, battlefield) && !MovementHelper.isOccupied(x, y - 1, enemies))
		{
			final int remainingMovement = movement - MovementHelper.getMovementCost(x, y - 1, battlefield);
			if (remainingMovement >= 0)
			{
				MovementHelper.GetMovementOptionsCore(x, y - 1, allUnits, enemies, movingUnit, battlefield, remainingMovement, options);
			}
		}
		if (MovementHelper.isInBounds(x + 1, y, battlefield) && !MovementHelper.isOccupied(x + 1, y, enemies))
		{
			final int remainingMovement = movement - MovementHelper.getMovementCost(x + 1, y, battlefield);
			if (remainingMovement >= 0)
			{
				MovementHelper.GetMovementOptionsCore(x + 1, y, allUnits, enemies, movingUnit, battlefield, remainingMovement, options);
			}
		}
		if (MovementHelper.isInBounds(x, y + 1, battlefield) && !MovementHelper.isOccupied(x, y + 1, enemies))
		{
			final int remainingMovement = movement - MovementHelper.getMovementCost(x, y + 1, battlefield);
			if (remainingMovement >= 0)
			{
				MovementHelper.GetMovementOptionsCore(x, y + 1, allUnits, enemies, movingUnit, battlefield, remainingMovement, options);
			}
		}
		if (MovementHelper.isInBounds(x - 1, y, battlefield) && !MovementHelper.isOccupied(x - 1, y, enemies))
		{
			final int remainingMovement = movement - MovementHelper.getMovementCost(x - 1, y, battlefield);
			if (remainingMovement >= 0)
			{
				MovementHelper.GetMovementOptionsCore(x - 1, y, allUnits, enemies, movingUnit, battlefield, remainingMovement, options);
			}
		}
		return options;
	}

	private static boolean isInBounds(final int x, final int y, final List<List<Tile>> battlefield)
	{
		final int height = battlefield.size();
		final int width = battlefield.get(0).size();

		return (x >= 0) && (x < width) && (y >= 0) && (y < height);
	}

	private static boolean isOccupied(final int x, final int y, final List<Unit> units)
	{
		boolean occupied = false;
		for (final Unit unit : units)
		{
			if ((unit.x == x) && (unit.y == y))
			{
				occupied = true;
			}
		}
		return occupied;
	}

	private static boolean isVisited(final int x, final int y, final HashSet<Tile> visited)
	{
		boolean isVisited = false;
		for (final Tile tile : visited)
		{
			if ((tile.x == x) && (tile.y == y))
			{
				isVisited = true;
			}
		}
		return isVisited;
	}
}
