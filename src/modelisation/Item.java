package modelisation;
import java.util.*;
import java.io.*;

public abstract class Item {
    private final String nom;
    private final List<Coord> forme;

    public Item(String nom, List<Coord> forme) {
        this.nom = nom;
        this.forme = forme;
    }

    public String nom() { return nom; }

    public List<Coord> forme() { return forme; }

    public abstract void utiliser(Hero hero, Combat combat);
    
    public boolean estMalediction() {
		return switch(nom) {
			case "Anneau de la bête" -> true;
			case "Anneau corrompu" -> true;
			case "Batterie Maudite" -> true;
			case "Main maléfique" -> true;
			default -> false;
		};
	}

	public boolean estConsommable() {
		// TODO Auto-generated method stub
		return false;
	}
}

