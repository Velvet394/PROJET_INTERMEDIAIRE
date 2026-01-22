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
    
    
    
 // ====== Mode déplacement (comme Donjon) ======
    private Weapon heldWeapon = null;
    private Coord heldOldOffset = null;
    private java.util.List<Coord> heldOldForme = null;
    private int heldOldRot = 0;
    private boolean deplacerMode = false;

    // ====== Gestion des malédictions à placer ======
    private boolean resolvingCurse = false;
    private ItemMaldiction currentCurse = null;

    // (optionnel) bouton pour refuser / bouton déplacer
    private Button deplacerBtn;
    private Button refuserBtn;
    
    
    
    
    
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
	    
	    deplacerBtn = new Button(exitX - 150, exitY, buttonWidth, buttonHeight, "Deplacer");
	    refuserBtn  = new Button(exitX + 150, exitY, buttonWidth, buttonHeight, "Refuser");
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
    	            
    	         // 如果敌人触发了诅咒：进入“必须放诅咒”模式
    	            if (combat.hasPendingMaledictions()) {
    	                resolvingCurse = true;
    	                currentCurse = combat.pollMalediction();
    	                // 强制进入移动/放置逻辑
    	                deplacerMode = true;
    	                heldWeapon = null; // 避免同时拿着武器
    	            }
    	            
    	            
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
		
//		if(state==CombatState.ENEMY_TURN) {
//			combat.tourEnnemis();
//            if (combat.estCombatTermine()!=1) state = CombatState.FINISHED;
//            else {state = CombatState.HERO_TURN;combat.getHero().rechargerCombat();}
//		}
		
		if(state==CombatState.FINISHED) {
			combat.getHero().expup();
			if(combat.getHero().testLevelUp()==1) {
				game.goTolvup();
			}
			else {game.goToTresor();
//			game.goToDonjon();
			}
			return;
		}
		
		if (p.action() != PointerEvent.Action.POINTER_DOWN) return;
		
		int x=p.location().x();
		int y=p.location().y();
//		if(tourTermine.isInside(x, y)) {
//			switch (state) {
//	        case ENEMY_TURN -> state = CombatState.ENEMY_TURN;
//	        case HERO_TURN -> state = CombatState.ENEMY_TURN;
//	        case FINISHED -> state = CombatState.FINISHED;
//	    }
//	        return;
//		}
		if (tourTermine.isInside(x, y)) {
		    // 正在放诅咒时，不允许结束回合
		    if (resolvingCurse) return;

		    if (state == CombatState.HERO_TURN) {
		        state = CombatState.ENEMY_TURN;

		        // 让敌人行动一次
		        combat.tourEnnemis();

		        //  敌人行动后立刻检查是否产生诅咒
		        if (combat.hasPendingMaledictions()) {
		            resolvingCurse = true;
		            currentCurse = combat.pollMalediction();
		            deplacerMode = true;
		            heldWeapon = null;
		        }

		        if (combat.estCombatTermine() != 1) {
		            state = CombatState.FINISHED;
		        } else {
		            state = CombatState.HERO_TURN;
		            combat.getHero().rechargerCombat();
		        }
		    }
		    return;
		}
		
		
		
		
		if (deplacerBtn.isInside(x, y)) {
		    deplacerMode = !deplacerMode;
		    // 切换时如果手上拿着武器，直接放回去（避免丢失）
		    if (!deplacerMode && heldWeapon != null) {
		        restoreHeldWeapon(combat.getHero().getBackpack());
		    }
		    return;
		}

