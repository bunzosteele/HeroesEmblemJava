package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public final class UnitRenderer
{

	public static void DrawEnemyStats(final HeroesEmblem game, final Unit unit, final int initialX, final int initialY, final int scaledSize) throws IOException
	{
		game.font.setColor(Color.WHITE);
		game.font.getData().setScale(.18f);
		game.font.draw(game.batcher, unit.name, initialX, initialY - game.font.getData().lineHeight);
		game.font.draw(game.batcher, "HP: " + unit.currentHealth + "/" + unit.maximumHealth, initialX, initialY - 2 * game.font.getData().lineHeight);
		game.font.draw(game.batcher, "LVL: " + unit.level, initialX, initialY - (3 * game.font.getData().lineHeight));
		game.font.getData().setScale(.33f);
	}

	public static void DrawOwnedStats(final HeroesEmblem game, final Unit unit, final int initialX, final int initialY, final int scaledSize) throws IOException
	{
		game.font.setColor(Color.WHITE);
		game.font.getData().setScale(.18f);
		game.font.draw(game.batcher, unit.name, initialX, initialY - game.font.getData().lineHeight);
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
		game.font.draw(game.batcher, "" + unit.cost, initialX + scaledSize, Gdx.graphics.getHeight() - (scaledSize / 4) - game.font.getData().lineHeight / 2);
		game.font.getData().setScale(.18f);
		game.font.draw(game.batcher, unit.name, initialX, initialY - game.font.getData().lineHeight);
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

	public static void DrawUnit(final HeroesEmblem game, final Unit unit, final int x, final int y, final int scaledSize, final String animation, final int frame)
	{
		UnitRenderer.DrawUnit(game, unit, x, y, scaledSize, animation, frame, false);
	}

	public static void DrawUnit(final HeroesEmblem game, final Unit unit, final int x, final int y, final int scaledSize, final String animation, final int frame, final boolean tapped)
	{
		int animationFrame = frame;
		if (tapped)
		{
			animationFrame = 1;
		}
		final AtlasRegion region = game.textureAtlas.findRegion(unit.type + "-" + animation + "-" + animationFrame + "-" + unit.team);
		final Sprite sprite = new Sprite(region);
		if (unit.isTakingDamage)
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
		if (unit.isGettingExperience)
		{
			game.font.setColor(new Color(1f, .8f, 0, 1f));
			game.batcher.setColor(new Color(1f, .8f, 0, .5f));
		}
		if (unit.isHealing)
		{
			game.font.setColor(new Color(0f, 1f, 0, 1f));
			game.batcher.setColor(new Color(0f, 1f, 0, .5f));
		}
		if (tapped && !(unit.isHealing || unit.isGettingExperience || unit.isTakingDamage))
		{
			game.batcher.setColor(new Color(1f, 1f, 1f, .7f));
		}
		game.batcher.draw(sprite, x, y, scaledSize, scaledSize);
		game.font.getData().setScale(.165f);
		game.font.draw(game.batcher, unit.damageDisplay, x + (scaledSize / 2), y + scaledSize);
		game.font.getData().setScale(.33f);
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
}
