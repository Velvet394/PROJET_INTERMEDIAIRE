package modelisation;
import java.util.*;
import java.io.*;

class Combat {
    private final Hero hero;
    private final HashMap<Integerte,Ennemi> ennemis;

    public Combat(Hero h, Map<Integerte,Ennemi e>) {
        this.hero = h;
        this.ennemis = e;
    }
    
    public HashMap<Integerte,Ennemi> getEnnemis() {
    	return ennemis;
    }
    
    public Hero getHero() {
    	return hero;
    }

    public void tourHero() {
    	hero.rechargerCombat();
    }

    public void tourEnnemis() {
    	for (Ennemi e: ennemis) {
    		if (!e.estMort()) {
    			EnnemyActionType a = e.choisirIntent(hero);
    			// a.executer()
    		}
    	}
    }
}

