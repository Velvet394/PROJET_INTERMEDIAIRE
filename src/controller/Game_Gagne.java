package controller;
import modelisation.HallOfFame;
import modelisation.HallOfFameEntry;
import modelisation.Hero;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Objects;

import com.github.forax.zen.PointerEvent;

import modelisation.Hero;

public class Game_Gagne implements Ecran {


	private final Game game;
	private final Hero hero;
	private static int WINDOW_WIDTH;
	private static int WINDOW_HEIGHT;
	
	private final java.util.List<HallOfFameEntry> entries;
	
	public Game_Gagne(Game g,Hero h) {
		game=g;
		hero=h;
		WINDOW_HEIGHT=Game.windowHeight();
	    WINDOW_WIDTH=Game.windowWidth();
	    
	    
// 写入
	    HallOfFame.add(HallOfFame.snapshotFromHero(hero));

	    // 加载所有记录用于显示
	    entries = HallOfFame.loadAll();
	}
	
//	public void render(Graphics2D g) {
//		g.setColor(new Color(20, 20, 40));
//	    g.fillRect(0, 0, Game.windowWidth(), Game.windowHeight());
//	    g.setColor(Color.WHITE);
//	    g.setFont(new Font("Arial", Font.BOLD, 24));
//	    String d="Vous Conquerez le Donjon";
//	    g.drawString(d, (WINDOW_WIDTH-d.length())/2, (WINDOW_HEIGHT-d.length())/2);
//	    //g.setColor(Color.WHITE);
//	    //g.setFont(new Font("Arial", Font.BOLD, 24));
//	    //String c="Clique pour quitter de jeu";
//	    //g.drawString(c, (WINDOW_WIDTH-d.length())/2-20, (WINDOW_HEIGHT-d.length())/2);
//	}
	public void render(Graphics2D g) {
		  g.setColor(new Color(20, 20, 40));
		  g.fillRect(0, 0, Game.windowWidth(), Game.windowHeight());

		  g.setColor(Color.WHITE);
		  g.setFont(new Font("Arial", Font.BOLD, 28));
		  g.drawString("Victoire !", 30, 50);

		  g.setFont(new Font("Arial", Font.PLAIN, 18));
		  g.drawString("Cliquez pour quitter.", 30, 80);

		  // 当前通关信息
		  var cur = HallOfFame.snapshotFromHero(hero);
		  g.drawString("Votre run: gold=" + cur.gold()
		      + "  value=" + cur.totalValue()
		      + "  lvl=" + cur.level()
		      + "  hpMax=" + cur.hpMax()
		      + "  manaMax=" + cur.manaMax(), 30, 110);

		  // 名人堂（按装备价值排序，也可以改成按 gold）
		  var sorted = new java.util.ArrayList<>(entries);
		  sorted.sort(java.util.Comparator.comparingInt(HallOfFameEntry::totalValue).reversed());

		  g.setFont(new Font("Arial", Font.BOLD, 20));
		  g.drawString("Hall of Fame (Top 10 by totalValue)", 30, 150);

		  g.setFont(new Font("Arial", Font.PLAIN, 16));
		  int y = 180;
		  int limit = Math.min(10, sorted.size());
		  for (int i = 0; i < limit; i++) {
		    var e = sorted.get(i);
		    String weapons = String.join(",", e.weapons());
		    if (weapons.length() > 50) weapons = weapons.substring(0, 50) + "...";

		    g.drawString(
		        (i + 1) + ") " + e.timestamp()
		            + " | gold=" + e.gold()
		            + " | value=" + e.totalValue()
		            + " | lvl=" + e.level()
		            + " | hpMax=" + e.hpMax()
		            + " | manaMax=" + e.manaMax()
		            + " | " + weapons,
		        30, y
		    );
		    y += 22;
		    if (y > Game.windowHeight() - 30) break;
		  }
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
