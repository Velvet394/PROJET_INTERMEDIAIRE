package controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.github.forax.zen.*;

import com.github.forax.zen.Application;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import modelisation.*;



public class Game_Donjon implements Ecran {
	private final Game game;
	private final Hero hero;
	private final Donjon donjon;
	private static int WINDOW_WIDTH;
	private static int WINDOW_HEIGHT;
    private static final int ROOM_SIZE = 70;
    private static final int DONJON_WIDTH = 5;
    private static final int DONJON_HEIGHT = 11;
    private static final int GRID_WIDTH = 7;
    private static final int GRID_HEIGHT = 5;
    private static final int CELL_SIZE = 60;
    
    private ViewState viewState = ViewState.DUNGEON;
    
    public Button exit;
    public Button sac;

    private static int selectedX = 0;
    private static int selectedY = 0;
	
	public Game_Donjon(Hero h,Donjon d,Game g) {
		Objects.requireNonNull(h);
		Objects.requireNonNull(d);
		Objects.requireNonNull(g);
		hero=h;
		//donjon=Generation.genererDonjon();
		donjon=d;
		game=g;
		
		int buttonWidth = 120;
	    int buttonHeight = 40;
	    int margin = 20;

	    int exitX = Game.windowHeight()  - buttonWidth  - margin;
	    int exitY = Game.windowWidth() - buttonHeight - margin;
	    
	    WINDOW_HEIGHT=Game.windowHeight();
	    WINDOW_WIDTH=Game.windowWidth();

	    exit = new Button(exitX, exitY, buttonWidth, buttonHeight, "Exit");
	    
	    int buttonWidth2 = 120;
	    int buttonHeight2 = 40;
	    int margin2 = 20;

	    int exitX2 = margin;
	    int exitY2 = margin;

	    sac = new Button(exitX2, exitY2, buttonWidth2, buttonHeight2, "Sac");
	}
	/*
	public Game_Donjon(Hero h, Donjon d) {
		Objects.requireNonNull(h);
		Objects.requireNonNull(d);
		hero=h;
		donjon=d;
	}*/
	
	public void RefreshPosition() {}
	
	
	
	//public void visiter() {}
	public void exit() {System.exit(0);}
	
