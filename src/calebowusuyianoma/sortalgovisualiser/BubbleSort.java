package calebowusuyianoma.sortalgovisualiser;

import java.util.ArrayList;

public class BubbleSort extends Sort {
    public static final String NAME = "Bubblesort";

    private boolean justRanSwap;
    private int i = 0, j = 0;

    public int[] getPointers() {
        return new int[] {i, j};
    }

    public boolean justRanSwap() {
        return justRanSwap;
    }

    public void moveToNextStep(ArrayList<Integer> data) {
        if (!running() && j == 0) {
            j = data.size() - 1;
            setRunning(true);
        } else if (j > i + 1) {
            j--;
            justRanSwap = false;
        } else if (i < data.size() - 1) {
            i++;
            j = data.size() - 1;
            justRanSwap = false;
        } else {
            setSorted(true);
            i = 0;
            j = 0;
        }
    }

    public void sort(ArrayList<Integer> data) {
        for (int i = 0; i < data.size() - 1; i++) {
            for (int j = data.size() - 1; j > i; j--) {
                if (data.get(j) < data.get(j - 1)) {
                    swap(data, j);
                }
            }
        }
    }

    public void swap(ArrayList<Integer> data) {
        if (data.get(j) < data.get(j - 1)) {
            swap(data, j);
        }
        justRanSwap = true;
    }

    private void swap(ArrayList<Integer> data, int index) {
        int temp = data.get(index);
        data.set(index, data.get(index - 1));
        data.set(index - 1, temp);
    }
}
