package modelisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * Static registry of Ennemies, Weapons, Items, Floors and Dungeons
 * */
public class Generation {
	
	/**
	   * Generates a random list of enemies for a combat.
	   */
	public static List<Ennemi> genererEnnemis(){
		var num=Dice.roll(1, 5);//nombres des ennemis max [1,5] tous inclus
		ArrayList<Ennemi> list=new ArrayList<>();
		for(int i=0;i<num;i++) {
			int j=Dice.roll(0, 100);
			if(j<75) {
				list.add(new Ennemi(EnnemisBase.templates.get(Dice.roll(0, 1))));
			}
			else if(j<95) {
				list.add(new Ennemi(EnnemisBase.templates.get(Dice.roll(2, 3))));
			}
			else{list.add(new Ennemi(EnnemisBase.templates.get(Dice.roll(4, 5))));}
		}
		return list;
	}
	
	/**
	   * Generates a random list of items (usually used for treasure rewards).
	*/
	public static List<Item> genererItems(){
		var num=Dice.roll(1, 3);//nombres des tresor max [1,5] tous inclus
		ArrayList<Item> list=new ArrayList<>();
		for(int i=0;i<num;i++) {
			list.add(new Weapon(WeaponBase.templates.get(Dice.roll(0, WeaponBase.NUMITM-1))));
		}
		return list;
	}
	
	/**
	   * Generates a random list of weapons (for example, merchant inventory).
	*/
	public static List<Weapon> genererArticles(){
		var num=Dice.roll(1, 4);//nombres des tresor max [1,5] tous inclus
		ArrayList<Weapon> list=new ArrayList<>();
		for(int i=0;i<num;i++) {
			list.add(new Weapon(WeaponBase.templates.get(Dice.roll(0, WeaponBase.NUMITM-1))));
		}
		return list;
	}
	
	/**
	   * Generates a random map of rooms for an {@link Etape} (one floor).
    */
//	public static Map<Coord, Room> genererEtape(){
//		var map=new HashMap<Coord,Room>();
//		for(int i=0;i<5;i++) {
//			for(int j=0;j<11;j++) {
//				map.put(new Coord(i,j), new Room(RoomBase.templates.get(Dice.roll(0, RoomBase.NUMROOM-1))));
//			}
//		}
//		return map;
//	}
	public static Map<Coord, Room> genererEtape() {
	    final int W = 5;
	    final int H = 11;

	    var map = new HashMap<Coord, Room>();

	    // RoomBase 里你的模板顺序是：
	    // 0 ENEMY, 1 TREASURE, 2 MERCHANT, 3 HEALER, 4 EMPTY, 5 GATE, 6 EXIT (noublier pas les nouvelle room comme decmal)
	    final int IDX_GATE = RoomBase.NUMROOM - 2;
	    final int IDX_EXIT = RoomBase.NUMROOM - 1;
	    final int NORMAL_COUNT = RoomBase.NUMROOM - 2; // 只随机 0..4，不包含 GATE/EXIT

	    // ---- 随机选出 1 个 EXIT + 2 个 GATE 的坐标（确保互不相同）----
	    Coord exit = new Coord(Dice.roll(0, W - 1), Dice.roll(0, H - 1));
	    while(exit.equals(new Coord(0,0))) {exit = new Coord(Dice.roll(0, W - 1), Dice.roll(0, H - 1));}

	    Coord gate1;
	    do {
	        gate1 = new Coord(Dice.roll(0, W - 1), Dice.roll(0, H - 1));
	    } while (gate1.equals(exit)||gate1.equals(new Coord(0,0)));

	    Coord gate2;
	    do {
	        gate2 = new Coord(Dice.roll(0, W - 1), Dice.roll(0, H - 1));
	    } while (gate2.equals(exit) || gate2.equals(gate1)||gate2.equals(new Coord(0,0)));

	    // ---- 填充整层 ----
	    for (int i = 0; i < W; i++) {
	        for (int j = 0; j < H; j++) {
	            Coord c = new Coord(i, j);

	            if (c.equals(exit)) {
	                map.put(c, new Room(RoomBase.templates.get(IDX_EXIT)));
	            } else if (c.equals(gate1) || c.equals(gate2)) {
	                map.put(c, new Room(RoomBase.templates.get(IDX_GATE)));
	            } else {
	                // 普通房间：只从 0..4 随机（不包含 GATE/EXIT）
	                int idx = Dice.roll(0, NORMAL_COUNT);
	                map.put(c, new Room(RoomBase.templates.get(idx)));
	            }
	        }
	    }
	    return map;
	}
	
   /**
	* Generates a complete dungeon with several floors.
	*/
	public static Donjon genererDonjon() {
		var map=new Donjon();
		for(int i=0;i<=Donjon.maxEtape;i++) {
			map.ajouterEtape(i, new Etape());
		}
		return map;
	}
}
