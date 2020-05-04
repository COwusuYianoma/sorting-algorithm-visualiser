package calebowusuyianoma.sortalgovisualiser;

import java.util.ArrayList;

public class InsertionSort extends Sort {
    private static final String NAME = "Insertion sort";

    private int keyIndex, key, sortedElementIndex;

    public void moveToNextStep(ArrayList<Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("The data should contain at least one element, but it is null");
        } else if (data.size() <= 1) {
            setSorted(true);
        } else if (!running()) {
            setRunning(true);
            keyIndex = 1;
            setVariableValuesBeforeSortingData(data);
        } else if (keyLessThanSortedElement(data)) {
            shiftSortedElementToTheRight(data);
        } else {
            data.set(sortedElementIndex + 1, key);
            if (++keyIndex < data.size()) {
                setVariableValuesBeforeSortingData(data);
            } else {
                setSorted(true);
            }
        }
    }

    private void setVariableValuesBeforeSortingData(ArrayList<Integer> data) {
        key = data.get(keyIndex);
        sortedElementIndex = keyIndex - 1;
    }

    private boolean keyLessThanSortedElement(ArrayList<Integer> data) {
        return (sortedElementIndex >= 0) && (data.get(sortedElementIndex) > key);
    }

    private void shiftSortedElementToTheRight(ArrayList<Integer> data) {
        data.set(sortedElementIndex + 1, data.get(sortedElementIndex));
        sortedElementIndex -= 1;
    }

    public void sort(ArrayList<Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("The data should contain at least one element, but it is null");
        }

        if ((data.isEmpty()) || (data.size() == 1)) {
            return;
        }

        sort(data, 0, data.size() - 1);
    }

    public void sort(ArrayList<Integer> data, int left, int right) {
        if (data == null) {
            throw new IllegalArgumentException("The data should contain at least one element, but it is null");
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

        if (left >= data.size() - 1) {
            throw new IllegalArgumentException("Index left must be < data.size() - 1, but left is " + left +
                    " and (data.size() - 1) equals " + (data.size() - 1));
        }

        if (right > data.size() - 1) {
            throw new IllegalArgumentException("Index right must be <= data.size() - 1, but right is " + right +
                    " and (data.size() - 1) equals " + (data.size() - 1));
        }
    }

    public static String getName() {
        return NAME;
    }

    public int getKeyIndex() {
        return keyIndex;
    }

    public int getKey() {
        return key;
    }

    public int getSortedElementIndex() {
        return sortedElementIndex;
    }

    public void setKeyIndex(int index) {
        keyIndex = index;
    }

    public void setKey(int value) {
        key = value;
    }

    public void setSortedElementIndex(int index) {
        sortedElementIndex = index;
    }
}
