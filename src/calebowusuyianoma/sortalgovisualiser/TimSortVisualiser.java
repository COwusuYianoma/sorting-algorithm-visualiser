package calebowusuyianoma.sortalgovisualiser;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class TimSortVisualiser extends SortVisualiser {
    // TODO: decide if the visualisers should keep the data as state; my gut says no

    private final int minimumRun = 32;
    private final MergeSort mergeSort = new MergeSort();

    private boolean runningInsertionSort, inMergeForLoop;
    private int spaceBetweenBars, numberOfElements, insertionSortLoopIndex, right, left, key, keyIndex,
            sortedElementIndex, runSize, mergeStartIndex, mergeEndIndex, previousMergeStartIndex;

    public TimSortVisualiser() {}

    public TimSortVisualiser(int spaceBetweenBars) {
        this.spaceBetweenBars = spaceBetweenBars;
    }

    public void moveToNextStep(ArrayList<Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("The data should contain at least one element, but it is null");
        } else if (data.size() <= 1) {
            setSorted(true);
        } else if (!isRunning()) {
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

    public void paint(Graphics g, int maxArrayValue, int maxBarHeight,
                                          int xCoordinate, int barWidth, ArrayList<Integer> data) {

        for (int i = 0; i < data.size(); i++) {
            if (isSorted()) {
                g.setColor(Color.MAGENTA);
            } else if (runningInsertionSort && currentElementIsPartOfSortedInsertionSortSubArray(i, left, keyIndex)) {
                g.setColor(Color.ORANGE);
            } else if (runningInsertionSort && i == keyIndex) {
                if (data.get(i) == key) {
                    g.setColor(Color.CYAN);
                } else {
                    g.setColor(Color.ORANGE);
                }
            } else if (!runningInsertionSort && currentElementIsBeingMerged(i, previousMergeStartIndex, mergeEndIndex)) {
                g.setColor(Color.ORANGE);
            } else {
                g.setColor(Color.BLACK);
            }

            fillRectangle(g, i, maxArrayValue, maxBarHeight, xCoordinate, barWidth, data);
            xCoordinate += (barWidth + spaceBetweenBars);
        }
    }

    // TODO: remove unnecessary args
    private boolean currentElementIsPartOfSortedInsertionSortSubArray(int index, int left, int keyIndex) {
        return (index >= left) && (index < keyIndex);
    }

    // TODO: remove unnecessary args
    private boolean currentElementIsBeingMerged(int index, int mergeStartIndex, int mergeEndIndex) {

        return (index >= mergeStartIndex) && (index <= mergeEndIndex);
    }
}
