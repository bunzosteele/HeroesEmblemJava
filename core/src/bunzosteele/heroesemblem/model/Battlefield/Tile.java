package bunzosteele.heroesemblem.model.Battlefield;

import java.io.IOException;

public class Tile
{
	public String type;
	public int defenseModifier;
	public int evasionModifier;
	public int movementCost;
	public int altitude;
	public int x;
	public int y;

	public Tile(final String type, final int defenseModifier, final int evasionModifier, final int movementCost, final int altitude) throws IOException
	{
		this.type = type;
		this.defenseModifier = defenseModifier;
		this.evasionModifier = evasionModifier;
		this.movementCost = movementCost;
		this.altitude = altitude;
	}
}
