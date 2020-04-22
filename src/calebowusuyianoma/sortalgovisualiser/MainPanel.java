package calebowusuyianoma.sortalgovisualiser;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
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
    private final String defaultText = "Select a sorting algorithm";

    private int verticalSpace, spaceBetweenBars, max, size;
    private boolean sorting, sortingAlgorithmJustRan;
    private ArrayGenerator arrayGenerator;
    private ArrayList<Integer> data, originalData;
    private BubbleSort bubbleSort;
    private MergeSort mergeSort;
    private Timer timer;
    private String sortingAlgorithmSelected, sortingAlgorithmRunning;
    private String[] sortingAlgorithmsListText;
    private JComboBox sortingAlgorithmsList;
    private JButton sortButton, undoSortButton;
    private JPanel panel;

    public MainPanel() {
        timer = new Timer(100, this);
        sortingAlgorithmSelected = defaultText;
        verticalSpace = 150;
        spaceBetweenBars = 5;
        arrayGenerator = new ArrayGenerator();
        max = 100;
        size = 10;
        data = arrayGenerator.generateRandomArray(size, max);
        originalData = new ArrayList<>();
        for(int i = 0; i < data.size(); i++) {
            originalData.add(data.get(i));
        }

        //data = new ArrayList<>(Arrays.asList(9, 7, 5, 3, 1));
        //data = new ArrayList<>(Arrays.asList(8, 7, 6, 5, 4, 3, 2, 1));
        // data = new ArrayList<>(Arrays.asList(7, 6, 5, 4, 3, 2, 1));

        sortingAlgorithmsListText = new String[] {defaultText, "Bubble sort", "Merge sort"};
        sortingAlgorithmsList = new JComboBox(sortingAlgorithmsListText);
        sortingAlgorithmsList.setSelectedIndex(0);
        sortingAlgorithmsList.addActionListener(e -> {
            String selectedAlgorithm = (String)sortingAlgorithmsList.getSelectedItem();
            sortingAlgorithmSelected = selectedAlgorithm;
            if(!sorting) {
                setSortingAlgorithm(selectedAlgorithm);
            }
        });

        sortButton = new JButton("Sort");
        sortButton.addActionListener(this);

        undoSortButton = new JButton("Undo sort");
        undoSortButton.addActionListener(e -> {
            if(!sorting) {
                if(sortingAlgorithmJustRan) {
                    for(int i = 0; i < originalData.size(); i++) {
                        data.set(i, originalData.get(i));
                    }

                    sortingAlgorithmJustRan = false;
                    setSortingAlgorithm(sortingAlgorithmSelected);
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Please run a sorting algorithm!");
                }
            }
        });

        panel = new JPanel();
        panel.add(sortingAlgorithmsList);
        panel.add(sortButton);
        panel.add(undoSortButton);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.PAGE_END);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(sorting) {
            switch(sortingAlgorithmRunning) {
                case(BubbleSort.name):
                    actionPerformedBubbleSort(e);
                    break;
                case(MergeSort.name):
                    actionPerformedMergeSort(e);
                    break;
                default:
                    break;
            }
        }
        else {
            switch(sortingAlgorithmSelected) {
                case(BubbleSort.name):
                    bubbleSort = new BubbleSort();
                    actionPerformedBubbleSort(e);
                    break;
                case(MergeSort.name):
                    mergeSort = new MergeSort();
                    actionPerformedMergeSort(e);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Please select a sorting algorithm!");
                    break;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int maxBarHeight = getHeight() - verticalSpace;
        int maxValue = max(data);

        if(sorting) {
            paintComponentForSortingAlgorithm(g, maxValue, maxBarHeight);
        } else {
            int x = 5;
            int width = (getWidth() / data.size()) - spaceBetweenBars;

            if(sortingAlgorithmJustRan) {
                g.setColor(Color.MAGENTA);
            } else {
                g.setColor(Color.BLACK);
            }

            for (int i = 0; i < data.size(); i++) {
                fillRectangles(g, i, maxValue, maxBarHeight, x, width);
                x += (width + spaceBetweenBars);
            }
        }
    }

    private void actionPerformedBubbleSort(ActionEvent e) {
        if(bubbleSort.running() && !bubbleSort.justRanSwap()) {
            bubbleSort.swap(data);
        }

        if(bubbleSort.running() && bubbleSort.justRanSwap()) {
            bubbleSort.adjustPointers(data);
        }

        if(!bubbleSort.running() && e.getActionCommand() != null) {
            if (e.getActionCommand().equals("Sort")) {
                bubbleSort.setSorted(false);
                timer.start();
                bubbleSort.adjustPointers(data);
                sortingAlgorithmJustRan = false;
                sorting = true;
                sortingAlgorithmRunning = BubbleSort.name;
            }
        }

        if(bubbleSort.running() && bubbleSort.sorted()) {
            timer.stop();
            bubbleSort.setRunning(false);
            sorting = false;
            sortingAlgorithmRunning = null;
            sortingAlgorithmJustRan = true;
        }

        repaint();
    }

    private void actionPerformedMergeSort(ActionEvent e) {
        if(mergeSort.running() && mergeSort.sorted()) {
            timer.stop();
            mergeSort.setRunning(false);
            sorting = false;
            sortingAlgorithmRunning = null;
            sortingAlgorithmJustRan = true;
        }

        if(mergeSort.running()) {
            mergeSort.adjustPointers(data);
        }

        if(!mergeSort.running() && e.getActionCommand() != null) {
            if (e.getActionCommand().equals("Sort")) {
                mergeSort.setSorted(false);
                timer.start();
                mergeSort.adjustPointers(data);
                sortingAlgorithmJustRan = false;
                sorting = true;
                sortingAlgorithmRunning = MergeSort.name;
            }
        }

        repaint();
    }

    private void setSortingAlgorithm(String selectedAlgorithm) {
        switch(selectedAlgorithm) {
            case(BubbleSort.name):
                bubbleSort = new BubbleSort();
                break;
            case(MergeSort.name):
                mergeSort = new MergeSort();
                break;
            default:
                break;
        }
    }

    private void paintComponentForSortingAlgorithm(Graphics g, int maxValue, int maxBarHeight) {
        switch (sortingAlgorithmRunning) {
            case(BubbleSort.name):
                paintComponentForBubbleSort(g, maxValue, maxBarHeight);
                break;
            case(MergeSort.name):
                paintComponentForMergeSort(g, maxValue, maxBarHeight);
                break;
            default:
                break;
        }
    }

    private void paintComponentForBubbleSort(Graphics g, int maxValue, int maxBarHeight) {
        int[] pointers = bubbleSort.getPointers();
        int x = 5;
        int width = (getWidth() / data.size()) - spaceBetweenBars;
        for (int i = 0; i < data.size(); i++) {
            if(bubbleSort.sorted()) {
                g.setColor(Color.MAGENTA);
            } else if(bubbleSort.running() && contains(pointers, i)) {
                g.setColor(Color.CYAN);
            } else {
                g.setColor(Color.BLACK);
            }

            fillRectangles(g, i, maxValue, maxBarHeight, x, width);
            x += (width + spaceBetweenBars);
        }
    }

    private void paintComponentForMergeSort(Graphics g, int maxValue, int maxBarHeight) {
        int currentTreeNode = data.size() - 1;
        Map<String, Integer> pointerMap = new HashMap<>();
        Map<Integer, Boolean> merged = new HashMap<>();
        if(mergeSort.running()) {
            currentTreeNode = mergeSort.getCurrentTreeNode();
            Map<Integer, Map<String, Integer>> pointerMaps = mergeSort.getPointerMaps();
            pointerMap = pointerMaps.get(currentTreeNode);
            merged = mergeSort.getMerged();
        }

        int x = 5;
        int width = (getWidth() / data.size()) - spaceBetweenBars;
        for (int i = 0; i < data.size(); i++) {
            if(mergeSort.sorted()) {
                g.setColor(Color.MAGENTA);
            } else if(mergeSort.running() && merged.containsKey(currentTreeNode)) {
                if(pointerMap.get(mergeSort.LOW) <= i && pointerMap.get(mergeSort.HIGH) >= i) {
                    g.setColor(Color.MAGENTA);
                } else {
                    g.setColor(Color.BLACK);
                }
            } else if(mergeSort.running() && pointerMap.containsValue(i)) {
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

            fillRectangles(g, i, maxValue, maxBarHeight, x, width);
            x += (width + spaceBetweenBars);
        }
    }

    private void fillRectangles(Graphics g, int index, int maxValue, int maxBarHeight, int x, int width) {
        int height = (int)(((double)data.get(index) / maxValue) * maxBarHeight);
        g.fillRect(x, 0, width, height);
    }

    private boolean contains(int[] arr, final int key) {
        return Arrays.stream(arr).anyMatch(i -> i == key);
    }
}