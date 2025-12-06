package App;

import com.github.forax.zen.Application;
import com.github.forax.zen.PointerEvent;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.KeyboardEvent.Key;

import modelisation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.List;

public class BackpachHeroApp {

  // --- Écrans possibles ---
  private enum Screen {
    MENU,
    DUNGEON,
    COMBAT,
    MERCHANT,   // Marchand
    HEALER,     // Guérisseur
    TREASURE    // Trésor
  }

  // --- État global de l'application ---
  private static Screen currentScreen = Screen.MENU;

  // Menu
  private static final String[] MENU_OPTIONS = {
      "Nouvelle Partie", "Charger Partie", "Options", "Quitter"
  };
  private static int hoveredOption = 0;
  private static BufferedImage backgroundImage;
  private static BufferedImage heroSprite;

  // Fenêtre
  private static int WINDOW_WIDTH;
  private static int WINDOW_HEIGHT;

  // Modèle du jeu
  private static Hero hero;
  private static Donjon donjon;
  private static int currentEtape = 0;
  private static Coord currentCoord = new Coord(0, 0);  // position "logique" du héros

  // Combat courant
  private static Combat currentCombat;
  private static CombatState combatState = CombatState.HERO_TURN;

  // Marchand courant
  public static Enter_Market currentMarket = null;

  // Guérisseur (pour affichage de la quantité soignée)
  private static Enter_Healer currentHealer = null;
  private static int lastHealAmount = 0;

  public static void main(String[] args) {
    // Image de fond du menu
    try {
      backgroundImage = ImageIO.read(new File("./data/backpack-hero-1nqjl.jpg"));
    } catch (IOException e) {
      e.printStackTrace();
      backgroundImage = null;
    }

    // Sprite du héros
    try {
      heroSprite = ImageIO.read(new File("./data/Satchel_Idle_Gif.gif"));
    } catch (IOException e) {
      e.printStackTrace();
      heroSprite = null;
    }

    // Modèle
    hero = new Hero();
    donjon = Generation.genererDonjon();

    Application.run(Color.BLACK, context -> {
      var screenInfo = context.getScreenInfo();
      WINDOW_WIDTH = screenInfo.width();
      WINDOW_HEIGHT = screenInfo.height();

      while (true) {
        var event = context.pollEvent();
        if (event != null) {
          if (event instanceof PointerEvent pointerEvent) {
            handlePointer(pointerEvent);
          } else if (event instanceof KeyboardEvent keyboardEvent) {
            handleKeyboard(keyboardEvent);
          }
        }

        context.renderFrame(BackpachHeroApp::render);
      }
    });
  }

  // =================== RENDER GLOBAL ===================

  private static void render(Graphics2D g) {
    switch (currentScreen) {
      case MENU -> renderMenu(g);
      case DUNGEON -> renderDungeon(g);
      case COMBAT -> renderCombat(g);
      case MERCHANT -> renderMerchant(g);
      case HEALER -> renderHealer(g);
      case TREASURE -> renderTreasure(g);
    }
  }

  // =================== MENU ===================

