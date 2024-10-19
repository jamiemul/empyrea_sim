import java.util.ArrayList;
import java.util.List;

enum TileState {
    OCEAN, GRASS, MOUNTAIN
}

enum Element {
    FIRE, AIR, EARTH, WATER
}

enum Token {
    DROUGHT,
    STORM,
    VOLCANO,
    HARVEST
}

public class Tile {

    private TileState state;
    private List<Element> elements;
    private List<Tile> neighbors;
    private List<Unit> units;
    private List<Token> tokens;

    // Constructor for non-central tiles
    public Tile(TileState state, List<Element> element) {
        this.state = state;
        this.elements = element;  // Single element for regular tiles
        this.neighbors = new ArrayList<>();
        this.tokens = new ArrayList<>();
    }

    // Add a neighboring tile to the set of neighbors
    public void addNeighbor(Tile neighbor) {
        neighbors.add(neighbor);
    }

    public TileState getState() {
        return state;
    }

    public List<Element> getElements() {
        return elements;
    }

    public List<Tile> getNeighbors() {
        return neighbors;
    }

    @Override
    public String toString() {
        return "[" + state + ", " + elements + "]";
    }

    public void addUnit(Unit unit) {
        units.add(unit);
    }

    public List<Unit> getUnits() {
        return units;
    }

    public List<Token> getTokens() {
        return tokens; // Add this getter
    }

    public void addToken(Token token) {
        tokens.add(token); // Method to add a token
    }

    public void printUnits() {
        for (Unit unit : units) {
            System.out.print(unit.getType() + "(" + unit.getOwner().getName() + ") ");
        }
    }
}
