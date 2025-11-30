package controller;
import view.*;
import App.*;
import modelisation.*;
import com.github.forax.zen.*;

public class GameController {
    private Hero hero;
    private Donjon donjon;
    private Combat combat;
    private BackPACKVIEW backpackView;
    private CombatView combatView;
    private DonjonView dungeonView;

    public GameController(Hero hero, Donjon donjon) {
        this.hero = hero;
        this.donjon = donjon;
        this.backpackView = new BackPACKVIEW(hero.getBackpack());
        this.combatView = new CombatView(combat);
        this.dungeonView = new DonjonView(donjon);
    }

   
}

