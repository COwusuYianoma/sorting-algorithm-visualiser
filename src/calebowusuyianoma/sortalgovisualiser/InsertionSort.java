package calebowusuyianoma.sortalgovisualiser;

import java.util.ArrayList;

public class InsertionSort extends Sort {
    public static final String name = "Insertion sort";

    private int keyIndex;

    public int getKeyIndex() {
        return keyIndex;
    }

    public void adjustPointers(ArrayList<Integer> data) {
        if(!running()) {
            setRunning(true);
            keyIndex = 1;
        } else {
            int key = data.get(keyIndex);
            int sortedElementIndex = keyIndex - 1;
            while(sortedElementIndex >= 0 && data.get(sortedElementIndex) > key) {
                data.set(sortedElementIndex + 1, data.get(sortedElementIndex));
                sortedElementIndex -= 1;
            }
            data.set(sortedElementIndex + 1, key);
            keyIndex += 1;
            if(keyIndex == data.size()) {
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
