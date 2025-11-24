package modelisation;

import java.util.List;
import java.util.Objects;

public class ItemMaldiction implements Item {
	private final String name="Maldiction";
	private final List<Coord> forme;
	private Coord offsetCoord=new Coord(0,0);
	
	public ItemMaldiction(List<Coord> l) {
		Objects.requireNonNull(l);
		// TODO Auto-generated constructor stub
		
		forme=l;
	}
	
	@Override
	public List<Coord> forme() {
		// TODO Auto-generated method stub
		return forme;
	}
	
	@Override
	public Coord offsetCoord() {
		// TODO Auto-generated method stub
		return offsetCoord;
	}
}

