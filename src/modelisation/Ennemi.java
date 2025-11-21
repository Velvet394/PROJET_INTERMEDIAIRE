package modelisation;
import java.util.*;
import java.io.*;

public class Ennemi {
    private final String nom;
    private final Hp hp;
    private final int dmg;
    private int block;
    private final List<EnnemyActionType> intentions;
    private final int tendanceATTACK; 
    private final int tendanceSKILL;
    private final int tendanceDEFEND; 
    private final int tendanceHEAL;
    private final int tendanceCURSE;
    private final int tendanceBUFF;

    public Ennemi(String nom, int pv, int d ,List<EnnemyActionType> skills,int att,int sk,int def, int heal,int cur,int buf) {
    	Objects.requireNonNull(nom);
    	Objects.requireNonNull(skills);
    	if(pv<=0||d<0||att<0||sk<0||def<0||heal<0||cur<0||buf<0) {
    		throw new IllegalArgumentException("Hp/DMG de ennemi est inferieur que 0");
    	}
        this.nom = nom;
        this.hp = new Hp(pv);
        this.intentions = skills;
        this.block = 0;
        this.dmg=d;
        tendanceATTACK=att;
        tendanceSKILL=sk;
        tendanceDEFEND=def;
        tendanceHEAL=heal;
        tendanceCURSE=cur;
        tendanceBUFF=buf;
    }

    public boolean estMort() { 
    	return hp.isDead(); 
    }

    public EnnemyActionType choisirIntent(Hero hero,Random r) {
    	if (this.hp.current() <= this.hp.max()/2) {
    		if(r.nextInt()<=tendanceHEAL)
    			{return EnnemyActionType.HEAL;}
    		else if(r.nextInt()<=tendanceDEFEND) {
    			return EnnemyActionType.DEFEND;
    		}
    	} else if (hero.estProteger()) {
    		return EnnemyActionType.BUFF;
    	} else {
    		return EnnemyActionType.ATTACK;
    	}
    }

    public Hp getHp() { 
    	return hp; 
    }
}