package modelisation;

import java.util.ArrayList;
import java.util.List;

public class ennemisBase {
	public static final List<Ennemi> templates = new ArrayList<>();

    static {
        templates.add(new Ennemi("Slime", 30, 5,new));
        templates.add(new Ennemi("Goblin", 50, 10));
        templates.add(new Ennemi("Orc", 80, 15));
        templates.add(new Ennemi("Orc", 80, 15));
    }

}
