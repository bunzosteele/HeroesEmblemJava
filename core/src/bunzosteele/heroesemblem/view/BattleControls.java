package bunzosteele.heroesemblem.view;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.ShopState;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitGenerator;

public class BattleControls {
	HeroesEmblem game;
	BattleState state;
	int idleFrame = 1;
	int attackFrame = 1;
	int xOffset;
	int yOffset;
	int largeButtonWidth;
	int smallButtonWidth;
	int height;
	
	public BattleControls(HeroesEmblem game, BattleState state){
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
			}
		},0,1/3.0f);
		xOffset = 0;
		yOffset = 0;
		largeButtonWidth = (4* Gdx.graphics.getWidth() /5) /3;
		smallButtonWidth = Gdx.graphics.getWidth() /5;
		height = Gdx.graphics.getHeight()/ 5;
	}
	
	public void draw(){	
		drawMove();
		drawAttack();
		drawAbility();
		drawEnd();
	}
	
	private void drawMove(){
		Color color = new Color(.3f,.3f,.3f,1);
		
		game.shapeRenderer.begin(ShapeType.Filled);
		game.shapeRenderer.setColor(color);	
		game.shapeRenderer.rect(xOffset, yOffset, largeButtonWidth, height);
		game.shapeRenderer.end();
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("LeagueGothic-CondensedRegular.otf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = height/2;
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		game.batcher.begin();
		font.draw(game.batcher, "Move", xOffset, yOffset + 3 * height / 4, largeButtonWidth, 1, false);
		game.batcher.end();
		font.dispose();
	}
	
	private void drawAttack(){
		Color color = new Color(.3f,.3f,.3f,1);
		
		game.shapeRenderer.begin(ShapeType.Filled);
		game.shapeRenderer.setColor(color);	
		game.shapeRenderer.rect(xOffset + largeButtonWidth, yOffset, largeButtonWidth, height);
		game.shapeRenderer.end();
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("LeagueGothic-CondensedRegular.otf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = height/2;
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		game.batcher.begin();
		font.draw(game.batcher, "Attack", xOffset + largeButtonWidth, yOffset + 3 * height / 4, largeButtonWidth, 1, false);
		game.batcher.end();
		font.dispose();
	}
	private void drawAbility(){
		Color color = new Color(.3f,.3f,.3f,1);
		
		game.shapeRenderer.begin(ShapeType.Filled);
		game.shapeRenderer.setColor(color);	
		game.shapeRenderer.rect(xOffset + 2 * largeButtonWidth, yOffset, largeButtonWidth, height);
		game.shapeRenderer.end();
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("LeagueGothic-CondensedRegular.otf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = height/2;
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		game.batcher.begin();
		font.draw(game.batcher, "Ability", xOffset + 2 * largeButtonWidth, yOffset + 3 * height / 4, largeButtonWidth, 1, false);
		game.batcher.end();
		font.dispose();
	}
	private void drawEnd(){
		Color color = new Color(.3f,.3f,.3f,1);
		
		game.shapeRenderer.begin(ShapeType.Filled);
		game.shapeRenderer.setColor(color);	
		game.shapeRenderer.rect(xOffset + 3 * largeButtonWidth, yOffset,smallButtonWidth, height);
		game.shapeRenderer.end();
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("LeagueGothic-CondensedRegular.otf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = height/2;
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		game.batcher.begin();
		font.draw(game.batcher, "End Turn", xOffset + 3 * largeButtonWidth, yOffset + 3 * height / 4, smallButtonWidth, 1, false);
		game.batcher.end();
		font.dispose();
	}
		
	public boolean isTouched(float x, float y){
		if(x >= xOffset && x < xOffset + 3 * largeButtonWidth + smallButtonWidth){
			if( y >= yOffset && y < yOffset + height){
				return true;
			}
		}
		return false;
	}
	
	public void processTouch(float x, float y) throws IOException{
		if(x >= xOffset && x < xOffset + largeButtonWidth){
			if( y >= yOffset && y < yOffset + height){
				processBuyTouch();
			}
		}
	}
	
	private void processBuyTouch() throws IOException{
	}
}
