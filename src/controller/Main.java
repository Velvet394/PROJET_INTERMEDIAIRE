package controller;

import modelisation.Coord;
import modelisation.Donjon;
import modelisation.Generation;
import modelisation.Hero;
import modelisation.Weapon;
import modelisation.WeaponBase;

public class Main {
	public static void main(String[] args) {
		Weapon durandal = new Weapon(WeaponBase.templates.get(0)); // épée verticale
	     Weapon shield   = new Weapon(WeaponBase.templates.get(1)); // bouclier 2x2
	     Weapon manaStone= new Weapon(WeaponBase.templates.get(2)); // pierre de mana
	     
	     Hero hero = new Hero();
	     durandal.translate(new Coord(0, 0));
	     shield.translate(new Coord(1, 0));
	     manaStone.translate(new Coord(3, 0));
	     
	     hero.ajouterDansSac(durandal);
	        hero.ajouterDansSac(shield);
	        hero.ajouterDansSac(manaStone);
	     
	     Donjon donjon = Generation.genererDonjon();
	     
	     var game=new Game(hero,donjon);
	     game.start();
	}
}
