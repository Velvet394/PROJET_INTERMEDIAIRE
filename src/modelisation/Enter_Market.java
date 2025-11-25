package modelisation;

import java.util.ArrayList;
import java.util.List;

public class Enter_Market implements Enter {
	public final List<Item> items;
	
	public Enter_Market() {
		items=new ArrayList<>(Generation.genererItems());
	}
	
	@Override
	public void apply(Hero h) {
		// TODO Auto-generated method stub
		
	}
}
