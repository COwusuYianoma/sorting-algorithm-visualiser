package calebowusuyianoma.sortalgovisualiser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

class InsertionSortTest {
    private final InsertionSort insertionSort = new InsertionSort();
    private final TestUtilities testUtilities = new TestUtilities();

    private ArrayList<Integer> data;
    private ArrayList<Integer> expected;

    @Test
    public void sortLeavesDataUnchangedWhenDataIsEmpty() {
        // Arrange
        data = new ArrayList<>();

        // Act
        insertionSort.sort(data);

        // Assert
        Assertions.assertTrue(data.isEmpty());
    }

    @Test
    public void sortExecutesCorrectlyWhenDataNearlySorted() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(1, 5, 3, 7, 9));
        expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        // Act
        insertionSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortExecutesCorrectlyWhenDataIsReversed() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3, 1));
        expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        // Act
        insertionSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortExecutesCorrectlyWhenDataContainsOneElement() {
        // Arrange
        data = new ArrayList<>(Collections.singletonList(6));
        ArrayList<Integer> expected = new ArrayList<>(Collections.singletonList(6));

        // Act
        insertionSort.sort(data);

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
        insertionSort.sort(data);
        System.out.println();
        System.out.print("Array after having been sorted: ");
        System.out.println(data.toString());

        // Assert
        Assertions.assertTrue(testUtilities.isSorted(data));
    }
}