//		if (resolvingCurse && refuserBtn.isInside(x, y)) {
//		    int dmg = getRefusMaledictionDamage(combat.getHero());
//		    combat.getHero().damage(dmg);
//
//		    // 丢弃当前诅咒，若队列还有继续，否则退出
//		    currentCurse = null;
//		    if (combat.hasPendingMaledictions()) {
//		        currentCurse = combat.pollMalediction();
//		    } else {
//		        resolvingCurse = false;
//		    }
//		    return;
//		}
		if (resolvingCurse && refuserBtn.isInside(x, y)) {
		    // ✅ 用 Hero 的新方法：自增计数 + 扣血
		    int dmg = combat.getHero().refuserMalediction();

		    // 丢弃当前诅咒，若还有下一个则继续，否则退出 curse 模式
		    currentCurse = null;
		    if (combat.hasPendingMaledictions()) {
		        currentCurse = combat.pollMalediction();
		    } else {
		        resolvingCurse = false;
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
		 
//		 Coord target = new Coord(gridX, gridY);
		  
		 
		 
//		 var i=combat.getHero().getBackpack().contenu().getOrDefault(target, null);
//		 if(i!=null && i instanceof Weapon w) {
//			 w.utiliser(combat.getHero(),combat.getEnnemi(ptr),combat);
//			 combat.RefreshListEnnemis();
//			 ptr=0;
//			 
//		 }
		 
		 Coord cell = new Coord(gridX, gridY);
		 var bp = combat.getHero().getBackpack();

		 // A) 如果正在处理诅咒：点击格子就是放诅咒
		 if (resolvingCurse && currentCurse != null) {
//		     currentCurse.translate(cell);
//
//		     // 关键：诅咒“会挤占冲突格子里的物品”等逻辑应在 Backpack.placer(Item) 内实现
//		     // 如果 placer 只有 Weapon 版本，在 Backpack 加一个 placer(Item) 或 placer(ItemMaldiction)
//		     bp.PlacerMalediction(currentCurse);
//
//		     // 放完一个，看还有没有下一个
//		     currentCurse = null;
//		     if (combat.hasPendingMaledictions()) {
//		         currentCurse = combat.pollMalediction();
//		     } else {
//		         resolvingCurse = false;
//		     }
//		     return;
			 
			// 记录旧 offset，放不下时要恢复
			 Coord old = currentCurse.offsetCoord();

			 // 先把诅咒移动到玩家点击的位置（作为 offset）
			 currentCurse.translate(cell);

			 //  放置前先检查边界
			 if (!bp.peutPlacerMalediction(currentCurse)) {
			     // 放不下：恢复旧 offset，不消耗诅咒，让玩家重新点
			     currentCurse.translate(old);
			     return;
			 }

			 //  真正放置（会顶掉冲突物品）
			 bp.PlacerMalediction(currentCurse);

			 // 后续：记录接受、切换到下一个、退出 resolvingCurse...
			 combat.getHero().addMalediction(1);

			 currentCurse = null;
			 if (combat.hasPendingMaledictions()) currentCurse = combat.pollMalediction();
			 else resolvingCurse = false;
			 return;
		 }
		 
		// B) 移动模式：像 Donjon 一样拿起/放下武器
		 if (deplacerMode) {
		     handleBackpackMoveClick(cell);
		     return;
		 }

		 // C) 非移动模式：点击武器=使用（保持你原行为）
		 Item clicked = bp.contenu().getOrDefault(cell, null);
		 if (clicked instanceof Weapon w) {
		     w.utiliser(combat.getHero(), combat.getEnnemi(ptr), combat);
		     combat.RefreshListEnnemis();
		     
		     if (combat.estCombatTermine() != 1 || combat.getEnnemis().isEmpty()) {
		    	    state = CombatState.FINISHED;
		    	    return;
		    	}
		     
		     ptr = 0;
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
        
        int remaining = (currentCurse == null ? 0 : 1) + combat.pendingMaledictionsCount();
        g.drawString("Restant: " + remaining, 20, 120);

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
		
		
		for (var entry : combat.getHero().getBackpack().contenu().entrySet()) {
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
	        
	        g.setColor(Color.RED);
		    g.fillRect(tourTermine.x(), tourTermine.y(), tourTermine.width(), tourTermine.height());
		    g.setColor(Color.WHITE);
		    g.drawString("Termine", tourTermine.x() + 20, tourTermine.y() + 25);
	    	
	    }
	    
	    g.setColor(Color.WHITE);
	    g.setFont(new Font("Arial", Font.PLAIN, 18));
	    g.drawString(""+combat.getHero().or(), backpackOriginX+CELL_SIZE, backpackOriginY+2*CELL_SIZE+50);

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
	    
	 // --- boutons ---
	    g.setColor(Color.RED);
	    g.fillRect(tourTermine.x(), tourTermine.y(), tourTermine.width(), tourTermine.height());
	    g.setColor(Color.WHITE);
	    g.drawString("Termine", tourTermine.x() + 20, tourTermine.y() + 25);

	    // Déplacer toggle
	    g.setColor(deplacerMode ? Color.GREEN : Color.DARK_GRAY);
	    g.fillRect(deplacerBtn.x(), deplacerBtn.y(), deplacerBtn.width(), deplacerBtn.height());
	    g.setColor(Color.WHITE);
	    g.drawString("Deplacer", deplacerBtn.x() + 15, deplacerBtn.y() + 25);

	    // Curse overlay
	    if (resolvingCurse) {
	        int dmg = getRefusMaledictionDamage(combat.getHero());
	        g.setColor(Color.ORANGE);
	        g.drawString("MALEDICTION ! Placez-la dans le sac.", 20, 80);
	        g.drawString("Ou Refuser : -" + dmg + " HP", 20, 100);

	        g.setColor(Color.GRAY);
	        g.fillRect(refuserBtn.x(), refuserBtn.y(), refuserBtn.width(), refuserBtn.height());
	        g.setColor(Color.WHITE);
	        g.drawString("Refuser", refuserBtn.x() + 20, refuserBtn.y() + 25);
	        
	     // --- preview de la forme à placer ---
	        if (currentCurse != null) {
	            final int previewCell = 18;
	            final int ox = 20;
	            final int oy = 140;

	            int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
	            int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
	            for (var c : currentCurse.forme()) {
	                minX = Math.min(minX, c.x());
	                minY = Math.min(minY, c.y());
	                maxX = Math.max(maxX, c.x());
	                maxY = Math.max(maxY, c.y());
	            }

	            int w = (maxX - minX + 1) * previewCell + 10;
	            int h = (maxY - minY + 1) * previewCell + 10;

	            g.setColor(Color.WHITE);
	            g.drawString("Forme:", ox, oy - 10);

	            g.setColor(Color.DARK_GRAY);
	            g.fillRect(ox, oy, w, h);
	            g.setColor(Color.WHITE);
	            g.drawRect(ox, oy, w, h);

	            g.setColor(new Color(180, 0, 180));
	            for (var c : currentCurse.forme()) {
	                int px = ox + 5 + (c.x() - minX) * previewCell;
	                int py = oy + 5 + (c.y() - minY) * previewCell;
	                g.fillRect(px, py, previewCell - 2, previewCell - 2);
	            }
	        }
	    }
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
    
    
    // v2
    
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
    
    
    
    
    
    
    //v2
    
    
    private void handleBackpackMoveClick(Coord cell) {
        var bp = combat.getHero().getBackpack();

        if (heldWeapon == null) {
            Item clicked = bp.getItemAt(cell);
            if (clicked instanceof Weapon w) {
                heldWeapon = w;
                heldOldOffset = w.offsetCoord();
                heldOldForme  = new java.util.ArrayList<>(w.forme());
                heldOldRot    = w.rotationQuarterTurns();
                bp.retirer(w);
            }
            return;
        }

        heldWeapon.translate(cell);
        if (bp.peutPlacer(heldWeapon)) {
            bp.placer(heldWeapon);
            clearHeld();
        } else {
            heldWeapon.translate(heldOldOffset);
        }
    }

    private void restoreHeldWeapon(Backpack bp) {
        // 还原形状与旋转信息（按你 Donjon 里写的方式）
        heldWeapon.forme().clear();
        heldWeapon.forme().addAll(heldOldForme);
        heldWeapon.setRotationQuarterTurns(heldOldRot);
        heldWeapon.translate(heldOldOffset);
        bp.placer(heldWeapon);
        clearHeld();
    }

    private void clearHeld() {
        heldWeapon = null;
        heldOldOffset = null;
        heldOldForme = null;
        heldOldRot = 0;
    }

    // 读取 Hero.private int refusMalediction：不想新 class，那就用反射读取
    private static int getRefusMaledictionDamage(Hero hero) {
        try {
            var f = hero.getClass().getDeclaredField("refusMalediction");
            f.setAccessible(true);
            return (int) f.get(hero);
        } catch (ReflectiveOperationException e) {
            // 如果字段名不一致或被改了，给个兜底值避免崩
            return 1;
        }
    }

    
}
