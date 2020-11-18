package com.calebkoy.sortingalgovisualiser;

import java.awt.Graphics;
import java.util.ArrayList;

public interface SortVisualiser {
    boolean isSorted();
    void setRunning(boolean running);
    void setSorted(boolean sorted);
    void moveToNextStep(ArrayList<Integer> data);
    void paint(Graphics g, int maxArrayValue, int maxBarHeight,
                                  int xCoordinate, int barWidth, ArrayList<Integer> data);
}
