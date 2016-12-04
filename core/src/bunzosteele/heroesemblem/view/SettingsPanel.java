package bunzosteele.heroesemblem.view;


import java.io.IOException;
import java.util.List;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.HighscoreManager;
import bunzosteele.heroesemblem.model.MusicManager;
import bunzosteele.heroesemblem.model.ShopState;
import bunzosteele.heroesemblem.model.Units.LocationDto;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitDto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;

public class SettingsPanel extends PopupPanel
{
	int buttonSize;
	public static Sound sfxDemo = Gdx.audio.newSound(Gdx.files.internal("buy.wav"));
	static boolean backEnabled = true;
	static boolean isDisplayingConfirmation = false;
	boolean isInGame;
	ShopState shopState = null;
	BattleState battleState = null;
		
	public SettingsPanel(final HeroesEmblem game, ShopState state, int width, int height, int xOffset, int yOffset)
	{
		super(game, width, height, xOffset, yOffset);
		this.buttonSize = width / 8;
		this.isInGame = true;
		this.shopState = state;
	}
	
	public SettingsPanel(final HeroesEmblem game, BattleState state, int width, int height, int xOffset, int yOffset)
	{
		super(game, width, height, xOffset, yOffset);
		this.buttonSize = width / 8;
		this.isInGame = true;
		this.battleState = state;
	}
	
	public void drawBackground(){
		super.drawBackground();
	}
	
