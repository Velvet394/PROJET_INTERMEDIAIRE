package modelisation;
import java.util.*;
import java.io.*;

public class Donjon {
    private final Map<Integer, Etape> etapes = new HashMap<>();
    private int etape=0;
    private Coord position=new Coord(0,0);
    public static final int maxEtape=5;
    
    public void moveEtape() {
    	if(etape==maxEtape) {
    		Jeu.jeuTermine();
    		return;
    	}
    	if(!etapes.containsKey(etape+1)) {Jeu.jeuTermine();}
    	etape++;
    }
    
    public Etape getetape() {
    	if(etape>=maxEtape) {
    		throw new IllegalArgumentException("etape error");
    	}
    	
    	return etapes.get(etape);
    }
    
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
