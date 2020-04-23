package calebowusuyianoma.sortalgovisualiser;

import java.util.ArrayList;

public class InsertionSort {
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
