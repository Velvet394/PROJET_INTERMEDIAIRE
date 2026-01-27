package modelisation;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry of room templates used for random floor generation.
 */
public class RoomBase {
	public static final List<Room> templates = new ArrayList<>();
	public static final int NUMROOM = 8;

    static {
        templates.add(new Room(RoomType.ENEMY, new Enter_Combat())); //nom, hp, dmg, blockpoint, healpoint tendance att, skill, def, heal , buff
        templates.add(new Room(RoomType.TREASURE, new Enter_Tresor()));
        templates.add(new Room(RoomType.MERCHANT, new Enter_Market()));
        templates.add(new Room(RoomType.HEALER, new Enter_Healer()));
        templates.add(new Room(RoomType.EMPTY, new Enter_Empty()));
        templates.add(new Room(RoomType.DECMAL, new Enter_Decmal()));
        templates.add(new Room(RoomType.GATE, new Enter_moveEtape()));
        templates.add(new Room(RoomType.EXIT, new Enter_Exit()));
    }
}
