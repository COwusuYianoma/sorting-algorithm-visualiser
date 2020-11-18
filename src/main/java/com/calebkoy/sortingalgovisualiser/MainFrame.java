package com.calebkoy.sortingalgovisualiser;

import javax.swing.JFrame;
import java.awt.Dimension;

public class MainFrame extends JFrame {
    public MainFrame() {
        super("Sorting algorithm visualiser");
        MainPanel mainPanel = new MainPanel(new DefaultOptionPane(), 10,
                1, 100);
        add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1250, 600));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}