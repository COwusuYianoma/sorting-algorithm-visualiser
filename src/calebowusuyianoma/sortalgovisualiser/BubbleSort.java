package calebowusuyianoma.sortalgovisualiser;

import java.util.ArrayList;

public class BubbleSort extends Sort {
    private static final String NAME = "Bubblesort";

    private int outerForLoopVariable, innerForLoopVariable;

    public void moveToNextStep(ArrayList<Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("The data should contain at least one element, but it is null");
        } else if (data.size() <= 1) {
            setSorted(true);
        } else if (!isRunning()) {
            setRunning(true);
            innerForLoopVariable = data.size() - 1;
        } else if (!innerForLoopTerminationExpressionValid()) {
            if (shouldSwap(data)) {
                swap(data, innerForLoopVariable);
            }
            innerForLoopVariable--;

            if (innerForLoopTerminationExpressionValid()) {
                outerForLoopVariable++;
            }
        } else if (!outerForLoopTerminationExpressionValid(data)) {
            innerForLoopVariable = data.size() - 1;
        } else {
            setSorted(true);
        }
    }

    private boolean innerForLoopTerminationExpressionValid() {
        return innerForLoopVariable <= outerForLoopVariable;
    }

    private boolean shouldSwap(ArrayList<Integer> data) {
        return data.get(innerForLoopVariable) < data.get(innerForLoopVariable - 1);
    }

    private boolean outerForLoopTerminationExpressionValid(ArrayList<Integer> data) {
        return outerForLoopVariable >= data.size() - 1;
    }

    public void sort(ArrayList<Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("The data should contain at least one element, but it is null");
        }

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

    public static String getName() {
        return NAME;
    }

    public int getOuterForLoopVariable() {
        return outerForLoopVariable;
    }

    public int getInnerForLoopVariable() {
        return innerForLoopVariable;
    }

    public int[] getForLoopVariables() {
        return new int[] {outerForLoopVariable, innerForLoopVariable};
    }

    public void setOuterForLoopVariable(int value) {
        outerForLoopVariable = value;
    }

    public void setInnerForLoopVariable(int value) {
        innerForLoopVariable = value;
    }
}
