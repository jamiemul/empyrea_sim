import java.util.ArrayList;
import java.util.List;

enum TileState {
    OCEAN, GRASS, MOUNTAIN
}

enum Element {
    FIRE, AIR, EARTH, WATER
}

public class Tile {
    private TileState state;
    private List<Element> elements;
    private List<Unit> followers; // Add followers
    private List<Unit> prophets; // Add prophets
    private List<Token> tokens;
    private List<Tile> neighbors;

    public Tile(TileState state, List<Element> elements) {
        this.state = state;
        this.elements = elements;
        this.followers = new ArrayList<>();
        this.prophets = new ArrayList<>();
        this.tokens = new ArrayList<>();
        this.neighbors = new ArrayList<>();
    }

    public TileState getState() {
        return state;
    }

    public void setState(TileState state) {
        this.state = state;
    }

    public List<Unit> getFollowers() {
        return followers;
    }

    public void convertFollowers(Player player) {
        for (Unit follower : followers) {
            follower.setOwner(player);
        }
    }

    public List<Unit> getProphets() {
        return prophets;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void addToken(Token token) {
        tokens.add(token);
    }

    public void addNeighbor(Tile neighbor) {
        neighbors.add(neighbor);
    }

    public List<Tile> getNeighbors() {
        return neighbors;
    }

    public List<Element> getElements() {
        return elements;
    }
}
