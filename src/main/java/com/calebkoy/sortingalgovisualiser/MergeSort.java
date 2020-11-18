package com.calebkoy.sortingalgovisualiser;

import java.util.ArrayList;

public class MergeSort {
    private static final String NAME = "Merge sort";

    public void sort(ArrayList<Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("The data should not be null");
        }

        sort(data, 0, data.size() - 1);
    }

    private void sort(ArrayList<Integer> data, int lowIndex, int highIndex) {
        if (lowIndex < highIndex) {
            int middleIndex = (int) Math.floor((double) (lowIndex + highIndex) / 2);
            sort(data, lowIndex, middleIndex);
            sort(data, middleIndex + 1, highIndex);
            merge(data, lowIndex, middleIndex, highIndex);
        }
    }

    public void merge(ArrayList<Integer> data, int lowIndex, int middleIndex, int highIndex) {
        if (data == null) {
            throw new IllegalArgumentException("The data should not be null");
        }

        // The third condition may occur when timsort calls merge()
        if ((data.isEmpty()) || (data.size() == 1) || (middleIndex > data.size() - 1)) {
            return;
        }

        validateIndices(data, lowIndex, middleIndex, highIndex);

        ArrayList<Integer> leftSubArray = new ArrayList<>(data.subList(lowIndex, middleIndex + 1));
        ArrayList<Integer> rightSubArray = new ArrayList<>(data.subList(middleIndex + 1, highIndex + 1));
        leftSubArray.add(Integer.MAX_VALUE);
        rightSubArray.add(Integer.MAX_VALUE);

        int i = 0;
        int j = 0;
        for (int k = lowIndex; k < highIndex + 1; k++) {
            if (leftSubArray.get(i) < rightSubArray.get(j)) {
                data.set(k, leftSubArray.get(i));
                i++;
            } else {
                data.set(k, rightSubArray.get(j));
                j++;
            }
        }
    }

    private void validateIndices(ArrayList<Integer> data, int lowIndex, int middleIndex, int highIndex) {
        if (lowIndex < 0) {
            throw new IllegalArgumentException("lowIndex must be >= 0 but is " + lowIndex);
        }

        if (lowIndex > middleIndex) {
            throw new IllegalArgumentException("lowIndex must be <= middleIndex, but lowIndex is " + lowIndex +
                    " and middleIndex is " + middleIndex);
        }

        if (highIndex > data.size() - 1) {
            throw new IllegalArgumentException("highIndex must be <= data.size() - 1, but highIndex is " +
                    highIndex + " and (data.size() - 1) equals " + (data.size() - 1));
        }

        if (middleIndex > highIndex) {
            throw new IllegalArgumentException("middleIndex must be <= highIndex, but middleIndex is " + middleIndex +
                    " and highIndex is " + highIndex);
        }
    }

    public static String getName() {
        return NAME;
    }
}
