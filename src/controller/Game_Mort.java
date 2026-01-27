package controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Objects;

import com.github.forax.zen.PointerEvent;

public class Game_Mort implements Ecran {

	private final Game game;
	private static int WINDOW_WIDTH;
	private static int WINDOW_HEIGHT;
	
	public Game_Mort(Game g) {
		game=g;
		WINDOW_HEIGHT=Game.windowHeight();
	    WINDOW_WIDTH=Game.windowWidth();
	}
	
	public void render(Graphics2D g) {
		g.setColor(new Color(20, 20, 40));
	    g.fillRect(0, 0, Game.windowWidth(), Game.windowHeight());
	    g.setColor(Color.WHITE);
	    g.setFont(new Font("Arial", Font.BOLD, 24));
	    String d="DEAD";
	    g.drawString(d, (WINDOW_WIDTH-d.length())/2, (WINDOW_HEIGHT-d.length())/2);
	    //g.setColor(Color.WHITE);
	    //g.setFont(new Font("Arial", Font.BOLD, 24));
	    //String c="Clique pour quitter de jeu";
	    //g.drawString(c, (WINDOW_WIDTH-d.length())/2-20, (WINDOW_HEIGHT-d.length())/2);
	}
	
	public void gererClique(PointerEvent p) {
		Objects.requireNonNull(p);
	    if (p.action() != PointerEvent.Action.POINTER_DOWN) {
	      return;
	    }
	    int x = p.location().x();
	    int y = p.location().y();
	    
	    if(x>0&&y>0) {
	    	//game.goToMenu();
	    	System.exit(0);
	    }
	    }
}
