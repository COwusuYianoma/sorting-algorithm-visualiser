package calebowusuyianoma.sortalgovisualiser;

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
}
