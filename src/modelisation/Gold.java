package modelisation;

import java.util.Objects;

public class Gold implements Item {
	private int num;
	private Coord set;
	
	public Gold(int n) {
		if(n<0) {
			throw new IllegalArgumentException("num de Argent error");
		}
		set=new Coord(0, 0);
	}
	
	public void confision(Gold g) {
		Objects.requireNonNull(g);
		num+=g.num;
		return;
	}
	
	public boolean peutUse(Gold g) {
		Objects.requireNonNull(g);
		if(num<g.num) {
			return false;
		}
		return true;
	}
	
	public void use(Gold g) {
		Objects.requireNonNull(g);
		if(!peutUse(g)) {return;}
		num-=g.num;
		return;
	}
	
	
	
}
