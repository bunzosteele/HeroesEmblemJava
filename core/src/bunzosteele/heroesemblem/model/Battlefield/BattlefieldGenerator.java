package bunzosteele.heroesemblem.model.Battlefield;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public final class BattlefieldGenerator
{

	public static List<List<Tile>> GenerateBattlefield(final int battlefieldId) throws IOException
	{
		final XmlReader reader = new XmlReader();
		final Element tileStats = reader.parse(Gdx.files.internal("TileStats.xml"));
		final Element battlefieldXml = reader.parse(Gdx.files.internal(battlefieldId + ".tmx"));
		final Map<Integer, TileType> tilesById = BattlefieldGenerator.GenerateTileKeys(battlefieldXml);
		Element terrainLayer = null;
		for (final Element e : battlefieldXml.getChildrenByName("layer"))
		{
			if (e.getAttribute("name").equals("Terrain"))
			{
				terrainLayer = e.getChildByName("data");
			}
		}

		final List<List<Tile>> battlefield = BattlefieldGenerator.GenerateBattlefieldTiles(terrainLayer, tilesById, tileStats, battlefieldXml.getChildByName("layer").getInt("width"));
		return battlefield;
	}
	
	public static int DetectMinimumMap(){
		final XmlReader reader = new XmlReader();
		int battlefieldId = 0;
		while(true){
			try
			{
				reader.parse(Gdx.files.internal(battlefieldId + ".tmx"));
			} catch (Exception e)
			{
				return battlefieldId + 1;
			}
			battlefieldId--;
		}
	}
	
	public static int DetectMaximumMap(){
		final XmlReader reader = new XmlReader();
		int battlefieldId = 0;
		while(true){
			try
			{
				reader.parse(Gdx.files.internal(battlefieldId + ".tmx"));
			} catch (Exception e)
			{
				return battlefieldId - 1;
			}
			battlefieldId++;
		}
	}

	private static List<List<Tile>> GenerateBattlefieldTiles(final Element xml, final Map<Integer, TileType> tilesById, final Element tileStats, final int width) throws IOException
	{
		int rowCount = 0;
		int columnCount = 0;
		final List<List<Tile>> battlefield = new ArrayList<List<Tile>>();
		List<Tile> row = new ArrayList<Tile>();
		final Array<Element> tiles = xml.getChildrenByName("tile");
		for (final Element e : tiles)
		{
			if (columnCount == width)
			{
				columnCount = 0;
				rowCount++;
				battlefield.add(row);
				row = new ArrayList<Tile>();
			}
			final int tileId = e.getInt("gid");
			final TileType tileType = tilesById.get(tileId);
			final Tile tile = TileBuilder.BuildTile(tileType, tileStats);
			tile.x = columnCount;
			tile.y = rowCount;
			row.add(tile);
			columnCount++;
		}
		battlefield.add(row);

		return battlefield;
	}

	public static List<Spawn> GenerateSpawns(final int battlefieldId) throws IOException
	{
		final XmlReader reader = new XmlReader();
		final Element tileStats = reader.parse(Gdx.files.internal("TileStats.xml"));
		final Element battlefieldXml = reader.parse(Gdx.files.internal(battlefieldId + ".tmx"));
		Element terrainLayer = null;
		Element tileset = null;
		for (final Element e : battlefieldXml.getChildrenByName("tileset"))
		{
			if (e.getAttribute("name").equals("Spawns"))
			{
				tileset = e;
			}
		}
		for (final Element e : battlefieldXml.getChildrenByName("layer"))
		{
			if (e.getAttribute("name").equals("Spawns"))
			{
				terrainLayer = e.getChildByName("data");
			}
		}

		return BattlefieldGenerator.GetSpawns(tileset, terrainLayer, battlefieldXml.getChildByName("layer").getInt("width"));
	}

	private static Map<Integer, TileType> GenerateTileKeys(final Element xml)
	{
		Element tileset = null;
		for (final Element e : xml.getChildrenByName("tileset"))
		{
			if (e.getAttribute("name").equals("Terrain"))
			{
				tileset = e;
			}
		}
		final List<Element> tiles = new ArrayList<Element>();
		for (final Element e : tileset.getChildrenByName("tile"))
		{
			tiles.add(e);
		}

		final Map<Integer, TileType> tilesById = new HashMap<Integer, TileType>();
		for (final Element tile : tiles)
		{
			final int id = tile.getInt("id") + 1;
			final Element tileInfo = tile.getChildByName("image");
			final String tileSrc = tileInfo.getAttribute("source");
			final String tileName = tileSrc.substring(0, tileSrc.indexOf("."));
			final TileType type = TileType.valueOf(tileName);
			tilesById.put(id, type);
		}
		return tilesById;
	}

	private static List<Spawn> GetSpawns(final Element tileset, final Element terrainLayer, final int width)
	{
		int playerSpawnId = 0;
		final int baseId = tileset.getInt("firstgid");
		for (final Element tile : tileset.getChildrenByName("tile"))
		{
			final Element image = tile.getChildByName("image");
			final String tileSrc = image.getAttribute("source");
			final String tileName = tileSrc.substring(0, tileSrc.indexOf("."));
			if (tileName.equals("PlayerSpawn"))
			{
				playerSpawnId = tile.getInt("id");
			}
		}

		playerSpawnId += baseId;
		final List<Spawn> spawns = new ArrayList<Spawn>();
		int x = 0;
		int y = 0;
		for (final Element tile : terrainLayer.getChildrenByName("tile"))
		{
			if (x == width)
			{
				x = 0;
				y += 1;
			}

			final int spawnId = tile.getInt("gid");
			if (spawnId > 0)
			{
				spawns.add(new Spawn(x, y, spawnId == playerSpawnId));
			}
			x++;
		}
		return spawns;
	}
}
