package modelisation;

public class Testmain {
	public static void main(String[] args) {
		Hero hero = new Hero();
		
		var combat=new Combat(hero,Generation.genererEnnemis()); 
		
		combat.startCombat();
		/*
		Donjon donjon=Generation.genererDonjon();
		
		hero.ajouterDansSac(new Weapon(WeaponBase.templates.get(0)));
		
		for(int i=0;i<5;i++) {
			for(int j=0;j<11;j++) {
				donjon.moveSalle(hero, new Coord(i,j));
			}
		}*/
		
		
	}
}
