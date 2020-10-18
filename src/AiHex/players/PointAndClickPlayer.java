package AiHex.players;

import AiHex.gameMechanics.Runner;

import java.awt.Point;
import java.util.ArrayList;

import AiHex.hexBoards.Board;
import AiHex.gameMechanics.Move;

public class PointAndClickPlayer implements Player {
    private Runner game = null;
    private int colour = 0;

    public PointAndClickPlayer(Runner game, int colour) {
        this.game = game;
        this.colour = colour;
    }

    public Move getMove() {
        switch (colour) {
            case Board.RED:
                System.out.print("Red move: ");
                break;
            case Board.BLUE:
                System.out.print("Blue move: ");
                break;
        }

        while (game.getBoard().getSelected() == null) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Point choice = game.getBoard().getSelected();

        Move move = new Move(colour, choice.x, choice.y);
        System.out.println("x: "+ choice.x + "y: " + choice.y);


        game.getBoard().setLatestMove(move);

        // x = column number (horizontal movement)
        // y = row number (vertical movement)

        game.getBoard().setSelected(null);

        return move; //returning the move selected by the user
    }

    public ArrayList<Board> getAuxBoards() {
        return new ArrayList<Board>();
    }
}
