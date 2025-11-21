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

    public Map<Coord, Item> contenu() { 
    	return contenu; 
    }

	public void retirerItem(Potion potion) {
		// TODO Auto-generated method stub
	}
}
