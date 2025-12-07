package controller;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.github.forax.zen.PointerEvent;

import modelisation.*;

public class Game_Tresor implements Ecran{
	
	private final Hero hero;
	private final ArrayList<Weapon> articles;
	private final ArrayList<Integer> width;
	private final ArrayList<Integer> height;
	private final Game game;
	private static int WINDOW_WIDTH;
	private static int WINDOW_HEIGHT;
	
	private int choix=0;
	
	
	private static final int GRID_WIDTH = 7;
    private static final int GRID_HEIGHT = 5;
    private static final int CELL_SIZE = 60;
    
    private Button exit;
	
	public Game_Tresor(Game g,List<Weapon> a,Hero h) {
		Objects.requireNonNull(g);
		Objects.requireNonNull(a);
		Objects.requireNonNull(h);
		game=g;
		articles=new ArrayList<>(a);
		hero=h;
		height=new ArrayList<>();
		width=new ArrayList<>();
		
		int buttonWidth = 120;
	    int buttonHeight = 40;
	    int margin = 20;

	    int exitX = Game.windowHeight()  - buttonWidth  - margin;
	    int exitY = Game.windowWidth() - buttonHeight - margin;
	    
	    WINDOW_HEIGHT=Game.windowHeight();
	    WINDOW_WIDTH=Game.windowWidth();
	    
		exit = new Button(exitX, exitY, buttonWidth, buttonHeight, "Exit");
		
		
		for(var i:articles) {
	    	Image m=i.image();
	    	if(m!=null) {
	    		int imw=m.getWidth(null);
		    	int imh=m.getHeight(null);
		    	height.add(imh);
		    	width.add(imw);
	    	}
	    	else {
                height.add(90);
		    	width.add(90);
            }
	    }
	}
	
	public void initExit() {
	    int buttonWidth = 120;
	    int buttonHeight = 40;
	    int margin = 20;

	    int exitX = Game.windowWidth()  - buttonWidth  - margin;
	    int exitY = Game.windowHeight() - buttonHeight - margin;

	    exit = new Button(exitX, exitY, buttonWidth, buttonHeight, "Exit");
	}
	
	public void render(Graphics2D g) {
	    g.setColor(new Color(20, 20, 40));
	    g.fillRect(0, 0, Game.windowWidth(), Game.windowHeight());

	    // Infos en haut
	    g.setColor(Color.WHITE);
	    g.setFont(new Font("Arial", Font.BOLD, 24));
	    g.drawString("Tresor", 20, 40);

	    g.setFont(new Font("Arial", Font.PLAIN, 18));
	    g.drawString("Or du héros : " + hero.or(), 20, 70);
	    g.drawString("Cliquez sur un objet pour l'acheter (si vous avez assez d'or).", 20, 95);

	    // Sac à dos en haut au centre (comme en combat)
	    int backpackPixelWidth  = GRID_WIDTH  * CELL_SIZE;
		int backpackPixelHeight = GRID_HEIGHT * CELL_SIZE;
		int backpackOriginX = (Game.windowWidth()  - backpackPixelWidth)  / 2;
		int backpackOriginY = (Game.windowHeight() - backpackPixelHeight) / 2;
		
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
		        
		        if (hero.getBackpack().contenu().containsKey(coord)) {
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
		
		Map<Coord, Item> contenu = hero.getBackpack().contenu();
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
	    	
	    }

	    // Héros en bas gauche (même rendu que combat)
	    

	    // Marchand en bas droite (même style que ennemi, mais avec 'Shop')
	    int merchantBoxSize = 90;
	    int merchantBoxX = 30;
	    int merchantBoxY = Game.windowHeight()-100;

	    g.setColor(new Color(90, 60, 20));
	    g.fillRect(merchantBoxX, merchantBoxY, merchantBoxSize, merchantBoxSize);
	    g.setColor(Color.WHITE);
	    g.drawRect(merchantBoxX, merchantBoxY, merchantBoxSize, merchantBoxSize);
	    g.drawString("Boite", merchantBoxX + 15, merchantBoxY + merchantBoxSize / 2);

	    // Liste des objets à vendre, sous le backpack
	    int ox=180+30;
	    int oy=Game.windowHeight()-270-30;
	    int pilewid=0;
	    //width.clear();
	    //height.clear();
	    int cunt=0;
	    for(var i:articles) {
	    	int px=ox+pilewid;
	    	int py=oy;
	    	Image m=i.image();
	    	if(m!=null) {
	    		int imw=m.getWidth(null);
		    	int imh=m.getHeight(null);
		    	//height.add(imh);
		    	//width.add(imw);
		    	g.drawImage(m, px, py, px + imw, py + imh, 0, 0, imw, imh, null);
		    	pilewid+=imw;
		    	if(cunt==choix) {
		    		g.setColor(Color.WHITE);
		    		g.drawRect(px, py, imw, imh);
		    	}
	    	}
	    	else {
                g.setColor(Color.WHITE);
                g.drawString(i.nom(), px + 5, py + 20);
                pilewid+=90;
                height.add(90);
		    	width.add(90);
                if(cunt==choix) {
                	g.setColor(Color.WHITE);
		    		g.drawRect(px, py, 90, 90);
                }
            }
	    	cunt++;
	    }

	    

	      
	

	    // Bouton "Retour" en bas à droite
	    initExit();
	    g.setColor(Color.RED);
	    g.fillRect(exit.x(), exit.y(), exit.width(), exit.height());
	    g.setColor(Color.WHITE);
	    g.drawString("Quit", exit.x() + 20, exit.y() + 25);
	  }

