package modelisation;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Generation {
	public List<Ennemi> genererEnnemis(Random r){
		Objects.requireNonNull(r);
		var num=r.nextInt(10);//nombres des ennemis max
		for(int i=0;i<num;i++) {
			
		}
	}
	
	public List<Item> genererItems(){}
}
