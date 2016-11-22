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
		buttonSize = width * 4 / 10;
		chainSize = 20;
		shadowSize = chainSize / 4; 
		backdropWidth = (int) (width * .8);
		backdropHeight = (int) Math.floor(backdropWidth * .353);
		dividerWidth = width - 2 * chainSize;
		dividerHeight = (int) (dividerWidth * .0547);
		int buttonRegionHeight =  height - chainSize - backdropHeight * 7 - shadowSize * 2 - chainSize;
		int buttonVerticalSpacing = (buttonRegionHeight - 2 * buttonSize - dividerHeight) / 4;
		endTurnX = xOffset + width - buttonSize - chainSize;
		endTurnY = buttonVerticalSpacing + chainSize;
		confirmX = xOffset + chainSize;
		confirmY = buttonVerticalSpacing + chainSize;
		abilityX = xOffset + chainSize;
		abilityY = buttonVerticalSpacing  * 3 + dividerHeight + buttonSize + chainSize;
		infoX = xOffset + width - buttonSize - chainSize;
		infoY = buttonVerticalSpacing  * 3 + dividerHeight + buttonSize + chainSize;
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
		game.shapeRenderer.rect(xOffset, yOffset, width, height, backgroundColor, backgroundColor, backgroundColor, backgroundColor);
	}
	
	public void drawBorder(){
		int chainXOffset = chainSize - shadowSize;
		int chainYOffset = chainSize - shadowSize;
		while (chainXOffset < width - (chainSize - shadowSize)){
			game.batcher.draw(game.sprites.ChainHorizontal, xOffset + chainXOffset, Gdx.graphics.getHeight() - chainSize, chainSize - shadowSize, chainSize);
			game.batcher.draw(game.sprites.ChainHorizontal, xOffset + chainXOffset, -shadowSize, chainSize - shadowSize, chainSize);
			chainXOffset += chainSize - shadowSize;
		}
		while (chainYOffset < height - (chainSize - shadowSize)){
			game.batcher.draw(game.sprites.ChainVertical, xOffset, chainYOffset, chainSize, chainSize - shadowSize);
			game.batcher.draw(game.sprites.ChainVertical, xOffset + width - (chainSize - shadowSize), chainYOffset, chainSize, chainSize - shadowSize);
			chainYOffset += chainSize - shadowSize;
		}
		
		game.batcher.draw(game.sprites.NewChainNW, xOffset, Gdx.graphics.getHeight() - (chainSize - shadowSize), chainSize - shadowSize, chainSize - shadowSize);
		game.batcher.draw(game.sprites.NewChainNE, xOffset + width - (chainSize - shadowSize), Gdx.graphics.getHeight() - (chainSize - shadowSize), chainSize - shadowSize, chainSize - shadowSize);
		game.batcher.draw(game.sprites.NewChainSW, xOffset, 0, chainSize - shadowSize, chainSize - shadowSize);
		game.batcher.draw(game.sprites.NewChainSE, xOffset + width - (chainSize - shadowSize), 0, chainSize - shadowSize, chainSize - shadowSize);
	}
	
	public void drawUnitStats() throws IOException{
		if(state.selected != null){
			game.font.setColor(Color.BLACK);
			game.font.getData().setScale(.25f);
			
			int portraitHeight = (int) (buttonSize * .66);
			int nameY =  height - chainSize - portraitHeight - shadowSize * 2;
			final AtlasRegion portraitRegion = game.textureAtlas.findRegion("Portrait" + state.selected.type + state.selected.team);
			game.batcher.draw(new Sprite(portraitRegion), xOffset + chainSize, nameY, portraitHeight, portraitHeight);
	
			int bannerWidth = buttonSize / 4;
			int bannerHeight = (int) (bannerWidth * 1.42);
			final AtlasRegion bannerRegion = game.textureAtlas.findRegion("Banner" + state.selected.team);
			game.batcher.draw(new Sprite(bannerRegion), xOffset, height - (chainSize / 2) - bannerHeight, bannerWidth, bannerHeight);
			
			final AtlasRegion nameBackdropRegion = game.textureAtlas.findRegion("NameBackdrop" + state.selected.team);
			int nameBackdropWidth = width - chainSize * 2 - portraitHeight;
			game.batcher.draw(new Sprite(nameBackdropRegion), xOffset + (chainSize + portraitHeight), nameY, nameBackdropWidth, portraitHeight + shadowSize * 2);
			game.font.draw(game.batcher, state.selected.name, xOffset + (chainSize + portraitHeight), nameY + (portraitHeight + shadowSize * 2) / 2 + game.font.getData().lineHeight, nameBackdropWidth - (nameBackdropWidth / 10), 1, false);
			game.font.draw(game.batcher, state.selected.type.toString(), xOffset + (chainSize + portraitHeight), nameY + (portraitHeight + shadowSize * 2) / 2 - game.font.getData().lineHeight /4 , nameBackdropWidth - (nameBackdropWidth / 10), 1, false);	
			game.batcher.draw(game.sprites.HealthBackdrop, xOffset + (width - backdropWidth) / 2, nameY - backdropHeight, backdropWidth, backdropHeight);
			game.font.draw(game.batcher, state.selected.currentHealth + "/" + state.selected.maximumHealth, xOffset + (width - backdropWidth) / 2 + backdropHeight, nameY - game.font.getData().lineHeight / 2, backdropWidth - (backdropWidth * 2 / 29) - backdropHeight - (nameBackdropWidth / 10), 1, false);
			DrawHealthBar(nameY);
			game.batcher.draw(game.sprites.ExperienceBackdrop, xOffset + (width - backdropWidth) / 2, nameY - backdropHeight * 2, backdropWidth, backdropHeight);
			game.font.draw(game.batcher, (state.selected.level < 10 ? "Lvl. 0" : "Lvl. ") + state.selected.level, xOffset + (width - backdropWidth) / 2 + backdropHeight, nameY - backdropHeight - game.font.getData().lineHeight / 2, backdropWidth - (backdropWidth * 2 / 29) - backdropHeight - (nameBackdropWidth / 10), 1, false);
			DrawExperienceBar(nameY);
			if (state.selected.team == 0 || state.perksPurchased >= 2) {
				game.font.getData().setScale(.35f);
				int statTextXOffset = xOffset + (width - backdropWidth) / 2 + backdropWidth * 57 / 116;
				int statTextWidth = backdropWidth * 32 / 116;
				int statRelativeYOffset = backdropHeight * 2 / 41 + (backdropHeight * 25 / 41) / 2 ;
				game.batcher.draw(game.sprites.AttackBackdrop, xOffset + (width - backdropWidth) / 2, nameY - backdropHeight * 3, backdropWidth, backdropHeight);	
				game.font.draw(game.batcher, "" + state.selected.attack, statTextXOffset, nameY - backdropHeight * 2 - statRelativeYOffset, statTextWidth, 1, false);
				game.batcher.draw(game.sprites.AccuracyBackdrop, xOffset + (width - backdropWidth) / 2, nameY - backdropHeight * 4, backdropWidth, backdropHeight);
				game.font.draw(game.batcher, "" + state.selected.accuracy, statTextXOffset, nameY - backdropHeight * 3 - statRelativeYOffset, statTextWidth, 1, false);
				game.batcher.draw(game.sprites.DefenseBackdrop, xOffset + (width - backdropWidth) / 2, nameY - backdropHeight * 5, backdropWidth, backdropHeight);
				UnitRenderer.SetDefenseFont(state.selected, null, state.battlefield, game.font);
				game.font.draw(game.batcher, "" + (state.selected.defense + state.battlefield.get(state.selected.y).get(state.selected.x).defenseModifier), statTextXOffset, nameY - backdropHeight * 4 - statRelativeYOffset, statTextWidth, 1, false);
				game.batcher.draw(game.sprites.EvasionBackdrop, xOffset + (width - backdropWidth) / 2, nameY - backdropHeight * 6, backdropWidth, backdropHeight);
				UnitRenderer.SetEvasionFont(state.selected, null, state.battlefield, game.font);
				game.font.draw(game.batcher, "" + (state.selected.evasion + state.battlefield.get(state.selected.y).get(state.selected.x).evasionModifier), statTextXOffset, nameY - backdropHeight * 5 - statRelativeYOffset, statTextWidth, 1, false);
			}
			
			game.font.getData().setScale(.33f);
		}
	}
	
	public void drawButtons(){
		
		if(state.currentPlayer == 0){
			if (!hasActions())
			{
				game.batcher.draw(game.sprites.EndTurnEmphasized, endTurnX, endTurnY, buttonSize, buttonSize);
			}else{
				game.batcher.draw(game.sprites.EndTurnEnabled, endTurnX, endTurnY, buttonSize, buttonSize);
			}
		}else{
			game.batcher.draw(game.sprites.EndTurnDisabled, endTurnX, endTurnY, buttonSize, buttonSize);
		}
		
		if(state.selected != null && state.selected.ability != null && state.CanUseAbility(state.selected)){
			if(state.isUsingAbility){
				game.batcher.draw(game.sprites.AbilityEmphasis, abilityX, abilityY, buttonSize, buttonSize);
			}else{
				game.batcher.draw(game.sprites.AbilityEnabled, abilityX, abilityY, buttonSize, buttonSize);
			}
		}else{
			game.batcher.draw(game.sprites.AbilityDisabled, abilityX, abilityY, buttonSize, buttonSize);
		}
		if(state.selected != null && state.selected.ability != null){
			game.font.getData().setScale(.20f);
			game.font.setColor(new Color(0f, 0f, 0f, 1f));
			game.font.draw(game.batcher, state.selected.ability.displayName, abilityX, abilityY + buttonSize / 2 + game.font.getLineHeight() / 2, buttonSize, 1, false);
		}
		
		if(state.targeted != null){
			game.batcher.draw(game.sprites.ConfirmEnabled, confirmX, confirmY, buttonSize, buttonSize);	
		}else if(state.CanUndo()){
			game.batcher.draw(game.sprites.UndoEnabled, confirmX, confirmY, buttonSize, buttonSize);	
		}else{
			game.batcher.draw(game.sprites.ConfirmDisabled, confirmX, confirmY, buttonSize, buttonSize);	
		}
		
		
		game.batcher.draw(game.sprites.InfoDisabled, infoX, infoY, buttonSize, buttonSize);
		
		game.batcher.draw(game.sprites.ControlsDivider, chainSize + xOffset, endTurnY + buttonSize - dividerHeight / 2 + (abilityY - buttonSize - endTurnY) / 2, dividerWidth, dividerHeight);
	}
	
	private void DrawHealthBar(int nameY){
		int leftOffset = (int) (backdropWidth * .396);
		int rightOffset = (int) (backdropWidth * .155);
		int barWidth = backdropWidth - leftOffset - rightOffset;
		int barHeight = (int) (barWidth * .302);

		if(state.selected != null){
			int healthPercent =  state.selected.currentHealth * 100 / state.selected.maximumHealth;
			int healthIndex = (int) (healthPercent / (double)  5);
			if(healthIndex == 0 && !state.selected.isDying)
				healthIndex++;
			
			if(healthIndex == 20 && state.selected.currentHealth != state.selected.maximumHealth)
				healthIndex--;
				
			int barYOffset = (int) (backdropHeight * .122);
			game.batcher.draw(new Sprite(game.textureAtlas.findRegion("Health" + healthIndex)), xOffset + (width - backdropWidth) / 2 + leftOffset, nameY - backdropHeight + barYOffset, barWidth, barHeight);
		}
		
	}
	
	private void DrawExperienceBar(int nameY){
		int leftOffset = (int) (backdropWidth * .396);
		int rightOffset = (int) (backdropWidth * .155);
		int barWidth = backdropWidth - leftOffset - rightOffset;
		int barHeight = (int) (barWidth * .302);
		
		if(state.selected != null){
			int experiencePercent =  state.selected.experience * 100 / state.selected.experienceNeeded;
			int experienceIndex = (int) (experiencePercent / (double) 5);
			
			if(experienceIndex == 20 && state.selected.experience != state.selected.experienceNeeded)
				experienceIndex--;
			
			int barYOffset = (int) (backdropHeight * .122);
			game.batcher.draw(new Sprite(game.textureAtlas.findRegion("Experience" + experienceIndex)), xOffset + (width - backdropWidth) / 2 + leftOffset, nameY + barYOffset - backdropHeight * 2, barWidth, barHeight);
		}	
	}
	
	private boolean hasActions()
	{
		for (final Unit unit : state.roster)
		{
			if (!state.IsTapped(unit))
			{
				return true;
			}
		}
		return false;
	}


	public boolean isTouched(final float x, final float y)
	{
		if ((x >= xOffset) && (x < (xOffset + width)))
		{
			if ((y >= yOffset) && (y < (yOffset + height)))
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
			state.selected = null;
			state.targeted = null;
			state.isMoving = false;
			state.isUsingAbility = false;
		}
	}
	
	private void processEndTouch()
	{
		state.EndTurn();
	}
	
	private void processAbilityTouch()
	{
		state.isMoving = false;
		state.targeted = null;
		if (state.CanUseAbility(state.selected))
		{
			state.isUsingAbility = !state.isUsingAbility;
			if (!state.selected.ability.areTargetsPersistent)
			{
				state.selected.ability.targets = new ArrayList<Integer>();
			}
		}
			
		if(!state.isUsingAbility && state.CanMove()){
			state.isMoving = true;
		}
	}
	
	public void processConfirmTouch()
	{
		if(state.targeted != null){
			state.ConfirmAttack();
		}else if(state.CanUndo()){
			Move previous = state.undos.pop();
			for(Unit unit : state.AllUnits()){
				if(unit.id == previous.unitId)
					state.selected = unit;
			}
			state.selected.x = previous.oldX;
			state.selected.y = previous.oldY;
			state.selected.hasMoved = false;
			state.isMoving = true;		
		}
		state.isUsingAbility = false;
	}
}
