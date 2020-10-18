package AiHex;

import AiHex.gameMechanics.GameRunner;
import AiHex.graphical.GUI;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Main {

    public static void main(String[] args) {
	// write your code here
        GUI.frame = new GUI();
        GUI.frame.setTitle("Hex");
        WindowListener l = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        GUI.frame.addWindowListener(l);
        GUI.frame.pack();
        GUI.frame.setVisible(true);

    }
}
