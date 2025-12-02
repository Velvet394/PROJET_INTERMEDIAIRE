package controller;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.PointerEvent;

import App.CombatApp;

import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.KeyboardEvent.Key;
import com.github.forax.zen.*;

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

    private static int selectedX = 0;
    private static int selectedY = 0;

    // Variable pour savoir si nous sommes en mode combat
    private static boolean enCombat = false;

    public static void main(String[] args) {
        initialiserDonjon();

        Application.run(Color.BLACK, context -> {
            var screenInfo = context.getScreenInfo();
            WINDOW_WIDTH = screenInfo.width();
            WINDOW_HEIGHT = screenInfo.height();

            // Boucle principale du jeu
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

                // Affichage en fonction de l'état du jeu
                if (enCombat) {
                    afficherCombat(context); // Si en mode combat, afficher l'écran de combat
                } else {
                    context.renderFrame(DonjonApp::afficherDonjon); // Sinon, afficher le donjon
                }
            }
        });
    }

    // Initialisation du donjon
    public static void initialiserDonjon() {
        Etape e1 = new Etape();
        e1.ajouterSalle(new Coord(0, 0), new Room(RoomType.ENEMY, new Enter_Combat()));
        e1.ajouterSalle(new Coord(1, 0), new Room(RoomType.ENEMY, new Enter_Combat()));
        e1.ajouterSalle(new Coord(2, 0), new Room(RoomType.TREASURE, new Enter_Tresor()));
        e1.ajouterSalle(new Coord(3, 0), new Room(RoomType.MERCHANT, new Enter_Market()));
        e1.ajouterSalle(new Coord(4, 0), new Room(RoomType.HEALER, new Enter_Healer()));
        donjon.ajouterEtape(0, e1);

        hero.position().setCoord(new Coord(0, 0));  // Initialiser la position du héros à (0, 0)
        updateSelectionFromHeroPosition();  // Mise à jour de la sélection dès le départ
    }
    
    

    // Mise à jour de la salle sélectionnée en fonction de la position du héros
    private static void updateSelectionFromHeroPosition() {
        selectedX = hero.position().getCoord().x();
        selectedY = hero.position().getCoord().y();
    }
    
    public static void commencerNouvellePartie() {
        System.out.println("Lancement d'une nouvelle partie...");
        // Créez et initialisez un héros et des ennemis pour le combat
        Hero hero = new Hero();  // Création d'un héros
        List<Ennemi> ennemis = Generation.genererEnnemis();  // Générez des ennemis
        CombatApp.initialiserCombat(hero, ennemis);  // Initialisation du combat
        CombatApp.main(new String[]{});  // Lancez le combat dans une nouvelle application
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

                // Affichage de la salle
                if (room == null) {
                    g.setColor(Color.GRAY); 
                } else {
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
                        case GATE:
                            g.setColor(Color.WHITE);
                            break;
                        default:
                            g.setColor(Color.DARK_GRAY);
                    }
                }
                g.fillRect(drawX, drawY, ROOM_SIZE, ROOM_SIZE);

                // Dessiner le héros
                if (x == hero.position().getCoord().x() && y == hero.position().getCoord().y()) {
                    g.setColor(Color.CYAN);
                    g.fillRect(drawX + 30, drawY + 30, 40, 40);
                }

                // Affichage du contour de la salle sélectionnée
                if (x == selectedX && y == selectedY) {
                    g.setColor(Color.WHITE);
                    g.drawRect(drawX, drawY, ROOM_SIZE, ROOM_SIZE);  
                }
            }
        }

        // Afficher les informations de l'état du héros
        g.setColor(Color.WHITE);
        g.drawString("PV: " + hero.hp() + "/" + hero.maxHp(), 10, 10);
        g.drawString("Sac à dos: " + hero.getBackpack().contenu().size() + " objets", 10, 30);
    }

    // Gérer la sélection de la salle
    public static void gererSelection(KeyboardEvent event) {
        // Gérer le déplacement de la sélection avec les touches directionnelles
        if (event.key() == Key.UP) {
            selectedY = Math.max(0, selectedY - 1);
        } else if (event.key() == Key.DOWN) {
            selectedY = Math.min(DONJON_HEIGHT - 1, selectedY + 1);
        } else if (event.key() == Key.LEFT) {
            selectedX = Math.max(0, selectedX - 1);
        } else if (event.key() == Key.RIGHT) {
            selectedX = Math.min(DONJON_WIDTH - 1, selectedX + 1);
        }

        // Si la touche espace est pressée, entrer dans la salle
        else if (event.key() == Key.SPACE) {
            entrerDansSalle();
        }
    }

    // Gérer le déplacement du héros
    public static void gererDeplacement(KeyboardEvent event) {
        Coord currentCoord = hero.position().getCoord();

        if (event.key() == Key.UP && currentCoord.y() > 0) {
            hero.position().move(Direction.UP);
            updateSelectionFromHeroPosition();  // Mettre à jour la sélection après le déplacement
        } else if (event.key() == Key.DOWN && currentCoord.y() < DONJON_HEIGHT - 1) {
            hero.position().move(Direction.DOWN);
            updateSelectionFromHeroPosition();
        } else if (event.key() == Key.LEFT && currentCoord.x() > 0) {
            hero.position().move(Direction.LEFT);
            updateSelectionFromHeroPosition();
        } else if (event.key() == Key.RIGHT && currentCoord.x() < DONJON_WIDTH - 1) {
            hero.position().move(Direction.RIGHT);
            updateSelectionFromHeroPosition();
        }
    }


    // Interaction avec la salle via un clic de souris
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

    // Traitement de l'entrée dans une salle
    public static void traiterSalle(Room room) {
        if (room != null) {
            switch (room.getType()) {
                case ENEMY:
                    IO.println("Combat contre un ennemi !");
                    // Commencer un combat
                    room.visiter(hero);  // Marquer la salle comme visitée
                    
                    // Logique pour démarrer le combat
                    Enter_Combat enterCombat = (Enter_Combat) room.getEnter();
                    enterCombat.apply(hero);  // Cette méthode gère l'apparition des ennemis et l'initiation du combat
                    
                    enCombat = true;  // Changer l'état en mode combat
                    break;
                
                case EMPTY:
                    IO.println("Cette salle est vide.");
                    room.visiter(hero);
                    break;
                    
                case MERCHANT:
                    IO.println("Voici le marchand.");
                    room.visiter(hero);
                    break;
                    
                case TREASURE:
                    IO.println("Voici le trésor.");
                    room.visiter(hero);
                    break;
                    
                case HEALER:
                    IO.println("Voici le guérisseur.");
                    room.visiter(hero);
                    break;
                
                case HALLWAY:
                    IO.println("Il n'y a rien à faire ici.");
                    room.visiter(hero);
                    break;
                
                case GATE:
                    IO.println("Ceci est une porte de sortie.");
                    room.visiter(hero);
                    break;
                
                case EVENT:
                    IO.println("Un événement surprise.");
                    room.visiter(hero);
                    break;
                
                default:
                    throw new IllegalArgumentException("Type de salle inconnu.");
            }
        }
    }
    
    public static void avancerVersEtageSuivant() {
        Etape etageActuel = donjon.getEtape(hero.position().getEtape());
        Room salleActuelle = etageActuel.getSalle(hero.position().getCoord());
        if (salleActuelle.getType() == RoomType.GATE) {
            int etageSuivant = hero.position().getEtape() + 1;
            if (etageSuivant < donjon.etapes().size()) {
            	Coord new_pos = new Coord(hero.position().getCoord().x() + 1, hero.position().getCoord().y() + 1);
                hero.position().moveTo(etageSuivant, new_pos);
                System.out.println("Vous avez avancé à l'étage suivant.");
            } else {
                System.out.println("Félicitations ! Vous avez terminé le jeu.");
            }
        }
    }

    
    public static void traiterSalle2(Room room) {
    	if (room != null) {
    		switch(room.getType()) {
    			case RoomType.ENEMY -> {
    				room.visiter(hero);
    			}
    			
    			case RoomType.EMPTY ->  {
    				IO.println("Cette salle est vide.");
    				room.visiter(hero);
    			}
    			
    			case RoomType.MERCHANT -> {
    				IO.println("Voici le marchant.");
    				room.visiter(hero);
    			}
    			
    			
    			case RoomType.TREASURE -> {
    				IO.println("Voici le trésor.");
    				room.visiter(hero);
    			}
    			
    			case RoomType.HEALER -> {
    				IO.println("Voici le guérisseur.");
    				room.visiter(hero);
    			}
    		
    			case RoomType.HALLWAY -> {
    				IO.println("Il n'y a rien a faire ici.");
    				room.visiter(hero);
    			}

    			case RoomType.GATE -> {
    				IO.println("Ceci est une porte de sortie.");
    				room.visiter(hero);
    			}
    		   
    			case RoomType.EVENT -> {
    				IO.println("Ceci eest un évenement surprise.");
    				room.visiter(hero);
    			}
    		    
    			
    			default -> new IllegalArgumentException();
    		};
    	}
    }

    // Entrer dans la salle sélectionnée
