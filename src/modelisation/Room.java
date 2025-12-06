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

    public void visiter(Hero h) { 
    	IO.println("Visite");
    	if(estVisite()) {return;}
    	this.visite = true; 
    	enter.apply(h);
    }
    
    public boolean visite() {
    	return visite;
    }
<<<<<<< HEAD

    // INUTILE
=======
/*
>>>>>>> branch 'master' of https://github.com/Velvet394/PROJET_INTERMEDIAIRE.git
    public List<Ennemi> getEnnemis() {
        if (enter instanceof Enter_Combat) {
            // L'Enter_Combat génère les ennemis lors de l'entrée dans la salle
            return ((Enter_Combat) enter).genererEnnemis();
        }
        return new ArrayList<>();  // Retourner une liste vide si la salle n'est pas un combat
<<<<<<< HEAD
    }
}
=======
    }*/
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
>>>>>>> branch 'master' of https://github.com/Velvet394/PROJET_INTERMEDIAIRE.git
