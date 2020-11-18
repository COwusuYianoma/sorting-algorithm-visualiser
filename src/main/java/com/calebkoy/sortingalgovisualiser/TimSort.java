package com.calebkoy.sortingalgovisualiser;

import java.util.ArrayList;

public class TimSort {
    private static final String NAME = "timsort";

    private final InsertionSort insertionSort = new InsertionSort();
    private final MergeSort mergeSort = new MergeSort();

    public void sort(ArrayList<Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("The data should not be null");
        }

        int n = data.size();
        int minimumRun = 32;

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

    public static String getName() {
        return NAME;
    }
}
