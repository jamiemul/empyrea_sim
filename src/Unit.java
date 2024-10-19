public class Unit {

    private String type; // Either "Follower" or "Prophet"
    private Player owner; // Reference to the player who owns the unit

    public Unit(String type, Player owner) {
        this.type = type;
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public Player getOwner() {
        return owner;
    }
}
