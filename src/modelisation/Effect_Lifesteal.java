package modelisation;

/**
 * An attack effect that also heals the hero by the dealt amount.
 */
public class Effect_Lifesteal implements Effect {
	private final int dmg;
	
	public Effect_Lifesteal(int d) {
		if(d<0) {
			throw new IllegalArgumentException("dmg de LifeSteal est inferieur que 0");
		}
		dmg=d;
	}
	
	@Override
	public void apply(Hero h, Ennemi e, Combat c) {
		// TODO Auto-generated method stub
		e.damage(dmg);
		h.heal(dmg);
	}
}
