package modelisation;

import java.util.Objects;
import java.util.*;

/**
 * Represents a room in a dungeon floor.
 * A room has a type, a visited state and an enter behavior.
 */
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

    /**
     * Marks the room as visited and applies its enter behavior to the hero.
     * If the room was already visited, returns  null.
     */
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