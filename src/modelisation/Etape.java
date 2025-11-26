package modelisation;
import java.util.*;
import java.io.*;

public class Etape {
    private final Map<Coord, Room> salles;
    private final List<Room> sallesNeighborNonVisite;
    
    public Etape() {
    	salles = new HashMap<>();
    	sallesNeighborNonVisite=new ArrayList<>();
    }
    
    public Etape(Map<Coord, Room> s) {
    	Objects.requireNonNull(s);
		salles = new HashMap<>(s);
		sallesNeighborNonVisite=new ArrayList<>();
	}
    
    public void RefreshListNeighbor() {
    	var list=new ArrayList<Room>();
    	sallesNeighborNonVisite.clear();
    	for(var i:salles.entrySet()) {
    		var room=i.getValue();
    		
    	}
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
