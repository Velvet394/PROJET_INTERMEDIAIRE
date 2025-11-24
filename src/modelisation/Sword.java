/*package modelisation;
import java.util.*;
import java.io.*;

class Sword extends Item {
	
    public Sword() {
        super("Épée", List.of(
                new Coord(0,0)   // 1 case
        ));
    }

    @Override
    public void utiliser(Hero hero, Combat combat) {
        if (!combat.getEnnemis().isEmpty()) {
            Ennemi e = combat.getEnnemis().get(0);
            e.getHp().damage(6);
        }
    }
}*/