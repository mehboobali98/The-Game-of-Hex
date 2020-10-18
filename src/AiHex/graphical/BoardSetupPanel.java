package AiHex.graphical;

import AiHex.gameMechanics.Runner;
import AiHex.gameMechanics.SeasonMechanics;
import AiHex.hexBoards.Board;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class BoardSetupPanel extends JPanel {

    private final JLabel sizeLabel;
    private final SpinnerNumberModel boardSizeConstraints;
    private final JSpinner boardSizeSpinner;
    private final JLabel turnStyleLabel;


    public BoardSetupPanel() {
        super();

        this.sizeLabel = new JLabel("Board size:");
        this.boardSizeConstraints = new SpinnerNumberModel(
                Board.DEFAULT_BOARD_SIZE,
                Board.MIN_SUPPORTED_BOARD_SIZE,
                Board.MAX_SUPPORTED_BOARD_SIZE,
                1);
        this.boardSizeSpinner = new JSpinner(boardSizeConstraints);

        this.turnStyleLabel = new JLabel("turn style:");

        setup();
    }

    private void setup() {
        this.add(sizeLabel);
        this.add(boardSizeSpinner);

        this.add(turnStyleLabel);
    }

    public int getBoardSize() {
        return (Integer) boardSizeSpinner.getValue();
    }

}
