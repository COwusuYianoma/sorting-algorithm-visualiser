package calebowusuyianoma.sortalgovisualiser;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.max;

public class MainPanel extends JPanel implements ActionListener {
    private final int timerDelayMultiplier = 5000;
    private final int spaceBetweenBars = 5;
    private final Timer timer = new Timer(100, this);
    private final JSlider sortingSpeedSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
    private final JLabel runningTimeLabel = new JLabel("Running time: ");

    private long startTime, endTime, runningTime;
    private boolean sorting, sortingAlgorithmJustRan;
    private ArrayList<Integer> data, originalData;
    private BubbleSort bubbleSort;
    private MergeSort mergeSort;
    private String sortingAlgorithmSelected, sortingAlgorithmRunning;

    public MainPanel() {
        int defaultArraySize = 10;
        int maxArrayValue = 100;
        ArrayGenerator arrayGenerator = new ArrayGenerator();
        data = arrayGenerator.generateRandomArray(defaultArraySize, maxArrayValue);
        setOriginalData();

        JLabel arraySizeSpinnerLabel = new JLabel("Array size: ");
        SpinnerModel spinnerModel = new SpinnerNumberModel(defaultArraySize, 2, 100, 1);
        JSpinner arraySizeSpinner = new JSpinner(spinnerModel);
        arraySizeSpinnerLabel.setLabelFor(arraySizeSpinner);

        JButton generateArrayButton = new JButton("Generate random array");
        generateArrayButton.addActionListener(e -> {
            if(!sorting) {
                int arraySize = (int) arraySizeSpinner.getValue();
                data = arrayGenerator.generateRandomArray(arraySize, maxArrayValue);
                setOriginalData();
                sortingAlgorithmJustRan = false;
                resetRunningTime();
                repaint();
            }
        });

        String defaultText = "Select a sorting algorithm";
        sortingAlgorithmSelected = defaultText;

        String[] sortingAlgorithmsListText = new String[] {defaultText, "Bubble sort", "Merge sort"};
        JComboBox<String> sortingAlgorithmsList = new JComboBox<>(sortingAlgorithmsListText);
        sortingAlgorithmsList.setSelectedIndex(0);
        sortingAlgorithmsList.addActionListener(e -> {
            String selectedAlgorithm = (String)sortingAlgorithmsList.getSelectedItem();
            sortingAlgorithmSelected = selectedAlgorithm;
            if(!sorting && selectedAlgorithm != null) {
                setSortingAlgorithm(selectedAlgorithm);
            }
        });

        JPanel sortingSpeedSliderPanel = new JPanel();
        sortingSpeedSliderPanel.setLayout(new BoxLayout(sortingSpeedSliderPanel, BoxLayout.PAGE_AXIS));

        JLabel sortingSpeedSliderLabel = new JLabel("Sorting speed");
        sortingSpeedSliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        sortingSpeedSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        sortingSpeedSlider.setMajorTickSpacing(50);
        sortingSpeedSlider.setMinorTickSpacing(10);
        sortingSpeedSlider.setPaintTicks(true);
        sortingSpeedSlider.setPaintLabels(true);
        sortingSpeedSlider.setVisible(true);
        sortingSpeedSlider.addChangeListener(this::respondToSpeedChange);

        sortingSpeedSliderPanel.add(sortingSpeedSliderLabel);
        sortingSpeedSliderPanel.add(sortingSpeedSlider);

        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(this);

        JButton undoSortButton = new JButton("Undo sort");
        undoSortButton.addActionListener(e -> {
            if(!sorting) {
                if(sortingAlgorithmJustRan) {
                    for(int i = 0; i < originalData.size(); i++) {
                        data.set(i, originalData.get(i));
                    }

                    sortingAlgorithmJustRan = false;
                    setSortingAlgorithm(sortingAlgorithmSelected);
                    resetRunningTime();
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Please run a sorting algorithm!");
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(1000, 70));
        panel.add(arraySizeSpinnerLabel);
        panel.add(arraySizeSpinner);
        panel.add(generateArrayButton);
        panel.add(sortingAlgorithmsList);
        panel.add(sortingSpeedSliderPanel);
        panel.add(sortButton);
        panel.add(undoSortButton);
        panel.add(runningTimeLabel);

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

        int maxBarHeight = getHeight() - 150;
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

    private void setOriginalData() {
        originalData = new ArrayList<>();
        originalData.addAll(data);
    }

    private void respondToSpeedChange(ChangeEvent e) {
        if(sorting) {
            JSlider source = (JSlider) e.getSource();
            if(!source.getValueIsAdjusting()) {
                int sortingSpeed = source.getValue();
                if(sortingSpeed == 0) {
                    timer.stop();
                    long currentTime = System.currentTimeMillis();
                    runningTime += currentTime - startTime;
                    startTime = 0;
                } else {
                    int delay = timerDelayMultiplier / sortingSpeed;
                    timer.setDelay(delay);
                    if(!timer.isRunning()) {
                        timer.start();
                        startTime = System.currentTimeMillis();
                    }
                }
            }
        }
    }

    private void resetRunningTime() {
        runningTime = 0;
        setRunningTimeLabel();
    }

    private void setRunningTimeLabel() {
        String text = "Running time: "
                + ((!sorting && sortingAlgorithmJustRan) ? (((double)runningTime / 1000) + " s") : "");
        runningTimeLabel.setText(text);
    }

    private void actionPerformedBubbleSort(ActionEvent e) {
        if(bubbleSort.running() && !bubbleSort.justRanSwap()) {
            if(sortButtonClickedWhileTimerStoppedMidSort(e)) {
                JOptionPane.showMessageDialog(this, "The sorting speed is 0!");

                return;
            }
            bubbleSort.swap(data);
        }

        if(bubbleSort.running() && bubbleSort.justRanSwap()) {
            if(sortButtonClickedWhileTimerStoppedMidSort(e)) {
                JOptionPane.showMessageDialog(this, "The sorting speed is 0!");

                return;
            }
            bubbleSort.adjustPointers(data);
        }

        if(!bubbleSort.running() && e.getActionCommand() != null) {
            if (e.getActionCommand().equals("Sort")) {
                bubbleSort.setSorted(false);
                int sortingSpeedSliderValue = sortingSpeedSlider.getValue();

                if(sortingSpeedSliderValue == 0) {
                    JOptionPane.showMessageDialog(this, "The sorting speed is 0!");

                    return;
                }
                int delay = timerDelayMultiplier / sortingSpeedSliderValue;
                timer.setDelay(delay);
                timer.start();

                startTime = System.currentTimeMillis();
                bubbleSort.adjustPointers(data);
                sortingAlgorithmJustRan = false;
                sorting = true;
                resetRunningTime();
                sortingAlgorithmRunning = BubbleSort.name;
            }
        }

        if(bubbleSort.running() && bubbleSort.sorted()) {
            if(sortButtonClickedWhileTimerStoppedMidSort(e)) {
                JOptionPane.showMessageDialog(this, "The sorting speed is 0!");

                return;
            }
            endTime = System.currentTimeMillis();
            timer.stop();
            runningTime += endTime - startTime;
            startTime = 0;
            endTime = 0;
            bubbleSort.setRunning(false);
            sorting = false;
            sortingAlgorithmRunning = null;
            sortingAlgorithmJustRan = true;
            setRunningTimeLabel();
        }

        repaint();
    }

    private void actionPerformedMergeSort(ActionEvent e) {
        if(mergeSort.running() && mergeSort.sorted()) {
            if(sortButtonClickedWhileTimerStoppedMidSort(e)) {
                JOptionPane.showMessageDialog(this, "The sorting speed is 0!");

                return;
            }
            endTime = System.currentTimeMillis();
            timer.stop();
            runningTime += endTime - startTime;
            startTime = 0;
            endTime = 0;
            mergeSort.setRunning(false);
            sorting = false;
            sortingAlgorithmRunning = null;
            sortingAlgorithmJustRan = true;
            setRunningTimeLabel();
        }

        if(mergeSort.running()) {
            if(sortButtonClickedWhileTimerStoppedMidSort(e)) {
                JOptionPane.showMessageDialog(this, "The sorting speed is 0!");

                return;
            }
            mergeSort.adjustPointers(data);
        }

        if(!mergeSort.running() && e.getActionCommand() != null) {
            if (e.getActionCommand().equals("Sort")) {
                mergeSort.setSorted(false);

                int sortingSpeedSliderValue = sortingSpeedSlider.getValue();
                if(sortingSpeedSliderValue == 0) {
                    JOptionPane.showMessageDialog(this, "The sorting speed is 0!");

                    return;
                }
                int delay = timerDelayMultiplier / sortingSpeedSliderValue;
                timer.setDelay(delay);
                timer.start();

                startTime = System.currentTimeMillis();
                mergeSort.adjustPointers(data);
                sortingAlgorithmJustRan = false;
                sorting = true;
                resetRunningTime();
                sortingAlgorithmRunning = MergeSort.name;
            }
        }

        repaint();
    }

    private boolean sortButtonClickedWhileTimerStoppedMidSort(ActionEvent e) {
        return e.getActionCommand() != null && e.getActionCommand().equals("Sort")
                && sortingSpeedSlider.getValue() == 0;
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
                if(pointerMap.get(MergeSort.LOW) <= i && pointerMap.get(MergeSort.HIGH) >= i) {
                    g.setColor(Color.MAGENTA);
                } else {
                    g.setColor(Color.BLACK);
                }
            } else if(mergeSort.running() && pointerMap.containsValue(i)) {
                if((pointerMap.get(MergeSort.LOW) == i || pointerMap.get(MergeSort.HIGH) == i)
                        && pointerMap.get(MergeSort.MIDDLE) == i) {
                    g.setColor(Color.GREEN);
                } else if(pointerMap.get(MergeSort.MIDDLE) == i) {
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