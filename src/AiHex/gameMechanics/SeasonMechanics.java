package AiHex.gameMechanics;

import AiHex.hexBoards.Board;
import java.awt.Color;

public class SeasonMechanics {

    private static final int[] oneSeasonPattern = {0, 0};

    private static final Color[] oneSeasonColours = {Color.WHITE};

    private int[] seasonPattern;
    private Color[] seasonColours;
    private int seasonCount;
    private int redPosition;
    private int bluePosition;

    public SeasonMechanics(int numberOfSeasons) {

        this.seasonCount = numberOfSeasons;
        switch (seasonCount) {
            case 1:
                seasonPattern = oneSeasonPattern;
                seasonColours = oneSeasonColours;
                break;
            default:
                System.err.println("invalid season count");
                break;


        }
        redPosition = 0;
        bluePosition = 0;
    }

}
