package modelisation;
import java.util.*;
import java.io.*;

public class Ennemi {
    private final String nom;
    private final Hp hp;
    private int block;
    private final List<EnnemyActionType> intentions;

    public Ennemi(String nom, int pv, List<EnnemyActionType> skills) {
        this.nom = nom;
        this.hp = new Hp(pv);
        this.intentions = skills;
        this.block = 0;
    }

    public boolean estMort() { 
    	return hp.isDead(); 
    }

    public EnnemyActionType choisirIntent(Hero hero) {
    	if (this.hp.current() <= this.hp.max()) {
    		return EnnemyActionType.HEAL;
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