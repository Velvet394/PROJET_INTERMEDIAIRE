package modelisation;

import java.util.List;

public class Enter_Combat implements Enter {
	@Override
	public void apply(Hero h) {
		List<Ennemi> ennemis = Generation.genererEnnemis();  // Assurez-vous que cette méthode renvoie une liste valide

        if (ennemis == null || ennemis.isEmpty()) {
            // Gérer le cas où il n'y a pas d'ennemis à combattre
            IO.println("Aucun ennemi n'a été généré pour le combat !");
            return;  // Retourne si pas d'ennemis
        }
		var combat=new Combat(h, ennemis);
		combat.startCombat();
	}
	/*
	public List<Ennemi> genererEnnemis() {
        return EnnemisBase.templates;  // Ici vous pouvez ajuster la logique de génération des ennemis si besoin
    }*/
}
