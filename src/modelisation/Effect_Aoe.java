package modelisation;

/**
 * An area-of-effect attack that damages every enemy in the combat.
 */
public class Effect_Aoe implements Effect {
	private final int dmg;
	
	public Effect_Aoe(int d) {
		// TODO Auto-generated constructor stub
		if(d<0) {
			throw new IllegalArgumentException("dmg de Aoe est inferieur que 0");
		}
		
		dmg=d;
	}
	
	/**
     * Deals area damage to all enemies in the combat.
     */
	@Override
	public void apply(Hero h, Ennemi e, Combat c) {
		// TODO Auto-generated method stub
		var list = c.getEnnemis();
		for(var i:list) {
			i.damage(dmg);
		}
	}
}
