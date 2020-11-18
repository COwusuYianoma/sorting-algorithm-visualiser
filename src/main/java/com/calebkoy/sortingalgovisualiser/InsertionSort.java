package com.calebkoy.sortingalgovisualiser;

import java.util.ArrayList;

public class InsertionSort {
    private static final String NAME = "Insertion sort";

    public void sort(ArrayList<Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("The data should not be null");
        }

        if ((data.isEmpty()) || (data.size() == 1)) {
            return;
        }

        sort(data, 0, data.size() - 1);
    }

    public void sort(ArrayList<Integer> data, int left, int right) {
        if (data == null) {
            throw new IllegalArgumentException("The data should not be null");
        }

        if ((data.isEmpty()) || (data.size() == 1)) {
            return;
        }

        validateIndices(data, left, right);

        for (int i = left + 1; i < right + 1; i++) {
            int key = data.get(i);
            int j = i - 1;
            while (j >= left && data.get(j) > key) {
                data.set(j + 1, data.get(j));
                j -= 1;
            }
            data.set(j + 1, key);
        }
    }

    private void validateIndices(ArrayList<Integer> data, int left, int right) {
        if ((left < 0) || (right < 0)) {
            throw new IllegalArgumentException("Indices left and right should be >= 0, but left is " + left +
                    " and right is " + right);
        }

        if ((left >= data.size() - 1) && (left != right)) {
            throw new IllegalArgumentException("Index left must be < data.size() - 1, but left is " + left +
                    ", (data.size() - 1) equals " + (data.size() - 1) + " and right is " + right);
        }

        if (right > data.size() - 1) {
            throw new IllegalArgumentException("Index right must be <= data.size() - 1, but right is " + right +
                    " and (data.size() - 1) equals " + (data.size() - 1));
        }
    }

    public static String getName() {
        return NAME;
    }
}
