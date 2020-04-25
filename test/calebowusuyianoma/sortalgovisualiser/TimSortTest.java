package calebowusuyianoma.sortalgovisualiser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class TimSortTest {
    private final TimSort timSort = new TimSort();
    private final TestUtilities testUtilities = new TestUtilities();

    private ArrayList<Integer> expected;

    @Test
    public void sortLeavesDataUnchangedWhenDataIsEmpty() {
        ArrayList<Integer> data = new ArrayList<>();
        timSort.sort(data);

        Assertions.assertTrue(data.isEmpty());
    }

    @Test
    public void sortExecutesCorrectlyWhenDataNearlySorted() {
        ArrayList<Integer> data = new ArrayList<>(Arrays.asList(1, 5, 3, 7, 9));
        timSort.sort(data);
        expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortExecutesCorrectlyWhenDataIsReversed() {
        ArrayList<Integer> data = new ArrayList<>(Arrays.asList(9, 7, 5, 3, 1));
        timSort.sort(data);
        expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortExecutesCorrectlyWhenDataContainsOneElement() {
        ArrayList<Integer> data = new ArrayList<>(Collections.singletonList(6));
        timSort.sort(data);
        ArrayList<Integer> expected = new ArrayList<>(Collections.singletonList(6));

        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortExecutesCorrectlyOnRandomData() {
        ArrayGenerator arrayGenerator = new ArrayGenerator();
        int size = arrayGenerator.generateRandomIntegerInRange(1, 1000);
        int minimumPossibleValue = 1;
        int maximumPossibleValue = 2000;
        ArrayList<Integer> data = arrayGenerator.generateRandomIntegerArray(size, minimumPossibleValue, maximumPossibleValue);

        System.out.print("Original random array of size " + data.size() + ": ");
        System.out.println(data.toString());

        timSort.sort(data);

        System.out.println();
        System.out.print("Array after having been sorted: ");
        System.out.println(data.toString());

        Assertions.assertTrue(testUtilities.isSorted(data));
    }
}