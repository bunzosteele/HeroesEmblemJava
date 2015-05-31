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
	public static Tile BuildTile(String tileKey, Element tileStats) throws IOException{
		Element grassStats = tileStats.getChildByName(TileType.Grass.toString());
		return new Tile(TileType.Grass, grassStats.getInt("DefenseModifier"), grassStats.getInt("AccuracyModifier"), grassStats.getInt("MovementCost"), grassStats.getInt("Altitude"), false);
	}
}
