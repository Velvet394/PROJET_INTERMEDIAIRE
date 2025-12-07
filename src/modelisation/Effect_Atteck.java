package modelisation;

/**
 * An attack effect.
 */
public class Effect_Atteck implements Effect {
private final int dmg;
	
	public Effect_Atteck(int d) {
		// TODO Auto-generated constructor stub
		if(d<0) {
			throw new IllegalArgumentException("dmg de Aoe est inferieur que 0");
		}
		
		dmg=d;
	}
	
	@Override
	public void apply(Hero h, Ennemi e, Combat c) {
		// TODO Auto-generated method stub
		e.damage(dmg);
	}
}
