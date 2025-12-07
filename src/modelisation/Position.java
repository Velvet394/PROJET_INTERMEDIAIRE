package modelisation;

/**
 * Represents the position of the hero in the dungeon:
 * which floor (etape) and which coordinate in the map.
 */
public class Position {
    private int etape;
    private Coord coord;

    public Position(int etape, Coord coord) {
        this.etape = etape;
        this.coord = coord;
    }

    public int getEtape() { return etape; }

    public Coord getCoord() { return coord; }

    /**
     * Moves the position to a new floor and coordinate.
     *
     */
    public void moveTo(int newEtape, Coord newCoord) {
        this.etape = newEtape;
        this.coord = newCoord;
    }
    
    public void setCoord(Coord newPos) {
    	coord = newPos;
    }
    
    /**
     * Moves the coordinate by one cell in the given direction.
     */
    public void move(Direction dir) {
    	switch(dir) {
    		case UP:
    			coord = new Coord(coord.x(), coord.y() + 1); 
    			break;
    		case DOWN:
    			coord = new Coord(coord.x(), coord.y() - 1); 
    			break;
    		case LEFT:
    			coord = new Coord(coord.x() - 1, coord.y()); 
    			break;
    		case RIGHT:
    			coord = new Coord(coord.x() + 1, coord.y()); 
    			break;
    		default:
    			break;
    	}
    }
    
}