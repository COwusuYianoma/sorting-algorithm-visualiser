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
    private final int SORTING_SPEED_SLIDER_MAJOR_TICK_SPACING = 50;
    private final int SORTING_SPEED_SLIDER_MINOR_TICK_SPACING = 10;
    private final int SORTING_SPEED_SLIDER_MINIMUM_VALUE = 0;
    private final int SORTING_SPEED_SLIDER_INITIAL_VALUE = 50;
    private final int SORTING_SPEED_SLIDER_MAXIMUM_VALUE = 100;
    private final int TIMER_INITIAL_DELAY = 100;
    private final int TIMER_DELAY_MULTIPLIER = 5000;
    private final int VERTICAL_SPACE_UNDER_LARGEST_BAR = 150;
    private final int SPACE_BETWEEN_BARS = 5;
    private final int MAX_ARRAY_VALUE = 100;
    private final int DEFAULT_ARRAY_SIZE = 10;
    private final int ARRAY_SIZE_SPINNER_MINIMUM = 2;
    private final int ARRAY_SIZE_SPINNER_MAXIMUM = 100;
    private final int ARRAY_SIZE_SPINNER_STEP = 1;
    private final String defaultText = "Select a sorting algorithm";

    private long startTime, endTime, runningTime;
    private boolean sorting, sortingAlgorithmJustRan;
    private ArrayGenerator arrayGenerator;
    private ArrayList<Integer> data, originalData;
    private BubbleSort bubbleSort;
    private MergeSort mergeSort;
    private Timer timer;
    private String sortingAlgorithmSelected, sortingAlgorithmRunning;
    private String[] sortingAlgorithmsListText;
    private JComboBox sortingAlgorithmsList;
    private JButton generateArrayButton, sortButton, undoSortButton;
    private JPanel panel, sortingSpeedSliderPanel;
    private JSpinner arraySizeSpinner;
    private JLabel arraySizeSpinnerLabel, runningTimeLabel, sortingSpeedSliderLabel;
    private JSlider sortingSpeedSlider;
    private SpinnerModel spinnerModel;

    public MainPanel() {
        timer = new Timer(TIMER_INITIAL_DELAY, this);
        sortingAlgorithmSelected = defaultText;
        arrayGenerator = new ArrayGenerator();
        data = arrayGenerator.generateRandomArray(DEFAULT_ARRAY_SIZE, MAX_ARRAY_VALUE);
        setOriginalData();

        arraySizeSpinnerLabel = new JLabel("Array size: ");
        spinnerModel = new SpinnerNumberModel(DEFAULT_ARRAY_SIZE, ARRAY_SIZE_SPINNER_MINIMUM,
                ARRAY_SIZE_SPINNER_MAXIMUM, ARRAY_SIZE_SPINNER_STEP);
        arraySizeSpinner = new JSpinner(spinnerModel);
        arraySizeSpinnerLabel.setLabelFor(arraySizeSpinner);

        generateArrayButton = new JButton("Generate random array");
        generateArrayButton.addActionListener(e -> {
            if(!sorting) {
                int arraySize = (int) arraySizeSpinner.getValue();
                data = arrayGenerator.generateRandomArray(arraySize, MAX_ARRAY_VALUE);
                setOriginalData();
                sortingAlgorithmJustRan = false;
                resetRunningTime();
                repaint();
            }
        });

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

        sortingSpeedSliderPanel = new JPanel();
        sortingSpeedSliderPanel.setLayout(new BoxLayout(sortingSpeedSliderPanel, BoxLayout.PAGE_AXIS));

        sortingSpeedSliderLabel = new JLabel("Sorting speed");
        sortingSpeedSliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        sortingSpeedSlider = new JSlider(JSlider.HORIZONTAL, SORTING_SPEED_SLIDER_MINIMUM_VALUE,
                SORTING_SPEED_SLIDER_MAXIMUM_VALUE, SORTING_SPEED_SLIDER_INITIAL_VALUE);
        sortingSpeedSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        sortingSpeedSlider.setMajorTickSpacing(SORTING_SPEED_SLIDER_MAJOR_TICK_SPACING);
        sortingSpeedSlider.setMinorTickSpacing(SORTING_SPEED_SLIDER_MINOR_TICK_SPACING);
        sortingSpeedSlider.setPaintTicks(true);
        sortingSpeedSlider.setPaintLabels(true);
        sortingSpeedSlider.setVisible(true);
        sortingSpeedSlider.addChangeListener(e -> {
            respondToSpeedChange(e);
        });

        sortingSpeedSliderPanel.add(sortingSpeedSliderLabel);
        sortingSpeedSliderPanel.add(sortingSpeedSlider);

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
                    resetRunningTime();
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Please run a sorting algorithm!");
                }
            }
        });

        runningTimeLabel = new JLabel("Running time: ");

        panel = new JPanel();
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

        int maxBarHeight = getHeight() - VERTICAL_SPACE_UNDER_LARGEST_BAR;
        int maxValue = max(data);

        if(sorting) {
            paintComponentForSortingAlgorithm(g, maxValue, maxBarHeight);
        } else {
            int x = 5;
            int width = (getWidth() / data.size()) - SPACE_BETWEEN_BARS;

            if(sortingAlgorithmJustRan) {
                g.setColor(Color.MAGENTA);
            } else {
                g.setColor(Color.BLACK);
            }

            for (int i = 0; i < data.size(); i++) {
                fillRectangles(g, i, maxValue, maxBarHeight, x, width);
                x += (width + SPACE_BETWEEN_BARS);
            }
        }
    }

    private void setOriginalData() {
        originalData = new ArrayList<>();
        for(int i = 0; i < data.size(); i++) {
            originalData.add(data.get(i));
        }
    }

    private void respondToSpeedChange(ChangeEvent e) {
        if(sorting) {
            JSlider source = (JSlider) e.getSource();
            if(!source.getValueIsAdjusting()) {
                int sortingSpeed = source.getValue();
                if(sortingSpeed == 0) {
                    timer.stop();
                } else {
                    int delay = TIMER_DELAY_MULTIPLIER / sortingSpeed;
                    timer.setDelay(delay);
                    if(!timer.isRunning()) {
                        timer.start();
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
                int delay = 5000 / sortingSpeedSliderValue;
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
            runningTime = endTime - startTime;
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
            runningTime = endTime - startTime;
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
                int delay = 5000 / sortingSpeedSliderValue;
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
        int width = (getWidth() / data.size()) - SPACE_BETWEEN_BARS;
        for (int i = 0; i < data.size(); i++) {
            if(bubbleSort.sorted()) {
                g.setColor(Color.MAGENTA);
            } else if(bubbleSort.running() && contains(pointers, i)) {
                g.setColor(Color.CYAN);
            } else {
                g.setColor(Color.BLACK);
            }

            fillRectangles(g, i, maxValue, maxBarHeight, x, width);
            x += (width + SPACE_BETWEEN_BARS);
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
        int width = (getWidth() / data.size()) - SPACE_BETWEEN_BARS;
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
            x += (width + SPACE_BETWEEN_BARS);
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