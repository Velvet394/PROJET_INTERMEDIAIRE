package modelisation;

/**
 * Represents an action executed when the hero enters a room.
 * Each implementation corresponds to a type of room (combat, market, treasure, etc.).
 */
public interface Enter {
	RoomType apply(Hero h);
}
