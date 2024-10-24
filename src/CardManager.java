import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CardManager {

    Board board;

    Predicate<Tile> isGrass = tile -> tile.getState() == TileState.GRASS;
    Predicate<Tile> isOcean = tile -> tile.getState() == TileState.OCEAN;
    Predicate<Tile> isMountain = tile -> tile.getState() == TileState.MOUNTAIN;
    Predicate<Tile> isNotOcean = tile -> tile.getState() != TileState.OCEAN;
    Predicate<Tile> isNotGrass = tile -> tile.getState() != TileState.GRASS;
    Predicate<Tile> isNotMountain = tile -> tile.getState() != TileState.MOUNTAIN;

    public Predicate<Tile> hasEnemyFollowers(int amount, Player player) {
        return tile -> tile.getFollowersNotOwned(player).size() > amount;
    }

    public Predicate<Tile> hasOwnedFollowers(int amount, Player player) {
        return tile -> tile.getFollowersOwned(player).size() > amount;
    }

    public Predicate<Tile> hasOwnedProphets(int amount, Player player) {
        return tile -> tile.getProphetsOwned(player).size() > amount;
    }

    public Predicate<Tile> hasElement(Element element) {
        return tile -> tile.getElements().contains(element);
    }

    public Predicate<Tile> hasTileType(TileState tileState) {
        return tile -> tile.getNeighbors().stream().anyMatch(neighbor -> neighbor.getState() == tileState);
    }

    public CardManager(Board board) {
        this.board = board;
    }

    public Card Flood(Player player) {
        Consumer<List<Tile>> effect = tiles -> tiles.forEach(tile -> tile.setState(TileState.OCEAN));
        return new Card(
            "Flood",
            List.of(Element.WATER),
            effect,
            List.of(isGrass), // Add multiple predicates if needed
            1
        );
    }

    public Card RainOfFire(Player player) {
        Consumer<List<Tile>> effect = tiles -> tiles.forEach(tile -> tile.setState(TileState.MOUNTAIN));
        return new Card(
            "Rain of Fire",
            List.of(Element.FIRE),
            effect,
            List.of(isGrass),
            1
        );
    }

}
