package AiHex.hexBoards;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class BoardData {

    public static final int RED_BORDER1_NODE = 0; // top border
    public static final int BLUE_BORDER1_NODE = 1; // right border
    public static final int RED_BORDER2_NODE = 2; // bottom border
    public static final int BLUE_BORDER2_NODE = 3; // left border
    private HexLocation[][] board; // 2d array of hex objects
    private AdjMatrix redAdjMatrix;
    private AdjMatrix blueAdjMatrix;
    private int size;

    public BoardData(int size) {
        this.size = size;
        this.board = new HexLocation[size][size];
        this.redAdjMatrix = new AdjMatrix((int) Math.pow(size, 2) + 4); // size ^ 2 + 4, if size = 7, then 53x53 matrix
        this.blueAdjMatrix = new AdjMatrix((int) Math.pow(size, 2) + 4);
        initBoard();
        initAdjMatrix();
    }

    public int getSize()
    {
        return size;
    }

    private void initAdjMatrix() {
        /*
         * first, we build identical neighbour relationships for the main bodies
         * of both red and blue
         */
        for (int row = 0; row < size; row++)
            for (int column = 0; column < size; column++) {
                int node = board[column][row].getNodeID(); // node == 4 to 52

                ArrayList<Integer> neighbours = getBoardNeighbours(column, row);

                System.out.println("Neighbors of node with value: " + node);
                for(int n : neighbours)
                {
                    System.out.print(n + " ");
                }
                System.out.println();

                for (int neighbour : neighbours) {
                    blueAdjMatrix.write(node, neighbour, AdjMatrix.LINK); // LINK == 1
                    redAdjMatrix.write(node, neighbour, AdjMatrix.LINK);
                }
            }

        /*
         * next, we build the neighbour relationships for the individual borders
         */

        //Blue borders
        for (int row = 0; row < size; row++) {
            int leftSideNeighbour = board[0][row].getNodeID();
            int rightSideNeighbour = board[size - 1][row].getNodeID();
            blueAdjMatrix.write(BLUE_BORDER1_NODE, leftSideNeighbour,
                    AdjMatrix.LINK);
            blueAdjMatrix.write(BLUE_BORDER2_NODE, rightSideNeighbour,
                    AdjMatrix.LINK);
        }

        //Red borders
        for (int column = 0; column < size; column++) {
            int northSideNeighbour = board[column][0].getNodeID();
            int southSideNeighbour = board[column][size - 1].getNodeID();
            redAdjMatrix.write(RED_BORDER1_NODE, northSideNeighbour,
                    AdjMatrix.LINK);
            redAdjMatrix.write(RED_BORDER2_NODE, southSideNeighbour,
                    AdjMatrix.LINK);
        }

        /*
         * finally, make all nodes reach themselves ?
         */
        for (int i = 0; i < redAdjMatrix.size(); i++) {
            redAdjMatrix.write(i, i, AdjMatrix.LINK);
            blueAdjMatrix.write(i, i, AdjMatrix.LINK);
        }

    }

    //it was private before
    public ArrayList<Integer> getBoardNeighbours(int x, int y) {

        ArrayList<Point> temp = new ArrayList<Point>();

        temp.add(new Point(x - 1, y)); // top

        temp.add(new Point(x + 1, y - 1)); //bottom left

        temp.add(new Point(x, y - 1)); // left

        temp.add(new Point(x + 1, y)); // bottom

        temp.add(new Point(x - 1, y + 1)); // top right

        temp.add(new Point(x, y + 1)); // //right

        ArrayList<Integer> neighbours = new ArrayList<Integer>();

        for (Point n : temp)
            if (n.x > -1 && n.y > -1 && n.x < size && n.y < size) {

                int nodeIndex = board[n.x][n.y].getNodeID(); // node id starts from 4 and goes up to 52 if size = 7
                neighbours.add(nodeIndex);
            }
        return neighbours;
    }

    public void initBoard() {
        Random random = new Random();
        int nodeIndex = 4; // 0-3 are taken by borders
        for (int row = 0; row < size; row++)
            for (int column = 0; column < size; column++) {
                board[column][row] = new HexLocation(nodeIndex);
                nodeIndex++;
            }
    }

    public AdjMatrix getAdjMatrix(int colour) {
        AdjMatrix returnVal;
        switch (colour) {
            case Board.RED:
                returnVal = redAdjMatrix.clone();
                break;
            case Board.BLUE:
                returnVal = blueAdjMatrix.clone();
                break;
            default:
                System.err.println("incorrect colour");
                returnVal = null;
                break;
        }
        return returnVal;
    }

    public AdjMatrix getAdjMatrix(int colour, int season) {
        AdjMatrix returnVal;
        switch (colour) {
            case Board.RED:
                returnVal = redAdjMatrix.clone();
                break;
            case Board.BLUE:
                returnVal = blueAdjMatrix.clone();
                break;
            default:
                System.err.println("incorrect colour");
                returnVal = null;
                break;
        }
        for (int row = 0; row < size; row++)
            for (int column = 0; column < size; column++)
                if (board[column][row].getValue() == Board.BLANK && board[column][row].getSeason() != season) {
                    int node = board[column][row].getNodeID();
                    returnVal.wipeNode(node);
                }
        return returnVal;
    }

    public BoardData(int size, HexLocation[][] board,
                     AdjMatrix redAdjMatrix, AdjMatrix blueAdjMatrix) {
        this.size = size;
        this.board = board;
        this.redAdjMatrix = redAdjMatrix;
        this.blueAdjMatrix = blueAdjMatrix;
    }

    public boolean checkWin(int colour) {
        boolean returnVal = false;
        switch (colour) {
            case Board.RED:
                if (redAdjMatrix.read(RED_BORDER1_NODE, RED_BORDER2_NODE) == AdjMatrix.LINK)
                    returnVal = true;
                break;
            case Board.BLUE:
                if (blueAdjMatrix.read(BLUE_BORDER1_NODE, BLUE_BORDER2_NODE) == AdjMatrix.LINK)
                    returnVal = true;
                break;
            default:
                System.err.println("incorrect colour");
                break;
        }
        return returnVal;
    }

    @Override
    public BoardData clone() {
        HexLocation[][] boardClone = new HexLocation[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                boardClone[i][j] = board[i][j].clone();
        return new BoardData(size, boardClone, redAdjMatrix.clone(), blueAdjMatrix.clone());
    }

    public HexLocation get(int x, int y) {
        return board[x][y];
    }

    private ArrayList<Integer> getAdjMatrixNeighbours(int node, int colour) {
        ArrayList<Integer> neighbours = new ArrayList<Integer>();
        AdjMatrix adjMatrix = null;
        switch (colour) {
            case Board.RED:
                adjMatrix = redAdjMatrix;
                break;
            case Board.BLUE:
                adjMatrix = blueAdjMatrix;
                break;
        }

        for (int i = 0; i < adjMatrix.size(); i++)
            if (i != node && adjMatrix.read(node, i) == AdjMatrix.LINK)
                neighbours.add(i);
        return neighbours;
    }

    public ArrayList<Integer> getPath(int start, int target, int colour) {
        ArrayList<Integer> tree = new ArrayList<Integer>();
        ArrayList<Integer> parentIndex = new ArrayList<Integer>();
        boolean finished = false;
        tree.add(start);
        parentIndex.add(-1);

        /*
         * iterate through the tree (whilst adding to it)
         */
        for (int index = 0; index < tree.size() && !finished; index++) {
            int node = tree.get(index);
            if (node == target)
                finished = true;
            else {
                ArrayList<Integer> newChildren = getAdjMatrixNeighbours(
                        node, colour);
                Collections.shuffle(newChildren);
                for (int child : newChildren)
                    if (!tree.contains(child)) {
                        tree.add(child);
                        parentIndex.add(index);
                    }
            }
        }
        ArrayList<Integer> path = new ArrayList<Integer>();
        /*
         * find the path;
         */
        if (finished) {
            int treeIndex = tree.indexOf(target);
            path.add(tree.get(treeIndex));
            int parent = parentIndex.get(treeIndex);

            while (parent != -1) {
                path.add(tree.get(parent));
                parent = parentIndex.get(parent);
            }
        }
        return path;
    }

    public ArrayList<Point> getWinningPath(int colour) {
        ArrayList<Integer> nodePath;
        ArrayList<Point> winningPath = new ArrayList<Point>();
        int borderA = 0;
        int borderB = 0;
        switch (colour) {
            case Board.RED:
                borderA = RED_BORDER1_NODE;
                borderB = RED_BORDER2_NODE;
                break;
            case Board.BLUE:
                borderA = BLUE_BORDER1_NODE;
                borderB = BLUE_BORDER2_NODE;
                break;
        }
        nodePath = getPath(borderA, borderB, colour);
        for (int node : nodePath) {
            Point xy = getXYpos(node);
            if (xy != null)
                winningPath.add(xy);

        }
        return winningPath;
    }

    public Point getXYpos(int node) {
        if (node < 4)
            return null;
        else {
            node = node - 4;
            int y = node / size;
            int x = node % size;
            return new Point(x, y);
        }
    }

    //setting the board based on the current player
    public void set(int x, int y, int value) {

        //redAdjMatrix.print();
        int node = board[x][y].getNodeID(); // 4
       // System.out.println("node value: " + node);
        board[x][y].setValue(value);

        switch (value) {
            case Board.RED:
                redAdjMatrix.bypassAndRemoveNode(node);
                blueAdjMatrix.wipeNode(node);
                break;
            case Board.BLUE:
                redAdjMatrix.wipeNode(node);
                blueAdjMatrix.bypassAndRemoveNode(node);
                break;
            default:
                //redAdjMatrix.wipeNode(node);
                //System.err.println("incorrect colour 123");
                break;
        }

      /*  System.out.println("Red: ");
        redAdjMatrix.print();
        System.out.println("Blue: ");
        blueAdjMatrix.print();

       */
    }

    /*
     * sets a nodes value, but does not bypass it. this is useful if we want to
     * keep the neighbour structure intact.
     */
    public void set_noNewLinks(int x, int y, int value) {
        int node = board[x][y].getNodeID();
        board[x][y].setValue(value);
        switch (value) {
            case Board.RED:
                blueAdjMatrix.wipeNode(node);
                break;
            case Board.BLUE:
                redAdjMatrix.wipeNode(node);
                break;
            default:
                System.err.println("incorrect colour");
                break;
        }
    }

    public void printBoard()
    {
        for(int i=0;i<this.size;i++)
        {
            for(int j=0;j<this.size;j++)
            {
                System.out.print(this.board[i][j].getValue() + " ");
            }

            System.out.println();
        }
    }
}