	public void draw(){
		super.drawBorder();
		game.font.setColor(Color.BLACK);
		game.font.getData().setScale(.4f);
		game.font.draw(game.batcher, "Settings", xOffset, yOffset + height - game.font.getData().lineHeight, width, 1, false);
		game.font.getData().setScale(.33f);
		if(!SettingsPanel.isDisplayingConfirmation){
			float sfxVolume = game.settings.getFloat("sfxVolume", .5f);
			game.font.draw(game.batcher, "SFX Volume:", xOffset, yOffset + height - game.font.getData().lineHeight * 7 / 2, width, 1, false);
			for(int i = 0; i < 5; i++){
				if(sfxVolume == .25f * i){
					game.batcher.draw(game.sprites.EmptyEnabled, xOffset + buttonSize / 2 + (i * (buttonSize * 3 / 2)), yOffset + height - buttonSize * 5 / 2, buttonSize, buttonSize);
				}else{
					game.batcher.draw(game.sprites.EmptyDisabled, xOffset + buttonSize / 2 + (i * (buttonSize * 3 / 2)), yOffset + height - buttonSize * 5 / 2, buttonSize, buttonSize);
				}
				game.font.draw(game.batcher, "" + i, xOffset + buttonSize / 2 + (i * (buttonSize * 3 / 2)), yOffset + height - buttonSize * 2 + game.font.getData().lineHeight / 2, (float) buttonSize, 1, false);
			}
			
			float musicVolume = game.settings.getFloat("musicVolume", .5f);
			game.font.draw(game.batcher, "Music Volume:", xOffset, yOffset + height - game.font.getData().lineHeight *  7 / 2 - buttonSize * 3 /2, width, 1, false);
			for(int i = 0; i < 5; i++){
				if(musicVolume == .25f * i){
					game.batcher.draw(game.sprites.EmptyEnabled, xOffset + buttonSize / 2 + (i * (buttonSize * 3 / 2)), yOffset + height - buttonSize * 4, buttonSize, buttonSize);
				}else{
					game.batcher.draw(game.sprites.EmptyDisabled, xOffset + buttonSize / 2 + (i * (buttonSize * 3 / 2)), yOffset + height - buttonSize * 4, buttonSize, buttonSize);
				}
				game.font.draw(game.batcher, "" + i, xOffset + buttonSize / 2 + (i * (buttonSize * 3 / 2)), yOffset + height - buttonSize * 7 / 2 + game.font.getData().lineHeight / 2, (float) buttonSize, 1, false);
			}
			
			float cpuSpeed = game.settings.getFloat("cpuSpeed", .5f);
			game.font.draw(game.batcher, "CPU Speed:", xOffset, yOffset + height - game.font.getData().lineHeight *  7 / 2 - buttonSize * 3, width, 1, false);
			for(int i = 0; i < 5; i++){
				if(cpuSpeed == 1.1f - (i * .25f)){
					game.batcher.draw(game.sprites.EmptyEnabled, xOffset + buttonSize / 2 + (i * (buttonSize * 3 / 2)), yOffset + height - buttonSize * 11 / 2, buttonSize, buttonSize);
				}else{
					game.batcher.draw(game.sprites.EmptyDisabled, xOffset + buttonSize / 2 + (i * (buttonSize * 3 / 2)), yOffset + height - buttonSize * 11 / 2, buttonSize, buttonSize);
				}
				game.font.draw(game.batcher, "" + (i + 1), xOffset + buttonSize / 2 + (i * (buttonSize * 3 / 2)), yOffset + height - buttonSize * 5 + game.font.getData().lineHeight / 2, (float) buttonSize, 1, false);
			}
	
			game.batcher.draw(game.sprites.DeleteButton, xOffset + (width - buttonSize * 3) / 2, yOffset + buttonSize / 2, buttonSize * 3, (buttonSize * 75) / 122);
			
			if(!isInGame){
				game.font.draw(game.batcher, "Erase Highscores", xOffset, yOffset + game.font.getData().lineHeight * 3, width, 1, false);
			}else{
				game.font.draw(game.batcher, "Quit Game", xOffset, yOffset + game.font.getData().lineHeight * 3, width, 1, false);
			}
		}else{
			game.font.getData().setScale(.66f);
			if(!isInGame){
				game.font.draw(game.batcher, "Erase Highscores?", xOffset, yOffset + height - game.font.getData().lineHeight * 7 / 2, width, 1, false);
			}else{
				game.font.draw(game.batcher, "Resign?", xOffset, yOffset + height - game.font.getData().lineHeight * 7 / 2, width, 1, false);
			}
			game.batcher.draw(game.sprites.EmptyEnabled, xOffset + width / 3 - buttonSize / 2, yOffset + height - buttonSize * 8 / 2, buttonSize, buttonSize);
			game.batcher.draw(game.sprites.EmptyEnabled, xOffset + width * 2 / 3 - buttonSize / 2, yOffset + height - buttonSize * 8 / 2, buttonSize, buttonSize);
			game.font.getData().setScale(.33f);
			game.font.draw(game.batcher, "Yes", xOffset + width / 3 - buttonSize / 2, yOffset + height - buttonSize * 7 / 2 + game.font.getData().lineHeight / 2, buttonSize, 1, false);
			game.font.draw(game.batcher, "No", xOffset + width * 2 / 3 - buttonSize / 2, yOffset + height - buttonSize * 7 / 2 + game.font.getData().lineHeight / 2, buttonSize, 1, false);
		}
	}
	
	public void processTouch(final float x, final float y) throws IOException{
		int clickedX = Gdx.input.getX();
		int clickedY = Gdx.graphics.getHeight() - Gdx.input.getY();
		if(!SettingsPanel.isDisplayingConfirmation){
			if(clickedY > yOffset + buttonSize / 2 && clickedY < yOffset + buttonSize){
				checkDeleteTouch(clickedX);
			}else if(clickedY > yOffset + height - buttonSize * 11 / 2 && clickedY < yOffset + height - buttonSize * 9 / 2){
				checkCpuTouch(clickedX);
			}else if(clickedY > yOffset + height - buttonSize * 4 && clickedY < yOffset + height - buttonSize * 3){
				checkMusicTouch(clickedX);
			}else if(clickedY > yOffset + height - buttonSize * 5 / 2 && clickedY < yOffset + height - buttonSize * 3 / 2){
				checkSfxTouch(clickedX);
			}
		}else{
			checkDeleteTouch(clickedX, clickedY);
		}
	}
	
