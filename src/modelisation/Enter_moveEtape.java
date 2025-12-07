package modelisation;

/**
 * Enter behavior for gate rooms (used to go to another floor).
 */
public class Enter_moveEtape implements Enter {
	@Override
	public RoomType apply(Hero h) {
		// TODO Auto-generated method stub
		return RoomType.GATE;
		
	}
}
