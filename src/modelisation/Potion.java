/*package modelisation;
import java.util.*;

public class Potion extends Item {

    private boolean utilisee = false;

    public Potion() {
        super("Potion", List.of(
                new Coord(0,0)      // 1 case
        ));
    }

    @Override
    public void utiliser(Hero hero, Combat combat) {
        if (utilisee) return;     

        hero.heal(10);            

        utilisee = true;

        hero.getBackpack().retirerItem(this);
    }

    @Override
    public boolean estConsommable() {
        return true;
    }
}*/