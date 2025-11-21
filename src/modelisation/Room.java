package modelisation;

public class Room {
    private RoomType type;
    private boolean visite;

    public Room(RoomType type) {
        this.type = type;
        this.visite = false;
    }

    public RoomType getType() { 
    	return type; 
    }

    public void setType(RoomType t) { 
    	this.type = t; 
    }

    public boolean estVisite() { 
    	return visite; 
    }

    public void visiter() { 
    	this.visite = true; 
    }
}