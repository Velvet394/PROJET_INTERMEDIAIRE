package modelisation;

import java.util.List;
/**
 * Base interface for all items that can be stored in the backpack.
 */
public interface Item {
	List<Coord> forme();
	/**
	   * Returns the current offset (top-left absolute coordinate) in the backpack.
	   *
	 */
	Coord offsetCoord();
	//void Item(Item i);
	String nom();
	void rotation();
	void translate(Coord offset);
	/**
	   * Returns the mana capacity provided by this item when
	   * it is present in the backpack.
	   * Default implementation returns 0.
	*/
	default int manaCapacity() {
        return 0;
    }
}


/*package modelisation;
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
*/
