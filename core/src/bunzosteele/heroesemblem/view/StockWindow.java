package bunzosteele.heroesemblem.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.ShopState;
import bunzosteele.heroesemblem.model.Units.Unit;

public class StockWindow {
	HeroesEmblem game;
	ShopState state;
	int idleFrame = 1;
	int attackFrame = 1;
	int xOffset;
	int yOffset;
	int width;
	int height;
	int columnWidth;
	
	public StockWindow(HeroesEmblem game, ShopState state, int width, int height, int xOffset, int yOffset){
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
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		columnWidth = width / 9;
	}
	
	public void draw(){
		int unitOffset = 0;
		AtlasRegion pedestalRegion = game.textureAtlas.findRegion("Pedestal");
		Sprite pedestalSprite = new Sprite(pedestalRegion);

		for(Unit unit : state.stock){
			if (unitOffset < 4){
				game.batcher.draw(pedestalSprite, xOffset + columnWidth + (columnWidth * unitOffset * 2), yOffset - columnWidth/2 + 3 *height/4, columnWidth, columnWidth);
				if(state.selected != null && state.selected.isEquivalentTo(unit))
					UnitRenderer.DrawUnit(game, unit, xOffset + columnWidth + (columnWidth * unitOffset * 2), yOffset - columnWidth/2 + 3 *height/4, columnWidth, "Attack", attackFrame);
				else
					UnitRenderer.DrawUnit(game, unit, xOffset + columnWidth + (columnWidth * unitOffset * 2), yOffset - columnWidth/2 + 3 *height/4, columnWidth, "Idle", idleFrame);
			}
			else{
				game.batcher.draw(pedestalSprite, xOffset + columnWidth + (columnWidth * (unitOffset-4) * 2), yOffset - columnWidth/2 + height/4, columnWidth, columnWidth);
				if(state.selected != null && state.selected.isEquivalentTo(unit))
					UnitRenderer.DrawUnit(game, unit, xOffset + columnWidth + (columnWidth * (unitOffset-4) * 2), yOffset - columnWidth/2 + height/4, columnWidth, "Attack", attackFrame);
				else
					UnitRenderer.DrawUnit(game, unit, xOffset + columnWidth + (columnWidth * (unitOffset-4) * 2), yOffset - columnWidth/2 + height/4, columnWidth, "Idle", idleFrame);
			}
				unitOffset++;
		}
	}
	
	public void drawBackground(){
		
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
		int unitOffset = 0;
		boolean hit = false;
		for(Unit unit : state.stock){
			if (unitOffset < 4){
				int lowerXBound = xOffset + columnWidth + (columnWidth * unitOffset * 2);
				int upperXBound = lowerXBound+columnWidth;
				int lowerYBound = yOffset - columnWidth/2 + 3 *height/4;
				int upperYBound = yOffset - columnWidth/2 + 3 *height/4 + columnWidth;
				if(x >= lowerXBound && x < upperXBound){
					if( y >= lowerYBound && y < upperYBound){
						state.selected = state.stock.get(unitOffset);
						hit = true;
					}
				}
			}
			else{
				int lowerXBound = xOffset + columnWidth + (columnWidth * (unitOffset-4) * 2);
				int upperXBound = lowerXBound+columnWidth;
				int lowerYBound = yOffset - columnWidth/2 + height/4;
				int upperYBound = (yOffset - columnWidth/2 + height/4) + columnWidth;
				if(x >= lowerXBound && x < upperXBound){
					if( y >= lowerYBound && y < upperYBound){
						state.selected = state.stock.get(unitOffset);
						hit = true;
					}
				}
			}
				unitOffset++;
		}
		if(!hit){
			state.selected = null;
		}
	}
}
