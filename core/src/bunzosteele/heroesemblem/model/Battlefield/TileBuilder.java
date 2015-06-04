package bunzosteele.heroesemblem.model.Battlefield;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public final class TileBuilder {
	public static Tile BuildTile(TileType tileType, Element xml) throws IOException{
		Element tileStats = xml.getChildByName(tileType.toString());
		return new Tile(tileType, tileStats.getInt("DefenseModifier"), tileStats.getInt("AccuracyModifier"), tileStats.getInt("MovementCost"), tileStats.getInt("Altitude"));
	}
}
