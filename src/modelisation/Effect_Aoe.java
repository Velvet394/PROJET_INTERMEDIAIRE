package modelisation;

public class Effect_Aoe implements Effect {
	private final int dmg;
	
	public Effect_Aoe(int d) {
		// TODO Auto-generated constructor stub
		if(d<0) {
			throw new IllegalArgumentException("dmg de Aoe est inferieur que 0");
		}
		
		dmg=d;
	}
	
	@Override
	public void apply(Hero h, Ennemi e, Combat c) {
		// TODO Auto-generated method stub
		var list = c.getEnnemis();
		for(var i:list) {
			i.damage(dmg);
		}
	}
}
