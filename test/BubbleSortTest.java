import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;

class BubbleSortTest {
    private BubbleSort bubbleSort = new BubbleSort();
    private ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

    @Test
    public void sortLeavesDataUnchangedWhenDataIsEmpty() {
        ArrayList<Integer> data = new ArrayList<>();
        bubbleSort.sort(data);

        Assertions.assertTrue(data.isEmpty());
    }

    @Test
    public void sortExecutesCorrectlyWhenDataNearlySorted() {
        ArrayList<Integer> data = new ArrayList<>(Arrays.asList(1, 5, 3, 7, 9));
        bubbleSort.sort(data);

        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortExecutesCorrectlyWhenDataIsReversed() {
        ArrayList<Integer> data = new ArrayList<>(Arrays.asList(9, 7, 5, 3, 1));
        bubbleSort.sort(data);

        Assertions.assertEquals(expected, data);
    }
}