  private static void renderMenu(Graphics2D g) {
    if (backgroundImage != null) {
      g.drawImage(backgroundImage, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
    } else {
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    Font fontTitre = new Font("Arial", Font.BOLD, 40);
    g.setFont(fontTitre);
    String bienvenueText = "Bienvenue dans BackPackHero";
    int bienvenueX = (WINDOW_WIDTH - g.getFontMetrics().stringWidth(bienvenueText)) / 2;
    int bienvenueY = 100;
    g.setColor(Color.WHITE);
    g.drawString(bienvenueText, bienvenueX, bienvenueY);

    Font fontOptions = new Font("Arial", Font.PLAIN, 30);
    g.setFont(fontOptions);

    int optionHeight = 50;
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

  private static void handleMenuKeyboard(KeyboardEvent event) {
    if (event.key() == Key.UP) {
      hoveredOption = (hoveredOption > 0) ? hoveredOption - 1 : MENU_OPTIONS.length - 1;
    } else if (event.key() == Key.DOWN) {
      hoveredOption = (hoveredOption < MENU_OPTIONS.length - 1) ? hoveredOption + 1 : 0;
    } else if (event.key() == Key.SPACE) {
      executeMenuOption(hoveredOption);
    }
  }

  private static void handleMenuPointer(PointerEvent pointerEvent) {
    int mouseX = pointerEvent.location().x();
    int mouseY = pointerEvent.location().y();

    int optionHeight = 50;
    int totalMenuHeight = MENU_OPTIONS.length * optionHeight;
    int startY = (WINDOW_HEIGHT - totalMenuHeight) / 2;

    int hoverIndex = -1;
    for (int i = 0; i < MENU_OPTIONS.length; i++) {
      int optionY = startY + i * optionHeight;
      int optionWidth = 200;
      int optionX = (WINDOW_WIDTH - optionWidth) / 2;

      if (mouseX >= optionX && mouseX <= optionX + optionWidth &&
          mouseY >= optionY - 30 && mouseY <= optionY + 10) {
        hoverIndex = i;
        break;
      }
    }

    hoveredOption = (hoverIndex == -1) ? hoveredOption : hoverIndex;

    if (pointerEvent.action() == PointerEvent.Action.POINTER_DOWN && hoverIndex != -1) {
      executeMenuOption(hoverIndex);
    }
  }

  private static void executeMenuOption(int index) {
    switch (index) {
      case 0 -> startNewGame();
      case 1 -> System.out.println("Charger partie (à implémenter)");
      case 2 -> System.out.println("Options (à implémenter)");
      case 3 -> System.exit(0);
      default -> { /* rien */ }
    }
  }

  // =================== NOUVELLE PARTIE ===================

  private static void startNewGame() {
    System.out.println("Lancement d'une nouvelle partie...");
    hero = new Hero();
    hero.addOr(20); // un peu d'or au début
    donjon = Generation.genererDonjon();
    currentEtape = 0;
    currentCoord = new Coord(0, 0);
    currentCombat = null;
    currentMarket = null;
    currentHealer = null;
    lastHealAmount = 0;
    currentScreen = Screen.DUNGEON;

    // synchroniser la position du héros dans le modèle
    hero.moveTo(currentEtape, currentCoord);

    initStartingBackpack();

    Etape etape = donjon.getEtape(currentEtape);
    if (etape != null) {
      Room startRoom = etape.getSalle(currentCoord);
      if (startRoom != null && !startRoom.estVisite()) {
        startRoom.visiter(hero);
      }
      etape.RefreshListNeighbor();
    }
  }

  private static void initStartingBackpack() {
    Backpack sac = hero.getBackpack();

    Weapon melee = new Weapon(
        "Epee", 1, 0, false,
        List.of(new Coord(0, 0)),
        List.of(new Effect_Atteck(6))
    , "./weapons/durantal.png");
    melee.translate(new Coord(2, 1));
    sac.placer(melee);

    Weapon ranged = new Weapon(
        "Arc", 1, 0, false,
        List.of(new Coord(0, 0), new Coord(1, 0)),
        List.of(new Effect_Atteck(4))
    , "./weapons/durantal.png");
    ranged.translate(new Coord(0, 0));
    sac.placer(ranged);

    Weapon armor = new Weapon(
        "Armure", 1, 0, false,
        List.of(new Coord(0, 0), new Coord(0, 1)),
        List.of(new Effect_Def(6))
    , "./weapons/shield.png");
    armor.translate(new Coord(3, 0));
    sac.placer(armor);

    Weapon shield = new Weapon(
        "Bouclier", 1, 0, false,
        List.of(new Coord(0, 0)),
        List.of(new Effect_Def(7))
    , "./weapons/shield.png");
    shield.translate(new Coord(0, 2));
    sac.placer(shield);

    Weapon magic = new Weapon(
        "Baton", 1, 5, false,
        List.of(new Coord(0, 0)),
        List.of(new Effect_Atteck(10))
    , "./weapons/durantal.png");
    magic.translate(new Coord(4, 0));
    sac.placer(magic);

    Weapon manaStone = new Weapon(
        "Mana", 0, 0, true,
        List.of(new Coord(0, 0)),
        List.of(new Effect_Charge(5))
    , "./weapons/manastone.png");
    manaStone.translate(new Coord(4, 1));
    sac.placer(manaStone);

    Gold gold = new Gold(5);
    gold.translate(new Coord(4, 2));
    sac.placer(gold);
  }

  // =================== DONJON ===================

  private static void renderDungeon(Graphics2D g) {
    g.setColor(new Color(20, 20, 20));
    g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.PLAIN, 18));
    String heroInfo = "HP: " + hero.hp() + "/" + hero.maxHp()
        + "  |  Mana: " + hero.mana() + "/" + hero.maxMana()
        + "  |  Or: " + hero.or();
    g.drawString(heroInfo, 20, 30);

    g.drawString("Étape : " + currentEtape + " (ESC pour retourner au menu)", 20, 55);

    Etape etape = donjon.getEtape(currentEtape);
    if (etape == null) {
      g.drawString("Aucune étape trouvée", 20, 80);
      return;
    }

    Map<Coord, Room> salles = etape.getSalles();
    if (salles.isEmpty()) {
      g.drawString("Aucune salle dans cette étape", 20, 80);
      return;
    }

    int maxX = 0;
    int maxY = 0;
    for (Coord c : salles.keySet()) {
      if (c.x() > maxX) maxX = c.x();
      if (c.y() > maxY) maxY = c.y();
    }
    int cols = maxX + 1;
    int rows = maxY + 1;

    int marginTop = 100;
    int marginSide = 50;
    int usableWidth = WINDOW_WIDTH - 2 * marginSide;
    int usableHeight = WINDOW_HEIGHT - marginTop - 50;

    int cellWidth = usableWidth / cols;
    int cellHeight = usableHeight / rows;

    var neighbors = etape.sallesNeighborNonVisite();

    for (Map.Entry<Coord, Room> entry : salles.entrySet()) {
      Coord c = entry.getKey();
      Room room = entry.getValue();

      int x = marginSide + c.x() * cellWidth;
      int y = marginTop + c.y() * cellHeight;

      Color fill;
      if (c.equals(currentCoord)) {
        fill = Color.BLUE;
      } else if (room.estVisite()) {
        fill = Color.DARK_GRAY;
      } else if (neighbors.containsKey(c)) {
        fill = Color.ORANGE;
      } else {
        fill = Color.BLACK;
      }

      g.setColor(fill);
      g.fillRect(x + 2, y + 2, cellWidth - 4, cellHeight - 4);

      g.setColor(Color.GRAY);
      g.drawRect(x + 2, y + 2, cellWidth - 4, cellHeight - 4);

      g.setColor(Color.WHITE);
      g.setFont(new Font("Arial", Font.BOLD, 14));
      String letter = typeToLetter(room.getType());
      g.drawString(letter, x + cellWidth / 2 - 4, y + cellHeight / 2);
    }
  }

  private static String typeToLetter(RoomType type) {
    return switch (type) {
      case ENEMY -> "E";
      case TREASURE -> "T";
      case MERCHANT -> "M";
      case HEALER -> "H";
      case EXIT -> "X";
      case EMPTY, HALLWAY, GATE, EVENT -> ".";
    };
  }

  private static void handleDungeonKeyboard(KeyboardEvent event) {
    if (event.key() == Key.ESCAPE) {
      currentScreen = Screen.MENU;
      return;
    }

    Coord dest = null;
    if (event.key() == Key.UP) {
      dest = new Coord(currentCoord.x(), currentCoord.y() - 1);
    } else if (event.key() == Key.DOWN) {
      dest = new Coord(currentCoord.x(), currentCoord.y() + 1);
    } else if (event.key() == Key.LEFT) {
      dest = new Coord(currentCoord.x() - 1, currentCoord.y());
    } else if (event.key() == Key.RIGHT) {
      dest = new Coord(currentCoord.x() + 1, currentCoord.y());
    }

    if (dest != null) {
      moveHeroTo(dest);
    }
  }

  private static void handleDungeonPointer(PointerEvent pointerEvent) {
    if (pointerEvent.action() != PointerEvent.Action.POINTER_DOWN) {
      return;
    }

    Etape etape = donjon.getEtape(currentEtape);
    if (etape == null) {
      return;
    }

    Map<Coord, Room> salles = etape.getSalles();
    if (salles.isEmpty()) {
      return;
    }

    int maxX = 0;
    int maxY = 0;
    for (Coord c : salles.keySet()) {
      if (c.x() > maxX) maxX = c.x();
      if (c.y() > maxY) maxY = c.y();
    }
    int cols = maxX + 1;
    int rows = maxY + 1;

    int marginTop = 100;
    int marginSide = 50;
    int usableWidth = WINDOW_WIDTH - 2 * marginSide;
    int usableHeight = WINDOW_HEIGHT - marginTop - 50;

    int cellWidth = usableWidth / cols;
    int cellHeight = usableHeight / rows;

    int mouseX = pointerEvent.location().x();
    int mouseY = pointerEvent.location().y();

    int col = (mouseX - marginSide) / cellWidth;
    int row = (mouseY - marginTop) / cellHeight;
    if (col < 0 || row < 0 || col >= cols || row >= rows) {
      return;
    }

    Coord clickedCoord = new Coord(col, row);
    moveHeroTo(clickedCoord);
  }

  // =================== DÉPLACEMENT LOGIQUE DU HÉROS ===================

  private static void moveHeroTo(Coord dest) {
    Etape etape = donjon.getEtape(currentEtape);
    if (etape == null) {
      return;
    }

    Room room = etape.getSalle(dest);
    if (room == null) {
      return;
    }

    int dx = Math.abs(dest.x() - currentCoord.x());
    int dy = Math.abs(dest.y() - currentCoord.y());
    if (dx + dy != 1) {
      return;
    }

    RoomType type = room.getType();
    var enter = room.getEnter();

    int hpBefore = hero.hp();
    boolean isHealerRoom = enter instanceof Enter_Healer;

    // appliquer les effets d'entrée (combat, marchand, healer, trésor, etc.)
    room.visiter(hero);
    currentCoord = dest;
    // synchroniser aussi la position dans le modèle
    hero.moveTo(currentEtape, currentCoord);

    etape.RefreshListNeighbor();

    // COMBAT
    if (enter instanceof Enter_Combat ec) {
      currentCombat = ec.getcombat();
      if (currentCombat != null) {
        hero.startCombat();
        combatState = CombatState.HERO_TURN;
        currentScreen = Screen.COMBAT;
      }
      return;
    }

    // MARCHAND
    if (enter instanceof Enter_Market em) {
      currentMarket = em;
      currentScreen = Screen.MERCHANT;
      return;
    }

    // GUERISSEUR
    if (isHealerRoom) {
      currentHealer = (Enter_Healer) enter;
      lastHealAmount = Math.max(0, hero.hp() - hpBefore);
      currentScreen = Screen.HEALER;
      return;
    }

    // TRESOR
    if (enter instanceof Enter_Tresor) {
      currentScreen = Screen.TREASURE;
      return;
    }

    // SORTIE -> PASSAGE A L'ÉTAGE SUIVANT
    if (type == RoomType.EXIT) {
      // si on n'est pas encore au dernier étage
      if (currentEtape < Donjon.maxEtape) {
        // déplacer le donjon sur l'étage suivant (logique interne)
        donjon.moveEtape();

        // index logique d'étage + coordonnées logiques
        currentEtape++;
        currentCoord = new Coord(0, 0);

        // synchroniser aussi la position du héros dans le modèle
        hero.moveTo(currentEtape, currentCoord);

        // on nettoie les états de salle spécifiques
        currentCombat = null;
        currentMarket = null;
        currentHealer = null;
        lastHealAmount = 0;

        Etape nouvelleEtape = donjon.getEtape(currentEtape);
        if (nouvelleEtape != null) {
          Room startRoom = nouvelleEtape.getSalle(currentCoord);
          if (startRoom != null && !startRoom.estVisite()) {
            startRoom.visiter(hero);
          }
          nouvelleEtape.RefreshListNeighbor();
        }
        System.out.println("Passage à l'étape " + currentEtape);
        currentScreen = Screen.DUNGEON;
      } else {
        // dernier étage terminé
        System.out.println("Donjon terminé !");
        currentScreen = Screen.MENU;
      }
      return;
    }

    // Salle normale / couloir
    currentScreen = Screen.DUNGEON;

    System.out.println("Salle visitée : " + dest
        + " | Type = " + type
        + " | HP héros = " + hero.hp() + "/" + hero.maxHp());
  }

  // =================== ECRAN COMBAT ===================

  private static void renderCombat(Graphics2D g) {
    g.setColor(new Color(10, 10, 30));
    g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

    if (currentCombat == null) {
      g.setColor(Color.WHITE);
      g.drawString("Aucun combat en cours", 50, 50);
      return;
    }

    Hero h = hero;
    List<Ennemi> ennemisVivants = currentCombat.getEnnemis().stream()
        .filter(e -> !e.estMort())
        .toList();

    Ennemi ennemi = ennemisVivants.isEmpty() ? null : ennemisVivants.get(0);

    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.PLAIN, 16));
    g.drawString("COMBAT - Tour du " +
        (combatState == CombatState.HERO_TURN ? "HÉROS" : "ENNEMI"), 20, 30);

