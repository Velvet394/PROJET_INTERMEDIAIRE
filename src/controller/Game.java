package controller;

import modelisation.*;

public class Game {
	private final Hero hero;
	private final Donjon donjon;
	private static int WINDOW_WIDTH;
    private static int WINDOW_HEIGHT;
    private static final int ROOM_SIZE = 100;
    private static final int DONJON_WIDTH = 5;
    private static final int DONJON_HEIGHT = 5;

    private static int selectedX = 0;
    private static int selectedY = 0;
	
	public Game() {
		hero=new Hero();
		donjon=Generation.genererDonjon();
	}
	
	public void start() {
		
	}
	
}
