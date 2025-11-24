package modelisation;

import java.util.List;
import java.util.Objects;

public class Weapon {
	private final String name;
	private final int cost;
	private final boolean estConsommable;
	private final List<Coord> forme;
	private final List<Effect> effects;
	private int offsetX=0;
	private int offsetY=0;
	
	public Weapon (String n, int c, boolean b, List<Coord> f, List<Effect> e) {
		name=n;
		cost=c;
		estConsommable=b;
		forme=f;
		effects=e;
	}
	
	 public String nom() { return name; }

	 public List<Coord> forme() { return forme; }
	 
	 public List<Effect> effects(){
		 return effects;
	 }
	 
	 public void utiliser(Hero hero, Ennemi ennemi, Combat combat) {
		 Objects.requireNonNull(hero);
		 Objects.requireNonNull(ennemi);
		 Objects.requireNonNull(combat);
		 if(!hero.ifcost(cost)) {
			 return;
		 }
		 hero.cost(cost);
		 for(var i:effects) {
			 i.apply(hero, ennemi,combat);
		 }
	 }
	 
	    
}
