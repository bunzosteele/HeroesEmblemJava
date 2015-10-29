package bunzosteele.heroesemblem.model.Units;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bunzosteele.heroesemblem.HeroesEmblem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public final class UnitGenerator
{
	private static int id = 0;
	

	private static int GenerateAbility(int team)
	{
		if (team > 0)
			return 0;
		
		final Random random = new Random();
		final int roll = random.nextInt(101);

		if (roll <= 50)
		{
			return 0;
		}
		if (roll <= 80)
		{
			return 1;
		}
		return 2;
	}

	private static int GenerateAccuracyBonus()
	{
		final Random random = new Random();
		final int roll = random.nextInt(21) - 1;
		return roll;
	}

	private static int GenerateAttackBonus()
	{
		final Random random = new Random();
		final int roll = random.nextInt(101);

		if (roll > 99)
		{
			return 3;
		}
		if (roll > 90)
		{
			return 2;
		}
		if (roll > 80)
		{
			return 2;
		}
		if (roll > 70)
		{
			return 1;
		}
		if (roll < 30)
		{
			return -1;
		}
		if (roll < 10)
		{
			return -2;
		}
		return 0;
	}

	private static int GenerateDefenseBonus()
	{
		final Random random = new Random();
		final int roll = random.nextInt(101);

		if (roll > 99)
		{
			return 3;
		}
		if (roll > 90)
		{
			return 2;
		}
		if (roll > 80)
		{
			return 2;
		}
		if (roll > 70)
		{
			return 1;
		}
		if (roll < 30)
		{
			return -1;
		}
		if (roll < 10)
		{
			return -2;
		}
		return 0;
	}

	public static List<Unit> GenerateEnemies(final int maxEnemies, final int difficulty, List<Unit> roster, HeroesEmblem game) throws IOException
	{
		final List<Unit> enemies = new ArrayList<Unit>();
		enemies.add(UnitGenerator.GenerateUnit(1, UnitGenerator.GenerateUnitType(), roster, game));
		int remainingPoints = difficulty;
		final Random random = new Random();
		while (remainingPoints > 0)
		{
			if (enemies.size() < maxEnemies)
			{
				final int roll = random.nextInt(2);
				if (roll == 0)
				{
					final int levelUpRoll = random.nextInt(enemies.size());
					enemies.get(levelUpRoll).AddExperience(50);
				} else
				{
					enemies.add(UnitGenerator.GenerateUnit(1, UnitGenerator.GenerateUnitType(), roster, game));
				}
			} else
			{
				final int levelUpRoll = random.nextInt(enemies.size());
				enemies.get(levelUpRoll).AddExperience(50);
			}
			remainingPoints--;
		}

		return enemies;
	}

	private static int GenerateEvasionBonus()
	{
		final Random random = new Random();
		final int roll = random.nextInt(11) - 5;
		return roll;
	}

	private static int GenerateHealthBonus()
	{
		final Random random = new Random();
		final int roll = random.nextInt(101);

		if (roll > 99)
		{
			return 5;
		}
		if (roll > 95)
		{
			return 5;
		}
		if (roll > 90)
		{
			return 3;
		}
		if (roll > 80)
		{
			return 2;
		}
		if (roll > 70)
		{
			return 1;
		}
		if (roll < 30)
		{
			return -1;
		}
		if (roll < 20)
		{
			return -2;
		}
		if (roll < 10)
		{
			return -3;
		}
		if (roll < 5)
		{
			return -4;
		}
		if (roll < 1)
		{
			return -5;
		}
		return 0;
	}

	private static int GenerateMovementBonus()
	{
		final Random random = new Random();
		final int roll = random.nextInt(101);

		if (roll > 90)
		{
			return 1;
		}
		if (roll < 10)
		{
			return -1;
		}
		return 0;
	}

	public static List<Unit> GenerateStock(List<Unit> roster, HeroesEmblem game) throws IOException
	{
		final List<Unit> stock = new ArrayList<Unit>();
		while (stock.size() < 8)
		{
			final UnitType unitType = UnitGenerator.GenerateUnitType();
			stock.add(UnitGenerator.GenerateUnit(0, unitType, roster, game));
		}
		return stock;
	}

	public static Unit GenerateUnit(final int team, final UnitType type, List<Unit> roster, final HeroesEmblem game) throws IOException
	{
		UnitGenerator.id++;
		int costModifier = 0;
		final int attackBonus = UnitGenerator.GenerateAttackBonus();
		costModifier += attackBonus * 50;
		final int defenseBonus = UnitGenerator.GenerateDefenseBonus();
		costModifier += defenseBonus * 50;
		final int evasionBonus = UnitGenerator.GenerateEvasionBonus();
		costModifier += evasionBonus * 10;
		final int accuracyBonus = UnitGenerator.GenerateAccuracyBonus();
		costModifier += accuracyBonus * 5;
		final int movementBonus = UnitGenerator.GenerateMovementBonus();
		costModifier += movementBonus * 100;
		final int healthBonus = UnitGenerator.GenerateHealthBonus();
		costModifier += healthBonus * 25;
		final int ability = UnitGenerator.GenerateAbility(team);
		costModifier += ability * 250;
		if (ability == 0)
		{
			costModifier -= 250;
		}

		final XmlReader reader = new XmlReader();
		final Element xml = reader.parse(Gdx.files.internal("UnitStats.xml"));
		final Element unitStats = xml.getChildByName(type.toString());
		final int attack = unitStats.getInt("Attack") + attackBonus;
		final int defense = unitStats.getInt("Defense") + defenseBonus;
		final int evasion = unitStats.getInt("Evasion") + evasionBonus;
		final int accuracy = unitStats.getInt("Accuracy") + accuracyBonus;
		final int movement = unitStats.getInt("Movement") + movementBonus;
		final int maximumHealth = unitStats.getInt("MaximumHealth") + healthBonus;
		final int maximumRange = unitStats.getInt("MaximumRange");
		final int minimumRange = unitStats.getInt("MinimumRange");
		final int cost = unitStats.getInt("Cost") + costModifier;

		final String name = UnitGenerator.GenerateUnitName(roster);

		if (type == UnitType.Spearman)
		{
			return new Spearman(team, name, attack, defense, evasion, accuracy, movement, maximumHealth, maximumRange, minimumRange, ability, cost, UnitGenerator.id, game.settings.getFloat("cpuSpeed", 1.1f));
		} else if (type == UnitType.Archer)
		{
			return new Archer(team, name, attack, defense, evasion, accuracy, movement, maximumHealth, maximumRange, minimumRange, ability, cost, UnitGenerator.id, game.settings.getFloat("cpuSpeed", 1.1f));
		} else if (type == UnitType.Footman)
		{
			return new Footman(team, name, attack, defense, evasion, accuracy, movement, maximumHealth, maximumRange, minimumRange, ability, cost, UnitGenerator.id, game.settings.getFloat("cpuSpeed", 1.1f));
		} else if (type == UnitType.Knight)
		{
			return new Knight(team, name, attack, defense, evasion, accuracy, movement, maximumHealth, maximumRange, minimumRange, ability, cost, UnitGenerator.id, game.settings.getFloat("cpuSpeed", 1.1f));
		} else if (type == UnitType.Mage)
		{
			return new Mage(team, name, attack, defense, evasion, accuracy, movement, maximumHealth, maximumRange, minimumRange, ability, cost, UnitGenerator.id, game.settings.getFloat("cpuSpeed", 1.1f));
		} else
		{
			return new Priest(team, name, attack, defense, evasion, accuracy, movement, maximumHealth, maximumRange, minimumRange, ability, cost, UnitGenerator.id, game.settings.getFloat("cpuSpeed", 1.1f));
		}
	}

	private static String GenerateUnitName(List<Unit> roster) throws IOException
	{
		final XmlReader reader = new XmlReader();
		final Element xml = reader.parse(Gdx.files.internal("Names.xml"));
		final Array<Element> names = xml.getChildrenByName("name");
		final Random random = new Random();
		final int roll = random.nextInt(names.size);
		String name = names.get(roll).getText();
		while(UnitGenerator.IsNameTaken(roster, name)){
			name = names.get(roll).getText();
		}
		return names.get(roll).getText();
	}
	
	private static boolean IsNameTaken(List<Unit> roster, String name){
		for(Unit unit : roster){
			if(unit.name == name)
				return true;
		}
		return false;
	}

	private static UnitType GenerateUnitType()
	{
		final Random random = new Random();
		final int roll = random.nextInt(7);
		if (roll == 1)
		{
			return UnitType.Spearman;
		} else if (roll == 2)
		{
			return UnitType.Archer;
		} else if (roll == 3)
		{
			return UnitType.Footman;
		} else if (roll == 4)
		{
			return UnitType.Knight;
		} else if (roll == 5)
		{
			return UnitType.Mage;
		} else
		{
			return UnitType.Priest;
		}
	}
}
