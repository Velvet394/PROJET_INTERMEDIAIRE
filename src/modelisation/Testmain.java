/*package modelisation;

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
*/

package modelisation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Testmain {

    public static void main(String[] args) {
        IO.println("=== Début du Testmain Donjon / Sac / Combat ===");

        // 1) Création du héros
        Hero hero = new Hero();
        IO.println("Héros créé : HP = " + hero.hp() + "/" + hero.maxHp()
                + ", mana = " + hero.mana() + "/" + hero.maxMana());

        // 2) Préparation des armes depuis les templates
        Weapon durandal = new Weapon(WeaponBase.templates.get(0)); // épée verticale
        Weapon shield   = new Weapon(WeaponBase.templates.get(1)); // bouclier 2x2
        Weapon manaStone= new Weapon(WeaponBase.templates.get(2)); // pierre de mana

        // 2.1) Rotation / translation des armes (test des opérations de forme)
        // Durandal : on la laisse verticale, placée en (0,0)
        durandal.translate(new Coord(0, 0));

        // Shield : on le fait pivoter une fois, puis on le place à droite de Durandal
        shield.rotation(); // test de rotation
        shield.translate(new Coord(1, 0));

        // ManaStone : on la place plus à droite
        manaStone.translate(new Coord(3, 0));

        // 2.2) Placement dans le sac du héros
        hero.ajouterDansSac(durandal);
        hero.ajouterDansSac(shield);
        hero.ajouterDansSac(manaStone);

        // 2.3) Affichage du contenu du sac (coord -> type d'Item)
        IO.println("\n=== Contenu initial du sac du héros ===");
        afficherSac(hero);

        // 3) Génération du donjon complet
        Donjon donjon = Generation.genererDonjon();
        IO.println("\n=== Donjon généré, début de l'exploration ===");

        // On parcourt toutes les étapes (0 à Donjon.maxEtape)
        for (int niveau = 0; niveau <= Donjon.maxEtape; niveau++) {
            Etape etape = donjon.getEtape(niveau);
            IO.println("\n--- Étape " + niveau + " ---");

            // Tant qu'il reste des salles voisines non visitées, on avance
            boolean encoreDesSalles = true;
            while (encoreDesSalles) {
                Map<Coord, Room> voisins = etape.sallesNeighborNonVisite();
                if (voisins.isEmpty()) {
                    encoreDesSalles = false;
                    IO.println("Plus aucune salle voisine non visitée sur cette étape.");
                    break;
                }

                // On choisit arbitrairement la première salle voisine
                Coord choix = voisins.keySet().iterator().next();
                Room salle = voisins.get(choix);

                IO.println("\nLe héros se déplace vers la salle " + choix
                        + " de type " + salle.getType());

                // IMPORTANT :
                // On suppose que Donjon.moveSalle(hero, choix) :
                //  - vérifie que la salle est voisine non visitée,
                //  - appelle Room.visiter(hero),
                //  - donc déclenche Enter.apply(hero) = combat / trésor / etc,
                //  - puis Etape.RefreshListNeighbor().
                donjon.moveSalle(hero, choix);

                // Affiche l'état du héros après la salle (combat, soin, trésor, etc.)
                IO.println("État du héros après cette salle : "
                        + hero.hp() + "/" + hero.maxHp() + " PV, "
                        + hero.mana() + "/" + hero.maxMana() + " mana.");

                // On peut aussi réafficher le sac pour vérifier les changements
                IO.println("Aperçu du sac après cette salle :");
                afficherSac(hero);
            }

            if (niveau < Donjon.maxEtape) {
                IO.println("\nÉtape " + niveau + " terminée, passage à l'étape suivante.");
                donjon.moveEtape(); // met à jour l'indice d'étape interne du Donjon
            } else {
                IO.println("\nDernière étape du donjon terminée.");
            }
        }

        IO.println("\n=== Exploration du donjon terminée ===");
        IO.println("Testmain terminé.");
    }

    /**
     * Affiche le contenu du sac du héros sous forme :
     * (x,y) : nom de l'item ou [vide]
     */
    private static void afficherSac(Hero hero) {
        Map<Coord, Item> contenu = hero.getBackpack().contenu();
        List<Coord> coords = new ArrayList<>(contenu.keySet());
        // Tri pour un affichage propre (d'abord y, puis x ou inverse : ici x puis y)
        coords.sort(Comparator.<Coord>comparingInt(Coord::x)
                              .thenComparingInt(Coord::y));

        for (Coord c : coords) {
            Item item = contenu.get(c);
            String texte;
            if (item == null) {
                texte = "[vide]";
            } else if (item instanceof Weapon w) {
                texte = "Weapon(" + w.name() + ")";
            } else if (item instanceof ItemMaldiction) {
                texte = "Malediction";
            } else {
                texte = item.getClass().getSimpleName();
            }
            IO.println("  " + c + " : " + texte);
        }
    }
}