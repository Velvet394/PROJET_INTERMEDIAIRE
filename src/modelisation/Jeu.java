package modelisation;
import java.util.*;
import java.io.*;

public class Jeu {
    private final Donjon donjon;
    private final Hero hero;
    private Combat combatActuel;

    public Jeu() {
        this.hero = new Hero();
        this.donjon = Generation.genererDonjon();
    }
    
    public static void jeuTermine() {
    	throw new RuntimeException("##############Jeu termine##############");
    }

    public void deplacerHero(Coord vers) {
    	
    }
    
    public void ouvrirSalle() {}
    
    public void lancerCombat(List<Ennemi> e) {
        combatActuel = new Combat(hero, e);
    }
}
