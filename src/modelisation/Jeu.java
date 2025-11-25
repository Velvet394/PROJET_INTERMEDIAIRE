package modelisation;
import java.util.*;
import java.io.*;

public class Jeu {
    private final Donjon donjon;
    private final Hero hero;
    private Combat combatActuel;

    public Jeu() {
        this.hero = new Hero();
        this.donjon = new Donjon();
    }
    
    public static void jeuTermine() {}

    public void deplacerHero(Coord vers) {
    	
    }
    
    public void ouvrirSalle() {}
    
    public void lancerCombat(List<Ennemi> e) {
        combatActuel = new Combat(hero, e);
    }
}