	public void afficheSac(Graphics2D g) {
		
		g.setColor(Color.RED);
	    g.fillRect(exit.x(), exit.y(), exit.width(), exit.height());
	    g.setColor(Color.WHITE);
	    g.drawString("Quit", exit.x() + 20, exit.y() + 25);
	    
	    g.setColor(Color.RED);
	    g.fillRect(sac.x(), sac.y(), sac.width(), sac.height());
	    g.setColor(Color.WHITE);
	    g.drawString("Sac", sac.x() + 20, sac.y() + 25);
		
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

	        //int gx = c.x();
	        //int gy = c.y();
	        //int px = backpackOriginX + gx * CELL_SIZE;
	        //int py = backpackOriginY + gy * CELL_SIZE;

	        // 如果是武器，就画图片；否则画文字
	        /*
	        if (item instanceof Weapon weapon) {
	        	
	            if (img != null) {
	                // 按格子大小缩放绘制
	                g.drawImage(img, px, py, CELL_SIZE, CELL_SIZE, null);
	            } else {
	                // 没有图片时备用显示方式：画名字
	                g.setColor(Color.WHITE);
	                g.drawString(weapon.nom(), px + 5, py + 20);
	            }
	        } else {
	            g.setColor(Color.WHITE);
	            g.drawString(item.nom(), px + 5, py + 20);
	        }*/
	    }
		
	
	public void start() {
		Application.run(Color.BLACK, context -> {
            var screenInfo = context.getScreenInfo();
            WINDOW_WIDTH = screenInfo.width();
            WINDOW_HEIGHT = screenInfo.height();
            
            //exit=new Button(WINDOW_WIDTH-30,WINDOW_HEIGHT-30,30,30,"Exit");
            initExit();
            initSac();

            // Boucle principale du jeu
            while (true) {
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
            	
                /*Event Event = context.pollEvent();
                if (Event != null && Event instanceof KeyboardEvent k) {
                    gererDeplacement(k);
                    gererSelection(k);
                }
                
                if (Event != null && Event instanceof PointerEvent p) {
                	if(p.action() == PointerEvent.Action.POINTER_DOWN) {
                		interagirAvecSalle(p);
                }}*/
                
            	/*
            	context.renderFrame(g -> {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
                    renderDonjon(g);
                });*/
            	
            	context.renderFrame(g -> {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, Game.windowWidth(), Game.windowHeight());

                    // 这里根据当前状态决定画什么
                    switch (viewState) {
                        case DUNGEON -> renderDonjon(g);
                        case BACKPACK -> afficheSac(g);
                    }
                });
                
            }
        });
    }
	
	@Override
	public void render(Graphics2D g) {
		switch (viewState) {
        case DUNGEON -> renderDonjon(g);
        case BACKPACK -> afficheSac(g);
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
	
	public void initSac() {
	    int buttonWidth = 120;
	    int buttonHeight = 40;
	    int margin = 20;

	    int exitX = margin;
	    int exitY = margin;

	    sac = new Button(exitX, exitY, buttonWidth, buttonHeight, "Sac");
	}
	
	@Override
	public void gererClique(PointerEvent p) {
		Objects.requireNonNull(p);
		
		if (p.action() != PointerEvent.Action.POINTER_DOWN) return;
		
		int x=p.location().x();
		int y=p.location().y();
		
		if(exit.isInside(x, y)) {
			exit();
			return;
		}
		
		if(sac.isInside(x, y)) {
			/*
			context.renderFrame(g -> {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
                afficheSac(g);
            });*/
			/*
			if(viewState==ViewState.BACKPACK) {
				viewState=ViewState.DUNGEON;
			}
			else if(viewState==ViewState.DUNGEON) {
				viewState=ViewState.BACKPACK;
			}*/
			/*
			 * 
			viewState = (viewState == ViewState.DUNGEON)
	                ? ViewState.BACKPACK
	                : ViewState.DUNGEON;
	                */
			
			switch (viewState) {
	        case DUNGEON -> viewState = ViewState.BACKPACK;
	        case BACKPACK -> viewState = ViewState.DUNGEON;
	    }
	        return;
		}
		/*
		if(x>=100 && x<=WINDOW_WIDTH-100 && y>=100 && y<=WINDOW_HEIGHT-100) {
			for(int i=0;i<7;i++) {
				for(int j=0;j<5;j++) {
					if(x>=(100+i*30) && x<=(WINDOW_WIDTH-100-)) {}
				}
			}
		}*/
		 // 2. 计算地图区域的位置（和渲染时同样的公式）
	    int dungeonPixelWidth  = DONJON_WIDTH * ROOM_SIZE;
	    int dungeonPixelHeight = DONJON_HEIGHT * ROOM_SIZE;
	    int dungeonOriginX = (Game.windowWidth()  - dungeonPixelWidth)  / 2;
	    int dungeonOriginY = (Game.windowHeight() - dungeonPixelHeight) / 2;

	    // 3. 判断点击是否在地图区域外
	    if (x < dungeonOriginX || x >= dungeonOriginX + dungeonPixelWidth ||
	        y < dungeonOriginY || y >= dungeonOriginY + dungeonPixelHeight) {
	        // 点击在地图外（比如空白背景），什么都不做
	        return;
	    }

	    // 4. 计算点击的格子坐标 (gridX, gridY)
	    int gridX = (x - dungeonOriginX) / ROOM_SIZE;  // 0..DONJON_WIDTH-1
	    int gridY = (y - dungeonOriginY) / ROOM_SIZE;  // 0..DONJON_HEIGHT-1
	    
	    Coord target = new Coord(gridX, gridY);

	    // 3. 通过 Donjon 来访问房间（用你已有的合法性判断）
	    var r=donjon.moveSalle(hero, target);
	    if(r!=null) {
	    switch(r) {
	    case ENEMY -> {game.goToCombat();}
	    case EMPTY -> {}
	    case HEALER -> {hero.heal(Dice.roll(3, 10));}
	    case GATE -> {donjon.moveEtape();}
	    case MERCHANT -> {game.goToMarket();}
	    case TREASURE -> {game.goToTresor();}
	    case EXIT -> {System.exit(0);}
	    default -> {}
	    }
	    }
	    // 如果你想更新 selectedX/selectedY 用于高亮，可以在这里同步一下
	    selectedX = gridX;
	    selectedY = gridY;
	    

	    
	    // 5. 用 (gridX, gridY) 去访问你的 Donjon / Etape / Room
//	    var etape = donjon.getEtape(/* 当前楼层编号，例如 0 或 hero 所在楼层 */ 0);
//	    var room = etape.getSalle(new Coord(gridX, gridY));
//	    if (room == null) {
//	        return; // 没有房间，可能是空的区域
//	    }

	    // 6. 标记当前选中房间（如果你想画高亮）
//	    selectedX = gridX;
//	    selectedY = gridY;

	    // 7. 按你的逻辑访问房间
//	    room.visiter(hero); // 这里会触发 Enter.apply(...)
	    // 如果是敌人房间，你可以在 visiter() 里启动战斗，或者这里判断 room.getType()
		
		
	}
	
	//public void afficheDonjon(Graphics2D g) {}
	public void renderDonjon(Graphics2D g) {
		
		initExit();
		initSac();
		
		g.setColor(Color.RED);
	    g.fillRect(exit.x(), exit.y(), exit.width(), exit.height());
	    g.setColor(Color.WHITE);
	    g.drawString("Quit", exit.x() + 20, exit.y() + 25);
	    
	    g.setColor(Color.RED);
	    g.fillRect(sac.x(), sac.y(), sac.width(), sac.height());
	    g.setColor(Color.WHITE);
	    g.drawString("Sac", sac.x() + 20, sac.y() + 25);
	    
	    int dungeonPixelWidth  = DONJON_WIDTH * ROOM_SIZE;
	    int dungeonPixelHeight = DONJON_HEIGHT * ROOM_SIZE;
	    int dungeonOriginX = (Game.windowWidth()  - dungeonPixelWidth)  / 2;
	    int dungeonOriginY = (Game.windowHeight() - dungeonPixelHeight) / 2;

	    for (int gy = 0; gy < DONJON_HEIGHT; gy++) {
	        for (int gx = 0; gx < DONJON_WIDTH; gx++) {
	            int px = dungeonOriginX + gx * ROOM_SIZE;  // 屏幕上的 x
	            int py = dungeonOriginY + gy * ROOM_SIZE;  // 屏幕上的 y

	            // 用 (px, py) 作为这个房间格子的左上角
	            g.setColor(Color.DARK_GRAY);
	            g.fillRect(px, py, ROOM_SIZE, ROOM_SIZE);

	            g.setColor(Color.WHITE);
	            g.drawRect(px, py, ROOM_SIZE, ROOM_SIZE);
	            
	            
	            if(donjon.getetape().sallesNeighborNonVisite().containsKey(new Coord(gx, gy))) {
	            	if(donjon.getetape().sallesNeighborNonVisite().get(new Coord(gx, gy)).getType()==RoomType.ENEMY) {
	            		if (ImagesBase.ennemi != null) {
	            	        g.drawImage(ImagesBase.ennemi, px, py, ROOM_SIZE, ROOM_SIZE, null);
	            	    } else {
	            	        g.setColor(Color.RED);
	            	        g.drawString("E", px + 10, py + 20); // 兜底标记
	            	    }
	            	}
		            
		            else if(donjon.getetape().sallesNeighborNonVisite().get(new Coord(gx, gy)).getType()==RoomType.GATE) {
		            	if (ImagesBase.gate != null) {
		                    g.drawImage(ImagesBase.gate, px, py, ROOM_SIZE, ROOM_SIZE, null);
		                } else {
		                    g.setColor(Color.RED);
		                    g.drawString("G", px + 10, py + 20); 
		                }
		            }
	            
		            else if(donjon.getetape().sallesNeighborNonVisite().get(new Coord(gx, gy)).getType()==RoomType.EXIT) {
		            	if (ImagesBase.exit != null) {
		                    g.drawImage(ImagesBase.exit, px, py, ROOM_SIZE, ROOM_SIZE, null);
		                } else {
		                    g.setColor(Color.RED);
		                    g.drawString("EXIT", px + 10, py + 20); 
		                }
		            }
	            	
		            else if(donjon.getetape().sallesNeighborNonVisite().get(new Coord(gx, gy)).getType()==RoomType.MERCHANT) {
		            	if (ImagesBase.market != null) {
		                    g.drawImage(ImagesBase.market, px, py, ROOM_SIZE, ROOM_SIZE, null);
		                } else {
		                    g.setColor(Color.RED);
		                    g.drawString("M", px + 10, py + 20); 
		                }
		            }
	            	
	            	
		            else if(donjon.getetape().sallesNeighborNonVisite().get(new Coord(gx, gy)).getType()==RoomType.HEALER) {
		            	if (ImagesBase.heal != null) {
		                    g.drawImage(ImagesBase.heal, px, py, ROOM_SIZE, ROOM_SIZE, null);
		                } else {
		                    g.setColor(Color.RED);
		                    g.drawString("H", px + 10, py + 20); 
		                }
		            }
	            	
		            else if(donjon.getetape().sallesNeighborNonVisite().get(new Coord(gx, gy)).getType()==RoomType.TREASURE) {
		            	if (ImagesBase.tresor != null) {
		                    g.drawImage(ImagesBase.tresor, px, py, ROOM_SIZE, ROOM_SIZE, null);
		                } else {
		                    g.setColor(Color.RED);
		                    g.drawString("T", px + 10, py + 20); 
		                }
		            }
		            
		            
		            else {
	            	g.drawImage(ImagesBase.visible, px, py, ROOM_SIZE, ROOM_SIZE, null);}
	            }
	        }
	    }
	    
	    g.setColor(Color.WHITE);
	    g.setFont(new Font("Arial", Font.PLAIN, 18));
	    String heroInfo = "HP: " + hero.hp() + "/" + hero.maxHp()
	        + "  |  Mana: " + hero.mana() + "/" + hero.maxMana()
	        + "  |  Or: " + hero.or();
	    g.drawString(heroInfo, 10, 100);

	    g.drawString("Étape : " + donjon.etape() + " (ESC pour retourner au menu)", 20, 120);

	}
}


	
