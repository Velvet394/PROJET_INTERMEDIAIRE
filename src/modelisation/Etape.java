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
    	var res=new ArrayList<Room>();
    	sallesNeighborNonVisite.clear();
    	for(var i:salles.entrySet()) {
    		var room=i.getValue();
    		var set = i.getKey();
    		Optional.ofNullable(salles.getOrDefault(new Coord(set.x()+1, set.y()), null)).ifPresent(list::add);
    		Optional.ofNullable(salles.getOrDefault(new Coord(set.x()-1, set.y()), null)).ifPresent(list::add);
    		Optional.ofNullable(salles.getOrDefault(new Coord(set.x(), set.y()+1), null)).ifPresent(list::add);
    		Optional.ofNullable(salles.getOrDefault(new Coord(set.x(), set.y()-1), null)).ifPresent(list::add);
    		/*var resl=salles.getOrDefault(new Coord(set.x()-1, set.y()), null);
    		var resr=salles.getOrDefault(new Coord(set.x()+1, set.y()), null);
    		var resu=salles.getOrDefault(new Coord(set.x(), set.y()+1), null);
    		var resd=salles.getOrDefault(new Coord(set.x(), set.y()-1), null);*/
    	}
    	for(var i:list) {
    		if(!i.estVisite()) {
    			sallesNeighborNonVisite.add(i);
    		}
    	}
    }
    
    public void visite(Coord set) {
    	var res=salles.getOrDefault(set, null);
    	if(res==null) {
    		return;
    	}
    	if(sallesNeighborNonVisite.contains(res)) {
    		res.visiter();
    	}
    	return;
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
