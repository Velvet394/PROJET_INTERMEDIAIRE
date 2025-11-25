package controller;

public class TestZen implements GameView{
	private SimpleGameData gameData;   // Données du jeu (modèle)
    private ZenCanvas canvas;          // Le canevas Zen pour dessiner

    // Constructeur
    public SimpleGameView(SimpleGameData gameData) {
        this.gameData = gameData;
        this.canvas = new ZenCanvas(800, 600);  // Taille de la fenêtre (800x600)
        this.canvas.setBackgroundColor(Color.WHITE);  // Fond de la fenêtre
        this.canvas.setFont(new Font("Arial", Font.PLAIN, 14));  // Police
    }

    // Méthode pour dessiner la carte du donjon
    public void drawMap() {
        Etage etage = gameData.getCurrentEtage();  // Récupérer l'étage actuel du donjon
        
        for (int y = 0; y < etage.getHeight(); y++) {
            for (int x = 0; x < etage.getWidth(); x++) {
                Coord coord = new Coord(x, y);  // Coordonnées de la salle
                Room room = etage.getSalle(coord);  // Récupérer la salle à cette coordonnée

                // Afficher la salle selon son type (ex : ennemi, trésor, etc.)
                if (room == null) {
                    canvas.drawRectangle(x * 60, y * 60, 60, 60, Color.GRAY);  // Mur
                } else {
                    switch (room.getType()) {
                        case ENEMY -> canvas.drawRectangle(x * 60, y * 60, 60, 60, Color.RED); // Ennemi
                        case TREASURE -> canvas.drawRectangle(x * 60, y * 60, 60, 60, Color.YELLOW); // Trésor
                        case MERCHANT -> canvas.drawRectangle(x * 60, y * 60, 60, 60, Color.GREEN); // Marchand
                        default -> canvas.drawRectangle(x * 60, y * 60, 60, 60, Color.CYAN); // Autre
                    }
                }
            }
        }
    }

    // Méthode pour afficher les informations sur le héros (ex : PV)
    public void drawHeroInfo() {
        canvas.setColor(Color.BLACK);
        canvas.drawText(10, 20, "HP: " + gameData.getHeroHP());  // Afficher les points de vie
    }

    // Méthode pour afficher le sac à dos
    public void drawBackpack() {
        int width = 5; // 5 colonnes dans le sac à dos
        int height = 3; // 3 lignes dans le sac
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Coord coord = new Coord(x, y);  // Coordonnée dans le sac à dos
                Item item = gameData.getBackpackItem(coord);  // Récupérer l'objet dans cette case
                if (item != null) {
                    // Afficher le nom de l'objet dans cette case du sac
                    canvas.drawText(x * 60 + 10, y * 60 + 30, item.getName());
                } else {
                    // Afficher une case vide dans le sac
                    canvas.drawRectangle(x * 60, y * 60, 60, 60, Color.LIGHT_GRAY);
                }
            }
        }
    }

    // Méthode pour mettre à jour l'affichage
    public void updateView() {
        canvas.clear();  // Effacer l'écran
        drawMap();       // Dessiner la carte du donjon
        drawHeroInfo();  // Afficher les informations du héros
        drawBackpack();  // Afficher le sac à dos
        canvas.refresh();  // Rafraîchir l'affichage
    }

    // Méthode pour gérer les événements clavier
    public void handleKeyEvent(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            gameData.moveHero(Direction.UP);  // Déplacer le héros vers le haut
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            gameData.moveHero(Direction.DOWN);  // Déplacer le héros vers le bas
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            gameData.moveHero(Direction.LEFT);  // Déplacer le héros vers la gauche
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            gameData.moveHero(Direction.RIGHT);  // Déplacer le héros vers la droite
        }
    }
}