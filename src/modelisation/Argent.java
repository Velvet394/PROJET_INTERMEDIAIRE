package modelisation;

import java.util.Objects;

public record Argent(int num,Coord forme) {
	
	public Argent {
		if(num<0) {
			throw new IllegalArgumentException("num de Argent error");
		}
		Objects.requireNonNull(forme);
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
	
	public Argent use(int c) {
		if(num<=c) {
			throw new IllegalArgumentException("Error use Argent");
		}
		return new Argent(num-c);
	}
	
}
