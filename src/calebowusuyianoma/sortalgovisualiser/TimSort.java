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
                mergeSort.merge(data, start, midpoint, end);
            }
            size *= 2;
        }
    }

    public void moveToNextStep(ArrayList<Integer> data) {
        if (!isRunning() && data.size() > 1) {
            setRunning(true);
            numberOfElements = data.size();
            runSize = minimumRun;
            setInsertionSortLoopVariables(data);
            runningInsertionSort = true;
        } else if (runningInsertionSort && keyLessThanSortedElement(data)) {
            shiftSortedElementToTheRight(data);
            if (!keyLessThanSortedElement(data)) {
                data.set(sortedElementIndex + 1, key);
            }
        } else if (runningInsertionSort && (++keyIndex <= right)) {
            key = data.get(keyIndex);
            sortedElementIndex = keyIndex - 1;
        } else if (runningInsertionSort() && !updatedInsertionSortLoopTerminationExpressionValid()) {
            setInsertionSortLoopVariables(data);
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

    private boolean keyLessThanSortedElement(ArrayList<Integer> data) {
        return (sortedElementIndex >= left) && (data.get(sortedElementIndex) > key);
    }

    private void shiftSortedElementToTheRight(ArrayList<Integer> data) {
        data.set(sortedElementIndex + 1, data.get(sortedElementIndex));
        sortedElementIndex -= 1;
    }

    private boolean updatedInsertionSortLoopTerminationExpressionValid() {
        insertionSortLoopIndex += minimumRun;
        if (insertionSortLoopIndex >= numberOfElements - 1) {
            runningInsertionSort = false;

            return true;
        }

        return false;
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

    public boolean runningInsertionSort() {
        return runningInsertionSort;
    }
}
