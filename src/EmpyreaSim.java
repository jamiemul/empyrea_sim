import java.util.List;

public class EmpyreaSim {

    public static void main(String[] args) {
        Board board = new Board();
        board.printBoard();
        List<Player> players = List.of(new Player("Player 1", Element.FIRE, board),
                                       new Player("Player 2", Element.WATER, board),
                                       new Player("Player 3", Element.EARTH, board),
                                       new Player("Player 4", Element.AIR, board));

        System.out.print("Worship weight: " + players.get(0).calculateWorshipWeight());
        int EPOCH_MANA = 16;
    }
}
