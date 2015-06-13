package bunzosteele.heroesemblem.view;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.ShopState;

public class ShopUnitStatusPanel {
	HeroesEmblem game;
	ShopState state;
	int currentFrame = 1;
	int xOffset;
	int yOffset;
	int width;
	int height;
	
	public ShopUnitStatusPanel(HeroesEmblem game, ShopState state, int width, int height, int xOffset, int yOffset){
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
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
	}
	
	public void draw() throws IOException{
		int scaledSize = height/12;
		if(state.selected != null){
			UnitRenderer.DrawUnit(game, state.selected, xOffset, Gdx.graphics.getHeight() - scaledSize, scaledSize, "Idle", currentFrame);
			if (!state.roster.contains(state.selected))
				UnitRenderer.DrawStockStats(game, state.selected, xOffset, Gdx.graphics.getHeight() - scaledSize, scaledSize);
			else
				UnitRenderer.DrawOwnedStats(game, state.selected, xOffset, Gdx.graphics.getHeight() - scaledSize, scaledSize);
		}
	}
	
	public void drawBackground(){
		game.shapeRenderer.setColor(.6f,.3f,.1f,1);	
		game.shapeRenderer.rect(xOffset, yOffset, width, height);
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
