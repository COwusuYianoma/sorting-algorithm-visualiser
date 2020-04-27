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
import java.util.Map;

import static java.util.Collections.max;

public class MainPanel extends JPanel implements ActionListener {
    private final int timerDelayMultiplier = 5000;
    private final int spaceBetweenBars = 5;
    private final String[] validSortingAlgorithms = new String[]{BubbleSort.getName(), InsertionSort.getName(),
            MergeSort.getName(), TimSort.getName()};
    private final Timer timer = new Timer(100, this);
    private final JSlider sortingSpeedSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
    private final JLabel runningTimeLabel = new JLabel("Running time: ");

    private long startTime, endTime, runningTime;
    private boolean sorting, sortingAlgorithmJustRan;
    private String sortingAlgorithmSelected, sortingAlgorithmRunning;
    private ArrayList<Integer> data, preSortedData;
    private BubbleSort bubbleSort;
    private InsertionSort insertionSort;
    private MergeSort mergeSort;
    private TimSort timSort;

    public MainPanel() {
        int defaultArraySize = 10;
        int minimumPossibleValue = 1;
        int maximumPossibleValue = 100;
        ArrayGenerator arrayGenerator = new ArrayGenerator();
        data = arrayGenerator.generateRandomIntegerArray(defaultArraySize, minimumPossibleValue, maximumPossibleValue);
        setPreSortedData();

        JLabel arraySizeSpinnerLabel = new JLabel("Array size: ");
        SpinnerModel spinnerModel = new SpinnerNumberModel(defaultArraySize, 2, 200, 1);
        JSpinner arraySizeSpinner = new JSpinner(spinnerModel);
        arraySizeSpinnerLabel.setLabelFor(arraySizeSpinner);

        JButton generateArrayButton = new JButton("Generate random array");
        generateArrayButton.addActionListener(e -> {
            if (!sorting) {
                int arraySize = (int) arraySizeSpinner.getValue();
                data = arrayGenerator.generateRandomIntegerArray(arraySize, minimumPossibleValue, maximumPossibleValue);
                setPreSortedData();
                sortingAlgorithmJustRan = false;
                resetRunningTime();
                repaint();
            }
        });

        String defaultSortingAlgorithmsListText = "Select a sorting algorithm";
        sortingAlgorithmSelected = defaultSortingAlgorithmsListText;

        String[] sortingAlgorithmsListText = new String[] {defaultSortingAlgorithmsListText, BubbleSort.getName(),
                InsertionSort.getName(), MergeSort.getName(), TimSort.getName()};
        JComboBox<String> sortingAlgorithmsList = new JComboBox<>(sortingAlgorithmsListText);
        sortingAlgorithmsList.setSelectedIndex(0);
        sortingAlgorithmsList.addActionListener(e ->
                sortingAlgorithmSelected = (String) sortingAlgorithmsList.getSelectedItem());

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
        sortingSpeedSlider.addChangeListener(this::respondToSortingSpeedSliderChange);

        sortingSpeedSliderPanel.add(sortingSpeedSliderLabel);
        sortingSpeedSliderPanel.add(sortingSpeedSlider);

        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(this);

        JButton undoSortButton = new JButton("Undo sort");
        undoSortButton.addActionListener(e -> {
            if (!sorting) {
                respondToUndoSortButtonClick();
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

    private void setPreSortedData() {
        preSortedData = new ArrayList<>();
        preSortedData.addAll(data);
    }

    private void resetRunningTime() {
        runningTime = 0;
        setRunningTimeLabel();
    }

    private void setRunningTimeLabel() {
        String text = "Running time: "
                + ((!sorting && sortingAlgorithmJustRan) ? (((double) runningTime / 1000) + " s") : "");
        runningTimeLabel.setText(text);
    }

    private void respondToSortingSpeedSliderChange(ChangeEvent e) {
        if (sorting) {
            JSlider source = (JSlider) e.getSource();
            if (!source.getValueIsAdjusting()) {
                int sortingSpeed = source.getValue();
                if (sortingSpeed == 0) {
                    timer.stop();
                    long currentTime = System.currentTimeMillis();
                    runningTime += currentTime - startTime;
                    startTime = 0;
                } else {
                    int delay = timerDelayMultiplier / sortingSpeed;
                    timer.setDelay(delay);
                    if (!timer.isRunning()) {
                        timer.start();
                        startTime = System.currentTimeMillis();
                    }
                }
            }
        }
    }

    private void respondToUndoSortButtonClick() {
        if (sortingAlgorithmJustRan) {
            for (int i = 0; i < preSortedData.size(); i++) {
                data.set(i, preSortedData.get(i));
            }

            sortingAlgorithmJustRan = false;
            resetRunningTime();
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Please run a sorting algorithm!");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (sorting) {
            switch (mapSortingAlgorithmStringToEnum(sortingAlgorithmRunning)) {
                case BUBBLESORT:
                    respondToAction(e, bubbleSort);
                    break;
                case INSERTIONSORT:
                    respondToAction(e, insertionSort);
                    break;
                case MERGESORT:
                    respondToAction(e, mergeSort);
                    break;
                case TIMSORT:
                    respondToAction(e, timSort);
                    break;
            }
        } else if (Arrays.asList(validSortingAlgorithms).contains(sortingAlgorithmSelected)) {
            runSortingAlgorithm();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a sorting algorithm!");
        }
    }

    private SortingAlgorithm mapSortingAlgorithmStringToEnum(String sortingAlgorithm) {
        if (sortingAlgorithm.equals(BubbleSort.getName())) return SortingAlgorithm.BUBBLESORT;
        if (sortingAlgorithm.equals(InsertionSort.getName())) return SortingAlgorithm.INSERTIONSORT;
        if (sortingAlgorithm.equals(MergeSort.getName())) return SortingAlgorithm.MERGESORT;
        if (sortingAlgorithm.equals(TimSort.getName())) return SortingAlgorithm.TIMSORT;

        return SortingAlgorithm.UNKNOWN;
    }

    private void respondToAction(ActionEvent e, Sort sort) {
        if (sortButtonClickedWhenSortingSpeedIsZero(e)) {
            JOptionPane.showMessageDialog(this, "The sorting speed is 0!");

            return;
        } else if (sort.sorted()) {
            timer.stop();
            updateFinalRunningTime();
            resetVariablesAfterSorting();
            setRunningTimeLabel();
            sort.setRunning(false);
        } else {
            sort.moveToNextStepInVisualisation(data);
        }

        repaint();
    }

    private boolean sortButtonClickedWhenSortingSpeedIsZero(ActionEvent e) {
        return e.getActionCommand().equals("Sort") && sortingSpeedSlider.getValue() == 0;
    }

    private void updateFinalRunningTime() {
        endTime = System.currentTimeMillis();
        runningTime += endTime - startTime;
    }

    private void resetVariablesAfterSorting() {
        startTime = 0;
        endTime = 0;
        sorting = false;
        sortingAlgorithmRunning = null;
        sortingAlgorithmJustRan = true;
    }

    private void runSortingAlgorithm() {
        int sortingSpeedSliderValue = sortingSpeedSlider.getValue();
        if (sortingSpeedSliderValue == 0) {
            JOptionPane.showMessageDialog(this, "The sorting speed is 0!");

            return;
        }

        Sort sort = initialiseSortingAlgorithm();
        sort.setSorted(false);
        startTimerBeforeSorting(sortingSpeedSliderValue);
        resetVariablesBeforeSorting();
        resetRunningTime();
        sort.moveToNextStepInVisualisation(data);
        repaint();
    }

    private Sort initialiseSortingAlgorithm() {
        switch (mapSortingAlgorithmStringToEnum(sortingAlgorithmSelected)) {
            case BUBBLESORT:
                sortingAlgorithmRunning = sortingAlgorithmSelected;
                bubbleSort = new BubbleSort();

                return bubbleSort;
            case INSERTIONSORT:
                sortingAlgorithmRunning = sortingAlgorithmSelected;
                insertionSort = new InsertionSort();

                return insertionSort;
            case MERGESORT:
                sortingAlgorithmRunning = sortingAlgorithmSelected;
                mergeSort = new MergeSort();

                return mergeSort;
            case TIMSORT:
                sortingAlgorithmRunning = sortingAlgorithmSelected;
                timSort = new TimSort();

                return timSort;
            case UNKNOWN:
            default:
                throw new IllegalStateException("Tried to initialise an unrecognised sorting algorithm: " +
                        sortingAlgorithmSelected);
        }
    }

    private void startTimerBeforeSorting(int sortingSpeedSliderValue) {
        timer.setDelay(timerDelayMultiplier / sortingSpeedSliderValue);
        timer.start();
    }

    private void resetVariablesBeforeSorting() {
        startTime = System.currentTimeMillis();
        sorting = true;
        sortingAlgorithmJustRan = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int maxBarHeight = getHeight() - 150;
        int maxArrayValue = max(data);

        if (sorting) {
            paintComponentForSortingAlgorithm(g, maxArrayValue, maxBarHeight);
        } else {
            int x = 5;
            int barWidth = (getWidth() / data.size()) - spaceBetweenBars;

            if (sortingAlgorithmJustRan) {
                g.setColor(Color.MAGENTA);
            } else {
                g.setColor(Color.BLACK);
            }

            for (int i = 0; i < data.size(); i++) {
                fillRectangle(g, i, maxArrayValue, maxBarHeight, x, barWidth);
                x += (barWidth + spaceBetweenBars);
            }
        }
    }

    private void paintComponentForSortingAlgorithm(Graphics g, int maxArrayValue, int maxBarHeight) {
        switch (mapSortingAlgorithmStringToEnum(sortingAlgorithmRunning)) {
            case BUBBLESORT:
                paintComponentForBubbleSort(g, maxArrayValue, maxBarHeight);
                break;
            case INSERTIONSORT:
                paintComponentForInsertionSort(g, maxArrayValue, maxBarHeight);
                break;
            case MERGESORT:
                paintComponentForMergeSort(g, maxArrayValue, maxBarHeight);
                break;
            case TIMSORT:
                paintComponentForTimSort(g, maxArrayValue, maxBarHeight);
                break;
            case UNKNOWN:
            default:
                throw new IllegalStateException("Tried to paint component for an unrecognised sorting algorithm: " +
                        sortingAlgorithmSelected);
        }
    }

    private void paintComponentForBubbleSort(Graphics g, int maxArrayValue, int maxBarHeight) {
        int[] forLoopVariables = bubbleSort.getForLoopVariables();
        int x = 5;
        int barWidth = (getWidth() / data.size()) - spaceBetweenBars;
        for (int i = 0; i < data.size(); i++) {
            if (bubbleSort.sorted()) {
                g.setColor(Color.MAGENTA);
            } else if (bubbleSort.running() && contains(forLoopVariables, i)) {
                g.setColor(Color.CYAN);
            } else {
                g.setColor(Color.BLACK);
            }

            fillRectangle(g, i, maxArrayValue, maxBarHeight, x, barWidth);
            x += (barWidth + spaceBetweenBars);
        }
    }

    private void fillRectangle(Graphics g, int index, int maxValue, int maxBarHeight, int x, int width) {
        int height = (int) (((double) data.get(index) / maxValue) * maxBarHeight);
        g.fillRect(x, 0, width, height);
    }

    private boolean contains(int[] arr, final int key) {
        return Arrays.stream(arr).anyMatch(i -> i == key);
    }

    private void paintComponentForInsertionSort(Graphics g, int maxArrayValue, int maxBarHeight) {
        int keyIndex = insertionSort.getKeyIndex();
        int key = insertionSort.getKey();
        int x = 5;
        int barWidth = (getWidth() / data.size()) - spaceBetweenBars;
        for (int i = 0; i < data.size(); i++) {
            if (i < keyIndex) {
                g.setColor(Color.ORANGE);
            } else if (i == keyIndex) {
                if (data.get(i) == key) {
                    g.setColor(Color.CYAN);
                } else {
                    g.setColor(Color.ORANGE);
                }
            } else {
                g.setColor(Color.BLACK);
            }

            fillRectangle(g, i, maxArrayValue, maxBarHeight, x, barWidth);
            x += (barWidth + spaceBetweenBars);
        }
    }

    private void paintComponentForMergeSort(Graphics g, int maxArrayValue, int maxBarHeight) {
        int currentTreeNode = mergeSort.getCurrentTreeNode();
        Map<String, Integer> currentTreeNodePointerMap = mergeSort.getTreeNodePointerMaps().get(currentTreeNode);
        Map<Integer, Boolean> mergedTreeNodes = mergeSort.getMergedTreeNodes();
        int x = 5;
        int barWidth = (getWidth() / data.size()) - spaceBetweenBars;
        for (int i = 0; i < data.size(); i++) {
            if (mergeSort.sorted()) {
                g.setColor(Color.MAGENTA);
            } else if (mergeSort.running() && mergedTreeNodes.containsKey(currentTreeNode)) {
                if (currentTreeNodePointerMap.get(MergeSort.getLow()) <= i && currentTreeNodePointerMap.get(MergeSort.getHigh()) >= i) {
                    g.setColor(Color.MAGENTA);
                } else {
                    g.setColor(Color.BLACK);
                }
            } else if (mergeSort.running() && currentTreeNodePointerMap.containsValue(i)) {
                if ((currentTreeNodePointerMap.get(MergeSort.getLow()) == i || currentTreeNodePointerMap.get(MergeSort.getHigh()) == i)
                        && currentTreeNodePointerMap.get(MergeSort.getMiddle()) == i) {
                    g.setColor(Color.GREEN);
                } else if(currentTreeNodePointerMap.get(MergeSort.getMiddle()) == i) {
                    g.setColor(Color.YELLOW);
                } else {
                    g.setColor(Color.CYAN);
                }
            } else {
                g.setColor(Color.BLACK);
            }

            fillRectangle(g, i, maxArrayValue, maxBarHeight, x, barWidth);
            x += (barWidth + spaceBetweenBars);
        }
    }

    private void paintComponentForTimSort(Graphics g, int maxArrayValue, int maxBarHeight) {
        int left = timSort.getLeft();
        int keyIndex = timSort.getKeyIndex();
        int key = timSort.getKey();
        int mergeStartIndex = timSort.getMergeStartIndex();
        int mergeEndIndex = timSort.getMergeEndIndex();
        boolean insertionSorting = timSort.isInsertionSorting();
        int x = 5;
        int width = (getWidth() / data.size()) - spaceBetweenBars;
        for (int i = 0; i < data.size(); i++) {
            if(timSort.sorted()) {
                g.setColor(Color.MAGENTA);
            } else if(i >= left && i < keyIndex) {
                g.setColor(Color.ORANGE);
            } else if(i == keyIndex) {
                if(data.get(i) == key) {
                    g.setColor(Color.CYAN);
                } else {
                    g.setColor(Color.ORANGE);
                }
            } else if(!insertionSorting && i >= mergeStartIndex && i <= mergeEndIndex) {
                g.setColor(Color.ORANGE);
            } else {
                g.setColor(Color.BLACK);
            }

            fillRectangle(g, i, maxArrayValue, maxBarHeight, x, width);
            x += (width + spaceBetweenBars);
        }
    }
}