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
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitType;

public final class UnitRenderer {
		
	public static void DrawUnit(HeroesEmblem game, Unit unit, int x, int y, int scaledSize, String animation, int frame, boolean tapped){
		int animationFrame = frame;
		if(tapped){
			animationFrame = 1;
		}
		AtlasRegion region = game.textureAtlas.findRegion(unit.type + "-" + animation + "-" + animationFrame + "-" + unit.team);
		Sprite sprite = new Sprite(region);
		if(unit.isTakingDamage){
			game.font.setColor(new Color(1f, 0, 0, 1f));
			if(!unit.isDying){
				game.batcher.setColor(new Color(1f, 0, 0, .5f));
			}else{
				game.batcher.setColor(new Color(1f, 0, 0, .1f * unit.deathFrame));
			}
		}else if(unit.isGettingExperience){
			game.font.setColor(new Color(1f, .8f, 0, 1f));
			game.batcher.setColor(new Color(1f, .8f, 0, .5f));
		}else if(unit.isHealing){
			game.font.setColor(new Color(0f, 1f, 0, 1f));
			game.batcher.setColor(new Color(0f, 1f, 0, .5f));
		}else if(tapped){
			game.batcher.setColor(new Color(1f, 1f, 1f, .7f));
		}
		game.batcher.draw(sprite, x, y, scaledSize, scaledSize);
		game.font.getData().setScale(.2f);
		game.font.draw(game.batcher, unit.damageDisplay, x + scaledSize /2, y + scaledSize);
		game.batcher.setColor(Color.WHITE);
	}
	
	public static void DrawUnit(HeroesEmblem game, Unit unit, int x, int y, int scaledSize, String animation, int frame){
		DrawUnit(game,unit, x, y,scaledSize, animation, frame, false);
	}
	
