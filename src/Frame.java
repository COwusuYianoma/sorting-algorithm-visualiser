import javax.swing.JFrame;
import java.awt.Dimension;

public class Frame extends JFrame {
    public Frame() {
        super("Sorting algorithm visualiser");

        Panel panel = new Panel();
        add(panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 1000));
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }
}

// These two lines cause the window to open up at a reasonable size.
//        setPreferredSize(new Dimension(1000, 1000));
//        pack();

//        setLayout(new GridBagLayout());
//        GridBagConstraints c = new GridBagConstraints();
//        c.weightx = 1;
//        c.weighty = 1;
//        c.gridx = 0;
//        c.gridy = 0;
//        c.fill = GridBagConstraints.HORIZONTAL;
//        Panel panel = new Panel();
//        add(panel, c);

//        setLayout(new BorderLayout());
//        Panel panel = new Panel();
//        add(panel, BorderLayout.PAGE_START);