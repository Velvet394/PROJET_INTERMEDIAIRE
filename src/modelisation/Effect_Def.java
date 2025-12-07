package modelisation;

/**
 * A defensive effect that increases the hero's block for the current turn.
 */
public class Effect_Def implements Effect {
	private final int blockPoint;
	
	public Effect_Def(int b) {
		// TODO Auto-generated constructor stub
		if(b<0) {
			throw new IllegalArgumentException("EffectDef error");
		}
		blockPoint=b;
	}
	
	@Override
	public void apply(Hero h, Ennemi e, Combat c) {
		// TODO Auto-generated method stub
		h.defend(blockPoint);
	}
}
