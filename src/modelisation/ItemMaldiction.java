package modelisation;

import java.util.List;
import java.util.Objects;

/**
 * Represents a curse item placed in the backpack.
 */
public final class ItemMaldiction implements Item {
	private final String name="Maldiction";
	private final List<Coord> forme;
	private Coord offsetCoord=new Coord(0,0);
	
	public ItemMaldiction(List<Coord> l) {
		Objects.requireNonNull(l);
		// TODO Auto-generated constructor stub
		
		forme=l;
	}
	
	@Override
	public void rotation() {
		// TODO Auto-generated method stub
		IO.println("Peut pas rotation");
	}
	
	public void translate(Coord offset) {
		 Objects.requireNonNull(offset);
		 offsetCoord=offset;
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
	
	@Override
	public boolean equals(Object o) {
	    return o instanceof ItemMaldiction other
	    		&& Objects.equals(forme, other.forme)
	            && Objects.equals(offsetCoord, other.offsetCoord);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(forme, offsetCoord);
	}

	@Override
	public String nom() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void interaction(Hero h,Integer i) {
		return;
	}
}

