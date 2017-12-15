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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class UnitDetailsPanel extends PopupPanel
{	
	ShopState shopState = null;
	BattleState battleState = null;
	int tileSize;
	int backdropWidth;
	int backdropHeight;
		
	public UnitDetailsPanel(final HeroesEmblem game, ShopState state, int width, int height, int xOffset, int yOffset)
	{
		super(game, width, height, xOffset, yOffset);
		this.shopState = state;
		tileSize = width / 8;
		backdropWidth = tileSize * 12 / 5;
		backdropHeight = (int) Math.floor(backdropWidth * .353);
	}
	
	public UnitDetailsPanel(final HeroesEmblem game, BattleState state, int width, int height, int xOffset, int yOffset)
	{
		super(game, width, height, xOffset, yOffset);
		this.battleState = state;
		tileSize = width / 8;
		backdropWidth = tileSize * 12 / 5;
		backdropHeight = (int) Math.floor(backdropWidth * .353);
	}
	
	public UnitDetailsPanel(final HeroesEmblem game, int width, int height, int xOffset, int yOffset)
	{
		super(game, width, height, xOffset, yOffset);
		tileSize = width / 8;
		backdropWidth = tileSize * 12 / 5;
		backdropHeight = (int) Math.floor(backdropWidth * .353);
	}
	
	public void drawBackground(){
		super.drawBackground(true);
	}
	
	public void draw() throws IOException{
		super.drawBorder();
		if(shopState != null && shopState.selected != null){
			drawUnitDetails(shopState.selected);
		}else if (battleState != null && battleState.selected != null){
			drawUnitDetails(battleState.selected);
		}
	}
	
	public void draw(UnitDto unit) throws IOException{
		super.drawBorder();
		drawUnitDetails(unit);
	}
	
	public void drawUnitDetails(UnitDto unit) throws IOException{
		if(unit != null){
			game.font.setColor(Color.BLACK);
			game.font.getData().setScale(.20f);	
			int portraitHeight = tileSize - chainSize - shadowSize;
			int nameBackdropWidth = tileSize * 2 - chainSize - shadowSize;
			int nameBackdropHeight = nameBackdropWidth * 44 / 92;
			int nameY =  height + yOffset - chainSize - shadowSize - nameBackdropHeight;
			int contentXOffset = xOffset + chainSize + shadowSize + (width - (nameBackdropWidth + chainSize * 2 + shadowSize * 2 + portraitHeight + backdropWidth * 2)) / 2;
			final AtlasRegion portraitRegion = game.textureAtlas.findRegion("Portrait" + unit.type + unit.team);
			game.batcher.draw(new Sprite(portraitRegion), contentXOffset, nameY + shadowSize, portraitHeight, portraitHeight);		
			final AtlasRegion nameBackdropRegion = game.textureAtlas.findRegion("NameBackdrop" + unit.team);
			game.font.setColor(Color.WHITE);
			game.batcher.draw(new Sprite(nameBackdropRegion), contentXOffset + portraitHeight, yOffset + height - chainSize - shadowSize - nameBackdropHeight, nameBackdropWidth, nameBackdropHeight);
			game.font.draw(game.batcher, unit.name, contentXOffset + portraitHeight, nameY + (portraitHeight + shadowSize * 2) / 2 + game.font.getData().lineHeight, nameBackdropWidth - (nameBackdropWidth / 10), 1, false);
			game.font.draw(game.batcher, unit.type.toString(), contentXOffset + portraitHeight, nameY + (portraitHeight + shadowSize * 2) / 2 - game.font.getData().lineHeight /4 , nameBackdropWidth - (nameBackdropWidth / 10), 1, false);	
			game.font.getData().setScale(.25f);
			game.font.setColor(Color.BLACK);
			game.batcher.draw(game.sprites.HealthBackdrop, contentXOffset, nameY - backdropHeight, backdropWidth, backdropHeight);
			DrawHealthBar(0, unit.maximumHealth, true, nameY, contentXOffset);
			game.batcher.draw(game.sprites.ExperienceBackdrop, contentXOffset, nameY - backdropHeight * 2, backdropWidth, backdropHeight);
			DrawExperienceBar(unit.experience, unit.experienceNeeded, nameY, contentXOffset);
			final XmlReader reader = new XmlReader();
			final Element xml = reader.parse(Gdx.files.internal("UnitStats.xml"));
			final Element unitStats = xml.getChildByName(unit.type.toString());		
			game.batcher.draw(game.sprites.AttackBackdrop, contentXOffset + nameBackdropWidth + portraitHeight, yOffset + height - chainSize - shadowSize - backdropHeight, backdropWidth, backdropHeight);
			game.batcher.draw(game.sprites.AccuracyBackdrop, contentXOffset + nameBackdropWidth + portraitHeight, yOffset + height - chainSize - shadowSize - backdropHeight * 2, backdropWidth, backdropHeight);
			game.batcher.draw(game.sprites.DefenseBackdrop, contentXOffset + nameBackdropWidth + portraitHeight + backdropWidth, yOffset + height - chainSize - shadowSize - backdropHeight, backdropWidth, backdropHeight);
			game.batcher.draw(game.sprites.EvasionBackdrop, contentXOffset + nameBackdropWidth + portraitHeight + backdropWidth, yOffset + height - chainSize - shadowSize - backdropHeight * 2, backdropWidth, backdropHeight);
			int statTextXOffset = contentXOffset + backdropWidth * 57 / 116 + portraitHeight + nameBackdropWidth;
			int statTextWidth = backdropWidth * 32 / 116;
			int statRelativeYOffset = backdropHeight * 2 / 41 + (backdropHeight * 25 / 41) / 2 - backdropHeight;	
			game.font.setColor(Color.BLACK);
			game.font.draw(game.batcher, "0/" + unit.maximumHealth, contentXOffset + backdropHeight, nameY - game.font.getData().lineHeight / 2, backdropWidth - (backdropWidth * 2 / 29) - backdropHeight - (nameBackdropWidth / 10), 1, false);
			game.font.draw(game.batcher, (unit.level < 10 ? "Lvl. 0" : "Lvl. ") + unit.level, contentXOffset + backdropHeight, nameY - backdropHeight - game.font.getData().lineHeight / 2, backdropWidth - (backdropWidth * 2 / 29) - backdropHeight - (nameBackdropWidth / 10), 1, false);
			game.font.getData().setScale(.35f);
			game.font.draw(game.batcher, "" + unit.attack, statTextXOffset, nameY - statRelativeYOffset, statTextWidth, 1, false);
			game.font.draw(game.batcher, "" + unit.accuracy, statTextXOffset, nameY - backdropHeight - statRelativeYOffset, statTextWidth, 1, false);
			game.font.draw(game.batcher, "" + unit.defense, statTextXOffset + backdropWidth, nameY  - statRelativeYOffset, statTextWidth, 1, false);
			game.font.draw(game.batcher, "" + unit.evasion, statTextXOffset + backdropWidth, nameY - backdropHeight - statRelativeYOffset, statTextWidth, 1, false);
			game.font.setColor(Color.WHITE);
			game.font.draw(game.batcher, "Movement Speed: " + unit.movement, contentXOffset + backdropWidth, nameY - backdropHeight * 2 - statRelativeYOffset + game.font.getData().lineHeight / 2, backdropWidth * 2, 1, false);
			game.font.draw(game.batcher, "Ability: " + (unit.ability == null ? "None" : unit.ability), contentXOffset + backdropWidth, nameY - backdropHeight * 2 - statRelativeYOffset - game.font.getData().lineHeight * 3 / 4, backdropWidth * 2, 1, false);
			game.font.getData().setScale(.2f);				
			int scrollWidth = backdropHeight * 93 / 42;
			game.font.setColor(Color.BLACK);
			game.batcher.draw(game.sprites.StockNameBackdrop, contentXOffset + (backdropWidth - scrollWidth) / 2, yOffset + height - chainSize - shadowSize - backdropHeight * 4, backdropHeight * 93 / 42, backdropHeight);
			game.font.draw(game.batcher, "Experience: " + unit.experience + "/" + unit.experienceNeeded, contentXOffset, nameY - backdropHeight * 3 - statRelativeYOffset + game.font.getData().lineHeight * 6 / 10, backdropWidth, 1, false);
			game.font.draw(game.batcher, "Damage Dealt: " + unit.damageDealt, contentXOffset, nameY - backdropHeight * 3 - statRelativeYOffset - game.font.getData().lineHeight * 4 / 10, backdropWidth, 1, false);
			game.font.draw(game.batcher, "Units Killed: " + unit.unitsKilled, contentXOffset, nameY - backdropHeight * 3 - statRelativeYOffset - game.font.getData().lineHeight * 14 / 10, backdropWidth, 1, false);
			game.font.getData().setScale(.25f);
			game.font.setColor(Color.WHITE);
			game.font.draw(game.batcher, "Biography:", xOffset + chainSize + shadowSize, nameY - backdropHeight * 3 - statRelativeYOffset - game.font.getData().lineHeight * 5 / 4, width - chainSize * 2 - shadowSize * 2, 1, false);
			game.font.getData().setScale(.2f);
			game.font.draw(game.batcher, unit.backStory, xOffset + chainSize + shadowSize, nameY - backdropHeight * 3 - statRelativeYOffset - game.font.getData().lineHeight * 13 / 4, width - chainSize * 2 - shadowSize * 2, 1, true);
		}
	}
	
	public void drawUnitDetails(Unit unit) throws IOException{
		if(unit != null){
			game.font.setColor(Color.BLACK);
			game.font.getData().setScale(.20f);	
			int portraitHeight = tileSize - chainSize - shadowSize;
			int nameBackdropWidth = tileSize * 2 - chainSize - shadowSize;
			int nameBackdropHeight = nameBackdropWidth * 44 / 92;
			int nameY =  height + yOffset - chainSize - shadowSize - nameBackdropHeight;
			int contentXOffset = xOffset + chainSize + shadowSize + (width - (nameBackdropWidth + chainSize * 2 + shadowSize * 2 + portraitHeight + backdropWidth * 2)) / 2;
			final AtlasRegion portraitRegion = game.textureAtlas.findRegion("Portrait" + unit.type + unit.team);
			game.batcher.draw(new Sprite(portraitRegion), contentXOffset, nameY + shadowSize, portraitHeight, portraitHeight);		
			final AtlasRegion nameBackdropRegion = game.textureAtlas.findRegion("NameBackdrop" + unit.team);
			game.batcher.draw(new Sprite(nameBackdropRegion), contentXOffset + portraitHeight, yOffset + height - chainSize - shadowSize - nameBackdropHeight, nameBackdropWidth, nameBackdropHeight);
			game.font.setColor(Color.WHITE);
			game.font.draw(game.batcher, unit.name, contentXOffset + portraitHeight, nameY + (portraitHeight + shadowSize * 2) / 2 + game.font.getData().lineHeight, nameBackdropWidth - (nameBackdropWidth / 10), 1, false);
			game.font.draw(game.batcher, unit.type.toString(), contentXOffset + portraitHeight, nameY + (portraitHeight + shadowSize * 2) / 2 - game.font.getData().lineHeight /4 , nameBackdropWidth - (nameBackdropWidth / 10), 1, false);	
			game.font.setColor(Color.BLACK);
			game.font.getData().setScale(.25f);
			game.batcher.draw(game.sprites.HealthBackdrop, contentXOffset, nameY - backdropHeight, backdropWidth, backdropHeight);
			DrawHealthBar(unit.currentHealth, unit.maximumHealth, unit.isDying, nameY, contentXOffset);
			game.batcher.draw(game.sprites.ExperienceBackdrop, contentXOffset, nameY - backdropHeight * 2, backdropWidth, backdropHeight);
			DrawExperienceBar(unit.experience, unit.experienceNeeded, nameY, contentXOffset);
			final XmlReader reader = new XmlReader();
			final Element xml = reader.parse(Gdx.files.internal("UnitStats.xml"));
			final Element unitStats = xml.getChildByName(unit.type.toString());		
			game.batcher.draw(game.sprites.AttackBackdrop, contentXOffset + nameBackdropWidth + portraitHeight, yOffset + height - chainSize - shadowSize - backdropHeight, backdropWidth, backdropHeight);
			game.batcher.draw(game.sprites.AccuracyBackdrop, contentXOffset + nameBackdropWidth + portraitHeight, yOffset + height - chainSize - shadowSize - backdropHeight * 2, backdropWidth, backdropHeight);
			game.batcher.draw(game.sprites.DefenseBackdrop, contentXOffset + nameBackdropWidth + portraitHeight + backdropWidth, yOffset + height - chainSize - shadowSize - backdropHeight, backdropWidth, backdropHeight);
			game.batcher.draw(game.sprites.EvasionBackdrop, contentXOffset + nameBackdropWidth + portraitHeight + backdropWidth, yOffset + height - chainSize - shadowSize - backdropHeight * 2, backdropWidth, backdropHeight);
			int statTextXOffset = contentXOffset + backdropWidth * 57 / 116 + portraitHeight + nameBackdropWidth;
			int statTextWidth = backdropWidth * 32 / 116;
			int statRelativeYOffset = backdropHeight * 2 / 41 + (backdropHeight * 25 / 41) / 2 - backdropHeight;	
			if(shopState != null){
				if(!shopState.roster.contains(unit)){
					UnitRenderer.SetHealthFont(unit, unitStats, game.font);
					game.font.draw(game.batcher, unit.currentHealth + "/" + unit.maximumHealth, contentXOffset + backdropHeight, nameY - game.font.getData().lineHeight / 2, backdropWidth - (backdropWidth * 2 / 29) - backdropHeight - (nameBackdropWidth / 10), 1, false);
					game.font.setColor(Color.BLACK);
					game.font.draw(game.batcher, (unit.level < 10 ? "Lvl. 0" : "Lvl. ") + unit.level, contentXOffset + backdropHeight, nameY - backdropHeight - game.font.getData().lineHeight / 2, backdropWidth - (backdropWidth * 2 / 29) - backdropHeight - (nameBackdropWidth / 10), 1, false);
					game.font.getData().setScale(.35f);
					UnitRenderer.SetAttackFont(unit, unitStats, game.font);
					game.font.draw(game.batcher, "" + unit.attack, statTextXOffset, nameY - statRelativeYOffset, statTextWidth, 1, false);
					UnitRenderer.SetAccuracyFont(unit, unitStats, game.font);
					game.font.draw(game.batcher, "" + unit.accuracy, statTextXOffset, nameY - backdropHeight - statRelativeYOffset, statTextWidth, 1, false);
					UnitRenderer.SetDefenseFont(unit, unitStats, null, game.font);
					game.font.draw(game.batcher, "" + unit.defense, statTextXOffset + backdropWidth, nameY  - statRelativeYOffset, statTextWidth, 1, false);
					UnitRenderer.SetEvasionFont(unit, unitStats, null, game.font);
					game.font.draw(game.batcher, "" + unit.evasion, statTextXOffset + backdropWidth, nameY - backdropHeight - statRelativeYOffset, statTextWidth, 1, false);
					UnitRenderer.SetMovementFont(unit, unitStats, game.font);
				}else{
					game.font.setColor(Color.BLACK);
					game.font.draw(game.batcher, unit.currentHealth + "/" + unit.maximumHealth, contentXOffset + backdropHeight, nameY - game.font.getData().lineHeight / 2, backdropWidth - (backdropWidth * 2 / 29) - backdropHeight - (nameBackdropWidth / 10), 1, false);
					game.font.draw(game.batcher, (unit.level < 10 ? "Lvl. 0" : "Lvl. ") + unit.level, contentXOffset + backdropHeight, nameY - backdropHeight - game.font.getData().lineHeight / 2, backdropWidth - (backdropWidth * 2 / 29) - backdropHeight - (nameBackdropWidth / 10), 1, false);
					game.font.getData().setScale(.35f);
					game.font.draw(game.batcher, "" + unit.attack, statTextXOffset, nameY - statRelativeYOffset, statTextWidth, 1, false);
					game.font.draw(game.batcher, "" + unit.accuracy, statTextXOffset, nameY - backdropHeight - statRelativeYOffset, statTextWidth, 1, false);
					game.font.draw(game.batcher, "" + unit.defense, statTextXOffset + backdropWidth, nameY  - statRelativeYOffset, statTextWidth, 1, false);
					game.font.draw(game.batcher, "" + unit.evasion, statTextXOffset + backdropWidth, nameY - backdropHeight - statRelativeYOffset, statTextWidth, 1, false);
				}
			}else{
				if(unit.team == 0 || battleState.perksPurchased >= 2){
					game.font.setColor(Color.BLACK);
					game.font.draw(game.batcher, unit.currentHealth + "/" + unit.maximumHealth, contentXOffset + backdropHeight, nameY - game.font.getData().lineHeight / 2, backdropWidth - (backdropWidth * 2 / 29) - backdropHeight - (nameBackdropWidth / 10), 1, false);
					game.font.draw(game.batcher, (unit.level < 10 ? "Lvl. 0" : "Lvl. ") + unit.level, contentXOffset + backdropHeight, nameY - backdropHeight - game.font.getData().lineHeight / 2, backdropWidth - (backdropWidth * 2 / 29) - backdropHeight - (nameBackdropWidth / 10), 1, false);
					game.font.getData().setScale(.35f);
					game.font.draw(game.batcher, "" + unit.attack, statTextXOffset, nameY - statRelativeYOffset, statTextWidth, 1, false);
					game.font.draw(game.batcher, "" + unit.accuracy, statTextXOffset, nameY - backdropHeight - statRelativeYOffset, statTextWidth, 1, false);
					UnitRenderer.SetDefenseFont(unit, null, battleState.battlefield, game.font);
					game.font.draw(game.batcher, "" + unit.defense, statTextXOffset + backdropWidth, nameY  - statRelativeYOffset, statTextWidth, 1, false);
					UnitRenderer.SetEvasionFont(unit, null, battleState.battlefield, game.font);
					game.font.draw(game.batcher, "" + unit.evasion, statTextXOffset + backdropWidth, nameY - backdropHeight - statRelativeYOffset, statTextWidth, 1, false);
				}else{
					game.font.setColor(Color.BLACK);
					game.font.draw(game.batcher, unit.currentHealth + "/" + unit.maximumHealth, contentXOffset + backdropHeight, nameY - game.font.getData().lineHeight / 2, backdropWidth - (backdropWidth * 2 / 29) - backdropHeight - (nameBackdropWidth / 10), 1, false);
					game.font.draw(game.batcher, (unit.level < 10 ? "Lvl. 0" : "Lvl. ") + unit.level, contentXOffset + backdropHeight, nameY - backdropHeight - game.font.getData().lineHeight / 2, backdropWidth - (backdropWidth * 2 / 29) - backdropHeight - (nameBackdropWidth / 10), 1, false);
					game.font.getData().setScale(.35f);
					game.font.draw(game.batcher, "?", statTextXOffset, nameY - statRelativeYOffset, statTextWidth, 1, false);
					game.font.draw(game.batcher, "?", statTextXOffset, nameY - backdropHeight - statRelativeYOffset, statTextWidth, 1, false);
					game.font.draw(game.batcher, "?", statTextXOffset + backdropWidth, nameY  - statRelativeYOffset, statTextWidth, 1, false);
					game.font.draw(game.batcher, "?", statTextXOffset + backdropWidth, nameY - backdropHeight - statRelativeYOffset, statTextWidth, 1, false);
				}
			}
			game.font.setColor(Color.BLACK);
			if(unit.ability != null){
				int abilityIconSize = tileSize * 2 / 5;
				game.batcher.draw(unit.ability.GetAbilityIcon(game), contentXOffset + nameBackdropWidth + portraitHeight + abilityIconSize, yOffset + height - chainSize - shadowSize * 2 - backdropHeight * 2 - abilityIconSize, abilityIconSize, abilityIconSize);
				game.batcher.draw(unit.ability.GetAbilityIcon(game), contentXOffset + nameBackdropWidth * 4 + portraitHeight - abilityIconSize * 3, yOffset + height - chainSize - shadowSize * 2 - backdropHeight * 2 - abilityIconSize, abilityIconSize, abilityIconSize);
				game.font.setColor(Color.WHITE);
				game.font.draw(game.batcher, unit.ability.displayName, contentXOffset + backdropWidth, nameY - backdropHeight * 2 - statRelativeYOffset + game.font.getData().lineHeight / 2 - shadowSize, backdropWidth * 2, 1, false);
				game.font.getData().setScale(.2f);
				game.font.draw(game.batcher, unit.ability.description, contentXOffset + backdropWidth, nameY - backdropHeight * 3 - statRelativeYOffset + game.font.getData().lineHeight * 3, backdropWidth * 2, 1, true);
				game.font.setColor(Color.BLACK);
			}

			int footprintSize = tileSize / 5;
			int totalMovementWidth = footprintSize * unit.movement;
			for(int i = 0; i < unit.movement; i++){
				if(i < unitStats.getInt("Movement") || battleState != null){
					game.batcher.draw(game.sprites.FootprintsBlack, contentXOffset + ((backdropWidth - totalMovementWidth) / 2) + footprintSize * i, yOffset + height - chainSize - shadowSize - backdropHeight * 3 - footprintSize, footprintSize, footprintSize * 23 / 20);
				}
				else{
					game.batcher.draw(game.sprites.FootprintsWhite, contentXOffset + ((backdropWidth - totalMovementWidth) / 2) + footprintSize * i, yOffset + height - chainSize - shadowSize - backdropHeight * 3 - footprintSize, footprintSize, footprintSize * 23 / 20);
				}
			}

			if(unit.team == 0){
				int scrollWidth = backdropHeight * 93 / 42;
				game.batcher.draw(game.sprites.StockNameBackdrop, contentXOffset + (backdropWidth - scrollWidth) / 2, yOffset + height - chainSize - shadowSize - backdropHeight * 4 - footprintSize, backdropHeight * 93 / 42, backdropHeight);
				game.font.getData().setScale(.18f);
				game.font.draw(game.batcher, "Experience: " + unit.experience + "/" + unit.experienceNeeded, contentXOffset, nameY - footprintSize - backdropHeight * 3 - statRelativeYOffset + game.font.getData().lineHeight * 6 / 10, backdropWidth, 1, false);
				game.font.draw(game.batcher, "Damage Dealt: " + unit.damageDealt, contentXOffset, nameY - footprintSize - backdropHeight * 3 - statRelativeYOffset - game.font.getData().lineHeight * 4 / 10, backdropWidth, 1, false);
				game.font.draw(game.batcher, "Units Killed: " + unit.unitsKilled, contentXOffset, nameY - footprintSize - backdropHeight * 3 - statRelativeYOffset - game.font.getData().lineHeight * 14 / 10, backdropWidth, 1, false);
				game.font.getData().setScale(.25f);
				game.font.setColor(Color.WHITE);
				game.font.draw(game.batcher, "Biography:", xOffset + chainSize + shadowSize, nameY - footprintSize - backdropHeight * 3 - statRelativeYOffset - game.font.getData().lineHeight * 3 / 2, width - chainSize * 2 - shadowSize * 2, 1, false);
				game.font.getData().setScale(.2f);
				game.font.draw(game.batcher, unit.backStory, xOffset + chainSize + shadowSize, nameY - footprintSize - backdropHeight * 3 - statRelativeYOffset - game.font.getData().lineHeight * 7 / 2, width - chainSize * 2 - shadowSize * 2, 1, true);
				game.font.setColor(Color.BLACK);
			}
		}
	}
	
	private void DrawHealthBar(int currentHealth, int maxHealth, boolean isDying, int nameY, int contentXOffset){
		int leftOffset = (int) (backdropWidth * .396);
		int rightOffset = (int) (backdropWidth * .155);
		int barWidth = backdropWidth - leftOffset - rightOffset;
		int barHeight = (int) (barWidth * .302);

		int healthPercent =  currentHealth * 100 / maxHealth;
		int healthIndex = (int) (healthPercent / (double)  5);
		if(healthIndex == 0 && !isDying)
			healthIndex++;
		
		if(healthIndex == 20 && currentHealth != maxHealth)
			healthIndex--;
			
		int barYOffset = (int) (backdropHeight * .122);
		game.batcher.draw(new Sprite(game.textureAtlas.findRegion("Health" + healthIndex)), contentXOffset + leftOffset, nameY - backdropHeight + barYOffset, barWidth, barHeight);
	}
	
	private void DrawExperienceBar(int experience, int experienceNeeded, int nameY, int contentXOffset){
		int leftOffset = (int) (backdropWidth * .396);
		int rightOffset = (int) (backdropWidth * .155);
		int barWidth = backdropWidth - leftOffset - rightOffset;
		int barHeight = (int) (barWidth * .302);

		int experiencePercent = experience * 100 / experienceNeeded;
		int experienceIndex = (int) (experiencePercent / (double) 5);
		
		if(experienceIndex == 20 && experience != experienceNeeded)
			experienceIndex--;
		
		int barYOffset = (int) (backdropHeight * .122);
		game.batcher.draw(new Sprite(game.textureAtlas.findRegion("Experience" + experienceIndex)), contentXOffset + leftOffset, nameY + barYOffset - backdropHeight * 2, barWidth, barHeight);	
	}
}
