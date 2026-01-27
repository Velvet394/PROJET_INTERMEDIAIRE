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



import com.github.forax.zen.KeyboardEvent;

public class Game_Market implements Ecran{
	
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
    
    
    //ver2
    
 // --- copier Game_Donjon ---
    private Weapon heldWeapon = null;
    private Coord heldOldOffset = null;
    private java.util.List<Coord> heldOldForme = null;
    private int heldOldRot = 0;

    // 额外：区分“拿的是背包里的武器”还是“商店里的武器”
    private boolean heldFromShop = false;
    private int heldShopIndex = -1;
    
    
	
	public Game_Market(Game g,List<Weapon> a,Hero h) {
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
	    g.drawString("Marchand", 20, 40);

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
		
		for (var entry : hero.getBackpack().contenu().entrySet()) {
		    Coord c = entry.getKey();
		    Item it = entry.getValue();
		    if (!(it instanceof ItemMaldiction)) continue;

		    int px = backpackOriginX + c.x() * CELL_SIZE;
		    int py = backpackOriginY + c.y() * CELL_SIZE;

		    g.setColor(new Color(120, 0, 120));
		    g.fillRect(px + 4, py + 4, CELL_SIZE - 8, CELL_SIZE - 8);
		    g.setColor(Color.WHITE);
		    g.drawString("C", px + CELL_SIZE/2 - 4, py + CELL_SIZE/2 + 6);
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
	    
	    g.setColor(Color.WHITE);
	    g.setFont(new Font("Arial", Font.PLAIN, 18));
	    g.drawString(""+hero.or(), backpackOriginX+CELL_SIZE, backpackOriginY+2*CELL_SIZE+50);

	    // Héros en bas gauche (même rendu que combat)
	    

	    // Marchand en bas droite (même style que ennemi, mais avec 'Shop')
	    int merchantBoxSize = 90;
	    int merchantBoxX = 30;
	    int merchantBoxY = Game.windowHeight()-100;

	    g.setColor(new Color(90, 60, 20));
	    g.fillRect(merchantBoxX, merchantBoxY, merchantBoxSize, merchantBoxSize);
	    g.setColor(Color.WHITE);
	    g.drawRect(merchantBoxX, merchantBoxY, merchantBoxSize, merchantBoxSize);
	    g.drawString("Shop", merchantBoxX + 15, merchantBoxY + merchantBoxSize / 2);

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

	    if(articles.isEmpty()||width.isEmpty()||height.isEmpty()) return;

	    // Détection clic sur un item (coordonnées cohérentes avec renderMerchant)
	    int ox=180+30;
	    int oy=Game.windowHeight()-270-30;
	    int pilewid=0;
	    int cunt=0;
	    for(int i=0;i<articles.size();i++) {
	    	if(x>(ox+pilewid)&&x<=ox+pilewid+width.get(i)&&y>=oy&&y<=oy+height.get(i)) {
	    		
	    		// 如果之前正拿着一个“商店物品”，先把它恢复原样再换
	    	    if (heldWeapon != null) {
	    	      restoreHeldWeapon(hero.getBackpack());
	    	      // restoreHeldWeapon 会 clearHeld，所以这里继续设置新的 held
	    	    }
	    		choix=i;
	    		
	    		Weapon w = articles.get(i);
	    	    heldWeapon = w;
	    	    heldFromShop = true;
	    	    heldShopIndex = i;

	    	    heldOldOffset = w.offsetCoord();
	    	    heldOldForme  = new java.util.ArrayList<>(w.forme());
	    	    heldOldRot    = w.rotationQuarterTurns();

	    	    return; // 点商店物品只负责“拿起”，不继续往下执行
	    	}
	    	pilewid+=width.get(i);
	    }
	    
	    handleBackpackClick(x, y);
	    //////这边是没有旋转时的购买逻辑
//	    int sacPixelWidth  = GRID_WIDTH * CELL_SIZE;
//		int sacPixelHeight = GRID_HEIGHT * CELL_SIZE;
//		int sacOriginX = (Game.windowWidth()  - sacPixelWidth)  / 2;
//		int sacOriginY = (Game.windowHeight() - sacPixelHeight) / 2;
//		 /*
//		 if (x < sacOriginX || x >= sacOriginX + sacPixelWidth ||
//			        y < sacOriginY || y >= sacOriginY + sacPixelHeight) {
//			        return;
//			    }*/
//		
//		if (x > sacOriginX && x <= sacOriginX + sacPixelWidth &&
//		        y > sacOriginY && y <= sacOriginY + sacPixelHeight) {
//		 int gridX = (x - sacOriginX) / CELL_SIZE;
//		 int gridY = (y - sacOriginY) / CELL_SIZE;
//		 
//		 Coord target = new Coord(gridX, gridY);
//		 if(articles.isEmpty()) return;
//		 if(hero.getBackpack().contenu().containsKey(target)) {
//			 if(hero.getBackpack().contenu().getOrDefault(target, null)==null) {
//				 articles.get(choix).translate(target);
//				 if(hero.getBackpack().peutPlacer(articles.get(choix))){
//					 if(hero.or()>=articles.get(choix).price()) {
//						 hero.useOr(articles.get(choix).price());
//						 hero.getBackpack().placer(articles.get(choix));
//						 articles.remove(choix);
//						 width.remove(choix);
//						 height.remove(choix);
//						 choix=0;
//						 //hero.useOr(articles.get(choix).price());
//					 }
//				 }
//			 }
//		 }
//	  }
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
	  
	  
	  //v2
	  
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
	  
	  
	  //ver2
	  
	  public void handleKeyboard(KeyboardEvent k) {
		  Objects.requireNonNull(k);
		  if (heldWeapon == null) return;

		  if (k.action() == KeyboardEvent.Action.KEY_PRESSED && k.key() == KeyboardEvent.Key.R) {
		    heldWeapon.rotation();
		  }
		}
	  
	  private void clearHeld() {
		  heldWeapon = null;
		  heldOldOffset = null;
		  heldOldForme = null;
		  heldOldRot = 0;
		  heldFromShop = false;
		  heldShopIndex = -1;
		}
	  
	  private void restoreHeldWeapon(Backpack bp) {
		  // 恢复形状
		  heldWeapon.forme().clear();
		  heldWeapon.forme().addAll(heldOldForme);

		  // 恢复旋转计数
		  heldWeapon.setRotationQuarterTurns(heldOldRot);

		  // 恢复位置
		  heldWeapon.translate(heldOldOffset);

		  // 如果是从背包拿起的，要放回背包
		  if (!heldFromShop) {
		    bp.placer(heldWeapon);
		  }
		  clearHeld();
		}
	  
	  private void handleBackpackClick(int x, int y) {
		  int backpackPixelWidth  = GRID_WIDTH  * CELL_SIZE;
		  int backpackPixelHeight = GRID_HEIGHT * CELL_SIZE;
		  int backpackOriginX = (Game.windowWidth()  - backpackPixelWidth)  / 2;
		  int backpackOriginY = (Game.windowHeight() - backpackPixelHeight) / 2;

		  var bp = hero.getBackpack();

		  // 点击在背包外：如果手上有东西 -> 取消（恢复）
		  if (x < backpackOriginX || x >= backpackOriginX + backpackPixelWidth ||
		      y < backpackOriginY || y >= backpackOriginY + backpackPixelHeight) {
		    if (heldWeapon != null) {
		      restoreHeldWeapon(bp);
		    }
		    return;
		  }

		  int gridX = (x - backpackOriginX) / CELL_SIZE;
		  int gridY = (y - backpackOriginY) / CELL_SIZE;
		  Coord cell = new Coord(gridX, gridY);

		  // 1) 手上没东西：点背包里的武器 -> 拿起
		  if (heldWeapon == null) {
		    Item clicked = bp.getItemAt(cell);
		    if (clicked instanceof Weapon w) {
		      heldWeapon = w;
		      heldFromShop = false;

		      heldOldOffset = w.offsetCoord();
		      heldOldForme  = new java.util.ArrayList<>(w.forme());
		      heldOldRot    = w.rotationQuarterTurns();

		      bp.retirer(w);   // 从背包里拿起（临时移除）
		    }
		    return;
		  }

		  // 2) 手上有武器：尝试放到 cell
		  heldWeapon.translate(cell);

		  if (!bp.peutPlacer(heldWeapon)) {
		    // 放不下：恢复 translate（但仍保持“拿着”状态）
		    heldWeapon.translate(heldOldOffset);
		    return;
		  }

		  // 放得下：分两种情况
		  if (!heldFromShop) {
		    // (A) 背包里拿起的武器：直接放下
		    bp.placer(heldWeapon);
		    clearHeld();
		    return;
		  }

		  // (B) 商店里拿起的武器：需要买得起才放下 + 从商店移除
		  int price = heldWeapon.price();
		  if (hero.or() < price) {
		    // 买不起：不放下，恢复 translate
		    heldWeapon.translate(heldOldOffset);
		    return;
		  }

		  // 真正购买
		  hero.useOr(price);
		  bp.placer(heldWeapon);

		  // 从商店列表移除（用 heldShopIndex 保证删对）
		  int idx = heldShopIndex;
		  if (idx >= 0 && idx < articles.size()) {
		    articles.remove(idx);
		    if (idx < width.size())  width.remove(idx);
		    if (idx < height.size()) height.remove(idx);
		  }

		  // choix 也修正到合法范围
		  if (articles.isEmpty()) {
		    choix = 0;
		  } else {
		    choix = Math.min(choix, articles.size() - 1);
		  }

		  clearHeld();
		}
	  
	  

}
