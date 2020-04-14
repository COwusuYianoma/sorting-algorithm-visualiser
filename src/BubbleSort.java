import java.util.ArrayList;

public class BubbleSort {

    public void sort(ArrayList<Integer> data) {
        if (data.isEmpty()) {
            return;
        }

        boolean sorted = false;

        while(!sorted) {
            sorted = true;
            for (int i = 0; i < data.size() - 1; i++) {
                if (data.get(i) > data.get(i + 1)) {
                    swap(data, i);
                    sorted = false;
                }
            }
        }
    }

    private void swap(ArrayList<Integer> data, int index) {
        int temp = data.get(index);
        data.set(index, data.get(index + 1));
        data.set(index + 1, temp);
    }
}
