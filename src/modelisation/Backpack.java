package modelisation;
import java.util.*;
import java.io.*;

class Backpack {
    private final Map<Coord, Item> contenu = new HashMap<>();
    private int width = 7;
    private int height = 5;

    public void initialiser15Cases() {
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 3; y++) {
                contenu.put(new Coord(x, y), null);
            }
        }
    }

    public boolean peutPlacer(Item item, Coord base) {
        for (Coord c : item.forme()) {
            Coord abs = new Coord(base.x() + c.x(), base.y() + c.y());
            if (!contenu.containsKey(abs)) return false;     // case inexistante
            if (contenu.get(abs) != null) return false;       // déjà occupée
        }
        return true;
    }

    public void placer(Item item, Coord base) {
    	if (!peutPlacer(item,base)) throw new IllegalArgumentException();
    	
        for (Coord c : item.forme()) {
            Coord abs = new Coord(base.x() + c.x(), base.y() + c.y());
            contenu.put(abs, item);
        }
    }
    
    public boolean peutPlacerMalediction(Item item) {
    	Objects.requireNonNull(item);
    	
    	if (item.estMalediction()) {
    		for (Coord c : item.forme()) {
    			if (!contenu.containsKey(c)) return false;
    			if (contenu.get(c) != null) return false;
    		}
    		
    		return true;
    	}
		return false;
    }
    
    public void ajouterMalediction(Item item) {
    	if (peutPlacerMalediction(item) == false) {
    		throw new IllegalArgumentException();
    	}
    	for (Coord c: item.forme()) {
    		contenu.putIfAbsent(c, item);
    	}
    }
    
    public void augmenterCapaciteBag() {
 	   width += 1;
    }

    public Map<Coord, Item> contenu() { 
    	return contenu; 
    }

    public void retirerItem(Item item) {
		for (Coord c : item.forme()) {
	        if (contenu.get(c) == item) {
	            contenu.put(c, null);  
	        }
	    }
	}
}
