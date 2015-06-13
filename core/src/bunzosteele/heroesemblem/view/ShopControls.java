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
import bunzosteele.heroesemblem.model.ShopState;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitGenerator;

public class ShopControls {
	HeroesEmblem game;
	ShopState state;
	int idleFrame = 1;
	int attackFrame = 1;
	int xOffset;
	int yOffset;
	int buttonWidth;
	int rosterWidth;
	int height;
	
	public ShopControls(HeroesEmblem game, ShopState state, int buttonWidth, int rosterWidth, int height){
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
		this.buttonWidth = buttonWidth;
		this.rosterWidth = rosterWidth;
		this.height = height;
	}
	
	public void draw(){	
		drawBuy();
		drawRoster();
		drawComplete();
	}
	
	public void drawBackground(){
		drawBuyBackground();
		drawRosterBackground();
		drawCompleteBackground();
	}
	
	private void drawBuy(){		
		game.font.setColor(Color.WHITE);
		game.font.getData().setScale(.4f);
		game.font.draw(game.batcher, "Buy", xOffset, yOffset + 3 * height / 4, buttonWidth, 1, false);
	}
	
	private void drawBuyBackground(){
		Color color = new Color(.3f,.3f,.3f,1);
		if(canPurchaseSelected())
			color = new Color(.3f,.8f,.3f,1);
		game.shapeRenderer.setColor(color);	
		game.shapeRenderer.rect(xOffset, yOffset, rosterWidth, height);
	}
	
	private void drawRoster(){
		int unitOffset = 0;
		int columnWidth = rosterWidth / 10; 
		int gapWidth = (2 * rosterWidth/10) / 9;
		
		for(Unit unit : state.roster){
			if(state.selected != null && state.selected.isEquivalentTo(unit))
				UnitRenderer.DrawUnit(game, unit, xOffset + buttonWidth + gapWidth + (gapWidth * unitOffset +1) + (columnWidth * unitOffset), (height - columnWidth) /2, columnWidth, "Attack", attackFrame);
			else
				UnitRenderer.DrawUnit(game, unit, xOffset + buttonWidth + gapWidth + (gapWidth * unitOffset +1) + (columnWidth * unitOffset), (height - columnWidth) /2, columnWidth, "Idle", idleFrame);
			unitOffset++;
		}
	}
	
	private void drawRosterBackground(){
		game.shapeRenderer.setColor(.6f,.3f,.1f,1);	
		game.shapeRenderer.rect(buttonWidth, yOffset, rosterWidth, height);
	}
	
	private void drawComplete(){
		game.font.setColor(Color.WHITE);
		game.font.getData().setScale(.4f);
		game.font.draw(game.batcher, "Complete", buttonWidth + rosterWidth, yOffset + 3 * height / 4, buttonWidth, 1, false);
	}
	
	private void drawCompleteBackground(){
		Color color = new Color(.3f,.3f,.3f,1);
		if(canStartGame())
			color = new Color(.3f,.8f,.3f,1);
		
		boolean canAffordMoreUnits = false;
		for(Unit unit : state.stock){
			if(unit.cost < state.gold)
				canAffordMoreUnits = true;
		}
		
		if(!canAffordMoreUnits || state.roster.size() >= 8)
			color = new Color(.4f,1f,.4f,1);		
		
		game.shapeRenderer.setColor(color);	
		game.shapeRenderer.rect(buttonWidth + rosterWidth, yOffset, buttonWidth, height);
	}
		
	public boolean isTouched(float x, float y){
		if(x >= xOffset && x < xOffset + 2 * buttonWidth + rosterWidth){
			if( y >= yOffset && y < yOffset + height){
				return true;
			}
		}
		return false;
	}
	
	public void processTouch(float x, float y) throws IOException{
		if(x >= xOffset && x < xOffset + buttonWidth){
			if( y >= yOffset && y < yOffset + height){
				processBuyTouch();
			}
		}
		if(x >= xOffset + buttonWidth && x < xOffset + buttonWidth + rosterWidth){
			if( y >= yOffset && y < yOffset + height){
				processRosterTouch(x);
			}
		}
		if(x >= xOffset + buttonWidth + rosterWidth && x < xOffset + 2* buttonWidth + rosterWidth){
			if( y >= yOffset && y < yOffset + height){
				processCompleteTouch();
			}
		}
	}
	
	private void processBuyTouch() throws IOException{
		if (canPurchaseSelected()){
			state.roster.add(state.selected);
			state.gold -= state.selected.cost;
			state.selected = null;
			state.stock = UnitGenerator.GenerateStock();
		}
	}
	
	private void processRosterTouch(float x){		
		int unitOffset = 0;
		int columnWidth = rosterWidth / 10; 
		int gapWidth = (2 * rosterWidth/10) / 9;
		boolean isHit = false;
		
		for(Unit unit : state.roster){
			int lowerXBound = xOffset + buttonWidth + gapWidth + (gapWidth * unitOffset +1)+ (columnWidth * unitOffset);
			int upperXBound = xOffset + buttonWidth + gapWidth + (gapWidth * unitOffset +1)+ (columnWidth * unitOffset) + columnWidth;
			
			if ( x >= lowerXBound && x <= upperXBound){
				isHit = true;
				state.selected = state.roster.get(unitOffset);
			}			
			unitOffset++;
		}
		if(!isHit){
			state.selected = null;
		}
	}
	
	private void processCompleteTouch() throws IOException{
		if(canStartGame()){
			game.setScreen(new BattleScreen(game, state));
			return;
		}			
	}
	
	private boolean canPurchaseSelected(){
		return state.selected != null && state.gold >= state.selected.cost && state.roster.size() < 8 && !state.roster.contains(state.selected);
	}
	
	private boolean canStartGame(){
		return state.roster.size() > 0;
	}
}
