package modelisation;

import java.util.ArrayList;
import java.util.List;

public class EnnemisBase {
	public static final List<Ennemi> templates = new ArrayList<>();
	public static final int NUMENN = 3;

    static {
        templates.add(new Ennemi("Slime", 30, 5,3,10,50,20,10,50,30)); //nom, hp, dmg, blockpoint, healpoint tendance att, skill, def, heal , buff
        templates.add(new Ennemi("BigRat", 50, 10, 5,3,40,30,50,20,15));
        templates.add(new Ennemi("Rat", 10, 3, 2, 2, 70,10,20,10,10));
    }

}
