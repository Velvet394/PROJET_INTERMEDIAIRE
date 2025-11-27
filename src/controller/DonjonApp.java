package controller;

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

public class DonjonApp {
    private static Donjon donjon = new Donjon();
    static Hero hero = new Hero();

    private static int WINDOW_WIDTH;
    private static int WINDOW_HEIGHT;
    private static final int ROOM_SIZE = 100; 

    private static final int DONJON_WIDTH = 5;  
    private static final int DONJON_HEIGHT = 5; 

    private static int selectedX = 0;  
    private static int selectedY = 0;  
	public static Ennemi ennemy;

    public static void main(String[] args) {
        initialiserDonjon();

        Application.run(Color.BLACK, context -> {
            var screenInfo = context.getScreenInfo();
            WINDOW_WIDTH = screenInfo.width();
            WINDOW_HEIGHT = screenInfo.height();

           
            while (true) {
                KeyboardEvent keyboardEvent = (KeyboardEvent) context.pollEvent();
                if (keyboardEvent != null) {
                    gererDeplacement(keyboardEvent);
                    gererSelection(keyboardEvent); 
                }

                PointerEvent pointerEvent = (PointerEvent) context.pollEvent();
                if (pointerEvent != null && pointerEvent.action() == PointerEvent.Action.POINTER_DOWN) {
                    interagirAvecSalle(pointerEvent);
                }

                context.renderFrame(DonjonApp::afficherDonjon);
            }
        });
    }

    public static void initialiserDonjon() {
        Etape e1 = new Etape();
        e1.ajouterSalle(new Coord(0, 0), new Room(RoomType.HALLWAY, new Enter_empty()));
        e1.ajouterSalle(new Coord(1, 0), new Room(RoomType.ENEMY, new Enter_Combat()));
        e1.ajouterSalle(new Coord(2, 0), new Room(RoomType.TREASURE, new Enter_Tresor()));
        e1.ajouterSalle(new Coord(3, 0), new Room(RoomType.MERCHANT, new Enter_Market()));
        e1.ajouterSalle(new Coord(4, 0), new Room(RoomType.HEALER, new Enter_Healer())); 
        donjon.ajouterEtape(0, e1);
    }


   
    public static void afficherDonjon(Graphics2D g) {
        Etape etage = donjon.getEtape(hero.position().getEtape());

        
        int centerX = (WINDOW_WIDTH - ROOM_SIZE * DONJON_WIDTH) / 2; // Centrer horizontalement
        int centerY = (WINDOW_HEIGHT - ROOM_SIZE * DONJON_HEIGHT) / 2; // Centrer verticalement

        
        for (int y = 0; y < DONJON_HEIGHT; y++) {
            for (int x = 0; x < DONJON_WIDTH; x++) {
                Coord c = new Coord(x, y);
                Room room = etage.getSalle(c);

                int drawX = centerX + x * ROOM_SIZE;
                int drawY = centerY + y * ROOM_SIZE;

                // Affichage de la salle
                if (room == null) {
                    g.setColor(Color.GRAY);  // Salle vide
                } else {
                    // Affichage selon le type de la salle
                    switch (room.getType()) {
                        case ENEMY:
                            g.setColor(Color.RED);
                            break;
                        case TREASURE:
                            g.setColor(Color.YELLOW);
                            break;
                        case MERCHANT:
                            g.setColor(Color.GREEN);
                            break;
                        case HEALER:
                            g.setColor(Color.BLUE);
                            break;
                        default:
                            g.setColor(Color.DARK_GRAY);
                    }
                }

                g.fillRect(drawX, drawY, ROOM_SIZE, ROOM_SIZE);

                // Dessiner le héros
                if (x == hero.position().getCoord().x() && y == hero.position().getCoord().y()) {
                    g.setColor(Color.CYAN);
                    g.fillRect(drawX + 30, drawY + 30, 40, 40);  // Héros dans la salle
                }

                // Dessiner les ennemis dans les salles
                if (room != null && room.getType() == RoomType.ENEMY) {
                    g.setColor(Color.RED);
                    g.fillOval(drawX + 30, drawY + 30, 40, 40); // Ennemi dans la salle
                }

               
                if (x == selectedX && y == selectedY) {
                    g.setColor(Color.WHITE);
                    g.drawRect(drawX, drawY, ROOM_SIZE, ROOM_SIZE);  // Contour de la salle sélectionnée
                }
            }
        }

        g.setColor(Color.WHITE);
        g.drawString("PV: " + hero.hp() + "/" + hero.maxHp(), 10, 10);
        g.drawString("Sac à dos: " + hero.getBackpack().contenu().size() + " objets", 10, 30);
    }

