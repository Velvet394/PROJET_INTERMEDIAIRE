package modelisation;

import java.util.List;
import java.util.Objects;

public class Weapon implements Item {
	private final String name;
	private final int cost;
	private final int mana;
	private final boolean estConsommable;
	private final List<Coord> forme;
	private final List<Effect> effects;
	private Coord offsetCoord=new Coord(0,0);
	
	public Weapon (String n, int c, int m, boolean b, List<Coord> f, List<Effect> e) {
		name=n;
		cost=c;
		mana=m;
		estConsommable=b;
		forme=f;
		effects=e;
	}
	
	 public String nom() { return name; }

	 @Override
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
		 
		 if(!hero.ifuse(mana)) {
			 return;
		 }
		 hero.use(mana);
		 
		 for(var i:effects) {
			 i.apply(hero, ennemi,combat);
		 }
		 
		 if(estConsommable) {
			 hero.getBackpack().contenu().entrySet().removeIf(entry -> entry.getValue()==this);
		 }
	 }
	 
	 
	 
	 @Override
	 public boolean equals(Object o) {
	     
	     return o instanceof Weapon weapon &&
	    		cost == weapon.cost &&
	            mana == weapon.mana &&
	            estConsommable == weapon.estConsommable &&
	            name.equals(weapon.name) &&
	            forme.equals(weapon.forme) &&
	            Objects.equals(effects, weapon.effects) &&
	            Objects.equals(offsetCoord, weapon.offsetCoord);
	 }

	 @Override
	 public int hashCode() {
	     return Objects.hash(name, cost, mana, estConsommable, forme, effects, offsetCoord);
	 }
	 
	 @Override
	public Coord offsetCoord() {
		// TODO Auto-generated method stub
		return offsetCoord;
	}
	 
	    
}
