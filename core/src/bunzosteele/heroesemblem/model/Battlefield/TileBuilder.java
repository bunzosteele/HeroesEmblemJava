package bunzosteele.heroesemblem.model.Battlefield;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;

public final class TileBuilder
{
	public static Tile BuildTile(final TileType tileType, final Element xml) throws IOException
	{
		final Element tileStats = xml.getChildByName(tileType.toString());
		return new Tile(tileType, tileStats.getInt("DefenseModifier"), tileStats.getInt("AccuracyModifier"), tileStats.getInt("MovementCost"), tileStats.getInt("Altitude"));
	}
}
