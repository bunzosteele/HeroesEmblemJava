package bunzosteele.heroesemblem.view;


import java.io.IOException;
import java.util.List;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.HighscoreManager;
import bunzosteele.heroesemblem.model.MusicManager;
import bunzosteele.heroesemblem.model.ShopState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.LocationDto;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitDto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class MapPanel extends PopupPanel
{	
	List<List<Tile>> nextBattlefield;
	int mapTileSize;
	public MapPanel(final HeroesEmblem game, List<List<Tile>> nextBattlefield, int width, int height, int xOffset, int yOffset)
	{
		super(game, width, height, xOffset, yOffset);
		this.mapTileSize = width / 14;
		this.nextBattlefield = nextBattlefield;
	}
	
	public void drawBackground(){
		super.drawBackground(true);
	}
	
	public void draw(){
		super.drawBorder();
		int rowOffset = 1;
		for ( List<Tile> row : nextBattlefield)
		{
			int tileOffset = 0;
			for (Tile tile : row)
			{
				AtlasRegion tileRegion = game.textureAtlas.findRegion(tile.type);
				Sprite tileSprite = new Sprite(tileRegion);
				game.batcher.draw(tileSprite, xOffset + mapTileSize * 1 / 2 + (mapTileSize * tileOffset + 1), yOffset + height - mapTileSize * 1 / 2 - (mapTileSize * rowOffset + 2), mapTileSize, mapTileSize);
				tileOffset++;
			}
			rowOffset++;
		}
		rowOffset = 1;
		for ( List<Tile> row : nextBattlefield)
		{
			int tileOffset = 0;
			for ( Tile tile : row)
			{
				if(tile.foreground != null){
					 AtlasRegion foregroundRegion = game.textureAtlas.findRegion(tile.foreground);
					 Sprite foregroundSprite = new Sprite(foregroundRegion);
					 game.batcher.draw(foregroundSprite, xOffset + mapTileSize * 1 / 2 + (mapTileSize * tileOffset + 1), yOffset + height - mapTileSize * 1 / 2 - (mapTileSize * rowOffset + 2), mapTileSize, mapTileSize);
				}
				tileOffset++;
			}
			rowOffset++;
		}
	}
	
	public void processTouch(final float x, final float y) throws IOException{
	}	
}
