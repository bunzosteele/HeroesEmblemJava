package bunzosteele.heroesemblem.view;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitType;
import bunzosteele.heroesemblem.model.Battlefield.Tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.XmlReader.Element;

public  class UnitRenderer {
	public static void DrawUnit( HeroesEmblem game,  Unit unit,  int x,  int y,  int scaledSize, boolean isDisplayEmphasis,  boolean tapped,  boolean isFlipped) {
		int animationFrame = 0;
		AtlasRegion region;
		if (unit.isAttacking) {
			animationFrame = unit.attackFrame;
			region = UnitSheets.get(unit.type).findRegion("Attack-" + unit.team + "-" + animationFrame);
		} 
		else 
		{
			if (isDisplayEmphasis) {
				animationFrame = unit.displayEmphasisFrame;
				if (animationFrame > unit.maxIdleFrame) {
					animationFrame -= (unit.maxIdleFrame + 1);
					region = UnitSheets.get(unit.type).findRegion("Attack-" + unit.team + "-" + animationFrame);
				} else {
					region = UnitSheets.get(unit.type).findRegion("Idle-" + unit.team + "-" + animationFrame);
				}
			} else {
				if (!tapped)
					animationFrame = unit.idleFrame;
				
				region = UnitSheets.get(unit.type).findRegion("Idle-" + unit.team + "-" + animationFrame);
			}
		}
		Sprite sprite = new Sprite(region);
		if (tapped && !(unit.isHealing || unit.isGettingExperience || unit.isTakingDamage || unit.isAttacking) && game.battleState != null) {
			game.batcher.setColor(new Color(1f, 1f, 1f, .7f));
		}
		
		if (isFlipped) sprite.flip(true, false);
		game.batcher.draw(sprite, x, y, scaledSize, scaledSize);	
		if (unit.isHealing && game.battleState != null && unit.damageDisplay >=0) {
			game.font.setColor(new Color(1f, 1f, 1f, 1f));
			game.font.getData().setScale(.33f);
			game.batcher.draw(game.sprites.HealBackground, x + scaledSize / 5, y + scaledSize / 5, (int) (scaledSize * .6), (int) (scaledSize * .6));
			game.font.draw(game.batcher, "" + unit.damageDisplay, x + scaledSize / 10, y + scaledSize / 2 + game.font.getLineHeight() * 2 / 5, (int) (scaledSize * .8), 1, false);
		}
		if (unit.isGettingExperience && game.battleState != null && unit.damageDisplay >= 0) {
			game.font.setColor(new Color(1f, 1f, 1f, 1f));
			game.font.getData().setScale(.33f);
			game.batcher.draw(game.sprites.ExperienceBackground, x + scaledSize / 5, y + scaledSize / 5, (int) (scaledSize * .6), (int) (scaledSize * .6));
			game.font.draw(game.batcher, "" + unit.damageDisplay, x + scaledSize / 10, y + scaledSize / 2 + game.font.getLineHeight() / 3, (int) (scaledSize * .8), 1, false);
		}
		if (unit.isTakingDamage && game.battleState != null && unit.damageDisplay > 0) {
			game.font.getData().setScale(.33f);
			Sprite background = unit.damageDisplay > 9 ? game.sprites.CritBackground : game.sprites.DamageBackground;
			game.batcher.draw(background, x + scaledSize / 10, y + scaledSize / 10, (int) (scaledSize * .8), (int) (scaledSize * .8));
			game.font.setColor(new Color(0, 0, 0, 1f));
			game.font.draw(game.batcher, "" + unit.damageDisplay, x + scaledSize / 10, y + scaledSize / 2 + game.font.getLineHeight() / 3, (int) (scaledSize * .8), 1, false);
		}else if (unit.isTakingDamage && game.battleState != null) {
			game.font.getData().setScale(.33f);
			game.font.setColor(new Color(1f, 1f, 1f, 1f));
			game.font.draw(game.batcher, "BLOCK!", x + scaledSize / 10, y + scaledSize / 2 + game.font.getLineHeight() / 2, (int) (scaledSize * .8), 1, false);
		}
		if (unit.isMissed && game.battleState != null) {
			game.font.getData().setScale(.33f);
			game.font.setColor(new Color(1f, 1f, 1f, 1f));
			game.font.draw(game.batcher, "MISS!", x + scaledSize / 10, y + scaledSize / 2 + game.font.getLineHeight() / 2, (int) (scaledSize * .8), 1, false);
		}
		if (unit.isLevelingUp && game.battleState != null) {
			game.font.getData().setScale(.33f);
			game.font.setColor(new Color(1f, 1f, 1f, 1f));
			game.font.draw(game.batcher, "LVL-UP!", x + scaledSize / 10, y + scaledSize / 2 + game.font.getLineHeight() / 2, (int) (scaledSize * .8), 1, false);
		}
		game.font.setColor(Color.WHITE);
		game.batcher.setColor(Color.WHITE);
	}

