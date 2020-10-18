package AiHex.graphical;

import AiHex.graphical.boardPanels.HexGroupPanel;
import AiHex.graphical.boardPanels.HexPanel;
import AiHex.gameMechanics.Runner;
import AiHex.gameMechanics.GameRunner;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import AiHex.hexBoards.Board;
import AiHex.graphical.boardPanels.HexGamePanel;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public
class GUI extends JFrame implements ActionListener {

    public static GUI frame;
    private Thread gameThread;
    private JPanel activeBoardsPanel = null;
    private JPanel buttonPanel = null;
    private JButton startButton = new JButton("Start");
    private Runner game;
    private JPanel auxBoardsPanel;
    private JPanel playBoardPanel;
    private JPanel gameSettings;
    private JPanel settingsPanel;
    private PlayerChoicePanel redPlayerOptions;
    private PlayerChoicePanel bluePlayerOptions;
    private BoardSetupPanel boardSettings;

     public GUI() {

        gameSettings = new JPanel(new GridLayout(3, 1));
        redPlayerOptions = new PlayerChoicePanel("Red");
        bluePlayerOptions = new PlayerChoicePanel("Blue");
        boardSettings = new BoardSetupPanel();

        gameSettings.add(redPlayerOptions);
        gameSettings.add(bluePlayerOptions);
        gameSettings.add(boardSettings);

        buttonPanel = new JPanel(new GridLayout(2, 1));
        startButton.setMnemonic(KeyEvent.VK_SPACE);
        startButton.setActionCommand("start");
        startButton.setEnabled(true);
        startButton.addActionListener(this);

        buttonPanel.add(startButton);

        settingsPanel = new JPanel(new BorderLayout());
        settingsPanel.add(gameSettings, BorderLayout.CENTER);
        settingsPanel.add(buttonPanel, BorderLayout.EAST);

        this.add(settingsPanel, BorderLayout.SOUTH);
    }

    private void prepareGame() {

        int red = redPlayerOptions.getPlayerType();
        String[] redArgs = redPlayerOptions.getArgs();
        int blue = bluePlayerOptions.getPlayerType();
        String[] blueArgs = bluePlayerOptions.getArgs();
        int boardSize = boardSettings.getBoardSize();
        game = new GameRunner(boardSize, red, redArgs, blue, blueArgs);
        gameThread = (Thread) game;
    }

    public void generateBoardPanels() {

        if (activeBoardsPanel != null)
            this.remove(activeBoardsPanel);

        activeBoardsPanel = new JPanel();
        activeBoardsPanel.setLayout(new BorderLayout());

        playBoardPanel = new JPanel();
        playBoardPanel.setLayout(new BorderLayout());

        auxBoardsPanel = new JPanel();
        auxBoardsPanel.setLayout(new GridLayout(2, 1));

        JPanel tickerPanels = new JPanel();
        tickerPanels.setLayout(new GridLayout(2, 1));

        JPanel redPanel = new JPanel();
        redPanel.add(new JLabel("Red:"));

        tickerPanels.add(redPanel);

        JPanel bluePanel = new JPanel();
        bluePanel.add(new JLabel("Blue:"));


        tickerPanels.add(bluePanel);

        HexPanel mainBoardPanel = new HexGamePanel(game.getBoard());
        mainBoardPanel.startAnimation();

        playBoardPanel.add(mainBoardPanel, BorderLayout.CENTER);
        playBoardPanel.add(tickerPanels, BorderLayout.SOUTH);

        activeBoardsPanel.add(playBoardPanel, BorderLayout.CENTER);

        auxBoardsPanel.add(new HexGroupPanel(game.getPlayerRed()));
        auxBoardsPanel.add(new HexGroupPanel(game.getPlayerBlue()));

        activeBoardsPanel.add(auxBoardsPanel, BorderLayout.EAST);

        this.add(activeBoardsPanel, BorderLayout.CENTER);

        frame.pack();
    }

    public void actionPerformed(ActionEvent e) {
        if ("start".equals(e.getActionCommand())) {

            if (game != null)
                game.stopGame();

            this.prepareGame();
            generateBoardPanels();
            gameThread.start();

        }
    }
}
