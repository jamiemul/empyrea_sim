import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private List<Unit> units;

    public Player(String name) {
        this.name = name;
        this.units = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void addUnit(Unit unit) {
        units.add(unit);
    }

    public void removeUnit(Unit unit) {
        units.remove(unit);
    }
}
