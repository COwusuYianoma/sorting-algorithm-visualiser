import java.util.ArrayList;

public class BubbleSort {
    ArrayList<Integer> sort(ArrayList<Integer> data) { // Does sort() need to return data?
        if (data.isEmpty()) {
            return data;
        }

        boolean sorted = false;

        while(!sorted) {
            sorted = true;
            for (int i = 0; i < data.size() - 1; i++) {
                if (data.get(i) > data.get(i + 1)) {
                    data = swap(data, i); // Test if this assignment is necessary and so if swap() needs to return
                    sorted = false;
                }
            }
        }

        return data;
    }

    private ArrayList<Integer> swap(ArrayList<Integer> data, int index) {
        int temp = data.get(index);
        data.set(index, data.get(index + 1));
        data.set(index + 1, temp);

        return data;
    }
}