	  public void gererClique(PointerEvent p) {
		  Objects.requireNonNull(p);
	    if (p.action() != PointerEvent.Action.POINTER_DOWN) {
	      return;
	    }
	    initExit();
	    
	    int x=p.location().x();
		int y=p.location().y();
	    
	    if(exit.isInside(x, y)) {
	    	game.goToDonjon();
	    	return;
	    }

	    // Bouton "Retour"


	    // Détection clic sur un item (coordonnées cohérentes avec renderMerchant)
	    int ox=180+30;
	    int oy=Game.windowHeight()-270-30;
	    int pilewid=0;
	    int cunt=0;
	    for(int i=0;i<articles.size();i++) {
	    	if(x>(ox+pilewid)&&x<=ox+pilewid+width.get(i)&&y>=oy&&y<=oy+height.get(i)) {
	    		choix=i;
	    	}
	    	pilewid+=width.get(i);
	    }
	    
	    int sacPixelWidth  = GRID_WIDTH * CELL_SIZE;
		int sacPixelHeight = GRID_HEIGHT * CELL_SIZE;
		int sacOriginX = (Game.windowWidth()  - sacPixelWidth)  / 2;
		int sacOriginY = (Game.windowHeight() - sacPixelHeight) / 2;
		 /*
		 if (x < sacOriginX || x >= sacOriginX + sacPixelWidth ||
			        y < sacOriginY || y >= sacOriginY + sacPixelHeight) {
			        return;
			    }*/
		
		if (x > sacOriginX && x <= sacOriginX + sacPixelWidth &&
		        y > sacOriginY && y <= sacOriginY + sacPixelHeight) {
		 int gridX = (x - sacOriginX) / CELL_SIZE;
		 int gridY = (y - sacOriginY) / CELL_SIZE;
		 
		 Coord target = new Coord(gridX, gridY);
		 
		 if(hero.getBackpack().contenu().containsKey(target)) {
			 if(hero.getBackpack().contenu().getOrDefault(target, null)==null) {
				 articles.get(choix).translate(target);
				 if(hero.getBackpack().peutPlacer(articles.get(choix))){
					 if(hero.or()>=0) {
						 hero.getBackpack().placer(articles.get(choix));
						 articles.remove(choix);
						 width.remove(choix);
						 height.remove(choix);
						 choix=0;
					 }
				 }
			 }
		 }
	  }
	    /*
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
	    }*/
	  }
}

