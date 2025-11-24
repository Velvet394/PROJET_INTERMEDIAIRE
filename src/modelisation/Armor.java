package modelisation;

import java.util.List;

public class Armor extends Item{
	private final Rarity rar;
    private final int cost;
    
	public Armor(String nom, List<Coord> forme, Rarity rar, int cost) {
		 super("Épée", List.of(
	                new Coord(0,0)   // 1 case
	     ));
		 
		 this.rar = rar;
		 this.cost = cost;
	}

	@Override
	public void utiliser(Hero hero, Combat combat) {
	}
}