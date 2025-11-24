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

    public void tourEnnemis(Random random) {
    	for (var e:ennemis) {
    		if (!e.estMort()) {
    			//EnnemyActionType a = e.choisirIntent(hero,random);
    			// a.executer()
    			e.Action();
    			e.executerAction(hero);
    		}
    	}
    }
    
    public boolean estCombatTermine() {
        
        if (hero.isDead()) {
            IO.println("Le héros est mort ! Le combat est terminé.");
            return true;
        }

        boolean tousEnnemisMorts = true;
        for (Ennemi e : ennemis) {
            if (!e.estMort()) {
                tousEnnemisMorts = false;
                break;
            }
        }

        if (tousEnnemisMorts) {
            IO.println("Tous les ennemis sont morts ! Vous avez gagné.");
            return true;
        }

        return false; 
    }
}

