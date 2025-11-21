package modelisation;
import java.util.*;
import java.io.*;

public class Donjon {
    private final Map<Integer, Etape> etapes = new HashMap<>();

    public void ajouterEtape(int num, Etape e) {
        etapes.put(num, e);
    }

    public Etape getEtape(int num) { return etapes.get(num); }
}
