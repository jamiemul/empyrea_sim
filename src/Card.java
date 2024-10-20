import java.util.List;
import java.util.function.Consumer;

public class Card {
    private String name;
    private List<Element> cost; // Elements required to play the card
    private Consumer<Tile> effect; // The effect the card has on the target tile
    private TileState requiredTileState; // Tile type constraints, if any
    private boolean affectsAdjacent; // Whether the card affects adjacent tiles

    public Card(String name, List<Element> cost, Consumer<Tile> effect, TileState requiredTileState, boolean affectsAdjacent) {
        this.name = name;
        this.cost = cost;
        this.effect = effect;
        this.requiredTileState = requiredTileState;
        this.affectsAdjacent = affectsAdjacent;
    }

    public String getName() {
        return name;
    }

    public List<Element> getCost() {
        return cost;
    }

    public void applyEffect(Tile tile) {
        // Check if the tile meets the requirements
        if (requiredTileState == null || tile.getState() == requiredTileState) {
            effect.accept(tile); // Apply the effect to the target tile
            if (affectsAdjacent) {
                // Apply effect to neighboring tiles if necessary
                for (Tile neighbor : tile.getNeighbors()) {
                    effect.accept(neighbor);
                }
            }
        } else {
            System.out.println("Card cannot be played on this tile type.");
        }
    }
}