    g.drawString("HP " + h.hp() + "/" + h.maxHp()
        + "   Mana " + h.mana() + "/" + h.maxMana()
        + "   Énergie " + h.energie()
        + "   Or " + h.or(), 20, 50);

    if (ennemi != null) {
      g.drawString("Ennemi : " + ennemi.name()
          + "  HP " + ennemi.hp(), 20, 70);
    }

    // Sac à dos en haut
    drawBackpack(g);

    // Héros en bas gauche
    int floorMargin = 110;
    int heroBoxSize = 90;
    int heroBoxX = 80;
    int heroBoxY = WINDOW_HEIGHT - floorMargin - heroBoxSize;

    g.setColor(new Color(30, 30, 60));
    g.fillRect(heroBoxX, heroBoxY, heroBoxSize, heroBoxSize);
    g.setColor(Color.WHITE);
    g.drawRect(heroBoxX, heroBoxY, heroBoxSize, heroBoxSize);

    if (heroSprite != null) {
      int imgW = heroSprite.getWidth();
      int imgH = heroSprite.getHeight();
      double scale = Math.min(
          (double) heroBoxSize / imgW,
          (double) heroBoxSize / imgH
      );
      int drawW = (int) (imgW * scale);
      int drawH = (int) (imgH * scale);

      int drawX = heroBoxX + (heroBoxSize - drawW) / 2;
      int drawY = heroBoxY + (heroBoxSize - drawH) / 2;

      g.drawImage(heroSprite, drawX, drawY, drawW, drawH, null);
    } else {
      g.setColor(Color.BLUE);
      g.fillRect(heroBoxX, heroBoxY, heroBoxSize, heroBoxSize);
      g.setColor(Color.WHITE);
      g.drawString("H", heroBoxX + heroBoxSize / 2 - 4, heroBoxY + heroBoxSize / 2);
    }

