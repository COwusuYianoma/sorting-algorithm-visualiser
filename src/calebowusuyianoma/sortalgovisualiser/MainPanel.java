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

import static java.util.Collections.max;

public class MainPanel extends JPanel implements ActionListener {
    private final int defaultArraySize, minimumPossibleValue, maximumPossibleValue;
    private final int timerDelayMultiplier = 5000;
    private final int spaceBetweenBars = 5;
    private final String defaultSortingAlgorithmRunningText = "No sorting algorithm is running";
    private final OptionPane optionPane;
    private final Timer timer = new Timer(100, this);
    private final JSlider sortingSpeedSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
    private final JLabel runningTimeLabel = new JLabel("Running time: ");
    private final String[] validSortingAlgorithms = new String[] {BubbleSort.getName(), InsertionSort.getName(),
            MergeSort.getName(), TimSort.getName()};

    private long startTime, endTime, runningTime;
    private boolean sorting, sortingAlgorithmJustRan;
    private String sortingAlgorithmRunning = defaultSortingAlgorithmRunningText;
    private String sortingAlgorithmSelected;
    private ArrayList<Integer> data, preSortedData;
    private SortVisualiser sortVisualiser;
    private JSpinner arraySizeSpinner;
    private JLabel arraySizeSpinnerLabel;
    private JButton generateArrayButton, sortButton, undoSortButton;
    private JComboBox<String> sortingAlgorithmsList;
    private JPanel sortingSpeedSliderPanel;

    public MainPanel(OptionPane optionPane, int defaultArraySize, int minimumPossibleValue, int maximumPossibleValue) {
        this.optionPane = optionPane;
        this.defaultArraySize = defaultArraySize;
        this.minimumPossibleValue = minimumPossibleValue;
        this.maximumPossibleValue = maximumPossibleValue;

        data = ArrayGenerator.generateRandomPositiveIntegerArray(defaultArraySize, minimumPossibleValue,
                maximumPossibleValue);
        setPreSortedData();

        createArraySizeSpinner();
        createGenerateArrayButton();
        createSortingAlgorithmsList();
        createSortingSpeedSliderPanel();
        createSortButton();
        createUndoSortButton();

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
        preSortedData = new ArrayList<>(data);
    }

    private void createArraySizeSpinner() {
        arraySizeSpinnerLabel = new JLabel("Array size: ");
        SpinnerModel spinnerModel = new SpinnerNumberModel(defaultArraySize, 2, 200, 1);
        arraySizeSpinner = new JSpinner(spinnerModel);
        arraySizeSpinnerLabel.setLabelFor(arraySizeSpinner);
    }

