package modelisation;

import java.util.ArrayList;
import java.util.List;

public class RoomBase {
	public static final List<Room> templates = new ArrayList<>();
	public static final int NUMROOM = 5;

    static {
        templates.add(new Room(RoomType.ENEMY, new Enter_Combat())); //nom, hp, dmg, blockpoint, healpoint tendance att, skill, def, heal , buff
        templates.add(new Room(RoomType.TREASURE, new Enter_Tresor()));
        templates.add(new Room(RoomType.MERCHANT, new Enter_Market()));
        templates.add(new Room(RoomType.HEALER, new Enter_Healer()));
    }
}
