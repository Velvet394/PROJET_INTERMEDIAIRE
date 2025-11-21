package modelisation;
import java.util.*;
import java.io.*;

public class Etape {
    private final Map<Coord, Room> salles = new HashMap<>();

    public void ajouterSalle(Coord c, Room s) {
        salles.put(c, s);
    }

    public Room getSalle(Coord c) {
        return salles.get(c);
    }
}
