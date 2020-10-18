package AiHex.players;

import AiHex.gameMechanics.Runner;

import java.awt.Point;
import java.util.*;
import AiHex.hexBoards.Board;
import AiHex.gameMechanics.Move;
import AiHex.hexBoards.BoardData;

public class AIPlayer implements Player {
    private Runner game = null;
    private static int colour;
    public static final int MAX = 100000;
    public static final int MIN = -100000;
    private int boardSize;
    private static int AIMarker;
    private static int HumanMarker;
    private int moveCount = 0;

    //constructor
    public AIPlayer(Runner game, int colour) {
        this.game = game;
        this.colour = colour;
        this.boardSize = game.getBoard().getSize();
    }

    //returns move for the AI player
    public Move getMove() {

        System.out.println("Thinking about my next move........");
        selectOpponentMarker();
        Random rand = new Random();

        if (moveCount == 0) {
            int x = (int) Math.ceil(game.getBoard().getSize() / 2);
            int y = (int) Math.ceil(game.getBoard().getSize() / 2);
            if (game.getBoard().data.get(x, y).getValue() != Board.BLANK) {
                x = rand.nextInt(boardSize);
            }
            Move m = new Move(colour, x, y);
            moveCount++;
            return m;
        }

        BoardData dummyBoard = game.getBoard().data.clone();
        //Pair p = selectOptimalMove(dummyBoard, AIMarker, 5, MIN, MAX);
        Pair p = MiniMax(dummyBoard,AIMarker,3);

        Move move = new Move(colour, p.m.getX(), p.m.getY());
        return move; //returning the move selected by the user
    }

    //Alpha-Beta Pruning
    Pair selectOptimalMove(BoardData game, int marker, int depth, int alpha, int beta) {

        Move bestMove = new Move(colour, -1, -1);
        int bestScore = (marker == AIMarker) ? MIN : MAX; // colour == 1

        //base-case
        if (depth == 0 || getLegalMoves(game, marker).size() == 0) {
            bestScore = getBoardState(game, marker);
            Pair p = new Pair(bestMove, bestScore);
            return p;
        }

        ArrayList<Move> legalMoves;
        legalMoves = getLegalMoves(game, marker);
        Random r = new Random();

        for (int i = 0; i < legalMoves.size(); i++) {
            int index = r.nextInt(legalMoves.size());
            Move currentMove = legalMoves.get(index);
            game.set(currentMove.getX(), currentMove.getY(), marker);

            if (marker == AIMarker) // max player
            {
                Pair p = selectOptimalMove(game, HumanMarker, depth - 1, alpha, beta);
                int score = p.getValue();
                if (bestScore < score) {
                    bestScore = score - depth;
                    bestMove = currentMove;
                    alpha = Math.max(alpha, bestScore); //update the value of alpha
                    game.set(currentMove.getX(), currentMove.getY(), Board.BLANK);

                    if (beta <= alpha)//prune
                    {
                        break;
                    }
                }
            } else {
                Pair p = selectOptimalMove(game, AIMarker, depth - 1, alpha, beta);
                int score = p.getValue();
                if (bestScore > score) {
                    bestScore = score + depth;
                    bestMove = currentMove;
                    beta = Math.min(beta, bestScore);
                    game.set(currentMove.getX(), currentMove.getY(), Board.BLANK);

                    if (beta <= alpha)//prune
                    {
                        break;
                    }
                }
            }
            //Undo Move
            game.set(currentMove.getX(), currentMove.getY(), Board.BLANK);
        }

        Pair p = new Pair(bestMove, bestScore);
        return p;
    }

    //Mini-max implementation
    Pair MiniMax(BoardData game, int marker, int depth) {

        Move bestMove = new Move(colour, -1, -1);
        int bestScore = (marker == AIMarker) ? MIN : MAX; // colour == 1

        //base-case
        if (depth == 0 || getLegalMoves(game, marker).size() == 0) {
            bestScore = getBoardState(game, AIMarker);
            Pair p = new Pair(bestMove, bestScore);
            return p;
        }

        ArrayList<Move> legalMoves;
        legalMoves = getLegalMoves(game, marker);
        Random r = new Random();

        for (int i = 0; i < legalMoves.size(); i++) {
            int index = r.nextInt(legalMoves.size()); //randomly selecting a move
            Move currentMove = legalMoves.get(index);
            game.set(currentMove.getX(), currentMove.getY(), marker); // applying the move on board

            if (marker == AIMarker) // max player
            {
                Pair p = MiniMax(game, HumanMarker, depth - 1);
                int score = p.getValue();

                if (bestScore < score) // for max player bestScore == -10000
                {
                    bestScore = score - depth;
                    bestMove = currentMove;
                    game.set(currentMove.getX(), currentMove.getY(), Board.BLANK); //undoing the move
                }
            } else { //Human player turn

                Pair p = MiniMax(game, AIMarker, depth - 1);
                int score = p.getValue();

                if (bestScore > score) {
                    bestScore = score + depth;
                    bestMove = currentMove;
                    game.set(currentMove.getX(), currentMove.getY(), Board.BLANK);// undoing the move
                }
            }
            //Undo Move
            game.set(currentMove.getX(), currentMove.getY(), Board.BLANK);
        }

        Pair p = new Pair(bestMove, bestScore);
        return p;
    }

