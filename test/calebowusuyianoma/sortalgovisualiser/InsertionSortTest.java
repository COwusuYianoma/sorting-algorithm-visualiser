package calebowusuyianoma.sortalgovisualiser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class InsertionSortTest {
    private final InsertionSort insertionSort = new InsertionSort();
    private final TestUtilities testUtilities = new TestUtilities();

    private ArrayList<Integer> expected;

    @Test
    public void sortLeavesDataUnchangedWhenDataIsEmpty() {
        ArrayList<Integer> data = new ArrayList<>();
        insertionSort.sort(data);

        Assertions.assertTrue(data.isEmpty());
    }

    @Test
    public void sortExecutesCorrectlyWhenDataNearlySorted() {
        ArrayList<Integer> data = new ArrayList<>(Arrays.asList(1, 5, 3, 7, 9));
        insertionSort.sort(data);
        expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortExecutesCorrectlyWhenDataIsReversed() {
        ArrayList<Integer> data = new ArrayList<>(Arrays.asList(9, 7, 5, 3, 1));
        insertionSort.sort(data);
        expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortExecutesCorrectlyWhenDataContainsOneElement() {
        ArrayList<Integer> data = new ArrayList<>(Collections.singletonList(6));
        insertionSort.sort(data);
        ArrayList<Integer> expected = new ArrayList<>(Collections.singletonList(6));

        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortExecutesCorrectlyOnRandomData() {
        ArrayGenerator arrayGenerator = new ArrayGenerator();
        int size = 10, max = 100;
        ArrayList<Integer> data = arrayGenerator.generateRandomArray(size, max);

        System.out.print("Original random array: ");
        System.out.println(data.toString());

        insertionSort.sort(data);

        System.out.println();
        System.out.print("Array after having been sorted: ");
        System.out.println(data.toString());

        Assertions.assertTrue(testUtilities.isSorted(data));
    }
}