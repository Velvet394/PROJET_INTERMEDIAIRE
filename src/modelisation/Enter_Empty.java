package modelisation;

/**
 * Enter behavior for empty rooms.
 * Nothing happens.
 */
public class Enter_Empty implements Enter {
    @Override
    public RoomType apply(Hero h) {
        // Ne fait rien
    	return RoomType.EMPTY;
    }
}