    // Gérer la sélection de la salle avec les flèches directionnelles et Entrée
    public static void gererSelection(KeyboardEvent event) {
        if (event.key() == Key.UP) {
            selectedY = Math.max(0, selectedY - 1);  // Déplacer vers le haut
        } else if (event.key() == Key.DOWN) {
            selectedY = Math.min(DONJON_HEIGHT - 1, selectedY + 1);  // Déplacer vers le bas
        } else if (event.key() == Key.LEFT) {
            selectedX = Math.max(0, selectedX - 1);  // Déplacer vers la gauche
        } else if (event.key() == Key.RIGHT) {
            selectedX = Math.min(DONJON_WIDTH - 1, selectedX + 1);  // Déplacer vers la droite
        } else if (event.key() == Key.SPACE) {
            Etape etage = donjon.getEtape(hero.position().getEtape());
            Room room = etage.getSalle(new Coord(selectedX, selectedY));

            if (room != null && room.getType() == RoomType.ENEMY) {
                System.out.println("Vous êtes projeté dans la salle de combat !");
              
                Object ennemy = room.getEnnemis();  
                Combat combat = new Combat(hero, (List<Ennemi>) ennemy);
                combat.startCombat();  // Gérer le combat ici
            } else {
                System.out.println("La salle ne contient pas d'ennemi.");
            }
        }
    }


    public static void gererDeplacement(KeyboardEvent event) {
        if (event.key() == Key.UP) {
            hero.position().move(Direction.UP);
        } else if (event.key() == Key.DOWN) {
            hero.position().move(Direction.DOWN);
        } else if (event.key() == Key.LEFT) {
            hero.position().move(Direction.LEFT);
        } else if (event.key() == Key.RIGHT) {
            hero.position().move(Direction.RIGHT);
        }
    }

    public static void interagirAvecSalle(PointerEvent event) {
       
        int mouseX = event.location().x();
        int mouseY = event.location().y();

        
        int offsetX = (WINDOW_WIDTH - ROOM_SIZE * DONJON_WIDTH) / 2;
        int offsetY = (WINDOW_HEIGHT - ROOM_SIZE * DONJON_HEIGHT) / 2;

        
        int x = (mouseX - offsetX) / ROOM_SIZE;
        int y = (mouseY - offsetY) / ROOM_SIZE;

       
        if (x >= 0 && x < DONJON_WIDTH && y >= 0 && y < DONJON_HEIGHT) {
            Etape etage = donjon.getEtape(hero.position().getEtape());
            Room room = etage.getSalle(new Coord(x, y));

            if (room != null) {
                traiterSalle(room);
            }
        }
    }

    
    public static void traiterSalle(Room room) {
        if (room == null) {
            return;
        }

        if (!room.estVisite()) {
            room.visiter();

            switch (room.getType()) {
                case ENEMY:
                    System.out.println("Combat contre un ennemi !");
                    // Gérer le combat ici
                    break;
                case TREASURE:
                    System.out.println("Trésor trouvé !");
                    // Gérer le trésor ici
                    break;
                case MERCHANT:
                    System.out.println("Marchand rencontré !");
                    // Gérer l'achat d'objets
                    break;
                case HEALER:
                    System.out.println("Guérisseur rencontré !");
                    // Soigner le héros
                    break;
                default:
                    System.out.println("Salle vide.");
            }
        } else {
            System.out.println("Salle déjà visitée.");
        }
    }
}