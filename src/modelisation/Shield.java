package modelisation;
import java.util.*;

public class Shield extends Item {

    public Shield() {
        super("Bouclier", List.of(
                new Coord(0,0),
                new Coord(1,0)   
        ));
    }

    @Override
    public void utiliser(Hero hero, Combat combat) {
        hero.heal(0);
    }
}
