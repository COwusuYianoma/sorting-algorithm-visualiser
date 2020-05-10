package calebowusuyianoma.sortalgovisualiser;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InsertionSortTest {
    private final InsertionSort insertionSort = new InsertionSort();

    private ArrayList<Integer> data;

    @Test
    public void sortThrowsExceptionWhenDataIsNull() {
        // Arrange
        data = null;
        String expected = "The data should not be null";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> insertionSort.sort(data));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void sortWithIndicesThrowsExceptionWhenDataIsNull() {
        // Arrange
        data = null;
        String expected = "The data should not be null";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                insertionSort.sort(data, 0, 3));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void sortWithIndicesThrowsExceptionWhenLeftIndexLessThanZero() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5));
        int left = -1;
        int right = 3;
        String expected = "Indices left and right should be >= 0, but left is " + left +
                " and right is " + right;

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                insertionSort.sort(data, left, right));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void sortWithIndicesThrowsExceptionWhenRightIndexLessThanZero() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5));
        int left = -1;
        int right = 3;
        String expected = "Indices left and right should be >= 0, but left is " + left +
                " and right is " + right;

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                insertionSort.sort(data, left, right));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void sortWithIndicesThrowsExceptionWhenLeftIndexEqualToDataSizeMinusOne() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5));
        int left = data.size() - 1;
        int right = 3;
        String expected = "Index left must be < data.size() - 1, but left is " + left +
                ", (data.size() - 1) equals " + (data.size() - 1) + " and right is " + right;

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                insertionSort.sort(data, left, right));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void sortWithIndicesThrowsExceptionWhenLeftIndexGreaterThanDataSizeMinusOne() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5));
        int left = data.size();
        int right = 4;
        String expected = "Index left must be < data.size() - 1, but left is " + left +
                ", (data.size() - 1) equals " + (data.size() - 1) + " and right is " + right;

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                insertionSort.sort(data, left, right));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void sortWithIndicesThrowsExceptionWhenRightIndexGreaterThanDataSizeMinusOne() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5));
        int right = 3;
        String expected = "Index right must be <= data.size() - 1, but right is " + right +
                " and (data.size() - 1) equals " + (data.size() - 1);

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                insertionSort.sort(data, 0, right));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void sortLeavesDataUnchangedWhenEmpty() {
        // Arrange
        data = new ArrayList<>();

        // Act
        insertionSort.sort(data);

        // Assert
        assertTrue(data.isEmpty());
    }

    @Test
    public void sortWithIndicesLeavesDataUnchangedWhenEmpty() {
        // Arrange
        data = new ArrayList<>();

        // Act
        insertionSort.sort(data, 0, 2);

        // Assert
        assertTrue(data.isEmpty());
    }

    @Test
    public void sortLeavesDataUnchangedWhenContainsSingleElement() {
        // Arrange
        data = new ArrayList<>(Collections.singletonList(6));
        ArrayList<Integer> expected = new ArrayList<>(Collections.singletonList(6));

        // Act
        insertionSort.sort(data);

        // Assert
        assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsNearlySortedData() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(1, 5, 3, 7, 9));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        // Act
        insertionSort.sort(data);

        // Assert
        assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsReversedData() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3, 1));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        // Act
        insertionSort.sort(data);

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
        insertionSort.sort(data);

        // Assert
        assertEquals(expected, data);
    }
}