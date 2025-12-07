package modelisation;
import java.util.*;
import java.io.*;

/**
 * Represents the hero's backpack.
 * It stores items arranged on a grid and handles placement rules.
 */

public class Backpack {
	/** Content of the BackPack*/
    private final Map<Coord, Item> contenu;
    private int width = 7;
    private int height = 5;
    
    /**
     * Creates the initial 5×3 backpack with empty cells.
     */
    public Backpack() {
    	contenu = new HashMap<>();
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 3; y++) {
                contenu.put(new Coord(x, y), null);
            }
        }
    }
    /**
     * Adds an empty cell to the backpack if the coordinate is valid.
     */
    public void allouerCase(Coord c) {
    	if(c.x()>7||c.x()<0) {return;}
    	if(c.y()>5||c.y()<0) {return;}
    	if(contenu.containsKey(c)) {return;}
    	contenu.put(c,null);
    }
    
    /**
     * Checks if an item 
     */
    public boolean peutPlacer(Item item) {
        for (Coord c : item.forme()) {
            Coord abs = new Coord(item.offsetCoord().x() + c.x(), item.offsetCoord().y() + c.y());
            if (!contenu.containsKey(abs)) return false;     // case inexistante
            if (contenu.get(abs) != null) return false;       // déjà occupée
        }
        return true;
    }

    /**
     * Places an item if the placement is valid.
     */
    public void placer(Item item) {
    	if (!peutPlacer(item)) return;
    	
        for (Coord c : item.forme()) {
            Coord abs = new Coord(item.offsetCoord().x() + c.x(), item.offsetCoord().y() + c.y());
            contenu.put(abs, item);
        }
    }
    
    //hero.getBackpack().contenu().entrySet().removeIf(entry -> entry.getValue()==this);
    /**
     * Places a curse item, removing displaced items.
     */
    public void PlacerMalediction(Item item) {
    	Objects.requireNonNull(item);
    	if(!peutPlacerMalediction(item)) {
    		throw new IllegalArgumentException("error");
    	}
    	ArrayList<Item> list=new ArrayList<>();
    	//if (item.estMalediction()) {
    		for (Coord c : item.forme()) {
    			Coord abs = new Coord(item.offsetCoord().x() + c.x(), item.offsetCoord().y() + c.y());
    			if (contenu.get(abs) != null) {
    				var res=contenu.get(abs);
    				if (!list.contains(res)) {
    					list.add(contenu.get(abs));
    				}
    			}
    		}
    		for(var i:list) {
    			//contenu().entrySet().removeIf(entry -> entry.getValue()==i);
    			for(var e:contenu.entrySet()) {
   				 if(e.getValue()==i) {
   					 e.setValue(item);
   				 }
   			 }
    		}
    		
    	//}
    }
    
    /**
     * Checks if a curse can be placed.
     */

    public boolean peutPlacerMalediction(Item item) {
    	Objects.requireNonNull(item);
    	//if (item.estMalediction()) {
    		for (Coord c : item.forme()) {
    			Coord abs = new Coord(item.offsetCoord().x() + c.x(), item.offsetCoord().y() + c.y());
    			if (!contenu.containsKey(abs)) return false;
    		}
    		
    		return true;
    	//}
    }
    /*
    public void ajouterMalediction(Item item) {
    	if (peutPlacerMalediction(item) == false) {
    		throw new IllegalArgumentException();
    	}
    	for (Coord c: item.forme()) {
    		contenu.putIfAbsent(c, item);
    	}
    }
    */
    public void augmenterCapaciteBag() {
 	   width += 1;
    }

    public Map<Coord, Item> contenu() { 
    	return contenu; 
    }
/*
    public void retirerItem(Item item) {
		for (Coord c : item.forme()) {
	        if (contenu.get(c) == item) {
	            contenu.put(c, null);  
	        }
	    }
	}
    */
    public Argent getArgent() {
    	 var res=contenu.entrySet().stream()
    			.map(e->e.getValue())
    			.filter(e->e instanceof Argent)
    			.findFirst()
    			.orElse(new Argent(0,new Coord(0,0)));
    	if(res instanceof Argent a) {
    		return a;
    	}
    	throw new IllegalArgumentException("getArgent gagne un valeur exception");
    }
    
    public Or getOr() {
    	var res=contenu.entrySet().stream()
    			.map(e->e.getValue())
    			.filter(e->e instanceof Or)
    			.findFirst()
    			.orElse(new Argent(0,new Coord(0,0)));
    	if(res instanceof Or a) {
    		return a;
    	}
    	throw new IllegalArgumentException("getOr gagne un valeur exception");
    }
    
    public Gold getGold() {
   	 var res=contenu.entrySet().stream()
   			.map(e->e.getValue())
   			.filter(e->e instanceof Gold)
   			.findFirst()
   			.orElse(new Gold(0));
   	if(res instanceof Gold a) {
   		return a;
   	}
   	throw new IllegalArgumentException("getGold gagne un valeur exception");
   }
    /**
     * Removes depleted Gold items.
     */
    public void RefreshMonnaie() {
    	for(var i:contenu.entrySet()) {
    		if(i.getValue() instanceof Gold g) {
    			if(g.num()<=0) {
    				contenu.put(i.getKey(), null);
    			}
    		}
    	}
    }
    
    public Item getItemAt(Coord coord) {
        return contenu.get(coord);
    }

    
    public void use(Or useor, Argent usear) {
    	Objects.requireNonNull(useor);
    	Objects.requireNonNull(usear);
    	var ar=getArgent();
    	var or=getOr();
    	if(ar.num()>=usear.num()) {
    		// baned
    	}
    	
    }
    
    public void use(Gold use) {
		Objects.requireNonNull(use);
		var g=getGold();
		if(!g.peutUse(use)) {return;}
		g.use(use);
		RefreshMonnaie();
	}
}
