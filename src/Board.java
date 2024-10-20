import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Board {

    private List<List<Tile>> grid;

    public Board() {
        this.grid = new ArrayList<>();
        initializeBoard();
        assignNeighbors();
    }

    private void initializeBoard() {
        // Predefined board layout
        TileState[][] layout = {
            {TileState.OCEAN, TileState.GRASS, TileState.OCEAN},
            {TileState.OCEAN, TileState.GRASS, TileState.GRASS, TileState.OCEAN},
            {TileState.OCEAN, TileState.MOUNTAIN, TileState.MOUNTAIN, TileState.MOUNTAIN, TileState.OCEAN},
            {TileState.OCEAN, TileState.GRASS, TileState.GRASS, TileState.OCEAN},
            {TileState.OCEAN, TileState.GRASS, TileState.OCEAN}
        };

        Random random = new Random();

        // Iterate over the layout and create tiles
        for (int rowIndex = 0; rowIndex < layout.length; rowIndex++) {
            List<Tile> row = new ArrayList<>();
            for (int colIndex = 0; colIndex < layout[rowIndex].length; colIndex++) {
                TileState state = layout[rowIndex][colIndex];

                if (rowIndex == 2 && colIndex == 2) {
                    // Central tile (middle of the 3rd row)
                    List<Element> allElements = List.of(Element.FIRE, Element.AIR, Element.EARTH, Element.WATER);
                    row.add(new Tile(state, allElements));
                } else if (state == TileState.OCEAN) {
                    // Ocean tiles have no elements
                    row.add(new Tile(state, List.of()));
                } else {
                    // Grass and Mountain tiles get random elements
                    Element randomElement = Element.values()[random.nextInt(Element.values().length)];
                    row.add(new Tile(state, Collections.singletonList(randomElement)));
                }
            }
            grid.add(row);
        }
    }

    // Assign neighbors based on the hexagonal grid layout
    private void assignNeighbors() {
        for (int rowIndex = 0; rowIndex < grid.size(); rowIndex++) {
            List<Tile> row = grid.get(rowIndex);
            for (int colIndex = 0; colIndex < row.size(); colIndex++) {
                Tile tile = row.get(colIndex);

                // Define neighboring tiles in a hex grid
                // Get neighbors from above, below, left, and right (and diagonals depending on the row)
                if (rowIndex > 0) { // Tile above
                    List<Tile> rowAbove = grid.get(rowIndex - 1);
                    if (colIndex < rowAbove.size()) {
                        tile.addNeighbor(rowAbove.get(colIndex)); // Same column
                    }
                    if (colIndex > 0) {
                        tile.addNeighbor(rowAbove.get(colIndex - 1)); // Diagonal left
                    }
                }
                if (rowIndex < grid.size() - 1) { // Tile below
                    List<Tile> rowBelow = grid.get(rowIndex + 1);
                    if (colIndex < rowBelow.size()) {
                        tile.addNeighbor(rowBelow.get(colIndex)); // Same column
                    }
                    if (colIndex > 0 && colIndex - 1 < rowBelow.size()) {
                        tile.addNeighbor(rowBelow.get(colIndex - 1)); // Diagonal left
                    }
                }
                if (colIndex > 0) { // Tile to the left
                    tile.addNeighbor(row.get(colIndex - 1));
                }
                if (colIndex < row.size() - 1) { // Tile to the right
                    tile.addNeighbor(row.get(colIndex + 1));
                }
            }
        }
    }

    private int[] findTilePosition(Tile targetTile) {
        for (int rowIndex = 0; rowIndex < grid.size(); rowIndex++) {
            List<Tile> row = grid.get(rowIndex);
            for (int colIndex = 0; colIndex < row.size(); colIndex++) {
                if (grid.get(rowIndex).get(colIndex) == targetTile) {
                    return new int[]{rowIndex, colIndex};
                }
            }
        }
        return null; // Tile not found
    }

    public void printBoard() {
        for (int rowIndex = 0; rowIndex < grid.size(); rowIndex++) {
            List<Tile> row = grid.get(rowIndex);

            // Print leading spaces for hexagonal alignment
            System.out.print(" ".repeat((grid.size() - row.size()) * 2));

            for (Tile tile : row) {
                System.out.print(tile.getState() + "  " + tile.getElements() + "  "); // Print tile state
            }
            System.out.println();
        }

        // Print connections for each tile
        System.out.println("\nTile Connections:");
        for (int rowIndex = 0; rowIndex < grid.size(); rowIndex++) {
            List<Tile> row = grid.get(rowIndex);
            for (int colIndex = 0; colIndex < row.size(); colIndex++) {
                Tile tile = row.get(colIndex);
                System.out.print("Tile at (" + rowIndex + "," + colIndex + ") has neighbors: ");
                for (Tile neighbor : tile.getNeighbors()) {
                    int[] position = findTilePosition(neighbor);
                    if (position != null) {
                        System.out.print("(" + position[0] + "," + position[1] + ") ");
                    }
                }
                System.out.println();
            }
        }
    }
}
