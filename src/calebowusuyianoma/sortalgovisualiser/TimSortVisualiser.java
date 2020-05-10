package calebowusuyianoma.sortalgovisualiser;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class TimSortVisualiser implements SortVisualiser {
    private final int minimumRun = 32;
    private final MergeSort mergeSort = new MergeSort();

    private boolean running, sorted, runningInsertionSort, inMergeForLoop;
    private int spaceBetweenBars, numberOfElements, insertionSortLoopIndex, right, left, key, keyIndex,
            sortedElementIndex, runSize, mergeStartIndex, mergeEndIndex, previousMergeStartIndex;

    public TimSortVisualiser() {}

    public TimSortVisualiser(int spaceBetweenBars) {
        this.spaceBetweenBars = spaceBetweenBars;
    }

    @Override
    public void moveToNextStep(ArrayList<Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("The data should contain at least one element, but it is null");
        } else if (data.size() <= 1) {
            setSorted(true);
        } else if (!running) {
            setRunning(true);
            numberOfElements = data.size();
            runSize = minimumRun;
            setInsertionSortLoopVariables(data);
            runningInsertionSort = true;
        } else if (runningInsertionSort) {
            if (!shouldInsertKey(data)) {
                shiftSortedElementToTheRight(data);
                if (shouldInsertKey(data)) {
                    data.set(sortedElementIndex + 1, key);
                    if (keyIndex == right) {
                        insertionSortLoopIndex += minimumRun;
                    }
                }
            } else if (++keyIndex <= right) {
                key = data.get(keyIndex);
                sortedElementIndex = keyIndex - 1;

                if (shouldInsertKey(data) && (keyIndex == right)) {
                    insertionSortLoopIndex += minimumRun;
                }
            } else if (!insertionSortLoopTerminationExpressionValid()) {
                setInsertionSortLoopVariables(data);
            } else {
                runningInsertionSort = false;
            }
        } else if (runSize < numberOfElements) {
            if (!inMergeForLoop) {
                mergeStartIndex = 0;
                inMergeForLoop = true;
            }

            int mergeMidpoint = mergeStartIndex + runSize - 1;
            mergeEndIndex = Math.min(mergeStartIndex + (runSize * 2) - 1, numberOfElements - 1);
            mergeSort.merge(data, mergeStartIndex, mergeMidpoint, mergeEndIndex);
            previousMergeStartIndex = mergeStartIndex;
            mergeStartIndex += (runSize * 2);

            if (mergeStartIndex >= numberOfElements) {
                inMergeForLoop = false;
                runSize *= 2;
            }
        } else {
            setSorted(true);
        }
    }

    private void setInsertionSortLoopVariables(ArrayList<Integer> data) {
        left = insertionSortLoopIndex;
        right = calculateRight();
        keyIndex = insertionSortLoopIndex + 1;
        key = data.get(keyIndex);
        sortedElementIndex = keyIndex - 1;
    }

    private int calculateRight() {
        return Math.min(insertionSortLoopIndex + minimumRun - 1, numberOfElements - 1);
    }

    private boolean shouldInsertKey(ArrayList<Integer> data) {
        return (sortedElementIndex < left) || (key >= data.get(sortedElementIndex));
    }

    private void shiftSortedElementToTheRight(ArrayList<Integer> data) {
        data.set(sortedElementIndex + 1, data.get(sortedElementIndex));
        sortedElementIndex -= 1;
    }

    private boolean insertionSortLoopTerminationExpressionValid() {
        return insertionSortLoopIndex >= numberOfElements - 1;
    }

    @Override
    public void paint(Graphics g, int maxArrayValue, int maxBarHeight,
                                          int xCoordinate, int barWidth, ArrayList<Integer> data) {

        for (int i = 0; i < data.size(); i++) {
            if (sorted) {
                g.setColor(Color.MAGENTA);
            } else if (runningInsertionSort && currentElementIsPartOfSortedInsertionSortSubArray(i)) {
                g.setColor(Color.ORANGE);
            } else if (runningInsertionSort && i == keyIndex) {
                if (data.get(i) == key) {
                    g.setColor(Color.CYAN);
                } else {
                    g.setColor(Color.ORANGE);
                }
            } else if (!runningInsertionSort && currentElementIsBeingMerged(i)) {
                g.setColor(Color.ORANGE);
            } else {
                g.setColor(Color.BLACK);
            }

            int height = (int) (((double) data.get(i) / maxArrayValue) * maxBarHeight);
            g.fillRect(xCoordinate, 0, barWidth, height);
            xCoordinate += (barWidth + spaceBetweenBars);
        }
    }

    private boolean currentElementIsPartOfSortedInsertionSortSubArray(int index) {
        return (index >= left) && (index < keyIndex);
    }

    private boolean currentElementIsBeingMerged(int index) {
        return (index >= previousMergeStartIndex) && (index <= mergeEndIndex);
    }

    public int getKey() {
        return key;
    }

    public int getKeyIndex() {
        return keyIndex;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public int getMinimumRun() {
        return minimumRun;
    }

    public int getRunSize() {
        return runSize;
    }

    public int getInsertionSortLoopIndex() {
        return insertionSortLoopIndex;
    }

    public int getSortedElementIndex() {
        return sortedElementIndex;
    }

    public boolean isRunningInsertionSort() {
        return runningInsertionSort;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isSorted() {
        return sorted;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    public void setKeyIndex(int index) {
        keyIndex = index;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setSortedElementIndex(int index) {
        sortedElementIndex = index;
    }

    public void setInsertionSortLoopIndex(int index) {
        insertionSortLoopIndex = index;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public void setRunSize(int runSize) {
        this.runSize = runSize;
    }

    public void setPreviousMergeStartIndex(int index) {
        previousMergeStartIndex = index;
    }

    public void setMergeEndIndex(int index) {
        mergeEndIndex = index;
    }

    public void setRunningInsertionSort(boolean running) {
        runningInsertionSort = running;
    }

    public void setInMergeForLoop(boolean inMergeForLoop) {
        this.inMergeForLoop = inMergeForLoop;
    }
}
