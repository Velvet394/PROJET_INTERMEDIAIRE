package controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Map;
import java.util.Objects;

import com.github.forax.zen.*;

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
    private static final int DONJON_HEIGHT = 11;
    private static final int GRID_WIDTH = 7;
    private static final int GRID_HEIGHT = 5;
    private static final int CELL_SIZE = 60;
    public Button exit;
    public Button sac;

    private static int selectedX = 0;
    private static int selectedY = 0;
	
	public Game() {
		hero=new Hero();
		donjon=Generation.genererDonjon();
	}
	
	public void RefreshPosition() {}
	
	
	
	//public void visiter() {}
	public void exit() {}
	public void afficheSac(Graphics2D g) {
		
		int backpackPixelWidth  = GRID_WIDTH  * CELL_SIZE;
		int backpackPixelHeight = GRID_HEIGHT * CELL_SIZE;
		int backpackOriginX = (WINDOW_WIDTH  - backpackPixelWidth)  / 2;
		int backpackOriginY = (WINDOW_HEIGHT - backpackPixelHeight) / 2;
		
		for (int gy = 0; gy < GRID_HEIGHT; gy++) {
		    for (int gx = 0; gx < GRID_WIDTH; gx++) {
		        int px = backpackOriginX + gx * CELL_SIZE;
		        int py = backpackOriginY + gy * CELL_SIZE;
		        // 画格子 + 物品
		        g.setColor(Color.DARK_GRAY);
	            g.fillRect(px, py, CELL_SIZE, CELL_SIZE);
	            g.setColor(Color.GRAY);
	            g.drawRect(px, py, CELL_SIZE, CELL_SIZE);
		    }
		}
		
		Map<Coord, Item> contenu = hero.getBackpack().contenu();
	    for (var entry : contenu.entrySet()) {
	        Coord c = entry.getKey();
	        Item item = entry.getValue();
	        if (item == null) continue;

	        int gx = c.x();
	        int gy = c.y();
	        int px = backpackOriginX + gx * CELL_SIZE;
	        int py = backpackOriginY + gy * CELL_SIZE;

	        // 如果是武器，就画图片；否则画文字
	        if (item instanceof Weapon weapon) {
	            Image img = weapon.image();
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
	        }
	    }
		
	}
	
	public void start() {
		Application.run(Color.BLACK, context -> {
            var screenInfo = context.getScreenInfo();
            WINDOW_WIDTH = screenInfo.width();
            WINDOW_HEIGHT = screenInfo.height();
            
            //exit=new Button(WINDOW_WIDTH-30,WINDOW_HEIGHT-30,30,30,"Exit");
            initExit();

            // Boucle principale du jeu
            while (true) {
            	Event e = context.pollEvent();
            	
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
            	
                /*Event Event = context.pollEvent();
                if (Event != null && Event instanceof KeyboardEvent k) {
                    gererDeplacement(k);
                    gererSelection(k);
                }
                
                if (Event != null && Event instanceof PointerEvent p) {
                	if(p.action() == PointerEvent.Action.POINTER_DOWN) {
                		interagirAvecSalle(p);
                }}*/
                
            	context.renderFrame(g -> {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
                    renderDonjon(g);
                });
                
            }
        });
    }
	
	public void initExit() {
	    int buttonWidth = 120;
	    int buttonHeight = 40;
	    int margin = 20;

	    int exitX = WINDOW_WIDTH  - buttonWidth  - margin;
	    int exitY = WINDOW_HEIGHT - buttonHeight - margin;

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
	
	public void gererClique(PointerEvent p) {
		Objects.requireNonNull(p);
		int x=p.location().x();
		int y=p.location().y();
		
		if(exit.isInside(x, y)) {
			exit();
		}
		
		if(exit.isInside(x, y)) {
			afficheSac();
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
	    int dungeonOriginX = (WINDOW_WIDTH  - dungeonPixelWidth)  / 2;
	    int dungeonOriginY = (WINDOW_HEIGHT - dungeonPixelHeight) / 2;

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
	    donjon.moveSalle(hero, target);

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
	    int dungeonOriginX = (WINDOW_WIDTH  - dungeonPixelWidth)  / 2;
	    int dungeonOriginY = (WINDOW_HEIGHT - dungeonPixelHeight) / 2;

	    for (int gy = 0; gy < DONJON_HEIGHT; gy++) {
	        for (int gx = 0; gx < DONJON_WIDTH; gx++) {
	            int px = dungeonOriginX + gx * ROOM_SIZE;  // 屏幕上的 x
	            int py = dungeonOriginY + gy * ROOM_SIZE;  // 屏幕上的 y

	            // 用 (px, py) 作为这个房间格子的左上角
	            g.setColor(Color.DARK_GRAY);
	            g.fillRect(px, py, ROOM_SIZE, ROOM_SIZE);

	            g.setColor(Color.WHITE);
	            g.drawRect(px, py, ROOM_SIZE, ROOM_SIZE);
	        }
	    }
	}
	
}

	
