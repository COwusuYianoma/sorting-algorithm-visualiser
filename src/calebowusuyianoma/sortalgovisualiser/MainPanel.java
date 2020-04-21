package calebowusuyianoma.sortalgovisualiser;

import javax.swing.Action;
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
    private ArrayGenerator arrayGenerator;
    private ArrayList<Integer> data;
    private Sort sort;
    private BubbleSort bubbleSort;
    private MergeSort mergeSort;
    private Timer timer;
    private int verticalSpace, spaceBetweenBars;
    private final String defaultText = "Select a sorting algorithm";
    private String sortAlgorithm;
    private boolean running;

    public MainPanel() {
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

        JPanel panel = new JPanel();

        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(this);

        String[] sortingAlgorithms = {defaultText, "Bubble sort", "Merge sort"};
        JComboBox sortingAlgorithmList = new JComboBox(sortingAlgorithms);
        sortingAlgorithmList.setSelectedIndex(0);
        sortingAlgorithmList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedAlgorithm = (String)sortingAlgorithmList.getSelectedItem();
                if(!running) {
                    System.out.println("Sort isn't running so going to set sort algo to: " + selectedAlgorithm);
                    setAlgorithm(selectedAlgorithm);
                }
            }
        });

        panel.add(sortButton);
        panel.add(sortingAlgorithmList);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.PAGE_END);

        // add(sortButton, BorderLayout.PAGE_END);
        // add(comboBox, BorderLayout.PAGE_END);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int maxBarHeight = getHeight() - verticalSpace;
        int maxValue = max(data);

        if(sort != null && sort.running) {
            switch (sortAlgorithm) {
                case(BubbleSort.name):
                    paintComponentForBubbleSort(g, maxValue, maxBarHeight);
                    break;
                case(MergeSort.name):
                    paintComponentForMergeSort(g, maxValue, maxBarHeight);
                    break;
                default:
                    break;
            }
        } else {
            int x = 5;
            int width = (getWidth() / data.size()) - spaceBetweenBars;
            for (int i = 0; i < data.size(); i++) {
                g.setColor(Color.BLACK);
                int height = (int) (((double) data.get(i) / maxValue) * maxBarHeight);
                g.fillRect(x, 0, width, height);
                x += (width + spaceBetweenBars);
            }
        }

    }

    private void paintComponentForBubbleSort(Graphics g, int maxValue, int maxBarHeight) {
        int[] pointers = bubbleSort.getPointers();
        int x = 5;
        int width = (getWidth() / data.size()) - spaceBetweenBars;
        for (int i = 0; i < data.size(); i++) {
            if(bubbleSort.sorted) {
                g.setColor(Color.MAGENTA);
            } else if(bubbleSort.running && contains(pointers, i)) {
                g.setColor(Color.CYAN);
            } else {
                g.setColor(Color.BLACK);
            }

            int height = (int)(((double)data.get(i) / maxValue) * maxBarHeight);
            g.fillRect(x, 0, width, height);
            x += (width + spaceBetweenBars);
        }
    }

    private void paintComponentForMergeSort(Graphics g, int maxValue, int maxBarHeight) {
        int currentTreeNode = data.size() - 1;
        Map<String, Integer> pointerMap = new HashMap<>();
        Map<Integer, Boolean> merged = new HashMap<>();
        if(mergeSort.running) {
            currentTreeNode = mergeSort.getCurrentTreeNode();
            Map<Integer, Map<String, Integer>> pointerMaps = mergeSort.getPointerMaps();
            pointerMap = pointerMaps.get(currentTreeNode);
            merged = mergeSort.getMerged();
        }

        int x = 5;
        int width = (getWidth() / data.size()) - spaceBetweenBars;
        for (int i = 0; i < data.size(); i++) {
            if(mergeSort.sorted) {
                g.setColor(Color.MAGENTA);
            } else if(mergeSort.running && merged.containsKey(currentTreeNode)) {
                if(pointerMap.get(mergeSort.LOW) <= i && pointerMap.get(mergeSort.HIGH) >= i) {
                    g.setColor(Color.MAGENTA);
                } else {
                    g.setColor(Color.BLACK);
                }
            } else if(mergeSort.running && pointerMap.containsValue(i)) {
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if(sortAlgorithm != null) {
            switch(sortAlgorithm) {
                case(BubbleSort.name):
                    actionPerformedBubbleSort(e);
                    break;
                case(MergeSort.name):
                    actionPerformedMergeSort(e);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Please select a sorting algorithm!");
                    break;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a sorting algorithm!");
        }

        repaint();
    }

    private void actionPerformedBubbleSort(ActionEvent e) {
        if (bubbleSort.running && !bubbleSort.justRanSwap()) {
            bubbleSort.swap(data);
        }

        if(bubbleSort.running && bubbleSort.justRanSwap()) {
            bubbleSort.adjustPointers(data);
        }

        if(!bubbleSort.running && e.getActionCommand() != null) {
            if (e.getActionCommand().equals("Sort")) {
                if(sortAlgorithm == null) {
                    JOptionPane.showMessageDialog(this, "Please select a sorting algorithm!");
                } else {
                    timer.start();
                    bubbleSort.adjustPointers(data);
                    running = true;
                }
            }
        }

        if(bubbleSort.sorted) {
            timer.stop();
            bubbleSort.setRunning(false);
            running = false;
        }

        repaint();
    }

    private void actionPerformedMergeSort(ActionEvent e) {
        if(mergeSort.sorted) {
            timer.stop();
            mergeSort.setRunning(false);
            running = false;

            return;
        }

        if(mergeSort.running) {
            mergeSort.adjustPointers(data);
        }

        if(!mergeSort.running && e.getActionCommand() != null) {
            if (e.getActionCommand().equals("Sort")) {
                if(sortAlgorithm == null) {
                    JOptionPane.showMessageDialog(this, "Please select a sorting algorithm!");
                } else {
                    timer.start();
                    mergeSort.adjustPointers(data);
                    running = true;
                }
            }
        }

        repaint();
    }

    private void setAlgorithm(String selectedAlgorithm) {
        switch(selectedAlgorithm) {
            case(BubbleSort.name):
                bubbleSort = new BubbleSort();
                sort = bubbleSort;
                sortAlgorithm = selectedAlgorithm;
                break;
            case(MergeSort.name):
                mergeSort = new MergeSort();
                sort = mergeSort;
                sortAlgorithm = selectedAlgorithm;
                break;
            default:
                break;
        }
    }

    private boolean contains(int[] arr, final int key) {
        return Arrays.stream(arr).anyMatch(i -> i == key);
    }
}