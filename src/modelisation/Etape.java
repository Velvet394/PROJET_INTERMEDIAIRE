package modelisation;
import java.util.*;
import java.io.*;

/**
 * Represents a single floor (etape) of the dungeon.
 * It stores all rooms on the floor and keeps track of
 * neighbor rooms that are not visited yet.
 */
public class Etape {
	/** All rooms of the floor indexed by their coordinates. */
    private final Map<Coord, Room> salles;
    
    /**
     * Rooms that are neighbours of at least one visited room
     * and are not visited yet.
     */
    private final Map<Coord,Room> sallesNeighborNonVisite;
    
    public Etape() {
    	salles = new HashMap<>(Generation.genererEtape());
    	var res=salles.get(new Coord(0, 0));
    	sallesNeighborNonVisite=new HashMap<>();
    	sallesNeighborNonVisite.put(new Coord(0, 0),res);
    }
    
    public Etape(Map<Coord, Room> s) {
    	Objects.requireNonNull(s);
		salles = new HashMap<>(s);
		var res=salles.get(new Coord(0, 0));
		sallesNeighborNonVisite=new HashMap<>();
    	sallesNeighborNonVisite.put(new Coord(0, 0),res);
	}
    
    /**
     * Rebuilds the map of non-visited neighbour rooms.
     * A room is considered a neighbour if it is adjacent to
     * at least one visited room and has not been visited yet.
     */
    public void RefreshListNeighbor() {
    	var list=new HashMap<Coord,Room>();
    	var res=new ArrayList<Room>();
    	//for(var i:salles.entrySet())
    	for(var i:salles.entrySet()) {
    		var room=i.getValue();
    		var set = i.getKey();
    		if(room.visite()) {
    			var l=new ArrayList<Coord>();
				l.add(new Coord(set.x()+1, set.y()));
				l.add(new Coord(set.x()-1, set.y()));
				l.add(new Coord(set.x(), set.y()+1));
				l.add(new Coord(set.x(), set.y()-1));
				
				for(var j:l) {
	    			var r=salles.getOrDefault(j, null);
	    			if(r!=null && !r.visite()) {
	    				list.put(j, r);
	    			}
	    		}
    		}

    		/*
    		Optional.ofNullable(salles.getOrDefault(new Coord(set.x()+1, set.y()), null)).ifPresent(list::add);
    		Optional.ofNullable(salles.getOrDefault(new Coord(set.x()-1, set.y()), null)).ifPresent(list::add);
    		Optional.ofNullable(salles.getOrDefault(new Coord(set.x(), set.y()+1), null)).ifPresent(list::add);
    		Optional.ofNullable(salles.getOrDefault(new Coord(set.x(), set.y()-1), null)).ifPresent(list::add);
    		*/
    		/*var resl=salles.getOrDefault(new Coord(set.x()-1, set.y()), null);
    		var resr=salles.getOrDefault(new Coord(set.x()+1, set.y()), null);
    		var resu=salles.getOrDefault(new Coord(set.x(), set.y()+1), null);
    		var resd=salles.getOrDefault(new Coord(set.x(), set.y()-1), null);*/
    	}
    	sallesNeighborNonVisite.clear();
    	for(var i:list.entrySet()) {
    		if(!i.getValue().estVisite()) {
    			sallesNeighborNonVisite.put(i.getKey(),i.getValue());
    		}
    	}
    }
   
//    public void RefreshListNeighbor() {
//    	var list=new HashMap<Coord,Room>();
//    	var res=new ArrayList<Room>();
//   	//for(var i:salles.entrySet())
//  	for(var i:sallesNeighborNonVisite.entrySet()) {
//    		var room=i.getValue();
//    		var set = i.getKey();
//    		var l=new ArrayList<Coord>();
//    				l.add(new Coord(set.x()+1, set.y()));
//    				l.add(new Coord(set.x()-1, set.y()));
//   				l.add(new Coord(set.x(), set.y()+1));
//    				l.add(new Coord(set.x(), set.y()-1));
//
//    		for(var j:l) {
//    			var r=salles.getOrDefault(j, null);
//    			if(r!=null) {
//    				list.put(j, r);
//    			}
//    		}
//    		/*
//    		Optional.ofNullable(salles.getOrDefault(new Coord(set.x()+1, set.y()), null)).ifPresent(list::add);
//    		Optional.ofNullable(salles.getOrDefault(new Coord(set.x()-1, set.y()), null)).ifPresent(list::add);
//    		Optional.ofNullable(salles.getOrDefault(new Coord(set.x(), set.y()+1), null)).ifPresent(list::add);
//    		Optional.ofNullable(salles.getOrDefault(new Coord(set.x(), set.y()-1), null)).ifPresent(list::add);
//    		*/
//    		/*var resl=salles.getOrDefault(new Coord(set.x()-1, set.y()), null);
//    		var resr=salles.getOrDefault(new Coord(set.x()+1, set.y()), null);
//    		var resu=salles.getOrDefault(new Coord(set.x(), set.y()+1), null);
//    		var resd=salles.getOrDefault(new Coord(set.x(), set.y()-1), null);*/
//    	}
//    	sallesNeighborNonVisite.clear();
//    	for(var i:list.entrySet()) {
//    		if(!i.getValue().estVisite()) {
//    			sallesNeighborNonVisite.put(i.getKey(),i.getValue());
//    		}
//    	}
//    }
    
    /**
     * Visits a room at a given coordinate with the provided hero,
     * only if that room is currently in the neighbour list.
     */
    public RoomType visite(Coord set,Hero h) {
    	var res=salles.getOrDefault(set, null);
    	if(res==null) {
    		return null;
    	}
    	var r=sallesNeighborNonVisite.getOrDefault(set, null);
    	if(/*sallesNeighborNonVisite.contains(res)*/ r!=null) {
    		//res.visiter();
    		var type=r.visiter(h);
    		return type;
    	}
    	return null;
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
    
    public Map<Coord, Room> sallesNeighborNonVisite() {
    	return sallesNeighborNonVisite;
    }
}
