enum UnitType {
    FOLLOWER, PROPHET
}

public class Unit {

    private UnitType type; // Either "Follower" or "Prophet"
    private Player owner; // Reference to the player who owns the unit

    public Unit(UnitType type, Player owner) {
        this.type = type;
        this.owner = owner;
    }

    public UnitType getType() {
        return type;
    }

    public void setType(UnitType type) {
        this.type = type;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

}
