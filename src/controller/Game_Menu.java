package controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;
import com.github.forax.zen.KeyboardEvent.Key;

public class Game_Menu implements Ecran {
	
	private final Game game;
	private static int WINDOW_WIDTH;
	private static int WINDOW_HEIGHT;
	
	private static final String[] MENU_OPTIONS = {
		      "Nouvelle Partie", "Charger Partie", "Options", "Quitter"
		  };
	
	private static int hoveredOption = 0;
	
	public Game_Menu(Game g) {
		game=g;
		WINDOW_HEIGHT=Game.windowHeight();
	    WINDOW_WIDTH=Game.windowWidth();
	}
	
	@Override
	public void render(Graphics2D g) {
	    if (ImagesBase.back != null) {
	      g.drawImage(ImagesBase.back, 0, 0, Game.windowWidth(), Game.windowHeight(), null);
	    } else {
	      g.setColor(Color.BLACK);
	      g.fillRect(0, 0, Game.windowWidth(), Game.windowHeight());
	    }

	    Font fontTitre = new Font("Arial", Font.BOLD, 40);
	    g.setFont(fontTitre);
	    String bienvenueText = "Bienvenue dans BackPackHero";
	    int bienvenueX = (Game.windowWidth() - g.getFontMetrics().stringWidth(bienvenueText)) / 2;
	    int bienvenueY = 100;
	    g.setColor(Color.WHITE);
	    g.drawString(bienvenueText, bienvenueX, bienvenueY);

	    Font fontOptions = new Font("Arial", Font.PLAIN, 30);
	    g.setFont(fontOptions);

	    int optionHeight = 50;
	    int totalMenuHeight = MENU_OPTIONS.length * optionHeight;
	    int startY = (Game.windowHeight() - totalMenuHeight) / 2;

	    for (int i = 0; i < MENU_OPTIONS.length; i++) {
	      int optionY = startY + i * optionHeight;
	      if (i == hoveredOption) {
	        g.setColor(Color.YELLOW);
	      } else {
	        g.setColor(Color.WHITE);
	      }
	      int optionWidth = g.getFontMetrics().stringWidth(MENU_OPTIONS[i]);
	      int optionX = (Game.windowWidth() - optionWidth) / 2;
	      g.drawString(MENU_OPTIONS[i], optionX, optionY);
	    }
	  }
	
	@Override
	public void handleKeyboard(KeyboardEvent event) {
	    if (event.key() == Key.UP) {
	      hoveredOption = (hoveredOption > 0) ? hoveredOption - 1 : MENU_OPTIONS.length - 1;
	    } else if (event.key() == Key.DOWN) {
	      hoveredOption = (hoveredOption < MENU_OPTIONS.length - 1) ? hoveredOption + 1 : 0;
	    } else if (event.key() == Key.SPACE) {
	      executeMenuOption(hoveredOption);
	    }
	  }
	
	@Override
	public void gererClique(PointerEvent pointerEvent) {
	    int mouseX = pointerEvent.location().x();
	    int mouseY = pointerEvent.location().y();

	    int optionHeight = 50;
	    int totalMenuHeight = MENU_OPTIONS.length * optionHeight;
	    int startY = (Game.windowHeight() - totalMenuHeight) / 2;

	    int hoverIndex = -1;
	    for (int i = 0; i < MENU_OPTIONS.length; i++) {
	      int optionY = startY + i * optionHeight;
	      int optionWidth = 200;
	      int optionX = (Game.windowWidth() - optionWidth) / 2;

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
	
	public void executeMenuOption(int index) {
	    switch (index) {
	      case 0 -> game.goToDonjon();
	      case 1 -> System.out.println("Charger partie (à implémenter)");
	      case 2 -> System.out.println("Options (à implémenter)");
	      case 3 -> System.exit(0);
	      default -> { /* rien */ }
	    }
	  }
	
}
