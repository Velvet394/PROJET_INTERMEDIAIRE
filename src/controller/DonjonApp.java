package controller;

import App.CombatApp;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.KeyboardEvent.Key;
import com.github.forax.zen.PointerEvent;

import modelisation.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class DonjonApp {
    private static Donjon donjon = new Donjon();
    private static Hero hero = new Hero();

    private static int WINDOW_WIDTH;
    private static int WINDOW_HEIGHT;
    private static final int ROOM_SIZE = 100;
    private static final int DONJON_WIDTH = 5;
    private static final int DONJON_HEIGHT = 5;

    // case sélectionnée (contour blanc)
    private static int selectedX = 0;
    private static int selectedY = 0;

    public static void main(String[] args) {
        initialiserDonjon();

        Application.run(Color.BLACK, context -> {
            var screenInfo = context.getScreenInfo();
            WINDOW_WIDTH = screenInfo.width();
            WINDOW_HEIGHT = screenInfo.height();

            // boucle principale du donjon
            while (true) {
<<<<<<< HEAD
                // un évènement max par frame (~60 FPS)
                Event event = context.pollOrWaitEvent(16);

                if (event instanceof KeyboardEvent k) {
                    gererClavier(k);
                } else if (event instanceof PointerEvent p) {
                    // Si tu veux activer la souris :
                    // interagirAvecSalle(p);
=======
                Event Event = context.pollEvent();
                if (Event != null && Event instanceof KeyboardEvent k) {
                    gererDeplacement(k);
                    gererSelection(k);
>>>>>>> branch 'master' of https://github.com/Velvet394/PROJET_INTERMEDIAIRE.git
                }

<<<<<<< HEAD
                context.renderFrame(g -> afficherDonjon(g));
=======
                if (Event != null && Event instanceof PointerEvent p) {
                	if(p.action() == PointerEvent.Action.POINTER_DOWN) {
                		interagirAvecSalle(p);
                }}

                // Affichage en fonction de l'état du jeu
                if (enCombat) {
                    afficherCombat(context); // Si en mode combat, afficher l'écran de combat
                } else {
                    context.renderFrame(DonjonApp::afficherDonjon); // Sinon, afficher le donjon
                }
>>>>>>> branch 'master' of https://github.com/Velvet394/PROJET_INTERMEDIAIRE.git
            }
        });
    }

    // Initialisation simple du donjon
    public static void initialiserDonjon() {
        Etape e1 = new Etape();
        e1.ajouterSalle(new Coord(0, 0), new Room(RoomType.ENEMY,    new Enter_Combat()));
        e1.ajouterSalle(new Coord(1, 0), new Room(RoomType.ENEMY,    new Enter_Combat()));
        e1.ajouterSalle(new Coord(2, 0), new Room(RoomType.TREASURE, new Enter_Tresor()));
        e1.ajouterSalle(new Coord(3, 0), new Room(RoomType.MERCHANT, new Enter_Market()));
        e1.ajouterSalle(new Coord(4, 0), new Room(RoomType.HEALER,   new Enter_Healer()));
        donjon.ajouterEtape(0, e1);

        // position initiale du héros
        hero.position().setCoord(new Coord(0, 0));
        updateSelectionFromHeroPosition();
    }

    private static void updateSelectionFromHeroPosition() {
        selectedX = hero.position().getCoord().x();
        selectedY = hero.position().getCoord().y();
    }

    // Affichage du donjon
    public static void afficherDonjon(Graphics2D g) {
        Etape etage = donjon.getEtape(hero.position().getEtape());
        int centerX = (WINDOW_WIDTH - ROOM_SIZE * DONJON_WIDTH) / 2;
        int centerY = (WINDOW_HEIGHT - ROOM_SIZE * DONJON_HEIGHT) / 2;

        for (int y = 0; y < DONJON_HEIGHT; y++) {
            for (int x = 0; x < DONJON_WIDTH; x++) {
                Coord c = new Coord(x, y);
                Room room = etage.getSalle(c);

                int drawX = centerX + x * ROOM_SIZE;
                int drawY = centerY + y * ROOM_SIZE;

                if (room == null) {
                    g.setColor(Color.GRAY);
                } else {
                    switch (room.getType()) {
                        case ENEMY -> g.setColor(Color.RED);
                        case TREASURE -> g.setColor(Color.YELLOW);
                        case MERCHANT -> g.setColor(Color.GREEN);
                        case HEALER -> g.setColor(Color.BLUE);
                        case GATE -> g.setColor(Color.WHITE);
                        default -> g.setColor(Color.DARK_GRAY);
                    }
                }

                g.fillRect(drawX, drawY, ROOM_SIZE, ROOM_SIZE);

                // héros
                if (x == hero.position().getCoord().x()
                        && y == hero.position().getCoord().y()) {
                    g.setColor(Color.CYAN);
                    g.fillRect(drawX + 30, drawY + 30, 40, 40);
                }

                // contour de la case sélectionnée
                if (x == selectedX && y == selectedY) {
                    g.setColor(Color.WHITE);
                    g.drawRect(drawX, drawY, ROOM_SIZE, ROOM_SIZE);
                }
            }
        }

        // infos héros
        g.setColor(Color.WHITE);
        g.drawString("PV: " + hero.hp() + "/" + hero.maxHp(), 10, 20);
        g.drawString("Sac à dos: " + hero.getBackpack().contenu().size() + " objets", 10, 40);
    }

    // Gestion du clavier (flèches + ESPACE + ESCAPE)
    public static void gererClavier(KeyboardEvent event) {
        if (event.action() != KeyboardEvent.Action.KEY_PRESSED) {
            return;
        }

        if (event.key() == Key.UP) {
            selectedY = Math.max(0, selectedY - 1);
        } else if (event.key() == Key.DOWN) {
            selectedY = Math.min(DONJON_HEIGHT - 1, selectedY + 1);
        } else if (event.key() == Key.LEFT) {
            selectedX = Math.max(0, selectedX - 1);
        } else if (event.key() == Key.RIGHT) {
            selectedX = Math.min(DONJON_WIDTH - 1, selectedX + 1);
        } else if (event.key() == Key.SPACE) {
            entrerDansSalle();
        } else if (event.key() == Key.ESCAPE) {
            System.exit(0);
        }
    }

    // Entrer dans la salle sélectionnée
    public static void entrerDansSalle() {
        Etape etage = donjon.getEtape(hero.position().getEtape());
        Coord cible = new Coord(selectedX, selectedY);
        Room room = etage.getSalle(cible);

        if (room == null) {
            IO.println("Aucune salle à cet endroit.");
            return;
        }

        // déplacer le héros sur la salle
        hero.position().setCoord(cible);

        // appliquer l'effet de la salle
        room.visiter(hero);

        switch (room.getType()) {
            case ENEMY -> {
                IO.println("Combat contre un ennemi !");
                List<Ennemi> ennemis = Generation.genererEnnemis();
                System.out.println("Nombre d'ennemis générés : " + (ennemis == null ? 0 : ennemis.size()));
                if (ennemis != null && !ennemis.isEmpty()) {
                    CombatApp.initialiserCombat(hero, ennemis);
                    CombatApp.main(new String[]{});  // ouvre la fenêtre de combat

                    if (hero.hp() <= 0) {
                        IO.println("Le héros est mort... Game Over.");
                        System.exit(0);
                    } else {
                        IO.println("Le héros a survécu au combat.");
                    }
                } else {
                    IO.println("Aucun ennemi généré.");
                }
            }
            case TREASURE -> IO.println("Salle de trésor.");
            case MERCHANT -> IO.println("Salle du marchand.");
            case HEALER -> IO.println("Salle du guérisseur.");
            case GATE -> avancerVersEtageSuivant();
            default -> {
                // EMPTY, HALLWAY, EVENT...
            }
        }
    }

    // Clic souris optionnel (si tu veux l'activer)
    public static void interagirAvecSalle(PointerEvent event) {
        if (event.action() != PointerEvent.Action.POINTER_DOWN) {
            return;
        }

        int mouseX = event.location().x();
        int mouseY = event.location().y();

        int offsetX = (WINDOW_WIDTH - ROOM_SIZE * DONJON_WIDTH) / 2;
        int offsetY = (WINDOW_HEIGHT - ROOM_SIZE * DONJON_HEIGHT) / 2;

        int x = (mouseX - offsetX) / ROOM_SIZE;
        int y = (mouseY - offsetY) / ROOM_SIZE;

        if (x >= 0 && x < DONJON_WIDTH && y >= 0 && y < DONJON_HEIGHT) {
            selectedX = x;
            selectedY = y;
            entrerDansSalle();
        }
    }

    // Passer à l'étage suivant si on est sur une GATE
    public static void avancerVersEtageSuivant() {
        Etape etageActuel = donjon.getEtape(hero.position().getEtape());
        Room salleActuelle = etageActuel.getSalle(hero.position().getCoord());
        if (salleActuelle != null && salleActuelle.getType() == RoomType.GATE) {
            int etageSuivant = hero.position().getEtape() + 1;
            if (etageSuivant < donjon.etapes().size()) {
                Coord new_pos = new Coord(
                        hero.position().getCoord().x(),
                        hero.position().getCoord().y()
                );
                hero.position().moveTo(etageSuivant, new_pos);
                IO.println("Vous avez avancé à l'étage suivant.");
            } else {
                IO.println("Félicitations ! Vous avez terminé le jeu.");
            }
        }
    }
}
