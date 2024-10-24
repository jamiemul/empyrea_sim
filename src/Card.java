import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Card {

    private String name;
    private List<Element> cost; // Elements required to play the card
    private Consumer<List<Tile>> effect; // The effect the card has on the target tile
    private List<Predicate<Tile>> predicates;
    private int VP;

    public Card(String name, List<Element> cost, Consumer<List<Tile>> effect, List<Predicate<Tile>> predicates,
                int VP) {
        this.name = name;
        this.cost = cost;
        this.effect = effect;
        this.predicates = predicates;
        this.VP = VP;
    }

    public String getName() {
        return name;
    }

    public List<Element> getCost() {
        return cost;
    }

    public void applyEffect(List<Tile> tiles) {
        effect.accept(tiles);
    }

    public boolean checkPredicates(Tile tile) {
        for (Predicate<Tile> predicate : predicates) {
            if (!predicate.test(tile)) {
                return false;
            }
        }
        return true;
    }

    public void convertToProphet(Tile tile, Player player) {
        if (!tile.getFollowers().isEmpty() && tile.getFollowersOwned(player).size() > 0) {
            tile.getFollowersOwned(player).get(0).setType(UnitType.PROPHET);
        }
    }

    public boolean convertFollower(Tile tile, Player player, Boolean test) {
        if (!tile.getFollowers().isEmpty()
            && tile.getProphetsOwned(player).size() > 0
            && tile.getFollowersNotOwned(player).size() > 0) {
            if (!test) {
                Unit follower = tile.getFollowersNotOwned(player).get(0);
                follower.setOwner(player);
            }
            return true;
        }
        return false;
    }

    public boolean killFollowers(Tile tile, Player player, int amount, Boolean test) {
        List<Unit> enemyFollowers = tile.getFollowersNotOwned(player);
        if (!enemyFollowers.isEmpty() && enemyFollowers.size() >= amount) {
            if (!test) {
                for (int i = 0; i < amount; i++) {
                    Unit followerToKill = enemyFollowers.get(i);
                    tile.removeFollower(followerToKill);
                }
                return true;
            }
        }
        return false;
    }

    public void terraform(Tile tile, TileState state) {
        if (tile.getState() != state) {
            tile.setState(state);
        }
    }
}
