import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import static java.util.Collections.max;

public class Panel extends JPanel {
    private ArrayGenerator arrayGenerator;
    private ArrayList<Integer> data;

    public Panel() {
        arrayGenerator = new ArrayGenerator();
        int max = 100;
        int size = 2;
        data = arrayGenerator.generateRandomArray(size, max);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.BLACK);

        int verticalSpace = 50;
        int maxBarHeight = getHeight() - verticalSpace;
        int maxValue = max(data);
        int x = 5, y = 0, spaceBetweenBars = 10;
        int width = (getWidth() / data.size()) - spaceBetweenBars;
        for (int i = 0; i < data.size(); i++) {
            int height = (int)(((double)data.get(i) / maxValue) * maxBarHeight);
            g2.fillRect(x, y, width, height);
            x += (width + spaceBetweenBars);
        }
    }
}

//    private double proportionOfPanelWidthOccupiedByBars = (double)4 / 6;

//        for (int i = 0; i < data.size(); i++) {
//            System.out.println("Element at index " + i + " is: " + data.get(i));
//        }

//        double multiplier = (double)1 / data.size();
//        int barLength = 25;
//        int barWidth = (int)(multiplier * proportionOfPanelWidthOccupiedByBars * getWidth());

//            int x = (int)((i * proportionOfPanelWidthOccupiedByBars) + (getWidth() * (double)1 / 6));
//            int height = data.get(i) * barLength;
//            System.out.println("proportionOfPanelWidthOccupiedByBars: " + proportionOfPanelWidthOccupiedByBars);
//            System.out.println("x: " + x);
//            System.out.println("barWidth: " + barWidth);
//            System.out.println("height: " + height);
//            g2.fillRect(x, 0, barWidth, height);