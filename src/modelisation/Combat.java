package modelisation;
import java.util.*;
import java.io.*;

class Combat {
    private final Hero hero;
    private final List<Ennemi> ennemis;

    public Combat(Hero h, List<Ennemi> e) {
        this.hero = h;
        this.ennemis = e;
    }
    
    public Ennemi getEnnemi(int index) {
    	return ennemis.get(index);
    }
    
    public List<Ennemi> getEnnemis(int index) {
    	return ennemis;
    }
    
    public Hero getHero() {
    	return hero;
    }

    public void tourHero() {
    	hero.rechargerCombat();
    }

    public void tourEnnemis() {
    	for (var e:ennemis) {
    		if (!e.estMort()) {
    			EnnemyActionType a = e.choisirIntent(hero);
    			// a.executer()
    		}
    	}
    }
}

