import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

enum Advancement {
    LANDSCAPE, ODYSSEY, RITUALS, SETTLERS, ASCENSION, ALCHEMY, ORACLE, PILGRIMS, CONVERSION
}

enum Action {
    NONE, MIGRATE, POPULATE, WORSHIP
}

public class Player {

    private String name;
    private List<Unit> units;
    private EnumSet<Advancement> advancements;  // EnumSet for advancements
    private Map<Element, Integer> elements;
    private Element innatePower;  // Innate power reference
    private Board board;
    private Action lastAction;
    private List<Card> cards;
    private int VP = 0;

    public Player(String name, Element innatePower, Board board) {
        this.elements = Map.of(Element.FIRE, 0, Element.AIR, 0, Element.EARTH, 0, Element.WATER, 0);
        this.name = name;
        this.units = new ArrayList<>();
        this.advancements = EnumSet.noneOf(Advancement.class); // No advancements at start
        this.innatePower = innatePower;
        this.board = board;
    }

    public void takeTurn() {
        // Play card or take advancement

        EnumSet availableActions = getAvailableActions();
        getActionWeighting(availableActions);
    }

    public void getActionWeighting(EnumSet<Action> availableActions) {
        float worshipWeight = availableActions.contains(Action.WORSHIP) ? calculateWorshipWeight() : 0.0f;
        float migrateWeight = availableActions.contains(Action.MIGRATE) ? calculateMigrateWeight() : 0.0f;
        float populateWeight = availableActions.contains(Action.POPULATE) ? calculatePopulateWeight() : 0.0f;

        List<Map.Entry<Float, Runnable>> actions = List.of(
            Map.entry(worshipWeight, this::worship),
            Map.entry(migrateWeight, this::migrate),
            Map.entry(populateWeight, this::populate)
        );

        actions.stream()
            .max(Map.Entry.comparingByKey())
            .ifPresent(entry -> entry.getValue().run());
    }

    public float calculateWorshipWeight() {
        int majorityTiles = getMajorityTiles();
        int totalElements = getTotalElements();

        // Normalize totalElements (0 elements -> 1, 8 elements -> 0)
        float normalizedTotalElements = 1.0f - (totalElements / 8.0f);

        // Normalize majorityTiles (0 tiles -> 0, 3 tiles -> 1)
        float normalizedMajorityTiles = Math.min(majorityTiles / 2.0f, 1.0f);
        float weightedMajorityTiles = (float) Math.pow(normalizedMajorityTiles, 2);

        return (normalizedTotalElements * 0.6f) + (weightedMajorityTiles * 0.4f);
    }

    public int getMajorityTiles() {
        int majorityTiles = 0;
        for (Tile tile : board.getAllTiles()) {
            if (!tile.getMajorityOwner().isEmpty() && tile.getMajorityOwner().get() == this) {
                majorityTiles += 1;
            }
        }

        return majorityTiles;
    }

    public int getTotalElements() {
        return elements.values().stream().mapToInt(Integer::intValue).sum();
    }

    public float calculateMigrateWeight() {
        return 0.0f;
    }

    public float calculatePopulateWeight() {
        return 0.0f;
    }

    public EnumSet getAvailableActions() {
        if (lastAction == Action.NONE) {
            return EnumSet.of(Action.MIGRATE, Action.POPULATE, Action.WORSHIP);
        } else if (lastAction == Action.MIGRATE) {
            return EnumSet.of(Action.POPULATE, Action.WORSHIP);
        } else if (lastAction == Action.POPULATE) {
            return EnumSet.of(Action.MIGRATE, Action.WORSHIP);
        } else {
            return EnumSet.of(Action.MIGRATE, Action.POPULATE);
        }
    }

    public String getName() {
        return name;
    }

    // Add an advancement
    public void addAdvancement(Advancement advancement) {
        advancements.add(advancement);
    }

    // Check if the player has an advancement
    public boolean hasAdvancement(Advancement advancement) {
        return advancements.contains(advancement);
    }

    public void migrate() {
        int moves = 3;
        findMajorityMove(moves);
        if (moves > 0) {
            findSpreadMove(moves);
        }
        findSpreadMove(moves);
        // Perform migration, move unit, gain card
        if (hasAdvancement(Advancement.SETTLERS)) {
            settlers();
        }
        if (hasAdvancement(Advancement.ALCHEMY)) {
            alchemy();
        }
        if (hasAdvancement(Advancement.ODYSSEY)) {
            odyssey();
        }
    }

