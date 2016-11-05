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
	public String foreground;

	public Tile(final String type, final int defenseModifier, final int evasionModifier, final int movementCost, final int altitude, final String foreground) throws IOException
	{
		this.type = type;
		this.defenseModifier = defenseModifier;
		this.evasionModifier = evasionModifier;
		this.movementCost = movementCost;
		this.altitude = altitude;
		this.foreground = foreground;
	}
	
	public Tile(TileDto tileDto){
		this.type = tileDto.type;
		this.defenseModifier = tileDto.defenseModifier;
		this.evasionModifier = tileDto.evasionModifier;
		this.movementCost = tileDto.movementCost;
		this.altitude = tileDto.altitude;
		this.x = tileDto.x;
		this.y = tileDto.y;
		this.foreground = tileDto.foreground;
	}
}
