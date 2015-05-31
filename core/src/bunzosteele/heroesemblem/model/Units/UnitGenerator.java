package bunzosteele.heroesemblem.model.Units;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public final class UnitGenerator {
	private static int id = 0;
	
	public static List<Unit> GenerateStock() throws IOException{
		List<Unit> stock = new ArrayList<Unit>();
		while(stock.size() < 8){
			UnitType unitType = GenerateUnitType();
			stock.add(GenerateUnit(0, unitType));
		}
		return stock;
	}
	
	public static Unit GenerateUnit(int team, UnitType type) throws IOException{
		UnitGenerator.id++;
		int costModifier = 0;
		int attackBonus = GenerateAttackBonus();
		costModifier += attackBonus * 50;
		int defenseBonus = GenerateDefenseBonus();
		costModifier += defenseBonus * 50;
		int evasionBonus = GenerateEvasionBonus();
		costModifier += evasionBonus * 10;
		int accuracyBonus = GenerateAccuracyBonus();
		costModifier += accuracyBonus * 5;
		int movementBonus = GenerateMovementBonus();
		costModifier += movementBonus * 200;
		int healthBonus = GenerateHealthBonus();
		costModifier += healthBonus * 25;
		int ability = GenerateAbility();
		costModifier += ability * 200;
		if (ability == 0){
			costModifier -= 200;
		}
				
		XmlReader reader = new XmlReader();
		Element xml = reader.parse(Gdx.files.internal("UnitStats.xml"));
		Element unitStats = xml.getChildByName(type.toString());
		int attack = unitStats.getInt("Attack") + attackBonus;
		int defense = unitStats.getInt("Defense") + defenseBonus;
		int evasion = unitStats.getInt("Evasion") + evasionBonus;
		int accuracy = unitStats.getInt("Accuracy") + accuracyBonus;
		int movement = unitStats.getInt("Movement") + movementBonus;
		int maximumHealth = unitStats.getInt("MaximumHealth") + healthBonus;
		int maximumRange = unitStats.getInt("MaximumRange");
		int minimumRange = unitStats.getInt("MinimumRange");
		int cost = unitStats.getInt("Cost") + costModifier;
		
		String name = GenerateUnitName();
		
		if(type == UnitType.Spearman){
			return new Spearman(team, name, attack, defense, evasion, accuracy, movement, maximumHealth, maximumRange, minimumRange, ability, cost, id);
		}else if(type == UnitType.Archer){
			return new Archer(team, name, attack, defense, evasion, accuracy, movement, maximumHealth, maximumRange, minimumRange, ability, cost, id);
		}else if(type == UnitType.Footman){
			return new Footman(team, name, attack, defense, evasion, accuracy, movement, maximumHealth, maximumRange, minimumRange, ability, cost, id);
		}else if(type == UnitType.Knight){
			return new Knight(team, name, attack, defense, evasion, accuracy, movement, maximumHealth, maximumRange, minimumRange, ability, cost, id);
		}else if(type == UnitType.Mage){
			return new Mage(team, name, attack, defense, evasion, accuracy, movement, maximumHealth, maximumRange, minimumRange, ability, cost, id);
		}else{
			return new Priest(team, name, attack, defense, evasion, accuracy, movement, maximumHealth, maximumRange, minimumRange, ability, cost, id);
		}
	}
	
	private static UnitType GenerateUnitType(){
		Random random = new Random();
		int roll = random.nextInt(7);
		if(roll == 1){
			return UnitType.Spearman;
		}else if(roll == 2){
			return UnitType.Archer;
		}else if(roll == 3){
			return UnitType.Footman;
		}else if(roll == 4){
			return UnitType.Knight;
		}else if(roll == 5){
			return UnitType.Mage;
		}else{
			return UnitType.Priest;
		}
	}
	
	private static int GenerateAttackBonus(){
		Random random = new Random();
		int roll = random.nextInt(101);
		
		if(roll > 99)
			return 3;
		if(roll > 90)
			return 2;
		if(roll > 80)
			return 2;
		if(roll > 70)
			return 1;
		if(roll < 30)
			return -1;
		if(roll < 10)
			return -2;
		return 0;
	}
	
	private static int GenerateDefenseBonus(){
		Random random = new Random();
		int roll = random.nextInt(101);
		
		if(roll > 99)
			return 3;
		if(roll > 90)
			return 2;
		if(roll > 80)
			return 2;
		if(roll > 70)
			return 1;
		if(roll < 30)
			return -1;
		if(roll < 10)
			return -2;
		return 0;
	}
	
	private static int GenerateEvasionBonus(){
		Random random = new Random();
		int roll = random.nextInt(11) -5;
		return roll;
	}
	
	private static int GenerateAccuracyBonus(){
		Random random = new Random();
		int roll = random.nextInt(21) -1;
		return roll;
	}
	
	private static int GenerateMovementBonus(){
		Random random = new Random();
		int roll = random.nextInt(101);
		
		if(roll > 90)
			return 1;
		if(roll < 10)
			return -1;
		return 0;
	}
	
	private static int GenerateAbility(){
		Random random = new Random();
		int roll = random.nextInt(101);
		
		if(roll <= 50)
			return 0;
		if(roll <= 80)
			return 1;
		return 2;
	}
	
	private static int GenerateHealthBonus(){
		Random random = new Random();
		int roll = random.nextInt(101);
		
		if(roll > 99)
			return 5;
		if(roll > 95)
			return 5;
		if(roll > 90)
			return 3;
		if(roll > 80)
			return 2;
		if(roll > 70)
			return 1;
		if(roll < 30)
			return -1;
		if(roll < 20)
			return -2;
		if(roll < 10)
			return -3;
		if(roll < 5)
			return -4;
		if(roll < 1)
			return -5;
		return 0;
	}
	
	private static String GenerateUnitName() throws IOException{
		XmlReader reader = new XmlReader();
		Element xml = reader.parse(Gdx.files.internal("Names.xml"));
		Array<Element> names = xml.getChildrenByName("name");
		Random random = new Random();
		int roll = random.nextInt(names.size);
		return names.get(roll).getText();
	}
}
