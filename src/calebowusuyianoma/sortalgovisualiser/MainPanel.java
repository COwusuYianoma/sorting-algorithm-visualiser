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
    private InsertionSort insertionSort;
    private MergeSort mergeSort;
    private TimSort timSort;
    private String sortingAlgorithmSelected, sortingAlgorithmRunning;

    public MainPanel() {
        int defaultArraySize = 10;
        int minPossibleValue = 1;
        int maxPossibleValue = 100;
        ArrayGenerator arrayGenerator = new ArrayGenerator();
        data = arrayGenerator.generateRandomArray(defaultArraySize, minPossibleValue, maxPossibleValue);
        setOriginalData();

        JLabel arraySizeSpinnerLabel = new JLabel("Array size: ");
        SpinnerModel spinnerModel = new SpinnerNumberModel(defaultArraySize, 2, 200, 1);
        JSpinner arraySizeSpinner = new JSpinner(spinnerModel);
        arraySizeSpinnerLabel.setLabelFor(arraySizeSpinner);

        JButton generateArrayButton = new JButton("Generate random array");
        generateArrayButton.addActionListener(e -> {
            if(!sorting) {
                int arraySize = (int) arraySizeSpinner.getValue();
                data = arrayGenerator.generateRandomArray(arraySize, minPossibleValue, maxPossibleValue);
                setOriginalData();
                sortingAlgorithmJustRan = false;
                resetRunningTime();
                repaint();
            }
        });

        String defaultText = "Select a sorting algorithm";
        sortingAlgorithmSelected = defaultText;

        String[] sortingAlgorithmsListText = new String[] {defaultText, BubbleSort.NAME,
                InsertionSort.NAME, MergeSort.NAME, TimSort.NAME};
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
        String switchVariable = sorting ? sortingAlgorithmRunning : sortingAlgorithmSelected;
        switch(switchVariable) {
            case(BubbleSort.NAME):
                actionPerformedBubbleSort(e);
                break;
            case(InsertionSort.NAME):
                actionPerformedInsertionSort(e);
                break;
            case(MergeSort.NAME):
                actionPerformedMergeSort(e);
                break;
            case(TimSort.NAME):
                actionPerformedTimSort(e);
                break;
            default:
                if(!sorting) {
                    JOptionPane.showMessageDialog(this, "Please select a sorting algorithm!");
                }
                break;
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
                fillRectangle(g, i, maxValue, maxBarHeight, x, width);
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
        if(!sorting) {
            bubbleSort = new BubbleSort();
        }

        if(bubbleSort.running()) {
            if(sortButtonClickedWhenSortingSpeedIsZero(e)) {
                JOptionPane.showMessageDialog(this, "The sorting speed is 0!");

                return;
            } else if(bubbleSort.sorted()) {
                timer.stop();
                updateFinalRunningTime();
                resetVariablesAfterSorting();
                setRunningTimeLabel();
                bubbleSort.setRunning(false);
            } else if(bubbleSort.justSwappedElements()) {
                bubbleSort.moveToNextStep(data);
            } else {
                bubbleSort.swap(data);
            }
        } else if(sortButtonClicked(e)) {
            int sortingSpeedSliderValue = sortingSpeedSlider.getValue();
            if(sortingSpeedSliderValue == 0) {
                JOptionPane.showMessageDialog(this, "The sorting speed is 0!");

                return;
            }

            bubbleSort.setSorted(false);
            startTimerBeforeSorting(sortingSpeedSliderValue);
            resetVariablesBeforeSorting();
            resetRunningTime();
            sortingAlgorithmRunning = BubbleSort.NAME;
            bubbleSort.moveToNextStep(data);
        }

        repaint();
    }

    private void actionPerformedInsertionSort(ActionEvent e) {
        if(!sorting) {
            insertionSort = new InsertionSort();
        }

        if(insertionSort.running()) {
            if(sortButtonClickedWhenSortingSpeedIsZero(e)) {
                JOptionPane.showMessageDialog(this, "The sorting speed is 0!");

                return;
            } else if(insertionSort.sorted()) {
                timer.stop();
                updateFinalRunningTime();
                resetVariablesAfterSorting();
                setRunningTimeLabel();
                insertionSort.setRunning(false);
            } else {
                insertionSort.moveToNextStep(data);
            }
        } else if(sortButtonClicked(e)) {
            int sortingSpeedSliderValue = sortingSpeedSlider.getValue();
            if(sortingSpeedSliderValue == 0) {
                JOptionPane.showMessageDialog(this, "The sorting speed is 0!");

                return;
            }

            insertionSort.setSorted(false);
            startTimerBeforeSorting(sortingSpeedSliderValue);
            resetVariablesBeforeSorting();
            resetRunningTime();
            sortingAlgorithmRunning = InsertionSort.NAME;
            insertionSort.moveToNextStep(data);
        }

        repaint();
    }

    private void actionPerformedMergeSort(ActionEvent e) {
        if(!sorting) {
            mergeSort = new MergeSort();
        }

        if(mergeSort.running()) {
            if(sortButtonClickedWhenSortingSpeedIsZero(e)) {
                JOptionPane.showMessageDialog(this, "The sorting speed is 0!");

                return;
            } else if(mergeSort.sorted()) {
                timer.stop();
                updateFinalRunningTime();
                resetVariablesAfterSorting();
                setRunningTimeLabel();
                mergeSort.setRunning(false);
            } else {
                mergeSort.moveToNextStep(data);
            }
        } else if(sortButtonClicked(e)) {
            int sortingSpeedSliderValue = sortingSpeedSlider.getValue();
            if(sortingSpeedSliderValue == 0) {
                JOptionPane.showMessageDialog(this, "The sorting speed is 0!");

                return;
            }

            mergeSort.setSorted(false);
            startTimerBeforeSorting(sortingSpeedSliderValue);
            resetVariablesBeforeSorting();
            resetRunningTime();
            sortingAlgorithmRunning = MergeSort.NAME;
            mergeSort.moveToNextStep(data);
        }

        repaint();
    }

    private void actionPerformedTimSort(ActionEvent e) {
        if(!sorting) {
            timSort = new TimSort();
        }

        if(timSort.running()) {
            if(sortButtonClickedWhenSortingSpeedIsZero(e)) {
                JOptionPane.showMessageDialog(this, "The sorting speed is 0!");

                return;
            } else if(timSort.sorted()) {
                timer.stop();
                updateFinalRunningTime();
                resetVariablesAfterSorting();
                setRunningTimeLabel();
                timSort.setRunning(false);
            } else {
                timSort.moveToNextStep(data);
            }
        } else if(sortButtonClicked(e)) {
            int sortingSpeedSliderValue = sortingSpeedSlider.getValue();
            if(sortingSpeedSliderValue == 0) {
                JOptionPane.showMessageDialog(this, "The sorting speed is 0!");

                return;
            }

            timSort.setSorted(false);
            startTimerBeforeSorting(sortingSpeedSliderValue);
            resetVariablesBeforeSorting();
            resetRunningTime();
            sortingAlgorithmRunning = TimSort.NAME;
            timSort.moveToNextStep(data);
        }

        repaint();
    }

    private boolean sortButtonClickedWhenSortingSpeedIsZero(ActionEvent e) {
        return sortButtonClicked(e) && sortingSpeedSlider.getValue() == 0;
    }

    private boolean sortButtonClicked(ActionEvent e) {
        return e.getActionCommand() != null && e.getActionCommand().equals("Sort");
    }

    private void startTimerBeforeSorting(int sortingSpeedSliderValue) {
        timer.setDelay(timerDelayMultiplier / sortingSpeedSliderValue);
        timer.start();
    }

    private void updateFinalRunningTime() {
        endTime = System.currentTimeMillis();
        runningTime += endTime - startTime;
    }

    private void resetVariablesBeforeSorting() {
        startTime = System.currentTimeMillis();
        sorting = true;
        sortingAlgorithmJustRan = false;
    }

    private void resetVariablesAfterSorting() {
        startTime = 0;
        endTime = 0;
        sorting = false;
        sortingAlgorithmRunning = null;
        sortingAlgorithmJustRan = true;
    }

    private void setSortingAlgorithm(String selectedAlgorithm) {
        switch(selectedAlgorithm) {
            case(BubbleSort.NAME):
                bubbleSort = new BubbleSort();
                break;
            case(InsertionSort.NAME):
                insertionSort = new InsertionSort();
            case(MergeSort.NAME):
                mergeSort = new MergeSort();
                break;
            default:
                break;
        }
    }

    private void paintComponentForSortingAlgorithm(Graphics g, int maxValue, int maxBarHeight) {
        switch (sortingAlgorithmRunning) {
            case(BubbleSort.NAME):
                paintComponentForBubbleSort(g, maxValue, maxBarHeight);
                break;
            case(InsertionSort.NAME):
                paintComponentForInsertionSort(g, maxValue, maxBarHeight);
                break;
            case(MergeSort.NAME):
                paintComponentForMergeSort(g, maxValue, maxBarHeight);
                break;
            case(TimSort.NAME):
                paintComponentForTimSort(g, maxValue, maxBarHeight);
                break;
            default:
                break;
        }
    }

    private void paintComponentForBubbleSort(Graphics g, int maxValue, int maxBarHeight) {
        int[] pointers = bubbleSort.getForLoopVariables();
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

            fillRectangle(g, i, maxValue, maxBarHeight, x, width);
            x += (width + spaceBetweenBars);
        }
    }

    private void paintComponentForInsertionSort(Graphics g, int maxValue, int maxBarHeight) {
        int keyIndex = insertionSort.getKeyIndex();
        int key = insertionSort.getKey();
        int x = 5;
        int width = (getWidth() / data.size()) - spaceBetweenBars;
        for(int i = 0; i < data.size(); i++) {
            if(i < keyIndex) {
                g.setColor(Color.ORANGE);
            } else if(i == keyIndex) {
                if(data.get(i) == key) {
                    g.setColor(Color.CYAN);
                } else {
                    g.setColor(Color.ORANGE);
                }
            } else {
                g.setColor(Color.BLACK);
            }

            fillRectangle(g, i, maxValue, maxBarHeight, x, width);
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

            fillRectangle(g, i, maxValue, maxBarHeight, x, width);
            x += (width + spaceBetweenBars);
        }
    }

    private void paintComponentForTimSort(Graphics g, int maxValue, int maxBarHeight) {
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

            fillRectangle(g, i, maxValue, maxBarHeight, x, width);
            x += (width + spaceBetweenBars);
        }
    }

    private void fillRectangle(Graphics g, int index, int maxValue, int maxBarHeight, int x, int width) {
        int height = (int)(((double)data.get(index) / maxValue) * maxBarHeight);
        g.fillRect(x, 0, width, height);
    }

    private boolean contains(int[] arr, final int key) {
        return Arrays.stream(arr).anyMatch(i -> i == key);
    }
}