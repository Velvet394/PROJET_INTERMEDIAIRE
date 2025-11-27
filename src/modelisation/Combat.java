package modelisation;
import java.util.*;
import java.io.*;

public class Combat {
    private final Hero hero;
    private final List<Ennemi> ennemis;

    public Combat(Hero h, List<Ennemi> e) {
        this.hero = h;
        this.ennemis = e;
    }
    
    

	public Ennemi getEnnemi(int index) {
    	return ennemis.get(index);
    }
    
    public List<Ennemi> getEnnemis() {
    	return ennemis;
    }
    
    public Hero getHero() {
    	return hero;
    }

    public void tourHero() {
    	boolean termine=false; // ATTENTION!!! quand on clique termine round, il est mettre en true
    	hero.rechargerCombat();
    	//while(/*!termine*/hero.energie()>2) {
    		hero.afficheAnduse(new Coord(0,0),ennemis.get(0),this);//fonction pour affiche en graphique et choisir item
    		RefreshListEnnemis();
    	//}
    }

    public void tourEnnemis() {
    	for (var e:ennemis) {
    		if (!e.estMort()) {
    			//EnnemyActionType a = e.choisirIntent(hero,random);
    			// a.executer()
    			e.Action();
    			e.executerAction(hero);
    			RefreshListEnnemis();
    		}
    	}
    }
    
    public void RefreshListEnnemis() { // l'appliquer chaque action de hero et ennemi
    	/*var list=new ArrayList<Integer>();
    	int index=0;
    	for (Ennemi e : ennemis) {
    		if(e.estMort()) {
    			list.add(index);
    		}
    		index++;
    	}
    	for(var i:list) {
    		ennemis.remove(i);
    	}*/
    	ennemis.removeIf(Ennemi::estMort);
    }
    
    public int estCombatTermine() {
        
        if (hero.isDead()) {
            IO.println("Le héros est mort ! Le combat est terminé.");
            Jeu.jeuTermine();
            return 3;
        }
        if(ennemis.isEmpty()) {
        	return 2;
        }
        return 1; 
    }
    
    public void startCombat() {
    	var state=CombatState.HERO_TURN;
    	hero.startCombat();
    	while (state != CombatState.FINISHED) {
    	    switch (state) {
    	        case HERO_TURN -> {
    	            tourHero();
    	            if (estCombatTermine()!=1) state = CombatState.FINISHED;
    	            else state = CombatState.ENEMY_TURN;
    	        }
    	        case ENEMY_TURN -> {
    	            tourEnnemis();
    	            if (estCombatTermine()!=1) state = CombatState.FINISHED;
    	            else state = CombatState.HERO_TURN;
    	        }
    	    }
    	}
    	hero.tresor(Generation.genererItems());
    }



	
}

