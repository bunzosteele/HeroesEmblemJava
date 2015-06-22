package bunzosteele.heroesemblem.model.Units;

import java.util.HashSet;
import java.util.Map;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;

public interface Ai
{
	public int GetTileScore(Tile tile, BattleState state);
	public HashSet<Unit> GetTargets(BattleState state);
	public int GetTargetScore(Unit target, BattleState state);
}