	public static void SetAccuracyFont( Unit unit,  Element unitStats,  BitmapFont font) {
		int baseStat = unitStats.getInt("Accuracy");
		int accuracy = unit.accuracy;
		if (accuracy == baseStat) {
			font.setColor(new Color(0, 0, 0, 1));
		}
		if (accuracy > baseStat) {
			font.setColor(new Color(0, 0, 255, 1));
		}
		if (accuracy < baseStat) {
			font.setColor(new Color(.5f, 0f, 0f, 1f));
		}
	}

	public static void SetAttackFont( Unit unit,  Element unitStats, BitmapFont font) {
		int baseStat = unitStats.getInt("Attack");
		int attack = unit.attack;
		if (attack == baseStat) {
			font.setColor(new Color(0, 0, 0, 1));
		}
		if (attack > baseStat) {
			font.setColor(new Color(0f, 0f, 1f, 1f));
		}
		if (attack < baseStat) {
			font.setColor(new Color(.5f, 0f, 0f, 1f));
		}
	}

	public static void SetDefenseFont(Unit unit,  Element unitStats, List<List<Tile>> battlefield,  BitmapFont font) {
		int baseStat;
		int defense;
		if (unitStats != null) {
			baseStat = unitStats.getInt("Defense");
			defense = unit.defense;
		} else if (battlefield != null && (unit.y >= 0 && unit.x >= 0)) {
			baseStat = 0;
			defense = battlefield.get(unit.y).get(unit.x).defenseModifier;
		}else{
			baseStat = 0;
			defense = 0;
		}
		if (defense > baseStat) {
			font.setColor(new Color(0f, 0f, 1f, 1f));
			return;
		}
		if (defense < baseStat) {
			font.setColor(new Color(.5f, 0f, 0f, 1f));
			return;
		}		
		font.setColor(new Color(0, 0, 0, 1));
	}

	public static void SetEvasionFont( Unit unit,  Element unitStats, List<List<Tile>> battlefield,  BitmapFont font) {
		int baseStat;
		int evasion;
		if (unitStats != null) {
			baseStat = unitStats.getInt("Evasion");
			evasion = unit.evasion;
		} else if (battlefield != null && (unit.y >= 0 && unit.x >= 0)) {
			baseStat = 0;
			evasion = battlefield.get(unit.y).get(unit.x).evasionModifier;
		} else {
			baseStat = 0;
			evasion = 0;
		}
		if (evasion > baseStat) {
			font.setColor(new Color(0f, 0f, 1f, 1f));
			return;
		}
		if (evasion < baseStat) {
			font.setColor(new Color(.5f, 0f, 0f, 1f));
			return;
		}
		font.setColor(new Color(0, 0, 0, 1));	
	}

	public static void SetHealthFont( Unit unit,  Element unitStats,
			 BitmapFont font) {
		 int baseStat = unitStats.getInt("MaximumHealth");
		 int currentHealth = unit.currentHealth;
		if (currentHealth == baseStat) {
			font.setColor(new Color(0, 0, 0, 1));
		}
		if (currentHealth > baseStat) {
			font.setColor(new Color(0, 0, 255, 1));
		}
		if (currentHealth < baseStat) {
			font.setColor(new Color(.7f, .4f, .4f, 1f));
		}
	}
	
	public static void SetMovementFont( Unit unit,  Element unitStats, BitmapFont font) {
		int baseStat = unitStats.getInt("Movement");
		int movement = unit.movement;
		if (movement == baseStat) {
			font.setColor(new Color(0, 0, 0, 1));
		}
		if (movement > baseStat) {
			font.setColor(new Color(0, 0, 255, 1));
		}
		if (movement < baseStat) {
			font.setColor(new Color(.7f, .4f, .4f, 1f));
		}
	}

	public static Map<UnitType, TextureAtlas> UnitSheets = CreateUnitSheets();

	private static Map<UnitType, TextureAtlas> CreateUnitSheets() {
		Map<UnitType, TextureAtlas> unitSheets = new HashMap<UnitType, TextureAtlas>();
		unitSheets.put(UnitType.Archer,
				new TextureAtlas(Gdx.files.internal("Archer.pack")));
		unitSheets.put(UnitType.Footman,
				new TextureAtlas(Gdx.files.internal("Footman.pack")));
		unitSheets.put(UnitType.Knight,
				new TextureAtlas(Gdx.files.internal("Knight.pack")));
		unitSheets.put(UnitType.Mage,
				new TextureAtlas(Gdx.files.internal("Mage.pack")));
		unitSheets.put(UnitType.Priest,
				new TextureAtlas(Gdx.files.internal("Priest.pack")));
		unitSheets.put(UnitType.Spearman,
				new TextureAtlas(Gdx.files.internal("Spearman.pack")));
		return unitSheets;
	}
}
