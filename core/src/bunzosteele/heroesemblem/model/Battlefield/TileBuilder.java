package bunzosteele.heroesemblem.model.Battlefield;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;

public final class TileBuilder
{
	public static Tile BuildTile(final String tileType, final Element xml) throws IOException
	{
		final Element tileStats = xml.getChildByName(tileType);
		return new Tile(tileType, tileStats.getInt("DefenseModifier"), tileStats.getInt("EvasionModifier"), tileStats.getInt("MovementCost"), tileStats.getInt("Altitude"));
	}
}
