package modelisation;

public class Effect_Heal implements Effect {
	private final int heal;
	
	public Effect_Heal(int h) {
		// TODO Auto-generated constructor stub
		if(h<0) {
			throw new IllegalArgumentException("heal de EffectHeal est inferieur que 0");
		}
		heal=h;
	}
	
	@Override
	public void apply(Hero h, Ennemi e, Combat c) {
		// TODO Auto-generated method stub
		h.heal(heal);
	}
}
