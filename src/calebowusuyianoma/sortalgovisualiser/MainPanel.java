package calebowusuyianoma.sortalgovisualiser;

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
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.max;

public class MainPanel extends JPanel implements ActionListener {
    private ArrayGenerator arrayGenerator;
    private ArrayList<Integer> data;
    private BubbleSort bubbleSort;
    private MergeSort mergeSort;
    private Timer timer;
    private int verticalSpace, spaceBetweenBars;

    public MainPanel() {
        bubbleSort = new BubbleSort();
        mergeSort = new MergeSort();
        timer = new Timer(100, this);
        verticalSpace = 150;
        spaceBetweenBars = 5;

        arrayGenerator = new ArrayGenerator();
        int max = 100;
        int size = 100;
        data = arrayGenerator.generateRandomArray(size, max);

        //data = new ArrayList<>(Arrays.asList(9, 7, 5, 3, 1));
        //data = new ArrayList<>(Arrays.asList(8, 7, 6, 5, 4, 3, 2, 1));
        // data = new ArrayList<>(Arrays.asList(7, 6, 5, 4, 3, 2, 1));

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

        // Get pointers
        // int[] pointers = bubbleSort.getPointers();

        int currentTreeNode = data.size() - 1;
        Map<String, Integer> pointerMap = new HashMap<>();
        Map<Integer, Boolean> merged = new HashMap<>();
        if(mergeSort.isRunning()) {
            currentTreeNode = mergeSort.getCurrentTreeNode();
            Map<Integer, Map<String, Integer>> pointerMaps = mergeSort.getPointerMaps();
            pointerMap = pointerMaps.get(currentTreeNode);
            merged = mergeSort.getMerged();
        }

        int x = 5;
        int width = (getWidth() / data.size()) - spaceBetweenBars;
        for (int i = 0; i < data.size(); i++) {
            //if(bubbleSort.isSorted()) {
            if(mergeSort.isSorted()) {
                System.out.println("The array is sorted");

                g.setColor(Color.MAGENTA);
                // } else if(bubbleSort.isRunning() && contains(pointers, i)) {
            } else if(mergeSort.isRunning() && merged.containsKey(currentTreeNode)) {
                if(pointerMap.get(mergeSort.LOW) <= i && pointerMap.get(mergeSort.HIGH) >= i) {
                    g.setColor(Color.MAGENTA);
                } else {
                    g.setColor(Color.BLACK);
                }
            } else if(mergeSort.isRunning() && pointerMap.containsValue(i)) {
                if((pointerMap.get(mergeSort.LOW) == i || pointerMap.get(mergeSort.HIGH) == i)
                        && pointerMap.get(mergeSort.MIDDLE) == i) {
                    g.setColor(Color.GREEN);
                } else if(pointerMap.get(mergeSort.MIDDLE) == i) {
                    g.setColor(Color.YELLOW);
                } else {
                    g.setColor(Color.CYAN);
                }
            } else {
                g.setColor(Color.BLACK);
            }

            int height = (int)(((double)data.get(i) / maxValue) * maxBarHeight);
            g.fillRect(x, 0, width, height);
            x += (width + spaceBetweenBars);
        }
    }

//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if (bubbleSort.isRunning() && !bubbleSort.justRanSwap()) {
//            bubbleSort.swap(data);
//        }
//
//        if(bubbleSort.isRunning() && bubbleSort.justRanSwap()) {
//            bubbleSort.adjustPointers(data);
//        }
//
//        if(!bubbleSort.isRunning() && e.getActionCommand() != null) {
//            if (e.getActionCommand().equals("Sort")) {
//                bubbleSort.adjustPointers(data);
//                timer.start();
//            }
//        }
//
//        if(bubbleSort.isSorted()) {
//            timer.stop();
//        }
//
//        repaint();
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(mergeSort.isSorted()) {
            timer.stop();
            mergeSort.setIsRunning(false);

            return;
        }

        if(mergeSort.isRunning()) {
            mergeSort.adjustPointers(data);
        }

        if(!mergeSort.isRunning() && e.getActionCommand() != null) {
            if (e.getActionCommand().equals("Sort")) {
                System.out.println("The Sort button was just clicked");
                System.out.println();

                timer.start();
                mergeSort.adjustPointers(data);
            }
        }

        repaint();
    }

    private boolean contains(int[] arr, final int key) {
        return Arrays.stream(arr).anyMatch(i -> i == key);
    }
}