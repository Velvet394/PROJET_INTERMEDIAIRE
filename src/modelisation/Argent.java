package modelisation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record Argent(int num,Coord set) implements Item {
	
	public Argent {
		if(num<0) {
			throw new IllegalArgumentException("num de Argent error");
		}
		Objects.requireNonNull(set);
	}
	
	public Argent(int n) {
		if(n<0) {
			throw new IllegalArgumentException("num de Argent error");
		}
		this(n,new Coord(0, 0));
	}
	
	public Argent confision(Argent a) {
		Objects.requireNonNull(a);
		return new Argent(a.num+this.num);
	}
	
	public Argent use(Argent u) {
		Objects.requireNonNull(u);
		if(num<=u.num) {
			throw new IllegalArgumentException("Error use Argent");
		}
		return new Argent(num-u.num);
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
