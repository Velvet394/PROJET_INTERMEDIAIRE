package modelisation;

import java.util.Objects;
import java.util.*;

public class Room {
    private RoomType type;
    private boolean visite;
    private final Enter enter;

    public Room(RoomType type,Enter e) {
    	Objects.requireNonNull(e);
    	Objects.requireNonNull(type);
        this.type = type;
        this.visite = false;
        this.enter=e;
    }
    
    public Room(Room r) {
    	Objects.requireNonNull(r);
    	type=r.type;
    	visite=false;
    	enter=r.enter;
    }

    public RoomType getType() { 
    	return type; 
    }

    public void setType(RoomType t) { 
    	this.type = t; 
    }
    
    public Enter getEnter() {
    	return enter;
    }

    public boolean estVisite() { 
    	return visite; 
    }

    public RoomType visiter(Hero h) { 
    	IO.println("Visite");
    	if(estVisite()) {return null;}
    	this.visite = true; 
    	var type=enter.apply(h);
    	
    	return type;
    }
    
    public boolean visite() {
    	return visite;
    }
    

}