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
		backdropWidth = (int) (width * .8);
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
			DrawHealthBar(nameY);
			game.batcher.draw(game.sprites.ExperienceBackdrop, xOffset + (width - backdropWidth) / 2, nameY - backdropHeight * 2, backdropWidth, backdropHeight);
			DrawExperienceBar(nameY);
			final XmlReader reader = new XmlReader();
			final Element xml = reader.parse(Gdx.files.internal("UnitStats.xml"));
			final Element unitStats = xml.getChildByName(state.selected.type.toString());		
			game.batcher.draw(game.sprites.AttackBackdrop, xOffset + (width - backdropWidth) / 2, nameY - backdropHeight * 3, backdropWidth, backdropHeight);
			game.batcher.draw(game.sprites.AccuracyBackdrop, xOffset + (width - backdropWidth) / 2, nameY - backdropHeight * 4, backdropWidth, backdropHeight);
			game.batcher.draw(game.sprites.DefenseBackdrop, xOffset + (width - backdropWidth) / 2, nameY - backdropHeight * 5, backdropWidth, backdropHeight);
			game.batcher.draw(game.sprites.EvasionBackdrop, xOffset + (width - backdropWidth) / 2, nameY - backdropHeight * 6, backdropWidth, backdropHeight);
			int statTextXOffset = xOffset + (width - backdropWidth) / 2 + backdropWidth * 57 / 116;
			int statTextWidth = backdropWidth * 32 / 116;
			int statRelativeYOffset = backdropHeight * 2 / 41 + (backdropHeight * 25 / 41) / 2 ;
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
	
		game.batcher.draw(game.sprites.EmptyDisabled, abilityX, abilityY, buttonSize, buttonSize);
		
		
		if(state.selected != null && state.selected.ability != null){
			game.font.getData().setScale(.2f);
			game.font.setColor(new Color(0f, 0f, 0f, 1f));
			game.font.draw(game.batcher, state.selected.ability.displayName, abilityX, abilityY + buttonSize / 2 + game.font.getLineHeight() / 2, buttonSize, 1, false);
		}
		
		if (canPurchaseSelected())
		{
			game.batcher.draw(game.sprites.HireEnabled, purchaseX, purchaseY, buttonSize, buttonSize);	
		}else{
			game.batcher.draw(game.sprites.HireDisabled, purchaseX, purchaseY, buttonSize, buttonSize);
		}
		
		game.batcher.draw(game.sprites.InfoDisabled, infoX, infoY, buttonSize, buttonSize);
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
		int clickedX = Gdx.input.getX();
		int clickedY = Gdx.graphics.getHeight() - Gdx.input.getY();
		if((clickedX > combatX && clickedX < combatX + buttonSize) && (clickedY > combatY && clickedY < combatY + buttonSize)){
			processCombatTouch();
		}else if((clickedX > abilityX && clickedX < abilityX + buttonSize) && (clickedY > abilityY && clickedY < abilityY + buttonSize)){
			processAbilityTouch();
		}else if((clickedX > purchaseX && clickedX < purchaseX + buttonSize) && (clickedY > purchaseY && clickedY < purchaseY + buttonSize)){
			processPurchaseTouch();
		}else if((clickedX > infoX && clickedX < infoX + buttonSize) && (clickedY > infoY && clickedY < infoY + buttonSize)){
			processInfoTouch();
		}else if((clickedX > xOffset + width - chainSize * 3) && (clickedY > yOffset + height - chainSize * 3)){
			processSettingsTouch();	
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
		
	}
	
	private void processSettingsTouch(){
		this.state.isSettingsOpen = !this.state.isSettingsOpen;
	}
}
