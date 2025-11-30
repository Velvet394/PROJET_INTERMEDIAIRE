package view;

import com.github.forax.zen.*;
import modelisation.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.*;

public class DonjonView {
    private Donjon donjon;
    
    public DonjonView(Donjon donjon) {
        this.donjon = donjon;
    }

    public void drawDungeon(Graphics2D g) {
        Map<Coord, Room> salles = donjon.getEtape(0).getSalles();
        for (Map.Entry<Coord, Room> entry : salles.entrySet()) {
            Coord coord = entry.getKey();
            Room room = entry.getValue();
            // Affichage d'une salle
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(coord.x() * 60, coord.y() * 60, 60, 60);
            g.setColor(Color.BLACK);
            g.drawString(room.getType().toString(), coord.x() * 60 + 10, coord.y() * 60 + 30);
        }
    }
}
