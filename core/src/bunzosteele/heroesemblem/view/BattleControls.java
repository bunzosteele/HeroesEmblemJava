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
	
	public BattleControls(HeroesEmblem game, BattleState state, int height, int endOffset, int endWidth){
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
		largeButtonWidth = endOffset/3;
		smallButtonWidth = endWidth;
		this.height = height;
	}
	
	public void draw(){	
		drawMove();
		drawAttack();
		drawAbility();
		drawEnd();
	}
	
	public void drawBackground(){
		drawMoveBackground();
		drawAttackBackground();
		drawAbilityBackground();
		drawEndBackground();
	}
	
	private void drawMove(){
		game.font.setColor(Color.WHITE);
		game.font.getData().setScale(.4f);
		game.font.draw(game.batcher, "Move", xOffset, yOffset + 3 * height / 4, largeButtonWidth, 1, false);
	}
	
	private void drawMoveBackground(){
		Color color = new Color(.3f,.3f,.3f,1);
		if (state.CanMove()){
			color = new Color(.4f,.6f,.4f,1);
		}
		if (state.isMoving){
			color = new Color(.5f,.9f,.5f,1);
		}
		
		game.shapeRenderer.setColor(color);	
		game.shapeRenderer.rect(xOffset, yOffset, largeButtonWidth, height);
	}
	
	private void drawAttack(){	
		game.font.setColor(Color.WHITE);
		game.font.getData().setScale(.4f);
		game.font.draw(game.batcher, "Attack", xOffset + largeButtonWidth, yOffset + 3 * height / 4, largeButtonWidth, 1, false);
	}
	
	private void drawAttackBackground(){
		Color color = new Color(.3f,.3f,.3f,1);
		if (state.CanAttack(state.selected)){
			color = new Color(.4f,.6f,.4f,1);
		}
		if (state.isAttacking){
			color = new Color(.5f,.9f,.5f,1);
		}
		
		game.shapeRenderer.setColor(color);	
		game.shapeRenderer.rect(xOffset + largeButtonWidth, yOffset, largeButtonWidth, height);
	}
	
	private void drawAbility(){
		game.font.setColor(Color.WHITE);
		game.font.getData().setScale(.4f);
		game.font.draw(game.batcher, "Ability", xOffset + 2 * largeButtonWidth, yOffset + 3 * height / 4, largeButtonWidth, 1, false);
	}
	
	private void drawAbilityBackground(){
		Color color = new Color(.3f,.3f,.3f,1);
		if (state.CanAttack(state.selected)){
			color = new Color(.4f,.6f,.4f,1);
		}
		if (state.isAttacking){
			color = new Color(.5f,.9f,.5f,1);
		}
		
		game.shapeRenderer.setColor(color);	
		game.shapeRenderer.rect(xOffset + 2 * largeButtonWidth, yOffset, largeButtonWidth, height);
	}
	
	private void drawEnd(){	
		game.font.setColor(Color.WHITE);
		game.font.getData().setScale(.4f);
		game.font.draw(game.batcher, "End Turn", xOffset + 3 * largeButtonWidth, yOffset + 3 * height / 4, smallButtonWidth, 1, false);
	}
	
	private void drawEndBackground(){
		Color color = new Color(.50f,.50f,.3f,1);
		if(!hasActions()){
			color = new Color(.70f,.70f,.4f,1);
		}		
		
		game.shapeRenderer.setColor(color);	
		game.shapeRenderer.rect(xOffset + 3 * largeButtonWidth, yOffset,smallButtonWidth, height);
	}
	
	private boolean hasActions(){
		for(Unit unit : state.roster){
			if(!unit.hasMoved || (!unit.hasAttacked && state.CanAttack(unit))){
				return true;
			}
		}
		return false;
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
		if(state.currentPlayer != 0)
			return;
					
		if(x >= xOffset && x < xOffset + largeButtonWidth){
			if( y >= yOffset && y < yOffset + height){
				processMoveTouch();
			}
		}
		if(x >= xOffset + largeButtonWidth && x < xOffset + 2 * largeButtonWidth){
			if( y >= yOffset && y < yOffset + height){
				processAttackTouch();
			}
		}
		if(x >= xOffset + 2* largeButtonWidth && x < xOffset + 3 * largeButtonWidth){
			if( y >= yOffset && y < yOffset + height){
				processAbilityTouch();
			}
		}
		if(x >= xOffset + 3* largeButtonWidth && x < Gdx.graphics.getWidth()){
			if( y >= yOffset && y < yOffset + height){
				processEndTouch();
			}
		}
	}
	
	public void processMoveTouch(){
		state.isAttacking = false;
		if(state.CanMove())
			state.isMoving = !state.isMoving;
	}
	
	public void processAttackTouch(){
		state.isMoving = false;
		if(state.CanAttack(state.selected))
			state.isAttacking = !state.isAttacking;
	}
	
	public void processAbilityTouch(){

	}
	
	public void processEndTouch(){
		state.EndTurn();
		
		
		// TODO: COMPUTERS TURN
		
		state.EndTurn();
	}
	
}
