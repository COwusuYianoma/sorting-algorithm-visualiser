package calebowusuyianoma.sortalgovisualiser;

import java.util.ArrayList;

public abstract class Sort {
    private boolean running, sorted;

    protected boolean isRunning() {
        return running;
    }

    protected boolean isSorted() {
        return sorted;
    }

    protected boolean isSorted(ArrayList<Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("The data should contain at least one element, but it is null");
        }

        for (int i = 0; i < data.size() - 1; i++) {
            if (data.get(i) > data.get(i + 1)) {
                return false;
            }
        }

        return true;
    }

    protected void setRunning(boolean running) {
        this.running = running;
    }

    protected void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    protected abstract void moveToNextStep(ArrayList<Integer> data);
}
