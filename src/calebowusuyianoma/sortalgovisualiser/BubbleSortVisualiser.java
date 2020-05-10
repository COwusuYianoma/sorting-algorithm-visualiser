package calebowusuyianoma.sortalgovisualiser;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class BubbleSortVisualiser implements SortVisualiser {
    private final BubbleSort bubbleSort = new BubbleSort();

    private int spaceBetweenBars, outerForLoopVariable, innerForLoopVariable;
    private boolean running, sorted;

    public BubbleSortVisualiser() {}

    public BubbleSortVisualiser(int spaceBetweenBars) {
        this.spaceBetweenBars = spaceBetweenBars;
    }

    @Override
    public void moveToNextStep(ArrayList<Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("The data should not be null");
        } else if (data.size() <= 1) {
            setSorted(true);
        } else if (!running) {
            setRunning(true);
            innerForLoopVariable = data.size() - 1;
        } else if (!innerForLoopTerminationExpressionValid()) {
            if (shouldSwap(data)) {
                bubbleSort.swap(data, innerForLoopVariable);
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

    @Override
    public void paint(Graphics g, int maxArrayValue, int maxBarHeight,
                                             int xCoordinate, int barWidth, ArrayList<Integer> data) {

        for (int i = 0; i < data.size(); i++) {
            if (sorted) {
                g.setColor(Color.MAGENTA);
            } else if (running && indexEqualsForLoopVariableValue(i)) {
                g.setColor(Color.CYAN);
            } else {
                g.setColor(Color.BLACK);
            }

            int height = (int) (((double) data.get(i) / maxArrayValue) * maxBarHeight);
            g.fillRect(xCoordinate, 0, barWidth, height);
            xCoordinate += (barWidth + spaceBetweenBars);
        }
    }

    private boolean indexEqualsForLoopVariableValue(int index) {
        return ((index == outerForLoopVariable) || (index == innerForLoopVariable));
    }

    public int getInnerForLoopVariable() {
        return innerForLoopVariable;
    }

    public int getOuterForLoopVariable() {
        return outerForLoopVariable;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isSorted() {
        return sorted;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    public void setInnerForLoopVariable(int value) {
        innerForLoopVariable = value;
    }

    public void setOuterForLoopVariable(int value) {
        outerForLoopVariable = value;
    }
}
