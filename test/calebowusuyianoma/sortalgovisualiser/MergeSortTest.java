package calebowusuyianoma.sortalgovisualiser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

class MergeSortTest {
    private final MergeSort mergeSort = new MergeSort();
    private final TestUtilities testUtilities = new TestUtilities();

    private ArrayList<Integer> data;
    private ArrayList<Integer> expected;

    @Test
    public void sortExecutesCorrectlyWhenDataNearlySorted() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(1, 5, 3, 7, 9));
        expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        // Act
        mergeSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortExecutesCorrectlyWhenDataIsReversed() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(6, 5, 4, 3, 2, 1));
        expected = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));

        // Act
        mergeSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortExecutesCorrectlyWhenDataContainsOneElement() {
        // Arrange
        data = new ArrayList<>(Collections.singletonList(6));
        expected = new ArrayList<>(Collections.singletonList(6));

        // Act
        mergeSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortExecutesCorrectlyOnRandomData() {
        // Arrange
        int size = ThreadLocalRandom.current().nextInt(1, 1001);
        data = ArrayGenerator.generateRandomIntegerArray(size, 1, 2001);
        System.out.print("Original random array of size " + data.size() + ": ");
        System.out.println(data.toString());

        // Act
        mergeSort.sort(data);
        System.out.println();
        System.out.print("Array after having been sorted: ");
        System.out.println(data.toString());

        // Assert
        Assertions.assertTrue(testUtilities.isSorted(data));
    }
}