package modelisation;
import java.util.*;
import java.io.*;

/**
 * Represents the main hero controlled by the player.
 * The hero has health points, energy, mana and a backpack of items.
 */

public class Hero {
	 /** Backpack containing all items of the hero. */
    private final Backpack sac;
    /** Current position of the Hero in the Dungeon */
    private Position position;
    /** Current health points */
    private int hp=40;
    /** Maximum Current health points */
    private int maxHp=40;
    /** Current mana points */
    private int mana=20;
    /** Maximum Current mana points */
    private int maxMana=20;
    /** Current experience points */
    private int exp=0;
    /** Maximum experience points */
    private int expMax=10;
    /** Current block value (temporary protection) */
    private int block=0;

    /** Energy available this turn (resets to 3 each turn) */
    private int energie = 3;
    /** Hero level */
    private int niveau = 1;
    
    /** Number of times the hero refused a curse. */
    private int refusMalediction = 0;
    /** Number of curses suffered. */
    private int foisMalediction = 0;
    //private boolean estProteger;
    private int or = 20;
    
    private int hpstk;
    private int manastk;
    private int hporigine;
    private int manaorigine;
    
    /**
     * Tests if the hero should level up and, if so, increases backpack capacity.
     */
    public int testLevelUp() {
    	if(exp>=expMax) {
    		niveau++;
//    		var r=Dice.roll(1, 3);
//    		for(int i=0;i<r;i++) {
//    			sac.allouerCase(null);
//    		}
    		exp=0;
    		expMax=(niveau^niveau)*10;
    		return 1;
    	}
    	return 0;
    }
    
    /**
     * This is the constructor
     */

    public Hero() {
        position = new Position(0, new Coord(0,0));
        sac  = new Backpack();
        hpstk=maxHp;
        manastk=maxMana;
        hporigine=maxHp;
        manaorigine=maxMana;
    }
    
    /**
     * Applies damage to the hero
     */
    
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

    /**
     * Heals the hero 
     */
    public void heal(int val) { 
    	hp = Math.min(maxHp, hp + val); 
    }
    
    /** Verifies if the hero is dead */

    public boolean isDead() { 
    	return hp <= 0;
    }

    /** Verifies if the hero is alive */
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
    
    /** Refills the Mana points */
    public void recharge() { 
    	mana = maxMana; 
    }
    
    /** Verifies if the hro's mana is usable */
    public boolean ifuse(int cost) {
    	if(mana<cost) {
    		return false;
    	}
    	return true;
    }

    /**
     *  Consumes mana points,
     */
    public void use(int val) { 
    	mana = Math.max(0, mana - val); 
    }
    
    /**
     * Checks if the hero has enough 
     */
    
    public boolean ifcost(int cost) {
    	if(energie<cost) {
    		return false;
    	}
    	return true;
    }
    
    /**
     * Consumes energy points
     */
    public void cost(int cost) {
    	if(energie<cost) {
    		return;
    	}
    	energie = Math.max(0, energie - cost);
    }
    /**
     * Adds protection points 
     */
    public void defend(int b) { 
    	if(b<0){
    		throw new IllegalArgumentException("Hero defend error");
    	}
        block += b;
    }
    
    /**
     * Increases current mana, without exceeding maxMana.
     */
    public void charge(int m) {
    	if(m<0) {
    		throw new IllegalArgumentException("charge argument error");
    	}
    	mana = Math.min(maxMana, mana + m); 
    	
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

    /**
     * Initializes a new combat: updates max mana from the backpack,
     * fully recharges mana and resets energy to 3.
     */
    public void startCombat() {
    	updateMaxManaFromBackpack();
    	recharge();
        energie = 3;
        //block = 0;
    }
    
    /**
     * Recharges energy to 3 at the beginning of the hero turn.
     */
    public void rechargerCombat() {
        energie = 3;
    }
    
    /** Adds Malediction to the Hero */
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
    
    public void useOr(int a) {
    	if(a>or) {return;}
    	or-=a;
    }
    
    public void tresor(List<Item> list) {}
    
    /**
     * Uses the item located at a given coordinate in the backpack
     * against an enemy during a combat.
     */
    public void afficheAnduse(Coord c,Ennemi e,Combat combat) {
    	var i=sac.contenu().getOrDefault(c, null);
    	if(i==null) {return;}
    	if(i instanceof Weapon w) {
    		w.utiliser(this, e, combat);
    	}
    }
    
    /**
     * Adds an item to the backpack (if there is enough space).
     */
    public void ajouterDansSac(Item item) {
        // TODO: gestion rotation, placement
    	sac.placer(item);
    }

	public Position position() {
		// TODO Auto-generated method stub
		return position;
	}

	public void moveTo(int etape, Coord coord) {
	    position.moveTo(etape, coord);
	}

	/**
     * Camculates maxMana from the items present in the backpack
     */
	private void updateMaxManaFromBackpack() {
	    int total = 0;
	    for (Item item : sac.contenu().values()) {
	        if (item != null) {
	            total += item.manaCapacity();
	        }
	    }
	    maxMana = total;
	    mana = Math.min(mana, maxMana);
	}

	 /**
     * Updates all hero points that depend on the backpack content.
     */
	public void updateStatsFromBackpack() {
	    updateMaxManaFromBackpack();
	}
	
	public void updateStats() {
		if((hpstk+hporigine)>maxHp) {
			hp=hp+hpstk+hporigine-maxHp;
		}
		else {hp=Math.min(hp,hpstk+hporigine);}
		if((manastk+manaorigine)>maxMana) {
			mana=mana+manastk+manaorigine-maxMana;
		}
		else {mana=Math.min(mana,manastk+manaorigine);}
		maxHp=hpstk+hporigine;
		maxMana=manastk+manaorigine;
	}
	
	public void interrefresh() {
		hpstk=0;
		manastk=0;
	}
	public void interhp(int x) {
		hpstk=hpstk+x;
	}
	public void intermana(int x) {
		manastk=manastk+x;
	}
	
	public void expup() {
		exp=exp+20;
	}
	
	public int exp() {
		return exp;
	}
	
	/** Hero refuses a curse: increments refusal count and applies damage. */
	public int refuserMalediction() {
	    refusMalediction++;          // 累计拒绝次数
	    int dmg = refusMalediction;  // 本次拒绝造成的伤害 = 当前拒绝次数
	    damage(dmg);
	    IO.println("Le hero refuse la malédiction (" + refusMalediction + " refus) et perd " + dmg + " PV.");
	    return dmg;
	}
	
	/** For UI */
	public int refusMaledictionCount() {
	    return refusMalediction;
	}

}
