import java.util.List;

public class CardEffects {

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
