package calebowusuyianoma.sortalgovisualiser;

import javax.swing.JFrame;
import java.awt.Dimension;

public class MainFrame extends JFrame {
    public MainFrame() {
        super("Sorting algorithm visualiser");
        MainPanel mainPanel = new MainPanel();
        add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1250, 600));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}