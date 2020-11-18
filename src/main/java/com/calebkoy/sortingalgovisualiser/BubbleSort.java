package com.calebkoy.sortingalgovisualiser;

import java.util.ArrayList;

public class BubbleSort {
    private static final String NAME = "Bubblesort";

    public void sort(ArrayList<Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("The data should not be null");
        }

        for (int i = 0; i < data.size() - 1; i++) {
            for (int j = data.size() - 1; j > i; j--) {
                if (data.get(j) < data.get(j - 1)) {
                    swap(data, j);
                }
            }
        }
    }

    public void swap(ArrayList<Integer> data, int key) {
        if (data == null) {
            throw new IllegalArgumentException("The data should not be null");
        }

        if ((key < 0) || (key > data.size() - 1)) {
            throw new IllegalArgumentException("The key should be a valid index of the array, but key is " +
                    key + " and the array has size " + data.size());
        }

        int temp = data.get(key);
        data.set(key, data.get(key - 1));
        data.set(key - 1, temp);
    }

    public static String getName() {
        return NAME;
    }
}
