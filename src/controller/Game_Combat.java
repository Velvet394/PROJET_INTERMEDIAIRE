package controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.*;

import com.github.forax.zen.Application;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import modelisation.*;

import modelisation.*;

public class Game_Combat implements Ecran {
	
	private final Game game;
	
	private static final int GRID_WIDTH = 7;
    private static final int GRID_HEIGHT = 5;
    private static final int CELL_SIZE = 60;
    private static int WINDOW_WIDTH;
    private static int WINDOW_HEIGHT;
    private static final int ENNEMI_SIZE=90;
    private CombatState state;
    
    private final Combat combat;
    public Button tourTermine;
    
    private int ptr=0;
    
    public Game_Combat(Combat c, Game g) {
    	Objects.requireNonNull(g);
    	Objects.requireNonNull(c);
    	combat=c;
    	game=g;
    	int buttonWidth = 120;
	    int buttonHeight = 40;
	    int margin = 20;
	    WINDOW_HEIGHT=Game.windowHeight();
	    WINDOW_WIDTH=Game.windowWidth();

	    int exitX = (WINDOW_WIDTH  - buttonWidth)/2;
	    int exitY = WINDOW_HEIGHT - buttonHeight - margin;
	    
	    state=CombatState.HERO_TURN;
	    
	    tourTermine = new Button(exitX, exitY, buttonWidth, buttonHeight, "Termine");
    }
    
    public void Combat_start() {
    	
    	combat.getHero().startCombat();
    	
    	Application.run(Color.BLACK, context -> {
            var screenInfo = context.getScreenInfo();
            WINDOW_WIDTH = screenInfo.width();
            WINDOW_HEIGHT = screenInfo.height();
            
            while (state!=CombatState.FINISHED) {
            	Event e = context.pollEvent();
            	if(e!=null) {
            	switch(e) {
            	case KeyboardEvent _ -> {
            		//gererDeplacement(e);
                    //gererSelection(e);
            	}
            	
            	case PointerEvent _ -> {
            		gererClique((PointerEvent)e);
            	}
            	
            	default -> {}
            	
            	
            	
            	}
            	}
            	
            	context.renderFrame(g -> {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
                    render(g);
                    // 这里根据当前状态决定画什么
                    
                    
                });
            	
            	switch (state) {
    	        case HERO_TURN -> {
    	            //tourHero();
    	            if (combat.estCombatTermine()!=1) state = CombatState.FINISHED;
    	            else state = CombatState.ENEMY_TURN;
    	        }
    	        case ENEMY_TURN -> {
    	            combat.tourEnnemis();
    	            if (combat.estCombatTermine()!=1) state = CombatState.FINISHED;
    	            else state = CombatState.HERO_TURN;
    	        }
    	        case FINISHED -> {break;}
    	    }
                
            }
        });
    }
    
