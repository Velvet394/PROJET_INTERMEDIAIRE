package modelisation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record Or(int num, Coord set) implements Item {
	public Or {
		if(num<0) {
			throw new IllegalArgumentException("num de Or error");
		}
		Objects.requireNonNull(set);
	}
	
	public Or(int n) {
		if(n<0) {
			throw new IllegalArgumentException("num de Or error");
		}
		this(n,new Coord(0, 0));
	}
	
	public Or confision(Or a) {
		Objects.requireNonNull(a);
		return new Or(a.num+this.num);
	}
	
	public Or use(Or u) {
		Objects.requireNonNull(u);
		if(num<=u.num) {
			throw new IllegalArgumentException("Error use Or");
		}
		return new Or(num-u.num);
	}
	
	@Override
	public Coord offsetCoord() {
		// TODO Auto-generated method stub
		return set;
	}
	
	@Override
	public List<Coord> forme(){
		var i=new ArrayList<Coord>();
		i.add(set);
		return i;
	}
}
