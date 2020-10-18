package AiHex.gameMechanics;

import AiHex.players.Player;
import AiHex.hexBoards.GameBoard;

public interface Runner {
    public static final int FAIR_TURN = 0;

    public static final String GAME_LIST = "Regular";
    public static final int GAME_CODES =  FAIR_TURN;

    public GameBoard getBoard();

    public Player getPlayerRed();

    public Player getPlayerBlue();

    public void stopGame();

    public String getCommentary();
}
