package modelisation;

public class Enter_Tresor implements Enter {
	@Override
	public void apply(Hero h) {
		// TODO Auto-generated method stub
		h.tresor(Generation.genererItems());
		
	}
}
