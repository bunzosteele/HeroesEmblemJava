package bunzosteele.heroesemblem.model.Units;

import java.io.IOException;

import bunzosteele.heroesemblem.model.Units.Abilities.Heal;

public class Priest extends Unit{
	public Priest(int team, String name, int attack, int defense, int evasion, int accuracy, int movement, int maximumHealth, int maximumRange, int minimumRange, int ability, int cost, int id) throws IOException{		
		super(team, name, attack, defense, evasion, accuracy, movement, maximumHealth, maximumRange, minimumRange, cost, id);
		this.type = UnitType.Priest;
		this.ability = new Heal();
	}
}
