package modelisation;
import java.util.*;
import java.io.*;

public class Ennemi {
    private final String nom;
    //private final Hp hp;
    private int hp;
    private final int maxHp;
    private final int dmg;
    private int block;
    private final ArrayList<EnnemyActionType> intentions;
    private final int tendanceATTACK; 
    private final int tendanceSKILL;
    private final int tendanceDEFEND; 
    private final int tendanceHEAL;
    //private final int tendanceCURSE;
    private final int tendanceBUFF;

    public Ennemi(String nom, int pv, int d ,/*List<EnnemyActionType> skills,*/int att,int sk,int def, int heal,/*int cur,*/int buf) {
    	Objects.requireNonNull(nom);
    	//Objects.requireNonNull(skills);
    	if(pv<=0||d<0||att<0||sk<0||def<0||heal<0||/*cur<0||*/buf<0) {
    		throw new IllegalArgumentException("Argument(s) de ennemi est/sont inferieur que 0");
    	}
        this.nom = nom;
        this.hp = pv;
        maxHp=pv;
        this.intentions = /*skills;*/new ArrayList<>();
        this.block = 0;
        this.dmg=d;
        tendanceATTACK=att;
        tendanceSKILL=sk;
        tendanceDEFEND=def;
        tendanceHEAL=heal;
        tendanceBUFF=buf;
        //tendanceCURSE=cur;
    }

    public boolean estMort() { 
    	return hp<=0; 
    }

    public EnnemyActionType choisirIntent(Hero hero,Random r) {
    	if (hp <= maxHp/2) {
    		if(r.nextInt(101)<=tendanceHEAL)
    			{return EnnemyActionType.HEAL;}
    		else if(r.nextInt(101)<=tendanceDEFEND) {
    			return EnnemyActionType.DEFEND;
    		}
    	}
    	if(r.nextInt(10)<=5) {
    		if(r.nextInt(101)<=tendanceSKILL) {
    			return EnnemyActionType.SKILL;
    		}
    		if(r.nextInt(101)<=tendanceBUFF) {
    			return EnnemyActionType.BUFF;
    		}
    	}
    	if(r.nextInt(101)<=tendanceATTACK) {
    		return EnnemyActionType.ATTACK;
    	}
    	return EnnemyActionType.CURSE;
    	} 

    public int hp() { 
    	return hp; 
    }
    
    public void executerAction(Hero hero, EnnemyActionType action) {
        switch (action) {
            case ATTACK:
                attack(hero);
                break;
            case DEFEND:
                defend();
                break;
            case HEAL:
                heal();
                break;
            case CURSE:
                curse(hero);
                break;
            case BUFF:
                buff();
                break;
            case SKILL:
                skill();
                break;
            default:
                throw new IllegalArgumentException("Action inconnue : " + action);
        }
    }
    
    private void attack(Hero hero) {
        int damage = 10;
        if (!estMort()) {
            hero.damage(damage);
            IO.println(nom + " attaque et inflige " + damage + " points de dégât.");
        }
    }
    
    public int dmg() {
    	return this.dmg;
    }
    
    @Override
    public int hashCode() {
    	return Objects.hash(nom,hp,intentions,dmg,block,tendanceATTACK,tendanceBUFF,tendanceDEFEND,tendanceHEAL,tendanceSKILL);
    }
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return super.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
    	return obj instanceof Ennemi e &&
    			this.intentions.equals(e.intentions) &&
    			this.nom.equals(e.nom) &&
    			//this.hp.equals(e.hp) &&
    			this.hp==e.hp &&
    			this.maxHp==e.maxHp &&
    			this.dmg==e.dmg &&
    			this.block==e.block &&
    			this.tendanceATTACK==e.tendanceATTACK &&
    			this.tendanceBUFF==e.tendanceBUFF &&
    			this.tendanceDEFEND==e.tendanceDEFEND &&
    			this.tendanceHEAL==e.tendanceHEAL &&
    			this.tendanceSKILL==e.tendanceSKILL;
    }
    
}