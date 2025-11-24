package modelisation;

public class Effect_Backfire implements Effect {
	private final int dmg;
	
	public Effect_Backfire(int d) {
		if(d<0) {
			throw new IllegalArgumentException("dmg de backfire inferieur que 0");
		}
		dmg=d;
	}
	
	@Override
	public void apply(Hero h, Ennemi e, Combat c) {
		// TODO Auto-generated method stub
		e.damage(dmg);
		h.damage(dmg/2);
	}
}
