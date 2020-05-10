package calebowusuyianoma.sortalgovisualiser;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class InsertionSortVisualiser implements SortVisualiser {
    private int spaceBetweenBars, keyIndex, key, sortedElementIndex;
    private boolean sorted, running;

    public InsertionSortVisualiser() {}

    public InsertionSortVisualiser(int spaceBetweenBars) {
        this.spaceBetweenBars = spaceBetweenBars;
    }

    @Override
    public void moveToNextStep(ArrayList<Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("The data should not be null");
        } else if (data.size() <= 1) {
            sorted = true;
        } else if (!running) {
            running = true;
            keyIndex = 1;
            setVariableValuesBeforeSortingData(data);
        } else if (keyLessThanSortedElement(data)) {
            shiftSortedElementToTheRight(data);
        } else {
            data.set(sortedElementIndex + 1, key);
            if (++keyIndex < data.size()) {
                setVariableValuesBeforeSortingData(data);
            } else {
                setSorted(true);
            }
        }
    }

    private void setVariableValuesBeforeSortingData(ArrayList<Integer> data) {
        key = data.get(keyIndex);
        sortedElementIndex = keyIndex - 1;
    }

    private boolean keyLessThanSortedElement(ArrayList<Integer> data) {
        return (sortedElementIndex >= 0) && (data.get(sortedElementIndex) > key);
    }

    private void shiftSortedElementToTheRight(ArrayList<Integer> data) {
        data.set(sortedElementIndex + 1, data.get(sortedElementIndex));
        sortedElementIndex -= 1;
    }

    @Override
    public void paint(Graphics g, int maxArrayValue, int maxBarHeight,
                                                int xCoordinate, int barWidth, ArrayList<Integer> data) {

        for (int i = 0; i < data.size(); i++) {
            if (sorted) {
                g.setColor(Color.MAGENTA);
            } else if (i < keyIndex) {
                g.setColor(Color.ORANGE);
            } else if (i == keyIndex) {
                if (data.get(i) == key) {
                    g.setColor(Color.CYAN);
                } else {
                    g.setColor(Color.ORANGE);
                }
            } else {
                g.setColor(Color.BLACK);
            }

            int height = (int) (((double) data.get(i) / maxArrayValue) * maxBarHeight);
            g.fillRect(xCoordinate, 0, barWidth, height);
            xCoordinate += (barWidth + spaceBetweenBars);
        }
    }

    public int getKeyIndex() {
        return keyIndex;
    }

    public int getKey() {
        return key;
    }

    public int getSortedElementIndex() {
        return sortedElementIndex;
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

    public void setKeyIndex(int index) {
        keyIndex = index;
    }

    public void setKey(int value) {
        key = value;
    }

    public void setSortedElementIndex(int index) {
        sortedElementIndex = index;
    }
}
