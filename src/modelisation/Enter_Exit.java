package modelisation;

/**
 * Enter behavior for exit rooms.
 * Used to represent the end of the floor.
 */
public class Enter_Exit implements Enter{
	@Override
	public RoomType apply(Hero h) {
		// TODO Auto-generated method stub
		return RoomType.EXIT;
	}
}
