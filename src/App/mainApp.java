package App;
import view.*;
import controller.*;

import java.awt.Color;

import com.github.forax.zen.Application;
public class mainApp {

	public static void main(String[] args) {
		Application.run(Color.BLACK, context -> {
            // Lancer le menu principal
            new MenuPrincipalApp();
        });

	}

}



