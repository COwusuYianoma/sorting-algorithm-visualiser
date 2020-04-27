package calebowusuyianoma.sortalgovisualiser;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class TimSort extends Sort {
    private static final String NAME = "timsort";

    private final int minRun = 32;
    private final InsertionSort insertionSort = new InsertionSort();
    private final MergeSort mergeSort = new MergeSort();

    private boolean insertionSorting, inMergeForLoop;
    private int numberOfElements, insertionSortLoopIndex, right,
            key, keyIndex, sortedElementIndex, left, runSize,
            mergeStartIndex, mergeEndIndex;

    public int getKey() {
        return key;
    }

    public int getKeyIndex() {
        return keyIndex;
    }

    public int getLeft() {
        return left;
    }

    public int getMergeStartIndex() {
        return mergeStartIndex;
    }

    public int getMergeEndIndex() {
        return mergeEndIndex;
    }

    public boolean isInsertionSorting() {
        return insertionSorting;
    }

    public void moveToNextStepInVisualisation(ArrayList<Integer> data) {
        if(!running()) {
            setRunning(true);
            numberOfElements = data.size();
            insertionSortLoopIndex = 0;
            setInsertionSortLoopVariables(data);
            insertionSorting = true;
        } else if(sortedElementIndex >= left && data.get(sortedElementIndex) > key) {
            data.set(sortedElementIndex + 1, data.get(sortedElementIndex));
            sortedElementIndex -= 1;
        } else if(insertionSorting){
            data.set(sortedElementIndex + 1, key);
            if(++keyIndex <= right) {
                key = data.get(keyIndex);
                sortedElementIndex = keyIndex - 1;
            } else {
                insertionSortLoopIndex += minRun;
                if(insertionSortLoopIndex < numberOfElements - 1) {
                    setInsertionSortLoopVariables(data);
                } else {
                    insertionSorting = false;
                    keyIndex = Integer.MIN_VALUE;
                    runSize = minRun;
                    mergeStartIndex = 0;
                    moveToNextMergeStep(data);
                }
            }
        } else {
            if(inMergeForLoop) {
                mergeStartIndex += (runSize * 2);
            } else {
                mergeStartIndex = 0;
            }
            moveToNextMergeStep(data);
        }
    }

    public void sort(ArrayList<Integer> data) {
        int n = data.size();

        for(int i = 0; i < n; i += minRun) {
            insertionSort.sort(data, i, Math.min(i + minRun - 1, n - 1));
        }

        int size = minRun;
        while(size < n) {
            for(int start = 0; start < n; start += (size * 2)) {
                int midpoint = start + size - 1;
                int end = Math.min(start + (size * 2) - 1, n - 1);
                mergeSort.merge(data, start, midpoint, end);
            }
            size *= 2;
        }
    }

    private void setInsertionSortLoopVariables(ArrayList<Integer> data) {
        left = insertionSortLoopIndex;
        right = calculateRight();
        keyIndex = insertionSortLoopIndex + 1;
        key = data.get(keyIndex);
        sortedElementIndex = keyIndex - 1;
    }

    private void moveToNextMergeStep(ArrayList<Integer> data) {
        if(runSize < numberOfElements) {
            if(mergeStartIndex < numberOfElements) {
                inMergeForLoop = true;
                int mergeMidpoint = mergeStartIndex + runSize - 1;
                mergeEndIndex = Math.min(mergeStartIndex + (runSize * 2) - 1, numberOfElements - 1);
                mergeSort.merge(data, mergeStartIndex, mergeMidpoint, mergeEndIndex);
            } else {
                inMergeForLoop = false;
                runSize *= 2;
            }
        } else {
            setSorted(true);
        }
    }

//    public void paintComponentForVisualisation(int panelWidth, ArrayList<Integer> data, int spaceBetweenBars,
//                                               Graphics g, int maxArrayValue, int maxBarHeight) {

        //int left = timSort.getLeft();
//        int keyIndex = timSort.getKeyIndex();
//        int key = timSort.getKey();
//        int mergeStartIndex = timSort.getMergeStartIndex();
//        int mergeEndIndex = timSort.getMergeEndIndex();
//        boolean insertionSorting = timSort.isInsertionSorting();
//        int x = 5;
//        int width = (panelWidth / data.size()) - spaceBetweenBars;
//        for (int i = 0; i < data.size(); i++) {
//            if(sorted()) {
//                g.setColor(Color.MAGENTA);
//            } else if(i >= left && i < keyIndex) {
//                g.setColor(Color.ORANGE);
//            } else if(i == keyIndex) {
//                if(data.get(i) == key) {
//                    g.setColor(Color.CYAN);
//                } else {
//                    g.setColor(Color.ORANGE);
//                }
//            } else if(!insertionSorting && i >= mergeStartIndex && i <= mergeEndIndex) {
//                g.setColor(Color.ORANGE);
//            } else {
//                g.setColor(Color.BLACK);
//            }
//
//            PaintUtilities.fillRectangle(g, i, maxArrayValue, maxBarHeight, x, width, data);
//            x += (width + spaceBetweenBars);
//        }
//    }

    private int calculateRight() {
        return Math.min(insertionSortLoopIndex + minRun - 1, numberOfElements - 1);
    }

    public static String getName() {
        return NAME;
    }
}
