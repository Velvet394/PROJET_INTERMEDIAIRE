package App;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.PointerEvent;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.KeyboardEvent.Key;
import modelisation.*;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BackpackApp {
    private static Hero hero; // Le héros
    private static int WINDOW_WIDTH;
    private static int WINDOW_HEIGHT;
    private static final int GRID_WIDTH = 7;  // Largeur du sac (en cases)
    private static final int GRID_HEIGHT = 5; // Hauteur du sac (en cases)
    private static final int CELL_SIZE = 60;  // Taille d'une cellule (case)
    private static int gridX, gridY; // Coordonnées de départ de la grille

    private static BufferedImage heroImage;  // L'image du héros

    // Méthode principale pour lancer l'application
    public static void main(String[] args) {
        // Crée un héros et ajoute des objets à son sac à dos
        hero = new Hero();

        // Créer quelques objets pour tester (Arme et Potion)
        Weapon epée = new Weapon("Epée", 10, 5, false, List.of(new Coord(0, 0), new Coord(0, 1), new Coord(0, 2)), List.of(new Effect_Atteck(12))); // Exemple d'arme
        hero.ajouterDansSac(epée);  // Ajouter l'arme au sac du héros

        // Ajouter d'autres objets si nécessaire
        Item potion = new Item() {
            @Override
            public List<Coord> forme() {
                return List.of(new Coord(0, 0)); // Représentation d'un objet avec une seule case
            }

            @Override
            public Coord offsetCoord() {
                return new Coord(0, 0); // Pas de décalage
            }

            @Override
            public String nom() {
                return "Potion";
            }
        };
        hero.ajouterDansSac(potion); // Ajouter une potion au sac

        // Charger l'image du héros avec ImageIO
        try {
            heroImage = ImageIO.read(new File("./data/Satchel_Idle_Gif.gif"));  // Remplacez par le chemin correct de votre image
        } catch (IOException e) {
            e.printStackTrace();  // Afficher l'erreur si l'image ne se charge pas
        }

        // Lancer l'application
        Application.run(Color.BLACK, context -> {
            var screenInfo = context.getScreenInfo();
            WINDOW_WIDTH = screenInfo.width();
            WINDOW_HEIGHT = screenInfo.height();

            // Calcul des coordonnées pour centrer la grille du sac à dos
            gridX = (WINDOW_WIDTH - (GRID_WIDTH * CELL_SIZE)) / 2;  // Centrer horizontalement
            gridY = (WINDOW_HEIGHT - (GRID_HEIGHT * CELL_SIZE)) / 3;  // Centrer verticalement, en laissant un espace en haut pour le titre

            // Boucle principale de l'application
            while (true) {
                var event = context.pollEvent();
                if (event != null) {
                    if (event instanceof PointerEvent pointerEvent) {
                        gererSouris(pointerEvent); // Gestion des clics de souris
                    } else if (event instanceof KeyboardEvent keyboardEvent) {
                        gererClavier(keyboardEvent); // Gestion des actions via clavier
                    }
                }

                // On affiche l'écran du backpack et le héros
                context.renderFrame(g -> afficherBackpackEtHeros(g));
            }
        });
    }

    // Affichage du backpack du héros et du héros lui-même
    public static void afficherBackpackEtHeros(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);  // Fond noir

        // Affichage du sac à dos sous forme de grille (centrée) avec couleur marron
        g.setColor(new Color(139, 69, 19));  // Couleur marron
        g.fillRect(gridX, gridY, GRID_WIDTH * CELL_SIZE + 10, GRID_HEIGHT * CELL_SIZE + 10);  // Zone du sac à dos avec marges

        g.setColor(Color.WHITE);
        g.drawString("Sac à dos du héros", gridX + 20, gridY - 10);

        // Affichage des objets dans le sac à dos (utilisation de la Map)
        Map<Coord, Item> items = hero.getBackpack().contenu();
        for (Map.Entry<Coord, Item> entry : items.entrySet()) {
            if (entry.getValue() != null) {  // Si l'objet n'est pas null
                // Calcul de la position du coin supérieur gauche de chaque case dans la grille
                int x = gridX + entry.getKey().x() * CELL_SIZE;
                int y = gridY + entry.getKey().y() * CELL_SIZE;

                // Dessiner le cadre de la cellule
                g.setColor(Color.DARK_GRAY);
                g.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                // Affichage du nom de l'objet dans chaque cellule
                g.setColor(Color.WHITE);
                g.drawString(entry.getValue().nom(), x + 5, y + 20);  // Affichage du nom de l'objet
            }
        }

        // Affichage du héros avec l'image
        int heroWidth = 120;
        int heroHeight = 120;
        int heroX = (WINDOW_WIDTH - heroWidth) / 2; // Centré horizontalement
        int heroY = WINDOW_HEIGHT - heroHeight - 10; // Positionné en bas

        // Dessiner l'image du héros
        g.drawImage(heroImage, heroX, heroY, heroWidth, heroHeight, null);  // Afficher l'image du héros

        // Affichage des informations du héros en bas
        g.setColor(Color.WHITE);
        g.drawString("Héros - PV: " + hero.hp() + "/" + hero.maxHp(), 20, WINDOW_HEIGHT - 60);
    }


    // Gestion des événements de souris (par exemple, utiliser un objet du sac)
    public static void gererSouris(PointerEvent event) {
        int mouseX = event.location().x();
        int mouseY = event.location().y();

        // Détection d'un clic sur un objet du sac
        Map<Coord, Item> items = hero.getBackpack().contenu();
        for (Map.Entry<Coord, Item> entry : items.entrySet()) {
            int x = gridX + entry.getKey().x() * CELL_SIZE;
            int y = gridY + entry.getKey().y() * CELL_SIZE;

            if (entry.getValue() != null && mouseX >= x && mouseX <= x + CELL_SIZE && mouseY >= y && mouseY <= y + CELL_SIZE) {
                // Action à effectuer : par exemple, utiliser l'objet
                UtiliserObjet(entry.getValue());
            }
        }
    }

    // Gestion des événements clavier (par exemple, utiliser un objet ou autre action)
    public static void gererClavier(KeyboardEvent event) {
        if (event.key() == Key.ESCAPE) {
            // Quitter ou d'autres actions
            System.exit(0);
        }
    }

    // Exemple d'utilisation d'un objet (afficher un message de test)
    public static void UtiliserObjet(Item item) {
        System.out.println("Objet utilisé: " + item.nom());

        // Ici, vous pouvez ajouter la logique pour utiliser l'objet dans le jeu
        if (item instanceof Weapon) {
            Weapon weapon = (Weapon) item;
            // Par exemple, appliquer des effets sur le héros ou un ennemi
            weapon.utiliser(hero, null, null); // Remplacer `null` par les objets appropriés
        }
    }
}