    @Override
    public void gererClique(PointerEvent p) {
		Objects.requireNonNull(p);
		
		if(state==CombatState.ENEMY_TURN) {
			combat.tourEnnemis();
            if (combat.estCombatTermine()!=1) state = CombatState.FINISHED;
            else {state = CombatState.HERO_TURN;combat.getHero().rechargerCombat();}
		}
		
		if(state==CombatState.FINISHED) {
			game.goToDonjon();
			return;
		}
		
		if (p.action() != PointerEvent.Action.POINTER_DOWN) return;
		
		int x=p.location().x();
		int y=p.location().y();
		if(tourTermine.isInside(x, y)) {
			switch (state) {
	        case ENEMY_TURN -> state = CombatState.ENEMY_TURN;
	        case HERO_TURN -> state = CombatState.ENEMY_TURN;
	        case FINISHED -> state = CombatState.FINISHED;
	    }
	        return;
		}
		
		for(int i=0;i<combat.getEnnemis().size();i++) {
			int margin=30;
			if(x>(WINDOW_WIDTH-margin-ENNEMI_SIZE*(i+1)) && x<(WINDOW_WIDTH-margin-ENNEMI_SIZE*i) &&
					y<(WINDOW_HEIGHT-margin) && y>(WINDOW_HEIGHT-margin-ENNEMI_SIZE)) {
				ptr=i;
				return;
			}
		}
		
		 int sacPixelWidth  = GRID_WIDTH * CELL_SIZE;
		 int sacPixelHeight = GRID_HEIGHT * CELL_SIZE;
		 int sacOriginX = (WINDOW_WIDTH  - sacPixelWidth)  / 2;
		 int sacOriginY = (WINDOW_HEIGHT - sacPixelHeight) / 2;
		 
		 if (x < sacOriginX || x >= sacOriginX + sacPixelWidth ||
			        y < sacOriginY || y >= sacOriginY + sacPixelHeight) {
			        return;
			    }
		 int gridX = (x - sacOriginX) / CELL_SIZE;
		 int gridY = (y - sacOriginY) / CELL_SIZE;
		 
		 Coord target = new Coord(gridX, gridY);
		 
		 var i=combat.getHero().getBackpack().contenu().getOrDefault(target, null);
		 if(i!=null && i instanceof Weapon w) {
			 w.utiliser(combat.getHero(),combat.getEnnemi(ptr),combat);
			 combat.RefreshListEnnemis();
			 ptr=0;
			 
		 }
		 
		 
    }
    
