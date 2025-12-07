package controller;

import java.util.List;

import modelisation.Coord;
import modelisation.Donjon;
import modelisation.Effect;
import modelisation.Generation;
import modelisation.Hero;
import modelisation.Weapon;
import modelisation.WeaponBase;

public class Main {
	
	public static void main(String[] args) {
		
		//-----game----
		Weapon durandal = new Weapon(WeaponBase.templates.get(0)); // épée verticale
	     Weapon shield   = new Weapon(WeaponBase.templates.get(1)); // bouclier 2x2
	     Weapon manaStone= new Weapon(WeaponBase.templates.get(2)); // pierre de mana
	     Weapon gold=new Weapon(
	    	        "Gold",                     // 名字
	    	        0,                          // attack = 0
	    	        0,                          // 其他数值 = 0
	    	        false,                      // 和你示例保持一致
	    	        List.<Coord>of(new Coord(0,0)), // 形状只有 1 格
	    	        List.<Effect>of(),          // 没有效果
	    	        "./image/weapons/gold.png"  // 你可以改路径
	    	    );
	     
	     Hero hero = new Hero();
	     durandal.translate(new Coord(0, 0));
	     shield.translate(new Coord(1, 0));
	     manaStone.translate(new Coord(3, 0));
	     gold.translate(new Coord(1,2));
	     
	     hero.ajouterDansSac(durandal);
	        hero.ajouterDansSac(shield);
	        hero.ajouterDansSac(manaStone);
	        hero.ajouterDansSac(gold);
	     
	     Donjon donjon = Generation.genererDonjon();
	     
	     var game=new Game(hero,donjon);
	     game.start();
	}
}
