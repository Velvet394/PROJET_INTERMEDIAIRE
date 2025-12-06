package modelisation;
import java.util.*;
import java.io.*;

public class Hero {
    private final Backpack sac;
    //private final Hp hp = new Hp(40);
    //private final Mana mana = new Mana(0);
    //private final Exp exp = new Exp(0, 20);
    //private Position position;
    private int hp=40;
    private int maxHp=40;
    private int mana=200;
    private int maxMana=200;
    private int exp=0;
    private int expMax=10;
    private int block=0;

    private int energie = 3;
    private int niveau = 1;
    private int refusMalediction = 0;
    private int foisMalediction = 0;
    //private boolean estProteger;
    private int or = 0;
    
    public void testLevelUp() {
    	if(exp>=expMax) {
    		niveau++;
    		var r=Dice.roll(1, 3);
    		for(int i=0;i<r;i++) {
    			sac.allouerCase(null);
    		}
    		exp=0;
    		expMax=(niveau+niveau)*10;
    	}
    }

    public Hero() {
    	sac=new Backpack();
        //position = new Position(0, new Coord(0,0));
        //sac.initialiser15Cases();
    }
    
    public void damage(int dmg) {
    	var res=block-dmg;
    	if(res>0) {
    		this.block=res;
    		return;
    	}
    	hp = Math.max(0, hp + block - dmg);
    	block = 0;
    	return;
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
    
    /*
    public int current() { 
    	return hp; 
    }*/
/*
    public int max() { 
    	return maxHp; 
    }*/
    /*
    public void activateProtection() {
    	estProteger = true;
    }

    public void desactivateProtection() {
    	estProteger = false;
    }
    
    public boolean estProteger() {
    	return estProteger;
    }
    */
    public void recharge() { 
    	mana = maxMana; 
    }
    
    public boolean ifuse(int cost) {
    	if(mana<cost) {
    		return false;
    	}
    	return true;
    }

    public void use(int val) { 
    	mana = Math.max(0, mana - val); 
    }
    
    public boolean ifcost(int cost) {
    	if(energie<cost) {
    		return false;
    	}
    	return true;
    }
    
    public void cost(int cost) {
    	if(energie<cost) {
    		return;
    	}
    	energie = Math.max(0, energie - cost);
    }
    
    public void defend(int b) { 
    	if(b<0){
    		throw new IllegalArgumentException("Hero defend error");
    	}
        block += b;
    }
    
    public void charge(int m) {
    	if(m<0) {
    		throw new IllegalArgumentException("charge argument error");
    	}
    	hp = Math.min(maxMana, mana + m); 
    	
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

    public void startCombat() {
        recharge();
        energie = 3;
    }
    
    public void rechargerCombat() {
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
    
    public int hp() {
    	return hp;
    }
    public int maxHp() {
    	return maxHp;
    }
    
    public int mana() {
    	return mana;
    }
    
    public int maxMana() {
    	return maxMana;
    }
    
    public int energie() {
    	return energie;
    }
    
    public int or() {
    	return or;
    }
    
    public void addOr(int a) {
    	or += a;
    }
    
    public void tresor(List<Item> list) {}
    
    public void afficheAnduse(Coord c,Ennemi e,Combat combat) {
    	var i=sac.contenu().getOrDefault(c, null);
    	if(i==null) {return;}
    	if(i instanceof Weapon w) {
    		w.utiliser(this, e, combat);
    	}
    }

    public void ajouterDansSac(Item item) {
        // TODO: gestion rotation, placement
    	sac.placer(item);
    }
/*
	public Position position() {
		// TODO Auto-generated method stub
		return position;
	}
<<<<<<< HEAD

	public void moveTo(int etape, Coord coord) {
	    position.moveTo(etape, coord);
	}
=======
*/
	
>>>>>>> branch 'master' of https://github.com/Velvet394/PROJET_INTERMEDIAIRE.git

	
}
