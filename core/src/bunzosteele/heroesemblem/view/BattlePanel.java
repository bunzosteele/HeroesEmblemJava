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
	int summaryWidth;
	int summaryHeight;
	int nameBackdropWidth;
	int nameBackdropHeight;
	int nameY;

	public BattlePanel(final HeroesEmblem game, final BattleState state, final int width, final int height, final int xOffset, final int yOffset)
	{
		this.game = game;
		this.state = state;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		buttonSize = width / 3;
		chainSize = width / 15;
		shadowSize = chainSize / 3; 
		backdropWidth = (int) (width * .8);
		backdropHeight = (int) Math.floor(backdropWidth * .353);
		dividerWidth = width - buttonSize;
		dividerHeight = (int) (dividerWidth * .0547);
		int buttonRegionHeight =  height - chainSize - backdropHeight * 7 - shadowSize * 2 - chainSize;
		int buttonVerticalSpacing = (buttonRegionHeight - 2 * buttonSize - dividerHeight) / 4;
		endTurnX = xOffset + width - buttonSize - buttonSize / 3;
		endTurnY = buttonVerticalSpacing + chainSize;
		confirmX = xOffset + buttonSize / 3;
		confirmY = buttonVerticalSpacing + chainSize;
		abilityX = xOffset + buttonSize / 3;
		abilityY = buttonVerticalSpacing  * 3 + dividerHeight + buttonSize + chainSize;
		infoX = xOffset + width - buttonSize - buttonSize / 3;
		infoY = buttonVerticalSpacing  * 3 + dividerHeight + buttonSize + chainSize;	
		summaryWidth = buttonSize * 47 / 100;
		summaryHeight = summaryWidth * 40 / 47;
		nameBackdropWidth = buttonSize * 2 - chainSize - shadowSize;
		nameBackdropHeight = nameBackdropWidth * 44 / 92;
		nameY =  height + yOffset - chainSize - shadowSize - nameBackdropHeight;
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
		int chainXOffset = xOffset;
		int chainYOffset = height + yOffset - chainSize;
		
		while (chainXOffset < xOffset + width){
			if(chainXOffset > xOffset && chainXOffset < xOffset + width - chainSize)
				game.batcher.draw(game.sprites.ChainHorizontal, chainXOffset, yOffset + height - chainSize - shadowSize, chainSize, chainSize + shadowSize);
			game.batcher.draw(game.sprites.ChainHorizontal, chainXOffset, yOffset - shadowSize, chainSize, chainSize + shadowSize);
			chainXOffset += chainSize;
		}
		while (chainYOffset >= yOffset){
			if(chainYOffset > yOffset && chainYOffset < yOffset + height - chainSize)
				game.batcher.draw(game.sprites.ChainVertical, xOffset, chainYOffset, chainSize + shadowSize, chainSize);
			game.batcher.draw(game.sprites.ChainVertical, xOffset + width - chainSize, chainYOffset, chainSize + shadowSize, chainSize);
			chainYOffset -= chainSize;
		}
		
		game.batcher.draw(game.sprites.ChainNW, xOffset, yOffset + height - chainSize, chainSize, chainSize);
		game.batcher.draw(game.sprites.ChainNE, xOffset + width - chainSize, yOffset + height - chainSize, chainSize, chainSize);
		game.batcher.draw(game.sprites.ChainSW, xOffset, yOffset, chainSize, chainSize);
		game.batcher.draw(game.sprites.ChainSE, xOffset + width - chainSize, yOffset, chainSize, chainSize);
	}
	
	public void drawUnitStats() throws IOException{
		if(state.selected != null){
			game.font.setColor(Color.BLACK);
			game.font.getData().setScale(.20f);
			
			int portraitHeight = buttonSize - chainSize - shadowSize;
			final AtlasRegion portraitRegion = game.textureAtlas.findRegion("Portrait" + state.selected.type + state.selected.team);
			game.batcher.draw(new Sprite(portraitRegion), xOffset + chainSize + shadowSize, nameY + shadowSize, portraitHeight, portraitHeight);
	
			int bannerWidth = buttonSize / 4;
			int bannerHeight = (int) (bannerWidth * 1.42);
			final AtlasRegion bannerRegion = game.textureAtlas.findRegion("Banner" + state.selected.team);
			game.batcher.draw(new Sprite(bannerRegion), xOffset + chainSize - shadowSize, height - chainSize + shadowSize - bannerHeight, bannerWidth, bannerHeight);
			
			final AtlasRegion nameBackdropRegion = game.textureAtlas.findRegion("NameBackdrop" + state.selected.team);
			game.batcher.draw(new Sprite(nameBackdropRegion), xOffset + (chainSize + portraitHeight) + shadowSize, yOffset + height - chainSize - shadowSize - nameBackdropHeight, nameBackdropWidth, nameBackdropHeight);
			game.font.draw(game.batcher, state.selected.name, xOffset + (chainSize + portraitHeight) + shadowSize, nameY + (portraitHeight + shadowSize * 2) / 2 + game.font.getData().lineHeight, nameBackdropWidth - (nameBackdropWidth / 10), 1, false);
			game.font.draw(game.batcher, state.selected.type.toString(), xOffset + (chainSize + portraitHeight) + shadowSize, nameY + (portraitHeight + shadowSize * 2) / 2 - game.font.getData().lineHeight /4 , nameBackdropWidth - (nameBackdropWidth / 10), 1, false);	
			game.batcher.draw(game.sprites.HealthBackdrop, xOffset + (width - backdropWidth) / 2, nameY - backdropHeight, backdropWidth, backdropHeight);
			game.font.getData().setScale(.25f);
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
				game.batcher.draw(game.sprites.EvasionBackdrop, xOffset + (width - backdropWidth) / 2, nameY - backdropHeight * 6, backdropWidth, backdropHeight);

				if(state.selected.x >= 0 && state.selected.y >= 0){
					UnitRenderer.SetDefenseFont(state.selected, null, state.battlefield, game.font);
					game.font.draw(game.batcher, "" + (state.selected.defense + state.battlefield.get(state.selected.y).get(state.selected.x).defenseModifier), statTextXOffset, nameY - backdropHeight * 4 - statRelativeYOffset, statTextWidth, 1, false);
					UnitRenderer.SetEvasionFont(state.selected, null, state.battlefield, game.font);
					game.font.draw(game.batcher, "" + (state.selected.evasion + state.battlefield.get(state.selected.y).get(state.selected.x).evasionModifier), statTextXOffset, nameY - backdropHeight * 5 - statRelativeYOffset, statTextWidth, 1, false);
				}else{
					game.font.draw(game.batcher, "" + state.selected.defense, statTextXOffset, nameY - backdropHeight * 4 - statRelativeYOffset, statTextWidth, 1, false);
					game.font.draw(game.batcher, "" + state.selected.evasion, statTextXOffset, nameY - backdropHeight * 5 - statRelativeYOffset, statTextWidth, 1, false);

				}
			}
			
			game.font.getData().setScale(.33f);
		}
	}
	
	public void drawButtons(){
		if(state.isInTactics){
			if(state.isTacticsOpen){
				game.batcher.draw(game.sprites.EmptyEmphasis, abilityX, abilityY, buttonSize, buttonSize);
			}else{
				game.batcher.draw(game.sprites.EmptyEnabled, abilityX, abilityY, buttonSize, buttonSize);
			}
			int summaryX = abilityX + (buttonSize - summaryWidth) / 2;
			int summaryY = abilityY + (buttonSize - summaryHeight) / 2;
			
			game.batcher.draw(game.sprites.RosterSummary, summaryX, summaryY, summaryWidth, summaryHeight);
			for(int i = 0; i < state.unplacedUnits.size(); i++){
				int slotWidth = summaryWidth * 7 / 47;
				int slotHeight = slotWidth * 6 / 7;
				int slotXOffset = getSlotXOffset(i);
				int slotYOffset = getSlotYOffset(i);
				game.batcher.draw(game.sprites.RosterSummarySlot, summaryX + slotXOffset, summaryY + slotYOffset, slotWidth, slotHeight);
			}
			
			if(state.unplacedUnits.size() == 0){
				game.batcher.draw(game.sprites.CombatEmphasis, endTurnX, endTurnY, buttonSize, buttonSize);
			}else if(state.roster.size() > state.unplacedUnits.size()){
				game.batcher.draw(game.sprites.CombatEnabled, endTurnX, endTurnY, buttonSize, buttonSize);
			}else{
				game.batcher.draw(game.sprites.CombatDisabled, endTurnX, endTurnY, buttonSize, buttonSize);
			}

		}else{
			if(state.selected != null && state.selected.ability != null && state.CanUseAbility(state.selected)){
				if(state.isUsingAbility){
					game.batcher.draw(game.sprites.EmptyEmphasis, abilityX, abilityY, buttonSize, buttonSize);
				}else{
					game.batcher.draw(game.sprites.EmptyEnabled, abilityX, abilityY, buttonSize, buttonSize);
				}
			}else{
				game.batcher.draw(game.sprites.EmptyDisabled, abilityX, abilityY, buttonSize, buttonSize);
			}
			if(state.selected != null && state.selected.ability != null){
				game.font.getData().setScale(.20f);
				game.font.setColor(new Color(0f, 0f, 0f, 1f));
				game.font.draw(game.batcher, state.selected.ability.displayName, abilityX, abilityY + buttonSize / 2 + game.font.getLineHeight() / 2, buttonSize, 1, false);
			}
			
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
		}
		
		if(state.targeted != null){
			game.batcher.draw(game.sprites.ConfirmEnabled, confirmX, confirmY, buttonSize, buttonSize);	
		}else if(state.CanUndo()){
			game.batcher.draw(game.sprites.UndoEnabled, confirmX, confirmY, buttonSize, buttonSize);	
		}else{
			game.batcher.draw(game.sprites.ConfirmDisabled, confirmX, confirmY, buttonSize, buttonSize);	
		}
		
		if (state.selected == null){
			game.batcher.draw(game.sprites.InfoDisabled, infoX, infoY, buttonSize, buttonSize);
		}else{
			if(state.isUnitDetailsOpen){
				game.batcher.draw(game.sprites.InfoClose, infoX, infoY, buttonSize, buttonSize);
			}else{
				game.batcher.draw(game.sprites.InfoOpen, infoX, infoY, buttonSize, buttonSize);
			}
		}

		game.batcher.draw(game.sprites.ControlsDivider, chainSize + xOffset, endTurnY + buttonSize - dividerHeight / 2 + (abilityY - buttonSize - endTurnY) / 2, dividerWidth, dividerHeight);
		game.batcher.draw(game.sprites.SettingsIcon, xOffset + width - chainSize * 2, yOffset + height - chainSize * 2, chainSize + shadowSize, chainSize + shadowSize);
	}
	
	private int getSlotXOffset(int slot){
		if(slot == 0 || slot == 7){
			return summaryWidth * 8 / 47;
		}else if(slot == 1 || slot == 6){
			return summaryWidth * 16 / 47;
		}else if(slot == 2 || slot == 5){
			return summaryWidth * 25 / 47;
		}else{
			return summaryWidth * 33 / 47;
		}
	}
	
	private int getSlotYOffset(int slot){
		if(slot == 6 || slot == 5){
			return summaryHeight * 7 / 40;
		}else if(slot == 7 || slot == 4){
			return summaryHeight * 12 / 40;
		}else if(slot == 0 || slot == 3){
			return summaryHeight * 23 / 40;
		}else{
			return summaryHeight * 27 / 40;
		}
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
			if (!state.IsTapped(unit) && !state.unplacedUnits.contains(unit))
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
		}else if((clickedX > xOffset + width - chainSize * 3) && (clickedY > yOffset + height - chainSize * 3)){
			processSettingsTouch();	
		}else if((clickedX > infoX && clickedX < infoX + buttonSize) && (clickedY > infoY && clickedY < infoY + buttonSize)){
			processInfoTouch();	
		}else if(state.selected != null && x >= xOffset + (width - backdropWidth) / 2 && x < xOffset + (width - backdropWidth) / 2 + backdropWidth && y > nameY - backdropHeight * 6 && y < nameY){	
		}else{
			state.selected = null;
			state.targeted = null;
			state.isMoving = false;
			state.isUsingAbility = false;
			state.isTacticsOpen = false;
		}
	}
	
	private void processEndTouch()
	{
		if(state.isInTactics){
			if(state.roster.size() > state.unplacedUnits.size()){
				state.selected = null;
				state.isTacticsOpen = false;
				state.isInTactics = false;
			}
		}else{
			state.EndTurn();
		}
	}
	
	private void processAbilityTouch()
	{
		if(!state.isInTactics){
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
		}else{
			state.isTacticsOpen = !state.isTacticsOpen;
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
	
	public void processLongTouch(float x, float y, HelpPanel helpPanel){
		String title = "";
		String text = "";
		helpPanel.setHeight(buttonSize * 2);
		helpPanel.setWidth(buttonSize * 6);
		if(state.selected != null && x >= xOffset + (width - backdropWidth) / 2 && x < xOffset + (width - backdropWidth) / 2 + backdropWidth){
			if(y >= nameY - backdropHeight && y < nameY){
				title = "Health";
				text = state.selected.name + " has " + state.selected.currentHealth + " health remaining.";
			}else if(y >= nameY - backdropHeight * 2 && y < nameY - backdropHeight){
				title = "Experience";
				text = state.selected.name + " needs " + (state.selected.experienceNeeded - state.selected.experience) + " experience reach level " + (state.selected.level + 1) + ".";
			}else if(y >= nameY - backdropHeight * 3 && y < nameY - backdropHeight * 2 && (state.selected.team == 0 || state.perksPurchased >= 2)){
				title = "Attack";
				text = state.selected.name + " has an attack strength of " + state.selected.attack + ".";
			}else if(y >= nameY - backdropHeight * 4 && y < nameY - backdropHeight * 3 && (state.selected.team == 0 || state.perksPurchased >= 2)){
				title = "Accuracy";
				text = state.selected.name + " has an accuracy score of " + state.selected.accuracy + ".";
			}
			else if(y >= nameY - backdropHeight * 5 && y < nameY - backdropHeight * 4 && (state.selected.team == 0 || state.perksPurchased >= 2)){
				title = "Defense";
				int defenseBoost = state.battlefield.get(state.selected.y).get(state.selected.x).defenseModifier;
				text = state.selected.name + " has a defense strength of " + state.selected.defense;
				if(defenseBoost > 0){
					text += " that is being increased by terrain.";
				}else if(defenseBoost < 0){
					text += " that is being reduced by terrain.";
				}else{
					text += ".";
				}	
			}
			else if(y >= nameY - backdropHeight * 6 && y < nameY - backdropHeight * 5 && (state.selected.team == 0 || state.perksPurchased >= 2)){
				title = "Evasion";
				int evasionBoost = state.battlefield.get(state.selected.y).get(state.selected.x).evasionModifier;
				text = state.selected.name + " has an evasion score of " + state.selected.evasion;
				if(evasionBoost > 0){
					text += " that is being increased by terrain.";
				}else if(evasionBoost < 0){
					text += " that is being reduced by terrain.";
				}else{
					text += ".";
				}
			}else if(state.selected != null && ! state.isInTactics && (x > abilityX && x < abilityX + buttonSize) && (y > abilityY && y < abilityY + buttonSize)){
				title = state.selected.ability != null ? state.selected.ability.displayName : "Ability";
				text = state.selected.ability != null ? state.selected.ability.description : "This " + (state.selected.team == 0 ? "hero" : "unit") + " has no special ability.";
				helpPanel.setWidth(buttonSize * 9);		
			}else{
				state.isLongPressed = false;
				return;
			}
			helpPanel.title = title;
			helpPanel.text = text;
			helpPanel.isVisible = true;		
			state.isLongPressed = false;
		}
	}
	
	private void processSettingsTouch(){
		this.state.isSettingsOpen = !this.state.isSettingsOpen;
	}
	
	private void processInfoTouch(){
		if(state.selected != null){
			state.isUnitDetailsOpen = !state.isUnitDetailsOpen;
		}
	}
}
