import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum Advancement {
    LANDSCAPE, ODYSSEY, RITUALS, SETTLERS, ASCENSION, ALCHEMY, ORACLE, PILGRIMS, CONVERSION
}

public class Player {

    private String name;
    private List<Unit> units;
    private EnumSet<Advancement> advancements;  // EnumSet for advancements
    private byte fire = 0;
    private byte water = 0;
    private byte earth = 0;
    private byte air = 0;
    private Element innatePower;  // Innate power reference
    private Board board;

    public Player(String name, Element innatePower, Board board) {
        this.name = name;
        this.units = new ArrayList<>();
        this.advancements = EnumSet.noneOf(Advancement.class); // No advancements at start
        this.innatePower = innatePower;
        this.board = board;
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

    public void migrate(Player player) {
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
            if (tile.getMajorityOwner().contains(this) && tile.getMajorityOwner().size() > 1) {
                for (Tile adjacentTile : tile.getNeighbors()) {
                    List<Unit> followers = adjacentTile.getFollowersOwned(this);
                    List<Unit> prophets = adjacentTile.getProphetsOwned(this);
                    if (followers.size() + prophets.size() >= 2) {
                        Unit moveFollower = followers.get(0);
                        adjacentTile.removeFollower(moveFollower);
                        tile.addFollower(moveFollower);
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
                        Unit moveFollower = followers.get(0);
                        adjacentTile.removeFollower(moveFollower);
                        tile.addFollower(moveFollower);
                        movesLeft--;
                    }
                    if (movesLeft == 0) {
                        break;
                    }
                }
            }
        }
    }

    public void populate(Player player) {
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

    public void worship(Player player) {
        // Gain innate power and elements where you have majority
        if (hasAdvancement(Advancement.ASCENSION)) {
            ascension();
        }
        if (hasAdvancement(Advancement.PILGRIMS)) {
            pilgrims();
        }
        if (hasAdvancement(Advancement.CONVERSION)) {
            conversion();
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
}
