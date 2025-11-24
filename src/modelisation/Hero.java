package modelisation;
import java.util.*;
import java.io.*;

class Hero {
    private final Backpack sac = new Backpack();
    //private final Hp hp = new Hp(40);
    //private final Mana mana = new Mana(0);
    //private final Exp exp = new Exp(0, 20);
    private Position position;
    private int hp=20;
    private int maxHp=20;
    private int mana=20;
    private int mqxMana=20;
    private int exp=0;
    private int expMax=10;

    private int energie = 3;
    private int niveau = 1;
    private int refusMalediction = 0;
    private int foisMalediction = 0;
    private boolean estProteger;

    public Hero() {
        position = new Position(0, new Coord(0,0));
        sac.initialiser15Cases();
    }
    
    public void damage(int dmg) { 
    	hp = Math.max(0, hp - dmg); 
    }

    public void heal(int val) { 
    	hp = Math.min(maxHp, hp + val); 
    }

    public boolean isDead() { 
    	return hp <= 0;
    }

    public boolean isAlive() { 
    	return hp > 0; 
    }
    
    public int current() { 
    	return hp; 
    }

    public int max() { 
    	return maxHp; 
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
    public void recharge() { 
    	mana = maxMana; 
    }

    public void use(int val) { 
    	mana = Math.max(0, mana - val); 
    }
    /*public void damage(int dmg) { 
    	hp.damage(dmg); 
    }

    public void heal(int v) { 
    	hp.heal(v); 
    }

    public void expUp(int v) { 
    	exp.add(v); 
    }*/

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
