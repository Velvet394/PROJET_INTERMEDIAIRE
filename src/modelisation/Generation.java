package modelisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class Generation {
	public static List<Ennemi> genererEnnemis(){
		var num=Dice.roll(1, 5);//nombres des ennemis max [1,5] tous inclus
		ArrayList<Ennemi> list=new ArrayList<>();
		for(int i=0;i<num;i++) {
			list.add(new Ennemi(EnnemisBase.templates.get(Dice.roll(0, EnnemisBase.NUMENN-1))));
		}
		return list;
	}
	
	public static List<Item> genererItems(){
		var num=Dice.roll(1, 3);//nombres des tresor max [1,5] tous inclus
		ArrayList<Item> list=new ArrayList<>();
		for(int i=0;i<num;i++) {
			list.add(new Weapon(WeaponBase.templates.get(Dice.roll(0, WeaponBase.NUMITM-1))));
		}
		return list;
	}
	
	public static List<Weapon> genererArticles(){
		var num=Dice.roll(1, 4);//nombres des tresor max [1,5] tous inclus
		ArrayList<Weapon> list=new ArrayList<>();
		for(int i=0;i<num;i++) {
			list.add(new Weapon(WeaponBase.templates.get(Dice.roll(0, WeaponBase.NUMITM-1))));
		}
		return list;
	}
	
	public static Map<Coord, Room> genererEtape(){
		var map=new HashMap<Coord,Room>();
		for(int i=0;i<5;i++) {
			for(int j=0;j<11;j++) {
				map.put(new Coord(i,j), new Room(RoomBase.templates.get(Dice.roll(0, RoomBase.NUMROOM-1))));
			}
		}
		return map;
	}
	
	public static Donjon genererDonjon() {
		var map=new Donjon();
		for(int i=0;i<=Donjon.maxEtape;i++) {
			map.ajouterEtape(i, new Etape());
		}
		return map;
	}//s
}
