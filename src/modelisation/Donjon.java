package modelisation;
import java.util.*;
import java.io.*;
/**
 * Represents the dungeon that the Hero will exxplore 
 * */

public class Donjon {
	
	/** The floors in the Dungeon and each floor is represented by a number */
    private final Map<Integer, Etape> etapes = new HashMap<>();
    private int etape=0;
    private Coord position=new Coord(0,0);
    
    public static final int maxEtape=5;
    
    /**
     * Updates the current floor in the Dungeon 
     * */
    public int moveEtape() {
    	if(etape>=maxEtape) {
    		//Jeu.jeuTermine();
    		return 3;
    	}
    	if(!etapes.containsKey(etape+1)) {
    		//Jeu.jeuTermine();
    		return 3;
    		}
    	etape++;
    	return 1;
    }
    
    /**
     * Gets the current floor of the dungeon
     * */
    public Etape getetape() {
    	if(etape>maxEtape) {
    		throw new IllegalArgumentException("etape error");
    	}
    	
    	return etapes.get(etape);
    }
    
    /**
     * Represents the movement of the hero in the dungeon
     * */
    public RoomType moveSalle(Hero h,Coord c) {
    	var e=etapes.getOrDefault(etape, null);
    	if(e==null) {
    		throw new IllegalArgumentException("error etape dans Donjon");
    	}
    	var res=e.sallesNeighborNonVisite().getOrDefault(c, null);
    	if(res==null) {return null;}
    	var type=res.visiter(h);
    	position=c;
    	e.RefreshListNeighbor();
    	return type;
    }

    /**
     * Adds a new floor to the Dungeon  
     * */
    public void ajouterEtape(int num, Etape e) {
    	if(num>maxEtape) {
    		throw new IllegalArgumentException("error de ajouteretape");
    	}
        etapes.put(num, e);
    }
    
    public Etape getEtape(int num) { return etapes.get(num); }
    
    public Map<Integer, Etape> etapes() {
    	return etapes;
    }
    
    public int etape() {
    	return etape;
    }
}
