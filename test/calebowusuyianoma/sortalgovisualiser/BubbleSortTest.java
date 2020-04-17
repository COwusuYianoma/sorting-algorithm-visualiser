package calebowusuyianoma.sortalgovisualiser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

class BubbleSortTest {
    private BubbleSort bubbleSort = new BubbleSort();
    private ArrayList<Integer> expected;
    private ArrayGenerator arrayGenerator;
    private TestUtilities testUtilities = new TestUtilities();

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
        expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortExecutesCorrectlyWhenDataIsReversed() {
        ArrayList<Integer> data = new ArrayList<>(Arrays.asList(9, 7, 5, 3, 1));
        bubbleSort.sort(data);
        expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortExecutesCorrectlyOnRandomData() {
        arrayGenerator = new ArrayGenerator();
        int size = 10, max = 100;
        ArrayList<Integer> data = arrayGenerator.generateRandomArray(size, max);

        System.out.print("Original random array: ");
        System.out.println(data.toString());

        bubbleSort.sort(data);

        System.out.println();
        System.out.print("Array after having been sorted by MergeSort: ");
        System.out.println(data.toString());

        Assertions.assertTrue(testUtilities.isSorted(data));
    }
}