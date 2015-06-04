package bunzosteele.heroesemblem.model.Battlefield;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public final class BattlefieldGenerator {
	
	public static List<List<Tile>> GenerateBattlefield(int battlefieldId) throws IOException{
		XmlReader reader = new XmlReader();
		Element tileStats = reader.parse(Gdx.files.internal("TileStats.xml"));
		Element battlefieldXml = reader.parse(Gdx.files.internal(battlefieldId + ".tmx"));
		Map<Integer, TileType> tilesById = GenerateTileKeys(battlefieldXml);
		Element terrainLayer = null;
		for(Element e : battlefieldXml.getChildrenByName("layer")){
			if (e.getAttribute("name").equals("Terrain"))
				terrainLayer = e.getChildByName("data");
		}
		
		List<List<Tile>> battlefield = GenerateBattlefieldTiles(terrainLayer, tilesById, tileStats, battlefieldXml.getChildByName("layer").getInt("width"));
		return battlefield;
	}
	
	public static List<Spawn> GenerateSpawns(int battlefieldId) throws IOException{
		XmlReader reader = new XmlReader();
		Element tileStats = reader.parse(Gdx.files.internal("TileStats.xml"));
		Element battlefieldXml = reader.parse(Gdx.files.internal(battlefieldId + ".tmx"));
		Element terrainLayer = null;
		Element tileset = null;
		for(Element e : battlefieldXml.getChildrenByName("tileset")){
			if (e.getAttribute("name").equals("Spawns"))
				tileset = e;
		}
		for(Element e : battlefieldXml.getChildrenByName("layer")){
			if (e.getAttribute("name").equals("Spawns"))
				terrainLayer = e.getChildByName("data");
		}
		
		return GetSpawns(tileset, terrainLayer, battlefieldXml.getChildByName("layer").getInt("width"));
	}
	
	private static List<Spawn> GetSpawns(Element tileset, Element terrainLayer, int width){
		int playerSpawnId = 0;
		int baseId = tileset.getInt("firstgid");
		for(Element tile : tileset.getChildrenByName("tile")){
			Element image = tile.getChildByName("image");
			String tileSrc = image.getAttribute("source");
			String tileName = tileSrc.substring(0, tileSrc.indexOf("."));
			if(tileName.equals("PlayerSpawn")){
				playerSpawnId = tile.getInt("id");
			}
		}
		
		playerSpawnId += baseId;
		List<Spawn> spawns = new ArrayList<Spawn>();
		int x = 0;
		int y = 0;
		for(Element tile : terrainLayer.getChildrenByName("tile")){
			if (x == width){
				x = 0;
				y += 1;
			}
			
			int spawnId = tile.getInt("gid");
			if(spawnId > 0){
				spawns.add(new Spawn(x, y, spawnId == playerSpawnId));
			}		
			x++;
		}
		
		return spawns;
	}
	
	
	
	private static Map<Integer, TileType> GenerateTileKeys(Element xml){
		Element tileset = null;
		for(Element e : xml.getChildrenByName("tileset")){
			if (e.getAttribute("name").equals("Terrain"))
				tileset = e;
		}
		List<Element> tiles = new ArrayList<Element>();
		for(Element e : tileset.getChildrenByName("tile")){
			tiles.add(e);
		}
		
		Map<Integer, TileType> tilesById = new HashMap<Integer, TileType>();
		for(Element tile : tiles){
			int id = tile.getInt("id") + 1;
			Element tileInfo = tile.getChildByName("image");
			String tileSrc = tileInfo.getAttribute("source");
			String tileName = tileSrc.substring(0, tileSrc.indexOf("."));
			TileType type = TileType.valueOf(tileName);
			tilesById.put(id, type);
		}
		return tilesById;
	}
	
	private static List<List<Tile>> GenerateBattlefieldTiles(Element xml, Map<Integer, TileType> tilesById, Element tileStats, int width) throws IOException{
		int rowCount = 0;
		List<List<Tile>> battlefield = new ArrayList<List<Tile>>();
		List<Tile> row = new ArrayList<Tile>();
		Array<Element> tiles = xml.getChildrenByName("tile");
		for(Element e : tiles){
			if (rowCount == width){
			rowCount = 0;
			battlefield.add(0, row);
			row = new ArrayList<Tile>();	
			}
			int tileId = e.getInt("gid");
			TileType tileType = tilesById.get(tileId);
			row.add(TileBuilder.BuildTile(tileType, tileStats));
			rowCount++;
		}
		battlefield.add(0, row);
		
		return battlefield;
	}
}
