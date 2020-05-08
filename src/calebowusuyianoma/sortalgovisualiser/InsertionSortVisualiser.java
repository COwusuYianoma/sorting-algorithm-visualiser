package calebowusuyianoma.sortalgovisualiser;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class InsertionSortVisualiser extends SortVisualiser {
    private int spaceBetweenBars, keyIndex, key, sortedElementIndex;

    public InsertionSortVisualiser() {}

    public InsertionSortVisualiser(int spaceBetweenBars) {
        this.spaceBetweenBars = spaceBetweenBars;
    }

    public void moveToNextStep(ArrayList<Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("The data should contain at least one element, but it is null");
        } else if (data.size() <= 1) {
            setSorted(true);
        } else if (!isRunning()) {
            setRunning(true);
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

    public void paint(Graphics g, int maxArrayValue, int maxBarHeight,
                                                int xCoordinate, int barWidth, ArrayList<Integer> data) {

        for (int i = 0; i < data.size(); i++) {
            if (i < keyIndex) {
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

            fillRectangle(g, i, maxArrayValue, maxBarHeight, xCoordinate, barWidth, data);
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
