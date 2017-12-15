package bunzosteele.heroesemblem.model.Battlefield;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;

public final class TileBuilder
{
	public static Tile BuildTile(final String tileType, final Element xml, final String foreground) throws IOException
	{
		String parsedType = tileType;
		if(parsedType.contains("/")){
			int length = parsedType.split("/").length;
			parsedType = parsedType.split("/")[length-1];
		}
		String parsedForeground = foreground;
		if(parsedForeground != null && parsedForeground.contains("/")){
			int length = parsedForeground.split("/").length;
			parsedForeground = parsedForeground.split("/")[length-1];
		}
		final Element tileStats = xml.getChildByName(parsedType.split("-")[0]);
		return new Tile(parsedType, tileStats.getInt("DefenseModifier"), tileStats.getInt("EvasionModifier"), tileStats.getInt("MovementCost"), tileStats.getInt("Altitude"), parsedForeground);
	}
}
