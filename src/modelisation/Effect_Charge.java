package modelisation;

public class Effect_Charge implements Effect{
	private final int chargePoint;
	
	public Effect_Charge(int c) {
		// TODO Auto-generated constructor stub
		if(c<0) {
			throw new IllegalArgumentException("EffectCharge argument error");
		}
		
		chargePoint=c;
	}
	
	@Override
	public void apply(Hero h, Ennemi e, Combat c) {
		// TODO Auto-generated method stub
		h.charge(chargePoint);
	}
}
