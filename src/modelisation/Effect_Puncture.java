package modelisation;

import java.util.Objects;

public class Effect_Puncture implements Effect {
	private final int dmg;
	private final int puncturePoint;
	
	public Effect_Puncture(int d,int p) {
		if(d<0||p<0) {
			throw new IllegalArgumentException("dmg de puncture est inferieur que 0");
		}
		dmg=d;
		puncturePoint=p;
	}
	
	@Override
	public void apply(Hero h, Ennemi e, Combat c) {
		// TODO Auto-generated method stub
		Objects.requireNonNull(h);
		Objects.requireNonNull(e);
		Objects.requireNonNull(c);
		int cp=0;
		var list=c.getEnnemis();
		var att=list.indexOf(e);
		for(var i:list) {
			if(cp>=puncturePoint) {break;}
			var index=list.indexOf(i);
			if(att>index) {
				continue;
			}
			list.get(index).damage(dmg);
			cp++;
		}
		
	}
}