//    public static void entrerDansSalle() {
//        Etape etage = donjon.getEtape(hero.position().getEtape());
//        Room room = etage.getSalle(new Coord(selectedX, selectedY));
//
//        if (room != null && !room.estVisite()) {
//            System.out.println("Entrée dans la salle (" + selectedX + ", " + selectedY + ")");
//            room.visiter(hero);
//
//            
//            if (room.getType() == RoomType.ENEMY) {
//                List<Ennemi> ennemis = room.getEnnemis();
//                if (ennemis != null && !ennemis.isEmpty()) {
//                    //enCombat = true;
//                    //Combat combat = new Combat(hero, ennemis);
//                    //combat.startCombat();
//                	CombatApp.initialiserCombat(hero, ennemis);
//                    CombatApp.main(new String[]{});
//                } else {
//                    System.out.println("Aucun ennemi à combattre.");
//                }
//            }
//            
//            if (room.getType() == RoomType.GATE) {
//            	
//            }
//            
//            
//            if (room.getType() == RoomType.MERCHANT) {
//            	
//            }
//            //(Trésor, Marchand, Guérisseur)
//        }
//    }
    
    
    public static void entrerDansSalle() {
        Etape etage = donjon.getEtape(hero.position().getEtape());
        Room room = etage.getSalle(new Coord(selectedX, selectedY));

        if (room == null) return;

//        if (room.getType() == RoomType.ENEMY) {
//        	Enter_Combat c = (Enter_Combat) room.getEnter(); 
//        	List<Ennemi> ennemis = c.genererEnnemis();
//        	if (ennemis != null && !ennemis.isEmpty()) {
//                CombatApp.initialiserCombat(hero, ennemis); // Initialiser le combat avec le héros et les ennemis
//                CombatApp.main(new String[]{}); // Lancer l'application de combat
//                enCombat = true; // Mettre l'état du jeu en mode combat
//            }
//        }
        // Visiter la salle -> Enter.apply() s'occupe de tout
        room.visiter(hero);
        
    }


    // Affichage du combat
    public static void afficherCombat(ApplicationContext context) {
        context.renderFrame(g -> {
            // Effacer l'écran avec un fond noir
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

            // Affichage de l'état du combat
            g.setColor(Color.WHITE);
            g.drawString("Combat en cours!", 10, 20);

            // Affichage des points de vie du héros
            g.drawString("Héros - PV: " + hero.hp() + "/" + hero.maxHp(), 10, 40);

            // Afficher l'inventaire du héros
            g.drawString("Sac à dos: " + hero.getBackpack().contenu().size() + " objets", 10, 80);

            // Optionnellement, ajouter un bouton pour attaquer ou utiliser un objet
            g.setColor(Color.GREEN);
            g.fillRect(WINDOW_WIDTH - 200, WINDOW_HEIGHT - 100, 180, 40);
            g.setColor(Color.BLACK);
            g.drawString("Attaquer", WINDOW_WIDTH - 180, WINDOW_HEIGHT - 80);
        });
    }

}
