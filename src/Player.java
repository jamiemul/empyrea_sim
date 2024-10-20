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

}
