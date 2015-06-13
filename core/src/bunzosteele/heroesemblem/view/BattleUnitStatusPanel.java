package bunzosteele.heroesemblem.view;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
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
	
	public BattleUnitStatusPanel(HeroesEmblem game, BattleState state, int width, int height, int xOffset, int yOffset){
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
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
	}
	
	public void draw() throws IOException{
		int scaledSize = height/14;
		if(state.selected != null){
			UnitRenderer.DrawUnit(game, state.selected, xOffset, Gdx.graphics.getHeight() - scaledSize, scaledSize, "Attack", currentFrame);
			if (!state.roster.contains(state.selected))
				UnitRenderer.DrawEnemyStats(game, state.selected, xOffset, Gdx.graphics.getHeight() - scaledSize, scaledSize);
			else
				UnitRenderer.DrawOwnedStats(game, state.selected, xOffset, Gdx.graphics.getHeight() - scaledSize, scaledSize);
		}
		game.font.setColor(Color.WHITE);
		game.font.getData().setScale(.4f);
		game.font.draw(game.batcher, "Round: " + state.turnCount, xOffset + scaledSize, yOffset + game.font.getData().lineHeight);
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