	private void checkDeleteTouch(int x){
		if(x > xOffset + (width - buttonSize * 3) / 2 && x < (xOffset + (width - buttonSize * 3) / 2) + buttonSize * 3){
			SettingsPanel.isDisplayingConfirmation = true;
		}
	}
	
	private void checkCpuTouch(int x){
		for(int i = 0; i < 5; i++){
			if(x >= xOffset + buttonSize / 2 + (i * (buttonSize * 3 / 2)) && x <= xOffset + buttonSize * 3 / 2 + (i * (buttonSize * 3 / 2))){
				game.settings.putFloat("cpuSpeed", 1.1f - (i * .25f));
				game.settings.flush();
			}
		}
	}
	
	private void checkMusicTouch(int x){
		for(int i = 0; i < 5; i++){
			if(x >= xOffset + buttonSize / 2 + (i * (buttonSize * 3 / 2)) && x <= xOffset + buttonSize * 3 / 2 + (i * (buttonSize * 3 / 2))){
				game.settings.putFloat("musicVolume", i * .25f);
				game.settings.flush();
				MusicManager.nowPlaying.setVolume(i * .25f);
			}
		}
	}
	
	private void checkSfxTouch(int x){
		for(int i = 0; i < 5; i++){
			if(x >= xOffset + buttonSize / 2 + (i * (buttonSize * 3 / 2)) && x <= xOffset + buttonSize * 3 / 2 + (i * (buttonSize * 3 / 2))){
				game.settings.putFloat("sfxVolume", i * .25f);
				game.settings.flush();
				SettingsPanel.sfxDemo.play(i * .25f);
			}
		}
	}
	
	private void checkDeleteTouch(int x, int y){
		if(x > xOffset + width / 3 - buttonSize / 2 && x < xOffset + width / 3 + buttonSize / 2 && y > yOffset + height - buttonSize * 8 / 2 && y < yOffset + height - buttonSize * 6 / 2){
			if(isInGame){
				if(shopState != null){
					endGame(shopState.roster, shopState.graveyard, shopState.roundsSurvived);
				}else{
					endGame(battleState.roster, battleState.graveyard, battleState.roundsSurvived);
				}		
			}else{
				HighscoreManager.EraseHighscores();
			}
		}else if(x > xOffset + width * 2 / 3 - buttonSize / 2 && x < xOffset + width * 2 / 3 + buttonSize / 2 && y > yOffset + height - buttonSize * 8 / 2 && y < yOffset + height - buttonSize * 6 / 2){
			SettingsPanel.isDisplayingConfirmation = false;
		}
	}
	
	private void endGame(List<Unit> roster, List<UnitDto> graveyard, int roundsSurvived){
		for(Unit unit : roster){
			graveyard.add(generateUnitDto(unit, roundsSurvived));
		}
		game.setScreen(new GameOverScreen(game, roundsSurvived, graveyard));
	}
	
	private UnitDto generateUnitDto(Unit deceased, int roundsSurvived){
		UnitDto unitDto = new UnitDto();
		unitDto.type = deceased.type.toString();
		unitDto.name = deceased.name;
		unitDto.attack = deceased.attack;
		unitDto.defense = deceased.defense;
		unitDto.evasion = deceased.evasion;
		unitDto.accuracy = deceased.accuracy;
		unitDto.movement = deceased.movement;
		unitDto.maximumHealth = deceased.maximumHealth;
		unitDto.level = deceased.level;
		if(deceased.ability == null){
			unitDto.ability = "None";
		}else{
			unitDto.ability = deceased.ability.displayName;
		}
		unitDto.unitsKilled = deceased.unitsKilled;
		unitDto.damageDealt = deceased.damageDealt;
		LocationDto location = new LocationDto();
		location.battlefieldId = Integer.MIN_VALUE;
		location.x = -1;
		location.y = -1;
		unitDto.locationKilled = location;
		unitDto.roundKilled = roundsSurvived;
		unitDto.isMale = deceased.isMale;
		unitDto.backStory = deceased.backStory;
		return unitDto;
	}
}
