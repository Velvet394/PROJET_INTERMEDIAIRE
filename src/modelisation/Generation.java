package modelisation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Generation {
	public List<Ennemi> genererEnnemis(){
		var num=Dice.roll(1, 5);//nombres des ennemis max [1,5] tous inclus
		ArrayList<Ennemi> list=new ArrayList<>();
		for(int i=0;i<num;i++) {
			list.add(new Ennemi(EnnemisBase.templates.get(Dice.roll(0, EnnemisBase.NUMENN-1))));
		}
		return list;
	}
	
	//public List<Item> genererItems(){}
}
