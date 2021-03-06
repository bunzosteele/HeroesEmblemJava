package bunzosteele.heroesemblem.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.Move;
import bunzosteele.heroesemblem.model.ShopState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.XmlReader.Element;

public class ShopPanel
{
	HeroesEmblem game;
	ShopState state;
	int xOffset;
	int yOffset;
	int width;
	int height;
	int buttonSize;
	int combatX;
	int combatY;
	int purchaseX;
	int purchaseY;
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
	int nameBackdropWidth;
	int nameBackdropHeight;
	int nameY;

	public ShopPanel(final HeroesEmblem game, final ShopState state, final int width, final int height, final int xOffset, final int yOffset)
	{
		this.game = game;
		this.state = state;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		buttonSize = width / 3;
		this.chainSize = width / 15;
		this.shadowSize = chainSize / 3; 
		backdropWidth = width * 8 / 10;
		backdropHeight = (int) Math.floor(backdropWidth * .353);
		dividerWidth = width - buttonSize;
		dividerHeight = (int) (dividerWidth * .0547);
		int buttonRegionHeight =  height - chainSize - backdropHeight * 7 - shadowSize * 2 - chainSize;
		int buttonVerticalSpacing = (buttonRegionHeight - 2 * buttonSize - dividerHeight) / 4;
		combatX = xOffset + width - buttonSize - buttonSize / 3;
		combatY = buttonVerticalSpacing + chainSize;
		purchaseX = xOffset + buttonSize / 3;
		purchaseY = buttonVerticalSpacing + chainSize;
		abilityX = xOffset + buttonSize / 3;
		abilityY = buttonVerticalSpacing  * 3 + dividerHeight + buttonSize + chainSize;
		infoX = xOffset + width - buttonSize - buttonSize / 3;
		infoY = buttonVerticalSpacing  * 3 + dividerHeight + buttonSize + chainSize;
		nameBackdropWidth = buttonSize * 2 - chainSize - shadowSize;
		nameBackdropHeight = nameBackdropWidth * 44 / 92;
		nameY =  height + yOffset - chainSize - shadowSize - nameBackdropHeight;
	}

