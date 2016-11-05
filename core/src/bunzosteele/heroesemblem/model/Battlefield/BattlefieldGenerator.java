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
		final Map<Integer, String> tilesById = BattlefieldGenerator.GenerateTileKeys(battlefieldXml, "Terrain");
		final Map<Integer, String> foregroundsById = BattlefieldGenerator.GenerateTileKeys(battlefieldXml, "Foreground");
		Element terrainLayer = null;
		Element foregroundLayer = null;
		for (final Element e : battlefieldXml.getChildrenByName("layer"))
		{
			if (e.getAttribute("name").equals("Terrain"))
			{
				terrainLayer = e.getChildByName("data");
			}
			if (e.getAttribute("name").equals("Foreground"))
			{
				foregroundLayer = e.getChildByName("data");
			}
		}
		final List<List<Tile>> battlefield = BattlefieldGenerator.GenerateBattlefieldTiles(terrainLayer, foregroundLayer, tilesById, foregroundsById, tileStats, battlefieldXml.getChildByName("layer").getInt("width"),  battlefieldXml.getChildByName("layer").getInt("height"));
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

	private static List<List<Tile>> GenerateBattlefieldTiles(final Element tileXml, final Element foregroundXml, final Map<Integer, String> tilesById, final Map<Integer, String> foregroundsById, final Element tileStats, final int width, final int height) throws IOException
	{
		final List<List<Tile>> battlefield = new ArrayList<List<Tile>>();
		List<Tile> row = new ArrayList<Tile>();
		final Array<Element> tiles = tileXml.getChildrenByName("tile");
		final Array<Element> foregrounds = foregroundXml.getChildrenByName("tile");
		for (int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++){
				final int tileId = tiles.get(width * y + x).getInt("gid");
				final int foregroundId = foregrounds.get(width * y + x).getInt("gid");
				final String tileType = tilesById.get(tileId);
				final String foregroundType = foregroundsById.get(foregroundId);
				final Tile tile = TileBuilder.BuildTile(tileType, tileStats, foregroundType);
				tile.x = x;
				tile.y = y;
				row.add(tile);
			}
			battlefield.add(row);
			row = new ArrayList<Tile>();
		}
		return battlefield;
	}

	public static List<Spawn> GenerateSpawns(final int battlefieldId) throws IOException
	{
		final XmlReader reader = new XmlReader();
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

	private static Map<Integer, String> GenerateTileKeys(final Element xml, String keyType)
	{
		Element tileset = null;
		int firstgid = -1;
		for (final Element e : xml.getChildrenByName("tileset"))
		{
			if (e.getAttribute("name").equals(keyType))
			{
				tileset = e;
				firstgid = Integer.parseInt(e.getAttribute("firstgid"));
			}
		}
		final List<Element> tiles = new ArrayList<Element>();
		for (final Element e : tileset.getChildrenByName("tile"))
		{
			tiles.add(e);
		}

		final Map<Integer, String> tilesById = new HashMap<Integer, String>();
		for (final Element tile : tiles)
		{
			final int id = tile.getInt("id") + firstgid;
			final Element tileInfo = tile.getChildByName("image");
			final String tileSrc = tileInfo.getAttribute("source");
			final String tileName = tileSrc.substring(0, tileSrc.indexOf("."));
			final String type = tileName;
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
			if (tileName.equals("Spawns/PlayerSpawn"))
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
