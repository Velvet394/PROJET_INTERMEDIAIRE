package modelisation;

import java.util.ArrayList;


import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Represents a weapon or usable item placed inside the backpack.
 * A weapon has a shape, a cost, optional mana consumption and applies effects.
 */
public class Weapon implements Item {
	private final String name;
	private final int cost;
	private final int mana;
	private final boolean estConsommable;
	private final List<Coord> forme;
	private final List<Effect> effects;
	private Coord offsetCoord=new Coord(0,0);
	
	private int rotationQuarterTurns = 0;
	
	private Image image;
	
	BiConsumer<Hero,Integer> interaction;
	
	public Weapon (String n, int c, int m, boolean b, List<Coord> f, List<Effect> e,String imagePath,BiConsumer<Hero,Integer> inter) {
		Objects.requireNonNull(f);
		Objects.requireNonNull(e);
		name=n;
		cost=c;
		mana=m;
		estConsommable=b;
		forme=new ArrayList<>(Objects.requireNonNull(f));
		effects=new ArrayList<>(Objects.requireNonNull(e));
		interaction=inter;
		
		 try {
	            image = ImageIO.read(new File(imagePath));
	        } catch (IOException ex) {
	            ex.printStackTrace();
	            image = null; 
	        }
	}
	
	public Weapon(Weapon w) {
		Objects.requireNonNull(w);
		name=w.name;
		cost=w.cost;
		mana=w.mana;
		estConsommable=w.estConsommable;
		forme=new ArrayList<>(Objects.requireNonNull(w.forme));
		effects=new ArrayList<>(Objects.requireNonNull(w.effects));
		interaction=w.interaction;
		image=w.image;
		rotationQuarterTurns=w.rotationQuarterTurns;
	}
	
	 public String nom() { return name; }

	 @Override
	 public List<Coord> forme() { return forme; }
	 
	 public List<Effect> effects(){
		 return effects;
	 }
	 /**
	     * Use of the weapon in a combat
	 */
	 public void utiliser(Hero hero, Ennemi ennemi, Combat combat) {
		 Objects.requireNonNull(hero);
		 Objects.requireNonNull(ennemi);
		 Objects.requireNonNull(combat);
		 if(!hero.ifcost(cost)) {
			 return;
		 }
		 
		 if(!hero.ifuse(mana)) {
			 return;
		 }
		 
		 hero.cost(cost);
		 hero.use(mana);
		 
		 IO.println("USE Weapon");
		 
		 for(var i:effects) {
			 i.apply(hero, ennemi,combat);
		 }
		 
		 if(estConsommable) {
			 //hero.getBackpack().contenu().entrySet().removeIf(entry -> entry.getValue()==this);
			 for(var i:hero.getBackpack().contenu().entrySet()) {
				 if(i.getValue()==this) {
					 i.setValue(null);
				 }
			 }
		 }
	 }
	 
	 /**
	  * Rotation of the hero backpack
	  * */
	 /*
	 public void rotation() {
		 var list=new ArrayList<Coord>();
		 for(var i:forme) {
			 list.add(new Coord(i.y(),-i.x()));
		 }
		 forme.clear();
		 forme.addAll(list);
	 }*/
	 public void rotation() {
		  var list = new ArrayList<Coord>();
		  for (var c : forme) {
		    list.add(new Coord(c.y(), -c.x()));
		  }
		  forme.clear();
		  forme.addAll(list);

		  // normalize: shift so minX/minY become 0
		  int minX = 0, minY = 0;
		  for (var c : forme) {
		    minX = Math.min(minX, c.x());
		    minY = Math.min(minY, c.y());
		  }
		  if (minX < 0 || minY < 0) {
		    var normalized = new ArrayList<Coord>(forme.size());
		    for (var c : forme) {
		      normalized.add(new Coord(c.x() - minX, c.y() - minY));
		    }
		    forme.clear();
		    forme.addAll(normalized);
		  }

		  rotationQuarterTurns = (rotationQuarterTurns + 1) & 3;
		}
	 
	 /**
	  * Translates the current position to the new position
	  * */
	 public void translate(Coord offset) {
		 Objects.requireNonNull(offset);
		 offsetCoord=offset;
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
	 
	 public String name() {
		 return name;
	 }
	 
	 public Image image() {
	        return image;
	 }
	 
	 @Override
	 public int manaCapacity() {
	     return mana;
	 }
	 
	 
	 public void interaction(Hero h,Integer i) {
		 interaction.accept(h,i);
		 return;
	 }
	 
	 
	 public void setRotationQuarterTurns(int t) {
		  rotationQuarterTurns = t & 3;
		}
	 public int rotationQuarterTurns() { return rotationQuarterTurns; }
}
