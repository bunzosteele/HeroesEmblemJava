package bunzosteele.heroesemblem.model.Battlefield;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;

public final class TileBuilder
{
	public static Tile BuildTile(final String tileType, final Element xml, final String foreground) throws IOException
	{
		String parsedType = tileType;
		if(parsedType.contains("/")){
			parsedType = parsedType.split("/")[1];
		}
		String parsedForeground = foreground;
		if(parsedForeground != null && parsedForeground.contains("/")){
			parsedForeground = parsedForeground.split("/")[1];
		}
		final Element tileStats = xml.getChildByName(parsedType.split("-")[0]);
		return new Tile(parsedType, tileStats.getInt("DefenseModifier"), tileStats.getInt("EvasionModifier"), tileStats.getInt("MovementCost"), tileStats.getInt("Altitude"), parsedForeground);
	}
}
