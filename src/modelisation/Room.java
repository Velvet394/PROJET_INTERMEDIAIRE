package modelisation;

import java.util.Objects;

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
    	if(estVisite()) {return;}
    	this.visite = true; 
    	enter.apply(null);
    }

	public Object getEnnemis() {
		// TODO Auto-generated method stub
		return null;
	}
}
/*
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
}*/