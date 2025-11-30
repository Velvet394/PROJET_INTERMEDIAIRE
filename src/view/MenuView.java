package view;
import controller.*;

import com.github.forax.zen.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MenuView  {

    private ApplicationContext context;


    private void onPointerEvent(PointerEvent event) {
        if (event.action() == PointerEvent.Action.POINTER_UP) {
            int x = event.location().x();
            int y = event.location().y();

            // Si l'utilisateur clique sur un bouton de démarrage du jeu
            if (x > 350 && x < 450 && y > 500 && y < 550) {
                // Lancer le donjon
                new DonjonApp();
                //context.exit();  // Fermer le menu
                context.dispose();
            }
        }
    }

}
