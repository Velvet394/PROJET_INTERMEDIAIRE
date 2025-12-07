package modelisation;

public class Enter_Tresor implements Enter {
	
	@Override
	public RoomType apply(Hero h) {
		// TODO Auto-generated method stub
		return RoomType.TREASURE;
	}
	
	public void apply1(Hero h) {
		// TODO Auto-generated method stub
		h.tresor(Generation.genererItems());
		
	}
}
