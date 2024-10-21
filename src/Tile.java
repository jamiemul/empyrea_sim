import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

enum TileState {
    OCEAN, GRASS, MOUNTAIN
}

enum Element {
    FIRE, AIR, EARTH, WATER
}

public class Tile {

    private TileState tileState;
    private List<Element> elements;
    private List<Unit> followers; // Add followers
    private List<Unit> prophets; // Add prophets
    private List<Token> tokens;
    private List<Tile> neighbors;

    public Tile(TileState state, List<Element> elements) {
        this.tileState = state;
        this.elements = elements;
        this.followers = new ArrayList<>();
        this.prophets = new ArrayList<>();
        this.tokens = new ArrayList<>();
        this.neighbors = new ArrayList<>();
    }

    public TileState getState() {
        return tileState;
    }

    public void setState(TileState state) {
        this.tileState = state;
    }

    public List<Unit> getFollowers() {
        return followers;
    }

    public List<Unit> getFollowersNotOwned(Player player) {
        List<Unit> followers =
            getUnitsOfType("Follower").stream().filter(follower -> follower.getOwner() != player).collect(
                Collectors.toList());

        return followers;
    }

    public List<Unit> getFollowersOwned(Player player) {
        List<Unit> followers = getUnitsOfType("Follower").stream().filter(follower -> follower.getOwner() == player)
            .collect(
                Collectors.toList());

        return followers;
    }

    public List<Unit> getProphetsNotOwned(Player player) {
        List<Unit> followers =
            getUnitsOfType("Prophet").stream().filter(follower -> follower.getOwner() != player).collect(
                Collectors.toList());

        return followers;
    }

    public List<Unit> getProphetsOwned(Player player) {
        List<Unit> followers = getUnitsOfType("Prophet").stream().filter(follower -> follower.getOwner() == player)
            .collect(
                Collectors.toList());

        return followers;
    }

    public List<Unit> getUnitsOfType(String type) {
        List<Unit> units = new ArrayList<>();

        for (Unit unit : this.followers) {
            if (unit.getType().equals(type)) {
                units.add(unit);
            }
        }

        return units;
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

    public void addFollower(Unit follower) {
        followers.add(follower);
    }

    public void removeFollower(final Unit followerToKill) {
        followers.remove(followerToKill);
    }

    public Optional<Player> getPlayerWithMostUnits() {
        Map<Player, Integer> playerUnitCount = new HashMap<>();

        // Count followers
        for (Unit follower : followers) {
            Player owner = follower.getOwner();
            playerUnitCount.put(owner, playerUnitCount.getOrDefault(owner, 0) + 1);
        }

        // Count prophets
        for (Unit prophet : prophets) {
            Player owner = prophet.getOwner();
            playerUnitCount.put(owner, playerUnitCount.getOrDefault(owner, 0) + 1);
        }

        // Find the player with the most units
        return playerUnitCount.entrySet()
            .stream()
            .max(Map.Entry.comparingByValue()) // Find the max value entry
            .map(Map.Entry::getKey); // Return the player with the most units, if any
    }
}
