package bunzosteele.heroesemblem.view;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public final class UnitRenderer
{

	public static void DrawEnemyStats(final HeroesEmblem game, final Unit unit, final int initialX, final int initialY, final int scaledSize, boolean hasSpies) throws IOException
	{
		game.font.setColor(Color.WHITE);
		game.font.getData().setScale(.18f);
		game.font.draw(game.batcher, unit.name, initialX, initialY - game.font.getData().lineHeight);
		game.font.getData().setScale(.20f);
		game.font.draw(game.batcher, "HP: " + unit.currentHealth + "/" + unit.maximumHealth, initialX, initialY - 2 * game.font.getData().lineHeight);
		game.font.draw(game.batcher, "LVL: " + unit.level, initialX, initialY - (3 * game.font.getData().lineHeight));
		if(hasSpies){
			game.font.draw(game.batcher, "ATK: " + unit.attack, initialX, initialY - (4 * game.font.getData().lineHeight));
			game.font.draw(game.batcher, "DEF: " + unit.defense, initialX, initialY - (5 * game.font.getData().lineHeight));
			game.font.draw(game.batcher, "EVP: " + unit.evasion, initialX, initialY - (6 * game.font.getData().lineHeight));
			game.font.draw(game.batcher, "ACC: " + unit.accuracy, initialX, initialY - (7 * game.font.getData().lineHeight));
			game.font.draw(game.batcher, "MOVE: " + unit.movement, initialX, initialY - (8 * game.font.getData().lineHeight));		
		}	
		game.font.getData().setScale(.33f);
	}

	public static void DrawOwnedStats(final HeroesEmblem game, final Unit unit, final int initialX, final int initialY, final int scaledSize) throws IOException
	{
		game.font.setColor(Color.WHITE);
		game.font.getData().setScale(.18f);
		game.font.draw(game.batcher, unit.name, initialX, initialY - game.font.getData().lineHeight);
		game.font.getData().setScale(.20f);
		game.font.draw(game.batcher, "LVL: " + unit.level, initialX, initialY - 2 * game.font.getData().lineHeight);
		game.font.draw(game.batcher, "EXP: " + unit.experience + "/" + unit.experienceNeeded, initialX, initialY - (3 * game.font.getData().lineHeight));
		game.font.draw(game.batcher, "HP: " + unit.currentHealth + "/" + unit.maximumHealth, initialX, initialY - (4 * game.font.getData().lineHeight));
		game.font.draw(game.batcher, "ATK: " + unit.attack, initialX, initialY - (5 * game.font.getData().lineHeight));
		game.font.draw(game.batcher, "DEF: " + unit.defense, initialX, initialY - (6 * game.font.getData().lineHeight));
		game.font.draw(game.batcher, "EVP: " + unit.evasion, initialX, initialY - (7 * game.font.getData().lineHeight));
		game.font.draw(game.batcher, "ACC: " + unit.accuracy, initialX, initialY - (8 * game.font.getData().lineHeight));
		game.font.draw(game.batcher, "MOVE: " + unit.movement, initialX, initialY - (9 * game.font.getData().lineHeight));
		game.font.draw(game.batcher, "ABILITY:", initialX, initialY - (10 * game.font.getData().lineHeight));
		if (unit.ability == null)
		{
			game.font.draw(game.batcher, "NONE", initialX, initialY - (11 * game.font.getData().lineHeight));
		} else
		{
			game.font.draw(game.batcher, unit.ability.displayName, initialX, initialY - (11 * game.font.getData().lineHeight));
		}
		game.font.getData().setScale(.33f);
	}

	public static void DrawStockStats(final HeroesEmblem game, final Unit unit, final int initialX, final int initialY, final int scaledSize) throws IOException
	{
		final XmlReader reader = new XmlReader();
		final Element xml = reader.parse(Gdx.files.internal("UnitStats.xml"));
		final Element unitStats = xml.getChildByName(unit.type.toString());
		game.font.setColor(Color.WHITE);
		game.font.getData().setScale(.33f);
		game.font.draw(game.batcher, "" + unit.cost, initialX + scaledSize, initialY + game.font.getData().lineHeight / 2);
		game.font.getData().setScale(.18f);
		game.font.draw(game.batcher, unit.name, initialX, initialY - game.font.getData().lineHeight);
		game.font.getData().setScale(.20f);
		UnitRenderer.SetHealthFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "HP: " + unit.currentHealth + "/" + unit.maximumHealth, initialX, initialY - 2 * game.font.getData().lineHeight);
		UnitRenderer.SetAttackFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "ATK: " + unit.attack, initialX, initialY - (3 * game.font.getData().lineHeight));
		UnitRenderer.SetDefenseFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "DEF: " + unit.defense, initialX, initialY - (4 * game.font.getData().lineHeight));
		UnitRenderer.SetEvasionFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "EVP: " + unit.evasion, initialX, initialY - (5 * game.font.getData().lineHeight));
		UnitRenderer.SetAccuracyFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "ACC: " + unit.accuracy, initialX, initialY - (6 * game.font.getData().lineHeight));
		UnitRenderer.SetMovementFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "MOVE: " + unit.movement, initialX, initialY - (7 * game.font.getData().lineHeight));
		UnitRenderer.SetAbilityFont(unit, game.font);
		game.font.draw(game.batcher, "ABILITY:", initialX, initialY - (8 * game.font.getData().lineHeight));
		if (unit.ability == null)
		{
			game.font.draw(game.batcher, "NONE", initialX, initialY - (9 * game.font.getData().lineHeight));
		} else
		{
			game.font.draw(game.batcher, unit.ability.displayName, initialX, initialY - (9 * game.font.getData().lineHeight));
		}
		game.font.getData().setScale(.33f);
	}
	
	public static void DrawStockInfo(final HeroesEmblem game, final Unit unit, final int initialX, final int initialY, final int scaledSize, final int totalWidth, final int xOffset) throws IOException
	{
		int columnWidth = (totalWidth - (initialX - xOffset)) / 3;
		final XmlReader reader = new XmlReader();
		final Element xml = reader.parse(Gdx.files.internal("UnitStats.xml"));
		final Element unitStats = xml.getChildByName(unit.type.toString());
		game.font.setColor(Color.WHITE);
		game.font.getData().setScale(.25f);
		game.font.draw(game.batcher, "Cost:" + unit.cost, initialX, initialY, columnWidth, 1, false);
		game.font.draw(game.batcher, unit.name, initialX, initialY - game.font.getData().lineHeight, columnWidth, 1, false);
		UnitRenderer.SetHealthFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "HP: " + unit.currentHealth + "/" + unit.maximumHealth, initialX + 2 * columnWidth, initialY - 2 * game.font.getData().lineHeight, columnWidth, 1, false);
		UnitRenderer.SetAttackFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "ATK: " + unit.attack, initialX + columnWidth, initialY, columnWidth, 1, false);
		UnitRenderer.SetDefenseFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "DEF: " + unit.defense, initialX + columnWidth, initialY - game.font.getData().lineHeight, columnWidth, 1, false);
		UnitRenderer.SetEvasionFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "EVP: " + unit.evasion, initialX + columnWidth, initialY - (2 * game.font.getData().lineHeight), columnWidth, 1, false);
		UnitRenderer.SetAccuracyFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "ACC: " + unit.accuracy, initialX + 2 * columnWidth, initialY, columnWidth, 1, false);
		UnitRenderer.SetMovementFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "MOVE: " + unit.movement, initialX + 2 * columnWidth, initialY - game.font.getData().lineHeight, columnWidth, 1, false);
		UnitRenderer.SetAbilityFont(unit, game.font);
		game.font.draw(game.batcher, "ABILITY:", initialX, initialY  - (2 * game.font.getData().lineHeight), columnWidth, 1, false);
		if (unit.ability == null)
		{
			game.font.draw(game.batcher, "NONE", initialX, initialY - (3 * game.font.getData().lineHeight), columnWidth, 1, false);
		} else
		{
			game.font.draw(game.batcher, unit.ability.displayName, initialX, initialY - (3 * game.font.getData().lineHeight), columnWidth, 1, false);
		}
		game.font.setColor(Color.WHITE);		
		game.font.draw(game.batcher, unit.backStory, xOffset + ((initialX - xOffset) / 2), initialY - 5 * game.font.getData().lineHeight, totalWidth - (initialX - xOffset), 1, true);
		game.font.getData().setScale(.33f);
	}
	
	public static void DrawOwnedInfo(final HeroesEmblem game, final Unit unit, final int initialX, final int initialY, final int scaledSize, final int totalWidth, int xOffset) throws IOException
	{
		int columnWidth = (totalWidth - (initialX - xOffset)) / 3;
		final XmlReader reader = new XmlReader();
		final Element xml = reader.parse(Gdx.files.internal("UnitStats.xml"));
		final Element unitStats = xml.getChildByName(unit.type.toString());
		game.font.setColor(Color.WHITE);
		game.font.getData().setScale(.25f);
		game.font.draw(game.batcher, unit.name, initialX, initialY, columnWidth, 1, false);
		UnitRenderer.SetHealthFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "HP: " + unit.currentHealth + "/" + unit.maximumHealth, initialX + 2 * columnWidth, initialY - 2 * game.font.getData().lineHeight, columnWidth, 1, false);
		UnitRenderer.SetAttackFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "ATK: " + unit.attack, initialX + columnWidth, initialY, columnWidth, 1, false);
		UnitRenderer.SetDefenseFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "DEF: " + unit.defense, initialX + columnWidth, initialY - game.font.getData().lineHeight, columnWidth, 1, false);
		UnitRenderer.SetEvasionFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "EVP: " + unit.evasion, initialX + columnWidth, initialY - (2 * game.font.getData().lineHeight), columnWidth, 1, false);
		UnitRenderer.SetAccuracyFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "ACC: " + unit.accuracy, initialX + 2 * columnWidth, initialY, columnWidth, 1, false);
		UnitRenderer.SetMovementFont(unit, unitStats, game.font);
		game.font.draw(game.batcher, "MOVE: " + unit.movement, initialX + 2 * columnWidth, initialY - game.font.getData().lineHeight, columnWidth, 1, false);
		UnitRenderer.SetAbilityFont(unit, game.font);
		game.font.draw(game.batcher, "ABILITY:", initialX, initialY  - (1 * game.font.getData().lineHeight), columnWidth, 1, false);
		if (unit.ability == null)
		{
			game.font.draw(game.batcher, "NONE", initialX, initialY - (2 * game.font.getData().lineHeight), columnWidth, 1, false);
		} else
		{
			game.font.draw(game.batcher, unit.ability.displayName, initialX, initialY - (2 * game.font.getData().lineHeight), columnWidth, 1, false);
		}
		game.font.setColor(Color.WHITE);
		game.font.draw(game.batcher, "LVL: " + unit.level, initialX - columnWidth, initialY - 4 * game.font.getData().lineHeight, columnWidth, 1, false);
		game.font.draw(game.batcher, "Exp: " + unit.experience + " / " + unit.experienceNeeded, initialX, initialY - 4 * game.font.getData().lineHeight, columnWidth, 1, false);
		game.font.draw(game.batcher, "DMG: " + unit.damageDealt, initialX + columnWidth, initialY - 4 * game.font.getData().lineHeight, columnWidth, 1, false);
		game.font.draw(game.batcher, "KILLS: " + unit.unitsKilled, initialX + 2 * columnWidth, initialY - 4 * game.font.getData().lineHeight, columnWidth, 1, false);
		game.font.draw(game.batcher, unit.backStory, xOffset + ((initialX - xOffset) / 2), initialY - 5 * game.font.getData().lineHeight, totalWidth - (initialX - xOffset), 1, true);
		game.font.getData().setScale(.33f);
	}

	public static void DrawUnit(final HeroesEmblem game, final Unit unit, final int x, final int y, final int scaledSize, boolean isDisplayEmphasis, final boolean tapped)
	{
		int animationFrame = 0;
		AtlasRegion region;
		if(unit.isAttacking){
			animationFrame = unit.attackFrame;
			region = UnitSheets.get(unit.type).findRegion("Attack-" + unit.team + "-" + animationFrame);
		}else{
			if(isDisplayEmphasis){
				animationFrame = unit.displayEmphasisFrame;
				if(animationFrame > unit.maxIdleFrame){
					animationFrame -= (unit.maxIdleFrame + 1);
					region = UnitSheets.get(unit.type).findRegion("Attack-" + unit.team + "-" + animationFrame);	
				}else{
					region = UnitSheets.get(unit.type).findRegion("Idle-" + unit.team + "-" + animationFrame);	
				}
			}else{
				if(!tapped)
					animationFrame = unit.idleFrame;
				region = UnitSheets.get(unit.type).findRegion("Idle-" + unit.team + "-" + animationFrame);
			}
		}
		final Sprite sprite = new Sprite(region);
		if (unit.isTakingDamage && game.battleState != null)
		{
			game.font.setColor(new Color(1f, 0, 0, 1f));
			if (!unit.isDying)
			{
				game.batcher.setColor(new Color(1f, 0, 0, .5f));
			} else
			{
				game.batcher.setColor(new Color(1f, 0, 0, .1f * unit.deathFrame));
			}
		}
		if (unit.isGettingExperience && game.battleState != null)
		{
			game.font.setColor(new Color(1f, .8f, 0, 1f));
			game.batcher.setColor(new Color(1f, .8f, 0, .5f));
		}
		if (unit.isHealing && game.battleState != null)
		{
			game.font.setColor(new Color(0f, 1f, 0, 1f));
			game.batcher.setColor(new Color(0f, 1f, 0, .5f));
		}
		if (tapped && !(unit.isHealing || unit.isGettingExperience || unit.isTakingDamage || unit.isAttacking) && game.battleState != null)
		{
			game.batcher.setColor(new Color(1f, 1f, 1f, .7f));
		}
		game.batcher.draw(sprite, x, y, scaledSize, scaledSize);
		if((unit.isHealing || unit.isGettingExperience || unit.isTakingDamage || unit.isMissed) && game.battleState != null){
			game.font.getData().setScale(.165f);
			game.font.draw(game.batcher, unit.damageDisplay, x + (scaledSize / 2), y + scaledSize);
			game.font.getData().setScale(.33f);
		}
		game.batcher.setColor(Color.WHITE);
	}

	public static void SetAbilityFont(final Unit unit, final BitmapFont font)
	{
		if (unit.ability == null)
		{
			font.setColor(new Color(255, 0, 0, 1));
		} else
		{
			font.setColor(new Color(255, 215, 0, 1));
		}
	}

	public static void SetAccuracyFont(final Unit unit, final Element unitStats, final BitmapFont font)
	{
		final int baseStat = unitStats.getInt("Accuracy");
		final int accuracy = unit.accuracy;
		if (accuracy == baseStat)
		{
			font.setColor(new Color(255, 255, 255, 1));
		}
		if (accuracy > baseStat)
		{
			font.setColor(new Color(255, 215, 0, 1));
		}
		if (accuracy < baseStat)
		{
			font.setColor(new Color(255, 0, 0, 1));
		}
	}

	public static void SetAttackFont(final Unit unit, final Element unitStats, final BitmapFont font)
	{
		final int baseStat = unitStats.getInt("Attack");
		final int attack = unit.attack;
		if (attack == baseStat)
		{
			font.setColor(new Color(255, 255, 255, 1));
		}
		if (attack > baseStat)
		{
			font.setColor(new Color(255, 215, 0, 1));
		}
		if (attack < baseStat)
		{
			font.setColor(new Color(255, 0, 0, 1));
		}
	}

	public static void SetDefenseFont(final Unit unit, final Element unitStats, final BitmapFont font)
	{
		final int baseStat = unitStats.getInt("Defense");
		final int defense = unit.defense;
		if (defense == baseStat)
		{
			font.setColor(new Color(255, 255, 255, 1));
		}
		if (defense > baseStat)
		{
			font.setColor(new Color(255, 215, 0, 1));
		}
		if (defense < baseStat)
		{
			font.setColor(new Color(255, 0, 0, 1));
		}
	}

	public static void SetEvasionFont(final Unit unit, final Element unitStats, final BitmapFont font)
	{
		final int baseStat = unitStats.getInt("Evasion");
		final int evasion = unit.evasion;
		if (evasion == baseStat)
		{
			font.setColor(new Color(255, 255, 255, 1));
		}
		if (evasion > baseStat)
		{
			font.setColor(new Color(255, 215, 0, 1));
		}
		if (evasion < baseStat)
		{
			font.setColor(new Color(255, 0, 0, 1));
		}
	}

	public static void SetHealthFont(final Unit unit, final Element unitStats, final BitmapFont font)
	{
		final int baseStat = unitStats.getInt("MaximumHealth");
		final int currentHealth = unit.currentHealth;
		if (currentHealth == baseStat)
		{
			font.setColor(new Color(255, 255, 255, 1));
		}
		if (currentHealth > baseStat)
		{
			font.setColor(new Color(255, 215, 0, 1));
		}
		if (currentHealth < baseStat)
		{
			font.setColor(new Color(255, 0, 0, 1));
		}
	}

	public static void SetMovementFont(final Unit unit, final Element unitStats, final BitmapFont font)
	{
		final int baseStat = unitStats.getInt("Movement");
		final int move = unit.movement;
		if (move == baseStat)
		{
			font.setColor(new Color(255, 255, 255, 1));
		}
		if (move > baseStat)
		{
			font.setColor(new Color(255, 215, 0, 1));
		}
		if (move < baseStat)
		{
			font.setColor(new Color(255, 0, 0, 1));
		}
	}
	
	public static Map<UnitType, TextureAtlas> UnitSheets = CreateUnitSheets();
	
	private static Map<UnitType, TextureAtlas> CreateUnitSheets(){
		Map<UnitType, TextureAtlas> unitSheets = new HashMap<UnitType, TextureAtlas>();
		unitSheets.put(UnitType.Archer, new TextureAtlas(Gdx.files.internal("Archer.pack")));
		unitSheets.put(UnitType.Footman, new TextureAtlas(Gdx.files.internal("Footman.pack")));
		unitSheets.put(UnitType.Knight, new TextureAtlas(Gdx.files.internal("Knight.pack")));
		unitSheets.put(UnitType.Mage, new TextureAtlas(Gdx.files.internal("Mage.pack")));
		unitSheets.put(UnitType.Priest, new TextureAtlas(Gdx.files.internal("Priest.pack")));
		unitSheets.put(UnitType.Spearman, new TextureAtlas(Gdx.files.internal("Spearman.pack")));
		return unitSheets;
	}
}
