package calebowusuyianoma.sortalgovisualiser;

import java.awt.Graphics;
import java.util.ArrayList;

public abstract class SortVisualiser {
    private boolean running, sorted;

    protected boolean isRunning() {
        return running;
    }

    protected boolean isSorted() {
        return sorted;
    }

    protected void setRunning(boolean running) {
        this.running = running;
    }

    protected void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    protected void fillRectangle(Graphics g, int index, int maxValue, int maxBarHeight,
                               int xCoordinate, int width, ArrayList<Integer> data) {

        if (g == null) {
            throw new IllegalArgumentException("The Graphics object should not be null");
        }

        if (data == null) {
            throw new IllegalArgumentException("The data should contain at least one element, but it is null");
        }

        int height = (int) (((double) data.get(index) / maxValue) * maxBarHeight);
        g.fillRect(xCoordinate, 0, width, height);
    }

    protected abstract void moveToNextStep(ArrayList<Integer> data);

    protected abstract void paint(Graphics g, int maxArrayValue, int maxBarHeight,
                                  int xCoordinate, int barWidth, ArrayList<Integer> data);
}
