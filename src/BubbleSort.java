import java.util.ArrayList;

public class BubbleSort {
    private MainPanel mainPanel;
    
    public BubbleSort() {}

    public BubbleSort(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public void sort(ArrayList<Integer> data) {
        if (data.isEmpty()) {
            return;
        }

        for (int i = 0; i < data.size(); i++) {
            for (int j = data.size() - 1; j > i; j--) {
//                panel.repaint();
                if (data.get(j) < data.get(j - 1)) {
                    swap(data, j);
//                    panel.repaint();
                }
            }
        }
//        panel.repaint();
    }

    private void swap(ArrayList<Integer> data, int index) {
        int temp = data.get(index);
        data.set(index, data.get(index - 1));
        data.set(index - 1, temp);
    }
}
