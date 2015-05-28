package bunzosteele.heroesemblem.model.Units;

import java.util.ArrayList;
import java.util.List;

public final class UnitGenerator {
	public static List<Unit> GenerateStock(){
		List<Unit> stock = new ArrayList<Unit>();
		stock.add(new Unit());
		stock.add(new Unit());
		stock.add(new Unit());
		stock.add(new Unit());
		stock.add(new Unit());
		stock.add(new Unit());
		stock.add(new Unit());
		stock.add(new Unit());
		return stock;
	}
}
