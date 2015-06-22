package bunzosteele.heroesemblem.model.Battlefield;

import java.io.IOException;

public class Tile
{
	public TileType type;
	public int defenseModifier;
	public int accuracyModifier;
	public int movementCost;
	public int altitude;
	public int x;
	public int y;

	public Tile(final TileType type, final int defenseModifier, final int accuracyModifier, final int movementCost, final int altitude) throws IOException
	{
		this.type = type;
		this.defenseModifier = defenseModifier;
		this.accuracyModifier = accuracyModifier;
		this.movementCost = movementCost;
		this.altitude = altitude;
	}
}
