package calebowusuyianoma.sortalgovisualiser;

// import java.awt.Graphics;
import java.util.ArrayList;

public abstract class Sort {
    private boolean running, sorted;

    protected boolean running() {
        return running;
    }

    protected boolean sorted() {
        return sorted;
    }

    protected void setRunning(boolean running) {
        this.running = running;
    }

    protected void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    protected abstract void moveToNextStepInVisualisation(ArrayList<Integer> data);

    //protected abstract void paintComponentForVisualisation(int panelWidth, ArrayList<Integer> data, int spaceBetweenBars,
      //                                                     Graphics g, int maxArrayValue, int maxBarHeight);
}
