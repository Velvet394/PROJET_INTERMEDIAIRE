package modelisation;

public class Position {
    private int etape;
    private Coord coord;

    public Position(int etape, Coord coord) {
        this.etape = etape;
        this.coord = coord;
    }

    public int getEtape() { return etape; }

    public Coord getCoord() { return coord; }

    public void moveTo(int newEtape, Coord newCoord) {
        this.etape = newEtape;
        this.coord = newCoord;
    }
}
