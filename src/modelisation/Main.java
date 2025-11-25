package modelisation;
import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args) {

        // 1. 创建英雄
        Hero hero = new Hero();
        System.out.println("Héros créé avec " + hero.hp() + " HP et " + hero.mana() + " mana.");

        // 2. 随机生成一批敌人
        List<Ennemi> ennemis = Generation.genererEnnemis();
        System.out.println("Nombre d'ennemis générés : " + ennemis.size());

        // 3. 用这些敌人创建一个 Combat
        Combat combat = new Combat(hero, ennemis);

        // 4. 准备一把武器模板（简单起见，就用 WeaponBase.templates[0]）
        Weapon weaponTemplate = WeaponBase.templates.get(0);
        System.out.println("Arme utilisée pour le test : " + weaponTemplate.nom());

        System.out.println("\n===== Début du combat de test (sans interface graphique) =====");

        int tour = 1;
        // 我们限制最多打 10 个回合，避免逻辑有问题时卡死
        while (combat.estCombatTermine() == 1 && tour <= 10) {
            System.out.println("\n----- Tour " + tour + " : tour du héros -----");

            // 每个英雄回合，重置能量
            hero.rechargerCombat();

            if (!ennemis.isEmpty()) {
                // 选第一个敌人作为目标
                Ennemi cible = combat.getEnnemi(0);

                // 从模板复制一把武器（避免直接修改模板）
                Weapon arme = new Weapon(weaponTemplate);

                System.out.println("Le héros utilise " + arme.nom() + " sur le premier ennemi.");
                // 这里会自动检查 cost / mana，并应用所有 Effect
                arme.utiliser(hero, cible, combat);
            } else {
                System.out.println("Il n'y a plus d'ennemis.");
            }

            // 刷新一下敌人列表，移除已经死亡的
            combat.RefreshListEnnemis();

            // 打印当前状态
            afficherEtat(hero, ennemis);

            // 检查战斗是否已经结束（2 = tous les ennemis morts, 3 = héros mort, 1 = continue）
            int etat = combat.estCombatTermine();
            if (etat != 1) break;

            System.out.println("\n----- Tour " + tour + " : tour des ennemis -----");

            // 敌人行动（调用你在 Combat 里已经写好的逻辑）
            combat.tourEnnemis();

            // 再打印一次状态
            afficherEtat(hero, ennemis);

            // 再检查一次战斗是否结束
            etat = combat.estCombatTermine();
            if (etat != 1) break;

            tour++;
        }

        int resultat = combat.estCombatTermine();
        System.out.println("\n===== Fin du combat de test =====");
        if (resultat == 2) {
            System.out.println("Victoire du héros !");
        } else if (resultat == 3) {
            System.out.println("Le héros est mort...");
        } else {
            System.out.println("Combat interrompu (limite de tours atteinte ou logique à vérifier).");
        }
    }

    /**
     * 打印当前英雄与敌人的简单状态
     */
    private static void afficherEtat(Hero hero, List<Ennemi> ennemis) {
        System.out.println("État du héros : HP " + hero.hp() + "/" + hero.maxHp()
                + ", mana " + hero.mana() + "/" + hero.maxMana());

        if (ennemis.isEmpty()) {
            System.out.println("Il ne reste plus aucun ennemi.");
        } else {
            System.out.println("État des ennemis :");
            int i = 0;
            for (Ennemi e : new ArrayList<>(ennemis)) {
                String status = e.estMort() ? "mort" : "vivant";
                System.out.println("  Ennemi " + i + " : " + status);
                i++;
            }
        }
    }
}
