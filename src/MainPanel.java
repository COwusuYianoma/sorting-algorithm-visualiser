import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import static java.util.Collections.max;

public class MainPanel extends JPanel implements ActionListener {
    private ArrayGenerator arrayGenerator;
    private ArrayList<Integer> data;
    private BubbleSort bubbleSort;
    private Timer timer;
    private int verticalSpace, spaceBetweenBars;

    public MainPanel() {
        bubbleSort = new BubbleSort();
        timer = new Timer(200, this);
        verticalSpace = 100;
        spaceBetweenBars = 10;

        arrayGenerator = new ArrayGenerator();
        int max = 100;
        int size = 20;
        data = arrayGenerator.generateRandomArray(size, max);

//        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3, 1));

        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(this);
        setLayout(new BorderLayout());
        add(sortButton, BorderLayout.PAGE_END);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int maxBarHeight = getHeight() - verticalSpace;
        int maxValue = max(data);
        int[] pointers = bubbleSort.getPointers();
        int x = 5;
        int width = (getWidth() / data.size()) - spaceBetweenBars;
        for (int i = 0; i < data.size(); i++) {
            if(bubbleSort.isSorted()) {
                g.setColor(Color.MAGENTA);
            } else if(bubbleSort.isRunning() && contains(pointers, i)) {
                g.setColor(Color.CYAN);
            } else {
                g.setColor(Color.BLACK);
            }

            int height = (int)(((double)data.get(i) / maxValue) * maxBarHeight);
            g.fillRect(x, 0, width, height);
            x += (width + spaceBetweenBars);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (bubbleSort.isRunning() && !bubbleSort.justRanSwap()) {
            bubbleSort.swap(data);
        }

        if(bubbleSort.isRunning() && bubbleSort.justRanSwap()) {
            bubbleSort.adjustPointers(data);
        }

        if(!bubbleSort.isRunning() && e.getActionCommand() != null) {
            if (e.getActionCommand().equals("Sort")) {
                bubbleSort.adjustPointers(data);
                timer.start();
            }
        }

        if(bubbleSort.isSorted()) {
            timer.stop();
        }

        repaint();
    }

    private boolean contains(int[] arr, final int key) {
        return Arrays.stream(arr).anyMatch(i -> i == key);
    }
}