package view;

import com.github.forax.zen.*;
import modelisation.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class CombatView {
    private Combat combat;
    
    public CombatView(Combat combat) {
        this.combat = combat;
    }

    public void drawCombat(Graphics2D g) {
        // Affichage de la santé du héros
        g.setColor(Color.RED);
        g.fillRect(10, 10, combat.getHero().hp() * 2, 20);  // Exemple de barre de vie du héros
        
        // Affichage de la santé des ennemis
        List<Ennemi> ennemis = combat.getEnnemis();
        int y = 40;
        for (Ennemi ennemi : ennemis) {
            g.setColor(Color.GREEN);
            g.fillRect(10, y, ennemi.hp() * 2, 20);  // Exemple de barre de vie de l'ennemi
            y += 30;
        }
    }
}

