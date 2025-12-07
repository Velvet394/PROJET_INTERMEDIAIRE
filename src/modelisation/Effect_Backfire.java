package modelisation;

/**
 * A risky effect that damages the enemy and also hurts the hero.
 */
public class Effect_Backfire implements Effect {
	private final int dmg;
	
	public Effect_Backfire(int d) {
		if(d<0) {
			throw new IllegalArgumentException("dmg de backfire inferieur que 0");
		}
		dmg=d;
	}
	
	/**
     * Deals damage to the enemy and half damage to the hero.
     */
	@Override
	public void apply(Hero h, Ennemi e, Combat c) {
		// TODO Auto-generated method stub
		e.damage(dmg);
		h.damage(dmg/2);
	}
}
