package bunzosteele.heroesemblem.view;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

public class BattleWindow {
	HeroesEmblem game;
	BattleState state;
	Texture img;
	int idleFrame = 1;
	int attackFrame = 1;
	int xOffset;
	int yOffset;
	int width;
	int height;
	int tileWidth;
	int tileHeight;
	
	public BattleWindow(HeroesEmblem game, BattleState state){
		this.game = game;
		this.state = state;
		Timer.schedule(new Task(){
			@Override
			public void run(){
				idleFrame++;
				attackFrame++;
				if(idleFrame > 3)
					idleFrame = 1;
				if(attackFrame > 2)
					attackFrame = 1;
			}}, 0 , 1/3f);
		xOffset = 0;
		yOffset = Gdx.graphics.getHeight() / 5;
		width = 4 * Gdx.graphics.getWidth() / 5;
		height = 4 * Gdx.graphics.getHeight() / 5;
		tileWidth = width / 16;
		tileHeight = height / 9;
	}
	
	public void draw(){
		drawBattlefield();
		drawUnits();
	}
	
	private void drawBattlefield(){
		TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("HeroesEmblem.pack"));
		game.batcher.begin();
		int rowOffset = 0;
		for(List<Tile> row : state.battlefield){
			int tileOffset = 0;
			for(Tile tile : row){
				AtlasRegion tileRegion = textureAtlas.findRegion(tile.type.toString());
				Sprite tileSprite = new Sprite(tileRegion);
				game.batcher.draw(tileSprite, xOffset + tileWidth * tileOffset, yOffset + tileHeight * rowOffset, tileWidth, tileHeight);
				tileOffset ++;
			}	
			rowOffset++;
			tileOffset = 0;
		}
		game.batcher.end();
		textureAtlas.dispose();
	}
	
	private void drawUnits(){
		for(Unit unit : state.roster){
			UnitRenderer.DrawUnit(game, unit, unit.x * tileWidth, Gdx.graphics.getHeight() - (unit.y + 1) * tileHeight, tileWidth, "Idle", idleFrame);
		}
		for(Unit unit : state.enemies){
			UnitRenderer.DrawUnit(game, unit, unit.x * tileWidth, Gdx.graphics.getHeight() - (unit.y + 1) * tileHeight, tileWidth, "Idle", idleFrame);
		}
	}
	
	public boolean isTouched(float x, float y){
		if(x >= xOffset && x < xOffset + width){
			if( y >= yOffset && y < yOffset + height){
				return true;
			}
		}
		return false;
	}
	
	public void processTouch(float x, float y){
		for(Unit unit : state.roster){
			if(unit.x * tileWidth < x && x <= unit.x * tileWidth +tileWidth){
				if(Gdx.graphics.getHeight() - (unit.y + 1) * tileHeight < y && y <= Gdx.graphics.getHeight() - (unit.y) * tileHeight){
					state.selected = unit;
				}
			}
		}
		for(Unit unit : state.enemies){
			if(unit.x * tileWidth < x && x <= unit.x * tileWidth +tileWidth){
				if(Gdx.graphics.getHeight() - (unit.y + 1) * tileHeight < y && y <= Gdx.graphics.getHeight() - (unit.y) * tileHeight){
					state.selected = unit;
				}
			}
		}
	}
}
