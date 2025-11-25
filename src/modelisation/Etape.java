package modelisation;
import java.util.*;
import java.io.*;

public class Etape {
    private final Map<Coord, Room> salles;
    
    public Etape() {
    	salles = new HashMap<>();
    }
    
    public Etape(Map<Coord, Room> s) {
		salles = new HashMap<>(s);
	}
    
    public void ajouterSalle(Coord c, Room s) {
        salles.put(c, s);
    }

    public Room getSalle(Coord c) {
        return salles.get(c);
    }
    
    public Map<Coord, Room> getSalles() {
    	return salles;
    }
}
