package calebowusuyianoma.sortalgovisualiser;

import java.util.ArrayList;

public class BubbleSort extends Sort {
    public static final String NAME = "Bubblesort";

    private boolean justSwappedElements;
    private int outerForLoopVariable, innerForLoopVariable;

    public void sort(ArrayList<Integer> data) {
        for (int i = 0; i < data.size() - 1; i++) {
            for (int j = data.size() - 1; j > i; j--) {
                if (data.get(j) < data.get(j - 1)) {
                    swap(data, j);
                }
            }
        }
    }

    private void swap(ArrayList<Integer> data, int key) {
        int temp = data.get(key);
        data.set(key, data.get(key - 1));
        data.set(key - 1, temp);
    }

    public void moveToNextStep(ArrayList<Integer> data) {
        if (!running() && innerForLoopVariable == 0) {
            innerForLoopVariable = data.size() - 1;
            setRunning(true);
        } else if (innerForLoopVariable > outerForLoopVariable + 1) {
            innerForLoopVariable--;
            justSwappedElements = false;
        } else if (outerForLoopVariable < data.size() - 1) {
            outerForLoopVariable++;
            innerForLoopVariable = data.size() - 1;
            justSwappedElements = false;
        } else {
            setSorted(true);
        }
    }

    public void swap(ArrayList<Integer> data) {
        if (data.get(innerForLoopVariable) < data.get(innerForLoopVariable - 1)) {
            swap(data, innerForLoopVariable);
        }
        justSwappedElements = true;
    }

    public int[] getForLoopVariables() {
        return new int[] {outerForLoopVariable, innerForLoopVariable};
    }

    public boolean justSwappedElements() {
        return justSwappedElements;
    }
}
