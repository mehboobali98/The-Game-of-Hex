package AiHex.players;

import AiHex.hexBoards.BoardData;

//class to get connected components of a hex player
public class EvaluationFunction {

    static int ROW;
    static int COL;
    public static int counter = 0;

    //constructor
    EvaluationFunction(int row, int col) {
        ROW = row;
        COL = col;
    }

    //check for valid indices and the board location is empty
    boolean isSafe(BoardData game, int row, int col,
                   boolean visited[][], int value) {
        return (row >= 0) && (row < ROW) &&
                (col >= 0) && (col < COL) &&
                (game.get(row, col).getValue() == value && !visited[row][col]);
    }

    // It only considers the 6 neighbors as adjacent vertices as its a hex
    int DFS(BoardData game, int row, int col, boolean visited[][], int value) {

        int rowNbr[] = new int[]{-1, -1, 0, 0, 1, 1};
        int colNbr[] = new int[]{0, 1, -1, 1, -1, 0};

        int length = 0;
        // Mark this cell as visited
        visited[row][col] = true;
        counter++;
        length++;

        // Recur for all connected neighbours
        for (int k = 0; k < 6; ++k)
            if (isSafe(game, row + rowNbr[k], col + colNbr[k], visited, value))
                length += DFS(game, row + rowNbr[k], col + colNbr[k], visited, value);

        return length;
    }

    //wrapper function to count connected hexes
    int countConnectedNodes(BoardData game, int value) {

        // Initially all cells are unvisited
        boolean visited[][] = new boolean[ROW][COL];

        // Initialize count as 0 and traverse through the all cells
        // of given matrix
        int count = 0;
        int length = 0;
        for (int i = 0; i < ROW; ++i)
            for (int j = 0; j < COL; ++j)
                if (game.get(i, j).getValue() == value && !visited[i][j]) {
                    length = DFS(game, i, j, visited, value);
                    if (length > 1) { //only count connected pieces with length greater than 1
                        ++count;
                    } else if (length <= 1) {
                        counter--;
                    }
                }
        return count;
    }
}