    public void findMajorityMove(int moves) {
        int movesLeft = moves;
        for (Tile tile : board.getAllTiles()) {
            if (!tile.getMajorityOwner().isEmpty() && tile.getMajorityOwner().get() == this) {
                for (Tile adjacentTile : tile.getNeighbors()) {
                    List<Unit> followers = adjacentTile.getFollowersOwned(this);
                    List<Unit> prophets = adjacentTile.getProphetsOwned(this);
                    if (followers.size() + prophets.size() >= 2) {
                        if (prophets.size() > 1) {
                            moveUnit(tile, prophets, adjacentTile);
                        } else {
                            moveUnit(tile, followers, adjacentTile);
                        }
                        movesLeft--;
                    }
                    if (movesLeft == 0) {
                        break;
                    }
                }
            }
        }
    }

    public void findSpreadMove(int moves) {
        int movesLeft = moves;

        for (Tile tile : board.getAllTiles()) {
            List<Unit> followers = tile.getFollowersOwned(this);
            List<Unit> prophets = tile.getProphetsOwned(this);
            if (followers.size() + prophets.size() >= 2) {
                for (Tile adjacentTile : tile.getNeighbors()) {
                    if (adjacentTile.getFollowersOwned(this).size() == 0
                        && adjacentTile.getProphetsOwned(this).size() == 0) {
                        if (prophets.size() > 1) {
                            moveUnit(tile, prophets, adjacentTile);
                        } else {
                            moveUnit(tile, followers, adjacentTile);
                        }
                        movesLeft--;
                    }
                    if (movesLeft == 0) {
                        break;
                    }
                }
            }
        }
    }

    private void moveUnit(final Tile tile, final List<Unit> unit, final Tile adjacentTile) {
        Unit unitToMove = unit.get(0);
        adjacentTile.removeFollower(unitToMove);
        tile.addFollower(unitToMove);
    }

    public void populate() {
        populateTiles();
        // Add units to each tile with units
        if (hasAdvancement(Advancement.RITUALS)) {
            rituals();
        }
        if (hasAdvancement(Advancement.LANDSCAPE)) {
            landscape();
        }
        if (hasAdvancement(Advancement.ORACLE)) {
            oracle();
        }
    }

    public void populateTiles() {
        List<Tile> tiles = board.getAllTiles();
        for (Tile tile : tiles) {
            if (tile.getFollowersOwned(this).size() > 0) {
                tile.addFollower(new Unit(UnitType.FOLLOWER, this));
            }
        }
    }

    public void worship() {
        if (hasAdvancement(Advancement.ASCENSION)) {
            ascension();
        }
        if (hasAdvancement(Advancement.PILGRIMS)) {
            pilgrims();
        }
        if (hasAdvancement(Advancement.CONVERSION)) {
            conversion();
        }

        // Gain innate power
        this.elements.put(innatePower, Math.min(this.elements.get(innatePower) + 1, 2));

        List<Tile> tiles = board.getAllTiles();
        for (Tile tile : tiles) {
            if (tile.getState() != TileState.OCEAN
                && !tile.getMajorityOwner().isEmpty() && tile.getMajorityOwner().get() == this) {
                List<Element> tileElements = tile.getElements();

                if (tileElements.size() == 1) {
                    this.elements.put(tileElements.get(0), Math.min(this.elements.get(tileElements.get(0)) + 1, 2));
                } else {
                    gainMostNeededElement();
                }

            }
        }
    }

    public void gainMostNeededElement() {
        if (!getElementOfValue(0)) {
            getElementOfValue(1);
        }
    }

    public boolean getElementOfValue(int value) {
        for (Map.Entry<Element, Integer> entry : elements.entrySet()) {
            if (entry.getValue() == value && entry.getKey() != innatePower) {
                elements.put(entry.getKey(), 1);
                return true;
            }
        }
        return false;
    }

    // Sample advancement functions
    public void landscape() {
        System.out.println(name + " used LANDSCAPE advancement");
    }

    public void odyssey() {
        System.out.println(name + " used ODYSSEY advancement");
    }

    public void alchemy() {
        System.out.println(name + " used ALCHEMY advancement");
    }

    public void rituals() {
        System.out.println(name + " used RITUALS advancement");
    }

    public void oracle() {
        System.out.println(name + " used ORACLE advancement");
    }

    public void settlers() {
        System.out.println(name + " used SETTLERS advancement");
    }

    public void ascension() {
        System.out.println(name + " used ASCENSION advancement");
    }

    public void conversion() {
        System.out.println(name + " used CONVERSION advancement");
    }

    public void pilgrims() {
        System.out.println(name + " used PILGRIMS advancement");
    }

    public int getVP() {
        return VP;
    }

    public void increaseVP(int vp) {
        VP += vp;
    }
}
