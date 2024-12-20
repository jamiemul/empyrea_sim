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
    private Optional<Player> majorityOwner;

    public Tile(TileState state, List<Element> elements) {
        this.tileState = state;
        this.elements = elements;
        this.followers = new ArrayList<>();
        this.prophets = new ArrayList<>();
        this.tokens = new ArrayList<>();
        this.neighbors = new ArrayList<>();
    }

    public Optional<Player> getMajorityOwner() {
        return updateMajorityOwner();
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

    public Optional<Player> updateMajorityOwner() {
        List<Player> majority = getPlayerWithMostUnits();
        if (majority.size() == 1) {
            majorityOwner = Optional.ofNullable(majority.get(0));
            return majorityOwner;
        }
        return Optional.empty();
    }

    public List<Player> getPlayerWithMostUnits() {
        Map<Player, Integer> playerUnitCount = new HashMap<>();

        // Combine followers and prophets into a single loop
        List<Unit> allUnits = new ArrayList<>();
        allUnits.addAll(followers);
        allUnits.addAll(prophets);

        // Count units for each player
        for (Unit unit : allUnits) {
            Player owner = unit.getOwner();
            playerUnitCount.put(owner, playerUnitCount.getOrDefault(owner, 0) + 1);
        }

        // Early return if no units
        if (playerUnitCount.isEmpty()) {
            return new ArrayList<>(); // No players found
        }

        // Find the maximum unit count
        int maxCount = playerUnitCount.values().stream().max(Integer::compare).orElse(0);

        // Collect players with the maximum unit count
        return playerUnitCount.entrySet()
            .stream()
            .filter(entry -> entry.getValue() == maxCount)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }
}
