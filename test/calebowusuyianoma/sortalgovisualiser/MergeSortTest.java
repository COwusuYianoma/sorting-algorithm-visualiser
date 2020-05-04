package calebowusuyianoma.sortalgovisualiser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

class MergeSortTest {
    private final MergeSort mergeSort = new MergeSort();

    private ArrayList<Integer> data;
    private ArrayList<Integer> expected;

    @Test
    public void sortLeavesDataUnchangedWhenEmpty() {
        // Arrange
        data = new ArrayList<>();

        // Act
        mergeSort.sort(data);

        // Assert
        Assertions.assertTrue(data.isEmpty());
    }

    @Test
    public void sortLeavesDataUnchangedWhenContainsSingleElement() {
        // Arrange
        data = new ArrayList<>(Collections.singletonList(6));
        expected = new ArrayList<>(Collections.singletonList(6));

        // Act
        mergeSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsNearlySortedData() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(1, 5, 3, 7, 9));
        expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        // Act
        mergeSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsReversedData() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(6, 5, 4, 3, 2, 1));
        expected = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));

        // Act
        mergeSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsRandomData() {
        // Arrange
        int size = ThreadLocalRandom.current().nextInt(1, 1001);
        data = ArrayGenerator.generateRandomPositiveIntegerArray(size, 1, 2001);

        // Act
        mergeSort.sort(data);

        // Assert
        Assertions.assertTrue(TestUtilities.isSorted(data));
    }
}