package bunzosteele.heroesemblem.model.Units;

import java.io.IOException;

import bunzosteele.heroesemblem.model.Units.Abilities.Ability;

public class Spearman extends Unit{
	public Spearman(int team, String name, int attack, int defense, int evasion, int accuracy, int movement, int maximumHealth, int maximumRange, int minimumRange, int ability, int cost, int id) throws IOException{		
		super(team, name, attack, defense, evasion, accuracy, movement, maximumHealth, maximumRange, minimumRange, cost, id);
		this.type = UnitType.Spearman;
		this.ability = new Ability();
	}
}