	public static void DrawStockStats(HeroesEmblem game, Unit unit, int initialX, int initialY, int scaledSize) throws IOException{
		XmlReader reader = new XmlReader();
		Element xml = reader.parse(Gdx.files.internal("UnitStats.xml"));
		Element unitStats = xml.getChildByName(unit.type.toString());
		game.font.setColor(Color.WHITE);
		game.font.getData().setScale(.2f);
		game.font.draw(game.batcher, "" + unit.cost, initialX + scaledSize, Gdx.graphics.getHeight());	
		game.font.draw(game.batcher, unit.name, initialX, initialY);	
		SetHealthFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "HP: " + unit.currentHealth + "/" + unit.maximumHealth, initialX, initialY - scaledSize);
		SetAttackFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "ATK: " + unit.attack, initialX, initialY - 2 * scaledSize);
		SetDefenseFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "DEF: " + unit.defense, initialX, initialY - 3 * scaledSize);
		SetEvasionFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "EVP: " + unit.evasion, initialX, initialY - 4 * scaledSize);
		SetAccuracyFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "ACC: " + unit.accuracy, initialX, initialY - 5 * scaledSize);
		SetMovementFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "MOVE: " + unit.movement, initialX, initialY - 6 * scaledSize);
		SetAbilityFont(unit, game.font);
		game.font.draw(game.batcher, "ABILITY: " + unit.ability.displayName, initialX, initialY - 7 * scaledSize);
	}
	
	public static void DrawEnemyStats(HeroesEmblem game, Unit unit, int initialX, int initialY, int scaledSize) throws IOException{
		game.font.setColor(Color.WHITE);
		game.font.getData().setScale(.2f);
		game.font.draw(game.batcher, unit.name, initialX, initialY);	
		game.font.draw(game.batcher, "LVL: " + unit.level, initialX, initialY - 2 * scaledSize);
		game.font.draw(game.batcher, "HP: " + unit.currentHealth + "/" + unit.maximumHealth, initialX, initialY - scaledSize);
	}
	
	public static void DrawOwnedStats(HeroesEmblem game, Unit unit, int initialX, int initialY, int scaledSize) throws IOException{
		game.font.setColor(Color.WHITE);
		game.font.getData().setScale(.2f);
		game.font.draw(game.batcher, unit.name, initialX, initialY);	
		game.font.draw(game.batcher, "LVL: " + unit.level, initialX, initialY - scaledSize);
		game.font.draw(game.batcher, "EXP: " + unit.experience + "/" + unit.experienceNeeded, initialX, initialY - 2 * scaledSize);
		game.font.draw(game.batcher, "HP: " + unit.currentHealth + "/" + unit.maximumHealth, initialX, initialY - 3 * scaledSize);
		game.font.draw(game.batcher, "ATK: " + unit.attack, initialX, initialY - 4 * scaledSize);
		game.font.draw(game.batcher, "DEF: " + unit.defense, initialX, initialY - 5 * scaledSize);
		game.font.draw(game.batcher, "EVP: " + unit.evasion, initialX, initialY - 6 * scaledSize);
		game.font.draw(game.batcher, "ACC: " + unit.accuracy, initialX, initialY - 7 * scaledSize);
		game.font.draw(game.batcher, "MOVE: " + unit.movement, initialX, initialY - 8 * scaledSize);
		game.font.draw(game.batcher, "ABILITY: " + unit.ability.displayName, initialX, initialY - 9 * scaledSize);
	}
	
	public static void SetHealthFont(Unit unit, Element unitStats, BitmapFont font){
		int baseStat = unitStats.getInt("MaximumHealth");
		int currentHealth = unit.currentHealth;
		if (currentHealth == baseStat)
			font.setColor(new Color(255, 255, 255, 1));		
		if (currentHealth > baseStat)
			font.setColor(new Color(255, 215, 0, 1));
		if (currentHealth < baseStat)
			font.setColor(new Color(255, 0, 0, 1));
	}
	
	public static void SetAttackFont(Unit unit, Element unitStats, BitmapFont font){
		int baseStat = unitStats.getInt("Attack");
		int attack = unit.attack;
		if (attack == baseStat)
			font.setColor(new Color(255, 255, 255, 1));	
		if (attack > baseStat)
			font.setColor(new Color(255, 215, 0, 1));
		if (attack < baseStat)
			font.setColor(new Color(255, 0, 0, 1));
	}
	
	public static void SetDefenseFont(Unit unit, Element unitStats, BitmapFont font){
		int baseStat = unitStats.getInt("Defense");
		int defense = unit.defense;
		if (defense == baseStat)
			font.setColor(new Color(255, 255, 255, 1));	
		if (defense > baseStat)
			font.setColor(new Color(255, 215, 0, 1));
		if (defense < baseStat)
			font.setColor(new Color(255, 0, 0, 1));
	}
	
	public static void SetEvasionFont(Unit unit, Element unitStats, BitmapFont font){
		int baseStat = unitStats.getInt("Evasion");
		int evasion = unit.evasion;
		if (evasion == baseStat)
			font.setColor(new Color(255, 255, 255, 1));	
		if (evasion > baseStat)
			font.setColor(new Color(255, 215, 0, 1));
		if (evasion < baseStat)
			font.setColor(new Color(255, 0, 0, 1));		
	}
	
	public static void SetAccuracyFont(Unit unit, Element unitStats, BitmapFont font){
		int baseStat = unitStats.getInt("Accuracy");
		int accuracy = unit.accuracy;
		if (accuracy == baseStat)
			font.setColor(new Color(255, 255, 255, 1));	
		if (accuracy > baseStat)
			font.setColor(new Color(255, 215, 0, 1));
		if (accuracy < baseStat)
			font.setColor(new Color(255, 0, 0, 1));
	}
	
	public static void SetMovementFont(Unit unit, Element unitStats, BitmapFont font){
		int baseStat = unitStats.getInt("Movement");
		int move = unit.movement;
		if (move == baseStat)
			font.setColor(new Color(255, 255, 255, 1));	
		if (move > baseStat)
			font.setColor(new Color(255, 215, 0, 1));
		if (move < baseStat)
			font.setColor(new Color(255, 0, 0, 1));
	}
	
	public static void SetAbilityFont(Unit unit, BitmapFont font){
		String ability = unit.ability.displayName;
		if (ability.equals("None"))
			font.setColor(new Color(255, 0, 0, 1));
		else
			font.setColor(new Color(255, 215, 0, 1));
	}
}
