package bunzosteele.heroesemblem.model.Battlefield;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public final class BattlefieldGenerator {
	public static List<List<Tile>> GenerateBattlefield(int battlefieldId) throws IOException{
		XmlReader reader = new XmlReader();
		Element tileStats = reader.parse(Gdx.files.internal("TileStats.xml"));
		FileHandle handle = Gdx.files.internal(battlefieldId + ".txt");
		String file = handle.readString();
		List<List<Tile>> battlefield = new ArrayList<List<Tile>>();
		for(String row : file.split("\n")){
			List<Tile> tileRow = new ArrayList<Tile>();
			for(String tile : row.split(" ")){
				tileRow.add(TileBuilder.BuildTile(tile, tileStats));
			}
			battlefield.add(tileRow);
		}
		return battlefield;
	}
}
