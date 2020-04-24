package calebowusuyianoma.sortalgovisualiser;

import java.util.ArrayList;

public class TimSort {
    private final InsertionSort insertionSort = new InsertionSort();
    private final MergeSort mergeSort = new MergeSort();

    public void sort(ArrayList<Integer> data) {
        int minRun = 32;
        int n = data.size();

        int insertionSortCount = 0;
        System.out.println();
        for(int i = 0; i < n; i += minRun) {
            insertionSortCount++;
            insertionSort.sort(data, i, Math.min(i + minRun - 1, n - 1));
            System.out.println("Run number " + insertionSortCount + " of insertion sort just finished");
            System.out.println("left: " + i + "; right: " + Math.min(i + minRun - 1, n - 1));
            System.out.print("Current state of data: ");
            System.out.println(data.toString());
            System.out.println();
        }

        int size = minRun;
        int mergeCount = 0; // TODO: remove this when you've finished debugging timsort
        while(size < n) {
            for(int start = 0; start < n; start += (size * 2)) {
                mergeCount++;
                int midpoint = start + size - 1;
                int end = Math.min(start + (size * 2) - 1, n - 1);
                mergeSort.merge(data, start, midpoint, end);

                System.out.println("Merge number " + mergeCount + " just finished");
                System.out.println("start: " + start + "; midpoint: " + midpoint + "; end: " + end);
                System.out.print("Current state of data: ");
                System.out.println(data.toString());
                System.out.println();
            }
            System.out.print("Just increased value of size from " + size);
            size *= 2;
            System.out.print(" to " + size);
            System.out.println();
        }
    }
}
