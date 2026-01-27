package modelisation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Gold implements Item {
	private int num;
	private Coord set;
	
	public Gold(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("num de Gold error");
        }
        this.num = n;
        this.set = new Coord(0, 0);
    }
		
	
	@Override
	public void rotation() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void translate(Coord offset) {
		set=offset;	
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
	
	@Override
	public String nom() {
		// TODO Auto-generated method stub
		return "Gold";
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
	
	public int num() {
		return num;
	}


	@Override
	public void interaction(Hero h, Integer i) {
		// TODO Auto-generated method stub
		
	}
	
}
