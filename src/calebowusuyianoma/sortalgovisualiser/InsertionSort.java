package calebowusuyianoma.sortalgovisualiser;

import java.util.ArrayList;

public class InsertionSort extends Sort {
    public static final String name = "Insertion sort";

    private int keyIndex, key, sortedElementIndex;

    public int getKeyIndex() {
        return keyIndex;
    }

    public int getKey() {
        return key;
    }

    public void adjustPointers(ArrayList<Integer> data) {
        if(!running()) {
            setRunning(true);
            keyIndex = 1;
            key = data.get(keyIndex);
            sortedElementIndex = keyIndex - 1;
        } else if(sortedElementIndex >= 0 && data.get(sortedElementIndex) > key) {
            data.set(sortedElementIndex + 1, data.get(sortedElementIndex));
            sortedElementIndex -= 1;
        } else {
            data.set(sortedElementIndex + 1, key);
            if(++keyIndex < data.size()) {
                key = data.get(keyIndex);
                sortedElementIndex = keyIndex - 1;
            } else {
                setSorted(true);
            }
        }
    }

    public void sort(ArrayList<Integer> data) {
        for(int i = 1; i < data.size(); i++) {
            int key = data.get(i);
            int j = i - 1;
            while(j >= 0 && data.get(j) > key) {
                data.set(j + 1, data.get(j));
                j -= 1;
            }
            data.set(j + 1, key);
        }
    }
}
