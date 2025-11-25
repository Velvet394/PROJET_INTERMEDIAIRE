package modelisation;

public class Enter_Combat implements Enter {
	@Override
	public void apply(Hero h) {
		// TODO Auto-generated method stub
		var combat=new Combat(h, Generation.genererEnnemis());
		combat.startCombat();
	}
}