	public void draw() throws IOException
	{
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
			game.font.setColor(Color.WHITE);
			game.font.draw(game.batcher, state.selected.name, xOffset + (chainSize + portraitHeight) + shadowSize, nameY + (portraitHeight + shadowSize * 2) / 2 + game.font.getData().lineHeight, nameBackdropWidth - (nameBackdropWidth / 10), 1, false);
			game.font.draw(game.batcher, state.selected.type.toString(), xOffset + (chainSize + portraitHeight) + shadowSize, nameY + (portraitHeight + shadowSize * 2) / 2 - game.font.getData().lineHeight /4 , nameBackdropWidth - (nameBackdropWidth / 10), 1, false);	
			game.font.setColor(Color.BLACK);
			game.batcher.draw(game.sprites.HealthBackdrop, xOffset + (width - backdropWidth) / 2, nameY - backdropHeight, backdropWidth, backdropHeight);
			DrawHealthBar(nameY);
			game.batcher.draw(game.sprites.ExperienceBackdrop, xOffset + (width - backdropWidth) / 2, nameY - backdropHeight * 2, backdropWidth, backdropHeight);
			DrawExperienceBar(nameY);
			game.font.getData().setScale(.25f);
			final XmlReader reader = new XmlReader();
			final Element xml = reader.parse(Gdx.files.internal("UnitStats.xml"));
			final Element unitStats = xml.getChildByName(state.selected.type.toString());		
			game.batcher.draw(game.sprites.AttackBackdrop, xOffset + (width - backdropWidth) / 2, nameY - backdropHeight * 3, backdropWidth, backdropHeight);
			game.batcher.draw(game.sprites.AccuracyBackdrop, xOffset + (width - backdropWidth) / 2, nameY - backdropHeight * 4, backdropWidth, backdropHeight);
			game.batcher.draw(game.sprites.DefenseBackdrop, xOffset + (width - backdropWidth) / 2, nameY - backdropHeight * 5, backdropWidth, backdropHeight);
			game.batcher.draw(game.sprites.EvasionBackdrop, xOffset + (width - backdropWidth) / 2, nameY - backdropHeight * 6, backdropWidth, backdropHeight);
			int statTextXOffset = xOffset + (width - backdropWidth) / 2 + backdropWidth * 57 / 116;
			int statTextWidth = backdropWidth * 32 / 116;
			int statRelativeYOffset = backdropHeight * 2 / 41 + (backdropHeight * 25 / 41) / 2;
			
			int footprintSize = buttonSize / 5;
			int totalMovementWidth = footprintSize * state.selected.movement;
			for(int i = 0; i < state.selected.movement; i++){
				if(i >= unitStats.getInt("Movement") && !state.roster.contains(state.selected)){
					game.batcher.draw(game.sprites.FootprintsWhite, xOffset + ((width - totalMovementWidth) / 2) + footprintSize * i, nameY - backdropHeight * 6 - footprintSize, footprintSize, footprintSize * 23 / 20);
				}
				else{
					game.batcher.draw(game.sprites.FootprintsBlack, xOffset + ((width - totalMovementWidth) / 2) + footprintSize * i, nameY - backdropHeight * 6 - footprintSize, footprintSize, footprintSize * 23 / 20);
				}
			}
			
			if (!this.state.roster.contains(state.selected)) {
				UnitRenderer.SetHealthFont(state.selected, unitStats, game.font);
				game.font.draw(game.batcher, state.selected.currentHealth + "/" + state.selected.maximumHealth, xOffset + (width - backdropWidth) / 2 + backdropHeight, nameY - game.font.getData().lineHeight / 2, backdropWidth - (backdropWidth * 2 / 29) - backdropHeight - (nameBackdropWidth / 10), 1, false);
				game.font.setColor(Color.BLACK);
				game.font.draw(game.batcher, (state.selected.level < 10 ? "Lvl. 0" : "Lvl. ") + state.selected.level, xOffset + (width - backdropWidth) / 2 + backdropHeight, nameY - backdropHeight - game.font.getData().lineHeight / 2, backdropWidth - (backdropWidth * 2 / 29) - backdropHeight - (nameBackdropWidth / 10), 1, false);
				game.font.getData().setScale(.35f);
				UnitRenderer.SetAttackFont(state.selected, unitStats, game.font);
				game.font.draw(game.batcher, "" + state.selected.attack, statTextXOffset, nameY - backdropHeight * 2 - statRelativeYOffset, statTextWidth, 1, false);
				UnitRenderer.SetAccuracyFont(state.selected, unitStats, game.font);
				game.font.draw(game.batcher, "" + state.selected.accuracy, statTextXOffset, nameY - backdropHeight * 3 - statRelativeYOffset, statTextWidth, 1, false);
				UnitRenderer.SetDefenseFont(state.selected, unitStats, null, game.font);
				game.font.draw(game.batcher, "" + state.selected.defense, statTextXOffset, nameY - backdropHeight * 4 - statRelativeYOffset, statTextWidth, 1, false);
				UnitRenderer.SetEvasionFont(state.selected, unitStats, null, game.font);
				game.font.draw(game.batcher, "" + state.selected.evasion, statTextXOffset, nameY - backdropHeight * 5 - statRelativeYOffset, statTextWidth, 1, false);
			}else{
				game.font.setColor(Color.BLACK);
				game.font.draw(game.batcher, state.selected.currentHealth + "/" + state.selected.maximumHealth, xOffset + (width - backdropWidth) / 2 + backdropHeight, nameY - game.font.getData().lineHeight / 2, backdropWidth - (backdropWidth * 2 / 29) - backdropHeight - (nameBackdropWidth / 10), 1, false);
				game.font.draw(game.batcher, "LVL." + state.selected.level, xOffset + (width - backdropWidth) / 2 + backdropHeight, nameY - backdropHeight - game.font.getData().lineHeight / 2, backdropWidth - (backdropWidth * 2 / 29) - backdropHeight - (nameBackdropWidth / 10), 1, false);
				game.font.getData().setScale(.35f);
				game.font.draw(game.batcher, "" + state.selected.attack, statTextXOffset, nameY - backdropHeight * 2 - statRelativeYOffset, statTextWidth, 1, false);
				game.font.draw(game.batcher, "" + state.selected.accuracy, statTextXOffset, nameY - backdropHeight * 3 - statRelativeYOffset, statTextWidth, 1, false);
				game.font.draw(game.batcher, "" + state.selected.defense, statTextXOffset, nameY - backdropHeight * 4 - statRelativeYOffset, statTextWidth, 1, false);
				game.font.draw(game.batcher, "" + state.selected.evasion, statTextXOffset, nameY - backdropHeight * 5 - statRelativeYOffset, statTextWidth, 1, false);
			}	
		}
	}
	
	public void drawButtons(){		
		if(canStartGame()){
			if (!state.CanBuy())
			{
				game.batcher.draw(game.sprites.CombatEmphasis, combatX, combatY, buttonSize, buttonSize);
			}else{
				game.batcher.draw(game.sprites.CombatEnabled, combatX, combatY, buttonSize, buttonSize);
			}
		}else{
			game.batcher.draw(game.sprites.CombatDisabled, combatX, combatY, buttonSize, buttonSize);
		}	
		
		if(state.selected != null && state.selected.ability != null){
			game.batcher.draw(game.sprites.AbilityDisabled, abilityX, abilityY, buttonSize, buttonSize);
			int iconXOffset = (buttonSize * 10) / 51;
			int iconYOffset = (buttonSize * 11) / 51;
			int iconSize = (buttonSize * 30) / 51;
			game.batcher.draw(state.selected.ability.GetAbilityIcon(game), abilityX + iconXOffset, abilityY + iconYOffset + 1, iconSize, iconSize);
		}else{
			game.batcher.draw(game.sprites.EmptyDisabled, abilityX, abilityY, buttonSize, buttonSize);
		}
		
		if (canPurchaseSelected())
		{
			game.batcher.draw(game.sprites.HireEnabled, purchaseX, purchaseY, buttonSize, buttonSize);	
		}else{
			game.batcher.draw(game.sprites.HireDisabled, purchaseX, purchaseY, buttonSize, buttonSize);
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
		
		game.batcher.draw(game.sprites.ControlsDivider, xOffset + (width - dividerWidth) / 2, combatY + buttonSize - dividerHeight / 2 + (abilityY - buttonSize - combatY) / 2, dividerWidth, dividerHeight);
		game.batcher.draw(game.sprites.SettingsIcon, xOffset + width - chainSize * 2, yOffset + height - chainSize * 2, chainSize + shadowSize, chainSize + shadowSize);
	}
	
	private boolean canStartGame()
	{
		return state.roster.size() > 0;
	}
	
	private boolean canPurchaseSelected()
	{
		return (state.selected != null) && (state.gold >= state.selected.cost) && (state.roster.size() < 8) && !state.roster.contains(state.selected);
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

	public void processTouch(final float x, final float y) throws IOException
	{
		if((x > combatX && x < combatX + buttonSize) && (y > combatY && y < combatY + buttonSize)){
			processCombatTouch();
		}else if((x > abilityX && x < abilityX + buttonSize) && (y > abilityY && y < abilityY + buttonSize)){
			processAbilityTouch();
		}else if((x > purchaseX && x < purchaseX + buttonSize) && (y > purchaseY && y < purchaseY + buttonSize)){
			processPurchaseTouch();
		}else if((x > infoX && x < infoX + buttonSize) && (y > infoY && y < infoY + buttonSize)){
			processInfoTouch();
		}else if((x > xOffset + width - chainSize * 3) && (y > yOffset + height - chainSize * 3)){
			processSettingsTouch();	
		}else if(state.selected != null && x >= xOffset + (width - backdropWidth) / 2 && x < xOffset + (width - backdropWidth) / 2 + backdropWidth && y > nameY - backdropHeight * 6 && y < nameY){	
		}else{
			state.selected = null;
		}
	}
	
	private void processCombatTouch() throws IOException{
		if (canStartGame())
		{
			game.shopState = null;
			game.setScreen(new BattleScreen(game, state));
			return;
		}		
	}
	
	private void processAbilityTouch(){	
	}
	
	private void processPurchaseTouch() throws IOException{
		if (canPurchaseSelected())
		{
			state.BuyUnit();		
			if(state.CanBuy()){
				ShopScreen.buySound.play(game.settings.getFloat("sfxVolume", .5f));
			}else{
				ShopScreen.finalBuySound.play(game.settings.getFloat("sfxVolume", .5f));
			}
		}
	}
	
	private void processInfoTouch(){
		if(state.selected != null){
			state.isUnitDetailsOpen = !state.isUnitDetailsOpen;
		}
	}
	
	private void processSettingsTouch(){
		this.state.isSettingsOpen = !this.state.isSettingsOpen;
	}
	
	public void processLongTouch(float x, float y, HelpPanel helpPanel){
		String title = "";
		String text = "";
		String subtext = "";
		helpPanel.setHeight(buttonSize * 2);
		helpPanel.setWidth(buttonSize * 6);
		if(state.selected != null && x >= xOffset + (width - backdropWidth) / 2 && x < xOffset + (width - backdropWidth) / 2 + backdropWidth){
			if(y >= nameY - backdropHeight && y < nameY){
				title = "Health";
				text = state.selected.name + " has " + state.selected.currentHealth + " health remaining.";
			}else if(y >= nameY - backdropHeight * 2 && y < nameY - backdropHeight){
				title = "Experience";
				text = state.selected.name + " needs " + (state.selected.experienceNeeded - state.selected.experience) + " experience reach level " + (state.selected.level + 1) + ".";
			}else if(y >= nameY - backdropHeight * 3 && y < nameY - backdropHeight * 2){
				title = "Attack";
				text = state.selected.name + " has an attack strength of " + state.selected.attack + ".";
			}else if(y >= nameY - backdropHeight * 4 && y < nameY - backdropHeight * 3){
				title = "Accuracy";
				text = state.selected.name + " has an accuracy score of " + state.selected.accuracy + ".";
			}
			else if(y >= nameY - backdropHeight * 5 && y < nameY - backdropHeight * 4){
				title = "Defense";
				text = state.selected.name + " has a defense strength of " + state.selected.defense + ".";
			}
			else if(y >= nameY - backdropHeight * 6 && y < nameY - backdropHeight * 5){
				title = "Evasion";
				text = state.selected.name + " has an evasion score of " + state.selected.evasion + ".";
			}else if(state.selected != null && (x > abilityX && x < abilityX + buttonSize) && (y > abilityY && y < abilityY + buttonSize)){
				title = state.selected.ability != null ? state.selected.ability.displayName : "Ability";
				text = state.selected.ability != null ? state.selected.ability.description : "This hero has no special ability.";
				helpPanel.setWidth(buttonSize * 9);		
			}else{
				state.isLongPressed = false;
				return;
			}
			helpPanel.title = title;
			helpPanel.text = text;
			helpPanel.subtext = subtext;
			helpPanel.isVisible = true;		
			state.isLongPressed = false;
		}
	}
}
