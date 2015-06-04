package bunzosteele.heroesemblem.model.Battlefield;

import java.io.IOException;

import bunzosteele.heroesemblem.model.Units.Abilities.Ability;

public class Tile {
	public TileType type;
	public int defenseModifier;
	public int accuracyModifier;
	public int movementCost;
	public int altitude;
	
	public Tile(TileType type, int defenseModifier, int accuracyModifier, int movementCost, int altitude) throws IOException{	
		this.type = type;
		this.defenseModifier = defenseModifier;
		this.accuracyModifier = accuracyModifier;
		this.movementCost = movementCost;
		this.altitude = altitude;
	}
}
