package calebowusuyianoma.sortalgovisualiser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class MergeSortTest {
    private final MergeSort mergeSort = new MergeSort();
    private final TestUtilities testUtilities = new TestUtilities();

    private ArrayList<Integer> expected;

    @Test
    public void sortExecutesCorrectlyWhenDataNearlySorted() {
        ArrayList<Integer> data = new ArrayList<>(Arrays.asList(1, 5, 3, 7, 9));
        mergeSort.sort(data);
        expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortExecutesCorrectlyWhenDataIsReversed() {
        ArrayList<Integer> data = new ArrayList<>(Arrays.asList(6, 5, 4, 3, 2, 1));
        mergeSort.sort(data);
        expected = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));

        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortExecutesCorrectlyWhenDataContainsOneElement() {
        ArrayList<Integer> data = new ArrayList<>(Collections.singletonList(6));
        mergeSort.sort(data);
        expected = new ArrayList<>(Collections.singletonList(6));

        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortExecutesCorrectlyOnRandomData() {
        ArrayGenerator arrayGenerator = new ArrayGenerator();
        int size = 10, max = 100;
        ArrayList<Integer> data = arrayGenerator.generateRandomArray(size, max);

        System.out.print("Original random array: ");
        System.out.println(data.toString());

        mergeSort.sort(data);

        System.out.println();
        System.out.print("Array after having been sorted by MergeSort: ");
        System.out.println(data.toString());

        Assertions.assertTrue(testUtilities.isSorted(data));
    }
}