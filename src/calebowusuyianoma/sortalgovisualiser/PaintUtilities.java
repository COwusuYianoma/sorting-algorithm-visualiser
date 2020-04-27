package calebowusuyianoma.sortalgovisualiser;

import java.awt.Graphics;
import java.util.ArrayList;

public class PaintUtilities {
    public static void fillRectangle(Graphics g, int index, int maxValue,
                                     int maxBarHeight, int xCoordinate, int width, ArrayList<Integer> data) {

        int height = (int) (((double) data.get(index) / maxValue) * maxBarHeight);
        g.fillRect(xCoordinate, 0, width, height);
    }
}
