package bunzosteele.heroesemblem.view;

import java.io.IOException;

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
import bunzosteele.heroesemblem.model.State;

public class UnitStatusPanel {
	HeroesEmblem game;
	State state;
	int currentFrame = 1;
	int xOffset;
	int yOffset;
	int width;
	int height;
	
	public UnitStatusPanel(HeroesEmblem game, State state){
		this.game = game;
		this.state = state;
		Timer.schedule(new Task(){
			@Override
			public void run(){
				currentFrame++;
				if(currentFrame > 3)
					currentFrame = 1;
			}
		},0,1/3.0f);
		xOffset = 4 * Gdx.graphics.getWidth()/5;
		yOffset = Gdx.graphics.getHeight()/5;
		width = Gdx.graphics.getWidth()/5;
		height = 4 * Gdx.graphics.getHeight()/5;
	}
	
	public void draw() throws IOException{
		game.shapeRenderer.begin(ShapeType.Filled);
		game.shapeRenderer.setColor(.6f,.3f,.1f,1);	
		game.shapeRenderer.rect(xOffset, yOffset, width, height);
		game.shapeRenderer.end();
		int scaledSize = height/12;
		if(state.selected != null){
			UnitRenderer.DrawUnit(game, state.selected, xOffset, Gdx.graphics.getHeight() - scaledSize, scaledSize, "Idle", currentFrame);
			if (!state.roster.contains(state.selected))
				UnitRenderer.DrawStockStats(game, state.selected, xOffset, Gdx.graphics.getHeight() - scaledSize, scaledSize);
			else
				UnitRenderer.DrawOwnedStats(game, state.selected, xOffset, Gdx.graphics.getHeight() - scaledSize, scaledSize);
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
		state.selected = null;
	}
}
