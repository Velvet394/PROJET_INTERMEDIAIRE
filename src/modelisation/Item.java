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

	public boolean estConsommable() {
		// TODO Auto-generated method stub
		return false;
	}
}

