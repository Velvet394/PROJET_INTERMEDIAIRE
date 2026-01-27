package controller;

import java.awt.Color;

import com.github.forax.zen.*;

import modelisation.*;

public class Game {
    private final Hero hero;
    private final Donjon donjon;

    private static int WINDOW_WIDTH;
    private static int WINDOW_HEIGHT;


    private final Ecran donjonEcran;
    private Ecran combatEcran;
    private Ecran marketEcran;
    private Ecran healEcran;
    private final Ecran menuEcran;
    private Ecran tresorEcran;
    private Ecran lvupEcran;
    private Ecran MortEcran;
    private Ecran GagneEcran;


    private Ecran currentEcran;

    public Game(Hero hero, Donjon donjon) {
        this.hero = hero;
        this.donjon = donjon;
        menuEcran=new Game_Menu(this);

        this.donjonEcran = new Game_Donjon(hero, donjon,this);
        //this.combatScreen   = new Game_Combat(this, hero);

        this.currentEcran  = menuEcran; 
    }

    // 提供给屏幕切换用
    public void goToDonjon() { currentEcran = donjonEcran; }
    public void goToCombat()  { 
    	var c=new Combat(hero, Generation.genererEnnemis());
    	combatEcran=new Game_Combat(c,this) ; 
    	currentEcran = combatEcran; }
    public void goToMarket()  { 
    	marketEcran=new Game_Market(this, Generation.genererArticles(), hero);
    	currentEcran = marketEcran; }
    public void goToHeal()  { /*currentEcran = healEcran;*/ }
    public void goToMenu()  { currentEcran = menuEcran; }
    public void goToTresor() {{ 
    	tresorEcran=new Game_Tresor(this, Generation.genererArticles(), hero);
    	currentEcran = tresorEcran; }}
    
    public void goTolvup() {
    	lvupEcran=new Game_lvup(this, hero);
    	currentEcran=lvupEcran;
    }
    public void goToMort() {
    	MortEcran=new Game_Mort(this);
    	currentEcran=MortEcran;
    }
    
    public void goToGagne() {
    	GagneEcran=new Game_Gagne(this);
    	currentEcran=GagneEcran;
    }

    public void start() {
        Application.run(Color.BLACK, context -> {
            var screenInfo = context.getScreenInfo();
            WINDOW_WIDTH  = screenInfo.width();
            WINDOW_HEIGHT = screenInfo.height();

            while (true) {
            	hero.getBackpack().interaction_refresh(hero);
                Event e = context.pollEvent();
                if (e != null) {
                    switch (e) {
                        case KeyboardEvent k -> currentEcran.handleKeyboard(k);
                        case PointerEvent  p -> currentEcran.gererClique(p);
                        default -> {}
                    }
                }

                context.renderFrame(g -> {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
                    currentEcran.render(g);   // 👈 关键：把画图交给当前屏幕
                });
            }
        });
    }

    public static int windowWidth()  { return WINDOW_WIDTH; }
    public static int windowHeight() { return WINDOW_HEIGHT; }
    
    public Hero hero() {
    	return hero;
    }
}