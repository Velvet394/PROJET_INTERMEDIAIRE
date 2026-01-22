package modelisation;

import java.util.ArrayList;
import java.util.List;

/**
 * Static registry of Weapon
 * */
public class WeaponBase {
	public static final List<Weapon> templates = new ArrayList<>();
	public static final int NUMITM = 3;
	
	static {
        templates.add(new Weapon("Durandal", 2, 5,false,List.<Coord>of(new Coord(0,0),new Coord(0,1),new Coord(0,2)),List.<Effect>of(new Effect_Puncture(12, 4), new Effect_Heal(4)),"./image/weapons/durantal.png",(h,c)->h.intermana(c*5))); // name, cost, manacost, estconsomme, listforme, listeffect
        templates.add(new Weapon("Shield", 1, 0,false,List.<Coord>of(new Coord(0,0),new Coord(0,1),new Coord(1,0),new Coord(1,1)),List.<Effect>of(new Effect_Def(4)),"./image/weapons/shield.png",(h,c)->h.interhp(c*5)));
        templates.add(new Weapon("ManaStone", 0, 0,true,List.<Coord>of(new Coord(0,0),new Coord(0,1)),List.<Effect>of(new Effect_Charge(10)),"./image/weapons/manastone.png",(h,c)->{}));
	}
}
//path image: "./image/weapons/x"