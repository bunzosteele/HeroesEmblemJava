package bunzosteele.heroesemblem.view;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.BattleState;


public class BattleUnitStatusPanel {
	HeroesEmblem game;
	BattleState state;
	int currentFrame = 1;
	int xOffset;
	int yOffset;
	int width;
	int height;
	
	public BattleUnitStatusPanel(HeroesEmblem game, BattleState state){
		this.game = game;
		this.state = state;
		Timer.schedule(new Task(){
			@Override
			public void run(){
				currentFrame++;
				if(currentFrame > 2)
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
			UnitRenderer.DrawUnit(game, state.selected, xOffset, Gdx.graphics.getHeight() - scaledSize, scaledSize, "Attack", currentFrame);
			if (!state.roster.contains(state.selected))
				UnitRenderer.DrawEnemyStats(game, state.selected, xOffset, Gdx.graphics.getHeight() - scaledSize, scaledSize);
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