    //assigns board a value based on the heuristics
    int getBoardState(BoardData game, int marker) {
        int opponentMarker = getOpponentMarker(marker);

        boolean isWon = checkWinForRedPlayer(game);
        if (isWon) {
            return MAX;
        }

        boolean isLost = checkWinForBluePlayer(game);
        if (isLost) {
            return MIN;
        }

        int bridgeF = bridgeFactor(game);
        int centerF = focusCenter(game);

        bridgeF = bridgeF * 2;
        centerF = centerF * 2;
        int value = bridgeF + centerF;

        EvaluationFunction ef = new EvaluationFunction(game.getSize(), game.getSize());
        //count connected hexes of AI player
        ef.countConnectedNodes(game, marker);
        int AIValue = EvaluationFunction.counter;
        EvaluationFunction.counter = 0;

        //count connected hexes of Human player
        ef.countConnectedNodes(game, opponentMarker);
        int humanValue = EvaluationFunction.counter;
        //Subtract both values to get board state
        int boardState = AIValue - humanValue;

        return boardState + value;  // there is no draw in this game
    }

    //bridge heuristic
    int bridgeFactor(BoardData game) {
        int score = 0, currentPlayer, val , x , y;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                currentPlayer = game.get(i, j).getValue();
                if (currentPlayer != Board.BLANK) {
                    ArrayList<Point> bridges = getBridges(i, j);
                    for (Point p : bridges) {
                        x = p.x;
                        y = p.y;
                        val = game.get(x, y).getValue();

                        if (val == AIMarker && currentPlayer == AIMarker) {
                            score += 3; // if bridge exists for maximising player
                        } else if (val == HumanMarker && currentPlayer == HumanMarker) {
                            score += -5; // bridge exists for minimising player
                        }
                    }
                }
            }
        }
        return score;
    }

    //makes the play in the middle of the board
    int focusCenter(BoardData game) {
        int score = 0;
        int c_row = boardSize / 2;
        int c_col = boardSize / 2;
        int c_value = game.get(c_row, c_col).getValue();

        if (c_value != Board.BLANK) {
            if (c_value == AIMarker) {
                score += 50; // maximizing player
            } else if (c_value == HumanMarker) {
                score += -50; // minimizing player
            }
            ArrayList<Point> n = getNeighbors(c_row, c_col);
            int count = 0;
            for (Point p : n) {
                int value = game.get(p.x, p.y).getValue();
                if (value == AIMarker) {
                    score += 3;
                    count += 1;
                } else if (value == HumanMarker) {
                    score += -3;
                    count += -1;
                }
            }

            if (count >= 4) {
                score -= 20;
            } else if (count <= -4) {
                score += 20;
            }
        }
        return score;
    }

    boolean checkWinForBluePlayer(BoardData game) {
        boolean goalFound = false;
        boolean visited[][] = new boolean[boardSize][boardSize];
        for (int i = 0; i < 1; ++i) {
            for (int j = 0; j < boardSize; ++j) {
                if (game.get(i, j).getValue() == 2 && !visited[i][j]) {
                    DFS(game, i, j, visited, 2);
                }
            }
        }

        for (int i = 0; i < boardSize; i++) {
            if (visited[boardSize - 1][i] == true) {
                goalFound = true;
                break;
            }
        }
        return goalFound;
    }

    boolean checkWinForRedPlayer(BoardData game) {
        boolean goalFound = false;
        boolean visited[][] = new boolean[boardSize][boardSize];
        for (int i = 0; i < boardSize; ++i) {
            for (int j = 0; j < 1; ++j) {
                if (game.get(i, j).getValue() == 1 && !visited[i][j]) {
                    DFS(game, i, j, visited, 1);
                }
            }
        }

        for (int i = 0; i < boardSize; i++) {
            if (visited[i][boardSize - 1] == true) {
                goalFound = true;
                break;
            }
        }
        return goalFound;
    }

    //DFS to check if a player has won
    void DFS(BoardData game, int row, int col, boolean visited[][], int value) {
        // These arrays are used to get row and columns
        int rowNbr[] = new int[]{-1, -1, 0, 0, 1, 1};
        int colNbr[] = new int[]{0, 1, -1, 1, -1, 0};
        // Mark this cell as visited
        visited[row][col] = true;

        // Recur for all connected neighbours
        for (int k = 0; k < 6; ++k)
            if (isSafe(game, row + rowNbr[k], col + colNbr[k], visited, value))
                DFS(game, row + rowNbr[k], col + colNbr[k], visited, value);
    }

    //checking if a neighbour is valid and it has not been visited before
    boolean isSafe(BoardData game, int row, int col, boolean visited[][], int value) {
        return (row >= 0) && (row < boardSize) &&
                (col >= 0) && (col < boardSize) &&
                (game.get(row, col).getValue() == value && !visited[row][col]);
    }

    //function to get empty spaces
    ArrayList<Move> getLegalMoves(BoardData game, int marker) {
        ArrayList<Move> legalMoves = new ArrayList<>();
        int rowStart, colStart, rowEnd, colEnd;

        if (marker == AIMarker) {
            rowStart = (int) Math.ceil(game.getSize() / 4);
            rowEnd = (int) Math.ceil(game.getSize() - game.getSize() / 4);
            colStart = 0;
            colEnd = game.getSize();

        } else {

            colStart = (int) Math.ceil(game.getSize() / 4);
            colEnd = (int) Math.ceil(game.getSize() - game.getSize() / 4);
            rowStart = 0;
            rowEnd = game.getSize();
        }

        for (int i = rowStart; i < rowEnd; i++) {
            for (int j = colStart; j < colEnd; j++) {
                if (game.get(i, j).getValue() == Board.BLANK) {
                    Move m = new Move(marker, i, j);
                    legalMoves.add(m);
                }
            }
        }

        Collections.shuffle(legalMoves); // shuffling the positions

        return legalMoves;
    }

    //getting neighbours for a point
    ArrayList<Point> getNeighbors(int x, int y) {
        ArrayList<Point> temp = new ArrayList<Point>();

        temp.add(new Point(x - 1, y)); // top

        temp.add(new Point(x + 1, y - 1)); //bottom left

        temp.add(new Point(x, y - 1)); // left

        temp.add(new Point(x + 1, y)); // bottom

        temp.add(new Point(x - 1, y + 1)); // top right

        temp.add(new Point(x, y + 1)); // //right

        ArrayList<Point> neighbours = new ArrayList<>();

        for (Point n : temp) {
            if (checkValidPosition(n.x, n.y)) { // if valid position add it to the list
                neighbours.add(n);
            }
        }
        return neighbours;
    }

    //get possible bridge positions for a hex
    ArrayList<Point> getBridges(int x, int y) {
        ArrayList<Point> temp = new ArrayList<Point>();

        temp.add(new Point(x + 2, y - 1));

        temp.add(new Point(x + 1, y - 2));

        temp.add(new Point(x + 1, y + 1));

        temp.add(new Point(x - 1, y - 1));

        temp.add(new Point(x - 1, y + 2));

        temp.add(new Point(x - 2, y + 1)); // //right

        ArrayList<Point> bridgePoints = new ArrayList<>();

        for (Point n : temp) {
            if (checkValidPosition(n.x, n.y)) { // if valid position add it to the list
                bridgePoints.add(n);
            }
        }
        return bridgePoints;
    }

    //function to check valid indices
    boolean checkValidPosition(int i, int j) {
        if (i > -1 && j > -1 && i < boardSize && j < boardSize) {
            return true;
        }
        return false;
    }

    //select opponent marker(identifier) based on first move
    void selectOpponentMarker() {
        if (colour == 1) {
            AIMarker = 1;
            HumanMarker = 2;
        }
        if (colour == 2) {
            AIMarker = 2;
            HumanMarker = 1;
        }

    }

    //switch turns between players
    int getOpponentMarker(int marker) {
        int opponentMarker;
        if (marker == HumanMarker) {
            opponentMarker = AIMarker;
        } else {
            opponentMarker = HumanMarker;
        }
        return opponentMarker;
    }

    public ArrayList<Board> getAuxBoards() {
        return new ArrayList<Board>();
    }
}

