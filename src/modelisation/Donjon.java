package modelisation;
import java.util.*;
import java.io.*;

public class Donjon {
    private final Map<Integer, Etape> etapes = new HashMap<>();
    private int etape=0;
    public static final int maxEtape=5;
    
    public void moveEtape() {
    	if(etape==maxEtape) {
    		Jeu.jeuTermine();
    	}
    	etape++;
    }
    
    public void moveSalle(Hero h,Coord c) {
    	var e=etapes.getOrDefault(etape, null);
    	if(e==null) {
    		throw new IllegalArgumentException("error etape dans Donjon");
    	}
    	var res=e.sallesNeighborNonVisite().getOrDefault(c, null);
    	if(res==null) {return;}
    	res.visiter(h);
    	e.RefreshListNeighbor();
    }

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
}
