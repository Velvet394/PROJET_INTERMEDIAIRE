package view;

import com.github.forax.zen.*;
import modelisation.*;

import java.awt.Color;
import java.awt.Graphics2D;

//Exemple de code pour afficher une grille de sac à dos
public class BackPACKVIEW {
 private static final int ROWS = 3;  // 3 lignes
 private static final int COLS = 5;  // 5 colonnes
 private Backpack backpack;

 public BackPACKVIEW(Backpack backpack) {
     this.backpack = backpack;
 }

 public void drawBackpack(Graphics2D g) {
     for (int i = 0; i < ROWS; i++) {
         for (int j = 0; j < COLS; j++) {
             Coord coord = new Coord(j, i);
             Item item = backpack.getItemAt(coord);
             // Dessiner un rectangle pour chaque case
             g.setColor(Color.GRAY);
             g.fillRect(j * 50, i * 50, 50, 50); // Exemple de taille de la case
             if (item != null) {
                 g.setColor(Color.BLACK);
                 g.drawString(item.nom(), j * 50 + 5, i * 50 + 25);
             }
         }
     }
 }
}