    g.setColor(Color.WHITE);
    g.drawString(h.hp() + "/" + h.maxHp(), heroBoxX, heroBoxY + heroBoxSize + 20);

    // Ennemi en bas droite
    int enemyBoxSize = 90;
    int enemyBoxX = WINDOW_WIDTH - 80 - enemyBoxSize;
    int enemyBoxY = heroBoxY;

    if (ennemi != null) {
      g.setColor(Color.RED);
      g.fillRect(enemyBoxX, enemyBoxY, enemyBoxSize, enemyBoxSize);
      g.setColor(Color.WHITE);
      g.drawRect(enemyBoxX, enemyBoxY, enemyBoxSize, enemyBoxSize);
      g.drawString("E", enemyBoxX + enemyBoxSize / 2 - 4, enemyBoxY + enemyBoxSize / 2);
      g.drawString(ennemi.hp() + " HP", enemyBoxX, enemyBoxY + enemyBoxSize + 20);
    }

    int btnWidth = 150;
    int btnHeight = 40;
    int btnX = (WINDOW_WIDTH - btnWidth) / 2;
    int btnY = WINDOW_HEIGHT - 80;

    g.setColor(Color.DARK_GRAY);
    g.fillRect(btnX, btnY, btnWidth, btnHeight);
    g.setColor(Color.WHITE);
    g.drawRect(btnX, btnY, btnWidth, btnHeight);
    g.drawString("Fin du tour", btnX + 25, btnY + 25);
  }

  // --- Backpack visuel agrandi, en haut au centre ---
  private static void drawBackpack(Graphics2D g) {
    Backpack sac = hero.getBackpack();
    var contenu = sac.contenu();

    int cols = 5;
    int rows = 3;
    int cellSize = 60;

    int bagWidth = 420;
    int bagHeight = 260;
    int bagX = (WINDOW_WIDTH - bagWidth) / 2;
    int bagY = 80;

    g.setColor(new Color(100, 60, 40));
    g.fillRoundRect(bagX, bagY, bagWidth, bagHeight, 40, 40);
    g.setColor(Color.DARK_GRAY);
    g.drawRoundRect(bagX, bagY, bagWidth, bagHeight, 40, 40);

    int gridWidth = cols * cellSize;
    int gridHeight = rows * cellSize;

    int startX = bagX + (bagWidth - gridWidth) / 2;
    int startY = bagY + (bagHeight - gridHeight) / 2;

    g.setFont(new Font("Arial", Font.PLAIN, 12));

    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < cols; x++) {
        int cellX = startX + x * cellSize;
        int cellY = startY + y * cellSize;

        g.setColor(Color.GRAY);
        g.drawRect(cellX, cellY, cellSize, cellSize);

        Item item = contenu.get(new Coord(x, y));
        if (item != null) {
          g.setColor(new Color(220, 220, 220));
          g.fillRect(cellX + 1, cellY + 1, cellSize - 2, cellSize - 2);
          g.setColor(Color.BLACK);

          String label;
          if (item instanceof Weapon w) {
            String name = w.name();
            label = name.substring(0, Math.min(3, name.length()));
          } else if (item instanceof ItemMaldiction) {
            label = "MAL";
          } else if (item instanceof Gold) {
            label = "OR";
          } else {
            label = "?";
          }
          g.drawString(label, cellX + cellSize / 2 - 10, cellY + cellSize / 2);
        }
      }
    }
  }

  // =================== PRIX DES OBJETS (MARCHAND) ===================

  private static int priceFor(Item item) {
    if (item instanceof Weapon w) {
      return switch (w.name()) {
        case "Epee"     -> 8;
        case "Arc"      -> 7;
        case "Armure"   -> 10;
        case "Bouclier" -> 6;
        case "Baton"    -> 12;
        case "Mana"     -> 5;
        default         -> 7;
      };
    } else if (item instanceof ItemMaldiction) {
      return 1;
    } else if (item instanceof Gold) {
      return -1;   // non achetable
    } else {
      return 5;
    }
  }

  // =================== ECRAN MARCHAND (avec backpack + héros + marchand) ===================

  private static void renderMerchant(Graphics2D g) {
    g.setColor(new Color(20, 20, 40));
    g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

    // Infos en haut
    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.BOLD, 24));
    g.drawString("Marchand", 20, 40);

    g.setFont(new Font("Arial", Font.PLAIN, 18));
    g.drawString("Or du héros : " + hero.or(), 20, 70);
    g.drawString("Cliquez sur un objet pour l'acheter (si vous avez assez d'or).", 20, 95);

    // Sac à dos en haut au centre (comme en combat)
    drawBackpack(g);

    // Héros en bas gauche (même rendu que combat)
    int floorMargin = 110;
    int heroBoxSize = 90;
    int heroBoxX = 80;
    int heroBoxY = WINDOW_HEIGHT - floorMargin - heroBoxSize;

    g.setColor(new Color(30, 30, 60));
    g.fillRect(heroBoxX, heroBoxY, heroBoxSize, heroBoxSize);
    g.setColor(Color.WHITE);
    g.drawRect(heroBoxX, heroBoxY, heroBoxSize, heroBoxSize);

    if (heroSprite != null) {
      int imgW = heroSprite.getWidth();
      int imgH = heroSprite.getHeight();
      double scale = Math.min(
          (double) heroBoxSize / imgW,
          (double) heroBoxSize / imgH
      );
      int drawW = (int) (imgW * scale);
      int drawH = (int) (imgH * scale);

      int drawX = heroBoxX + (heroBoxSize - drawW) / 2;
      int drawY = heroBoxY + (heroBoxSize - drawH) / 2;

      g.drawImage(heroSprite, drawX, drawY, drawW, drawH, null);
    } else {
      g.setColor(Color.BLUE);
      g.fillRect(heroBoxX, heroBoxY, heroBoxSize, heroBoxSize);
      g.setColor(Color.WHITE);
      g.drawString("H", heroBoxX + heroBoxSize / 2 - 4, heroBoxY + heroBoxSize / 2);
    }

    g.setColor(Color.WHITE);
    g.drawString("HP: " + hero.hp() + "/" + hero.maxHp(), heroBoxX, heroBoxY + heroBoxSize + 20);

    // Marchand en bas droite (même style que ennemi, mais avec 'Shop')
    int merchantBoxSize = 90;
    int merchantBoxX = WINDOW_WIDTH - 80 - merchantBoxSize;
    int merchantBoxY = heroBoxY;

    g.setColor(new Color(90, 60, 20));
    g.fillRect(merchantBoxX, merchantBoxY, merchantBoxSize, merchantBoxSize);
    g.setColor(Color.WHITE);
    g.drawRect(merchantBoxX, merchantBoxY, merchantBoxSize, merchantBoxSize);
    g.drawString("Shop", merchantBoxX + 15, merchantBoxY + merchantBoxSize / 2);

    // Liste des objets à vendre, sous le backpack
    int startX = 60;
    int startY = 360;  // en dessous du sac
    int itemHeight = 60;
    int itemWidth = WINDOW_WIDTH - 2 * startX;
    int gap = 10;

    g.setFont(new Font("Arial", Font.PLAIN, 16));

    if (currentMarket == null || currentMarket.items.isEmpty()) {
      g.drawString("Le marchand n'a plus rien à vendre.", startX, startY);
    } else {
      for (int i = 0; i < currentMarket.items.size(); i++) {
        Item item = currentMarket.items.get(i);
        int y = startY + i * (itemHeight + gap);

        g.setColor(new Color(60, 60, 90));
        g.fillRoundRect(startX, y, itemWidth, itemHeight, 10, 10);
        g.setColor(Color.WHITE);
        g.drawRoundRect(startX, y, itemWidth, itemHeight, 10, 10);

        int price = priceFor(item);

        String name;
        if (item instanceof Weapon w) {
          name = "Arme : " + w.name();
        } else if (item instanceof ItemMaldiction) {
          name = "Malediction";
        } else if (item instanceof Gold) {
          name = "Or";
        } else {
          name = "Item";
        }

        String line;
        if (price < 0) {
          line = (i + 1) + " - " + name + " (non achetable)";
        } else {
          line = (i + 1) + " - " + name + "  [" + price + " or]";
        }

        g.drawString(line, startX + 10, y + 25);
      }
    }

    // Bouton "Retour" en bas à droite
    int btnWidth = 150;
    int btnHeight = 40;
    int btnX = WINDOW_WIDTH - btnWidth - 40;
    int btnY = WINDOW_HEIGHT - btnHeight - 40;

    g.setColor(Color.DARK_GRAY);
    g.fillRect(btnX, btnY, btnWidth, btnHeight);
    g.setColor(Color.WHITE);
    g.drawRect(btnX, btnY, btnWidth, btnHeight);
    g.drawString("Retour", btnX + 45, btnY + 25);
  }

  private static void handleMerchantPointer(PointerEvent event) {
    if (event.action() != PointerEvent.Action.POINTER_DOWN) {
      return;
    }
    if (currentMarket == null) {
      currentScreen = Screen.DUNGEON;
      return;
    }

    int x = event.location().x();
    int y = event.location().y();

    // Bouton "Retour"
    int btnWidth = 150;
    int btnHeight = 40;
    int btnX = WINDOW_WIDTH - btnWidth - 40;
    int btnY = WINDOW_HEIGHT - btnHeight - 40;

    if (x >= btnX && x <= btnX + btnWidth && y >= btnY && y <= btnY + btnHeight) {
      currentScreen = Screen.DUNGEON;
      return;
    }

    if (currentMarket.items.isEmpty()) {
      return;
    }

    // Détection clic sur un item (coordonnées cohérentes avec renderMerchant)
    int startX = 60;
    int startY = 360;
    int itemHeight = 60;
    int itemWidth = WINDOW_WIDTH - 2 * startX;
    int gap = 10;

    for (int i = 0; i < currentMarket.items.size(); i++) {
      int boxY = startY + i * (itemHeight + gap);
      if (x >= startX && x <= startX + itemWidth &&
          y >= boxY && y <= boxY + itemHeight) {

        Item item = currentMarket.items.get(i);
        int price = priceFor(item);

        if (price < 0) {
          System.out.println("Cet objet ne peut pas être acheté.");
          return;
        }

        if (hero.or() < price) {
          System.out.println("Pas assez d'or pour acheter cet objet.");
          return;
        }

        hero.addOr(-price);
        hero.getBackpack().placer(item);
        currentMarket.items.remove(i);

        System.out.println("Item acheté chez le marchand pour " + price +
            " or. Or restant : " + hero.or());
        return;
      }
    }
  }

  // =================== ECRAN GUERISSEUR ===================

  private static void renderHealer(Graphics2D g) {
    g.setColor(new Color(20, 40, 20));
    g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.BOLD, 28));
    g.drawString("Guérisseur", 60, 80);

    g.setFont(new Font("Arial", Font.PLAIN, 18));
    g.drawString("Le guérisseur vous a soigné.", 60, 120);
    g.drawString("Vous avez récupéré : " + lastHealAmount + " PV.", 60, 150);
    g.drawString("HP actuels : " + hero.hp() + "/" + hero.maxHp(), 60, 180);
    g.drawString("Appuyez sur ESPACE / ESC ou cliquez sur \"Continuer\" pour revenir au donjon.", 60, 210);

    // Bouton "Continuer"
    int btnWidth = 200;
    int btnHeight = 50;
    int btnX = (WINDOW_WIDTH - btnWidth) / 2;
    int btnY = WINDOW_HEIGHT - 120;

    g.setColor(new Color(30, 80, 30));
    g.fillRect(btnX, btnY, btnWidth, btnHeight);
    g.setColor(Color.WHITE);
    g.drawRect(btnX, btnY, btnWidth, btnHeight);
    g.drawString("Continuer", btnX + 55, btnY + 30);
  }

  private static void handleHealerPointer(PointerEvent event) {
    if (event.action() != PointerEvent.Action.POINTER_DOWN) return;

    int x = event.location().x();
    int y = event.location().y();

    int btnWidth = 200;
    int btnHeight = 50;
    int btnX = (WINDOW_WIDTH - btnWidth) / 2;
    int btnY = WINDOW_HEIGHT - 120;

    if (x >= btnX && x <= btnX + btnWidth && y >= btnY && y <= btnY + btnHeight) {
      currentScreen = Screen.DUNGEON;
    } else {
      currentScreen = Screen.DUNGEON;
    }
  }

  // =================== ECRAN TRESOR (avec backpack affiché) ===================

  private static void renderTreasure(Graphics2D g) {
    g.setColor(new Color(40, 30, 10));
    g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

    g.setColor(Color.YELLOW);
    g.setFont(new Font("Arial", Font.BOLD, 28));
    g.drawString("Trésor", 60, 60);

    // Afficher le backpack pour voir où vont les objets
    drawBackpack(g);

    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.PLAIN, 18));
    int textY = 360;
    g.drawString("Vous ouvrez un coffre ! Les nouveaux objets sont ajoutés dans votre sac.", 60, textY);
    g.drawString("Regardez le backpack au-dessus pour voir où ils se placent.", 60, textY + 30);
    g.drawString("Appuyez sur ESPACE / ESC ou cliquez sur \"OK\" pour revenir au donjon.", 60, textY + 60);

    // Bouton "OK"
    int btnWidth = 150;
    int btnHeight = 50;
    int btnX = (WINDOW_WIDTH - btnWidth) / 2;
    int btnY = WINDOW_HEIGHT - 120;

    g.setColor(new Color(120, 90, 20));
    g.fillRect(btnX, btnY, btnWidth, btnHeight);
    g.setColor(Color.WHITE);
    g.drawRect(btnX, btnY, btnWidth, btnHeight);
    g.drawString("OK", btnX + 60, btnY + 30);
  }

  private static void handleTreasurePointer(PointerEvent event) {
    if (event.action() != PointerEvent.Action.POINTER_DOWN) return;

    int x = event.location().x();
    int y = event.location().y();

    int btnWidth = 150;
    int btnHeight = 50;
    int btnX = (WINDOW_WIDTH - btnWidth) / 2;
    int btnY = WINDOW_HEIGHT - 120;

    if (x >= btnX && x <= btnX + btnWidth && y >= btnY && y <= btnY + btnHeight) {
      currentScreen = Screen.DUNGEON;
    } else {
      currentScreen = Screen.DUNGEON;
    }
  }

  // =================== GESTION DU COMBAT (clics) ===================

  private static void handleCombatPointer(PointerEvent event) {
    if (event.action() != PointerEvent.Action.POINTER_DOWN) {
      return;
    }
    if (currentCombat == null) {
      return;
    }

    int x = event.location().x();
    int y = event.location().y();

    int btnWidth = 150;
    int btnHeight = 40;
    int btnX = (WINDOW_WIDTH - btnWidth) / 2;
    int btnY = WINDOW_HEIGHT - 80;

    if (x >= btnX && x <= btnX + btnWidth && y >= btnY && y <= btnY + btnHeight) {
      if (combatState == CombatState.HERO_TURN) {
        enemyTurn();
      }
      return;
    }

    int cols = 5;
    int rows = 3;
    int cellSize = 60;

    int bagWidth = 420;
    int bagHeight = 260;
    int bagX = (WINDOW_WIDTH - bagWidth) / 2;
    int bagY = 80;

    int gridWidth = cols * cellSize;
    int gridHeight = rows * cellSize;

    int startX = bagX + (bagWidth - gridWidth) / 2;
    int startY = bagY + (bagHeight - gridHeight) / 2;

    if (x < startX || x > startX + gridWidth || y < startY || y > startY + gridHeight) {
      return;
    }

    int col = (x - startX) / cellSize;
    int row = (y - startY) / cellSize;

    Coord coord = new Coord(col, row);
    Item item = hero.getBackpack().contenu().get(coord);
    if (item instanceof Weapon w && combatState == CombatState.HERO_TURN) {
      List<Ennemi> ennemisVivants = currentCombat.getEnnemis().stream()
          .filter(e -> !e.estMort())
          .toList();
      if (ennemisVivants.isEmpty()) {
        endCombatVictory();
        return;
      }
      Ennemi cible = ennemisVivants.get(0);
      w.utiliser(hero, cible, currentCombat);

      int state = currentCombat.estCombatTermine();
      if (state == 2) {
        endCombatVictory();
      } else if (state == 3) {
        endCombatDefeat();
      }
    }
  }

  private static void enemyTurn() {
    if (currentCombat == null) return;

    combatState = CombatState.ENEMY_TURN;

    for (Ennemi e : currentCombat.getEnnemis()) {
      if (!e.estMort()) {
        e.Action();
        e.executerAction(hero);
      }
    }

    int state = currentCombat.estCombatTermine();
    if (state == 1) {
      hero.rechargerCombat();
      combatState = CombatState.HERO_TURN;
    } else if (state == 2) {
      endCombatVictory();
    } else if (state == 3) {
      endCombatDefeat();
    }
  }

  private static void endCombatVictory() {
    System.out.println("Victoire du héros !");
    currentCombat = null;
    currentScreen = Screen.DUNGEON;
  }

  private static void endCombatDefeat() {
    System.out.println("Le héros est mort... GAME OVER");
    currentCombat = null;
    currentScreen = Screen.MENU;
  }

  // =================== ROUTAGE GLOBAL DES EVENTS ===================

  private static void handleKeyboard(KeyboardEvent event) {
    switch (currentScreen) {
      case MENU -> handleMenuKeyboard(event);
      case DUNGEON -> handleDungeonKeyboard(event);
      case COMBAT -> {
        if (event.key() == Key.ESCAPE) {
          // pas de fuite pour l'instant
        }
      }
      case MERCHANT, HEALER, TREASURE -> {
        if (event.key() == Key.ESCAPE || event.key() == Key.SPACE) {
          currentScreen = Screen.DUNGEON;
        }
      }
    }
  }

  private static void handlePointer(PointerEvent event) {
    switch (currentScreen) {
      case MENU -> handleMenuPointer(event);
      case DUNGEON -> handleDungeonPointer(event);
      case COMBAT -> handleCombatPointer(event);
      case MERCHANT -> handleMerchantPointer(event);
      case HEALER -> handleHealerPointer(event);
      case TREASURE -> handleTreasurePointer(event);
    }
  }
}
