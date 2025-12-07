package modelisation;

import java.util.ArrayList;
import java.util.List;
/**
 * Enter behavior for merchant rooms.
 * Creates a list of items that can be bought by the hero.
 */
public class Enter_Market implements Enter {
	public final List<Item> items;
	
	public Enter_Market() {
		items=new ArrayList<>(Generation.genererItems());
	}
	
	@Override
	public RoomType apply(Hero h) {
		// TODO Auto-generated method stub
		//cest partie graphique.
		return RoomType.MERCHANT;
	}
}
