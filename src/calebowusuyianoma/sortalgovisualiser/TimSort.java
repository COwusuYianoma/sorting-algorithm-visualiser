package calebowusuyianoma.sortalgovisualiser;

import java.util.ArrayList;

public class TimSort {
    public static final String name = "timsort";

    private final InsertionSort insertionSort = new InsertionSort();
    private final MergeSort mergeSort = new MergeSort();

    public void sort(ArrayList<Integer> data) {
        int minRun = 32;
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
}
