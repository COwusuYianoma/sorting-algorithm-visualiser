package calebowusuyianoma.sortalgovisualiser;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class BubbleSortVisualiser extends SortVisualiser {
    private int spaceBetweenBars, outerForLoopVariable, innerForLoopVariable;

    public BubbleSortVisualiser() {}

    public BubbleSortVisualiser(int spaceBetweenBars) {
        this.spaceBetweenBars = spaceBetweenBars;
    }

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

    // TODO: consider calling BubbleSort.swap() instead
    private void swap(ArrayList<Integer> data, int key) {
        int temp = data.get(key);
        data.set(key, data.get(key - 1));
        data.set(key - 1, temp);
    }

    private boolean outerForLoopTerminationExpressionValid(ArrayList<Integer> data) {
        return outerForLoopVariable >= data.size() - 1;
    }

    public void paint(Graphics g, int maxArrayValue, int maxBarHeight,
                                             int xCoordinate, int barWidth, ArrayList<Integer> data) {

        for (int i = 0; i < data.size(); i++) {
            if (isSorted()) { // TODO: determine if this condition is needed. If it's not, remove it and the corresponding tests.
                g.setColor(Color.MAGENTA);
            } else if (isRunning() && ((i == outerForLoopVariable) || (i == innerForLoopVariable))) { // TODO: refactor this conditional
                g.setColor(Color.CYAN);
            } else {
                g.setColor(Color.BLACK);
            }

            fillRectangle(g, i, maxArrayValue, maxBarHeight, xCoordinate, barWidth, data);
            xCoordinate += (barWidth + spaceBetweenBars);
        }
    }

    public int getInnerForLoopVariable() {
        return innerForLoopVariable;
    }

    public int getOuterForLoopVariable() {
        return outerForLoopVariable;
    }

    public void setInnerForLoopVariable(int value) {
        innerForLoopVariable = value;
    }

    public void setOuterForLoopVariable(int value) {
        outerForLoopVariable = value;
    }
}
