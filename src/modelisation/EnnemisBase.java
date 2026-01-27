package modelisation;

import java.util.ArrayList;
import java.util.List;

/**
 * Static registry of enemy templates used in the game.
 */
public class EnnemisBase {
	public static final List<Ennemi> templates = new ArrayList<>();
	public static final int NUMENN = 3;

    static {
        templates.add(new Ennemi("Slime", 30, 5,3,10,60,15,20,15,15,"./image/ennemis/slime.png")); //nom, hp, dmg, blockpoint, healpoint tendance att, skill, def, heal , buff
        templates.add(new Ennemi("BigRat", 50, 10, 5,3,60,10,25,10,15,"./image/ennemis/bigrat.png"));
        templates.add(new Ennemi("Rat", 10, 3, 2, 2, 90,5,25,10,10,"./image/ennemis/rat.png"));
//        templates.add(new Ennemi("Rat", 10, 3, 2, 2, 0,0,0,0,0,"./image/ennemis/rat.png"));
    }

}
// path image: "./image/ennemis/x"