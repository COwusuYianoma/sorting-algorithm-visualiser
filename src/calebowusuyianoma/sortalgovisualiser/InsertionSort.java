package calebowusuyianoma.sortalgovisualiser;

import java.util.ArrayList;

public class InsertionSort extends Sort {
    private static final String NAME = "Insertion sort";

    private int keyIndex, key, sortedElementIndex;

    public void sort(ArrayList<Integer> data) {
        sort(data, 0, data.size() - 1);
    }

    public void sort(ArrayList<Integer> data, int left, int right) {
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

    public void moveToNextStepInVisualisation(ArrayList<Integer> data) {
        if (!running() && data.size() > 1) {
            setRunning(true);
            keyIndex = 1;
            initialiseVariablesBeforeSortingData(data);
        } else if (keyLessThanSortedElement(data)) {
            shiftSortedElementToTheRight(data);
        } else {
            data.set(sortedElementIndex + 1, key);
            if (++keyIndex < data.size()) {
                initialiseVariablesBeforeSortingData(data);
            } else {
                setSorted(true);
            }
        }
    }

    private void initialiseVariablesBeforeSortingData(ArrayList<Integer> data) {
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

    public static String getName() {
        return NAME;
    }

    public int getKeyIndex() {
        return keyIndex;
    }

    public int getKey() {
        return key;
    }
}
