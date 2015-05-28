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
	
	public StockWindow(HeroesEmblem game, ShopState state){
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
	}
	
	public void draw(){
		int windowOffset = Gdx.graphics.getWidth() / 6;
		int columnWidth = (2 * Gdx.graphics.getWidth() / 3) / 9;
		int unitOffset = 0;
		TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("HeroesEmblem.pack"));
		AtlasRegion pedestalRegion = textureAtlas.findRegion("pedestal");
		Sprite pedestalSprite = new Sprite(pedestalRegion);

		for(Unit unit : state.stock){
			if (unitOffset < 4){
				game.batcher.begin();
				game.batcher.draw(pedestalSprite, windowOffset + columnWidth + (columnWidth * unitOffset * 2), 3 *Gdx.graphics.getHeight()/ 4, columnWidth, columnWidth);
				game.batcher.end();
				UnitRenderer.DrawUnit(game, unit, windowOffset + columnWidth + (columnWidth * unitOffset * 2), 3 *Gdx.graphics.getHeight()/ 4, columnWidth, "Idle", idleFrame);
			}
			else{
				game.batcher.begin();
				game.batcher.draw(pedestalSprite, windowOffset + columnWidth + (columnWidth * (unitOffset-4) * 2), Gdx.graphics.getHeight()/ 4, columnWidth, columnWidth);
				game.batcher.end();
				UnitRenderer.DrawUnit(game, unit, windowOffset + columnWidth + (columnWidth * (unitOffset-4) * 2), Gdx.graphics.getHeight()/ 4, columnWidth, "Idle", idleFrame);
			}
				unitOffset++;
		}

		textureAtlas.dispose();
	}
}
