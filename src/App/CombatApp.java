package App;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.PointerEvent;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.KeyboardEvent.Key;
import com.github.forax.zen.*;
import modelisation.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class CombatApp {
    private static Hero hero;
    private static List<Ennemi> ennemis;
    private static Combat combat;
    private static boolean enCombat = true;
    private static int WINDOW_WIDTH;
    private static int WINDOW_HEIGHT;

    // Initialisation du combat avec le héros et les ennemis
    public static void initialiserCombat(Hero hero, List<Ennemi> ennemis) {
        CombatApp.hero = hero;
        CombatApp.ennemis = ennemis;
        combat = new Combat(hero, ennemis);
    }

    // Méthode principale pour lancer l'application du combat
    public static void main(String[] args) {
        Application.run(Color.BLACK, context -> {
            var screenInfo = context.getScreenInfo();
            WINDOW_WIDTH = screenInfo.width();
            WINDOW_HEIGHT = screenInfo.height();

            // Boucle principale du combat
            while (enCombat) {
                var event = context.pollEvent();
                if (event != null) {
                    if (event instanceof PointerEvent pointerEvent) {
                        gererSouris(pointerEvent); // Gestion des clics pour sélectionner les ennemis et utiliser des objets
                    } else if (event instanceof KeyboardEvent keyboardEvent) {
                        gererClavier(keyboardEvent); // Gestion des actions via clavier
                    }
                }

                // On affiche l'écran du combat après chaque mise à jour de l'état
                context.renderFrame(g -> afficherCombat(g));
                updateCombatState(); // Mettre à jour l'état du combat (tour du héros / tour des ennemis)
            }
        });
    }

    // Affichage du combat : Héros, ennemis, et sac
    public static void afficherCombat(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Affichage des informations du héros
        g.setColor(Color.CYAN);
        g.drawString("Héros - PV: " + hero.hp() + "/" + hero.maxHp(), 10, 20);

        // Affichage des ennemis
        for (int i = 0; i < ennemis.size(); i++) {
            Ennemi ennemi = ennemis.get(i);
            g.setColor(Color.RED);
            g.drawString("Ennemi " + (i + 1) + " - PV: " + ennemi.hp() + "/" + ennemi.hp(), 10, 60 + i * 20);
        }

        // Affichage du sac du héros
        g.setColor(Color.WHITE);
        g.drawString("Sac : " + hero.getBackpack().contenu().size() + " objets", 10, 120);

        // Affichage des objets dans le sac
        for (int i = 0; i < hero.getBackpack().contenu().size(); i++) {
            g.drawString("Objet " + (i + 1) + ": " + hero.getBackpack().contenu().get(i).nom(), 10, 140 + i * 20);
        }

        // Affichage des options d'interaction (Attaquer / Utiliser un objet)
        int buttonWidth = 180;
        int buttonHeight = 40;
        int buttonMargin = 20;

        // Clic sur "Attaquer"
        g.setColor(Color.GREEN);
        g.fillRect(WINDOW_WIDTH - buttonWidth - buttonMargin, WINDOW_HEIGHT - buttonHeight - 2 * buttonMargin, buttonWidth, buttonHeight);
        g.setColor(Color.BLACK);
        g.drawString("Attaquer", WINDOW_WIDTH - buttonWidth / 2 - buttonMargin, WINDOW_HEIGHT - buttonHeight / 2 - 2 * buttonMargin);

        // Clic sur "Utiliser objet"
        g.setColor(Color.YELLOW);
        g.fillRect(WINDOW_WIDTH - buttonWidth - buttonMargin, WINDOW_HEIGHT - buttonHeight - buttonMargin, buttonWidth, buttonHeight);
        g.setColor(Color.BLACK);
        g.drawString("Utiliser objet", WINDOW_WIDTH - buttonWidth / 2 - buttonMargin, WINDOW_HEIGHT - buttonHeight / 2 - buttonMargin);
    }

    // Mise à jour de l'état du combat : gestion du tour du héros et des ennemis
    public static void updateCombatState() {
        if (combat.estCombatTermine() != 1) {
            enCombat = false; // Fin du combat si le héros est mort ou tous les ennemis sont tués
            if (combat.estCombatTermine() == 2) {
                System.out.println("Le combat est terminé, le héros a gagné !");
                // Lancer une action après la victoire, comme récolter un trésor
                hero.tresor(Generation.genererItems());
            } else if (combat.estCombatTermine() == 3) {
                System.out.println("Le combat est terminé, le héros a perdu !");
                // Fin du jeu, ou gestion de la défaite du héros
            }
        }
    }

    // Gestion des événements clavier (actions du joueur)
    public static void gererClavier(KeyboardEvent event) {
        if (event.key() == Key.SPACE) {
            // Si l'utilisateur appuie sur ESPACE, effectuer une action
            if (combat != null) {
                combat.tourHero(); // Le héros effectue son tour
                if (combat.estCombatTermine() == 1) {
                    combat.tourEnnemis(); // Les ennemis agissent après le tour du héros
                }
            }
        }
    }

    // Gestion des événements de la souris (sélection d'un ennemi ou d'un objet)
    public static void gererSouris(PointerEvent event) {
        int mouseX = event.location().x();
        int mouseY = event.location().y();

        // Détection des clics pour attaquer ou utiliser un objet
        int buttonWidth = 180;
        int buttonHeight = 40;
        int buttonMargin = 20;

        // Clic sur "Attaquer"
        if (mouseX >= (WINDOW_WIDTH - buttonWidth - buttonMargin) && mouseX <= (WINDOW_WIDTH - buttonMargin) &&
            mouseY >= (WINDOW_HEIGHT - buttonHeight - 2 * buttonMargin) && mouseY <= (WINDOW_HEIGHT - buttonMargin)) {
            if (ennemis != null && !ennemis.isEmpty()) {
                // Sélection de l'ennemi (ici, on attaque le premier ennemi de la liste)
                Ennemi ennemi = ennemis.get(0); // Attaque du premier ennemi
                // Le héros inflige des dégâts à l'ennemi
                ennemi.damage(12); // Le héros inflige des dégâts à l'ennemi avec sa propre méthode
                if (ennemi.estMort()) {
                    ennemis.remove(ennemi); // Ennemi vaincu, on le retire de la liste
                }
            }
        } 
        // Clic sur "Utiliser objet"
        else if (mouseX >= (WINDOW_WIDTH - buttonWidth - buttonMargin) && mouseX <= (WINDOW_WIDTH - buttonMargin) &&
                 mouseY >= (WINDOW_HEIGHT - buttonHeight - buttonMargin) && mouseY <= (WINDOW_HEIGHT - buttonMargin)) {
            if (!hero.getBackpack().contenu().isEmpty()) {
                // Utilisation d'un objet (par exemple, une arme)
                Item item = hero.getBackpack().contenu().get(0); // On prend le premier objet
                if (item instanceof Weapon weapon) {
                    // Utilisation de l'arme
                    weapon.utiliser(hero, ennemis.get(0), combat);
                }
            }
        } 
        // Clic sur un objet dans le sac
        else {
            for (int i = 0; i < hero.getBackpack().contenu().size(); i++) {
                if (mouseX >= 10 && mouseX <= 200 && mouseY >= 140 + i * 20 && mouseY <= 160 + i * 20) {
                    // Utiliser l'objet sélectionné
                    UtiliserObjet(hero.getBackpack().contenu().get(i));  
                }
            }
        }
    }

    // Exemple simple d'utilisation d'un objet
    public static void UtiliserObjet(Object objet) {
        System.out.println("Objet utilisé: " + objet);
        // Ici, tu peux ajouter de la logique pour l'utilisation d'objets (par exemple, soigner le héros)
        if (objet instanceof Weapon) {
            hero.heal(50); // Exemple d'objet "Potion" qui soigne de 50 PV
        }
    }
}
