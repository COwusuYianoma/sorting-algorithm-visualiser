package calebowusuyianoma.sortalgovisualiser;

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

    protected abstract void moveToNextStep(ArrayList<Integer> data);
}
