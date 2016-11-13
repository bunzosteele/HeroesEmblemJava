package bunzosteele.heroesemblem.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Move;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class BattlePanel
{
	HeroesEmblem game;
	BattleState state;
	int xOffset;
	int yOffset;
	int width;
	int height;
	int buttonSize;
	int endTurnX;
	int endTurnY;
	int confirmX;
	int confirmY;
	int abilityX;
	int abilityY;
	int infoX;
	int infoY;
	int chainSize;
	int shadowSize;
	int backdropWidth;
	int backdropHeight;
	int dividerWidth;
	int dividerHeight;

	public BattlePanel(final HeroesEmblem game, final BattleState state, final int width, final int height, final int xOffset, final int yOffset)
	{
		this.game = game;
		this.state = state;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		this.buttonSize = width * 4 / 10;
		this.chainSize = 20;
		this.shadowSize = chainSize / 4; 
		this.backdropWidth = (int) (this.width * .8);
		this.backdropHeight = (int) Math.floor(backdropWidth * .353);
		this.dividerWidth = this.width - 2 * chainSize;
		this.dividerHeight = (int) (dividerWidth * .0547);
		int buttonRegionHeight =  this.height - this.chainSize - backdropHeight * 7 - shadowSize * 2 - chainSize;
		int buttonVerticalSpacing = (buttonRegionHeight - 2 * buttonSize - dividerHeight) / 4;
		this.endTurnX = xOffset + width - buttonSize - (chainSize - shadowSize) * 2;
		this.endTurnY = buttonVerticalSpacing + chainSize;
		this.confirmX = xOffset + (chainSize - shadowSize) * 2;
		this.confirmY = buttonVerticalSpacing + chainSize;
		this.abilityX = xOffset + (chainSize - shadowSize) * 2;
		this.abilityY = buttonVerticalSpacing  * 3 + dividerHeight + buttonSize + chainSize;
		this.infoX = xOffset + width - buttonSize - (chainSize - shadowSize) * 2;
		this.infoY = buttonVerticalSpacing  * 3 + dividerHeight + buttonSize + chainSize;
	}

	public void draw() throws IOException
	{
		drawBorder();
		drawUnitStats();
		drawButtons();
	}

	public void drawBackground()
	{
		Color backgroundColor = new Color(.227f, .204f, .157f, 1);
		this.game.shapeRenderer.rect(this.xOffset, this.yOffset, this.width, this.height, backgroundColor, backgroundColor, backgroundColor, backgroundColor);
	}
	
	public void drawBorder(){
		int chainXOffset = 0;
		int chainYOffset = 0;
		while (chainXOffset < width){
			this.game.batcher.draw(this.game.sprites.horizontalChain, this.xOffset + chainXOffset, Gdx.graphics.getHeight() - chainSize, chainSize, chainSize);
			this.game.batcher.draw(this.game.sprites.horizontalChain, this.xOffset + chainXOffset, -shadowSize, chainSize, chainSize);
			chainXOffset += chainSize;
		}
		while (chainYOffset < height){
			this.game.batcher.draw(this.game.sprites.verticalChain, this.xOffset, chainYOffset, chainSize, chainSize);
			this.game.batcher.draw(this.game.sprites.verticalChain, this.xOffset + this.width - (chainSize - shadowSize), chainYOffset, chainSize, chainSize);
			chainYOffset += chainSize;
		}
		
		this.game.batcher.draw(this.game.sprites.chainNW, this.xOffset, Gdx.graphics.getHeight() - (chainSize), chainSize, chainSize);
		this.game.batcher.draw(this.game.sprites.chainNE, this.xOffset + this.width - chainSize, Gdx.graphics.getHeight() - (chainSize), chainSize -1, chainSize-1);
		this.game.batcher.draw(this.game.sprites.chainSW, this.xOffset -1, 0, chainSize -1, chainSize -1);
		this.game.batcher.draw(this.game.sprites.chainSE, this.xOffset + this.width - chainSize, 0, chainSize + 1, chainSize - 1);	
	}
	
	public void drawUnitStats() throws IOException{
		if(this.state.selected != null){
			game.font.setColor(Color.BLACK);
			game.font.getData().setScale(.1f);
			
			int portraitHeight = (int) (this.buttonSize * .66);
			int nameY =  this.height - this.chainSize - portraitHeight - shadowSize * 2;
			final AtlasRegion portraitRegion = game.textureAtlas.findRegion("Portrait" + this.state.selected.type + this.state.selected.team);
			this.game.batcher.draw(new Sprite(portraitRegion), this.xOffset + this.chainSize, nameY, portraitHeight, portraitHeight);
	
			int bannerWidth = buttonSize / 4;
			int bannerHeight = (int) (bannerWidth * 1.42);
			final AtlasRegion bannerRegion = game.textureAtlas.findRegion("Banner" + this.state.selected.team);
			this.game.batcher.draw(new Sprite(bannerRegion), this.xOffset, this.height - (this.chainSize / 2) - bannerHeight, bannerWidth, bannerHeight);
			
			final AtlasRegion nameBackdropRegion = game.textureAtlas.findRegion("NameBackdrop" + this.state.selected.team);
			int nameBackdropWidth = width - chainSize * 2 - portraitHeight;
			this.game.batcher.draw(new Sprite(nameBackdropRegion), this.xOffset + (this.chainSize + portraitHeight), nameY, nameBackdropWidth, portraitHeight + shadowSize * 2);
			game.font.draw(game.batcher, state.selected.name, this.xOffset + (this.chainSize + portraitHeight), nameY + (portraitHeight + shadowSize * 2) / 2 + game.font.getData().lineHeight, nameBackdropWidth - (nameBackdropWidth / 10), 1, false);
						
			this.game.batcher.draw(this.game.sprites.healthBackdrop, this.xOffset + (width - this.backdropWidth) / 2, nameY - this.backdropHeight, this.backdropWidth, this.backdropHeight);
			game.font.draw(game.batcher, state.selected.currentHealth + "/" + state.selected.maximumHealth, this.xOffset + (width - this.backdropWidth) / 2 + this.backdropHeight, nameY - game.font.getData().lineHeight / 2, this.backdropWidth - (this.backdropWidth * 2 / 29) - this.backdropHeight - (nameBackdropWidth / 10), 1, false);
			DrawHealthBar(nameY);
			this.game.batcher.draw(this.game.sprites.experienceBackdrop, this.xOffset + (width - this.backdropWidth) / 2, nameY - this.backdropHeight * 2, this.backdropWidth, this.backdropHeight);
			game.font.draw(game.batcher, "LVL." + state.selected.level, this.xOffset + (width - this.backdropWidth) / 2 + this.backdropHeight, nameY - this.backdropHeight - game.font.getData().lineHeight / 2, this.backdropWidth - (this.backdropWidth * 2 / 29) - this.backdropHeight - (nameBackdropWidth / 10), 1, false);
			DrawExperienceBar(nameY);
			if (state.selected.team == 0 || state.perksPurchased >= 2) {
				game.font.getData().setScale(.15f);
				this.game.batcher.draw(this.game.sprites.attackBackdrop, this.xOffset + (width - this.backdropWidth) / 2, nameY - this.backdropHeight * 3, this.backdropWidth, this.backdropHeight);
				game.font.draw(game.batcher, "" + state.selected.attack, this.xOffset + (width - this.backdropWidth) / 2 + this.backdropHeight, nameY - this.backdropHeight * 5 / 2 + game.font.getData().lineHeight / 4, this.backdropWidth - (this.backdropWidth * 2 / 29) - this.backdropHeight - (nameBackdropWidth / 10), 1, false);
				this.game.batcher.draw(this.game.sprites.accuracyBackdrop, this.xOffset + (width - this.backdropWidth) / 2, nameY - this.backdropHeight * 4, this.backdropWidth, this.backdropHeight);
				game.font.draw(game.batcher, state.selected.accuracy + "%", this.xOffset + (width - this.backdropWidth) / 2 + this.backdropHeight, nameY - this.backdropHeight * 7 / 2 + game.font.getData().lineHeight / 4, this.backdropWidth - (this.backdropWidth * 2 / 29) - this.backdropHeight - (nameBackdropWidth / 10), 1, false);
				this.game.batcher.draw(this.game.sprites.defenseBackdrop, this.xOffset + (width - this.backdropWidth) / 2, nameY - this.backdropHeight * 5, this.backdropWidth, this.backdropHeight);
				UnitRenderer.SetDefenseFont(state.selected, null, state.battlefield, game.font);
				game.font.draw(game.batcher, "" + (state.selected.defense + state.battlefield.get(state.selected.y).get(state.selected.x).defenseModifier), this.xOffset + (width - this.backdropWidth) / 2 + this.backdropHeight, nameY - this.backdropHeight * 9 / 2 + game.font.getData().lineHeight / 4, this.backdropWidth - (this.backdropWidth * 2 / 29) - this.backdropHeight - (nameBackdropWidth / 10), 1, false);
				this.game.batcher.draw(this.game.sprites.evasionBackdrop, this.xOffset + (width - this.backdropWidth) / 2, nameY - this.backdropHeight * 6, this.backdropWidth, this.backdropHeight);
				UnitRenderer.SetEvasionFont(state.selected, null, state.battlefield, game.font);
				game.font.draw(game.batcher, (state.selected.evasion + state.battlefield.get(state.selected.y).get(state.selected.x).evasionModifier) + "%", this.xOffset + (width - this.backdropWidth) / 2 + this.backdropHeight, nameY - this.backdropHeight * 11 / 2 + game.font.getData().lineHeight / 4, this.backdropWidth - (this.backdropWidth * 2 / 29) - this.backdropHeight - (nameBackdropWidth / 10), 1, false);
			}
			
			game.font.getData().setScale(.33f);
		}
	}
	
	public void drawButtons(){
		
		if(this.state.currentPlayer == 0){
			if (!this.hasActions())
			{
				this.game.batcher.draw(this.game.sprites.endTurnEmphasized, this.endTurnX, this.endTurnY, buttonSize, buttonSize);
			}else{
				this.game.batcher.draw(this.game.sprites.endTurnEnabled, this.endTurnX, this.endTurnY, buttonSize, buttonSize);
			}
		}else{
			this.game.batcher.draw(this.game.sprites.endTurnDisabled, this.endTurnX, this.endTurnY, buttonSize, buttonSize);
		}
		
		if(this.state.selected != null && this.state.selected.ability != null && this.state.CanUseAbility(this.state.selected)){
			if(this.state.isUsingAbility){
				this.game.batcher.draw(this.game.sprites.abilityEmphasis, this.abilityX, this.abilityY, buttonSize, buttonSize);
			}else{
				this.game.batcher.draw(this.game.sprites.abilityEnabled, this.abilityX, this.abilityY, buttonSize, buttonSize);
			}
		}else{
			this.game.batcher.draw(this.game.sprites.abilityDisabled, this.abilityX, this.abilityY, buttonSize, buttonSize);
		}
		
		if(this.state.targeted != null){
			this.game.batcher.draw(this.game.sprites.confirmEnabled, this.confirmX, this.confirmY, buttonSize, buttonSize);	
		}else if(this.state.CanUndo()){
			this.game.batcher.draw(this.game.sprites.undoEnabled, this.confirmX, this.confirmY, buttonSize, buttonSize);	
		}else{
			this.game.batcher.draw(this.game.sprites.confirmDisabled, this.confirmX, this.confirmY, buttonSize, buttonSize);	
		}
		
		
		this.game.batcher.draw(this.game.sprites.infoDisabled, this.infoX, this.infoY, buttonSize, buttonSize);
		
		this.game.batcher.draw(this.game.sprites.controlsDivider, this.chainSize + this.xOffset, this.endTurnY + buttonSize - dividerHeight / 2 + (abilityY - buttonSize - endTurnY) / 2, dividerWidth, dividerHeight);
	}
	
	private void DrawHealthBar(int nameY){
		int leftOffset = (int) (this.backdropWidth * .396);
		int rightOffset = (int) (this.backdropWidth * .155);
		int barWidth = this.backdropWidth - leftOffset - rightOffset;
		int barHeight = (int) (barWidth * .302);

		if(state.selected != null){
			int healthPercent =  state.selected.currentHealth * 100 / state.selected.maximumHealth;
			int healthIndex = Math.floorDiv(healthPercent, 5);
			if(healthIndex == 0 && !state.selected.isDying)
				healthIndex++;
			
			if(healthIndex == 20 && state.selected.currentHealth != state.selected.maximumHealth)
				healthIndex--;
				
			int barYOffset = (int) (this.backdropHeight * .122);
			this.game.batcher.draw(new Sprite(game.textureAtlas.findRegion("Health" + healthIndex)), this.xOffset + (width - this.backdropWidth) / 2 + leftOffset, nameY - this.backdropHeight + barYOffset, barWidth, barHeight);
		}
		
	}
	
	private void DrawExperienceBar(int nameY){
		int leftOffset = (int) (this.backdropWidth * .396);
		int rightOffset = (int) (this.backdropWidth * .155);
		int barWidth = this.backdropWidth - leftOffset - rightOffset;
		int barHeight = (int) (barWidth * .302);
		
		if(state.selected != null){
			int experiencePercent =  state.selected.experience * 100 / state.selected.experienceNeeded;
			int experienceIndex = Math.floorDiv(experiencePercent, 5);
			
			if(experienceIndex == 20 && state.selected.experience != state.selected.experienceNeeded)
				experienceIndex--;
			
			int barYOffset = (int) (this.backdropHeight * .122);
			this.game.batcher.draw(new Sprite(game.textureAtlas.findRegion("Experience" + experienceIndex)), this.xOffset + (width - this.backdropWidth) / 2 + leftOffset, nameY + barYOffset - this.backdropHeight * 2, barWidth, barHeight);
		}	
	}
	
	private boolean hasActions()
	{
		for (final Unit unit : this.state.roster)
		{
			if (!this.state.IsTapped(unit))
			{
				return true;
			}
		}
		return false;
	}


	public boolean isTouched(final float x, final float y)
	{
		if ((x >= this.xOffset) && (x < (this.xOffset + this.width)))
		{
			if ((y >= this.yOffset) && (y < (this.yOffset + this.height)))
			{
				return true;
			}
		}
		return false;
	}

	public void processTouch(final float x, final float y)
	{
		int clickedX = Gdx.input.getX();
		int clickedY = Gdx.graphics.getHeight() - Gdx.input.getY();
		if((clickedX > endTurnX && clickedX < endTurnX + buttonSize) && (clickedY > endTurnY && clickedY < endTurnY + buttonSize)){
			processEndTouch();
		}else if((clickedX > abilityX && clickedX < abilityX + buttonSize) && (clickedY > abilityY && clickedY < abilityY + buttonSize)){
			processAbilityTouch();
		}else if((clickedX > confirmX && clickedX < confirmX + buttonSize) && (clickedY > confirmY && clickedY < confirmY + buttonSize)){
			processConfirmTouch();
		}
		else{
			this.state.selected = null;
			this.state.targeted = null;
			this.state.isMoving = false;
			this.state.isUsingAbility = false;
		}
	}
	
	private void processEndTouch()
	{
		this.state.EndTurn();
	}
	
	private void processAbilityTouch()
	{
		this.state.isMoving = false;
		this.state.targeted = null;
		if (this.state.CanUseAbility(this.state.selected))
		{
			this.state.isUsingAbility = !this.state.isUsingAbility;
			if (!this.state.selected.ability.areTargetsPersistent)
			{
				this.state.selected.ability.targets = new ArrayList<Integer>();
			}
		}
			
		if(!this.state.isUsingAbility && this.state.CanMove()){
			this.state.isMoving = true;
		}
	}
	
	public void processConfirmTouch()
	{
		if(this.state.targeted != null){
			this.state.ConfirmAttack();
		}else if(this.state.CanUndo()){
			Move previous = this.state.undos.pop();
			for(Unit unit : this.state.AllUnits()){
				if(unit.id == previous.unitId)
					this.state.selected = unit;
			}
			this.state.selected.x = previous.oldX;
			this.state.selected.y = previous.oldY;
			this.state.selected.hasMoved = false;
			this.state.isMoving = true;		
		}
		this.state.isUsingAbility = false;
	}
}
