package modelisation;

public class Testmain {
	public static void main(String[] args) {
		 System.out.println("Start main");   // 用标准输出先打个标记

		    IO.println("via IO");
		
		Hero hero = new Hero();
		
		//var combat=new Combat(hero,Generation.genererEnnemis()); 
		
		hero.ajouterDansSac(new Weapon(WeaponBase.templates.get(0)));
		
		//combat.startCombat();
	
		Donjon donjon=Generation.genererDonjon();
		
		hero.ajouterDansSac(new Weapon(WeaponBase.templates.get(0)));
		
		for(int i=0;i<5;i++) {
			for(int j=0;j<11;j++) {
				donjon.moveSalle(hero, new Coord(i,j));
			}
		}
		IO.println("salles finit");
		
	}
}
