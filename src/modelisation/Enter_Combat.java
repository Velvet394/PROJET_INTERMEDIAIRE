package modelisation;

import java.util.List;
import java.util.Objects;

public class Enter_Combat implements Enter {
	private Combat combat=null;
	@Override
	public void apply(Hero h) {
		combat=null;
		List<Ennemi> ennemis = Generation.genererEnnemis();  // Assurez-vous que cette méthode renvoie une liste valide

        if (ennemis == null || ennemis.isEmpty()) {
            // Gérer le cas où il n'y a pas d'ennemis à combattre
            IO.println("Aucun ennemi n'a été généré pour le combat !");
            return;  // Retourne si pas d'ennemis
        }
		combat=new Combat(h, ennemis);
		//Objects.requireNonNull(combat);
		//combat.startCombat(); //a revoir si necessaire pour partie graphique
	}
	
	public Combat getcombat() {
		return combat;
	}
/*	
	public List<Ennemi> genererEnnemis() {
        return EnnemisBase.templates;  // Ici vous pouvez ajuster la logique de génération des ennemis si besoin
    }*/
}
