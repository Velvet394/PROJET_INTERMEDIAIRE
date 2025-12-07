package modelisation;
/**
 * Enter behavior for healer rooms.
 * The hero can recover HP in those rooms.
 */
public class Enter_Healer implements Enter {

	@Override
	public RoomType apply(Hero h) {
		// TODO Auto-generated method stub
		//h.heal(Dice.roll(3, 10));
		return RoomType.HEALER;
	}

}
