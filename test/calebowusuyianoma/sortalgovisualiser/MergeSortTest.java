package calebowusuyianoma.sortalgovisualiser;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MergeSortTest {
    private final MergeSort mergeSort = new MergeSort();

    private ArrayList<Integer> data;

    @Test
    public void sortThrowsExceptionWhenDataIsNull() {
        // Arrange
        data = null;
        String expected = "The data should not be null";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> mergeSort.sort(data));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void sortLeavesDataUnchangedWhenEmpty() {
        // Arrange
        data = new ArrayList<>();

        // Act
        mergeSort.sort(data);

        // Assert
        assertTrue(data.isEmpty());
    }

    @Test
    public void sortLeavesDataUnchangedWhenContainsSingleElement() {
        // Arrange
        data = new ArrayList<>(Collections.singletonList(6));
        ArrayList<Integer> expected = new ArrayList<>(Collections.singletonList(6));

        // Act
        mergeSort.sort(data);

        // Assert
        assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsNearlySortedData() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(1, 5, 3, 7, 9));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        // Act
        mergeSort.sort(data);

        // Assert
        assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsReversedData() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(6, 5, 4, 3, 2, 1));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));

        // Act
        mergeSort.sort(data);

        // Assert
        assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsRandomData() {
        // Arrange
        int size = ThreadLocalRandom.current().nextInt(1, 1001);
        data = ArrayGenerator.generateRandomPositiveIntegerArray(size, 1, 2001);
        ArrayList<Integer> expected = new ArrayList<>(data);
        Collections.sort(expected);

        // Act
        mergeSort.sort(data);

        // Assert
        assertEquals(expected, data);
    }

    @Test
    public void mergeThrowsExceptionWhenDataIsNull() {
        // Arrange
        data = null;
        String expected = "The data should not be null";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                mergeSort.merge(data, 0, 1, 2));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void mergeLeavesDataUnchangedWhenIsEmpty() {
        // Arrange
        data = new ArrayList<>();

        // Act
        mergeSort.merge(data, 0, 1, 2);

        // Assert
        assertTrue(data.isEmpty());
    }

    @Test
    public void mergeLeavesDataUnchangedWhenContainsSingleElement() {
        // Arrange
        data = new ArrayList<>(Collections.singletonList(6));
        ArrayList<Integer> expected = new ArrayList<>(Collections.singletonList(6));

        // Act
        mergeSort.merge(data, 0, 1, 2);

        // Assert
        assertEquals(expected, data);
    }

    @Test
    public void mergeThrowsExceptionWhenLowIndexLessThanZero() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7));
        int lowIndex = -1;
        String expected = "lowIndex must be >= 0 but is " + lowIndex;

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                mergeSort.merge(data, lowIndex, 1, 2));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void mergeThrowsExceptionWhenLowIndexGreaterThanMiddleIndex() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7));
        int lowIndex = 2;
        int middleIndex = 1;
        String expected = "lowIndex must be <= middleIndex, but lowIndex is " + lowIndex +
                " and middleIndex is " + middleIndex;

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                mergeSort.merge(data, lowIndex, middleIndex, 3));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void mergeThrowsExceptionWhenHighIndexGreaterThanDataSizeMinusOne() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7));
        int highIndex = 2;
        String expected = "highIndex must be <= data.size() - 1, but highIndex is " +
                highIndex + " and (data.size() - 1) equals " + (data.size() - 1);

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                mergeSort.merge(data, 0, 1, highIndex));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void mergeThrowsExceptionWhenMiddleIndexGreaterThanHighIndex() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7));
        int middleIndex = 1;
        int highIndex = 0;
        String expected = "middleIndex must be <= highIndex, but middleIndex is " + middleIndex +
                " and highIndex is " + highIndex;

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                mergeSort.merge(data, 0, middleIndex, highIndex));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void mergeCorrectlyMergesValidDataWhenIndicesAreValid() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(7, 9, 3, 5));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(3, 5, 7, 9));

        // Act
        mergeSort.merge(data, 0, 1, data.size() - 1);

        // Assert
        assertEquals(expected, data);
    }
}