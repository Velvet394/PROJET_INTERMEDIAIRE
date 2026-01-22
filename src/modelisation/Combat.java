package modelisation;
import java.util.*;
import java.io.*;

/**
 * Represents a combat between a hero and multiple enemies.
 * A combat alternates turns until one side is defeated.
 */

public class Combat {
    private final Hero hero;
    private final List<Ennemi> ennemis;
    
    private final Deque<ItemMaldiction> maledictionsEnAttente = new ArrayDeque<>();
    
    
    /////////////
    ///
    ///attention utilise static final

    private static final List<List<Coord>> CURSE_SHAPES = List.of(
    	    List.of(new Coord(0,0)),
    	    List.of(new Coord(0,0), new Coord(1,0)),
    	    List.of(new Coord(0,0), new Coord(0,1)),                
    	    List.of(new Coord(0,0), new Coord(1,0), new Coord(0,1)),
    	    List.of(new Coord(0,0), new Coord(1,0), new Coord(2,0)),
    	    List.of(new Coord(0,0), new Coord(0,1), new Coord(0,2)), 
    	    List.of(new Coord(0,0), new Coord(1,0), new Coord(0,1), new Coord(1,1)) // 2x2
    	);
    
    
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

//    public void tourHero() {
//    	boolean termine=false; // ATTENTION!!! quand on clique termine round, il est mettre en true
//    	hero.rechargerCombat();
//    	while(/*!termine*/hero.energie()>2) {
//    		hero.afficheAnduse(new Coord(0,0),ennemis.get(0),this);//fonction pour affiche en graphique et choisir item
//    		RefreshListEnnemis();
//    		IO.println(hero.energie());
//    	}
//    }
    
    /**
     * Executes the hero's turn using his available energy.
     */
    
    public void tourHero() {
        hero.rechargerCombat();     
        boolean aJoueAuMoinsUneFois = false;

        while (hero.energie() > 0) {
            if (ennemis.isEmpty()) {
                break;
            }

            hero.afficheAnduse(new Coord(0, 0), ennemis.get(0), this);
            RefreshListEnnemis();
            aJoueAuMoinsUneFois = true;

            
            if (!aJoueAuMoinsUneFois || hero.energie() == 3) {
                break;
            }
        }
    }

    /**
     * Executes the ennemy's turn 
     */
    public void tourEnnemis() {
    	for (var e:ennemis) {
    		if (!e.estMort()) {
    			//EnnemyActionType a = e.choisirIntent(hero,random);
    			// a.executer()
    			e.Action();
//    			e.executerAction(hero);
    			e.executerAction(hero, this);
    			RefreshListEnnemis();
    		}
    	}
    }
    
    /**
     * Refresh the list of ennemies if an ennemy is dead 
     */
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
    
    /**
     * Determines the combat state.
     */
    public int estCombatTermine() {
        
        if (hero.isDead()) {
            IO.println("Le héros est mort ! Le combat est terminé.");
            //Jeu.jeuTermine();
            return 3;
        }
        if(ennemis.isEmpty()) {
        	return 2;
        }
        return 1; 
    }
    
    /**
     * Starts the combat
     */
    
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



    public ItemMaldiction randomMalediction() {
    	
    	int n = CURSE_SHAPES.size();
    	if (n == 0) throw new IllegalStateException("CURSE_SHAPES is empty");
    	
    	int idx = Dice.roll(0, CURSE_SHAPES.size() - 1);
        // 如果 Dice.roll 是含上界/不含上界不确定，可以改成 new Random().nextInt(CURSE_SHAPES.size())
        var shape = CURSE_SHAPES.get(idx);
        return new ItemMaldiction(shape);
    }
    	
    	public void enqueueMalediction(ItemMaldiction m) {
    	    Objects.requireNonNull(m);
    	    maledictionsEnAttente.addLast(m);
    	}

    	public boolean hasPendingMaledictions() {
    	    return !maledictionsEnAttente.isEmpty();
    	}

    	public ItemMaldiction pollMalediction() {
    	    return maledictionsEnAttente.pollFirst();
    	}
    	
    	public int pendingMaledictionsCount() {
    		return maledictionsEnAttente.size();
    	}

	
}

