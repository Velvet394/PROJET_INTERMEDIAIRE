package modelisation;

import java.util.ArrayList;
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
		Objects.requireNonNull(f);
		Objects.requireNonNull(e);
		name=n;
		cost=c;
		mana=m;
		estConsommable=b;
		forme=new ArrayList<>(Objects.requireNonNull(f));
		effects=new ArrayList<>(Objects.requireNonNull(e));
	}
	
	public Weapon(Weapon w) {
		Objects.requireNonNull(w);
		name=w.name;
		cost=w.cost;
		mana=w.mana;
		estConsommable=w.estConsommable;
		forme=new ArrayList<>(Objects.requireNonNull(w.forme));
		effects=new ArrayList<>(Objects.requireNonNull(w.effects));
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
	 
	 public void rotation() {
		 var list=new ArrayList<Coord>();
		 for(var i:forme) {
			 list.add(new Coord(i.y(),-i.x()));
		 }
		 forme.clear();
		 forme.addAll(list);
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
	 
	 /*
	 @Override
	public void Item(Item i) {
		// TODO Auto-generated method stub
		
	}*/
	 
	    
}
