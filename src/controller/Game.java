package controller;

import java.awt.Color;

import com.github.forax.zen.Application;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import modelisation.*;

public class Game {
	private final Hero hero;
	private final Donjon donjon;
	private static int WINDOW_WIDTH;
    private static int WINDOW_HEIGHT;
    private static final int ROOM_SIZE = 100;
    private static final int DONJON_WIDTH = 5;
    private static final int DONJON_HEIGHT = 5;

    private static int selectedX = 0;
    private static int selectedY = 0;
	
	public Game() {
		hero=new Hero();
		donjon=Generation.genererDonjon();
	}
	
	public void RefreshPosition() {}
	
	public void afficheDonjon() {}
	
	public void gererClique() {}
	
	public void start() {
		Application.run(Color.BLACK, context -> {
            var screenInfo = context.getScreenInfo();
            WINDOW_WIDTH = screenInfo.width();
            WINDOW_HEIGHT = screenInfo.height();

            // Boucle principale du jeu
            while (true) {
            	Event e = context.pollEvent();
            	
            	switch(e) {
            	case KeyboardEvent _ -> {
            		//gererDeplacement(e);
                    //gererSelection(e);
            	}
            	
            	case PointerEvent _ -> {
            		gererClique(e);
            	}
            	
            	default -> {}
            	
            	
            	
            	}
            	
                /*Event Event = context.pollEvent();
                if (Event != null && Event instanceof KeyboardEvent k) {
                    gererDeplacement(k);
                    gererSelection(k);
                }
                
                if (Event != null && Event instanceof PointerEvent p) {
                	if(p.action() == PointerEvent.Action.POINTER_DOWN) {
                		interagirAvecSalle(p);
                }}*/
                
                context.renderFrame(Game::afficherDonjon);
                
            }
        });
    }
	}
	
}
