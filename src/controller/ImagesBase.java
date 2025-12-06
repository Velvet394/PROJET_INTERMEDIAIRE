package controller;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImagesBase {
	public static Image visible;
	public static Image visited;
	
	static {
		try {
            visible = ImageIO.read(new File("./image/general/visible.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
            visible = null; 
        }
	}
	
	static {
		try {
			visited = ImageIO.read(new File("./image/general/visited.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
            visited = null; 
        }
	}
}
