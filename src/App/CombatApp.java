package App;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.PointerEvent;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.KeyboardEvent.Key;

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
<<<<<<< HEAD
    public static void initialiserCombat(Hero h, List<Ennemi> es) {
        hero = h;
        ennemis = es;
        combat = new Combat(hero, ennemis);
        enCombat = true;
=======
    public static void initialiserCombat(Hero hero, List<Ennemi> ennemis) {
        CombatApp.hero = hero;
        CombatApp.ennemis = ennemis;
        combat = new Combat(hero, ennemis); //???? pourquoi new combat??
        
>>>>>>> branch 'master' of https://github.com/Velvet394/PROJET_INTERMEDIAIRE.git
    }
    
    public Combat(Hero h,List<>)

    // Méthode principale pour lancer l'application du combat
    public static void main(String[] args) {
        Application.run(Color.BLACK, context -> {
            var screenInfo = context.getScreenInfo();
            WINDOW_WIDTH = screenInfo.width();
            WINDOW_HEIGHT = screenInfo.height();

            // Boucle principale du combat
            while (enCombat) {
                Event event = context.pollOrWaitEvent(16);

                if (event instanceof PointerEvent p) {
                    gererSouris(p);
                } else if (event instanceof KeyboardEvent k) {
                    gererClavier(k);
                }

                updateCombatState();

                context.renderFrame(g -> afficherCombat(g));
            }
        });
    }

    // Affichage du combat : Héros, ennemis, sac, boutons
    public static void afficherCombat(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Héros
        g.setColor(Color.CYAN);
        g.drawString("Héros - PV: " + hero.hp() + "/" + hero.maxHp(), 10, 20);

        // Ennemis
        if (ennemis != null) {
            for (int i = 0; i < ennemis.size(); i++) {
                Ennemi ennemi = ennemis.get(i);
                g.setColor(Color.RED);
                g.drawString(
                        "Ennemi " + (i + 1) + " - PV: " + ennemi.hp() + "/" + ennemi.hp(),
                        10,
                        60 + i * 20
                );
            }
        }

        // Sac
        g.setColor(Color.WHITE);
        g.drawString("Sac : " + hero.getBackpack().contenu().size() + " objets", 10, 120);
        for (int i = 0; i < hero.getBackpack().contenu().size(); i++) {
            Item item = hero.getBackpack().contenu().get(i);
            g.drawString("Objet " + (i + 1) + ": " + item.nom(), 10, 140 + i * 20);
        }

        int buttonWidth = 180;
        int buttonHeight = 40;
        int buttonMargin = 20;

        int attackX = WINDOW_WIDTH - buttonWidth - buttonMargin;
        int attackY = WINDOW_HEIGHT - buttonHeight - 2 * buttonMargin;
        int itemX = WINDOW_WIDTH - buttonWidth - buttonMargin;
        int itemY = WINDOW_HEIGHT - buttonHeight - buttonMargin;

        // Bouton "Attaquer"
        g.setColor(Color.GREEN);
        g.fillRect(attackX, attackY, buttonWidth, buttonHeight);
        g.setColor(Color.BLACK);
        g.drawString("Attaquer", attackX + 20, attackY + 25);

        // Bouton "Utiliser objet"
        g.setColor(Color.YELLOW);
        g.fillRect(itemX, itemY, buttonWidth, buttonHeight);
        g.setColor(Color.BLACK);
        g.drawString("Utiliser objet", itemX + 10, itemY + 25);
    }

    // Mise à jour de l'état du combat
    public static void updateCombatState() {
        if (combat == null) {
            enCombat = false;
            return;
        }

        int status = combat.estCombatTermine(); // 1 = en cours, 2 = win, 3 = lose

        if (status == 1) {
            // combat en cours
            return;
        }

        // combat terminé
        enCombat = false;

        if (status == 2) {
            System.out.println("Le combat est terminé, le héros a gagné !");
            hero.tresor(Generation.genererItems());
        } else if (status == 3) {
            System.out.println("Le combat est terminé, le héros a perdu !");
        }
    }

    // Gestion clavier : ESPACE = tour complet, ESCAPE = quitter combat
    public static void gererClavier(KeyboardEvent event) {
        if (event.action() != KeyboardEvent.Action.KEY_PRESSED) {
            return;
        }

        if (event.key() == Key.SPACE) {
            if (combat != null) {
                // Tour du héros puis tour des ennemis si le combat continue
                combat.tourHero();
                if (combat.estCombatTermine() == 1) {
                    combat.tourEnnemis();
                }
            }
        } else if (event.key() == Key.ESCAPE) {
            enCombat = false;
        }
    }

    // Gestion souris : boutons + clic sur objet
    public static void gererSouris(PointerEvent event) {
        if (event.action() != PointerEvent.Action.POINTER_DOWN) {
            return;
        }

        int mouseX = event.location().x();
        int mouseY = event.location().y();

        int buttonWidth = 180;
        int buttonHeight = 40;
        int buttonMargin = 20;

        int attackX = WINDOW_WIDTH - buttonWidth - buttonMargin;
        int attackY = WINDOW_HEIGHT - buttonHeight - 2 * buttonMargin;
        int itemX = WINDOW_WIDTH - buttonWidth - buttonMargin;
        int itemY = WINDOW_HEIGHT - buttonHeight - buttonMargin;

        // clic sur "Attaquer"
        if (mouseX >= attackX && mouseX <= attackX + buttonWidth &&
            mouseY >= attackY && mouseY <= attackY + buttonHeight) {

            if (ennemis != null && !ennemis.isEmpty()) {
                Ennemi ennemi = ennemis.get(0);
                ennemi.damage(12);
                if (ennemi.estMort()) {
                    ennemis.remove(ennemi);
                }
            }
        }
        // clic sur "Utiliser objet"
        else if (mouseX >= itemX && mouseX <= itemX + buttonWidth &&
                 mouseY >= itemY && mouseY <= itemY + buttonHeight) {

            if (!hero.getBackpack().contenu().isEmpty() &&
                ennemis != null && !ennemis.isEmpty()) {

                Item item = hero.getBackpack().contenu().get(0);
                if (item instanceof Weapon weapon) {
                    weapon.utiliser(hero, ennemis.get(0), combat);
                }
            }
        }
        // clic sur un objet dans la liste du sac (texte)
        else {
            for (int i = 0; i < hero.getBackpack().contenu().size(); i++) {
                int x1 = 10;
                int y1 = 140 + i * 20 - 15;
                int x2 = 200;
                int y2 = 140 + i * 20 + 5;

                if (mouseX >= x1 && mouseX <= x2 &&
                    mouseY >= y1 && mouseY <= y2) {
                    utiliserObjet(hero.getBackpack().contenu().get(i));
                }
            }
        }
    }

    // Exemple simple
    public static void utiliserObjet(Item objet) {
        System.out.println("Objet utilisé: " + objet.nom());
        if (objet instanceof Weapon) {
            hero.heal(50); // exemple : une arme qui soigne (à adapter)
        }
    }
}