    @Override
    public void render(Graphics2D g) {
    	
    	
        g.setColor(new Color(10, 10, 30));
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        /*Hero h = hero;
        List<Ennemi> ennemisVivants = currentCombat.getEnnemis().stream()
            .filter(e -> !e.estMort())
            .toList();

        Ennemi ennemi = ennemisVivants.isEmpty() ? null : ennemisVivants.get(0);*/

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("COMBAT - Tour du " +
            (state == CombatState.HERO_TURN ? "HÉROS" : "ENNEMI"), 20, 30);

        g.drawString("HP " + combat.getHero().hp() + "/" + combat.getHero().maxHp()
            + "   Mana " + combat.getHero().mana() + "/" + combat.getHero().maxMana()
            + "   Énergie " + combat.getHero().energie()
            + "   Or " + combat.getHero().or(), 20, 50);
        
        g.setColor(Color.WHITE);
        g.drawString(combat.getHero().hp() + "/" + combat.getHero().maxHp(), 30, WINDOW_HEIGHT-10);
        g.drawImage(ImagesBase.hero,30,WINDOW_HEIGHT-120,90,90,null);

        for(int i=0;i<combat.getEnnemis().size();i++) {
        	g.drawString(combat.getEnnemi(i).name()
            + combat.getEnnemi(i).hp(),(WINDOW_WIDTH-30-(i+1)*ENNEMI_SIZE)+30 , WINDOW_HEIGHT-12);
        	
        	int margin=30;
    		int px=WINDOW_WIDTH-(i+1)*ENNEMI_SIZE-margin;
    		int py=WINDOW_HEIGHT-margin-ENNEMI_SIZE;
    		
        	if(combat.getEnnemi(i).image()!=null) {
	        	//g.drawImage(i.image(), px, py, CELL_SIZE, CELL_SIZE, null);
	        	
        		
	        	Image img = combat.getEnnemi(i).image();
	            int w = img.getWidth(null);
	            int h = img.getHeight(null);
	            g.drawImage(img, px, py, px + w, py + h, 0, 0, w, h, null);
	        }
	        else {
                // 没有图片时备用显示方式：画名字
                g.setColor(Color.WHITE);
                g.drawString(combat.getEnnemi(i).name(), px + 5, py + 20);
            }
        	
        	if(i==ptr) {
        		g.setColor(Color.WHITE);
            	g.drawRect(px, py, ENNEMI_SIZE, ENNEMI_SIZE);
        	}
        }

        // Sac à dos en haut
        //drawBackpack(g);
        int backpackPixelWidth  = GRID_WIDTH  * CELL_SIZE;
		int backpackPixelHeight = GRID_HEIGHT * CELL_SIZE;
		int backpackOriginX = (WINDOW_WIDTH  - backpackPixelWidth)  / 2;
		int backpackOriginY = (WINDOW_HEIGHT - backpackPixelHeight) / 2;
		
		for (int gy = 0; gy < GRID_HEIGHT; gy++) {
		    for (int gx = 0; gx < GRID_WIDTH; gx++) {
		    	
		    	Coord coord = new Coord(gx, gy);
		    	
		        int px = backpackOriginX + gx * CELL_SIZE;
		        int py = backpackOriginY + gy * CELL_SIZE;
		        // 画格子 + 物品
		        /*
		        g.setColor(Color.DARK_GRAY);
	            g.fillRect(px, py, CELL_SIZE, CELL_SIZE);
	            g.setColor(Color.GRAY);
	            g.drawRect(px, py, CELL_SIZE, CELL_SIZE);*/
		        
		        if (combat.getHero().getBackpack().contenu().containsKey(coord)) {
		            // 这个位置是“解锁”的格子（可以放东西）

		            g.setColor(Color.DARK_GRAY);   // 背景
		            g.fillRect(px, py, CELL_SIZE, CELL_SIZE);

		            g.setColor(Color.GRAY);        // 边框
		            g.drawRect(px, py, CELL_SIZE, CELL_SIZE);

		        } else {
		            // 这个位置没有 key，说明还没解锁
		            g.setColor(Color.BLACK);       // 更暗一点
		            g.fillRect(px, py, CELL_SIZE, CELL_SIZE);

		            g.setColor(Color.DARK_GRAY);
		            g.drawRect(px, py, CELL_SIZE, CELL_SIZE);
		        }
		    }
		}
		
		Map<Coord, Item> contenu = combat.getHero().getBackpack().contenu();
		Set<Weapon> set=new HashSet<>();
	    for (var entry : contenu.entrySet()) {
	        //Coord c = entry.getKey();
	        Item item = entry.getValue();
	        if (item == null) continue;
	        
	        if (item instanceof Weapon weapon) {
	        	set.add(weapon);
	        }
	     }
	    
	    for(var i: set) {
	    	int gx = i.offsetCoord().x();
	        int gy = i.offsetCoord().y();
	        int px = backpackOriginX + gx * CELL_SIZE;
	        int py = backpackOriginY + gy * CELL_SIZE;
	        
	        if(i.image()!=null) {
	        	//g.drawImage(i.image(), px, py, CELL_SIZE, CELL_SIZE, null);
	        	
	        	Image img = i.image();
	            int w = img.getWidth(null);
	            int h = img.getHeight(null);
	            g.drawImage(img, px, py, px + w, py + h, 0, 0, w, h, null);
	        }
	        else {
                // 没有图片时备用显示方式：画名字
                g.setColor(Color.WHITE);
                g.drawString(i.nom(), px + 5, py + 20);
            }
	        
	        g.setColor(Color.RED);
		    g.fillRect(tourTermine.x(), tourTermine.y(), tourTermine.width(), tourTermine.height());
		    g.setColor(Color.WHITE);
		    g.drawString("Termine", tourTermine.x() + 20, tourTermine.y() + 25);
	    	
	    }

        // Héros en bas gauche
        /*int floorMargin = 110;
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
        g.drawString("Fin du tour", btnX + 25, btnY + 25);*/
      }
/*
    public void tourHero() {
        combat.getHero().rechargerCombat();     
        boolean aJoueAuMoinsUneFois = false;

        while (state==CombatState.HERO_TURN) {
            if (combat.getEnnemis().isEmpty()) {
                break;
            }

            hero.afficheAnduse(new Coord(0, 0), ennemis.get(0), this);
            RefreshListEnnemis();
            aJoueAuMoinsUneFois = true;

            
            if (!aJoueAuMoinsUneFois || hero.energie() == 3) {
                break;
            }
        }
    }*/
    
}
