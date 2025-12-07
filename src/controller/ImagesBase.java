package controller;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImagesBase {
	public static Image visible;
	public static Image visited;
	public static Image hero;
	public static Image back;
	public static Image gate;
	public static Image ennemi;
	public static Image exit;
	public static Image market;
	public static Image heal;
	
	static {
		try {
            visible = ImageIO.read(new File("./image/general/visible.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
            visible = null; 
        }
		
		try {
			visited = ImageIO.read(new File("./image/general/visited.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
            visited = null; 
        }
		
		try {
			hero = ImageIO.read(new File("./image/general/hero.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
            hero = null; 
        }
		
		try {
			gate = ImageIO.read(new File("./image/general/gate.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
            gate = null; 
        }
		
		try {
			ennemi = ImageIO.read(new File("./image/general/ennemi.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
            ennemi = null; 
        }
		
		try {
            back = ImageIO.read(new File("./image/general/back.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
            back = null; 
        }
		
		try {
			exit = ImageIO.read(new File("./image/general/exit.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
            exit = null; 
        }
		
		try {
			market = ImageIO.read(new File("./image/general/market.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
            market = null; 
        }
		
		try {
			heal = ImageIO.read(new File("./image/general/heal.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
            heal = null; 
        }
		
		
	}
	

}
