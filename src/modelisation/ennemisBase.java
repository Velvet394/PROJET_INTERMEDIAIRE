package modelisation;

import java.util.ArrayList;
import java.util.List;

public class ennemisBase {
	public static final List<Ennemi> templates = new ArrayList<>();

    static {
        templates.add(new Ennemi("Slime", 30, 5,50,20,10,50,30));
        templates.add(new Ennemi("BigRat", 50, 10,40,30,50,20,15));
        templates.add(new Ennemi("Rat", 10, 3,70,10,20,10,10));
    }

}