    private void createGenerateArrayButton() {
        generateArrayButton = new JButton("Generate random array");
        generateArrayButton.addActionListener(e -> {
            if (!sorting) {
                int arraySize = (int) arraySizeSpinner.getValue();
                data = ArrayGenerator.generateRandomPositiveIntegerArray(arraySize, minimumPossibleValue,
                        maximumPossibleValue);
                setPreSortedData();
                sortingAlgorithmJustRan = false;
                resetRunningTime();
                repaint();
            }
        });
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

    private void createSortingAlgorithmsList() {
        String defaultSortingAlgorithmsListText = "Select a sorting algorithm";
        sortingAlgorithmSelected = defaultSortingAlgorithmsListText;

        String[] sortingAlgorithmsListText = new String[] {defaultSortingAlgorithmsListText, BubbleSort.getName(),
                InsertionSort.getName(), MergeSort.getName(), TimSort.getName()};
        sortingAlgorithmsList = new JComboBox<>(sortingAlgorithmsListText);
        sortingAlgorithmsList.setSelectedIndex(0);
        sortingAlgorithmsList.addActionListener(e ->
                sortingAlgorithmSelected = (String) sortingAlgorithmsList.getSelectedItem());
    }

    private void createSortingSpeedSliderPanel() {
        sortingSpeedSliderPanel = new JPanel();
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
    }

    private void createSortButton() {
        sortButton = new JButton("Sort");
        sortButton.addActionListener(this);
    }

    private void createUndoSortButton() {
        undoSortButton = new JButton("Undo sort");
        undoSortButton.addActionListener(e -> {
            if (!sorting) {
                respondToUndoSortButtonClick();
            }
        });
    }

    private void respondToSortingSpeedSliderChange(ChangeEvent e) {
        if (!sorting) return;

        JSlider source = (JSlider) e.getSource();
        if (source.getValueIsAdjusting()) return;

        int sortingSpeed = source.getValue();
        if (sortingSpeed == 0) {
            timer.stop();
            updateRunningTime();
        } else {
            int delay = timerDelayMultiplier / sortingSpeed;
            timer.setDelay(delay);
            if (!timer.isRunning()) {
                timer.start();
                startTime = System.currentTimeMillis();
            }
        }
    }

    private void updateRunningTime() {
        long currentTime = System.currentTimeMillis();
        runningTime += currentTime - startTime;
        startTime = 0;
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
        if (e == null) {
            throw new IllegalArgumentException("ActionEvent should not be null");
        } else if (sorting) {
            continueRunningVisualisation(e);
        } else if (Arrays.asList(validSortingAlgorithms).contains(sortingAlgorithmSelected)) {
            runSortingAlgorithm();
        } else {
            optionPane.showMessageDialog(this, "Please select a sorting algorithm!");
        }
    }

    private void continueRunningVisualisation(ActionEvent e) {
        if (sortButtonClickedWhenSortingSpeedIsZero(e)) {
            JOptionPane.showMessageDialog(this, "The sorting speed is 0!");

            return;
        } else if (sortVisualiser.isSorted()) {
            timer.stop();
            calculateFinalRunningTime();
            resetVariablesAfterSorting();
            setRunningTimeLabel();
            sortVisualiser.setRunning(false);
        } else {
            sortVisualiser.moveToNextStep(data);
        }

        repaint();
    }

    private boolean sortButtonClickedWhenSortingSpeedIsZero(ActionEvent e) {
        return sortButtonClicked(e) && (sortingSpeedSlider.getValue() == 0);
    }

    private boolean sortButtonClicked(ActionEvent e) {
        return (e.getActionCommand() != null) && (e.getActionCommand().equals("Sort"));
    }

    private void calculateFinalRunningTime() {
        endTime = System.currentTimeMillis();
        runningTime += endTime - startTime;
    }

    private void resetVariablesAfterSorting() {
        startTime = 0;
        endTime = 0;
        sorting = false;
        sortingAlgorithmRunning = defaultSortingAlgorithmRunningText;
        sortingAlgorithmJustRan = true;
    }

    private void runSortingAlgorithm() {
        int sortingSpeedSliderValue = sortingSpeedSlider.getValue();
        if (sortingSpeedSliderValue == 0) {
            JOptionPane.showMessageDialog(this, "The sorting speed is 0!");

            return;
        }

        setSortVisualiser();
        startTimerBeforeSorting(sortingSpeedSliderValue);
        sorting = true;
        sortingAlgorithmRunning = sortingAlgorithmSelected;
        sortingAlgorithmJustRan = false;
        resetRunningTime();
        startTime = System.currentTimeMillis();
        sortVisualiser.moveToNextStep(data);
        repaint();
    }

    private void setSortVisualiser() {
        switch (mapSortingAlgorithmStringToEnum(sortingAlgorithmSelected)) {
            case BUBBLESORT:
                sortVisualiser = new BubbleSortVisualiser(spaceBetweenBars);
                break;
            case INSERTIONSORT:
                sortVisualiser = new InsertionSortVisualiser(spaceBetweenBars);
                break;
            case MERGESORT:
                sortVisualiser = new MergeSortVisualiser(spaceBetweenBars);
                break;
            case TIMSORT:
                sortVisualiser = new TimSortVisualiser(spaceBetweenBars);
                break;
            case UNKNOWN:
            default:
                throw new IllegalStateException("Tried to initialise an unrecognised sorting algorithm: " +
                        sortingAlgorithmSelected);
        }
    }

    private SortingAlgorithm mapSortingAlgorithmStringToEnum(String sortingAlgorithm) {
        if (sortingAlgorithm.equals(BubbleSort.getName())) return SortingAlgorithm.BUBBLESORT;
        if (sortingAlgorithm.equals(InsertionSort.getName())) return SortingAlgorithm.INSERTIONSORT;
        if (sortingAlgorithm.equals(MergeSort.getName())) return SortingAlgorithm.MERGESORT;
        if (sortingAlgorithm.equals(TimSort.getName())) return SortingAlgorithm.TIMSORT;

        return SortingAlgorithm.UNKNOWN;
    }

    private void startTimerBeforeSorting(int sortingSpeedSliderValue) {
        timer.setDelay(timerDelayMultiplier / sortingSpeedSliderValue);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (g == null) {
            throw new IllegalArgumentException("Graphics object should not be null");
        }

        super.paintComponent(g);

        int xCoordinate = 5;
        int barWidth = (getWidth() / data.size()) - spaceBetweenBars;
        int maxBarHeight = getHeight() - 150;
        int maxArrayValue = max(data);

        if (sorting) {
            sortVisualiser.paint(g, maxArrayValue, maxBarHeight, xCoordinate, barWidth, data);
        } else {
            if (sortingAlgorithmJustRan) {
                g.setColor(Color.MAGENTA);
            } else {
                g.setColor(Color.BLACK);
            }

            for (int i = 0; i < data.size(); i++) {
                fillRectangle(g, i, maxArrayValue, maxBarHeight, xCoordinate, barWidth);
                xCoordinate += (barWidth + spaceBetweenBars);
            }
        }
    }

    private void fillRectangle(Graphics g, int index, int maxValue, int maxBarHeight, int xCoordinate, int width) {
        int height = (int) (((double) data.get(index) / maxValue) * maxBarHeight);
        g.fillRect(xCoordinate, 0, width, height);
    }

    public String getSortingAlgorithmRunning() {
        return sortingAlgorithmRunning;
    }

    public boolean isSorting() {
        return sorting;
    }

    public ArrayList<Integer> getData() {
        return data;
    }

    public void setSorting(boolean sorting) {
        this.sorting = sorting;
    }

    public void setSortingAlgorithmRunning(String sortingAlgorithmRunning) {
        if (sortingAlgorithmRunning == null) {
            throw new IllegalArgumentException("sortingAlgorithmRunning should not be null");
        }

        this.sortingAlgorithmRunning = sortingAlgorithmRunning;
    }

    public void setSortingAlgorithmSelected(String sortingAlgorithmSelected) {
        if (sortingAlgorithmSelected == null) {
            throw new IllegalArgumentException("sortingAlgorithmSelected should not be null");
        }

        this.sortingAlgorithmSelected = sortingAlgorithmSelected;
    }

    public void setSortingAlgorithmJustRan(boolean justRan) {
        sortingAlgorithmJustRan = justRan;
    }

    public void setSortVisualiser(SortVisualiser sortVisualiser) {
        if (sortVisualiser == null) {
            throw new IllegalArgumentException("Sort visualiser should not be null");
        }

        this.sortVisualiser = sortVisualiser;
    }
}