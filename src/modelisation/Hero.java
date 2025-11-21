package modelisation;
import java.util.*;
import java.io.*;

class Hero {
    private final Backpack sac = new Backpack();
    private final Hp hp = new Hp(40);
    private final Mana mana = new Mana(0);
    private final Exp exp = new Exp(0, 20);
    private Position position;

    private int energie = 3;
    private int niveau = 1;
    private int refusMalediction = 0;
    private int foisMalediction = 0;
    private boolean estProteger;

    public Hero() {
        position = new Position(0, new Coord(0,0));
        sac.initialiser15Cases();
    }
    
    public void activateProtection() {
    	estProteger = true;
    }

    public void desactivateProtection() {
    	estProteger = false;
    }
    
    public boolean estProteger() {
    	return estProteger;
    }
    
    public void damage(int dmg) { 
    	hp.damage(dmg); 
    }

    public void heal(int v) { 
    	hp.heal(v); 
    }

    public void expUp(int v) { 
    	exp.add(v); 
    }

    public void rechargerCombat() {
        mana.recharge();
        energie = 3;
    }
    
    public void addMalediction(int n) {
    	foisMalediction += n;
    	IO.println("Le hero a subi " + foisMalediction + " de malédictions.");
    }

    public Backpack sac() { 
    	return sac; 
    }
    
    public Backpack getBackpack() { 
    	return sac; 
    }

    public void ajouterDansSac(Item item) {
        // TODO: gestion rotation, placement
    }
}
