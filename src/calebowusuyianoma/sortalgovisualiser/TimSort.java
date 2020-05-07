package calebowusuyianoma.sortalgovisualiser;

import java.util.ArrayList;

public class TimSort extends Sort {
    private static final String NAME = "timsort";

    private final int minimumRun = 32;
    private final InsertionSort insertionSort = new InsertionSort();
    private final MergeSort mergeSort = new MergeSort();

    private boolean runningInsertionSort, inMergeForLoop;
    private int numberOfElements, insertionSortLoopIndex, right, left, key, keyIndex, sortedElementIndex,
            runSize, mergeStartIndex, mergeEndIndex, previousMergeStartIndex;

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

    public void sort(ArrayList<Integer> data) {
        int n = data.size();

        for (int i = 0; i < n; i += minimumRun) {
            insertionSort.sort(data, i, Math.min(i + minimumRun - 1, n - 1));
        }

        int size = minimumRun;
        while (size < n) {
            for (int start = 0; start < n; start += (size * 2)) {
                int midpoint = start + size - 1;
                int end = Math.min(start + (size * 2) - 1, n - 1);
                System.out.println("About to merge; low: " + start + "; middle: " + midpoint + "; high: " + end);
                mergeSort.merge(data, start, midpoint, end);
                System.out.println("Just merged");
                System.out.println();
            }
            size *= 2;
        }
    }

    public static String getName() {
        return NAME;
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

    public int getPreviousMergeStartIndex() {
        return previousMergeStartIndex;
    }

    public int getMergeEndIndex() {
        return mergeEndIndex;
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

    public void setKeyIndex(int index) {
        keyIndex = index;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setSortedElementIndex(int index) {
        sortedElementIndex = index;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public void setRunningInsertionSort(boolean running) {
        runningInsertionSort = running;
    }
}
