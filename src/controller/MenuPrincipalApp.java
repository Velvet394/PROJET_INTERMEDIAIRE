package controller;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.PointerEvent;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.KeyboardEvent.Key;
import com.github.forax.zen.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MenuPrincipalApp {
    private static int WINDOW_WIDTH;
    private static int WINDOW_HEIGHT;
    private static Image backgroundImage;

    private static final String[] MENU_OPTIONS = {"Nouvelle Partie", "Charger Partie", "Options", "Quitter"};
    private static int hoveredOption = 0; 

    public static void main(String[] args) {
        try {
            backgroundImage = ImageIO.read(new File("/home/asna/Documents/POO/TestProject/MyNewProject/data/backpack-hero-1nqjl.jpg"));  // Mettez le bon chemin de l'image
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Application.run(Color.BLACK, context -> {
            var screenInfo = context.getScreenInfo();
            WINDOW_WIDTH = screenInfo.width();
            WINDOW_HEIGHT = screenInfo.height();

            while (true) {
                var event = context.pollEvent();
                if (event != null) {
                    if (event instanceof PointerEvent pointerEvent) {
                        gererSouris(pointerEvent);
                    }
                    else if (event instanceof KeyboardEvent keyboardEvent) {
                        gererClavier(keyboardEvent);
                    }
                }

                context.renderFrame(MenuPrincipalApp::afficherMenu);
            }
        });
    }

    public static void afficherMenu(Graphics2D g) {
        g.drawImage(backgroundImage, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);

        Font fontTitre = new Font("Arial", Font.BOLD, 40);
        g.setFont(fontTitre);

        String bienvenueText = "Bienvenue dans BackPackHero";
        int bienvenueX = (WINDOW_WIDTH - g.getFontMetrics().stringWidth(bienvenueText)) / 2;
        int bienvenueY = 100;

        g.setColor(Color.WHITE);
        g.drawString(bienvenueText, bienvenueX, bienvenueY);

        Font fontOptions = new Font("Arial", Font.PLAIN, 30);
        g.setFont(fontOptions);

        int optionHeight = 50; // Espace entre les options
        int totalMenuHeight = MENU_OPTIONS.length * optionHeight;
        int startY = (WINDOW_HEIGHT - totalMenuHeight) / 2; 

        for (int i = 0; i < MENU_OPTIONS.length; i++) {
            int optionY = startY + i * optionHeight;

            if (i == hoveredOption) {
                g.setColor(Color.YELLOW); 
            } else {
                g.setColor(Color.WHITE); 
            }

            int optionWidth = g.getFontMetrics().stringWidth(MENU_OPTIONS[i]);
            int optionX = (WINDOW_WIDTH - optionWidth) / 2; 
            g.drawString(MENU_OPTIONS[i], optionX, optionY);
        }
    }

    public static void gererClavier(KeyboardEvent event) {
        if (event.key() == Key.UP) {
            // Déplacer la sélection vers le haut
            hoveredOption = (hoveredOption > 0) ? hoveredOption - 1 : MENU_OPTIONS.length - 1;
        } else if (event.key() == Key.DOWN) {
            // Déplacer la sélection vers le bas
            hoveredOption = (hoveredOption < MENU_OPTIONS.length - 1) ? hoveredOption + 1 : 0;
        } else if (event.key() == Key.SPACE) {
            // Si l'utilisateur appuie sur ESPACE, lancez l'action
            switch (hoveredOption) {
                case 0:
                    commencerNouvellePartie();  // Nouvelle Partie
                    break;
                case 1:
                    chargerPartie();  // Charger une Partie
                    break;
                case 2:
                    afficherOptions();  // Afficher les options
                    break;
                case 3:
                    System.exit(0);  // Quitter le jeu
                    break;
                default:
                    break;
            }
        }
    }

    // Gestion des événements de souris
    public static void gererSouris(PointerEvent pointerEvent) {
        int mouseX = pointerEvent.location().x();
        int mouseY = pointerEvent.location().y();

        // Détecter l'option survolée par la souris
        int hoverIndex = -1;
        for (int i = 0; i < MENU_OPTIONS.length; i++) {
            int optionY = (WINDOW_HEIGHT - MENU_OPTIONS.length * 50) / 2 + i * 50;
            int optionWidth = 150; // Largeur approximative des options (à ajuster selon la police)
            int optionX = (WINDOW_WIDTH - optionWidth) / 2;

            // Vérifier si la souris est au-dessus d'une option
            if (mouseX >= optionX && mouseX <= optionX + optionWidth &&
                mouseY >= optionY - 30 && mouseY <= optionY + 20) {
                hoverIndex = i;
                break;
            }
        }

        // Mettre à jour l'option survolée
        hoveredOption = hoverIndex;

        // Gérer un clic de souris sur une option
        if (pointerEvent.action() == PointerEvent.Action.POINTER_DOWN && hoveredOption != -1) {
            switch (hoveredOption) {
                case 0:  // Nouvelle Partie
                    commencerNouvellePartie();
                    break;
                case 1:  // Charger Partie
                    chargerPartie();
                    break;
                case 2:  // Options
                    afficherOptions();
                    break;
                case 3:  // Quitter
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }
    }

    public static void commencerNouvellePartie() {
        System.out.println("Lancement d'une nouvelle partie...");
        DonjonApp.main(new String[]{}); 
    }

    public static void chargerPartie() {
        System.out.println("Chargement de la partie...");
    }

    public static void afficherOptions() {
        System.out.println("Affichage des options...");
    }
}