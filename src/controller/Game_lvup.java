package controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.github.forax.zen.PointerEvent;

import modelisation.Coord;
import modelisation.Hero;
import modelisation.Item;
import modelisation.Weapon;

public class Game_lvup implements Ecran {
	
	private final Hero hero;
	private int nb=0;
	private final Game game;
	private static int WINDOW_WIDTH;
	private static int WINDOW_HEIGHT;
	
	private int choix=0;
	
	
	private static final int GRID_WIDTH = 7;
    private static final int GRID_HEIGHT = 5;
    private static final int CELL_SIZE = 60;
    
    private Button exit;
    
    public Game_lvup(Game g, Hero h) {
		Objects.requireNonNull(g);
		Objects.requireNonNull(h);
		game=g;

		hero=h;
		
		int buttonWidth = 120;
	    int buttonHeight = 40;
	    int margin = 20;

	    int exitX = Game.windowHeight()  - buttonWidth  - margin;
	    int exitY = Game.windowWidth() - buttonHeight - margin;
	    
	    WINDOW_HEIGHT=Game.windowHeight();
	    WINDOW_WIDTH=Game.windowWidth();
	    
		exit = new Button(exitX, exitY, buttonWidth, buttonHeight, "Exit");
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
	        
	        
	        drawWeaponImage(g, i, px, py);
//	        if(i.image()!=null) {
//	        	//g.drawImage(i.image(), px, py, CELL_SIZE, CELL_SIZE, null);
//	        	
//	        	Image img = i.image();
//	            int w = img.getWidth(null);
//	            int h = img.getHeight(null);
//	            g.drawImage(img, px, py, px + w, py + h, 0, 0, w, h, null);
//	        }
//	        else {
//                // 没有图片时备用显示方式：画名字
//                g.setColor(Color.WHITE);
//                g.drawString(i.nom(), px + 5, py + 20);
//            }
	    	
	    }
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
		 
		 if(!hero.getBackpack().contenu().containsKey(target)&&nb<3) {
			 if(hero.getBackpack().allouerCase(target)==1) {
				 nb++;
				 }
			 }
		 }
	  }
    
    private void drawWeaponImage(Graphics2D g, Weapon w, int px, int py) {
		  Image img = w.image();
		  if (img == null) {
		    g.setColor(Color.WHITE);
		    g.drawString(w.nom(), px + 5, py + 20);
		    return;
		  }

		  // 计算武器占用格子范围（forme 的 bounding box）
		  int maxX = 0, maxY = 0;
		  for (var c : w.forme()) {
		    maxX = Math.max(maxX, c.x());
		    maxY = Math.max(maxY, c.y());
		  }
		  int cellsW = maxX + 1;
		  int cellsH = maxY + 1;

		  int targetW = cellsW * CELL_SIZE;
		  int targetH = cellsH * CELL_SIZE;

		  int iw = img.getWidth(null);
		  int ih = img.getHeight(null);
		  if (iw <= 0 || ih <= 0) return;

		  int turns = w.rotationQuarterTurns() & 3;
		  double theta = (Math.PI / 2.0) * turns;

		  // ✅关键：90°/270° 时，图片旋转后的“有效宽高”要交换
		  int effW = ((turns & 1) == 0) ? iw : ih;
		  int effH = ((turns & 1) == 0) ? ih : iw;

		  // 缩放到目标区域（保持比例）
		  double sx = (double) targetW / effW;
		  double sy = (double) targetH / effH;
		  double s = Math.min(sx, sy);

		  var oldTx = g.getTransform();
		  try {
		    // 把原点移到目标矩形中心
		    double cx = px + targetW / 2.0;
		    double cy = py + targetH / 2.0;

		    g.translate(cx, cy);
		    g.rotate(theta);
		    g.scale(s, s);

		    // 绘制时让图片中心对齐原点（用原始 iw/ih）
		    g.drawImage(img, -iw / 2, -ih / 2, null);
		  } finally {
		    g.setTransform(oldTx);
		  }
		}

}
